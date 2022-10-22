package com.example.OneBlood.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.OneBlood.Adapters.DonorListAdapter;
import com.example.OneBlood.Adapters.EmergencyNoticeAdapters;
import com.example.OneBlood.Models.Donor;
import com.example.OneBlood.Models.EmergencyNotice;
import com.example.OneBlood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class HospitalEmergencyNoticeMenu extends AppCompatActivity {
    RecyclerView rv;
    EmergencyNoticeAdapters mEmergencyNoticeAdapters;
    Button btnViewOwnEmergencyNotice;
    List<EmergencyNotice> mEmergencyNotices;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_emergency_notice_menu);

        rv = findViewById(R.id.rvEmergencyNoticeList);
        btnViewOwnEmergencyNotice = findViewById(R.id.btnViewOwnEmergencyNotice);

        loadEmergencyNoticeList();

        btnViewOwnEmergencyNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HospitalEmergencyNoticeMenu.this, HospitalEmergencyNotice.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void loadEmergencyNoticeList() {
        mEmergencyNotices = new ArrayList<>();

        ProgressDialog dialog = ProgressDialog.show(HospitalEmergencyNoticeMenu.this, "",
                "Loading. Please wait...", true);   //show loading dialog

        db.collection("emergencyRequest")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot result = task.getResult();
                            if (!result.isEmpty()) {
                                for (QueryDocumentSnapshot document : result) {
                                    EmergencyNotice emergencyNotice = new EmergencyNotice(
                                            document.get("description").toString(),
                                            document.get("title").toString(),
                                            document.get("postedBy").toString(),
                                            document.get("date").toString(),
                                            document.getId(),
                                            document.get("blood type").toString(),
                                            document.get("contact").toString(),
                                            document.get("location").toString());
                                    mEmergencyNotices.add(emergencyNotice);
                                }
                            }
                        }
                    }
                });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();   //remove loading Dialog
                if(mEmergencyNotices.size() == 0){
                    Toast.makeText(HospitalEmergencyNoticeMenu.this, "No Requests Found.", Toast.LENGTH_SHORT).show();
                }
                rv.setLayoutManager(new LinearLayoutManager(HospitalEmergencyNoticeMenu.this));
                mEmergencyNoticeAdapters = new EmergencyNoticeAdapters(mEmergencyNotices,HospitalEmergencyNoticeMenu.this, false);
                rv.setAdapter(mEmergencyNoticeAdapters);
            }
        }, 1000);
    }
}