package com.build1.rapepreventionapp.PushNotif;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.build1.rapepreventionapp.R;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by JEMYLA VELILLA on 11/02/2018.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService{

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String messageTitle = remoteMessage.getNotification().getTitle();
        String messageBody = remoteMessage.getNotification().getBody();

        String click_action = remoteMessage.getNotification().getClickAction();

        String dataMessage = remoteMessage.getData().get("message");
        String dataFrom = remoteMessage.getData().get("from_user_id");

        final String packageName = getPackageName();
        Uri sound = Uri.parse("android.resource://" + packageName + "R.raw.notification");

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                        .setSmallIcon(R.drawable.logotypebfinalfinal)
                        .setContentTitle(messageTitle)
                        .setSound(sound)
                        .setContentText(messageBody);

        Intent resultIntent = new Intent(click_action);
        resultIntent.putExtra("dataMessage", dataMessage);
        resultIntent.putExtra("dataFrom", dataFrom);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        int mNotificationId = (int) System.currentTimeMillis();

        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
}
