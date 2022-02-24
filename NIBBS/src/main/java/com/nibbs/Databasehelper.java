package com.nibbs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Databasehelper extends SQLiteOpenHelper {
    public static final String DATA_TABLE = "DATA_TABLE";
    public static final String COLUMN_TITLE = "TITLE";
    public static final String COLUMN_SURNAME = "SURNAME";
    public static final String COLUMN_FIRSTNAME = "FIRSTNAME";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_MIDDLENAME = "MIDLENAME";
    public static final String COLUMN_DATEOFBIRTH = "DATEOFBIRTH";
    public static final String COLUMN_GENDER = "GENDER";
    public static final String COLUMN_MARITALSTATUS = "MARITALSTATUS";
    public static final String COLUMN_UPLOADSTATUS = "UPLOADSTATUS";
    public static final String COLUMN_INSTITUTION_CODE = "INSTITUTION_CODE";
    public static final String COLUMN_INSTITUTION_NAME = "INSTITUTION_NAME";
    public static final String COLUMN_AGENT_CODE = "AGENT_CODE";
    public static final String COLUMN_TICKET_ID = "TICKET_ID";
    public static final String COLUMN_VALIDATION_STATUS = "VALIDATION_STATUS";
    public static final String COLUMN_CAPTURE_DATE = "CAPTURE_DATE";
    public static final String COLUMN_SYNC_DATE = "SYNC_DATE";
    public static final String COLUMN_VALIDATION_DATE = "VALIDATION_DATE";
    public static final String COLUMN_STATE_OF_CAPTURE = "STATE_OF_CAPTURE";
    public static final String COLUMN_STATE_OF_SYNC = "STATE_OF_SYNC";

    public Databasehelper(@Nullable Context context) {
        super(context, "dataform.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createtablestatement = "CREATE TABLE " + DATA_TABLE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                " " + COLUMN_TITLE + " TEXT, " + COLUMN_SURNAME + " TEXT, " + COLUMN_FIRSTNAME + " TEXT, " +
                COLUMN_MIDDLENAME + " TEXT,  " + COLUMN_DATEOFBIRTH + " TEXT, " + " " + COLUMN_GENDER +
                " TEXT,  " + COLUMN_MARITALSTATUS + " TEXT," + COLUMN_UPLOADSTATUS + " TEXT,"
                + COLUMN_INSTITUTION_CODE + " TEXT," + COLUMN_INSTITUTION_NAME + " TEXT," + COLUMN_AGENT_CODE + " TEXT,"
                + COLUMN_TICKET_ID + " TEXT," + COLUMN_VALIDATION_STATUS + " TEXT," + COLUMN_CAPTURE_DATE + " TEXT,"
                + COLUMN_SYNC_DATE + " TEXT," + COLUMN_VALIDATION_DATE + " TEXT," + COLUMN_STATE_OF_CAPTURE + " TEXT,"
                + COLUMN_STATE_OF_SYNC + " TEXT)";
        sqLiteDatabase.execSQL(createtablestatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addone(Datamodel datamodel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, datamodel.getTitle());
        cv.put(COLUMN_SURNAME, datamodel.getSurname());
        cv.put(COLUMN_FIRSTNAME, datamodel.getFirstname());
        cv.put(COLUMN_MIDDLENAME, datamodel.getMiddlename());
        cv.put(COLUMN_DATEOFBIRTH, datamodel.getDateofbirth());
        cv.put(COLUMN_GENDER, datamodel.getGender());
        cv.put(COLUMN_MARITALSTATUS, datamodel.getMaritalstatus());
        cv.put(COLUMN_UPLOADSTATUS, datamodel.getUploadstatus());

        long insert = db.insert(DATA_TABLE, null, cv);
        if (insert == -1){
            return false;
        } else {
            return true;
        }
    }

    public List<Datamodel> getEveryone(){
        List<Datamodel> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + DATA_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);

        if (cursor.moveToFirst()){
            do{
                int dataid = cursor.getInt(0);
                String datatitle = cursor.getString(1);
                String datasurname = cursor.getString(2);
                String datafirstname = cursor.getString(3);
                String datamiddlename = cursor.getString(4);
                String datadob = cursor.getString(5);
                String datagender = cursor.getString(6);
                String datamaritalstatus = cursor.getString(7);
                String datauploadstatus = cursor.getString(8);
                Datamodel newdata = new Datamodel(dataid,datatitle,
                        datasurname,datafirstname,datamiddlename,
                        datadob,datagender,datamaritalstatus,datauploadstatus,
                        datasurname,datafirstname,datamiddlename,
                        datadob,datagender,datamaritalstatus,datauploadstatus,
                        datagender,datamaritalstatus,datauploadstatus);
                returnList.add(newdata);

            }while(cursor.moveToNext());
        }else {

        }

        cursor.close();
        db.close();
        return returnList;
    }

    public boolean deleteone(Datamodel datamodel){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + DATA_TABLE + " WHERE " + COLUMN_ID + " = " + datamodel.getId();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()){
            return  true;
        }else {
            return  false;
        }
    }
    public boolean updateone(Datamodel datamodel){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "UPDATE " + DATA_TABLE + " SET " + COLUMN_UPLOADSTATUS + " = " + datamodel.getUploadstatus() + " WHERE " + COLUMN_ID + " = " + datamodel.getId();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()){
            return  true;
        }else {
            return  false;
        }
    }
}

