package com.nibbs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class PreviewActivity extends AppCompatActivity {

    Databasehelper databasehelper;
    EditText title, firstname, surname, middle,
            dateico, gender, marital,soo,lga;

    Datamodel datamodel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        databasehelper = new Databasehelper(PreviewActivity.this);

        datamodel = new Datamodel(1,title.getText().toString(),surname.getText().toString(),
                firstname.getText().toString(),middle.getText().toString(), dateico.getText().toString(),
                gender.getText().toString(), marital.getText().toString(), "0",
                firstname.getText().toString(),middle.getText().toString(), dateico.getText().toString(),
                gender.getText().toString(), marital.getText().toString(), "0",
                dateico.getText().toString(), gender.getText().toString(), marital.getText().toString(),
                "0");

        databasehelper.addone(datamodel);
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