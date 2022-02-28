package com.nibbs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class SigningpageActivity extends AppCompatActivity {

    ImageView savebutton;
    SignaturePad mSignaturePad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signingpage);
        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {

            @Override
            public void onStartSigning() {
                //Event triggered when the pad is touched
            }

            @Override
            public void onSigned() {
                //Event triggered when the pad is signed
                savebutton.setEnabled(true);
//                resetbutton.setEnabled(true);
            }

            @Override
            public void onClear() {
                //Event triggered when the pad is cleared
                savebutton.setEnabled(false);
//                resetbutton.setEnabled(false);
            }
        });
        savebutton = findViewById(R.id.captureImage);
        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
                String path = Constant.saveToInternalStorage(signatureBitmap,getApplicationContext());
                if (path !=null) {
                    Intent in = new Intent(getApplicationContext(), SignatureActivity.class);
                    Constant.signatureimage = path;
                    in.putExtra("data", path);
                    startActivity(in);
//                Intent intent = new Intent();
//                intent.putExtra("sign", path);
//                setResult(RESULT_OK, intent);
                finish();
                }
            }
        });
//        resetbutton = findViewById(R.id.reset);
//        resetbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mSignaturePad.clear();
//            }
//        });

    }

}