package com.example.OneBlood.Models;

import java.util.Date;

public class EventTimeSlot {
    private String slot;
    private String location;
    private String user;
    private String date;
    private Date startTime;
    private Date endTime;


    public String getUser() {
            return user;
        }

    public void setUser(String user) {
            this.user = user;
        }

    public EventTimeSlot(){

    }
    public String getSlot() {
            return slot;
        }

    public void setSlot(String slot) {
            this.slot = slot;
        }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}


