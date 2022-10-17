package com.example.OneBlood.Models;

public class EventBooking {
    private String mId;
    private String slot;
    private String locationName;
    private String date;
    private String user;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
