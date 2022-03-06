package com.nibbs;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nibbs.services.Util;

public class BegincaptureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begincapture);
        Util.scheduleJob(getApplicationContext());
        Button button = findViewById(R.id.formdata);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), DataformActivity.class);
//                Intent in = new Intent(getApplicationContext(), BeginfaceActivity.class);
                startActivity(in);
//                finish();
            }
        });

        Toast.makeText(getApplicationContext(), "Submitted: "+Nibss.totalsubmitted()
                +" Uploaded: "+Nibss.totaluploded()+" Sync: "+Nibss.totalsync()+" Validate: "+Nibss.totalvalidate(), Toast.LENGTH_LONG).show();
        Log.d("TAG", "onCreate: Submitted: "+Nibss.totalsubmitted()
                +" Uploaded: "+Nibss.totaluploded()+" Sync: "+Nibss.totalsync()+" Validate: "+Nibss.totalvalidate());
 Toast.makeText(getApplicationContext(), "Submitted: "+Nibss.totalsubmittedlist(), Toast.LENGTH_LONG).show();
        Log.d("TAG", "onCreate: Submitted: "+Nibss.totalsubmittedlist());

//        LinearLayout nextlayout = findViewById(R.id.nextlayout);
//        nextlayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent in = new Intent(getApplicationContext(), FacecaptureActivity.class);
//                startActivity(in);
////                finish();
//            }
//        });
    }
}