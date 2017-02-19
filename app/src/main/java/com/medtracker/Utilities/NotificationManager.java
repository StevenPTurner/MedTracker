package com.medtracker.Utilities;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import com.medtracker.Activities.TakeDoseActivity;
import com.medtracker.Models.Alarm;
import com.medtracker.medtracker.R;

import java.util.Calendar;

/**
 * Created by spt10 on 19/02/2017.
 * //http://www.vogella.com/tutorials/AndroidNotifications/article.html
 //https://gist.github.com/BrandonSmith/6679223
 */

public class NotificationManager {

    public static Notification getNotification(String content, Context context) {
        Intent myIntent = new Intent(context, TakeDoseActivity.class);
        PendingIntent intent2 = PendingIntent.getActivity(context, 1,
                myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("MedTracker Alert!");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_pill_black_48dp);
        builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        builder.setLights(Color.RED, 3000, 3000);
        builder.setContentIntent(intent2);
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
        scheduleNotification(notification, alarm.getRCID(), context, currentAlarm);
    }

    public static void cancleNotification(){

    }
}
