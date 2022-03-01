package com.nibbs;

import static android.widget.Toast.LENGTH_LONG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ImageCapture;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;


import java.io.File;
import java.io.IOException;
import java.security.Policy;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.security.auth.callback.Callback;


import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.ErrorCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Surface;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.google.mlkit.vision.face.FaceLandmark;

public class CameraActivity extends AppCompatActivity implements Callback,
        OnClickListener, SurfaceHolder.Callback,Camera.FaceDetectionListener {

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private ImageView flipCamera;
    private ImageView flashCameraButton;
    private ImageView captureImage,backbutton;
    private int cameraId;
    private boolean flashmode = false;
    private int rotation;
    Bundle extras;

    Bitmap loadedImage = null;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 0);
        ORIENTATIONS.append(Surface.ROTATION_90, 90);
        ORIENTATIONS.append(Surface.ROTATION_180, 180);
        ORIENTATIONS.append(Surface.ROTATION_270, 270);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        // camera surface view created
        cameraId = CameraInfo.CAMERA_FACING_BACK;
        flipCamera =  findViewById(R.id.flipCamera);
        backbutton =  findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        TextView cameratext = findViewById(R.id.cameratext);
        flashCameraButton = findViewById(R.id.flash);
        captureImage = findViewById(R.id.captureImage);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        flipCamera.setOnClickListener(this);
        captureImage.setOnClickListener(this);
        flashCameraButton.setOnClickListener(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Camera.getNumberOfCameras() > 1) {
            flipCamera.setVisibility(View.VISIBLE);
        }
        if (!getBaseContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FLASH)) {
            flashCameraButton.setVisibility(View.GONE);
        }
        extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("key");
            cameratext.setText(extras.getString("data"));
            //The key argument here must match that used in the other activity
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if (!openCamera(CameraInfo.CAMERA_FACING_BACK)) {
            alertCameraDialog();
        }

    }

    private boolean openCamera(int id) {
        boolean result = false;
        cameraId = id;
        releaseCamera();
        try {
            camera = Camera.open(cameraId);
            camera.setFaceDetectionListener(new CameraActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (camera != null) {
            try {
                setUpCamera(camera);
                camera.setErrorCallback(new ErrorCallback() {

                    @Override
                    public void onError(int error, Camera camera) {

                    }
                });
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
                startFaceDetection(); // start face detection feature
                result = true;
            } catch (IOException e) {
                e.printStackTrace();
                result = false;
                releaseCamera();
            }
        }
        return result;
    }

    private void setUpCamera(Camera c) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degree = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 0;
                break;
            case Surface.ROTATION_90:
                degree = 90;
                break;
            case Surface.ROTATION_180:
                degree = 180;
                break;
            case Surface.ROTATION_270:
                degree = 270;
                break;

            default:
                break;
        }

        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            // frontFacing
            rotation = (info.orientation + degree) % 330;
            rotation = (360 - rotation) % 360;
        } else {
            // Back-facing
            rotation = (info.orientation - degree + 360) % 360;
        }
        c.setDisplayOrientation(rotation);
        Parameters params = c.getParameters();

        showFlashButton(params);

        List<String> focusModes = params.getSupportedFlashModes();
        if (focusModes != null) {
            if (focusModes
                    .contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                params.setFlashMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }
        }

        params.setRotation(rotation);
    }

    private void showFlashButton(Parameters params) {
        boolean showFlash = (getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FLASH) && params.getFlashMode() != null)
                && params.getSupportedFlashModes() != null
                && params.getSupportedFocusModes().size() > 1;

        flashCameraButton.setVisibility(showFlash ? View.VISIBLE
                : View.INVISIBLE);

    }

    private void releaseCamera() {
        try {
            if (camera != null) {
                camera.setPreviewCallback(null);
                camera.setErrorCallback(null);
                camera.stopPreview();
                camera.release();
                camera = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error", e.toString());
            camera = null;
        }
    }


    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        if (holder.getSurface() == null){
            // preview surface does not exist
            Log.d("TAG", "holder.getSurface() == null");
            return;
        }

        try {
            camera.stopPreview();

        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
            Log.d("TAG", "Error stopping camera preview: " + e.getMessage());
        }

        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();

            startFaceDetection(); // re-start face detection feature

        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
            Log.d("TAG", "Error starting camera preview: " + e.getMessage());
        }
    }


    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.flash) {
            flashOnButton();
        } else if (id == R.id.flipCamera) {
            flipCamera();
        } else if (id == R.id.captureImage) {
            takeImage();
        }
    }

    private void takeImage() {
        camera.takePicture(null, null, new PictureCallback() {

            private File imageFile;

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                try {
                    // convert byte array into bitmap

                    Bitmap rotatedBitmap = null;
                    loadedImage = BitmapFactory.decodeByteArray(data, 0,
                            data.length);

                    // rotate Image
                    Matrix rotateMatrix = new Matrix();
                    rotateMatrix.postRotate(rotation);
                    rotatedBitmap = Bitmap.createBitmap(loadedImage, 0, 0,
                            loadedImage.getWidth(), loadedImage.getHeight(),
                            rotateMatrix, false);
                    InputImage image = InputImage.fromBitmap(loadedImage, 0);
                    detectFaces(image);
//                    if (extras.getString("data").equals("LOOK INTO THE CAMERA.")){
//                        String path = Constant.saveToInternalStorage(loadedImage,getApplicationContext(),"photo");
//                        Constant.faceimage = path;
//                        Intent in = new Intent(getApplicationContext(), CameraActivity.class);
//                        in.putExtra("data", "SMILE TO THE CAMERA.");
//                        startActivity(in);
//                    }else if (extras.getString("data").equals("SMILE TO THE CAMERA.")){
//                        Intent in = new Intent(getApplicationContext(), CameraActivity.class);
//                        in.putExtra("data", "CLOSE YOUR EYES.");
//                        startActivity(in);
//                    }else {
//                        Intent in = new Intent(getApplicationContext(), FacecaptureActivity.class);
////                        in.putExtra("data", "CLOSE YOUR EYES.");
//                        startActivity(in);
//                    }



//                    InputImage image = InputImage.fromBitmap(loadedImage, 0);
//                    detectFaces(image);
//                    String path = Constant.saveToInternalStorage(loadedImage, getApplicationContext());
//                    if (path !=null) {
//                        Log.d("TAG", "onPictureTaken: "+ path);
//                        Intent intent = new Intent();
//                        intent.putExtra("picture", path);
//                        setResult(RESULT_OK, intent);
//                        finish();
//                    }
//                    String state = Environment.getExternalStorageState();
//                    File folder = null;
//                    if (state.contains(Environment.MEDIA_MOUNTED)) {
//                        folder = new File(Environment
//                                .getExternalStorageDirectory() + "/Demo");
//                    } else {
//                        folder = new File(Environment
//                                .getExternalStorageDirectory() + "/Demo");
//                    }
//
//                    boolean success = true;
//                    if (!folder.exists()) {
//                        success = folder.mkdirs();
//                    }
//                    if (success) {
//                        java.util.Date date = new java.util.Date();
//                        imageFile = new File(folder.getAbsolutePath()
//                                + File.separator
//                                + new Timestamp(date.getTime()).toString()
//                                + "Image.jpg");
//
//                        imageFile.createNewFile();
//                    } else {
//                        Toast.makeText(getBaseContext(), "Image Not saved",
//                                Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                    ByteArrayOutputStream ostream = new ByteArrayOutputStream();
//
//                    // save image into gallery
//                    rotatedBitmap.compress(CompressFormat.JPEG, 100, ostream);
//
//                    FileOutputStream fout = new FileOutputStream(imageFile);
//                    fout.write(ostream.toByteArray());
//                    fout.close();
//                    ContentValues values = new ContentValues();
//
//                    values.put(Images.Media.DATE_TAKEN,
//                            System.currentTimeMillis());
//                    values.put(Images.Media.MIME_TYPE, "image/jpeg");
//                    values.put(MediaStore.MediaColumns.DATA,
//                            imageFile.getAbsolutePath());
//
//                    CameraActivity.this.getContentResolver().insert(
//                            Images.Media.EXTERNAL_CONTENT_URI, values);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void flipCamera() {
        int id = (cameraId == CameraInfo.CAMERA_FACING_BACK ? CameraInfo.CAMERA_FACING_FRONT
                : CameraInfo.CAMERA_FACING_BACK);
        if (!openCamera(id)) {
            alertCameraDialog();
        }
    }

    private void alertCameraDialog() {
        Builder dialog = createAlert(CameraActivity.this,
                "Camera info", "error to open camera");
        dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });

        dialog.show();
    }

    private Builder createAlert(Context context, String title, String message) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(
                new ContextThemeWrapper(context,
                        android.R.style.Theme_Holo_Light_Dialog));
//        dialog.setIcon(R.drawable.ic_launcher);
        if (title != null)
            dialog.setTitle(title);
        else
            dialog.setTitle("Information");
        dialog.setMessage(message);
        dialog.setCancelable(false);
        return dialog;

    }

    private void flashOnButton() {
        if (camera != null) {
            try {
                Parameters param = camera.getParameters();
                param.setFlashMode(!flashmode ? Parameters.FLASH_MODE_TORCH
                        : Parameters.FLASH_MODE_OFF);
                camera.setParameters(param);
                flashmode = !flashmode;
            } catch (Exception e) {
                // TODO: handle exception
            }

        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    private void detectFaces(InputImage image) {
            // [START set_detector_options]
            FaceDetectorOptions options =
                    new FaceDetectorOptions.Builder()
                            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                            .setMinFaceSize(0.15f)
                            .enableTracking()
                            .build();
            // [END set_detector_options]

            // [START get_detector]
            FaceDetector detector = FaceDetection.getClient(options);
            // Or use the default options:
            // FaceDetector detector = FaceDetection.getClient();
            // [END get_detector]

            // [START run_detector]
            Task<List<Face>> result =
                    detector.process(image)
                            .addOnSuccessListener(
                                    new OnSuccessListener<List<Face>>() {
                                        @Override
                                        public void onSuccess(List<Face> faces) {
                                            // Task completed successfully
                                            // [START_EXCLUDE]
                                            // [START get_face_info]

                                            for (Face face : faces) {
                                                Rect bounds = face.getBoundingBox();
                                                float rotY = face.getHeadEulerAngleY();  // Head is rotated to the right rotY degrees
                                                float rotZ = face.getHeadEulerAngleZ();  // Head is tilted sideways rotZ degrees
                                                Constant.eyecoordinateText += "Head Bounds: " + bounds + "\n";
                                                Constant.HeadBounds.add(bounds);
                                                Constant.eyecoordinateText += "Head Rotation(Y-Axis): " + rotY + "\n";
                                                Constant.HeadRotationy.add(rotY);
                                                Constant.eyecoordinateText += "Head Rotation(Z-Axis): " + rotZ + "\n";
                                                Constant.HeadRotationz.add(rotZ);

                                                // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
                                                // nose available):
                                                FaceLandmark leftEar = face.getLandmark(FaceLandmark.LEFT_EAR);
                                                if (leftEar != null) {
                                                    PointF leftEarPos = leftEar.getPosition();
                                                    Constant.LeftEarPosition.add(leftEarPos);
                                                    Constant.eyecoordinateText += "Left Ear Position: " + leftEarPos.toString() + "\n";
                                                }

                                                // If classification was enabled:
                                                if (face.getSmilingProbability() != null) {
                                                    float smileProb = face.getSmilingProbability();
                                                    Constant.eyecoordinateText += "User Smiling: " + String.valueOf(smileProb) + "\n";
                                                    Constant.UserSmiling.add(smileProb);
                                                }
                                                if (face.getRightEyeOpenProbability() != null) {
                                                    float rightEyeOpenProb = face.getRightEyeOpenProbability();
                                                    Log.d("rightEyeOpenProb", String.valueOf(rightEyeOpenProb));
                                                    Constant.eyecoordinateText += "Right Eye Open: " + String.valueOf(rightEyeOpenProb) + "\n";
                                                    Constant.RightEyeOpen.add(rightEyeOpenProb);
                                                }

                                                // If face tracking was enabled:
                                                if (face.getTrackingId() != null) {
                                                    int id = face.getTrackingId();
                                                    Log.d("id", String.valueOf(id));
                                                }
                                            }
                                            if (extras.getString("data").equals("LOOK INTO THE CAMERA.")) {
                                                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                                                String imageFileName = "photo" + timeStamp + "_";
                                                Constant.faceimagename = imageFileName;
                                                String path = Constant.saveToInternalStorage(loadedImage, getApplicationContext(), imageFileName);
                                                Constant.faceimage = path;
                                                Intent in = new Intent(getApplicationContext(), CameraActivity.class);
                                                in.putExtra("data", "SMILE TO THE CAMERA.");
                                                startActivity(in);
                                            } else if (extras.getString("data").equals("SMILE TO THE CAMERA.")) {
                                                Intent in = new Intent(getApplicationContext(), CameraActivity.class);
                                                in.putExtra("data", "CLOSE YOUR EYES.");
                                                startActivity(in);
                                            } else {
                                                releaseCamera();
                                                Intent in = new Intent(getApplicationContext(), FacecaptureActivity.class);
//                        in.putExtra("data", "CLOSE YOUR EYES.");
                                                startActivity(in);
                                                finish();
                                            }
//                                        eyecoordinate.setText(Constant.eyecoordinateText);
                                            // [END get_face_info]
                                            // [END_EXCLUDE]
                                        }
                                    })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Task failed with an exception
                                            // ...
                                            Log.e("face error", e.toString());
                                        }
                                    });
            // [END run_detector]
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onFaceDetection(Camera.Face[] faces, Camera camera) {
        if (faces.length > 0){
            Log.d("FaceDetection", "face detected: "+ faces.length +
                    " Face 1 Location X: " + faces[0].rect.centerX() +
                    "Y: " + faces[0].rect.centerY() );
        }
    }

    public void startFaceDetection(){
        // Try starting Face Detection
        Camera.Parameters params = camera.getParameters();

        // start face detection only *after* preview has started
        if (params.getMaxNumDetectedFaces() > 0){
            // camera supports face detection, so can start it:
            camera.startFaceDetection();
        }
    }
}
