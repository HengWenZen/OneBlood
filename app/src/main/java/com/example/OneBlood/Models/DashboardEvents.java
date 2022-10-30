package com.example.OneBlood.Models;

public class DashboardEvents {

    String eventTitle;
    String eventId;
    String eventDate;
    String eventTime;

    public DashboardEvents(String eventTitle, String eventId, String eventDate, String eventTime) {
        this.eventTitle = eventTitle;
        this.eventId = eventId;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }
}
