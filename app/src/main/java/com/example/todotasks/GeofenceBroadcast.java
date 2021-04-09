package com.example.todotasks;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.todotasks.ui.MainActivity;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import static android.content.Context.NOTIFICATION_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

public class GeofenceBroadcast extends BroadcastReceiver {
    private static final String PRIMARY_CHANNEL_ID = "channelId";
    private static final int NOTIFICATION_ID = 0;
    NotificationManager notificationManager;
    private static String Action = "com.example.android.todotasks.ACTION_UPDATE_NOTIFICATION";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("was called","from the notification");
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        if (event.hasError()) {
            Toast.makeText(context, "error in delivering", Toast.LENGTH_SHORT).show();
        }
        int transition = event.getGeofenceTransition();
        if (transition == Geofence.GEOFENCE_TRANSITION_ENTER || transition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            createNotification(context,intent);

        }

    }

    private NotificationCompat.Builder getNotificationBuilder(Context context, Intent intent) {
        Intent actionIntent = new Intent(Action);
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID);
        notifyBuilder.setContentText(intent.getStringExtra("Detail"))

                .setContentTitle(intent.getStringExtra("Title"))
                .setSmallIcon(R.drawable.ic_baseline_notifications_none_24);
        Intent contentIntent = new Intent(context, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notifyBuilder.setContentIntent(pendingIntent)
                .setAutoCancel(true);
        PendingIntent pndIntent = PendingIntent.getBroadcast(context, NOTIFICATION_ID, actionIntent, PendingIntent.FLAG_ONE_SHOT);

        return notifyBuilder;
    }




    private void createNotification(Context context, Intent intent){
        NotificationCompat.Builder builder = getNotificationBuilder(context,intent);

       notificationManager.notify(NOTIFICATION_ID,builder.build());
    }
}
