package com.nibsssdkexample;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

//import com.nibbssdk.fingerprint.SimpleScanActivity;
import com.nibbssdk.Nibss;
import com.nibbssdk.PreviewActivity;
import com.nibbssdk.face.BeginfaceActivity;
import com.nibbssdk.fingerprint.BeginfingerprintActivity;
import com.nibbssdk.form.BegincaptureActivity;
import com.nibbssdk.signature.BeginsignatureActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Nibss.startuploading(getApplicationContext());
        TextView ade = findViewById(R.id.hello);
        ade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//            Intent in = new Intent(getApplicationContext(), PreviewActivity.class);
            Intent in = new Intent(getApplicationContext(), BegincaptureActivity.class);
                startActivity(in);
//                finish();
            }
        });
    }

}