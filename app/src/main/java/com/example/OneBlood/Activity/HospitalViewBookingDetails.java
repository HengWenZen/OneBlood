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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.OneBlood.Firebase;
import com.example.OneBlood.MyCallback;
import com.example.OneBlood.R;
import com.example.OneBlood.UserViewBooking;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HospitalViewBookingDetails extends AppCompatActivity {

    public static final String EXTRA_BOOKING_DATE = "noticeDate";
    public static final String EXTRA_BOOKING_TIME = "noticeDescription";
    public static final String EXTRA_BOOKING_HOSPITAL = "hospitalName";
    public static final String EXTRA_USER_NAME = "userName";
    public static final String EXTRA_BOOKING_ID = "bookingId";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Firebase mFirebase = new Firebase();

    TextInputLayout etUserBookingSlot, etUserBookingDate, etBookingUser;
    Button btnCancelAppointment, btnCompleteAppointment;
    String bookingId, bookingSlot, bookingUser, bookingDate, bookingHospital;
    ImageView ivBackToHospitalRequestMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_view_booking_details);

        etUserBookingDate = findViewById(R.id.etUserBookingDate);
        etUserBookingSlot = findViewById(R.id.etUserBookingSlot);
        etBookingUser = findViewById(R.id.etBookingUser);
        btnCancelAppointment = findViewById(R.id.btnHospitalCancelBooking);
        btnCompleteAppointment = findViewById(R.id.btnCompleteAppointment);
        ivBackToHospitalRequestMenu = findViewById(R.id.ivBackToHospitalRequestMenu);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        bookingId = (String)b.get(EXTRA_BOOKING_ID);
        bookingSlot = (timeSlot(Integer.valueOf((String)b.get(EXTRA_BOOKING_TIME))));
        bookingUser = (String)b.get(EXTRA_USER_NAME);
        Log.d("TAG", "onCreate: " + bookingUser);
        bookingDate = (String)b.get(EXTRA_BOOKING_DATE);
        bookingHospital = (String)b.get(EXTRA_BOOKING_HOSPITAL);
        etUserBookingDate.getEditText().setText((String)b.get(EXTRA_BOOKING_DATE));
        etUserBookingDate.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etBookingUser.getEditText().setText(bookingUser);
        etBookingUser.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etUserBookingSlot.getEditText().setText(bookingSlot);
        etUserBookingSlot.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));

        btnCancelAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelBooking();
            }
        });

        btnCompleteAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeBooking();
            }
        });

        ivBackToHospitalRequestMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void cancelBooking() {
        new AlertDialog.Builder(HospitalViewBookingDetails.this)
                .setMessage("Confirm Appointment Cancellation?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //Delete Data from Database
                        db.collection("userBooking").document(bookingId).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(HospitalViewBookingDetails.this, "Appointment Cancelled Successfully!", Toast.LENGTH_SHORT).show();
                                        Log.d("Document ID", bookingId);
                                        Intent i = new Intent(HospitalViewBookingDetails.this, HospitalViewBooking.class);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(HospitalViewBookingDetails.this, "Fail to Cancel Appointment! " + e.getMessage() , Toast.LENGTH_SHORT).show();
                                    }
                                });

                        //Edit the status of latest appointment collection in FireStore
                        db.collection("latestAppointment")
                                .whereEqualTo("user", bookingUser)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        Map<String, Object> latest = new HashMap<>();
                                        latest.put("slot", bookingSlot);
                                        latest.put("user", bookingUser);
                                        latest.put("date", bookingDate);
                                        latest.put("userStatus", "active");
                                        latest.put("location", bookingHospital);
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

    private void completeBooking(){
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        String user = (String)b.get(EXTRA_USER_NAME);

        new AlertDialog.Builder(HospitalViewBookingDetails.this)
                .setMessage("Mark Status of Appointment as Complete?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mFirebase.getData("users", null, new MyCallback() {
                            @Override
                            public void returnData(ArrayList<Map<String, Object>> docList) {
                                Log.d("firebase example", docList.toString());
                                ArrayList<String> list = new ArrayList<>();

                                //Change User status to inactive after donating blood
                                for (Map<String, Object> map : docList) {
                                    if (map.get("FullName").toString().equals(user)) {
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("status", "inactive");
                                        mFirebase.updData("users", user, map.get("id").toString());
                                    }
                                }
                            }
                        });

                        //Delete Appointment from active Appointment document in Firebase
                        db.collection("userBooking").document(bookingId).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("Document ID", bookingId);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });

                        db.collection("latestAppointment")
                                .whereEqualTo("user", bookingUser)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        Map<String, Object> latest = new HashMap<>();
                                        latest.put("slot", bookingSlot);
                                        latest.put("user", bookingUser);
                                        latest.put("date", bookingDate);
                                        latest.put("userStatus", "inactive");
                                        latest.put("location", bookingHospital);
                                        latest.put("status", "completed");

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

                        //Add completed appointment to completedAppointments Document in firebase
                        Map<String, Object> data = new HashMap<>();
                        data.put("slot", bookingSlot);
                        data.put("user", bookingUser);
                        data.put("date", bookingDate);
                        data.put("location", bookingHospital);
                        data.put("status", "completed");


                        db.collection("completedAppointments").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(HospitalViewBookingDetails.this, "Appointment Completed", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(HospitalViewBookingDetails.this, HospitalViewBooking.class);
                                startActivity(i);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(HospitalViewBookingDetails.this, "Fail to mark as Complete " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
        Intent i = new Intent(HospitalViewBookingDetails.this, HospitalViewBooking.class);
        startActivity(i);
        finish();
    }
}