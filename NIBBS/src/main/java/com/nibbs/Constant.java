package com.nibbs;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.Rect;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class Constant {
    public static List<Rect> HeadBounds;
    public static List<PointF> LeftEarPosition;
    public static List<Float> HeadRotationy, HeadRotationz,
            UserSmiling, RightEyeOpen ;

    public static String eyecoordinateText="";

    public static String saveToInternalStorage(Bitmap bitmapImage, Context context){
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profile.jpg");

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

    public static Bitmap loadImageFromStorage(String path)
    {
        Bitmap b = null;
        try {
            File f=new File(path, "profile.jpg");
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
    public static String title;
    public static String surname;
    public static String firstname;
    public static String middlename;
    public static String dob;
    public static String maritalstatus;
    public static String gender;
    public static String soo;
    public static String lga;
    public static String residentialaddress;
    public static String stateofresidence;
    public static String lgaofresidence;
    public static String landmarks;
    public static String email;
    public static String phonenumber;
    public static String phonenumber2;
    public static String accountlevel;
    public static String nin;
    public static String selectbank;
    public static String stateofcapture;
    public static String lgaofcapture;
}
