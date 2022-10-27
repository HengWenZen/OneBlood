package com.example.OneBlood.Models;

public class Notice {
    String mDescription, mTitle, mHospitalName, mDate, mId;

    public Notice(String id, String title, String description, String hospitalName, String date) {
        mId = id;
        mTitle = title;
        mDescription = description;
        mHospitalName = hospitalName;
        mDate = date;
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
}
