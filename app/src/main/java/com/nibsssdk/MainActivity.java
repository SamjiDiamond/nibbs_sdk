package com.nibsssdk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.nibbssdk.fingerprint.BeginfingerprintActivity;
import com.nibbssdk.form.BegincaptureActivity;
import com.nibbssdk.signature.BeginsignatureActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView ade = findViewById(R.id.hello);
        ade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//            Intent in = new Intent(getApplicationContext(), BeginsignatureActivity.class);
            Intent in = new Intent(getApplicationContext(), BegincaptureActivity.class);
                startActivity(in);
//                finish();
            }
        });
    }

}