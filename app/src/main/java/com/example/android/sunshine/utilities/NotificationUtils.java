package com.example.android.sunshine.utilities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.NotificationCompat.Action;

import com.example.android.sunshine.DetailActivity;
import com.example.android.sunshine.R;
import com.example.android.sunshine.sync.SunshineSyncIntentService;
import com.example.android.sunshine.sync.SunshineSyncTask;

/**
 * Created by Abdulrhman on 04/04/2017.
 */

public class NotificationUtils {
    public static final int NOTIFICATION_ID = 1;
    public static final int PENDINGINTENT_CONTENT_ID = 1;
    public static final int SYNC_ACTION_REQUEST_CODE = 10;
    public static final int DISMISS_ACTION_REQUEST_CODE = 20;



    public static void WeatherNotificationBuilder(Context context) {
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setContentTitle(context.getString(R.string.NotificaionTitle))
                .setContentInfo(context.getString(R.string.NotificaionContent))
                .setContentIntent(contentIntent(context))
                .setDefaults(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .addAction(syncAction(context))
                .addAction(dismissAction(context))
                .setSmallIcon(R.drawable.art_clear)
                .setLargeIcon(getLargIcon(context))
                .setAutoCancel(true);

        NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID,notification.build());
    }

    private static Action syncAction(Context context) {
        Intent intent = new Intent(context, SunshineSyncIntentService.class);
        intent.setAction(SunshineSyncTask.NOTIFICATION_SYNCED);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, SYNC_ACTION_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Action action = new Action(R.drawable.sync, "Sync Now", pendingIntent);

        return action;
    }

    private static Action dismissAction(Context context) {
        Intent intent = new Intent(context, SunshineSyncIntentService.class);
        intent.setAction(SunshineSyncTask.NOTIFICATION_DISMISS);
        PendingIntent pendingIntent = PendingIntent.getService(context, DISMISS_ACTION_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Action action = new Action(R.drawable.cancel, "no, thanks", pendingIntent);
        return action;
    }

    private static PendingIntent contentIntent(Context context) {
        Intent intent = new Intent(context, DetailActivity.class);

        PendingIntent pendingIntent = PendingIntent.getService(context, PENDINGINTENT_CONTENT_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }


    private static Bitmap getLargIcon(Context context) {
        Resources resources = context.getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher);
        return bitmap;
    }



}
