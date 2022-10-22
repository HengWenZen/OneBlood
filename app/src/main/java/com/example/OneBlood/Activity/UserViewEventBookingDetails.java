package com.example.OneBlood.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import android.widget.Button;
import android.widget.Toast;

import com.example.OneBlood.Adapters.ViewBookingAdapter;
import com.example.OneBlood.Adapters.ViewEventBookingAdapter;
import com.example.OneBlood.Models.Booking;
import com.example.OneBlood.Models.BookingEvent;
import com.example.OneBlood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserViewEventBookingDetails extends AppCompatActivity {

    public static final String EXTRA_EVENT_BOOKING_DATE = "eventBookingDate";
    public static final String EXTRA_EVENT_BOOKING_TIME = "eventBookingTime";
    public static final String EXTRA_EVENT_BOOKING_HOSPITAL = "eventBookingLocation";
    public static final String EXTRA_USER_NAME = "userName";
    public static final String EXTRA_EVENT_BOOKING_ID = "eventBookingId";

    TextInputLayout etBookingEventDate, etBookingEventSlot, etBookingEventHospital;
    String bookingId, bookingSlot;
    Button btnCancelBooking;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_event_booking_details);

        etBookingEventDate = findViewById(R.id.etBookingEventDate);
        etBookingEventSlot = findViewById(R.id.etBookingEventSlot);
        etBookingEventHospital = findViewById(R.id.etBookingEventHospital);
        btnCancelBooking = findViewById(R.id.btnCancelEventBooking);


        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        bookingId = (String)b.get(EXTRA_EVENT_BOOKING_ID);
        bookingSlot = ((String)b.get(EXTRA_EVENT_BOOKING_TIME));
        etBookingEventDate.getEditText().setText((String)b.get(EXTRA_EVENT_BOOKING_DATE));
        etBookingEventDate.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etBookingEventHospital.getEditText().setText((String)b.get(EXTRA_EVENT_BOOKING_HOSPITAL));
        etBookingEventHospital.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etBookingEventSlot.getEditText().setText(bookingSlot);
        etBookingEventSlot.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));


        btnCancelBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelBooking();
            }
        });

    }

    private void cancelBooking() {
        new AlertDialog.Builder(UserViewEventBookingDetails.this)
                .setMessage("Confirm Appointment Cancellation?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        db.collection("userEventBooking").document(bookingId).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(UserViewEventBookingDetails.this, "Appointment Cancelled Successfully!", Toast.LENGTH_SHORT).show();
                                        Log.d("Document ID", bookingId);
                                        Intent i = new Intent(UserViewEventBookingDetails.this, UserViewEventBooking.class);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(UserViewEventBookingDetails.this, "Fail to Cancel Appointment! " + e.getMessage() , Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).create().show();
    }
}