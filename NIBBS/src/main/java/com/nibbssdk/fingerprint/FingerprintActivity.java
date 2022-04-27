package com.nibbssdk.fingerprint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nibbssdk.database.Databasehelper;
import com.nibbssdk.signature.BeginsignatureActivity;
import com.nibbssdk.Constant;
import com.nibbssdk.R;

public class FingerprintActivity extends AppCompatActivity {

    Databasehelper databasehelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint);
        databasehelper = new Databasehelper(getApplicationContext());
        ImageView leftindex = findViewById(R.id.leftindex);
        SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        String stringfingerprintname = sharedpreferences.getString("fingerprintname","");
        String stringfingerprintimage = sharedpreferences.getString("fingerprintimage","");
        String[] fingerprintimage = stringfingerprintimage.split(";");
        String[] fingerprintname = stringfingerprintname.split(";");
        Bitmap b = Constant.loadImageFromStorage(fingerprintimage[0], fingerprintname[0]);
        leftindex.setImageBitmap(b);
        ImageView leftmiddle = findViewById(R.id.leftmiddle);
        Bitmap b1 = Constant.loadImageFromStorage(fingerprintimage[1], fingerprintname[1]);
        leftmiddle.setImageBitmap(b1);
        ImageView leftring = findViewById(R.id.leftring);
        Bitmap b2 = Constant.loadImageFromStorage(fingerprintimage[2], fingerprintname[2]);
        leftring.setImageBitmap(b2);
        ImageView leftlittle = findViewById(R.id.leftlittle);
        Bitmap b3 = Constant.loadImageFromStorage(fingerprintimage[3], fingerprintname[3]);
        leftlittle.setImageBitmap(b3);
        ImageView rightindex = findViewById(R.id.rightindex);
        Bitmap b4 = Constant.loadImageFromStorage(fingerprintimage[4], fingerprintname[4]);
        rightindex.setImageBitmap(b4);
        ImageView rightmiddle = findViewById(R.id.rightmiddle);
        Bitmap b5 = Constant.loadImageFromStorage(fingerprintimage[5], fingerprintname[5]);
        rightmiddle.setImageBitmap(b5);
        ImageView rightring = findViewById(R.id.rightring);
        Bitmap b6 = Constant.loadImageFromStorage(fingerprintimage[6], fingerprintname[6]);
        rightring.setImageBitmap(b6);
        ImageView rightlittle = findViewById(R.id.rightlittle);
        Bitmap b7 = Constant.loadImageFromStorage(fingerprintimage[7], fingerprintname[7]);
        rightlittle.setImageBitmap(b7);
        ImageView leftthumb = findViewById(R.id.leftthumb);
        Bitmap b8 = Constant.loadImageFromStorage(fingerprintimage[8], fingerprintname[8]);
        leftthumb.setImageBitmap(b8);
        ImageView rightthumb = findViewById(R.id.rightthumb);
        Bitmap b9 = Constant.loadImageFromStorage(fingerprintimage[9], fingerprintname[9]);
        rightthumb.setImageBitmap(b9);

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
                boolean addone = databasehelper.insertfingerprint(stringfingerprintimage,stringfingerprintname);
                if (addone) {
                    Intent in = new Intent(getApplicationContext(), BeginsignatureActivity.class);
                    startActivity(in);
                }else {
                    Constant.inserterrortoast(getApplicationContext());
                }
//                finish();
            }
        });
    }
}