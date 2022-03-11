package com.nibbssdk.form;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.nibbssdk.Constant;
import com.nibbssdk.R;
import com.nibbssdk.face.BeginfaceActivity;

public class LastdatacaptureActivity extends AppCompatActivity {

    EditText title, firstname, surname, middle, dateico;
    ImageView backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lastdatacapture);

        dateico = findViewById(R.id.dateofbirthEditText);
        title = findViewById(R.id.titleEditText);
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialogtitle(view);
            }
        });
        surname = findViewById(R.id.surnameEditText);
        firstname = findViewById(R.id.firstnameEditText);
        firstname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialogmarital(view);

            }
        });
        middle = findViewById(R.id.middleEditText);

        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
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

    private void showBottomSheetDialogtitle(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LastdatacaptureActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.bottom_sheet_title, viewGroup, false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        String tutorials[] = { "Level 1 - Low Level Accounts", "Level 2 - Mid Level Accounts", "Level 3 - High Level Accounts"};
        ListView list = dialogView.findViewById(R.id.list);
//        ArrayList<String> arrayList = new ArrayList<>();
//        arrayList.add("hello");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.my_custom_layout, tutorials);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem=(String) list.getItemAtPosition(position);
                title.setText(clickedItem);
                alertDialog.cancel();
//                Toast.makeText(DataformActivity.this,clickedItem,Toast.LENGTH_LONG).show();
            }
        });
        alertDialog.show();
    }
    private void showBottomSheetDialogmarital(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LastdatacaptureActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.bottom_sheet_title, viewGroup, false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        String tutorials[] = {"Access Bank Plc","Access Bank (Diamond)","ALAT by WEMA","ASO Savings and Loans", "Citibank Nigeria Limited",
                "Ecobank Nigeria Plc","Ekondo Microfinance Bank", "Fidelity Bank Plc", "FIRST BANK NIGERIA LIMITED", "First City Monument Bank Plc",
                "Guaranty Trust Bank Plc","Heritage Banking time Ltd.", "Jaiz Bank", "Key Stone Bank", "Kuda Bank", "Parallex Bank", "Polaris Bank",
                "Providus Bank", "Stanbic IBTC Bank Ltd.", "Standard Chartered Bank Nigeria Ltd.", "Sterling Bank Plc", "SunTrust Bank Nigeria Limited",
                "Union Bank of Nigeria Plc", "United Bank For Africa Plc", "Unity Bank Plc", "Wema Bank Plc","Zenith Bank"};
        String listbank_code[] = new String[]{ "044","063","035A","401","023","050","562","070","011","214","058","030","301","082","50211","526","076",
                "101","221","068","232","100","032","033","215","035","057",
        };
        ListView list = dialogView.findViewById(R.id.list);
//        ArrayList<String> arrayList = new ArrayList<>();
//        arrayList.add("hello");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.my_custom_layout, tutorials);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem=(String) list.getItemAtPosition(position);
                firstname.setText(clickedItem);
                alertDialog.cancel();
//                Toast.makeText(DataformActivity.this,clickedItem,Toast.LENGTH_LONG).show();
            }
        });
        alertDialog.show();
    }

    public void validate(){
        if (title.getText().toString().isEmpty()){
            Constant.toast(this,"Account Level");
        }else if (surname.getText().toString().isEmpty()){
            Constant.toast(this,"NIN");
        }else if (surname.getText().toString().length() != 11){
            Constant.toastincomplete(this,"NIN");
        }else if (firstname.getText().toString().isEmpty()){
            Constant.toast(this,"Select Bank");
        }else if (middle.getText().toString().isEmpty()){
            Constant.toast(this,"State Of Capture");
        }else if (dateico.getText().toString().isEmpty()){
            Constant.toast(this,"LGA Of Capture");
        }else {
            savedata();
            Intent in = new Intent(getApplicationContext(), BeginfaceActivity.class);
            startActivity(in);
        }
    }

    void savedata(){
        Constant.accountlevel = title.getText().toString();
        Constant.nin = surname.getText().toString();
        Constant.selectbank = middle.getText().toString();
        Constant.stateofcapture = middle.getText().toString();
        Constant.lgaofcapture = dateico.getText().toString();
    }
}