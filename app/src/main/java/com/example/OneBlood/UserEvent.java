package com.example.OneBlood;

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

import com.example.OneBlood.Adapters.EventsAdapter;
import com.example.OneBlood.Models.Events;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserEvent extends AppCompatActivity {


    Button btnViewEventAppointment;
    RecyclerView rv;
    EventsAdapter mEventsAdapter;
    List<Events> mEventsList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_event);

        rv = findViewById(R.id.rvEventList);
        btnViewEventAppointment = findViewById(R.id.btnViewEventAppointment);

        loadEventList();

        btnViewEventAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserEvent.this, UserViewEventBooking.class);
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
                });


        ProgressDialog dialog = ProgressDialog.show(UserEvent.this, "",
                "Loading. Please wait...", true);   //show loading dialog
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();   //remove loading Dialog
                rv.setLayoutManager(new LinearLayoutManager(UserEvent.this));
                mEventsAdapter = new EventsAdapter( mEventsList,UserEvent.this, false);
                rv.setAdapter(mEventsAdapter);
            }
        }, 1000);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UserEvent.this, UserDashBoard.class);
        startActivity(intent);
        finish();
    }
}