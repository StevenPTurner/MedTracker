package com.medtracker.Utilities;



import com.medtracker.Models.Alarm;
import com.medtracker.Models.Record;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    public static Calendar recordToCalendar(Record record) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, record.getMinute());
        calendar.set(Calendar.HOUR_OF_DAY, record.getHour());
        calendar.set(Calendar.DAY_OF_MONTH, record.getDay());
        calendar.set(Calendar.MONTH, (record.getMonth()-1));
        calendar.set(Calendar.YEAR, record.getYear());
        return calendar;
    }

    public static Record alarmToRecord(Alarm alarm, int dose) {
        int day = alarm.getDay();
        int hour = alarm.getHour();
        String medicationKey = alarm.getMedication_key();
        int minute = alarm.getMinute();
        int month = alarm.getMonth();
        int year = alarm.getYear();

        return new Record(medicationKey, dose, 0, minute, hour, day, month, year);
    }

    public static String calendarToString(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm EEE dd MMM");
        String formattedDate = sdf.format(calendar.getTime());
        return formattedDate;
    }

    public static String calendarToTime(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String formattedDate = sdf.format(calendar.getTime());
        return formattedDate;
    }

    public static String calendarToSDate(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMM");
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
