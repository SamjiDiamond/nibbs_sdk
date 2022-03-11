package com.nibbssdk.services;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

public class Util {

    public static void scheduleJob(Context context,Long delay) {
        ComponentName serviceComponent = new ComponentName(context, Uploadservice.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(30 * delay); // Wait at least 30s
        builder.setOverrideDeadline(60 * delay); // Maximum delay 60s
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);  // require unmetered network
        builder.setRequiresCharging(false);  // we don't care if the device is charging or not
        builder.setRequiresDeviceIdle(true); // device should be idle
        System.out.println("scheduler Job");

        JobScheduler jobScheduler = (JobScheduler)context.getSystemService(context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
//        Util.scheduleuploadedJob(context,Long.parseLong("1000"));
    }
 public static void scheduleuploadedJob(Context context, Long delay) {
        ComponentName serviceComponent = new ComponentName(context, Confirmuploadservice.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(30 * delay); // Wait at least 30s
        builder.setOverrideDeadline(60 * delay); // Maximum delay 60s
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);  // require unmetered network
        builder.setRequiresCharging(false);  // we don't care if the device is charging or not
        builder.setRequiresDeviceIdle(true); // device should be idle
        System.out.println("scheduler Job");

        JobScheduler jobScheduler = (JobScheduler)context.getSystemService(context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
    }

}