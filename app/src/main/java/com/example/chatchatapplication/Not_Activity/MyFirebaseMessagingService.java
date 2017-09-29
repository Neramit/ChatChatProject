package com.example.chatchatapplication.Not_Activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.chatchatapplication.Activity.FriendChatroom;
import com.example.chatchatapplication.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Neramit777 on 9/22/2017 at 3:15 PM.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */

    SharedPreferences sp;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Map<String, String> data = remoteMessage.getData();

        sendNotification(notification, data);
    }

    /**
     * Create and show a custom notification containing the received FCM message.
     *
     * @param notification FCM notification payload received.
     * @param data         FCM data payload received.
     */
    private void sendNotification(RemoteMessage.Notification notification, Map<String, String> data) {
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.message);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        // hogyfuho;hoigyu
        Intent intent = new Intent(this, FriendChatroom.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

//        String decrypted = Encoder.BuilderAES()
//                .message(notification.getBody())
//                .method(AES.Method.AES_CBC_PKCS5PADDING)
//                .key("mit&24737")
//                .keySize(AES.Key.SIZE_128)
//                .iVector(notification.getTitle())
//                .decrypt();

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(notification.getTitle())
                .setContentText("You have a message")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setContentInfo(notification.getTitle())
                .setLargeIcon(icon)
                .setColor(getColor(R.color.colorPrimary))
                .setSmallIcon(R.drawable.message);

        try {
            String picture_url = data.get("picture_url");
            if (picture_url != null && !"".equals(picture_url)) {
                URL url = new URL(picture_url);
                Bitmap bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                notificationBuilder.setStyle(
                        new NotificationCompat.BigPictureStyle().bigPicture(bigPicture).setSummaryText(notification.getBody())
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("NOTIFICATION" , sp.getString("vibrate_enable", "enable"));

        if (Objects.equals(sp.getString("vibrate_enable", "enable"), "enable"))
            notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        notificationBuilder.setLights(Color.YELLOW, 1000, 300);

        if (Objects.equals(sp.getString("sound_enable", "enable"), "enable")) {
            if (sp.getInt("music", 1) == 1)
                notificationBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            else {
                notificationBuilder.setSound(Uri.parse("android.resource://com.example.chatchatapplication/" + R.raw.ding));
//                notificationBuilder.setSound(RingtoneManager.getRingtone(this, uri));
            }
        }

        if (Objects.equals(sp.getString("notification_enable", "enable"), "enable")) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());
        }
    }
}
