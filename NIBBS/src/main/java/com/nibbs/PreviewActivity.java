package com.nibbs;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

public class PreviewActivity extends AppCompatActivity {

    Databasehelper databasehelper;
    EditText title, firstname, surname, middle,
            dateico, gender, marital,soo,lga;
    EditText title1, firstname1, surname1, middle1, dateico1, gender1, marital1;
    EditText title2, firstname2, surname2, middle2, dateico2;
    ImageView backbutton,signature;

    Datamodel datamodel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        databasehelper = new Databasehelper(PreviewActivity.this);
        backbutton = findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        dateico = findViewById(R.id.dateofbirthEditText);
        dateico.setText(Constant.dob);
        title = findViewById(R.id.titleEditText);
        title.setText(Constant.title);
        surname = findViewById(R.id.surnameEditText);
        surname.setText(Constant.surname);
        firstname = findViewById(R.id.firstnameEditText);
        firstname.setText(Constant.firstname);
        middle = findViewById(R.id.middleEditText);
        middle.setText(Constant.middlename);
        gender = findViewById(R.id.genderEditText);
        gender.setText(Constant.gender);
        marital = findViewById(R.id.maritalstatusEditText);
        marital.setText(Constant.maritalstatus);
        soo = findViewById(R.id.stateoforiginEditText);
        soo.setText(Constant.soo);
        lga = findViewById(R.id.lgaEditText);
        lga.setText(Constant.lga);
        dateico1 = findViewById(R.id.dateofbirthEditText1);
        dateico1.setText(Constant.residentialaddress);
        title1 = findViewById(R.id.titleEditText1);
        title1.setText(Constant.stateofresidence);
        surname1 = findViewById(R.id.surnameEditText1);
        surname1.setText(Constant.lgaofresidence);
        firstname1 = findViewById(R.id.firstnameEditText1);
        firstname1.setText(Constant.landmarks);
        middle1 = findViewById(R.id.middleEditText1);
        middle1.setText(Constant.email);
        gender1 = findViewById(R.id.genderEditText1);
        gender1.setText(Constant.phonenumber);
        marital1 = findViewById(R.id.maritalstatusEditText1);
        marital1.setText(Constant.phonenumber2);
        dateico2 = findViewById(R.id.dateofbirthEditText2);
        dateico2.setText(Constant.accountlevel);
        title2 = findViewById(R.id.titleEditText2);
        title2.setText(Constant.nin);
        surname2 = findViewById(R.id.surnameEditText2);
        surname2.setText(Constant.selectbank);
        firstname2 = findViewById(R.id.firstnameEditText2);
        firstname2.setText(Constant.stateofcapture);
        middle2 = findViewById(R.id.middleEditText2);
        middle2.setText(Constant.lgaofcapture);
        signature = findViewById(R.id.signaturepreview);
        Bitmap b = Constant.loadImageFromStorage(Constant.signatureimage);
        signature.setImageBitmap(b);
        Button signpage = findViewById(R.id.submit);
        signpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    final AlertDialog.Builder alertDialogBuilder= new AlertDialog.Builder(PreviewActivity.this);
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
//        datamodel = new Datamodel(1,title.getText().toString(),surname.getText().toString(),
//                firstname.getText().toString(),middle.getText().toString(), dateico.getText().toString(),
//                gender.getText().toString(), marital.getText().toString(), "0",
//                firstname.getText().toString(),middle.getText().toString(), dateico.getText().toString(),
//                gender.getText().toString(), marital.getText().toString(), "0",
//                dateico.getText().toString(), gender.getText().toString(), marital.getText().toString(),
//                "0");
//
//        databasehelper.addone(datamodel);
    }

    public void viewlist(){
        List<Datamodel> everyone = databasehelper.getEveryone();
        toast(everyone.toString());
    }

    public void deleteone(){
//        Datamodel delete = (Datamodel) ;
//        databasehelper.Deleteone(delete);
    }

    public void toast(String word){
        Toast.makeText(this, word+" cannot be empty", Toast.LENGTH_LONG).show();
    }
}