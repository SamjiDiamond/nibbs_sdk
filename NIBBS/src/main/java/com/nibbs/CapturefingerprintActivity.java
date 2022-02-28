package com.nibbs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class CapturefingerprintActivity extends AppCompatActivity {


    private ImageView captureImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capturefingerprint);

        captureImage = findViewById(R.id.captureImage);
        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), FingerprintActivity.class);
                startActivity(in);
            }
        });
    }
}