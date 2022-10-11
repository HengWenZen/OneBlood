package com.example.OneBlood.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.OneBlood.Labs.DonorLab;
import com.example.OneBlood.Adapters.DonorListAdapter;
import com.example.OneBlood.Models.Donor;
import com.example.OneBlood.R;

import java.util.List;

public class AvailableDonorMainMenu extends AppCompatActivity {

    RecyclerView rvDonorList;
    Button btnBecomeDonor;
    DonorListAdapter mDonorListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_donor_main_menu);

        rvDonorList = findViewById(R.id.rvDonorList);

        DonorLab donorLab = DonorLab.get(this);
        List<Donor> donors = donorLab.getDonorList();

        ProgressDialog dialog = ProgressDialog.show(AvailableDonorMainMenu.this, "",
                "Loading. Please wait...", true);   //show loading dialog
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();   //remove loading Dialog
                rvDonorList.setLayoutManager(new LinearLayoutManager(AvailableDonorMainMenu.this));
                mDonorListAdapter = new DonorListAdapter(donors, AvailableDonorMainMenu.this);
                rvDonorList.setAdapter(mDonorListAdapter);
            }
        }, 2000);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
