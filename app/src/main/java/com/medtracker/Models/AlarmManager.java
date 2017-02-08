package com.medtracker.Models;

/**
 * Created by spt10 on 08/02/2017.
 */

public class AlarmManager {
    private String medicationKey;
    private boolean has_alarm;
    private String currentAlarm;
    private int maxCount;
    private int currentCount;

    public AlarmManager() {

    }

    public AlarmManager(String medicationKey, boolean has_alarm, String currentAlarm, int maxCount, int currentCount) {
        this.medicationKey = medicationKey;
        this.has_alarm = has_alarm;
        this.currentAlarm = currentAlarm;
        this.maxCount = maxCount;
        this.currentCount = currentCount;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    public String getMedicationKey() {
        return medicationKey;
    }

    public void setMedicationKey(String medicationKey) {
        this.medicationKey = medicationKey;
    }

    public boolean isHas_alarm() {
        return has_alarm;
    }

    public void setHas_alarm(boolean has_alarm) {
        this.has_alarm = has_alarm;
    }

    public String getCurrentAlarm() {
        return currentAlarm;
    }

    public void setCurrentAlarm(String currentAlarm) {
        this.currentAlarm = currentAlarm;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }



}
