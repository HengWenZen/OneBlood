package com.example.OneBlood.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.OneBlood.Adapters.BloodRequestAdapters;
import com.example.OneBlood.Models.EmergencyNotice;
import com.example.OneBlood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HospitalBloodRequestMenu extends AppCompatActivity {

    public static SharedPreferences hospitalPreferences;
    private final String SHARED_PREFERENCE = "hospitalPreferences";
    private final String KEY_HOSPITAL_ID = "hospitalID";
    private final String KEY_HOSPITAL_NAME = "hospitalName";

    RecyclerView rv;
    BloodRequestAdapters mBloodRequestAdapters;
    Button btnViewOwnEmergencyNotice, btnNewEmergencyNotice;
    List<EmergencyNotice> mEmergencyNotices;
    ImageView ivBackToHospitalHome;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String postedBy, hospital;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_blood_request_menu);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        hospital = prefs.getString(KEY_HOSPITAL_NAME, "");

        rv = findViewById(R.id.rvEmergencyNoticeList);
        btnViewOwnEmergencyNotice = findViewById(R.id.btnViewOwnEmergencyNotice);
        btnNewEmergencyNotice = findViewById(R.id.btnNewEmergencyNotice);
        ivBackToHospitalHome = findViewById(R.id.ivBackToHospitalHome);

        loadEmergencyNoticeList();

        btnViewOwnEmergencyNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HospitalBloodRequestMenu.this, HospitalViewOwnBloodRequest.class);
                startActivity(intent);
                finish();
            }
        });

        btnNewEmergencyNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HospitalBloodRequestMenu.this, HospitalNewBloodRequest.class);
                startActivity(intent);
                finish();
            }
        });

        ivBackToHospitalHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void loadEmergencyNoticeList() {
        mEmergencyNotices = new ArrayList<>();

        //show loading dialog
        ProgressDialog dialog = ProgressDialog.show(HospitalBloodRequestMenu.this, "",
                "Loading. Please wait...", true);

        db.collection("emergencyRequest")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot result = task.getResult();
                            if (!result.isEmpty()) {
                                for (QueryDocumentSnapshot document : result) {
                                    postedBy = document.get("postedBy").toString();

                                    if(!postedBy.equals(hospital)) {
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
                    }
                });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();   //remove loading Dialog
                if(mEmergencyNotices.size() == 0){
                    Toast.makeText(HospitalBloodRequestMenu.this, "No Requests Found.", Toast.LENGTH_SHORT).show();
                }
                rv.setLayoutManager(new LinearLayoutManager(HospitalBloodRequestMenu.this));
                mBloodRequestAdapters = new BloodRequestAdapters(mEmergencyNotices, HospitalBloodRequestMenu.this, true, postedBy);
                rv.setAdapter(mBloodRequestAdapters);
            }
        }, 1000);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent( HospitalBloodRequestMenu.this, HospitalMenu.class);
        startActivity(i);
        finish();
    }
}