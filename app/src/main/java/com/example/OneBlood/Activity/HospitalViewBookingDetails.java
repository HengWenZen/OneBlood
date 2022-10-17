package com.example.OneBlood.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.OneBlood.R;

public class HospitalViewBookingDetails extends AppCompatActivity {

    public static final String EXTRA_BOOKING_DATE = "bookingDate";
    public static final String EXTRA_BOOKING_TIME = "bookingTime";
    public static final String EXTRA_BOOKING_HOSPITAL = "hospitalName";
    public static final String EXTRA_USER_NAME = "userName";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_view_booking_details);


    }
}