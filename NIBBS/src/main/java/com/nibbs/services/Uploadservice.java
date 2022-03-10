package com.nibbs.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.nibbs.Constant;
import com.nibbs.database.Databasehelper;
import com.nibbs.database.Datamodel;
import com.nibbs.request.PostRequest;
import com.nibbs.volley.InitiateVolley;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class Uploadservice extends JobService {
    Databasehelper databasehelper;
    @Override
    public boolean onStartJob(JobParameters params) {
        // Write your code here
        databasehelper = new Databasehelper(InitiateVolley.getInstance());
        List<Datamodel> notuploaded = databasehelper.getnotupload();
        Util.scheduleJob(getApplicationContext());
        Log.d("tag", "onStartJob: this is where we are");
        try{
            for(int i = 0; i<notuploaded.size(); i++){
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
                String[] fingerprintimage = notuploaded.get(i).getFingerimage().split(";");
                String[] fingerprintname = notuploaded.get(i).getFingerimagename().split(";");
                String finger_image = "";
                for (int a = 0; a <fingerprintname.length; a++ ){
                    Bitmap bitmap1 = Constant.loadImageFromStorage(notuploaded.get(i).getFaceimage(), notuploaded.get(i).getFaceimagename());
                    ByteArrayOutputStream byteArrayOutputStream1 = new ByteArrayOutputStream();
                    bitmap1.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream1);
                    if (finger_image.isEmpty()){
                        finger_image = Base64.encodeToString(byteArrayOutputStream1.toByteArray(), Base64.DEFAULT);
                    }else{
                        finger_image = ";"+Base64.encodeToString(byteArrayOutputStream1.toByteArray(), Base64.DEFAULT);
                    }
                }

//                Log.d(TAG, "onReceive: "+msg_from + " "+ msgBody);
                int finalI = i;
                new PostRequest() {
                    /* class com.ugswitch.simhost.service.MyNotificationService.AnonymousClass1 */

                    @Override // com.ugswitch.simhost.request.PostRequest
                    public void onError(String str) {
                        Log.d("TAG", "onError: " + str);
                    }

                    @Override // com.ugswitch.simhost.request.PostRequest
                    public void onSuccess(String str) {
                        databasehelper.updatesync(databasehelper.COLUMN_SYNC_DATE,"1",String.valueOf(notuploaded.get(finalI).getId()));

                        Log.d("TAG", "onSuccess: "+str);
//                        if (myJson.logstatus(str).equals("1")) {
//                            Config.savedProcess(InitiateVolley.getInstance(), "INBOX", "Sender: " + msg_from, "Message: " + msgBody, " ", "not-seen",timestamp);
//                            Intent intent = new Intent(Config.PUSH_NOTIFICATION);
//                            intent.putExtra("message", "");
//                            intent.putExtra("id", "");
//                            LocalBroadcastManager.getInstance(InitiateVolley.getInstance()).sendBroadcast(intent);
//                        }
                    }
                }.sendIncomingSmS(ticketID,title, surname,middle_name,first_name,gender,
                        date_of_birth,marital_status,nationality,state_of_origin,lga_of_origin,state_of_capture,
                        lga_of_capture,nin,residential_address,state_of_residence,lga_of_residence,landmarks,email,
                        phone_number_1,phone_number_2,face_image,finger_image);
            }
        }catch(Exception e){
//                            Log.d("Exception caught",e.getMessage());
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }

    void getrequest(Context context){
//        new GetRequest() {
//            /* class com.ugswitch.simhost.service.MyNotificationService.AnonymousClass4 */
//            // com.ugswitch.simhost.request.GetRequest
//            public void onError(String str) {
////                Util.schedulersendsmsJob(context, false);
//                Log.d("GETDATA1", str);
//            }
//
//            // com.simhost.request.GetRequest
//            public void onSuccess(String str) {
////                Util.schedulersendsmsJob(context,false);
//                Log.d("GETDATA2", str);
//                //                Intent i = new Intent(getActivity(), MapsActivityCurrentPlace.class);
////                i.putExtra("lat", adapter.getSelected().getimage1());
////                i.putExtra("lng", adapter.getSelected().getTitle());
////                startActivity(i);
//            }
//
//            // com.simhost.request.GetRequest
//            @RequiresApi(Build.VERSION_CODES.O)
//            public void onSuccess(JSONObject jSONObject) throws JSONException {
//                Log.i("TAG", jSONObject.get("status").toString());
////                Util.schedulersendsmsJob(context,false);
//
//                if (jSONObject.get("status").equals(1)){
////                    myJson.data_id =jSONObject.get("data_id").toString();
////                    String simslot =jSONObject.get("data_sim");
////                    String phone =jSONObject.get("data_phone").toString();
////                    String message =jSONObject.get("data_message").toString();
//
////                    sendSMS(phone,message,simslot);
//                }
//            }
//        }.requestcontact(context, "getSMS");

    }
}