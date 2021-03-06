package com.nibbssdk;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.Rect;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kotlin.jvm.JvmStatic;

public class Constant {
    public static String baseurl = "https://app-api.nibbstest.budpay.ng/";
    public static List<Rect> HeadBounds= new ArrayList<>();
    public static List<PointF> LeftEarPosition= new ArrayList<>();
    public static List<Float> HeadRotationy = new ArrayList<>(), HeadRotationz = new ArrayList<>(),
            UserSmiling = new ArrayList<>(), RightEyeOpen = new ArrayList<>();
    public static String samjiProbEyeOpen="0", samjiProbSmile="0";

    public static String eyecoordinateText="";

    @JvmStatic
    public static String logstatus(String str){
        JSONObject obj;
        String pageName = "";
        try {
            obj = new JSONObject(str);
            pageName = obj.getString("success");
        } catch (JSONException e) {
            e.printStackTrace();
            pageName = e.toString();
        }
        return pageName;
    }

    public static String saveToInternalStorage(Bitmap bitmapImage, Context context,String name){
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,name+".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    public static Bitmap loadImageFromStorage(String path, String name)
    {
        Bitmap b = null;
        try {
            File f=new File(path, name+".jpg");
            b = BitmapFactory.decodeStream(new FileInputStream(f));
//            signature.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return b;
    }

    public static void toast(Context context,String word){
        Toast.makeText(context, word+" cannot be empty", Toast.LENGTH_LONG).show();
    }
    public static void errortoast(Context context,String word){
        Toast.makeText(context, word, Toast.LENGTH_LONG).show();
    }
    public static void inserterrortoast(Context context){
        Toast.makeText(context, "Data not store check and save it again", Toast.LENGTH_LONG).show();
    }
    public static void toastincomplete(Context context,String word){
        Toast.makeText(context, "Incorrect "+word, Toast.LENGTH_LONG).show();
    }
    public static String table_id = "";
}
