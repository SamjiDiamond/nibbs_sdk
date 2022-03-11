package com.nibbssdk.face;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nibbssdk.fingerprint.BeginfingerprintActivity;
import com.nibbssdk.Constant;
import com.nibbssdk.R;

public class FacecaptureActivity extends AppCompatActivity {

    public static final int CAMERA_REQUEST_CODE = 1450;
    public String mCurrentPhotoPath;
    ImageView image;
    boolean correctimage = false;


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
        setContentView(R.layout.activity_facecapture);
        image = findViewById(R.id.imageview);
        Bitmap b = Constant.loadImageFromStorage(Constant.faceimage, Constant.faceimagename);
//        Matrix matrix = new Matrix();
//        image.setScaleType(ImageView.ScaleType.MATRIX);   //required
//        matrix.postRotate((float) -45, 0, 0);
//        image.setImageMatrix(matrix);
        image.setImageBitmap(b);
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
//                if (correctimage) {
                    Intent in = new Intent(getApplicationContext(), BeginfingerprintActivity.class);
                    startActivity(in);
//                finish();
//                }else {
//                    Toast.makeText(FacecaptureActivity.this, "No face detected, Recapture face", Toast.LENGTH_LONG).show();
//                }
            }
        });

        Button captureimage = findViewById(R.id.captureimage);
        captureimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.HeadBounds.clear();
                Constant.LeftEarPosition.clear();
                Constant.HeadRotationy.clear();
                Constant.HeadRotationz.clear();
                Constant.RightEyeOpen.clear();
                Constant.UserSmiling.clear();
                Intent in = new Intent(getApplicationContext(), CameraActivity.class);
                in.putExtra("data", "LOOK INTO THE CAMERA.");
                startActivity(in);
//                Intent in = new Intent(getApplicationContext(), CameraActivity.class);
//                startActivityForResult(in, 1);
//                startActivity(in);
//                takepicture();
            }
        });

        if (!Constant.HeadBounds.isEmpty()){
//        Log.d("TAG", "width1: "+Constant.HeadBounds.get(0).width());
//        Log.d("TAG", "width2 "+Constant.HeadBounds.get(1).width());
//        Log.d("TAG", "width3: "+Constant.HeadBounds.get(2).width());
//        Log.d("TAG", "height1: "+Constant.HeadBounds.get(0).height());
//        Log.d("TAG", "height2: "+Constant.HeadBounds.get(1).height());
//        Log.d("TAG", "height3: "+Constant.HeadBounds.get(2).height());
//        Log.d("TAG", "HeadBounds: "+Constant.HeadBounds);
//        Log.d("TAG", "LeftEarPosition: "+Constant.LeftEarPosition);
//        Log.d("TAG", "LeftEarPositionx: "+Constant.LeftEarPosition.get(0).x);
//        Log.d("TAG", "LeftEarPositiony: "+Constant.LeftEarPosition.get(0).y);
//        Log.d("TAG", "LeftEarPositionx: "+Constant.LeftEarPosition.get(1).x);
//        Log.d("TAG", "LeftEarPositiony: "+Constant.LeftEarPosition.get(1).y);
//        Log.d("TAG", "LeftEarPositionx: "+Constant.LeftEarPosition.get(2).x);
//        Log.d("TAG", "LeftEarPositiony: "+Constant.LeftEarPosition.get(2).y);
//        Log.d("TAG", "HeadRotationy: "+Constant.HeadRotationy);
//        Log.d("TAG", "HeadRotationy: "+Constant.HeadRotationy.get(0));
//        Log.d("TAG", "HeadRotationy: "+Constant.HeadRotationy.get(1));
//        Log.d("TAG", "HeadRotationy: "+Constant.HeadRotationy.get(2));
//        Log.d("TAG", "HeadRotationz: "+Constant.HeadRotationz);
//        Log.d("TAG", "HeadRotationz: "+Constant.HeadRotationz.get(0));
//        Log.d("TAG", "HeadRotationz: "+Constant.HeadRotationz.get(1));
//        Log.d("TAG", "HeadRotationz: "+Constant.HeadRotationz.get(2));
//        Log.d("TAG", "RightEyeOpen: "+Constant.RightEyeOpen);
//        Log.d("TAG", "UserSmiling: "+Constant.UserSmiling);

            if (Constant.UserSmiling.get(1) > 0.5){
                if (Constant.UserSmiling.get(0) < 0.1){
                    Log.d("TAG", "user smile to response to liveness");
                    if (Constant.RightEyeOpen.get(0) > 0.5){
                        if (Constant.RightEyeOpen.get(2) < 0.1){
                            Log.d("TAG", "user close eyes to response to liveness");
                            correctimage = true;
                        }
                    }
                }
            }
            if (Constant.HeadBounds.get(0).width() != Constant.HeadBounds.get(1).width()){
                if (Constant.HeadBounds.get(0).width() != Constant.HeadBounds.get(2).width()) {
                    if (Constant.HeadBounds.get(1).width() != Constant.HeadBounds.get(2).width()) {

                    }
                }
            }
        }else  {
            Toast.makeText(FacecaptureActivity.this, "No face detected", Toast.LENGTH_LONG).show();
//            Intent in = new Intent(getApplicationContext(), CameraActivity.class);
//            in.putExtra("data", "LOOK INTO THE CAMERA.");
//            startActivity(in);
        }
    }
}