package com.nibbs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FingerprintActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint);
        ImageView leftindex = findViewById(R.id.leftindex);
        Bitmap b = Constant.loadImageFromStorage(Constant.fingerprintimage.get(0), Constant.fingerprintname.get(0));
        leftindex.setImageBitmap(b);
        ImageView leftmiddle = findViewById(R.id.leftmiddle);
        Bitmap b1 = Constant.loadImageFromStorage(Constant.fingerprintimage.get(1), Constant.fingerprintname.get(1));
        leftmiddle.setImageBitmap(b1);
        ImageView leftring = findViewById(R.id.leftring);
        Bitmap b2 = Constant.loadImageFromStorage(Constant.fingerprintimage.get(2), Constant.fingerprintname.get(2));
        leftring.setImageBitmap(b2);
        ImageView leftlittle = findViewById(R.id.leftlittle);
        Bitmap b3 = Constant.loadImageFromStorage(Constant.fingerprintimage.get(3), Constant.fingerprintname.get(3));
        leftlittle.setImageBitmap(b3);
        ImageView rightindex = findViewById(R.id.rightindex);
        Bitmap b4 = Constant.loadImageFromStorage(Constant.fingerprintimage.get(4), Constant.fingerprintname.get(4));
        rightindex.setImageBitmap(b4);
        ImageView rightmiddle = findViewById(R.id.rightmiddle);
        Bitmap b5 = Constant.loadImageFromStorage(Constant.fingerprintimage.get(5), Constant.fingerprintname.get(5));
        rightmiddle.setImageBitmap(b5);
        ImageView rightring = findViewById(R.id.rightring);
        Bitmap b6 = Constant.loadImageFromStorage(Constant.fingerprintimage.get(6), Constant.fingerprintname.get(6));
        rightring.setImageBitmap(b6);
        ImageView rightlittle = findViewById(R.id.rightlittle);
        Bitmap b7 = Constant.loadImageFromStorage(Constant.fingerprintimage.get(7), Constant.fingerprintname.get(7));
        rightlittle.setImageBitmap(b7);
        ImageView leftthumb = findViewById(R.id.leftthumb);
        Bitmap b8 = Constant.loadImageFromStorage(Constant.fingerprintimage.get(8), Constant.fingerprintname.get(8));
        leftthumb.setImageBitmap(b8);
        ImageView rightthumb = findViewById(R.id.rightthumb);
        Bitmap b9 = Constant.loadImageFromStorage(Constant.fingerprintimage.get(9), Constant.fingerprintname.get(9));
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