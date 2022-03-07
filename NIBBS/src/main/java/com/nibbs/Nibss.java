package com.nibbs;

import com.nibbs.volley.InitiateVolley;

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

}
