package com.example.OneBlood;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.textfield.TextInputLayout;

public class ViewBloodRequestDetails extends AppCompatActivity {

    TextInputLayout etRequestTitle, etDateOfRequest, etNameOfRecipient, etRequestLocation, etRequestedBloodType, etRequestDescription, etRecipientContactDetail, etEmail;
    String RequestTitle, DateOfRequest, NameOfRecipient, RequestLocation, RequestedBloodType, RequestDescription;

    public static final String EXTRA_EMERGENCY_NOTICE_TITLE = "noticeTitle";
    public static final String EXTRA_EMERGENCY_NOTICE_DATE = "noticeDate";
    public static final String EXTRA_EMERGENCY_NOTICE_DESCRIPTION = "noticeDescription";
    public static final String EXTRA_EMERGENCY_NOTICE_LOCATION = "locationName";
    public static final String EXTRA_EMERGENCY_NOTICE_ID = "noticeID";
    public static final String EXTRA_EMERGENCY_NOTICE_NAME = "postedBy";
    public static final String EXTRA_REQUEST_BLOOD_TYPE = "request_blood_type";
    public static final String EXTRA_EMERGENCY_NOTICE_CONTACT = "noticeContact";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_blood_request_details);

        etRequestTitle = findViewById(R.id.etRequestTitle);
        etDateOfRequest = findViewById(R.id.etDateOfRequest);
        etNameOfRecipient = findViewById(R.id.etNameOfRecipient);
        etRequestLocation = findViewById(R.id.etRequestLocation);
        etRequestedBloodType = findViewById(R.id.etRequestedBloodType);
        etRequestDescription = findViewById(R.id.etRequestDescription);
        etRecipientContactDetail = findViewById(R.id.etContact);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        etRequestTitle.getEditText().setText((String)b.get(EXTRA_EMERGENCY_NOTICE_TITLE));
        etRequestTitle.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etDateOfRequest.getEditText().setText((String)b.get(EXTRA_EMERGENCY_NOTICE_DATE));
        etDateOfRequest.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etNameOfRecipient.getEditText().setText((String)b.get(EXTRA_EMERGENCY_NOTICE_NAME));
        etNameOfRecipient.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etRecipientContactDetail.getEditText().setText((String)b.get(EXTRA_EMERGENCY_NOTICE_CONTACT));
        etRecipientContactDetail.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etRequestLocation.getEditText().setText((String)b.get(EXTRA_EMERGENCY_NOTICE_LOCATION));
        etRequestLocation.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etRequestedBloodType.getEditText().setText((String)b.get(EXTRA_REQUEST_BLOOD_TYPE));
        etRequestedBloodType.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etRequestDescription.getEditText().setText((String)b.get(EXTRA_EMERGENCY_NOTICE_DESCRIPTION));
        etRequestDescription.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
    }
}