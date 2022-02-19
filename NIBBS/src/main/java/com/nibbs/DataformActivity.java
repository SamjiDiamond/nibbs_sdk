package com.nibbs;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;

import java.util.Calendar;
import java.util.List;

public class DataformActivity extends AppCompatActivity {

    DatePickerDialog picker;
    Databasehelper databasehelper;
    EditText title, firstname, surname, middle, dateico, gender, marital;
    Datamodel datamodel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dataform);
        databasehelper = new Databasehelper(DataformActivity.this);
        dateico = findViewById(R.id.dateofbirthEditText);
        dateico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(DataformActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dateico.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();


//                MonthYearPickerDialog pd = MonthYearPickerDialog.newInstance(5,12, 1999);
//                //new MonthYearPickerDialog();
//
//                pd.setListener(new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
//                        dateico.setText(selectedDay +"/"+  selectedMonth + "/" + selectedYear );
//
//
//                    }
//                });
                //pd.show();

            }
        });
        title = findViewById(R.id.titleEditText);
        surname = findViewById(R.id.surnameEditText);
        firstname = findViewById(R.id.firstnameEditText);
        middle = findViewById(R.id.middleEditText);
        gender = findViewById(R.id.genderEditText);
        marital = findViewById(R.id.maritalstatusEditText);
        TextView submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

    }

    public void validate(){
        if (title.getText().toString().isEmpty()){
            toast("title");
        }else if (surname.getText().toString().isEmpty()){
            toast("Surname");
        }else if (firstname.getText().toString().isEmpty()){
            toast("First name");
        }else if (middle.getText().toString().isEmpty()){
            toast("Middle name");
        }else if (dateico.getText().toString().isEmpty()){
            toast("Date of Birth");
        }else if (gender.getText().toString().isEmpty()){
            toast("Gender");
        }else if (marital.getText().toString().isEmpty()){
            toast("Marital Status");
        }else {
            try {
                datamodel = new Datamodel(1,title.getText().toString(),surname.getText().toString(),
                        firstname.getText().toString(),middle.getText().toString(), dateico.getText().toString(),
                        gender.getText().toString(), marital.getText().toString());

                databasehelper.addone(datamodel);
            } catch (Exception e) {
                e.printStackTrace();
//                datamodel = new Datamodel(1,"Mrs","odejinmi","ayomiposi",
//                        "Ruth", "26/10/1995", "female", "Married");
            }
        }
    }

    public void viewlist(){
         List<Datamodel> everyone = databasehelper.getEveryone();
    }

    public void deleteone(){
//        Datamodel delete = (Datamodel) ;
//        databasehelper.Deleteone(delete);
    }

    public void toast(String word){
        Toast.makeText(this, word+" cannot be empty", Toast.LENGTH_LONG).show();
    }
}