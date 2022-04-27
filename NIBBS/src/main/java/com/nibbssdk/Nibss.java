package com.nibbssdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nibbssdk.database.Databasehelper;
import com.nibbssdk.form.BegincaptureActivity;
import com.nibbssdk.services.Util;

public class Nibss {

    public static void startuploading(Context context){
            Util.scheduleJob(context, Long.parseLong("1"));
    }
    public static int totalsubmitted(Context context){
        return new Databasehelper(context).getall().size();
    }
//     public static String totalsubmittedlist(Context context){
//        return databasehelper(context).getall().toString();
//    }
    public static int totaluploded(Context context){
        return new Databasehelper(context).getuploaded().size();
    }
    public static int totalnotuploded(Context context){
        return new Databasehelper(context).getnotupload().size();
    }
    public static int totalfailed(Context context){
        return new Databasehelper(context).getfailed().size();
    }
    public static int totalsync(Context context){
        return new Databasehelper(context).getsync().size();
    }
    public static int totalnotsync(Context context){
        return new Databasehelper(context).getnotsync().size();
    }
 public static int totalvalidate(Context context){
        return new Databasehelper(context).getvalidated().size();
    }

    public static String agent_code (String data,Context context){
        String value = "";
        if(data == null){
            value ="66519437";
        }else{
            value = data;
        }
        storedata("agent_code", value,context );
        return value;
    }
    public static String intitution_code (String data,Context context){
        String value = "";
        if(data == null){
            value ="66519437";
        }else{
            value = data;
        }
        storedata("intitution_code", value,context );
        return value;
    }
     public static String agent_name (String data,Context context){
        String value = "";
        if(data == null){
            value ="Eyowo Sample Agent";
        }else{
            value = data;
        }
        storedata("agent_name", value,context );
        return value;
    }
    public static String institution_name (String data,Context context){
        String value = "";
        if(data == null){
            value ="Eyowo Sample Agent";
        }else{
            value = data;
        }
        storedata("institution_name", value,context );
        return value;
    }
    public static Class destination = BegincaptureActivity.class;
 private static void storedata(String s,String s1, Context context) {
     SharedPreferences sharedpreferences = context.getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
     SharedPreferences.Editor editor = sharedpreferences.edit();
     editor.putString(s, s1);
     editor.apply();
 }
    public static Databasehelper databasehelper(Context context){
        return new Databasehelper(context);
    }

    public static RequestQueue getRequestQueue(Context context) {
        return Volley.newRequestQueue(context);
    }

    public <T> void addToRequestQueue(Request<T> request, String str, Context context) {
        if (TextUtils.isEmpty(str)) {
            str = "TAG";
        }
        request.setTag(str);
        getRequestQueue(context).add(request);
    }

    public <T> void addToRequestQueue(Request<T> request,Context context) {
        request.setTag("TAG");
        getRequestQueue(context).add(request);
    }
}
