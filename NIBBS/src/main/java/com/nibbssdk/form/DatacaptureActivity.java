package com.nibbssdk.form;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.nibbssdk.Constant;
import com.nibbssdk.R;
import com.nibbssdk.database.Databasehelper;

public class DatacaptureActivity extends AppCompatActivity {

    EditText title, firstname, surname, middle, dateico, gender, marital;
    ImageView backbutton;
    Databasehelper databasehelper;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databasehelper = new Databasehelper(getApplicationContext());
        setContentView(R.layout.activity_datacapture);
        sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        Constant.table_id = sharedpreferences.getString("table_id", "");
        Constant.errortoast(getApplicationContext(),"My result "+Constant.table_id);
        dateico = findViewById(R.id.dateofbirthEditText);
        title = findViewById(R.id.titleEditText);
        surname = findViewById(R.id.surnameEditText);
        firstname = findViewById(R.id.firstnameEditText);
        middle = findViewById(R.id.middleEditText);
        gender = findViewById(R.id.genderEditText);
        marital = findViewById(R.id.maritalstatusEditText);

        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
//                Intent in = new Intent(getApplicationContext(), LastdatacaptureActivity.class);
//                startActivity(in);
            }
        });


        backbutton = findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void validate(){
        if (title.getText().toString().isEmpty()){
            Constant.toast(this,"Residential Address");
        }else if (surname.getText().toString().isEmpty()){
            Constant.toast(this,"State Of Residence");
        }else if (firstname.getText().toString().isEmpty()){
            Constant.toast(this,"LGA Of Residence");
        }else if (middle.getText().toString().isEmpty()){
            Constant.toast(this,"Landmarks");
        }else if (dateico.getText().toString().isEmpty()){
            Constant.toast(this,"Email");
        }else if (gender.getText().toString().isEmpty()){
            Constant.toast(this,"Phone Number");
        }else if (gender.getText().toString().length() != 11){
            Constant.toastincomplete(this,"Phone Number");
        }else if (marital.getText().toString().isEmpty()){
            Constant.toast(this,"Phone Number 2");
        }else if (gender.getText().toString().length() != 11){
            Constant.toastincomplete(this,"Phone Number 2");
//        }else if (soo.getText().toString().isEmpty()){
//            Constant.toast(this,"State Of Origin");
//            Constant.maritalstatus = marital.getText().toString();
//        }else if (lga.getText().toString().isEmpty()){
//            Constant.toast(this, "Local Government Area");
//            Constant.soo = soo.getText().toString();
        }else {
            savedata();
            String residentialaddress = title.getText().toString();
            String stateofresidence = surname.getText().toString();
            String lgaofresidence = firstname.getText().toString();
            String landmarks = middle.getText().toString();
            String email = dateico.getText().toString().replace(" ","");
            String phonenumber = gender.getText().toString().replace(" ","");
            String phonenumber2 = marital.getText().toString().replace(" ","");
            boolean addone = databasehelper.insertdatacaptureactivity(residentialaddress,stateofresidence,
                    lgaofresidence,landmarks,email,phonenumber,phonenumber2);
            if (addone) {
                Intent in = new Intent(getApplicationContext(), LastdatacaptureActivity.class);
                startActivity(in);
            }else {
                Constant.inserterrortoast(this);
            }

        }
    }

    void savedata(){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("residentialaddress",title.getText().toString());
        editor.putString("stateofresidence",surname.getText().toString());
        editor.putString("lgaofresidence",firstname.getText().toString());
        editor.putString("landmarks",middle.getText().toString());
        editor.putString("email",dateico.getText().toString());
        editor.putString("phonenumber",gender.getText().toString());
        editor.putString("phonenumber2",marital.getText().toString());
        editor.apply();
    }

}
