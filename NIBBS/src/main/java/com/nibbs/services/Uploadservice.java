package com.nibbs.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.camera.core.impl.Config;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.nibbs.Databasehelper;
import com.nibbs.Datamodel;
import com.nibbs.PreviewActivity;
import com.nibbs.request.GetRequest;
import com.nibbs.request.PostRequest;
import com.nibbs.volley.InitiateVolley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Uploadservice extends JobService {
    Databasehelper databasehelper;
    @Override
    public boolean onStartJob(JobParameters params) {
        // Write your code here
        databasehelper = new Databasehelper(InitiateVolley.getInstance());
        List<Datamodel> notuploaded = databasehelper.getnotupload();

        Log.d("tolubobo", "onStartJob: this is where we are");
        try{
            for(int i = 0; i<notuploaded.size(); i++){
//                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
//                msg_from = msgs[i].getOriginatingAddress();
//                msgBody = msgs[i].getMessageBody();
//                timestamp = msgs[i].getTimestampMillis();
//                Log.d(TAG, "onReceive: "+msg_from + " "+ msgBody);
                int finalI = i;
                new PostRequest() {
                    /* class com.ugswitch.simhost.service.MyNotificationService.AnonymousClass1 */

                    @Override // com.ugswitch.simhost.request.PostRequest
                    public void onError(String str) {
                    }

                    @Override // com.ugswitch.simhost.request.PostRequest
                    public void onSuccess(String str) {
                        databasehelper.updatesync(databasehelper.COLUMN_SYNC_DATE,"1",String.valueOf(notuploaded.get(finalI).getId()));
//                        Log.d(TAG, "onSuccess: "+str);
//                        if (myJson.logstatus(str).equals("1")) {
//                            Config.savedProcess(InitiateVolley.getInstance(), "INBOX", "Sender: " + msg_from, "Message: " + msgBody, " ", "not-seen",timestamp);
//                            Intent intent = new Intent(Config.PUSH_NOTIFICATION);
//                            intent.putExtra("message", "");
//                            intent.putExtra("id", "");
//                            LocalBroadcastManager.getInstance(InitiateVolley.getInstance()).sendBroadcast(intent);
//                        }
                    }
                }.sendIncomingSmS("msg_from", "msgBody","timestamp");
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