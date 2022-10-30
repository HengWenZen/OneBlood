package com.example.OneBlood.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.OneBlood.Firebase;
import com.example.OneBlood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class HospitalViewEventBookingDetails extends AppCompatActivity {

    public static final String EXTRA_EVENT_BOOKING_DATE = "eventBookingDate";
    public static final String EXTRA_EVENT_BOOKING_TIME = "eventBookingTime";
    public static final String EXTRA_EVENT_BOOKING_HOSPITAL = "eventBookingLocation";
    public static final String EXTRA_USER_NAME = "userName";
    public static final String EXTRA_EVENT_BOOKING_ID = "eventBookingId";

    TextInputLayout etEventBookingSlot, etEventBookingDate, etEventBookingUser, etEventBookingLocation;
    Button btnMarkAsComplete, btnHospitalCancelEventBooking;
    String eventBookingId, eventBookingSlot, eventBookingUser, eventBookingDate, eventBookingHospital;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Firebase mFirebase = new Firebase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_view_event_booking_details);
        etEventBookingDate = findViewById(R.id.etEventBookingDate);
        etEventBookingSlot = findViewById(R.id.etEventBookingSlot);
        etEventBookingUser = findViewById(R.id.etEventBookingUser);
        etEventBookingLocation = findViewById(R.id.etEventBookingLocation);
        btnHospitalCancelEventBooking = findViewById(R.id.btnCancelEventBooking);
        btnMarkAsComplete = findViewById(R.id.btnMarkAsComplete);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        eventBookingId = (String)b.get(EXTRA_EVENT_BOOKING_ID);
        eventBookingSlot = ((String)b.get(EXTRA_EVENT_BOOKING_TIME));
        eventBookingUser = (String)b.get(EXTRA_USER_NAME);
        eventBookingDate = (String)b.get(EXTRA_EVENT_BOOKING_DATE);
        eventBookingHospital = (String)b.get(EXTRA_EVENT_BOOKING_HOSPITAL);

        etEventBookingDate.getEditText().setText(eventBookingDate);
        etEventBookingDate.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etEventBookingLocation.getEditText().setText(eventBookingHospital);
        etEventBookingLocation.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etEventBookingSlot.getEditText().setText(eventBookingSlot);
        etEventBookingSlot.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etEventBookingUser.getEditText().setText(eventBookingUser);
        etEventBookingUser.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));

        btnHospitalCancelEventBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelBooking();
            }
        });

        btnMarkAsComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeBooking();
            }
        });
    }

    private void cancelBooking() {
        new AlertDialog.Builder(HospitalViewEventBookingDetails.this)
                .setMessage("Confirm Appointment Cancellation?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        db.collection("latestAppointment")
                                .whereEqualTo("user", eventBookingUser)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
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

                        db.collection("userEventBooking").document(eventBookingId).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(HospitalViewEventBookingDetails.this, "Appointment Cancelled Successfully!", Toast.LENGTH_SHORT).show();
                                        Log.d("Document ID", eventBookingId);
                                        Intent i = new Intent(HospitalViewEventBookingDetails.this, HospitalViewEventBooking.class);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(HospitalViewEventBookingDetails.this, "Fail to Cancel Appointment! " + e.getMessage() , Toast.LENGTH_SHORT).show();
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

    private void completeBooking(){
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        String user = (String)b.get(EXTRA_USER_NAME);

        new AlertDialog.Builder(HospitalViewEventBookingDetails.this)
                .setMessage("Mark Status of Appointment as Complete?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                       db.collection("users")
                               .get()
                               .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                   @Override
                                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                       Map<String, Object> user = new HashMap<>();
                                       user.put("status", "inactive");

                                       if (task.isSuccessful()) {
                                           QuerySnapshot result = task.getResult();
                                           if (!result.isEmpty()) {
                                               Log.d("Data Retrieved", result.toString());
                                               for (QueryDocumentSnapshot document : result) {
                                                   String userName = document.get("FullName").toString();
                                                   if(eventBookingUser.equals(userName)) {
                                                       Log.d("Document ID:", document.getId() + " => " + document.getData());
                                                       db.collection("users").document(document.getId()).update(user);
                                                       Log.d("TAG", "onComplete: User Status Updated Successfully!");
                                                   }
                                               }
                                           }
                                       }
                                   }
                               });



                        //Delete Appointment from activeAppointment document in Firebase
                        db.collection("userEventBooking").document(eventBookingId).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("Document ID", eventBookingId);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });

                        db.collection("latestAppointment")
                                .whereEqualTo("user", eventBookingUser)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        Map<String, Object> latest = new HashMap<>();
                                        latest.put("slot", eventBookingSlot);
                                        latest.put("user", eventBookingUser);
                                        latest.put("date", eventBookingDate);
                                        latest.put("userStatus", "inactive");
                                        latest.put("location", eventBookingHospital);
                                        latest.put("status", "completed");

                                        if (task.isSuccessful()) {
                                            QuerySnapshot result = task.getResult();
                                            if (!result.isEmpty()) {
                                                Log.d("Data Retrieved", result.toString());
                                                for (QueryDocumentSnapshot document : result) {
                                                    db.collection("latestAppointment").document(document.getId()).update(latest);
                                                    Log.d("Document ID:", "latestAppointment Updated Successfully!");
                                                }
                                            }
                                        }
                                    }
                                });

                        //Add completed appointment to completedAppointments Document in firebase
                        Map<String, Object> data = new HashMap<>();
                        data.put("slot", eventBookingSlot);
                        data.put("user", eventBookingUser);
                        data.put("date", eventBookingDate);
                        data.put("location", eventBookingHospital);
                        data.put("status", "completed");


                        db.collection("completedAppointments").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(HospitalViewEventBookingDetails.this, "Appointment Completed", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(HospitalViewEventBookingDetails.this, HospitalViewBooking.class);
                                startActivity(i);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(HospitalViewEventBookingDetails.this, "Fail to mark as Complete " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create().show();
    }

}