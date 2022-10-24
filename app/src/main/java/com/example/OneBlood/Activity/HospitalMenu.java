package com.example.OneBlood.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.OneBlood.R;

public class HospitalMenu extends AppCompatActivity {
    TextView tvTextView;
    Button btnHospitalNotice, btnHospitalEmergency, btnHospitalEvents, btnHospitalAppointments, btnLogOut;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_menu);

        btnHospitalAppointments = findViewById(R.id.btnHospitalAppointments);
        btnHospitalNotice = findViewById(R.id.btnHospitalNotice);
        btnHospitalEmergency = findViewById(R.id.btnHospitalEmergency);
        btnHospitalEvents = findViewById(R.id.btnHospitalNewNotice);
        btnLogOut = findViewById(R.id.btnHospitalLogOut);

        btnHospitalEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HospitalMenu.this, HospitalViewEvent.class);
                startActivity(i);
                finish();
            }
        });

        btnHospitalAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HospitalMenu.this, HospitalViewBooking.class);
                startActivity(i);
                finish();
            }
        });

        btnHospitalNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HospitalMenu.this, HospitalNoticeMenu.class);
                startActivity(i);
                finish();
            }
        });

        btnHospitalEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HospitalMenu.this, HospitalBloodRequestMenu.class);
                startActivity(i);
                finish();
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences hospitalPreference = getSharedPreferences("hospitalPreferences", MODE_PRIVATE);
                SharedPreferences.Editor spEditor = HospitalLogin.hospitalPreferences.edit();
                spEditor.clear();
                spEditor.apply();
                Intent q = new Intent(HospitalMenu.this, HospitalLogin.class);
                startActivity(q);
                finish();
            }
        });
    }
}
