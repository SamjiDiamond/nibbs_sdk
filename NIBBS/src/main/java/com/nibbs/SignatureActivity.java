package com.nibbs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SignatureActivity extends AppCompatActivity {
ImageView signature;
Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);
        signature = findViewById(R.id.signaturepreview);
        extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("data");
            Bitmap b = Constant.loadImageFromStorage(value);
            signature.setImageBitmap(b);
            //The key argument here must match that used in the other activity
        }
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
                    final AlertDialog.Builder alertDialogBuilder= new AlertDialog.Builder(SignatureActivity.this);
                                alertDialogBuilder.setTitle("BVN Enrolment Ticket");
                                alertDialogBuilder.setMessage("Ticket ID: 55932019112584436 \n Date Captured: Friday, January 3, 2020 \n Agen: Eyowo Sample Agen");
                                alertDialogBuilder.setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface arg0, int arg1) {


        }
    });
    AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();

            }
        });
        Button signpage = findViewById(R.id.captureimage);
        signpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), SigningpageActivity.class);
                startActivityForResult(in, 1);
//                startActivity(in);
//                finish();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                Log.d("TAG", "onActivityResult: "+data.getStringExtra("sign"));
                Bitmap b = Constant.loadImageFromStorage(data.getStringExtra("sign"));
                signature.setImageBitmap(b);
            }
        }
    }


}