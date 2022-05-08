package com.nibbssdk.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.nibbssdk.Constant;
import com.nibbssdk.database.Databasehelper;
import com.nibbssdk.database.Datamodel;
import com.nibbssdk.request.GetRequest;
import com.nibbssdk.request.PostRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Uploadservice extends JobService {
    Databasehelper databasehelper;
    List<Datamodel> notuploaded;
    @Override
    public boolean onStartJob(JobParameters params) {
        // Write your code here

        uploaddata();
        return true;
    }

    void uploaddata(){
        databasehelper = new Databasehelper(getApplicationContext());
        notuploaded = databasehelper.getnotupload();
        Log.d("tag", "onStartJob: this is where we are "+notuploaded.size());
        try{
            for (int i = 0; i < notuploaded.size(); i++) {
                Log.d("tag", "onStartJob: workin "+notuploaded.size());
                String ticketID = notuploaded.get(i).getTicketid();
                String title = notuploaded.get(i).getTitle();
                String surname = notuploaded.get(i).getSurname();
                String middle_name = notuploaded.get(i).getMiddlename();
                String first_name = notuploaded.get(i).getFirstname();
                String gender = notuploaded.get(i).getGender();
                String date_of_birth = notuploaded.get(i).getDateofbirth();
                String marital_status = notuploaded.get(i).getMaritalstatus();
                String nationality = notuploaded.get(i).getNationality();
                String state_of_origin = notuploaded.get(i).getSoo();
                String lga_of_origin = notuploaded.get(i).getLga();
                String state_of_capture = notuploaded.get(i).getStateofcapture();
                String lga_of_capture = notuploaded.get(i).getLgaofcapture();
                String bankname = notuploaded.get(i).getSelectbanke();
                String nin = notuploaded.get(i).getNin();
                String residential_address = notuploaded.get(i).getResidentialaddress();
                String state_of_residence = notuploaded.get(i).getStateofresidence();
                String lga_of_residence = notuploaded.get(i).getLgaofresidence();
                String landmarks = notuploaded.get(i).getLandmarks();
                String email = notuploaded.get(i).getEmail();
                String phone_number_1 = notuploaded.get(i).getPhonenumber();
                String phone_number_2 = notuploaded.get(i).getPhonenumber2();
                //Notifying the user
                Bitmap bitmap = Constant.loadImageFromStorage(notuploaded.get(i).getFaceimage(), notuploaded.get(i).getFaceimagename());
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                String face_image = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                Bitmap bitmap1 = Constant.loadImageFromStorage(notuploaded.get(i).getSignatureimage(), notuploaded.get(i).getSignatureimagename());
                ByteArrayOutputStream byteArrayOutputStream1 = new ByteArrayOutputStream();
                bitmap1.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream1);
                String sign_image = Base64.encodeToString(byteArrayOutputStream1.toByteArray(), Base64.DEFAULT);
                String[] fingerprintimage = notuploaded.get(i).getFingerimage().split(";");
                String[] fingerprintname = notuploaded.get(i).getFingerimagename().split(";");
                List<String> finger_image = new ArrayList<>();
                for (int a = 0; a < fingerprintname.length; a++) {
                    Bitmap bitmap2 = Constant.loadImageFromStorage(fingerprintimage[i], fingerprintname[i]);
                    ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
                    bitmap2.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream2);
                    finger_image.add(Base64.encodeToString(byteArrayOutputStream2.toByteArray(), Base64.DEFAULT));
                }
//                Log.d(TAG, "onReceive: "+msg_from + " "+ msgBody);
                int finalI = i;
                new PostRequest() {
                    /* class com.ugswitch.simhost.service.MyNotificationService.AnonymousClass1 */

                    @Override // com.ugswitch.simhost.request.PostRequest
                    public void onError(String str) {
                        Log.d("TAG", "onError: " + str);
//                        uploaddata();
                    }

                    @Override // com.ugswitch.simhost.request.PostRequest
                    public void onSuccess(String str) {
//                            databasehelper.updatesync(databasehelper.COLUMN_SYNC_DATE, "1", String.valueOf(notuploaded.get(finalI).getId()));
//                            uploading = true;
                        Log.d("TAG", "onSuccess: " + str);
                        if (Constant.logstatus(str).equals("true")) {
//                                uploadfingerprint(finalI);
                            Constant.errortoast(getApplicationContext(), "NO of notuploaded "+notuploaded.size());
                            Log.d("TAG", "onSuccess: " + String.valueOf(notuploaded.get(finalI).getId()));
                            databasehelper.updatesync(databasehelper.COLUMN_UPLOADSTATUS, "1", String.valueOf(notuploaded.get(finalI).getTicketid()));
                        }
                    }
                }.sendIncomingSmS(ticketID, title, surname, middle_name, first_name, gender,
                        date_of_birth, marital_status, nationality, state_of_origin, lga_of_origin, state_of_capture,
                        lga_of_capture, nin, residential_address, state_of_residence, lga_of_residence, landmarks, email,
                        phone_number_1, phone_number_2, sign_image, face_image, finger_image, bankname, getApplicationContext());
            }
            if (notuploaded.isEmpty()){
                getrequest();
            }
        } catch (Exception e) {
            Log.d("Exception caught",e.getMessage());
        }
    }
    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }

    void uploadfingerprint(int i){
        Log.d("tag", "onStartJob: tolubobo fingerprint");
        String ticketID = notuploaded.get(i).getTicketid();
        String[] fingerprintimage = notuploaded.get(i).getFingerimage().split(";");
        String[] fingerprintname = notuploaded.get(i).getFingerimagename().split(";");
        List<String> finger_image = new ArrayList<>();
            for (int a = 0; a < fingerprintname.length; a++) {
                Bitmap bitmap1 = Constant.loadImageFromStorage(fingerprintimage[i], fingerprintname[i]);
                ByteArrayOutputStream byteArrayOutputStream1 = new ByteArrayOutputStream();
                bitmap1.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream1);
                finger_image.add(Base64.encodeToString(byteArrayOutputStream1.toByteArray(), Base64.DEFAULT));
            }

        new PostRequest() {

            @Override
            public void onError(String str) {
                Log.d("TAG", "onError: " + str);
            }

            @Override
            public void onSuccess(String str) {
                Log.d("TAG", "onSuccess: " + str);
                if (Constant.logstatus(str).equals("true")) {
                    Constant.errortoast(getApplicationContext(), "NO of notuploaded "+notuploaded.size());
                    Log.d("TAG", "onSuccess: " + String.valueOf(notuploaded.get(i).getId()));
                    databasehelper.updatesync(databasehelper.COLUMN_UPLOADSTATUS, "1", String.valueOf(notuploaded.get(i).getTicketid()));

                }
            }
        }.sendfingerprint(ticketID, finger_image, getApplicationContext());
    }
    void getrequest(){
        List<Datamodel> notuploaded1 = databasehelper.getsync();
        Log.d("tag", "onStartJob: tolubobo");
        try{
            for(int i = 0; i<notuploaded1.size(); i++) {
                String ticketID = notuploaded1.get(i).getTicketid();
//                Log.d(TAG, "onReceive: "+msg_from + " "+ msgBody);
                int finalI = i;
                new GetRequest() {

                    @Override
                    public void onError(String str) {

                    }

                    @Override
                    public void onSuccess(String str) {
                        databasehelper.updatesync(databasehelper.COLUMN_SYNC_DATE, "1", String.valueOf(notuploaded1.get(finalI).getId()));
                        Log.d("TAG", "onSuccess: " + str);

                    }


                    @RequiresApi(Build.VERSION_CODES.O)
                    public void onSuccess(JSONObject jSONObject) throws JSONException {
                        Log.i("TAG", jSONObject.get("status").toString());
                        if (jSONObject.get("status").equals(1)) {
                            databasehelper.updatesync(databasehelper.COLUMN_STATE_OF_SYNC, "1", String.valueOf(ticketID));
                            databasehelper.updatesync(databasehelper.COLUMN_SYNC_DATE, new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date()), String.valueOf(ticketID));

                        }
                    }
                }.requestDisplay(ticketID,getApplicationContext());
            }
        }catch(Exception e){
                            Log.d("Exception caught",e.getMessage());
        }

        Util.scheduleJob(getApplicationContext(), Long.parseLong("200"));
    }
}