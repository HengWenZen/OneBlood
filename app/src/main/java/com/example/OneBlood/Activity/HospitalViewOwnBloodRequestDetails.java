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

import com.example.OneBlood.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

public class HospitalViewOwnBloodRequestDetails extends AppCompatActivity {

    public static final String EXTRA_OWN_REQUEST_DATE = "requestDate";
    public static final String EXTRA_OWN_REQUEST_TITLE = "requestTitle";
    public static final String EXTRA_OWN_REQUEST_DESCRIPTION = "requestDescription";
    public static final String EXTRA_OWN_REQUEST_BLOOD = "requestBloodType";
    public static final String EXTRA_OWN_REQUEST_POSTED_BY = "requestPostedBy";
    public static final String EXTRA_OWN_REQUEST_CONTACT = "requestContact";
    public static final String EXTRA_OWN_REQUEST_LOCATION = "requestLocation";
    public static final String EXTRA_OWN_REQUEST_ID = "requestId";

    TextInputLayout etHospitalRequestTitle, etHospitalRequestDate, etHospitalRequestDescription, etHospitalRequestContact, etHospitalRequestLocation, etHospitalRequestPostedBy, etHospitalRequestedBloodType;
    Button btnHospitalDeleteRequest;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String requestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_blood_request_details);

        etHospitalRequestTitle = findViewById(R.id.etHospitalRequestTitle);
        etHospitalRequestDate = findViewById(R.id.etHospitalRequestDate);
        etHospitalRequestDescription = findViewById(R.id.etHospitalRequestDescription);
        etHospitalRequestContact = findViewById(R.id.etHospitalRequestContact);
        etHospitalRequestLocation = findViewById(R.id.etHospitalRequestLocation);
        etHospitalRequestPostedBy = findViewById(R.id.etHospitalRequestPostedBy);
        etHospitalRequestedBloodType = findViewById(R.id.etHospitalRequestedBloodType);
        btnHospitalDeleteRequest = findViewById(R.id.btnHospitalDeleteRequest);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        requestId = (String) b.get(EXTRA_OWN_REQUEST_ID);
        Log.d("TAG", "onCreate: " + requestId);

        etHospitalRequestTitle.getEditText().setText((String) b.get(EXTRA_OWN_REQUEST_TITLE));
        etHospitalRequestTitle.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etHospitalRequestDate.getEditText().setText((String) b.get(EXTRA_OWN_REQUEST_DATE));
        etHospitalRequestDate.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etHospitalRequestDescription.getEditText().setText((String) b.get(EXTRA_OWN_REQUEST_DESCRIPTION));
        etHospitalRequestDescription.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etHospitalRequestContact.getEditText().setText((String) b.get(EXTRA_OWN_REQUEST_CONTACT));
        etHospitalRequestContact.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etHospitalRequestLocation.getEditText().setText((String) b.get(EXTRA_OWN_REQUEST_LOCATION));
        etHospitalRequestLocation.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etHospitalRequestPostedBy.getEditText().setText((String) b.get(EXTRA_OWN_REQUEST_POSTED_BY));
        etHospitalRequestPostedBy.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etHospitalRequestedBloodType.getEditText().setText((String) b.get(EXTRA_OWN_REQUEST_BLOOD));
        etHospitalRequestedBloodType.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));

        btnHospitalDeleteRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRequest();
            }
        });
    }

    private void deleteRequest() {
        new AlertDialog.Builder(HospitalViewOwnBloodRequestDetails.this)
                .setMessage("Confirm Appointment Cancellation?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        db.collection("emergencyRequest").document(requestId).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(HospitalViewOwnBloodRequestDetails.this, "Request Deleted Successfully!", Toast.LENGTH_SHORT).show();
                                        Log.d("Document ID", requestId);
                                        Intent i = new Intent(HospitalViewOwnBloodRequestDetails.this, HospitalBloodRequestMenu.class);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(HospitalViewOwnBloodRequestDetails.this, "Fail to Delete Request ! " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        Intent i = new Intent(HospitalViewOwnBloodRequestDetails.this, HospitalBloodRequestMenu.class);
        startActivity(i);
        finish();
    }
}