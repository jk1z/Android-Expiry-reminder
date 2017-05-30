package edu.monash.fit3027.fit3027_final_application;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import static android.content.Context.NOTIFICATION_SERVICE;


/**
 * Created by Jack on 23/5/17.
 */

public class NotifyHelper extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Intent newIntent = new Intent(context,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0 ,newIntent,0);

        Notification mNotify = new Notification.Builder(context)
                .setContentTitle("Expiry Reminder")
                .setContentText("Your item is about to expire")
                .setSmallIcon(R.drawable.ic_date_range_black_24dp)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(1,mNotify);
    }
}

