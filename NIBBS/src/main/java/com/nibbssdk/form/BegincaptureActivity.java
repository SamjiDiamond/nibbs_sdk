package com.nibbssdk.form;

import static com.nibbssdk.Nibss.databasehelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nibbssdk.Nibss;
import com.nibbssdk.R;
import com.nibbssdk.database.Databasehelper;
import com.nibbssdk.services.Util;

public class BegincaptureActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begincapture);
        new Databasehelper(getApplicationContext());
        sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        databasehelper(getApplicationContext()).getcurrenttable("odejinmi", "tolulope","Abraham");
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if (sharedpreferences.getBoolean("myfirsttime",true)){
//        editor.clear();
//        editor.apply();
        editor.putBoolean("myfirsttime", false);
        editor.commit();
        }else {
            Util.scheduleJob(getApplicationContext(), Long.parseLong("1"));
        }
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

        Toast.makeText(getApplicationContext(), "Submitted: "+ Nibss.totalsubmitted(getApplicationContext())
                +" Uploaded: "+Nibss.totaluploded(getApplicationContext())+" Sync: "+Nibss.totalsync(getApplicationContext())+" Validate: "+Nibss.totalvalidate(getApplicationContext()), Toast.LENGTH_LONG).show();
// Toast.makeText(getApplicationContext(), "Submitted: "+Nibss.totalsubmittedlist(), Toast.LENGTH_LONG).show();
//        Log.d("TAG", "onCreate: Submitted: "+Nibss.totalsubmittedlist());


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