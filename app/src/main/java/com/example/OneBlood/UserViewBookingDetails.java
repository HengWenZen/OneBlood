package com.example.OneBlood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class UserViewBookingDetails extends AppCompatActivity {
    public static final String EXTRA_BOOKING_DATE = "noticeDate";
    public static final String EXTRA_BOOKING_TIME = "noticeDescription";
    public static final String EXTRA_BOOKING_HOSPITAL = "hospitalName";
    public static final String EXTRA_USER_NAME = "userName";
    public static final String EXTRA_BOOKING_ID = "bookingId";

    TextInputLayout etBookingDate, etBookingSlot, etBookingHospital;
    Button btnCancelBooking;
    String bookingId, bookingSlot, userName;
    ImageView backToOwnAppointmentList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_booking_details);

        etBookingDate = findViewById(R.id.etBookingDate);
        etBookingSlot = findViewById(R.id.etBookingSlot);
        etBookingHospital = findViewById(R.id.etBookingHospital);
        btnCancelBooking = findViewById(R.id.btnCancelBooking);
        backToOwnAppointmentList = findViewById(R.id.backToOwnAppointmentList);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        userName = (String)b.get(EXTRA_USER_NAME);
        bookingId = (String)b.get(EXTRA_BOOKING_ID);
        bookingSlot = (timeSlot(Integer.valueOf((String)b.get(EXTRA_BOOKING_TIME))));
        etBookingDate.getEditText().setText((String)b.get(EXTRA_BOOKING_DATE));
        etBookingDate.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etBookingHospital.getEditText().setText((String)b.get(EXTRA_BOOKING_HOSPITAL));
        etBookingHospital.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etBookingSlot.getEditText().setText(bookingSlot);
        etBookingSlot.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));

        backToOwnAppointmentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnCancelBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelBooking();
            }
        });
    }

    private void cancelBooking() {
        new AlertDialog.Builder(UserViewBookingDetails.this)
                .setMessage("Confirm Appointment Cancellation?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        db.collection("userBooking").document(bookingId).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(UserViewBookingDetails.this, "Appointment Cancelled Successfully!", Toast.LENGTH_SHORT).show();
                                        Log.d("Document ID", bookingId);
                                        Intent i = new Intent(UserViewBookingDetails.this, UserViewBooking.class);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(UserViewBookingDetails.this, "Fail to Cancel Appointment! " + e.getMessage() , Toast.LENGTH_SHORT).show();
                                    }
                                });

                        db.collection("latestAppointment")
                                .whereEqualTo("user", userName)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task< QuerySnapshot > task) {
                                        Map<String, Object> latest = new HashMap<>();
                                        latest.put("userStatus", "active");
                                        latest.put("status", "available");

                                        if (task.isSuccessful()) {
                                            QuerySnapshot result = task.getResult();
                                            if (!result.isEmpty()) {
                                                Log.d("Data Retrieved", result.toString());
                                                for (QueryDocumentSnapshot document : result) {
                                                    Log.d("Document ID:", document.getId() + " => " + document.getData());
                                                    db.collection("latestAppointment").document(document.getId()).update(latest);
                                                }
                                            }
                                        }
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

    public String timeSlot(int position) {
        switch (position) {
            case 0:
                return "9:00-10:00";
            case 1:
                return "10:00-11:00";
            case 2:
                return "11:00-12:00";
            case 3:
                return "12:00-13:00";
            case 4:
                return "13:00-14:00";
            case 5:
                return "14:00-15:00";
            case 6:
                return "15:00-16:00";
            case 7:
                return "16:00-17:00";
            case 8:
                return "17:00-18:00";
            default:
                return "Closed";
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(UserViewBookingDetails.this, UserViewBooking.class);
        startActivity(intent);
    }
}