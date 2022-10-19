package com.example.OneBlood.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.OneBlood.Adapters.LocationAdapter;
import com.example.OneBlood.Labs.LocationLab;
import com.example.OneBlood.Models.DonateLocation;
import com.example.OneBlood.R;
import com.example.OneBlood.UserDashBoard;

import java.util.List;

public class Appointments extends AppCompatActivity {
    Button btnViewAppointment;
    RecyclerView mRecyclerView;
    LocationAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_appointment);

        btnViewAppointment = findViewById(R.id.btnViewAppointment);
        mRecyclerView = findViewById(R.id.myRecyclerView);

        btnViewAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Appointments.this, ViewBooking.class);
                startActivity(i);
                finish();
            }
        });

        LocationLab locationLab = LocationLab.get(Appointments.this);
        List<DonateLocation> locations = locationLab.getLocation();

        ProgressDialog dialog = ProgressDialog.show(Appointments.this, "",
                "Loading. Please wait...", true);   //show loading dialog
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();   //remove loading Dialog
                mRecyclerView.setLayoutManager(new LinearLayoutManager(Appointments.this));
                mAdapter = new LocationAdapter(locations, Appointments.this);
                mRecyclerView.setAdapter(mAdapter);
            }
        }, 1000);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Appointments.this, UserDashBoard.class);
        startActivity(intent);
        finish();
    }
}
