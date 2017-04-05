package com.example.android.sunshine.sync;

import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by Abdulrhman on 03/04/2017.
 */

public  class SunshineFirebaseDispatcher extends JobService {

AsyncTask asyncTask;
    @Override
    public boolean onStartJob(JobParameters job) {
      asyncTask=  new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                SunshineSyncTask.syncWeather(getBaseContext());
                return null;
            }
        };

        asyncTask.execute();
        jobFinished(job,false);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
if (asyncTask!=null)asyncTask.cancel(true);
        return false;
    }
}
