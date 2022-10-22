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
import android.widget.Toast;

import com.example.OneBlood.Adapters.BloodRequestAdapter;
import com.example.OneBlood.Adapters.EmergencyNoticeAdapters;
import com.example.OneBlood.Firebase;
import com.example.OneBlood.Labs.BloodRequestLab;
import com.example.OneBlood.Models.BloodRequest;
import com.example.OneBlood.Models.EmergencyNotice;
import com.example.OneBlood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BloodRequestMainMenu extends AppCompatActivity {

    Button btnViewYourRequest, btnNewRequest;
    RecyclerView rv;
    EmergencyNoticeAdapters mEmergencyNoticeAdapters;
    List<EmergencyNotice> mEmergencyNotices;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_blood_request_main_menu);

        btnNewRequest = findViewById(R.id.btnNewRequest);
        btnViewYourRequest = findViewById(R.id.btnViewYourRequest);
        rv = findViewById(R.id.rvBloodRequestList);

        loadRequestList();

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
    }

    private void loadRequestList() {
        mEmergencyNotices = new ArrayList<>();

        ProgressDialog dialog = ProgressDialog.show(com.example.OneBlood.Activity.BloodRequestMainMenu.this, "",
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
                if (mEmergencyNotices.size() == 0) {
                    Toast.makeText(BloodRequestMainMenu.this, "No Requests Found.", Toast.LENGTH_SHORT).show();
                }
                rv.setLayoutManager(new LinearLayoutManager(BloodRequestMainMenu.this));
                mEmergencyNoticeAdapters = new EmergencyNoticeAdapters(mEmergencyNotices, BloodRequestMainMenu.this, true);
                rv.setAdapter(mEmergencyNoticeAdapters);
            }
        }, 1000);
    }

}