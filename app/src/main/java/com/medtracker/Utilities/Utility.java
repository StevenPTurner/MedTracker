package com.medtracker.Utilities;

import com.medtracker.Models.Alarm;
import com.medtracker.Models.Record;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Used as a bunch of utility methods for purposes such as converting times
 */

public class Utility {

    //converts names (Sodium Valproate) to medication keys (sodium_valproate)
    public static String nameToKey(String name) {
        String key = name.toLowerCase();
        key = key.replaceAll(" ","_");
        return key;
    }

    //converts keys (sodium_valproate) to medication names (Sodium Valproate)
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

    //Converts alarm objects into alarm objects for time manipulation
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

    //Converts records to calendar objects for time manipulation
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

    //Coverts alarm to record for saving to database
    public static Record alarmToRecord(Alarm alarm, int dose) {
        Calendar calendar = Calendar.getInstance();
        final long tenMinutes = 600000; //used to determine if the dose was missed
        final long oneHour = 3600000;   //used to determine if the dose was late
        final long alarmTime = alarmToCalendar(alarm).getTimeInMillis();
        final long currentTime = calendar.getTimeInMillis();
        boolean late = false;
        boolean missed = false;

        //works out if a dose is either, missed, late or on time
        if(isTimeLongerThan(oneHour, alarmTime, currentTime)) {
            missed = true;
        } else if ( isTimeLongerThan(tenMinutes, alarmTime, currentTime)) {
            late = true;
        }

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        String medicationKey = alarm.getMedication_key();
        int minute = calendar.get(Calendar.MINUTE);
        int month = (calendar.get(Calendar.MONTH) + 1);
        int year = calendar.get(Calendar.YEAR);

        return Factory.record(medicationKey, dose, minute, hour, day, month, year, late, missed);
    }

    //Converts a calendar object to a displayable string
    public static String calendarToString(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm EEE dd MMM");
        return sdf.format(calendar.getTime());
    }

    //converts a calendar object to a displayable time only
    public static String calendarToTime(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String formattedDate = sdf.format(calendar.getTime());
        return formattedDate;
    }

    //converts a calendar object to a displayable date only
    public static String calendarToDate(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMM");
        return sdf.format(calendar.getTime());
    }

    //calculates the difference in time(in milli seconds) between a calendar object and current time
    public static long calcTimeDiff(Calendar calendar) {
        long startTime = calendar.getTimeInMillis();
        long currentTime = Calendar.getInstance().getTimeInMillis();
        return (startTime - currentTime);
    }

    //returns true or false if the final time is longer than the speicified time diff
    public static boolean isTimeLongerThan(long timeDiff, long initalTime, long finalTime) {
        return ((finalTime - initalTime) >= timeDiff);
    }
}
