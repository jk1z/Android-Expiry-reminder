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
 * Created by YunHao Zhang
 * Student ID: 26956047
 * A customised notify helper for this application
 */

public class NotifyHelper extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);//Set the sound of notification

        //Get notification manager
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Intent newIntent = new Intent(context, MainActivity.class); //When user click the notification it will lead user to MainActivity
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, newIntent, 0);
        //Thought of passing item object, but according to the stack overflow, but sometimes the system will lost parcelable object
        //https://stackoverflow.com/questions/40480355/pass-serializable-object-to-pending-intent/40515978#40515978
        String itemName = intent.getStringExtra("itemName");
        int daysLeft = intent.getIntExtra("daysLeft",0);

        Notification mNotify = new Notification.Builder(context)
                .setContentTitle("Expiry Reminder")
                .setContentText(daysLeftToString(daysLeft,itemName))
                .setSmallIcon(R.drawable.ic_date_range_black_24dp)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true)
                .setSound(sound)
                .build();
        notificationManager.notify(1, mNotify);
    }

    /**
     * Turns daysLeft into readable string
     * @param daysLeft How many days left
     * @param itemName Item's name
     * @return A string
     */
    private String daysLeftToString(int daysLeft, String itemName){
        String daysLeftString;
        if (daysLeft < 0){
            daysLeftString = itemName + " is expired";
        }else if (daysLeft == 0){
            daysLeftString = itemName + " is expire today";
        }else{
            daysLeftString = itemName + " is about to expire in " + daysLeft + " days";
        }
        return daysLeftString;
    }
}

