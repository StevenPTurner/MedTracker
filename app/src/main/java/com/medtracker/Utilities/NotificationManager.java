package com.medtracker.Utilities;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.medtracker.Models.Alarm;
import com.medtracker.medtracker.R;

import java.util.Calendar;

/**
 * Created by spt10 on 19/02/2017.
 */

public class NotificationManager {

    public static Notification getNotification(String content, Context context) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("MedTracker Alert!");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_pill_black_48dp);
        return builder.build();
    }

    public static void scheduleNotification(Notification notification, int id, Context context,
                                            Calendar calendar) {
        Intent notificationIntent = new Intent(context, NotificationReceiver.class);
        notificationIntent.putExtra(NotificationReceiver.NOTIFICATION_ID, id);
        notificationIntent.putExtra(NotificationReceiver.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(),pendingIntent);
        //alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    public static void enableNextAlarm(Alarm alarm, Context context) {
        String medicationName = Utility.keyToName(alarm.getMedication_key());
        Calendar currentAlarm = Utility.alarmToCalendar(alarm);
        Notification notification = getNotification("Take a dose of " + medicationName, context);

    }

    public static void cancleNotification(){

    }
}
