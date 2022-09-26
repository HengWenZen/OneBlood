package com.example.OneBlood;

public class Donor {
    String mName;
    String mId;
    String mContact;
    String mEmail;
    String mBloodType;

    public Boolean getDonor() {
        return isDonor;
    }

    public void setDonor(Boolean donor) {
        isDonor = donor;
    }

    Boolean isDonor;

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
}
