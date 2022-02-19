package com.nibbs;

import static android.widget.Toast.LENGTH_LONG;

import static androidx.core.content.FileProvider.getUriForFile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.hardware.camera2.params.Face;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FacecaptureActivity extends AppCompatActivity {

    public static final int CAMERA_REQUEST_CODE = 1450;
    public String mCurrentPhotoPath;
    ImageView image;
    public String encodedImage="";
    private ProgressDialog pDialog;
//    private com.google.android.gms.vision.face.FaceDetector detector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facecapture);
        image = findViewById(R.id.imageview);
        LinearLayout backlayout = findViewById(R.id.backlayout);
        backlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        LinearLayout nextlayout = findViewById(R.id.nextlayout);
        nextlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), FingerprintActivity.class);
                startActivity(in);
//                finish();
            }
        });

        CardView captureimage = findViewById(R.id.captureimage);
        captureimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               takepicture();
            }
        });
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents m
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private Bitmap getBitmapFromPathForImageView(String mCurrentPhotoPath, ImageView image) {
        // Get the dimensions of the View
        int targetW = image.getWidth();
        int targetH = image.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        Bitmap rotatedBitmap = bitmap;

        // rotate bitmap if needed
        try {
            ExifInterface ei = new ExifInterface(mCurrentPhotoPath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmap, 270);
                    break;
            }
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return rotatedBitmap;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private void takepicture(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    CAMERA_REQUEST_CODE);
        }
        else if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            Log.d("TAG", "takepicture: "+photoFile);
            // Continue only if the File was successfully created
            if (photoFile != null) {
//                Uri photoURI = getUriForFile(FacecaptureActivity.this, "com.nibbs", photoFile);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }

        }else {
            Log.d("TAG", "takepicture: permission needed");
//            FancyToast.makeText(FacecaptureActivity.this, String.format("Required permission not granted"),
//                    LENGTH_LONG, FancyToast.ERROR, true).show();
        }
    }
//    private void processCameraPicture(final String mCurrentPhotoPath1) {
//
//        // run image related code after the view was laid out
////        // to have all dimensions calculated
////        Intent intent = getIntent();
////        final String mCurrentPhotoPath1 = intent.getStringExtra("mCurrentPhotoPath");
////        // run image related code after the view was laid out
//        // to have all dimensions calculated
//        image.post(new Runnable() {
//            @Override
//            public void run() {
//                if (mCurrentPhotoPath1 != null) {
//
//                    Bitmap bitmap = getBitmapFromPathForImageView(mCurrentPhotoPath1, image);
//                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
//                    SparseArray<Face> faces = detector.detect(frame);
//
//                    Log.d("TAG", "Faces detected: " + String.valueOf(faces.size()));
///*
//                    Paint paint = new Paint();
//                    paint.setColor(Color.GREEN);
//                    paint.setTextSize((int) (16 * scale));
//                    paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
//                    paint.setStyle(Paint.Style.STROKE);
//                    paint.setStrokeWidth(5);
//*/
//                    Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
//                    Canvas canvas = new Canvas(mutableBitmap);
//
//                    for (int i = 0; i < faces.size(); ++i) {
//                        Face face = faces.valueAt(i);
///*
//                        canvas.drawRect(
//                                face.getPosition().x,
//                                face.getPosition().y,
//                                face.getPosition().x + face.getWidth(),
//                                face.getPosition().y + face.getHeight(), paint);
//                        canvas.drawText("Face " + (i + 1), face.getPosition().x + face.getWidth(), face.getPosition().y + face.getHeight(), paint);
//                        for (Landmark landmark : face.getLandmarks()) {
//                            int cx = (int) (landmark.getPosition().x);
//                            int cy = (int) (landmark.getPosition().y);
//                            canvas.drawCircle(cx, cy, 10, paint);
//                        }
//*/
//                        Path path = new Path();
//                        path.moveTo(face.getPosition().x, face.getPosition().y);
//                        path.lineTo(face.getPosition().x + face.getWidth(), face.getPosition().y);
//                        path.lineTo(face.getPosition().x + face.getWidth(), face.getPosition().y + face.getHeight());
//                        path.lineTo(face.getPosition().x, face.getPosition().y + face.getHeight());
//                        path.close();
//
//                    }
//                    if (faces.size() == 0) {
//                        final AlertDialog.Builder alertDialogBuilder= new AlertDialog.Builder(FacecaptureActivity.this);
//                        alertDialogBuilder.setTitle("Selfie  Error");
//                        alertDialogBuilder.setMessage("No face in the picture you took, Kindly retake the picture");
//                        alertDialogBuilder.setPositiveButton("OK",
//                                new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface arg0, int arg1) {
//
//                                    }
//                                });
//                        AlertDialog alertDialog = alertDialogBuilder.create();
//                        alertDialog.show();
//                        Toast toast = Toast.makeText(FacecaptureActivity.this, "Scan Failed: Found nothing to scan", Toast.LENGTH_SHORT);
//                        toast.show();
//                    } else {
//                        if (faces.size()!=1){
//                            final AlertDialog.Builder alertDialogBuilder= new AlertDialog.Builder(FacecaptureActivity.this);
//                            alertDialogBuilder.setTitle("Selfie Error");
//                            alertDialogBuilder.setMessage("Number of face should not be more than one");
//                            alertDialogBuilder.setPositiveButton("OK",
//                                    new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface arg0, int arg1) {
//
//
//                                        }
//                                    });
//                            AlertDialog alertDialog = alertDialogBuilder.create();
//                            alertDialog.show();
//                        }else {
//
//                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
//                            encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
//                            image.setImageBitmap(mutableBitmap);
////
////                            sharedpreferencesloginDetails = getSharedPreferences(myJson.LoginPREFERENCES, Context.MODE_PRIVATE);
////                            SharedPreferences.Editor editor = sharedpreferencesloginDetails.edit();
////                            editor.putString(myJson.image, mCurrentPhotoPath);
////                            editor.commit();
//
//                            //new FaceLivenessyncTask().execute(mCurrentPhotoPath);
//                            // FacecaptureActivity();
//
//                            Toast toast = Toast.makeText(FacecaptureActivity.this, " " + "Faces Detected: " + String.valueOf(faces.size()), Toast.LENGTH_SHORT);
//                            toast.show();
//                        }
//                    }
//
//                }
//            }
//        });
//    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 4;
            if (mCurrentPhotoPath.contains("jpg")) {
                Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
                image.setImageBitmap(bitmap);
//                processCameraPicture(mCurrentPhotoPath);
            }else {
                Toast.makeText(getApplicationContext(),"Path is empty",
                        LENGTH_LONG).show();

            }
        }else {
            Toast.makeText(getApplicationContext(),"request code issue",
                    LENGTH_LONG).show();

        }
    }
  //checking internet
    public boolean isInternetConnected() {
        // At activity startup we manually check the internet status and change
        // the text status
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;

    }

}