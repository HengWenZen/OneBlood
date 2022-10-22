package com.example.OneBlood.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.ContentProviderClient;
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

public class BloodRequestDetails extends AppCompatActivity {
    public static final String EXTRA_OWN_REQUEST_DATE = "requestDate";
    public static final String EXTRA_OWN_REQUEST_TITLE = "requestTitle";
    public static final String EXTRA_OWN_REQUEST_DESCRIPTION = "requestDescription";
    public static final String EXTRA_OWN_REQUEST_BLOOD = "requestBloodType";
    public static final String EXTRA_OWN_REQUEST_POSTED_BY = "requestPostedBy";
    public static final String EXTRA_OWN_REQUEST_CONTACT = "requestContact";
    public static final String EXTRA_OWN_REQUEST_LOCATION = "requestLocation";
    public static final String EXTRA_OWN_REQUEST_ID = "requestId";

    TextInputLayout etUserRequestTitle, etUserRequestDate, etUserRequestDescription, etUserRequestContact, etUserRequestLocation, etUserRequestPostedBy, etUserRequestedBloodType;
    Button btnDeleteRequest;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String requestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_blood_request_details);
        etUserRequestTitle = findViewById(R.id.etUserRequestTitle);
        etUserRequestDate = findViewById(R.id.etUserRequestDate);
        etUserRequestDescription = findViewById(R.id.etUserRequestDescription);
        etUserRequestContact = findViewById(R.id.etUserRequestContact);
        etUserRequestLocation = findViewById(R.id.etUserRequestLocation);
        etUserRequestPostedBy = findViewById(R.id.etPostedBy);
        etUserRequestedBloodType = findViewById(R.id.etUserRequestedBloodType);
        btnDeleteRequest = findViewById(R.id.btnDeleteRequest);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        requestId = (String) b.get(EXTRA_OWN_REQUEST_ID);
        Log.d("TAG", "onCreate: " + requestId);

        etUserRequestTitle.getEditText().setText((String) b.get(EXTRA_OWN_REQUEST_TITLE));
        etUserRequestTitle.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etUserRequestDate.getEditText().setText((String) b.get(EXTRA_OWN_REQUEST_DATE));
        etUserRequestDate.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etUserRequestDescription.getEditText().setText((String) b.get(EXTRA_OWN_REQUEST_DESCRIPTION));
        etUserRequestDescription.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etUserRequestContact.getEditText().setText((String) b.get(EXTRA_OWN_REQUEST_CONTACT));
        etUserRequestContact.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etUserRequestLocation.getEditText().setText((String) b.get(EXTRA_OWN_REQUEST_LOCATION));
        etUserRequestLocation.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etUserRequestPostedBy.getEditText().setText((String) b.get(EXTRA_OWN_REQUEST_POSTED_BY));
        etUserRequestPostedBy.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etUserRequestedBloodType.getEditText().setText((String) b.get(EXTRA_OWN_REQUEST_BLOOD));
        etUserRequestedBloodType.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));

        btnDeleteRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelBooking();
            }
        });


    }

    private void cancelBooking() {
        new AlertDialog.Builder(BloodRequestDetails.this)
                .setMessage("Confirm Appointment Cancellation?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        db.collection("emergencyRequest").document(requestId).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(BloodRequestDetails.this, "Request Deleted Successfully!", Toast.LENGTH_SHORT).show();
                                        Log.d("Document ID", requestId);
                                        Intent i = new Intent(BloodRequestDetails.this, UserViewBloodRequest.class);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(BloodRequestDetails.this, "Fail to Delete Request ! " + e.getMessage(), Toast.LENGTH_SHORT).show();
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