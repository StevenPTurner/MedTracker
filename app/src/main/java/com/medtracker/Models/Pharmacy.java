package com.medtracker.Models;

/**
 * Model for pharmacy object
 */

public class Pharmacy {
    private double lat;
    private double lng;
    private String name;
    private String info;
    private boolean openNow;

    public Pharmacy() {}

    public Pharmacy(double lat, double lng, String name, String info, boolean openNow) {
        this.lat = lat;
        this.lng = lng;
        this.name = name;
        this.info = info;
        this.openNow = openNow;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public boolean isOpenNow() {
        return openNow;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }
}
