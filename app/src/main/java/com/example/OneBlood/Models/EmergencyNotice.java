package com.example.OneBlood.Models;

public class EmergencyNotice {
    String mDescription, mTitle, mHospitalName, mDate, mId, requiredBloodType ,mContact ,mLocation;

    public EmergencyNotice(String description, String title, String hospitalName, String date, String id, String requiredBloodType, String contact, String location) {
        mDescription = description;
        mTitle = title;
        mHospitalName = hospitalName;
        mDate = date;
        mId = id;
        this.requiredBloodType = requiredBloodType;
        mContact = contact;
        mLocation = location;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getHospitalName() {
        return mHospitalName;
    }

    public void setHospitalName(String hospitalName) {
        mHospitalName = hospitalName;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getRequiredBloodType() {
        return requiredBloodType;
    }

    public void setRequiredBloodType(String requiredBloodType) {
        this.requiredBloodType = requiredBloodType;
    }

    public String getContact() {
        return mContact;
    }

    public void setContact(String contact) {
        mContact = contact;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }
}
