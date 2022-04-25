package com.nibbssdk;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nibbssdk.database.Databasehelper;

public class Nibss {

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
    public static int totalsync(Context context){
        return new Databasehelper(context).getsync().size();
    }
    public static int totalnotsync(Context context){
        return new Databasehelper(context).getnotsync().size();
    }
 public static int totalvalidate(Context context){
        return new Databasehelper(context).getvalidated().size();
    }

    public static String agent_code = "66519437";
    public static String intitution_code = "66519437";
    public static String agent_name = "Eyowo Sample Agen";
    public static String institution_name = "Eyowo Sample Agen";

    public static RequestQueue getRequestQueue(Context context) {
        RequestQueue mRequestQueue = null;
        if (mRequestQueue == null) {
           mRequestQueue = Volley.newRequestQueue(context);
        }
        return mRequestQueue;
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
