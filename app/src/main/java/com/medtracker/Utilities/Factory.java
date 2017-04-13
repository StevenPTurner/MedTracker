package com.medtracker.Utilities;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.medtracker.Models.Alarm;
import com.medtracker.Models.AlarmManager;
import com.medtracker.Models.Medication;
import com.medtracker.Models.Record;
import com.medtracker.Models.User;
import com.medtracker.medtracker.R;

import java.util.Calendar;

/**
 * Used to centralise the creation of objects for easy maintenance
 */

public class Factory {
    private final static String TAG = LogTag.factory;

    //creates a new user
    public static User user(String email, String displayName) {
        return new User(email, displayName, 1111);
    }

    public static Medication medication(String medicationName, String dose, String instructions) {
        int medicationDoseValue = Integer.parseInt(dose);
        Log.d(TAG, "A new medication has been built");
        return new Medication(instructions, medicationName, false, false, medicationDoseValue);
    }

    public static Alarm alarm(int id, int minute, int hour, String medicationKey, int RCID) {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentYear = calendar.get(Calendar.YEAR);

        if (currentHour > hour)
            currentDay = currentDay + 1;

        Log.d(TAG, "A new alarm has been built");
        return new Alarm(id, minute, hour, currentDay, currentMonth, currentYear, medicationKey, RCID);
    }

    public static AlarmManager alarmManager(String medicationKey) {
        Log.d(TAG, "A new alarm manager has been built");
        return new AlarmManager(medicationKey, false, "none", 0, 0);
    }

    public static Record record(String key, int dose, int minute, int hour, int day, int month,
                                int year, boolean late, boolean missed) {
        Log.d(TAG, "A new record has been built");
        return new Record(key, dose, 0, minute, hour, day, month, year, late, missed);
    }

    public static Notification notification(Context context, String title, String content,
                                            PendingIntent intent) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_pill_black_48dp);
        builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        builder.setLights(Color.RED, 3000, 3000);
        builder.setContentIntent(intent);
        Log.d(TAG, "A new notification has been built");
        return builder.build();
    }
}
