package com.example.OneBlood.Models;

public class Donor {
    String mName;
    String mId;
    String mContact;
    String mEmail;
    String mBloodType;
    String mUserStatus;
    Boolean isDonor;

    public Donor(String id, String name, String contact, String email, String bloodType, String userStatus) {
        mId = id;
        mName = name;
        mContact = contact;
        mEmail = email;
        mBloodType = bloodType;
        mUserStatus = userStatus;
    }

    public Boolean getDonor() {
        return isDonor;
    }

    public void setDonor(Boolean donor) {
        isDonor = donor;
    }

    public String getBloodType() {
        return mBloodType;
    }

    public void setBloodType(String bloodType) {
        mBloodType = bloodType;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getContact() {
        return mContact;
    }

    public void setContact(String contact) {
        mContact = contact;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getUserStatus() {
        return mUserStatus;
    }

    public void setUserStatus(String userStatus) {
        this.mUserStatus = userStatus;
    }
}
