package com.example.OneBlood.Models;

public class Events {

    String mStartDate, mEndDate, mLocation, imageURL, mId, mTitle, mDescription, mStartTime, mEndTime, mPostedBy;

    public Events(String startDate, String endDate, String location, String imageURL, String id, String title, String description, String startTime, String endTime, String postedBy) {
        mStartDate = startDate;
        mEndDate = endDate;
        mLocation = location;
        this.imageURL = imageURL;
        mId = id;
        mTitle = title;
        mDescription = description;
        mStartTime = startTime;
        mEndTime = endTime;
        mPostedBy = postedBy;
    }

    public String getStartDate() {
        return mStartDate;
    }

    public void setStartDate(String startDate) {
        mStartDate = startDate;
    }

    public String getEndDate() {
        return mEndDate;
    }

    public void setEndDate(String endDate) {
        mEndDate = endDate;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getStartTime() {
        return mStartTime;
    }

    public void setStartTime(String startTime) {
        mStartTime = startTime;
    }

    public String getEndTime() {
        return mEndTime;
    }

    public void setEndTime(String endTime) {
        mEndTime = endTime;
    }

    public String getPostedBy() {
        return mPostedBy;
    }

    public void setPostedBy(String postedBy) {
        mPostedBy = postedBy;
    }
}
