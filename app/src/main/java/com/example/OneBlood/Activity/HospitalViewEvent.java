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
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.OneBlood.Adapters.EventsAdapter;
import com.example.OneBlood.Models.Events;
import com.example.OneBlood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HospitalViewEvent extends AppCompatActivity {

    public static SharedPreferences hospitalPreferences;
    private final String SHARED_PREFERENCE = "hospitalPreferences";
    private final String KEY_HOSPITAL_ID = "hospitalID";
    private final String KEY_HOSPITAL_NAME = "hospitalName";
    private final String KEY_HOSPITAL_CONTACT = "hospitalContact";

    RecyclerView rv;
    EventsAdapter mEventsAdapter;
    Button btnPostNewEvent, btnMyEvents;
    List<Events> mEventsList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String hospitalName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_view_event);
        rv = findViewById(R.id.rvMyEventList);
        btnMyEvents = findViewById(R.id.btnMyEvents);
        btnPostNewEvent = findViewById(R.id.btnPostNewEvent);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        hospitalName = prefs.getString(KEY_HOSPITAL_NAME, "");

        loadEventList();

        btnPostNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HospitalViewEvent.this, HospitalNewEvent.class);
                startActivity(intent);
                finish();
            }
        });

        btnMyEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HospitalViewEvent.this, HospitalViewEventBooking.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadEventList(){
        mEventsList = new ArrayList<>();

        db.collection("events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot result = task.getResult();
                            if (!result.isEmpty()) {
                                Log.d("Data Retrieved", result.toString());
                                for (QueryDocumentSnapshot document : result) {
                                    Log.d("Document ID:", document.getId() + " => " + document.getData());
                                    String hospital = document.get("postedBy").toString();
                                    if(hospitalName.equals(hospital)) {
                                        Events events = new Events(document.get("startDate").toString(),
                                                document.get("endDate").toString(),
                                                document.get("location").toString(),
                                                document.get("imageUri").toString(),
                                                document.getId(),
                                                document.get("title").toString(),
                                                document.get("description").toString(),
                                                document.get("startTime").toString(),
                                                document.get("endTime").toString(),
                                                document.get("postedBy").toString());
                                        mEventsList.add(events);
                                    }
                                }
                            }
                        }
                    }
                });


        ProgressDialog dialog = ProgressDialog.show(HospitalViewEvent.this, "",
                "Loading. Please wait...", true);   //show loading dialog
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();   //remove loading Dialog
                rv.setLayoutManager(new LinearLayoutManager(HospitalViewEvent.this));
                mEventsAdapter = new EventsAdapter( mEventsList,HospitalViewEvent.this, true);
                rv.setAdapter(mEventsAdapter);
            }
        }, 1000);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HospitalViewEvent.this, HospitalMenu.class);
        startActivity(intent);
        finish();
    }
}