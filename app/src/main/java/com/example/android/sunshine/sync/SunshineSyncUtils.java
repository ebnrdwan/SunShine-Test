package com.example.android.sunshine.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;

import com.example.android.sunshine.data.WeatherContract;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

public class SunshineSyncUtils {
    private static final int SUNSHINE_REMINDER_INTERVAL_MINUTES = 1;
    private static final int SUNSHINE_REMINDER_INTERVAL_SECONDS = (int) (TimeUnit.MINUTES.toSeconds(SUNSHINE_REMINDER_INTERVAL_MINUTES));
    private static final int SUNSHINE_SYNC_FLEXTIME_SECONDS = SUNSHINE_REMINDER_INTERVAL_SECONDS;

    private static final String SUNSHINE_JOB_TAG = "sunshine_reminder_tag";
    public static boolean Sinitialized = false;

    public static void startImmediateSync(Context context) {
        Intent sunshineSyncIntentService = new Intent(context, SunshineSyncIntentService.class);
//        Toast.makeText(context, "start getting your data synced", Toast.LENGTH_SHORT).show();
        context.startService(sunshineSyncIntentService);
    }

    public static void initialized(final Context context) {
        if (Sinitialized == false) {
            AsyncTask task = new AsyncTask() {

                @Override
                protected Object doInBackground(Object[] objects) {
                    Cursor cursor = context.getContentResolver().query(WeatherContract.WeatherEntry.CONTENT_URI, null, null, null, null);
                    int i = cursor.getCount();
                    if (i > 0) {
                        startImmediateSync(context);
                    }

                    cursor.close();
                    return null;
                }
            };
            task.execute();
            JobDispatcherBuilder(context);
            Sinitialized = true;
        }
    }

    synchronized static void JobDispatcherBuilder(Context context) {

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        Job job = dispatcher.newJobBuilder()
                .setService(SunshineFirebaseDispatcher.class)
                .setTag(SUNSHINE_JOB_TAG)
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                .setConstraints(Constraint.ON_UNMETERED_NETWORK)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(SUNSHINE_REMINDER_INTERVAL_SECONDS, SUNSHINE_REMINDER_INTERVAL_SECONDS + SUNSHINE_SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();
        dispatcher.schedule(job);
    }
}


// TODO (9) Create a class called SunshineSyncUtils
//  TODO (10) Create a public static void method called startImmediateSync
//  TODO (11) Within that method, start the SunshineSyncIntentService