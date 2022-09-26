package com.example.OneBlood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.textfield.TextInputLayout;

public class ViewDonorDetails extends AppCompatActivity {

    public static final String EXTRA_DONOR_NAME = "donor_name";
    public static final String EXTRA_DONOR_CONTACT = "donor_contact";
    public static final String EXTRA_DONOR_BLOOD_TYPE = "donor_blood_type";
    public static final String EXTRA_DONOR_ID = "donor_id";
    public static final String EXTRA_DONOR_EMAIL = "donor_email";

    TextInputLayout etNameOfDonor, etDonorContact, etDonorEmail, etDonorBloodType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_donor_details);

        etNameOfDonor = findViewById(R.id.etNameOfDonor);
        etDonorBloodType = findViewById(R.id.etDonorBloodType);
        etDonorContact = findViewById(R.id.etDonorContact);
        etDonorEmail = findViewById(R.id.etDonorEmail);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        etNameOfDonor.getEditText().setText((String)b.get(EXTRA_DONOR_NAME));
        etNameOfDonor.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etDonorContact.getEditText().setText((String)b.get(EXTRA_DONOR_CONTACT));
        etDonorContact.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etDonorEmail.getEditText().setText((String)b.get(EXTRA_DONOR_EMAIL));
        etDonorEmail.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etDonorBloodType.getEditText().setText((String)b.get(EXTRA_DONOR_BLOOD_TYPE));
        etDonorBloodType.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
    }
}