package edu.monash.fit3027.fit3027_final_application.Helper;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import edu.monash.fit3027.fit3027_final_application.R;
import edu.monash.fit3027.fit3027_final_application.UI.Activity.MainActivity;
import edu.monash.fit3027.fit3027_final_application.model.Item;

import static android.content.Context.NOTIFICATION_SERVICE;


/**
 * Created by Jack on 23/5/17.
 */

public class NotifyHelper extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Intent newIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, newIntent, 0);

        String itemName = intent.getStringExtra("itemName");
        int daysLeft = intent.getIntExtra("daysLeft",0);
        String daysLeftString;
        if (daysLeft < 0){
            daysLeftString = itemName + " is expired";
        }else if (daysLeft == 0){
            daysLeftString = itemName + " is expire today";
        }else{
            daysLeftString = itemName + " is about to expire in " + daysLeft + " days";
        }
        Notification mNotify = new Notification.Builder(context)
                .setContentTitle("Expiry Reminder")
                .setContentText(daysLeftString)
                .setSmallIcon(R.drawable.ic_date_range_black_24dp)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true)
                .setSound(sound)
                .build();
        notificationManager.notify(1, mNotify);
    }
}

