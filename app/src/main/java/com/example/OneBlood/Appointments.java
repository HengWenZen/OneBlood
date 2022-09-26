package com.example.OneBlood;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import java.util.List;

public class Appointments extends AppCompatActivity {
    Button btnProceed;
    RecyclerView mRecyclerView;
    LocationAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_appointment);

        btnProceed = findViewById(R.id.btnMakeAppointment);
        mRecyclerView = findViewById(R.id.myRecyclerView);

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
        }, 2000);

    }
}
