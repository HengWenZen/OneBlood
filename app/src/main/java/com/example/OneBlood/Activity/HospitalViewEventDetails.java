package com.example.OneBlood.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.OneBlood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class HospitalViewEventDetails extends AppCompatActivity {
    public static final String EXTRA_EVENT_TITLE = "title";
    public static final String EXTRA_EVENT_LOCATION = "location";
    public static final String EXTRA_EVENT_DESCRIPTION = "description";
    public static final String EXTRA_EVENT_START_DATE = "startDate";
    public static final String EXTRA_EVENT_END_DATE = "endDate";
    public static final String EXTRA_EVENT_START_TIME = "startTime";
    public static final String EXTRA_EVENT_END_TIME = "endTime";
    public static final String EXTRA_EVENT_POSTED_BY = "postedBy";
    public static final String EXTRA_EVENT_ID = "eventId";
    public static final String EXTRA_EVENT_IMAGE_URI = "eventImageUri";

    TextInputLayout etHospitalViewTitle, etHospitalViewDescription, etHospitalViewLocation, etHospitalViewStartDate, etHospitalViewEndDate, etHospitalViewStartTime,etHospitalViewEndTime, etHospitalViewPostedBy;
    Button btnDeleteEvent;
    ImageView ivHospitalViewEventPic, ivBackToEventList;
    StorageReference storageReference;
    String  eventTitle, eventLocation, eventStartDate, eventEndDate, eventStartTime, eventEndTime, eventPostedBy, eventDescription, eventId, eventImageId;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_view_event_details);

        etHospitalViewDescription = findViewById(R.id.etHospitalViewDescription);
        etHospitalViewTitle = findViewById(R.id.etHospitalViewTitle);
        etHospitalViewLocation = findViewById(R.id.etHospitalViewLocation);
        etHospitalViewStartDate = findViewById(R.id.etHospitalViewStartDate);
        etHospitalViewEndDate = findViewById(R.id.etHospitalViewEndDate);
        etHospitalViewStartTime = findViewById(R.id.etHospitalViewStartTime);
        etHospitalViewEndTime = findViewById(R.id.etHospitalViewEndTime);
        etHospitalViewPostedBy = findViewById(R.id.etHospitalViewPostedBy);
        ivHospitalViewEventPic = findViewById(R.id.ivHospitalViewEventPic);
        ivBackToEventList = findViewById(R.id.ivBackToEventList);
        btnDeleteEvent = findViewById(R.id.btnDeleteEvent);

        eventEndDate = getIntent().getStringExtra(EXTRA_EVENT_END_DATE);
        eventStartDate = getIntent().getStringExtra(EXTRA_EVENT_START_DATE);
        eventTitle = getIntent().getStringExtra(EXTRA_EVENT_TITLE);
        eventStartTime = getIntent().getStringExtra(EXTRA_EVENT_START_TIME);
        eventEndTime = getIntent().getStringExtra(EXTRA_EVENT_END_TIME);
        eventPostedBy= getIntent().getStringExtra(EXTRA_EVENT_POSTED_BY);
        eventLocation = getIntent().getStringExtra(EXTRA_EVENT_LOCATION);
        eventDescription= getIntent().getStringExtra(EXTRA_EVENT_DESCRIPTION);
        eventId = getIntent().getStringExtra(EXTRA_EVENT_ID);
        eventImageId = getIntent().getStringExtra(EXTRA_EVENT_IMAGE_URI);

        etHospitalViewDescription.getEditText().setText(eventDescription);
        etHospitalViewDescription.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etHospitalViewTitle.getEditText().setText(eventTitle);
        etHospitalViewTitle.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etHospitalViewStartDate.getEditText().setText(eventStartDate);
        etHospitalViewStartDate.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etHospitalViewEndDate.getEditText().setText(eventEndDate);
        etHospitalViewEndDate.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etHospitalViewStartTime.getEditText().setText(eventStartTime);
        etHospitalViewStartTime.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etHospitalViewEndTime.getEditText().setText(eventEndTime);
        etHospitalViewEndTime.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etHospitalViewLocation.getEditText().setText(eventLocation);
        etHospitalViewLocation.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etHospitalViewPostedBy.getEditText().setText(eventPostedBy);
        etHospitalViewPostedBy.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));

        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference ref
                = storageReference
                .child(
                        "images/"
                                + eventImageId);
        try {
            File localfile = File.createTempFile("tempfile", ".jpg");
            ref.getFile(localfile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                            ivHospitalViewEventPic.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


        btnDeleteEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEvent();
            }
        });

        ivBackToEventList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void deleteEvent() {
        new AlertDialog.Builder(HospitalViewEventDetails.this)
                .setMessage("Cancel Event?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //Delete all User's Booking
                        db.collection("userEventBooking")
                                .whereEqualTo("title", eventTitle)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            QuerySnapshot result = task.getResult();
                                            if (!result.isEmpty()) {
                                                Log.d("Data Retrieved", result.toString());
                                                for (QueryDocumentSnapshot document : result) {
                                                    String title = document.get("title").toString();
                                                    String docId = document.getId();
                                                    if(eventTitle.equals(title)) {
                                                       db.collection("userEventBooking")
                                                               .document(docId)
                                                               .delete()
                                                               .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                           @Override
                                                           public void onComplete(@NonNull Task<Void> task) {
                                                               Log.d("TAG", "onComplete: Document Deleted Successfully");
                                                           }
                                                       }).addOnFailureListener(new OnFailureListener() {
                                                           @Override
                                                           public void onFailure(@NonNull Exception e) {

                                                           }
                                                       });
                                                    }
                                                }
                                            }
                                        }
                                    }
                                });

                        //Delete the Image in Firebase Storage
                        storageReference = FirebaseStorage.getInstance().getReference();
                        StorageReference ref
                                = storageReference
                                .child(
                                        "images/"
                                                + eventImageId);
                        ref.delete();

                        //Delete the Event in FireStore Database
                        db.collection("events").document(eventId).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(HospitalViewEventDetails.this, "Event Deleted Successfully!", Toast.LENGTH_SHORT).show();
                                        Log.d("Document ID",eventId);
                                        Intent i = new Intent(HospitalViewEventDetails.this, HospitalViewEvent.class);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(HospitalViewEventDetails.this, "Fail to Delete Event! " + e.getMessage() , Toast.LENGTH_SHORT).show();
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
        Intent i = new Intent(HospitalViewEventDetails.this, HospitalViewEvent.class);
        startActivity(i);
        finish();
    }
}