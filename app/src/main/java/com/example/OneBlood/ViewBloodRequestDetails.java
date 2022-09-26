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

    public static final String EXTRA_REQUEST_TITLE = "request_title";
    public static final String EXTRA_REQUEST_DATE = "request_date";
    public static final String EXTRA_REQUEST_DESCRIPTION = "request_description";
    public static final String EXTRA_REQUEST_BLOOD_TYPE = "request_blood_type";
    public static final String EXTRA_REQUEST_ID = "request_id";
    public static final String EXTRA_REQUEST_LOCATION = "request_location";
    public static final String EXTRA_RECIPIENT_NAME = "recipient_name";
    public static final String EXTRA_RECIPIENT_EMAIL = "recipient_email";
    public static final String EXTRA_RECIPIENT_CONTACT = "recipient_contact";

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
        etEmail = findViewById(R.id.etEmailOfRecipient);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        etRequestTitle.getEditText().setText((String)b.get(EXTRA_REQUEST_TITLE));
        etRequestTitle.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etDateOfRequest.getEditText().setText((String)b.get(EXTRA_REQUEST_DATE));
        etDateOfRequest.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etNameOfRecipient.getEditText().setText((String)b.get(EXTRA_RECIPIENT_NAME));
        etNameOfRecipient.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etRecipientContactDetail.getEditText().setText((String)b.get(EXTRA_RECIPIENT_CONTACT));
        etRecipientContactDetail.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etEmail.getEditText().setText((String)b.get(EXTRA_RECIPIENT_EMAIL));
        etEmail.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etRequestLocation.getEditText().setText((String)b.get(EXTRA_REQUEST_LOCATION));
        etRequestLocation.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etRequestedBloodType.getEditText().setText((String)b.get(EXTRA_REQUEST_BLOOD_TYPE));
        etRequestedBloodType.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etRequestDescription.getEditText().setText((String)b.get(EXTRA_REQUEST_DESCRIPTION));
        etRequestDescription.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
    }
}