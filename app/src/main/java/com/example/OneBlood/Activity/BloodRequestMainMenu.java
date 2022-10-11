package com.example.OneBlood.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.example.OneBlood.Adapters.BloodRequestAdapter;
import com.example.OneBlood.Labs.BloodRequestLab;
import com.example.OneBlood.Models.BloodRequest;
import com.example.OneBlood.R;

import java.util.List;

public class BloodRequestMainMenu extends AppCompatActivity {

    Button btnViewYourRequest, btnNewRequest;
    RecyclerView rv;
    BloodRequestAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_blood_request_main_menu);

        btnNewRequest = findViewById(R.id.btnNewRequest);
        btnViewYourRequest = findViewById(R.id.btnViewYourRequest);
        rv = findViewById(R.id.rvBloodRequestList);

        btnNewRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BloodRequestMainMenu.this, NewBloodRequest.class);
                startActivity(i);
            }
        });

        btnViewYourRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BloodRequestMainMenu.this, NewBloodRequest.class);
                startActivity(i);
            }
        });

        BloodRequestLab bloodRequestLab = BloodRequestLab.get(this);
        List<BloodRequest> bloodRequests = bloodRequestLab.getBloodRequest();

        ProgressDialog dialog = ProgressDialog.show(BloodRequestMainMenu.this, "",
                "Loading. Please wait...", true);   //show loading dialog
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();   //remove loading Dialog
                rv.setLayoutManager(new LinearLayoutManager(BloodRequestMainMenu.this));
                mAdapter = new BloodRequestAdapter(bloodRequests, BloodRequestMainMenu.this);
                rv.setAdapter(mAdapter);
            }
        }, 2000);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}