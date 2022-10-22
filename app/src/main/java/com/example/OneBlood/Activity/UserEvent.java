package com.example.OneBlood.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.example.OneBlood.Adapters.EventsAdapter;
import com.example.OneBlood.Adapters.LocationAdapter;
import com.example.OneBlood.Labs.EventLab;
import com.example.OneBlood.Models.Events;
import com.example.OneBlood.R;

import java.util.List;

public class UserEvent extends AppCompatActivity {


    Button btnViewEventAppointment;
    RecyclerView rv;
    EventsAdapter mEventsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_event);

        rv = findViewById(R.id.rvEventList);
        btnViewEventAppointment = findViewById(R.id.btnViewEventAppointment);

        btnViewEventAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserEvent.this, UserViewEventBooking.class);
                startActivity(intent);
                finish();
            }
        });


        EventLab eventLab = EventLab.get(this);
        List<Events> events = eventLab.getEventsList();

        ProgressDialog dialog = ProgressDialog.show(UserEvent.this, "",
                "Loading. Please wait...", true);   //show loading dialog
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();   //remove loading Dialog
                rv.setLayoutManager(new LinearLayoutManager(UserEvent.this));
                mEventsAdapter = new EventsAdapter(events, UserEvent.this);
                rv.setAdapter(mEventsAdapter);
            }
        }, 2000);




    }
}