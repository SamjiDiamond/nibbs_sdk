package com.nibbssdk.signature;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.nibbssdk.Constant;
import com.nibbssdk.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SigningpageActivity extends AppCompatActivity {

    ImageView savebutton;
    SignaturePad mSignaturePad;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signingpage);
        sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        Constant.table_id = sharedpreferences.getString("table_id","");
        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {

            @Override
            public void onStartSigning() {
                //Event triggered when the pad is touched
            }

            @Override
            public void onSigned() {
                //Event triggered when the pad is signed
                savebutton.setEnabled(true);
//                resetbutton.setEnabled(true);
            }

            @Override
            public void onClear() {
                //Event triggered when the pad is cleared
                savebutton.setEnabled(false);
//                resetbutton.setEnabled(false);
            }
        });
        savebutton = findViewById(R.id.captureImage);
        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "signature" + timeStamp + "_";
                Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
                String path = Constant.saveToInternalStorage(signatureBitmap,getApplicationContext(),imageFileName);
                if (path !=null) {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("signatureimagename",imageFileName);
                    editor.putString("signatureimage",path);
                    editor.apply();
                    Intent in = new Intent(getApplicationContext(), SignatureActivity.class);
                    in.putExtra("data", path);
                    startActivity(in);
//                Intent intent = new Intent();
//                intent.putExtra("sign", path);
//                setResult(RESULT_OK, intent);
                finish();
                }
            }
        });
//        resetbutton = findViewById(R.id.reset);
//        resetbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mSignaturePad.clear();
//            }
//        });

    }

}