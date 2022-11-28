package com.example.OneBlood.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.OneBlood.R;
import com.example.OneBlood.UserBloodRequestMainMenu;
import com.example.OneBlood.ViewBloodRequestDetails;
import com.google.android.material.textfield.TextInputLayout;

public class HospitalViewBloodRequestDetails extends AppCompatActivity {

    public static final String EXTRA_EMERGENCY_NOTICE_TITLE = "noticeTitle";
    public static final String EXTRA_EMERGENCY_NOTICE_DATE = "noticeDate";
    public static final String EXTRA_EMERGENCY_NOTICE_DESCRIPTION = "noticeDescription";
    public static final String EXTRA_EMERGENCY_NOTICE_LOCATION = "locationName";
    public static final String EXTRA_EMERGENCY_NOTICE_ID = "noticeID";
    public static final String EXTRA_EMERGENCY_NOTICE_NAME = "postedBy";
    public static final String EXTRA_REQUEST_BLOOD_TYPE = "request_blood_type";
    public static final String EXTRA_EMERGENCY_NOTICE_CONTACT = "noticeContact";

    TextInputLayout etRequestTitle, etDateOfRequest, etNameOfRecipient, etRequestLocation, etRequestedBloodType, etRequestDescription, etRecipientContactDetail, etEmail;
    String RequestTitle, DateOfRequest, NameOfRecipient, RequestLocation, RequestedBloodType, RequestDescription;
    ImageView backToRequestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_view_blood_request_details);

        etRequestTitle = findViewById(R.id.etViewRequestTitle);
        etDateOfRequest = findViewById(R.id.etViewRequestDate);
        etNameOfRecipient = findViewById(R.id.etViewPostedBy);
        etRequestLocation = findViewById(R.id.etViewRequestLocation);
        etRequestedBloodType = findViewById(R.id.etViewRequestedBloodType);
        etRequestDescription = findViewById(R.id.etViewRequestDescription);
        etRecipientContactDetail = findViewById(R.id.etViewRequestContact);
        backToRequestList = findViewById(R.id.ivBackToHospitalRequestList);

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

        backToRequestList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HospitalViewBloodRequestDetails.this, HospitalBloodRequestMenu.class);
        startActivity(intent);
        finish();
    }
}