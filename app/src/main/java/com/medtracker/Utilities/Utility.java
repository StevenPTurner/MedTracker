package com.medtracker.Utilities;

import android.util.Log;

import com.medtracker.Models.Alarm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by spt10 on 01/02/2017.
 */

public class Utility {

    public static String nameToKey(String name) {
        String key = name.toLowerCase();
        key = key.replaceAll(" ","_");

        return key;
    }

    public static String keyToName(String key) {
        String name = "";
        String[] toUpCase = key.split("_");

        for (int i=0; i< toUpCase.length; i++) {
            name = name +  toUpCase[i].substring(0,1).toUpperCase() +
                    toUpCase[i].substring(1).toLowerCase() +
                    " ";
        }
        name = name.trim();
        return name;
    }

    public static Calendar alarmToCalendar(Alarm alarm) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, alarm.getMinute());
        calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
        calendar.set(Calendar.DAY_OF_MONTH, alarm.getDay());
        calendar.set(Calendar.MONTH, (alarm.getMonth()-1));
        calendar.set(Calendar.YEAR, alarm.getYear());
        return calendar;
    }

    public static String calendarToString(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm EEE dd MMM");
        String formattedDate = sdf.format(calendar.getTime());
        return formattedDate;
    }

    public static String calcTimeDif(Calendar alarm) {
        long alarmTime = alarm.getTimeInMillis();
        long currentTime = Calendar.getInstance().getTimeInMillis();
        long timeDiff = alarmTime - currentTime;

        long hours = TimeUnit.MILLISECONDS.toHours(timeDiff);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeDiff - (hours * 60 * 60 * 1000));

        String timeTillDose = Long.toString(hours) + " Hours & " + Long.toString(minutes) + " Minutes from now";
        return timeTillDose;
    }

}
