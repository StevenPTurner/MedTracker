package com.medtracker.models;

/**
 * Created by home on 01/12/2016.
 */

public class User {
    private String email;
    private String displayName;
    private int pin;

    public User() {
    }

    public User(String email, String displayName, int pin) {
        this.email = email;
        this.displayName = displayName;
        this.pin = pin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }
}
