package com.nibbs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
    public static final String COLUMN_NATIONALITY = "NATIONALITY";
    public static final String COLUMN_STATE_OF_ORIGIN = "STATE_OF_ORIGIN";
    public static final String COLUMN_LGA= "LGA";
    public static final String COLUMN_RESIDENTIAL_ADDRESS= "RESIDENTIAL_ADDRESS";
    public static final String COLUMN_STATE_OF_RESIDENCE= "STATE_OF_RESIDENCE";
    public static final String COLUMN_LGA_OF_RESIDENCE= "LGA_OF_RESIDENCE";
    public static final String COLUMN_LANDMARKS= "LANDMARKS";
    public static final String COLUMN_EMAIL= "EMAIL";
    public static final String COLUMN_PHONENUMBER= "PHONENUMBER";
    public static final String COLUMN_PHONENUMBER2= "PHONENUMBER2";
    public static final String COLUMN_ACCOUNTLEVEL= "ACOUNTLEVEL";
    public static final String COLUMN_NIN= "NIN";
    public static final String COLUMN_SELECTBANK= "SELECTBANK";
    public static final String COLUMN_LGA_OF_CAPTURE= "LGA_OF_CAPTURE";
    public static final String COLUMN_SIGNATUREIMAGE= "SIGNATUREIMAGE";
    public static final String COLUMN_SIGNATUREIMAGENAME= "SIGNATUREIMAGENAME";
    public static final String COLUMN_FACEIMAGE= "FACEIMAGE";
    public static final String COLUMN_FACEIMAGENAME= "FACEIMAGENAME";

    public Databasehelper(@Nullable Context context) {
        super(context, "dataform.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createtablestatement = "CREATE TABLE " + DATA_TABLE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                " " + COLUMN_TITLE + " TEXT, " + COLUMN_SURNAME + " TEXT, " + COLUMN_FIRSTNAME + " TEXT, " +
                COLUMN_MIDDLENAME + " TEXT,  " + COLUMN_DATEOFBIRTH + " TEXT, " + " " + COLUMN_GENDER +
                " TEXT,  " + COLUMN_MARITALSTATUS + " TEXT," + COLUMN_UPLOADSTATUS + " TEXT DEFAULT 0,"
                + COLUMN_INSTITUTION_CODE + " TEXT," + COLUMN_INSTITUTION_NAME + " TEXT," + COLUMN_AGENT_CODE + " TEXT,"
                + COLUMN_TICKET_ID + " TEXT," + COLUMN_VALIDATION_STATUS + " TEXT DEFAULT 0," + COLUMN_CAPTURE_DATE + " TEXT,"
                + COLUMN_SYNC_DATE + " TEXT," + COLUMN_VALIDATION_DATE + " TEXT," + COLUMN_STATE_OF_CAPTURE + " TEXT,"
                + COLUMN_STATE_OF_SYNC + " TEXT DEFAULT 0," + COLUMN_STATE_OF_ORIGIN + " TEXT," + COLUMN_LGA + " TEXT,"
                + COLUMN_RESIDENTIAL_ADDRESS + " TEXT," + COLUMN_STATE_OF_RESIDENCE + " TEXT,"
                + COLUMN_LGA_OF_RESIDENCE + " TEXT," + COLUMN_LANDMARKS + " TEXT," + COLUMN_EMAIL + " TEXT,"
                + COLUMN_PHONENUMBER + " TEXT," + COLUMN_PHONENUMBER2 + " TEXT," + COLUMN_ACCOUNTLEVEL + " TEXT,"
                + COLUMN_NIN + " TEXT," + COLUMN_SELECTBANK + " TEXT," + COLUMN_LGA_OF_CAPTURE + " TEXT,"
                + COLUMN_SIGNATUREIMAGE + " TEXT," + COLUMN_SIGNATUREIMAGENAME + " TEXT," +
                COLUMN_FACEIMAGE + " TEXT," + COLUMN_FACEIMAGENAME + " TEXT," + COLUMN_NATIONALITY + " TEXT )";
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
        cv.put(COLUMN_INSTITUTION_CODE, datamodel.getUploadstatus());
        cv.put(COLUMN_INSTITUTION_NAME, datamodel.getUploadstatus());
        cv.put(COLUMN_AGENT_CODE, datamodel.getUploadstatus());
        cv.put(COLUMN_TICKET_ID, datamodel.getUploadstatus());
        cv.put(COLUMN_VALIDATION_STATUS, datamodel.getUploadstatus());
        cv.put(COLUMN_CAPTURE_DATE, datamodel.getUploadstatus());
        cv.put(COLUMN_SYNC_DATE, datamodel.getUploadstatus());
        cv.put(COLUMN_VALIDATION_DATE, datamodel.getUploadstatus());
        cv.put(COLUMN_STATE_OF_CAPTURE, datamodel.getUploadstatus());
        cv.put(COLUMN_STATE_OF_SYNC, datamodel.getUploadstatus());
        cv.put(COLUMN_NATIONALITY, datamodel.getNationality());
        cv.put(COLUMN_STATE_OF_ORIGIN, datamodel.getSoo());
        cv.put(COLUMN_LGA, datamodel.getLga());
        cv.put(COLUMN_RESIDENTIAL_ADDRESS, datamodel.getResidentialaddress());
        cv.put(COLUMN_STATE_OF_RESIDENCE, datamodel.getStateofresidence());
        cv.put(COLUMN_LGA_OF_RESIDENCE, datamodel.getLgaofresidence());
        cv.put(COLUMN_LANDMARKS, datamodel.getLandmarks());
        cv.put(COLUMN_EMAIL, datamodel.getEmail());
        cv.put(COLUMN_PHONENUMBER, datamodel.getPhonenumber());
        cv.put(COLUMN_PHONENUMBER2, datamodel.getPhonenumber2());
        cv.put(COLUMN_ACCOUNTLEVEL, datamodel.getAccountlevel());
        cv.put(COLUMN_NIN, datamodel.getNin());
        cv.put(COLUMN_SELECTBANK, datamodel.getSelectbanke());
        cv.put(COLUMN_LGA_OF_CAPTURE, datamodel.getLgaofcapture());
        cv.put(COLUMN_SIGNATUREIMAGE, datamodel.getSignatureimage());
        cv.put(COLUMN_SIGNATUREIMAGENAME, datamodel.getSignatureimagename());
        cv.put(COLUMN_FACEIMAGE, datamodel.getFaceimage());
        cv.put(COLUMN_FACEIMAGENAME, datamodel.getFaceimagename());

        long insert = db.insert(DATA_TABLE, null, cv);
        if (insert == -1){
            return false;
        } else {
            return true;
        }
    }

    public List<Datamodel> getall(){
        List<Datamodel> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + DATA_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);

        returnList = cursor(cursor);

        cursor.close();
        db.close();
        return returnList;
    }
    public List<Datamodel> getnotupload(){
        List<Datamodel> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + DATA_TABLE +" WHERE "+ COLUMN_UPLOADSTATUS +"= 0";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);

        returnList = cursor(cursor);

        cursor.close();
        db.close();
        return returnList;
    }
    public List<Datamodel> getuploaded(){
        List<Datamodel> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + DATA_TABLE +" WHERE "+ COLUMN_UPLOADSTATUS +" = 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);

        returnList = cursor(cursor);

        cursor.close();
        db.close();
        return returnList;
    }
    public List<Datamodel> getvalidated(){
        List<Datamodel> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + DATA_TABLE +" WHERE "+ COLUMN_VALIDATION_STATUS +" = 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);

        returnList = cursor(cursor);

        cursor.close();
        db.close();
        return returnList;
    }
    public List<Datamodel> getsync(){
        List<Datamodel> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + DATA_TABLE +" WHERE "+ COLUMN_STATE_OF_SYNC +" = 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);

        returnList = cursor(cursor);

        cursor.close();
        db.close();
        return returnList;
    }

    public void updatesync(String columname, String updatevalue, String position){

        String queryString = "UPDATE " + DATA_TABLE +" SET "+ columname+" = "+updatevalue +" WHERE "+ COLUMN_ID +" = "+position;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        Log.d("TAG", "updatesync: "+cursor);
//        returnList = cursor(cursor);
//
//        cursor.close();
//        db.close();
//        return returnList;
    }



    private  List<Datamodel> cursor(Cursor cursor){
        List<Datamodel> returnList = new ArrayList<>();
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
                String datainstitutioncode = cursor.getString(9);
                String datainstitutionname = cursor.getString(10);
                String dataagentcode = cursor.getString(11);
                String dataticketid = cursor.getString(12);
                String datavidationstatus = cursor.getString(13);
                String datacapturedate = cursor.getString(14);
                String datasyncdate = cursor.getString(15);
                String datavalidationdate = cursor.getString(16);
                String datastateofcapture = cursor.getString(17);
                String datastateofsyn = cursor.getString(18);
                String datasoo = cursor.getString(19);
                String datalga = cursor.getString(20);
                String dataresidentialaddress = cursor.getString(21);
                String datastateofresidence = cursor.getString(22);
                String datalgaofresisdence = cursor.getString(23);
                String datalandmarks = cursor.getString(24);
                String dataemail = cursor.getString(25);
                String dataphonenumber = cursor.getString(26);
                String dataphonenumber2 = cursor.getString(27);
                String dataaccountlevel = cursor.getString(28);
                String datanin = cursor.getString(29);
                String dataselectbank = cursor.getString(30);
                String datalgaofcapture = cursor.getString(31);
                String datasignatureimage = cursor.getString(32);
                String datasignatureimagename = cursor.getString(33);
                String datafaceimage = cursor.getString(34);
                String datafaceimagename = cursor.getString(35);
                String datanationality = cursor.getString(36);
                Datamodel newdata = new Datamodel(dataid,datatitle,
                        datasurname,datafirstname,datamiddlename,
                        datadob,datagender,datamaritalstatus,
                        datainstitutioncode,datainstitutionname,dataagentcode,
                        dataticketid,datacapturedate,datastateofcapture,
                        datasoo,datanationality,datalga,dataresidentialaddress,datastateofresidence,
                        datalgaofresisdence,datalandmarks,dataemail,
                        dataphonenumber,dataphonenumber2,dataaccountlevel,datanin,
                        dataselectbank,datalgaofcapture,datasignatureimage,
                        datasignatureimagename,datafaceimage,datafaceimagename);
                returnList.add(newdata);

            }while(cursor.moveToNext());
        }else {

        }
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

