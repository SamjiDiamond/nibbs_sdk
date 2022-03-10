package com.nibbs.signature;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.nibbs.Constant;
import com.nibbs.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SigningpageActivity extends AppCompatActivity {

    ImageView savebutton;
    SignaturePad mSignaturePad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signingpage);
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
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "fingerprint" + timeStamp + "_";
                Constant.signatureimagename = imageFileName;
                Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
                String path = Constant.saveToInternalStorage(signatureBitmap,getApplicationContext(),imageFileName);
                if (path !=null) {
                    Intent in = new Intent(getApplicationContext(), SignatureActivity.class);
                    Constant.signatureimage = path;
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