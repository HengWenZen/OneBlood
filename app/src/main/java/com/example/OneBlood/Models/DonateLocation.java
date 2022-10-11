package com.example.OneBlood.Models;

import java.util.UUID;

public class DonateLocation {

    private String mID;
    private String mTitle;
    private String mLatitude;
    private String mLongitude;
    private String mAddress;
    private String mContact;
    private String mOperationHrs;
    private String mImageUrl;

    public String getID() {
        return mID;
    }

    public void setID(String ID) {
        this.mID = ID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getLatitude() {
        return mLatitude;
    }

    public void setLatitude(String latitude) {
        this.mLatitude = latitude;
    }

    public String getLongitude() {
        return mLongitude;
    }

    public void setLongitude(String longitude) {
        this.mLongitude = longitude;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        this.mAddress = address;
    }

    public String getContact() {
        return mContact;
    }

    public void setContact(String contact) {
        this.mContact = contact;
    }

    public String getOperationHrs() {
        return mOperationHrs;
    }

    public void setOperationHrs(String operationHrs) {
        this.mOperationHrs = operationHrs;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageId) {
        this.mImageUrl = imageId;
    }
}
