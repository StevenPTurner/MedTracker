package com.medtracker.Models;

/**
 * Model for the user object
 */

public class User {
    private String email;
    private String display_name;
    private int pin;

    public User() {}

    public User(String email, String display_name, int pin) {
        this.email = email;
        this.display_name = display_name;
        this.pin = pin;
    }

    public String getEmail() {
        return email;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public int getPin() {
        return pin;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }
}
