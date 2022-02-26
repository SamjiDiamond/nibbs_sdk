package com.nibbs;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class LastdatacaptureActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_lastdatacapture);

        databasehelper = new Databasehelper(LastdatacaptureActivity.this);
        dateico = findViewById(R.id.dateofbirthEditText);
        title = findViewById(R.id.titleEditText);
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialogtitle();
            }
        });
        surname = findViewById(R.id.surnameEditText);
        firstname = findViewById(R.id.firstnameEditText);
        firstname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialogmarital();

            }
        });
        middle = findViewById(R.id.middleEditText);

        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), BeginfaceActivity.class);
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

    private void showBottomSheetDialogtitle() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_title);
        String tutorials[] = { "Level 1 - Low Level Accounts", "Level 2 - Mid Level Accounts", "Level 3 - High Level Accounts"};
        ListView list = bottomSheetDialog.findViewById(R.id.list);
//        ArrayList<String> arrayList = new ArrayList<>();
//        arrayList.add("hello");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.my_custom_layout, tutorials);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem=(String) list.getItemAtPosition(position);
                title.setText(clickedItem);
                bottomSheetDialog.cancel();
//                Toast.makeText(DataformActivity.this,clickedItem,Toast.LENGTH_LONG).show();
            }
        });
        bottomSheetDialog.show();
    }
    private void showBottomSheetDialogmarital() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_title);
        String tutorials[] = {"Access Bank Plc","Access Bank (Diamond)","ALAT by WEMA","ASO Savings and Loans", "Citibank Nigeria Limited",
                "Ecobank Nigeria Plc","Ekondo Microfinance Bank", "Fidelity Bank Plc", "FIRST BANK NIGERIA LIMITED", "First City Monument Bank Plc",
                "Guaranty Trust Bank Plc","Heritage Banking time Ltd.", "Jaiz Bank", "Key Stone Bank", "Kuda Bank", "Parallex Bank", "Polaris Bank",
                "Providus Bank", "Stanbic IBTC Bank Ltd.", "Standard Chartered Bank Nigeria Ltd.", "Sterling Bank Plc", "SunTrust Bank Nigeria Limited",
                "Union Bank of Nigeria Plc", "United Bank For Africa Plc", "Unity Bank Plc", "Wema Bank Plc","Zenith Bank"};
        String listbank_code[] = new String[]{ "044","063","035A","401","023","050","562","070","011","214","058","030","301","082","50211","526","076",
                "101","221","068","232","100","032","033","215","035","057",
        };
        ListView list = bottomSheetDialog.findViewById(R.id.list);
//        ArrayList<String> arrayList = new ArrayList<>();
//        arrayList.add("hello");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.my_custom_layout, tutorials);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem=(String) list.getItemAtPosition(position);
                firstname.setText(clickedItem);
                bottomSheetDialog.cancel();
//                Toast.makeText(DataformActivity.this,clickedItem,Toast.LENGTH_LONG).show();
            }
        });
        bottomSheetDialog.show();
    }
}