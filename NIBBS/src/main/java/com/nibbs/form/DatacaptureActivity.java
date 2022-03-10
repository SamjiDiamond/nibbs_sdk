package com.nibbs.form;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.nibbs.Constant;
import com.nibbs.R;

public class DatacaptureActivity extends AppCompatActivity {

    EditText title, firstname, surname, middle, dateico, gender, marital;
    ImageView backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datacapture);
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
//                Intent in = new Intent(getApplicationContext(), DatacaptureActivity.class);
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
            Intent in = new Intent(getApplicationContext(), LastdatacaptureActivity.class);
            startActivity(in);
        }
    }

    void savedata(){
        Constant.residentialaddress = title.getText().toString();
        Constant.stateofresidence = surname.getText().toString();
        Constant.lgaofresidence = middle.getText().toString();
        Constant.landmarks = middle.getText().toString();
        Constant.email = dateico.getText().toString();
        Constant.phonenumber = gender.getText().toString();
        Constant.phonenumber2 = marital.getText().toString();
    }

}
