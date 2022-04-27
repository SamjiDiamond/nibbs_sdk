package com.nibbssdk;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nibbssdk.database.Databasehelper;
import com.nibbssdk.database.Datamodel;
import com.nibbssdk.form.BegincaptureActivity;
import com.nibbssdk.services.Util;

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
    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
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
        Bitmap b1 = Constant.loadImageFromStorage(sharedpreferences.getString("signatureimage",""),sharedpreferences.getString("faceimagename",""));
        imageView.setImageBitmap(b1);
        dateico = findViewById(R.id.dateofbirthEditText);
        dateico.setText(sharedpreferences.getString("dob",""));
        title = findViewById(R.id.titleEditText);
        title.setText(sharedpreferences.getString("title",""));
        surname = findViewById(R.id.surnameEditText);
        surname.setText(sharedpreferences.getString("surname",""));
        firstname = findViewById(R.id.firstnameEditText);
        firstname.setText(sharedpreferences.getString("firstname",""));
        middle = findViewById(R.id.middleEditText);
        middle.setText(sharedpreferences.getString("middlename",""));
        gender = findViewById(R.id.genderEditText);
        gender.setText(sharedpreferences.getString("gender",""));
        marital = findViewById(R.id.maritalstatusEditText);
        marital.setText(sharedpreferences.getString("maritalstatus",""));
        soo = findViewById(R.id.stateoforiginEditText);
        soo.setText(sharedpreferences.getString("soo",""));
        lga = findViewById(R.id.lgaEditText);
        lga.setText(sharedpreferences.getString("lga",""));
        dateico1 = findViewById(R.id.dateofbirthEditText1);
        dateico1.setText(sharedpreferences.getString("email",""));
        title1 = findViewById(R.id.titleEditText1);
        title1.setText(sharedpreferences.getString("residentialaddress",""));
        surname1 = findViewById(R.id.surnameEditText1);
        surname1.setText(sharedpreferences.getString("stateofresidence",""));
        firstname1 = findViewById(R.id.firstnameEditText1);
        firstname1.setText(sharedpreferences.getString("lgaofresidence",""));
        middle1 = findViewById(R.id.middleEditText1);
        middle1.setText(sharedpreferences.getString("landmarks",""));
        gender1 = findViewById(R.id.genderEditText1);
        gender1.setText(sharedpreferences.getString("phonenumber",""));
        marital1 = findViewById(R.id.maritalstatusEditText1);
        marital1.setText(sharedpreferences.getString("phonenumber2",""));
        dateico2 = findViewById(R.id.dateofbirthEditText2);
        dateico2.setText(sharedpreferences.getString("lgaofcapture",""));
        title2 = findViewById(R.id.titleEditText2);
        title2.setText(sharedpreferences.getString("accountlevel",""));
        surname2 = findViewById(R.id.surnameEditText2);
        surname2.setText(sharedpreferences.getString("nin",""));
        firstname2 = findViewById(R.id.firstnameEditText2);
        firstname2.setText(sharedpreferences.getString("selectbank",""));
        middle2 = findViewById(R.id.middleEditText2);
        middle2.setText(sharedpreferences.getString("stateofcapture",""));
        signature = findViewById(R.id.signaturepreview);
        Bitmap b = Constant.loadImageFromStorage(sharedpreferences.getString("signatureimage",""),sharedpreferences.getString("signatureimagename",""));
        signature.setImageBitmap(b);
        ImageView leftindex = findViewById(R.id.leftindex);
        String[] fingerprintimage = sharedpreferences.getString("fingerprintimage","").split(";");
        String[] fingerprintname = sharedpreferences.getString("fingerprintname","").split(";");
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
                String agent_code = sharedpreferences.getString("agent_code","");
                String agent_name = sharedpreferences.getString("agent_name","");
                ticketid = agent_code;
                ticketid += new SimpleDateFormat("yyMMDD").format(new Date());
                ticketid += new SimpleDateFormat("HHMMSS").format(new Date());
                String date = new SimpleDateFormat("E, MMMM dd, yyyy").format(new Date());

//Friday, January 3, 2020
                boolean addone = databasehelper.insertcomplete(ticketid,agent_code,timeStamp, getApplicationContext());
                if (addone) {
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PreviewActivity.this);
                    alertDialogBuilder.setTitle("BVN Enrolment Ticket");
                    alertDialogBuilder.setMessage("Ticket ID: "+ticketid+" \n Date Captured: "+ date+" \n Agent: "+agent_name);
                    alertDialogBuilder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Util.scheduleJob(getApplicationContext(), Long.parseLong("1"));
                                    Intent in = new Intent(getApplicationContext(), Nibss.destination);
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