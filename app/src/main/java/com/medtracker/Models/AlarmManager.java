package com.medtracker.Models;

/**
 * Created by spt10 on 08/02/2017.
 */

public class AlarmManager {
    private String medication_key;
    private boolean has_alarm;
    private String current_alarm;
    private int max_count;
    private int current_count;

    public AlarmManager() {

    }

    public AlarmManager(String medication_key, boolean has_alarm, String current_alarm, int max_count, int current_count) {
        this.medication_key = medication_key;
        this.has_alarm = has_alarm;
        this.current_alarm = current_alarm;
        this.max_count = max_count;
        this.current_count = current_count;
    }

    public int getCurrent_count() {
        return current_count;
    }

    public void setCurrent_count(int current_count) {
        this.current_count = current_count;
    }

    public String getMedication_key() {
        return medication_key;
    }

    public void setMedication_key(String medication_key) {
        this.medication_key = medication_key;
    }

    public boolean isHas_alarm() {
        return has_alarm;
    }

    public void setHas_alarm(boolean has_alarm) {
        this.has_alarm = has_alarm;
    }

    public String getCurrent_alarm() {
        return current_alarm;
    }

    public void setCurrent_alarm(String current_alarm) {
        this.current_alarm = current_alarm;
    }

    public int getMax_count() {
        return max_count;
    }

    public void setMax_count(int max_count) {
        this.max_count = max_count;
    }



}
