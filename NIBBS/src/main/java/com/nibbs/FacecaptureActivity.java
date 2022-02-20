package com.nibbs;

import static android.widget.Toast.LENGTH_LONG;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.FaceDetector;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
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
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private void takepicture(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 4;
            if (mCurrentPhotoPath.contains("jpg")) {
//                Bitmap thumbmail = (Bitmap) data.getExtras().get("data");
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