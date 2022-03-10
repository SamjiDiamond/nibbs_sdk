package com.nibbs.fingerprint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nibbs.signature.BeginsignatureActivity;
import com.nibbs.Constant;
import com.nibbs.R;

public class FingerprintActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint);
        ImageView leftindex = findViewById(R.id.leftindex);
        String[] fingerprintimage = Constant.fingerprintimage.split(";");
        String[] fingerprintname = Constant.fingerprintname.split(";");
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
                Intent in = new Intent(getApplicationContext(), BeginsignatureActivity.class);
                startActivity(in);
//                finish();
            }
        });
    }
}