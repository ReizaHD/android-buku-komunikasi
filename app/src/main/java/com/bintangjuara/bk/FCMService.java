package com.bintangjuara.bk;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.bintangjuara.bk.activities.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        Log.d("FCM Token", "Refreshed token: "+token);;
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        if(message.getNotification() != null){
            sendNotification(message.getNotification());
        }else {
            Log.d("FCM", "Message Null");
        }
        Log.d("FCM", "Message Received");
    }

    private void sendNotification(RemoteMessage.Notification message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "channel_id";
            String channelName = "Channel Name";
            NotificationChannel channel = new NotificationChannel(
                    channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "channel_id")
                .setContentTitle(message.getTitle())
                .setContentText(message.getBody())
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(pendingIntent);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
