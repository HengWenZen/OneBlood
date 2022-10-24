package com.example.OneBlood;

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

import java.util.List;

public class UserAppointmentsMenu extends AppCompatActivity {
    Button btnViewAppointment;
    RecyclerView mRecyclerView;
    LocationAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_make_appointment);

        btnViewAppointment = findViewById(R.id.btnViewAppointment);
        mRecyclerView = findViewById(R.id.myRecyclerView);

        btnViewAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserAppointmentsMenu.this, UserViewBooking.class);
                startActivity(i);
                finish();
            }
        });

        LocationLab locationLab = LocationLab.get(UserAppointmentsMenu.this);
        List<DonateLocation> locations = locationLab.getLocation();

        ProgressDialog dialog = ProgressDialog.show(UserAppointmentsMenu.this, "",
                "Loading. Please wait...", true);   //show loading dialog
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();   //remove loading Dialog
                mRecyclerView.setLayoutManager(new LinearLayoutManager(UserAppointmentsMenu.this));
                mAdapter = new LocationAdapter(locations, UserAppointmentsMenu.this);
                mRecyclerView.setAdapter(mAdapter);
            }
        }, 1000);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UserAppointmentsMenu.this, UserDashBoard.class);
        startActivity(intent);
        finish();
    }
}
