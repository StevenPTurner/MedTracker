package com.medtracker.Models;

/**
 * Created by spt10 on 11/02/2017.
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

    public Record() {

    }

    public Record(String medication_key, int dose, int second, int minute, int hour, int day, int month, int year) {
        this.medication_key = medication_key;
        this.dose = dose;
        this.second = second;
        this.minute = minute;
        this.hour = hour;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getMedication_key() {
        return medication_key;
    }

    public void setMedication_key(String medication_key) {
        this.medication_key = medication_key;
    }

    public int getDose() {
        return dose;
    }

    public void setDose(int dose) {
        this.dose = dose;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}

