package com.nibsssdkexample;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

//import com.nibbssdk.fingerprint.SimpleScanActivity;
import com.nibbssdk.fingerprint.BeginfingerprintActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView ade = findViewById(R.id.hello);
        ade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//            Intent in = new Intent(getApplicationContext(), SimpleScanActivity.class);
            Intent in = new Intent(getApplicationContext(), BeginfingerprintActivity.class);
//            Intent in = new Intent(getApplicationContext(), BegincaptureActivity.class);
                startActivity(in);
//                finish();
            }
        });
    }

}