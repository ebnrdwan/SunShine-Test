//  TODO (1) Create a class called SunshineSyncTask
package com.example.android.sunshine.sync;

import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;

import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class SunshineSyncTask {

    public static String NOTIFICATION_SYNCED="sync-weather";
    public static String NOTIFICATION_DISMISS ="dismiss-weather";
    public static String SCHEDULE_NOTIFICATION="scheduled-weather";

    synchronized public static void syncWeather(Context context) {


        try {
            URL weatherUrl = NetworkUtils.getUrl(context);

            String jsonResponse = NetworkUtils.getResponseFromHttpUrl(weatherUrl);

            ContentValues[] valuesWeather = OpenWeatherJsonUtils.getWeatherContentValuesFromJson(context, jsonResponse);

            if (valuesWeather != null || valuesWeather.length > 0) {
                context.getContentResolver().delete(WeatherContract.WeatherEntry.CONTENT_URI, null, null);

                context.getContentResolver().bulkInsert(WeatherContract.WeatherEntry.CONTENT_URI, valuesWeather);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void ExecuteNotification (Context context,String action){
        if (!action.equals(null)) {


            if (action.equals(NOTIFICATION_SYNCED)){
                syncWeather(context);
            }
            else if (action.equals(NOTIFICATION_DISMISS)){
                ClearNotification(context);
            }
            else {
                return;
            }
        }




    }

    private static void ClearNotification(Context context){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }


}
//  TODO (2) Within SunshineSyncTask, create a synchronized public static void method called syncWeather
//      TODO (3) Within syncWeather, fetch new weather data
//      TODO(4) If we have valid results, delete the old data and insert the new