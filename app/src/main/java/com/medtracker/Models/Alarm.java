package com.medtracker.Models;

/**
 * Model for alarm objects
 */

public class Alarm {
    private int minute;
    private int hour;
    private int day;
    private int month;
    private int year;
    private int id;
    private String medication_key;
    private int RCID;

    public Alarm() {}

    public Alarm(int id, int minute, int hour, int day, int month, int year, String medication_key,
                 int RCID) {
        this.id = id;
        this.minute = minute;
        this.hour = hour;
        this.day = day;
        this.month = month;
        this.year = year;
        this.medication_key = medication_key;
        this.RCID = RCID;
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

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getId() {
        return id;
    }

    public String getMedication_key() {
        return medication_key;
    }

    public int getRCID() {
        return RCID;
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

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMedication_key(String medication_key) {
        this.medication_key = medication_key;
    }

    public void setRCID(int RCID) {
        this.RCID = RCID;
    }

}
