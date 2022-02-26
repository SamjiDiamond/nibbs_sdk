package com.nibbs;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DatacaptureActivity extends AppCompatActivity {

    DatePickerDialog picker;
    Databasehelper databasehelper;
    EditText title, firstname, surname, middle, dateico, gender, marital;
    Datamodel datamodel;
    ImageView backbutton;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    private String alldate = "";
    TextView textView;
    ArrayList<String> day = new ArrayList<>();
    int number  = 32;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datacapture);
        databasehelper = new Databasehelper(DatacaptureActivity.this);
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
                Intent in = new Intent(getApplicationContext(), LastdatacaptureActivity.class);
                startActivity(in);
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
}
