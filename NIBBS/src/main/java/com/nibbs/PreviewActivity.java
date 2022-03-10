package com.nibbs;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nibbs.database.Databasehelper;
import com.nibbs.database.Datamodel;
import com.nibbs.form.BegincaptureActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PreviewActivity extends AppCompatActivity {

    Databasehelper databasehelper;
    EditText title, firstname, surname, middle,
            dateico, gender, marital,soo,lga;
    EditText title1, firstname1, surname1, middle1, dateico1, gender1, marital1;
    EditText title2, firstname2, surname2, middle2, dateico2;
    ImageView backbutton,signature, imageView;
    String timeStamp, ticketid;
    Datamodel datamodel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        databasehelper = new Databasehelper(PreviewActivity.this);
        backbutton = findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        imageView = findViewById(R.id.imagepreview);
        Bitmap b1 = Constant.loadImageFromStorage(Constant.signatureimage,Constant.faceimagename);
        imageView.setImageBitmap(b1);
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
        Bitmap b = Constant.loadImageFromStorage(Constant.signatureimage,Constant.signatureimagename);
        signature.setImageBitmap(b);
        ImageView leftindex = findViewById(R.id.leftindex);
        String[] fingerprintimage = Constant.fingerprintimage.split(";");
        String[] fingerprintname = Constant.fingerprintname.split(";");
        Bitmap b0 = Constant.loadImageFromStorage(fingerprintimage[0], fingerprintname[0]);
        leftindex.setImageBitmap(b0);
        ImageView leftmiddle = findViewById(R.id.leftmiddle);
        Bitmap b11 = Constant.loadImageFromStorage(fingerprintimage[1], fingerprintname[1]);
        leftmiddle.setImageBitmap(b11);
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
        Button signpage = findViewById(R.id.submit);
        signpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                datamodel = new Datamodel(1,Constant.title,Constant.surname,
                        Constant.firstname,Constant.middlename, Constant.dob,
                        Constant.gender, Constant.maritalstatus,
                        Constant.institutioncode,Constant.institutionname, Constant.agentcode,
                        "ticketid",  timeStamp, Constant.stateofcapture,
                        Constant.soo,Constant.nationality,Constant.lga, Constant.residentialaddress,
                        Constant.stateofresidence, Constant.lgaofresidence, Constant.landmarks,
                        Constant.email,Constant.phonenumber, Constant.phonenumber2,
                        Constant.accountlevel, Constant.nin, Constant.selectbank,
                        Constant.lgaofcapture, Constant.signatureimage, Constant.signatureimagename,
                        Constant.faceimage,Constant.faceimagename, Constant.fingerprintimage,Constant.fingerprintname,
                        "0","0","0");

                boolean addone = databasehelper.addone(datamodel);
                if (addone) {
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PreviewActivity.this);
                    alertDialogBuilder.setTitle("BVN Enrolment Ticket");
                    alertDialogBuilder.setMessage("Ticket ID: 55932019112584436 \n Date Captured: Friday, January 3, 2020 \n Agen: Eyowo Sample Agen");
                    alertDialogBuilder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Intent in = new Intent(getApplicationContext(), BegincaptureActivity.class);
                                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(in);
                                    finish();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }else{
                    Log.d("TAG", "onClick: data not submitted");
                }
            }
        });

    }

    public void viewlist(){
//        List<Datamodel> everyone = databasehelper.getEveryone();
//        toast(everyone.toString());
    }

    public void deleteone(){
//        Datamodel delete = (Datamodel) ;
//        databasehelper.Deleteone(delete);
    }

    public void toast(String word){
        Toast.makeText(this, word+" cannot be empty", Toast.LENGTH_LONG).show();
    }
}