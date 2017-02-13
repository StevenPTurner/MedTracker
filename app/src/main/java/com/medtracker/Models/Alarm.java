package com.medtracker.Models;

/**
 * Created by spt10 on 08/02/2017.
 */

public class Alarm {
    private int minute;
    private int hour;
    private int day;
    private int month;
    private int year;
    private int id;
    private String medication_key;

    public Alarm() {

    }

    public Alarm(int id, int minute, int hour, int day, int month, int year, String medication_key) {
        this.id = id;
        this.minute = minute;
        this.hour = hour;
        this.day = day;
        this.month = month;
        this.year = year;
        this.medication_key = medication_key;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMedication_key() {
        return medication_key;
    }

    public void setMedication_key(String medication_key) {
        this.medication_key = medication_key;
    }

}
