package com.nibbssdk.form;

import static com.nibbssdk.Nibss.databasehelper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.TextView;

import com.nibbssdk.Constant;
import com.nibbssdk.Nibss;
import com.nibbssdk.PreviewActivity;
import com.nibbssdk.R;
import com.nibbssdk.database.Databasehelper;
import com.nibbssdk.database.Datamodel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DataformActivity extends AppCompatActivity {


    EditText title, firstname, surname, middle,
            dateico, gender, marital,soo,lga, nationality;
    ImageView backbutton;
    private String alldate = "";
    TextView textView;
    ArrayList<String> day = new ArrayList<>();
    int number  = 32;
    Databasehelper databasehelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dataform);
        databasehelper = new Databasehelper(getApplicationContext());
        dateico = findViewById(R.id.dateofbirthEditText);
        dateico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialogdate(view);

            }
        });
        title = findViewById(R.id.titleEditText);
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialogtitle(view);
            }
        });
        surname = findViewById(R.id.surnameEditText);
        firstname = findViewById(R.id.firstnameEditText);
        middle = findViewById(R.id.middleEditText);
        gender = findViewById(R.id.genderEditText);
        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialoggender(view);
            }
        });
        marital = findViewById(R.id.maritalstatusEditText);
        marital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialogmarital(view);
            }
        });
        soo = findViewById(R.id.stateoforiginEditText);
        lga = findViewById(R.id.lgaEditText);
        nationality = findViewById(R.id.nationalityEditText);
        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                databasehelper(getApplicationContext()).getcurrenttable("TOLULOPE", "ODEJINMI","Abraham");
//                Intent in = new Intent(getApplicationContext(), DatacaptureActivity.class);
//                startActivity(in);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(DataformActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.bottom_sheet_title, viewGroup, false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        String tutorials[] = { "Mrs", "Miss", "Mr", "Chief"};
        ListView list = dialogView.findViewById(R.id.list);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(DataformActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.bottom_sheet_title, viewGroup, false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        String tutorials[] = { "Single", "Married", "Divorsed", "Widow", "Widower"};
        ListView list = dialogView.findViewById(R.id.list);
//        ArrayList<String> arrayList = new ArrayList<>();
//        arrayList.add("hello");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.my_custom_layout, tutorials);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem=(String) list.getItemAtPosition(position);
                marital.setText(clickedItem);
                alertDialog.cancel();
//                Toast.makeText(DataformActivity.this,clickedItem,Toast.LENGTH_LONG).show();
            }
        });
        alertDialog.show();
    }
    private void showBottomSheetDialoggender(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DataformActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.bottom_sheet_title, viewGroup, false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        String tutorials[] = { "Male", "Female"};
        ListView list = dialogView.findViewById(R.id.list);
//        ArrayList<String> arrayList = new ArrayList<>();
//        arrayList.add("hello");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.my_custom_layout, tutorials);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem=(String) list.getItemAtPosition(position);
                gender.setText(clickedItem);
                alertDialog.cancel();
//                Toast.makeText(DataformActivity.this,clickedItem,Toast.LENGTH_LONG).show();
            }
        });
        alertDialog.show();
    }
    private void showBottomSheetDialogdate(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DataformActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.bottom_sheet_calendar, viewGroup, false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        String month[] = { "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        textView = dialogView.findViewById(R.id.textView);
        TextView daytext = dialogView.findViewById(R.id.daytext);
        TextView yeartext = dialogView.findViewById(R.id.yeartext);
        ListView list = dialogView.findViewById(R.id.month);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.my_custom_layout, month);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem=(String) list.getItemAtPosition(position);
//                alldate = clickedItem;
                textView.setText(clickedItem);
//                if (alldate.equals("September")||alldate.equals("April")||alldate.equals("June")||alldate.equals("November")){
//                    day.remove(32);
//                }else if (alldate.equals("February")){
//                    number = 31;
//                }else {
//                    number = 32;
//                }
//                generatedays();
//                Toast.makeText(DataformActivity.this,clickedItem,Toast.LENGTH_LONG).show();
            }
        });
        ArrayList<String> day1 = new ArrayList<>();
        for(int i = 1; i < number; i++){
            day1.add(""+i);
        }
        day.addAll(day1);
        ListView daylist = dialogView.findViewById(R.id.day);
        ArrayAdapter<String> dayarrayAdapter = new ArrayAdapter<String>(this,R.layout.my_custom_layout, day);
        daylist.setAdapter(dayarrayAdapter);
        daylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem=(String) daylist.getItemAtPosition(position);
