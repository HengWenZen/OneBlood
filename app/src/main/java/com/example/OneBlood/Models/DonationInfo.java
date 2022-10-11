package com.example.OneBlood.Models;

public class DonationInfo {
    String infoTitle, infoDetails, infoDescription;
    private boolean Expandable;

    public DonationInfo(String infoTitle, String infoDescription) {
        this.infoTitle = infoTitle;
        this.infoDescription = infoDescription;
        this.Expandable = false;
    }

    public boolean isExpandable() {
        return Expandable;
    }

    public void setExpandable(boolean expandable) {
        Expandable = expandable;
    }

    public String getInfoTitle() {
        return infoTitle;
    }

    public void setInfoTitle(String infoTitle) {
        this.infoTitle = infoTitle;
    }

    public String getInfoDetails() {
        return infoDetails;
    }

    public void setInfoDetails(String infoDetails) {
        this.infoDetails = infoDetails;
    }

    public String getInfoDescription() {
        return infoDescription;
    }

    public void setInfoDescription(String infoDescription) {
        this.infoDescription = infoDescription;
    }

    @Override
    public String toString() {
        return "DonationInfo{" +
                "infoTitle='" + infoTitle + '\'' +
                ", infoDetails='" + infoDetails + '\'' +
                ", infoDescription='" + infoDescription + '\'' +
                '}';
    }
}
