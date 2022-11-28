package com.example.OneBlood.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.OneBlood.R;
import com.example.OneBlood.UserDashBoard;
import com.example.OneBlood.UserLogin;

public class HospitalMenu extends AppCompatActivity {

    public static SharedPreferences hospitalPreferences;
    private final String SHARED_PREFERENCE = "hospitalPreferences";
    private final String KEY_HOSPITAL_NAME = "hospitalName";
    private final String KEY_HOSPITAL_CONTACT = "hospitalContact";

    TextView tvHospitalName;
    Button btnHospitalNotice, btnHospitalEmergency, btnHospitalEvents, btnHospitalAppointments, btnLogOut, btnAvailableDonor;
    String hospital;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_menu);

        btnHospitalAppointments = findViewById(R.id.btnHospitalAppointments);
        btnHospitalNotice = findViewById(R.id.btnHospitalNotice);
        btnHospitalEmergency = findViewById(R.id.btnHospitalEmergency);
        btnHospitalEvents = findViewById(R.id.btnHospitalNewNotice);
        btnAvailableDonor = findViewById(R.id.btnAvailableDonors);
        btnLogOut = findViewById(R.id.btnHospitalLogOut);
        tvHospitalName = findViewById(R.id.tvHospitalName);

        hospitalPreferences = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        hospital = hospitalPreferences.getString(KEY_HOSPITAL_NAME, "");

        tvHospitalName.setText(hospital);

        btnHospitalEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HospitalMenu.this, HospitalViewEvent.class);
                startActivity(i);
                finish();
            }
        });

        btnAvailableDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HospitalMenu.this, HospitalDonorList.class);
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

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setMessage("Log Out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(HospitalMenu.this, HospitalLogin.class);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).create().show();
    }
}
