package com.nibbs.fingerprint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nibbs.R;

public class BeginfingerprintActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beginfingerprint);
        Button button = findViewById(R.id.formdata);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), CapturefingerprintActivity.class);
                in.putExtra("data", "Left Index");
                startActivity(in);
//                finish();
            }
        });
    }
}