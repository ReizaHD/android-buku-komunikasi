package com.bintangjuara.bk;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bintangjuara.bk.activities.MainActivity;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;

public class WebSocketService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        Intent intent1 = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent1, PendingIntent.FLAG_IMMUTABLE
        );

        Notification notification = new NotificationCompat.Builder(this, "ChannelId1")
                .setContentTitle("Buku Komunikasi")
                .setContentText("Notifikasi Aktif")
                .setSmallIcon(R.drawable.logo)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
        return START_STICKY;
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(
                    "ChannelId1", "Foreground notification", NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class MyWebSocketClient extends WebSocketClient {
        private Context ctx;
        private NotificationManager notificationManager;

        public MyWebSocketClient(URI serverUri, Context ctx) {
            super(serverUri);
            this.ctx = ctx;
            this.notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        @Override
        public void onOpen(ServerHandshake handshakedata) {
            Log.d("Websocket", "Opened");
        }

        @Override
        public void onMessage(String message) {
            Log.d("Websocket", message);
            String textMessage;
            try {
                JSONObject jsonObject = new JSONObject(message);
                textMessage = jsonObject.getString("message");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            Log.d("Websocket", "Recieved message : "+ textMessage);
            sendNotification(textMessage);
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            Log.d("WebSocket", reason);
        }

        @Override
        public void onError(Exception ex) {
            Log.e("WebSocket", ex.getMessage());
        }

        private void sendNotification(String message) {
            String channelId = "ChannelId1"; // Make sure this matches the channel ID in your service
            Notification notification = new NotificationCompat.Builder(ctx, channelId)
                    .setContentTitle("New message")
                    .setContentText(message)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)
                    .build();

            notificationManager.notify((int) System.currentTimeMillis(), notification);
        }
    }
}
