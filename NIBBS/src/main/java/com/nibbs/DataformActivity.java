package com.nibbs;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DataformActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_dataform);
        databasehelper = new Databasehelper(DataformActivity.this);
        dateico = findViewById(R.id.dateofbirthEditText);
        dateico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialogdate();

            }
        });
        title = findViewById(R.id.titleEditText);
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialogtitle();
            }
        });
        surname = findViewById(R.id.surnameEditText);
        firstname = findViewById(R.id.firstnameEditText);
        middle = findViewById(R.id.middleEditText);
        gender = findViewById(R.id.genderEditText);
        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialoggender();
            }
        });
        marital = findViewById(R.id.maritalstatusEditText);
        marital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialogmarital();
            }
        });
        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                validate();
                Intent in = new Intent(getApplicationContext(), DatacaptureActivity.class);
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
        String tutorials[] = { "Mrs", "Miss", "Mr", "Chief"};
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
        String tutorials[] = { "Single", "Married", "Divorsed", "Widow", "Widower"};
        ListView list = bottomSheetDialog.findViewById(R.id.list);
//        ArrayList<String> arrayList = new ArrayList<>();
//        arrayList.add("hello");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.my_custom_layout, tutorials);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem=(String) list.getItemAtPosition(position);
                marital.setText(clickedItem);
                bottomSheetDialog.cancel();
//                Toast.makeText(DataformActivity.this,clickedItem,Toast.LENGTH_LONG).show();
            }
        });
        bottomSheetDialog.show();
    }
    private void showBottomSheetDialoggender() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_title);
        String tutorials[] = { "Male", "Female"};
        ListView list = bottomSheetDialog.findViewById(R.id.list);
//        ArrayList<String> arrayList = new ArrayList<>();
//        arrayList.add("hello");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.my_custom_layout, tutorials);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem=(String) list.getItemAtPosition(position);
                gender.setText(clickedItem);
                bottomSheetDialog.cancel();
//                Toast.makeText(DataformActivity.this,clickedItem,Toast.LENGTH_LONG).show();
            }
        });
        bottomSheetDialog.show();
    }
    private void showBottomSheetDialogdate() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_calendar);

        String month[] = { "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        textView = bottomSheetDialog.findViewById(R.id.textView);
        ListView list = bottomSheetDialog.findViewById(R.id.month);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.my_custom_layout, month);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem=(String) list.getItemAtPosition(position);
                alldate = clickedItem;
                textView.setText(alldate);
//                generatedays();
//                Toast.makeText(DataformActivity.this,clickedItem,Toast.LENGTH_LONG).show();
            }
        });
        generatedays();
        ListView daylist = bottomSheetDialog.findViewById(R.id.day);
        ArrayAdapter<String> dayarrayAdapter = new ArrayAdapter<String>(this,R.layout.my_custom_layout, day);
        daylist.setAdapter(dayarrayAdapter);
        daylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem=(String) daylist.getItemAtPosition(position);
                alldate += " - " +clickedItem;
                textView.setText(alldate);
//                Toast.makeText(DataformActivity.this,clickedItem,Toast.LENGTH_LONG).show();
            }
        });

        Calendar calendar = Calendar.getInstance();
        int currentyear = calendar.get(Calendar.YEAR);
        ArrayList<String> year = new ArrayList<>();
            for(int i = 1930; i < currentyear; i++){
                year.add(""+i);
            }
        ListView yearlist = bottomSheetDialog.findViewById(R.id.year);
        ArrayAdapter<String> yeararrayAdapter = new ArrayAdapter<String>(this,R.layout.my_custom_layout, year);
        yearlist.setAdapter(yeararrayAdapter);
        yearlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem=(String) yearlist.getItemAtPosition(position);
                alldate += " - " +clickedItem;
                textView.setText(alldate);
//                Toast.makeText(DataformActivity.this,clickedItem,Toast.LENGTH_LONG).show();
            }
        });

        Button submit = bottomSheetDialog.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateico.setText(alldate);
                bottomSheetDialog.cancel();
            }
        });

        bottomSheetDialog.show();
    }

    void generatedays(){
        if (alldate.equals("September")||alldate.equals("April")||alldate.equals("June")||alldate.equals("November")){
            number = 31;
        }else if (alldate.equals("February")){
            number = 31;
        }else {
            number = 32;
        }
        day.clear();
        for(int i = 1; i < number; i++){
            day.add(""+i);
        }
    }
    public void validate(){
        if (title.getText().toString().isEmpty()){
            toast("title");
//            viewlist();
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
                        gender.getText().toString(), marital.getText().toString(), "0",
                        firstname.getText().toString(),middle.getText().toString(), dateico.getText().toString(),
                        gender.getText().toString(), marital.getText().toString(), "0",
                        dateico.getText().toString(), gender.getText().toString(), marital.getText().toString(),
                        "0");

                databasehelper.addone(datamodel);
                Intent in = new Intent(getApplicationContext(), DatacaptureActivity.class);
                startActivity(in);
//                finish();
                toast("data saved");
            } catch (Exception e) {
                e.printStackTrace();
//                datamodel = new Datamodel(1,"Mrs","odejinmi","ayomiposi",
//                        "Ruth", "26/10/1995", "female", "Married");
            }
        }
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