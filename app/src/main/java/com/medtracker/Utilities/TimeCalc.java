package com.medtracker.Utilities;

import java.util.Calendar;

/**
 *  Used to preform calculations with time
 */

public class TimeCalc {

    //calculates the difference in time(in milli seconds) between a calendar object and current time
    public static long calcTimeDiff(Calendar calendar) {
        long startTime = calendar.getTimeInMillis();
        long currentTime = Calendar.getInstance().getTimeInMillis();
        return (startTime - currentTime);
    }

    //returns true or false if the final time is longer than the specified time diff
    public static boolean isTimeLongerThan(long timeDiff, long initalTime, long finalTime) {
        return ((finalTime - initalTime) >= timeDiff);
    }
}
