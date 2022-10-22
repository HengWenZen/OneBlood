package com.example.OneBlood.Models;

public class ViewBloodRequest {
    private String mID;
    private String mTitle;
    private String mBloodType;
    private String mLocation;
    private String mDescription;
    private String mPostedBy;
    private String mContact;
    private String mDate;

    public ViewBloodRequest(String ID, String title, String bloodType, String location, String description, String postedBy, String contact, String date) {
        mID = ID;
        mTitle = title;
        mBloodType = bloodType;
        mLocation = location;
        mDescription = description;
        mPostedBy = postedBy;
        mContact = contact;
        mDate = date;
    }

    public String getID() {
        return mID;
    }

    public void setID(String ID) {
        mID = ID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getBloodType() {
        return mBloodType;
    }

    public void setBloodType(String bloodType) {
        mBloodType = bloodType;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getPostedBy() {
        return mPostedBy;
    }

    public void setPostedBy(String postedBy) {
        mPostedBy = postedBy;
    }

    public String getContact() {
        return mContact;
    }

    public void setContact(String contact) {
        mContact = contact;
    }


    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }
}
