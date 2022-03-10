package com.nibbs.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.nibbs.Constant;
import com.nibbs.volley.InitiateVolley;

public class MyStartServiceReceiver extends BroadcastReceiver {

    // initialize listener
    public static ReceiverListener Listener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Constant.toast(InitiateVolley.getInstance(),"no internet");
        Log.d("tolubobo", "onReceive: xup");
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Initialize network info
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // check condition
        if (Listener != null) {

            // when connectivity receiver
            // listener  not null
            // get connection status
            boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

            // call listener method
            Listener.onNetworkChange(isConnected);
        }
        Util.scheduleJob(context);
        Util.scheduleuploadedJob(context);
    }

    public interface ReceiverListener {
        // create method
        void onNetworkChange(boolean isConnected);
    }
}

