package com.medtracker.Utilities;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.medtracker.Models.Alarm;
import com.medtracker.Models.AlarmManager;
import com.medtracker.Models.Medication;
import com.medtracker.medtracker.R;

import java.util.Calendar;

/**
 * Created by home on 13/04/2017.
 */

public class Factory {

    public static Medication medication(String medicationName, String dose, String instructions) {
        int medicationDoseValue = Integer.parseInt(dose);
        //Log.d(TAG, "Medication has been built");
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

        return new Alarm(id, minute, hour, currentDay, currentMonth, currentYear, medicationKey, RCID);
    }

    public static AlarmManager alarmManager(String medicationKey) {
        return new AlarmManager(medicationKey, false, "none", 0, 0);
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
        return builder.build();
    }
}
