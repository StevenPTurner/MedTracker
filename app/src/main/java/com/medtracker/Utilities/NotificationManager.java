package com.medtracker.Utilities;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import com.medtracker.Activities.TakeDoseActivity;
import com.medtracker.Models.Alarm;
import com.medtracker.medtracker.R;

import java.util.Calendar;

/**
 * Links of websites used
 * http://www.vogella.com/tutorials/AndroidNotifications/article.html
 * https://gist.github.com/BrandonSmith/6679223
 */

public class NotificationManager {
    public static final String TAG = LogTag.notificationManager;

    //used to create a notification object and it's intent
    private static Notification getNotification(String content, Context context, String alarmKey,
                                               String medicationKey) {
        Log.d(TAG, medicationKey);
        Intent intent = new Intent(context, TakeDoseActivity.class);
        intent.putExtra("alarmKey", alarmKey);
        intent.putExtra("medicationKey", medicationKey);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        return Factory.notification(context,"MedTracker Alert!", content,pendingIntent);
    }

    //used to set up alarm manager service and add notification to the notification queue
    private static void scheduleNotification(Notification notification, int id, Context context,
                                            Calendar calendar) {
        Intent notificationIntent = new Intent(context, NotificationReceiver.class);
        notificationIntent.putExtra(NotificationReceiver.NOTIFICATION_ID, id);
        notificationIntent.putExtra(NotificationReceiver.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(),pendingIntent);
    }

    //used to manage the setting up of a notification to the system services
    public static void enableNextAlarm(Alarm alarm, Context context) {
        String medicationName = Utility.keyToName(alarm.getMedication_key());
        String alarmKey = alarm.getMedication_key() + "_" + alarm.getId();
        String message = "Take a dose of " + medicationName;
        Calendar currentAlarm = Utility.alarmToCalendar(alarm);

        Notification notification = getNotification(message, context, alarmKey,
                alarm.getMedication_key());
        scheduleNotification(notification, alarm.getRCID(), context, currentAlarm);
    }

    public static void cancelNotification(){

    }
}
