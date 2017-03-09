package com.medtracker.Models;

/**
 * Model for the record object
 */

public class Record {
    private String medication_key;
    private int dose;
    private int second;
    private int minute;
    private int hour;
    private int day;
    private int month;
    private int year;
    private boolean missed;
    private boolean late;

    public Record() {}

    public Record(String medication_key, int dose, int second, int minute, int hour, int day,
                  int month, int year, boolean missed, boolean late) {
        this.medication_key = medication_key;
        this.dose = dose;
        this.second = second;
        this.minute = minute;
        this.hour = hour;
        this.day = day;
        this.month = month;
        this.year = year;
        this.missed = missed;
        this.late = late;
    }

    public boolean isMissed() {
        return missed;
    }

    public boolean isLate() {
        return late;
    }

    public int getMonth() {
        return month;
    }

    public String getMedication_key() {
        return medication_key;
    }

    public int getDose() {
        return dose;
    }

    public int getSecond() {
        return second;
    }

    public int getMinute() {
        return minute;
    }

    public int getHour() {
        return hour;
    }

    public int getDay() {
        return day;
    }

    public int getYear() {
        return year;
    }

    public void setMissed(boolean missed) {
        this.missed = missed;
    }

    public void setLate(boolean late) {
        this.late = late;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setMedication_key(String medication_key) {
        this.medication_key = medication_key;
    }

    public void setDose(int dose) {
        this.dose = dose;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setYear(int year) {
        this.year = year;
    }
}