//                alldate += " - " +clickedItem;
                daytext.setText(" - " +clickedItem);

//                Toast.makeText(DataformActivity.this,clickedItem,Toast.LENGTH_LONG).show();
            }
        });

        Calendar calendar = Calendar.getInstance();
        int currentyear = calendar.get(Calendar.YEAR);
        ArrayList<String> year = new ArrayList<>();
            for(int i = 1930; i < currentyear; i++){
                year.add(""+i);
            }
        ListView yearlist = dialogView.findViewById(R.id.year);
        ArrayAdapter<String> yeararrayAdapter = new ArrayAdapter<String>(this,R.layout.my_custom_layout, year);
        yearlist.setAdapter(yeararrayAdapter);

        yearlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem=(String) yearlist.getItemAtPosition(position);
//                alldate += " - " +clickedItem;
                yeartext.setText(" - "+clickedItem);
//                Toast.makeText(DataformActivity.this,clickedItem,Toast.LENGTH_LONG).show();
            }
        });

        Button submit = dialogView.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateico.setText(textView.getText().toString()+daytext.getText().toString()+yeartext.getText().toString());
                alertDialog.cancel();
//                bottomSheetDialog.cancel();
            }
        });

        alertDialog.show();
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
        ArrayList<String> day1 = new ArrayList<>();
        for(int i = 1; i < number; i++){
            day1.add(""+i);
        }
        day.addAll(day1);
    }
    public void validate(){
        if (title.getText().toString().isEmpty()){
            Constant.toast(this,"title");
        }else if (surname.getText().toString().isEmpty()){
            Constant.toast(this,"Surname");
        }else if (firstname.getText().toString().isEmpty()){
            Constant.toast(this,"First name");
        }else if (middle.getText().toString().isEmpty()){
            Constant.toast(this,"Middle name");
        }else if (dateico.getText().toString().isEmpty()){
            Constant.toast(this,"Date of Birth");
        }else if (gender.getText().toString().isEmpty()){
            Constant.toast(this,"Gender");
        }else if (marital.getText().toString().isEmpty()){
            Constant.toast(this,"Marital Status");
        }else if (soo.getText().toString().isEmpty()){
            Constant.toast(this,"State Of Origin");
        }else if (lga.getText().toString().isEmpty()){
            Constant.toast(this, "Local Government Area");
        }else {
            savedata();
//Friday, January 3, 2020
            String stringtitle = title.getText().toString();
            String stringsurname = surname.getText().toString().replace(" ","");
            String stringfirstname = firstname.getText().toString().replace(" ","");
            String stringmiddle = middle.getText().toString().replace(" ","");
            String stringdateico = dateico.getText().toString().replace(" ","");
            String stringgender= gender.getText().toString().replace(" ","");
            String stringmarital= marital.getText().toString();
            String stringnationality = nationality.getText().toString();;
            String stringsoo= soo.getText().toString();
            String stringlga= lga.getText().toString();
            boolean addone = databasehelper.insertdataformactivity(stringtitle,stringsurname,stringfirstname,
                    stringmiddle,stringdateico,stringgender,stringmarital,stringnationality,stringsoo, stringlga);
            if (addone) {
                databasehelper(this).getcurrenttable(stringsurname, stringfirstname,stringmiddle);
                Intent in = new Intent(getApplicationContext(), DatacaptureActivity.class);
                startActivity(in);
            }else {
                Constant.inserterrortoast(this);
            }

        }
    }

    void savedata(){
        Constant.title = title.getText().toString();
        Constant.surname = surname.getText().toString();
        Constant.firstname = firstname.getText().toString();
        Constant.middlename = middle.getText().toString();
        Constant.dob = dateico.getText().toString();
        Constant.gender = gender.getText().toString();
        Constant.maritalstatus = marital.getText().toString();
        Constant.nationality = nationality.getText().toString();
        Constant.soo = soo.getText().toString();
        Constant.lga = lga.getText().toString();
    }

}