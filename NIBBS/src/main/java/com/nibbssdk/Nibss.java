package com.nibbssdk;

import com.nibbssdk.database.Databasehelper;
import com.nibbssdk.volley.InitiateVolley;

public class Nibss {
   public static Databasehelper databasehelper = new Databasehelper(InitiateVolley.getInstance());
    public static int totalsubmitted(){
        return databasehelper.getall().size();
    }
     public static String totalsubmittedlist(){
        return databasehelper.getall().toString();
    }
    public static int totaluploded(){
        return databasehelper.getuploaded().size();
    }
    public static int totalsync(){
        return databasehelper.getsync().size();
    }
 public static int totalvalidate(){
        return databasehelper.getvalidated().size();
    }
    public static String agent_code = "12345678";
}
