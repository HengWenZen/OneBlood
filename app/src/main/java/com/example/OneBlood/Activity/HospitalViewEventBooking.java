package com.example.OneBlood.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.OneBlood.Adapters.ViewEventBookingAdapter;
import com.example.OneBlood.Models.BookingEvent;
import com.example.OneBlood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HospitalViewEventBooking extends AppCompatActivity {

    public static SharedPreferences hospitalPreferences;
    private final String SHARED_PREFERENCE = "hospitalPreferences";
    private final String KEY_HOSPITAL_ID = "hospitalID";
    private final String KEY_HOSPITAL_NAME = "hospitalName";
    private final String KEY_HOSPITAL_CONTACT = "hospitalContact";

    private ViewEventBookingAdapter mViewEventBookingAdapter;
    private RecyclerView rv;
    String hospital;

    List<String> date;
    List<BookingEvent> mBookings;
    ImageView ivBackToBookingList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_view_event_booking);

        rv = findViewById(R.id.rvHospitalViewEventBooking);
        ivBackToBookingList = findViewById(R.id.ivBackToBookingList);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        hospital = prefs.getString(KEY_HOSPITAL_NAME, "");

        loadExistingAppointment();

        ivBackToBookingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadExistingAppointment() {
        mBookings = new ArrayList<>();
        ProgressDialog dialog = ProgressDialog.show(HospitalViewEventBooking.this, "",
                "Loading....", true);

        db.collection("userEventBooking")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot result = task.getResult();
                            if (!result.isEmpty()) {
                                for (QueryDocumentSnapshot document : result) {
                                    boolean check = checkDate(document.get("date").toString(), document.get("time").toString());
                                    if (check) {
                                        String hospitalName = document.get("location").toString();
                                        if(hospital.equals(hospitalName)) {
                                            BookingEvent b = new BookingEvent(document.getId(),
                                                    document.get("location").toString(),
                                                    document.get("date").toString(),
                                                    document.get("time").toString(),
                                                    document.get("user").toString());
                                            mBookings.add(b);
                                        }
                                    }
                                }
                                Log.d("data", mBookings.toString());
                            }
                        }
                    }
                });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();
                if(mBookings.size() == 0) {
                    alertDataEmpty();
                }
                else {
                    mViewEventBookingAdapter = new ViewEventBookingAdapter(HospitalViewEventBooking.this, mBookings, true);
                    rv.setLayoutManager(new LinearLayoutManager(HospitalViewEventBooking.this));
                    rv.setAdapter(mViewEventBookingAdapter);
                }
            }
        }, 1000);
    }

    private void alertDataEmpty() {
        //Initialize alert dialog if the data is empty
        new AlertDialog.Builder(this)
                .setMessage("No Existing Appointment Made..")
                .setPositiveButton("Return", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent y = new Intent(HospitalViewEventBooking.this, HospitalViewEvent.class);
                        startActivity(y);
                        finish();
                    }
                }).create().show();
    }

    private boolean checkDate(String result, String slot){
        result = result.concat(" " + slot);
        Date strDate = null;
        try {
            strDate = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (new Date().getTime() > strDate.getTime()) {
            return false;
        }
        else{
            return true;
        }
    }

    public void adapterChange(int position){
        mBookings.remove(position);
        mViewEventBookingAdapter.notifyItemRemoved(position);
        if(mBookings.size() == 0) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HospitalViewEventBooking.this, HospitalViewEvent.class);
        startActivity(intent);
        finish();
    }
}