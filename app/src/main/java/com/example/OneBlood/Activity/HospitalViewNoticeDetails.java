package com.example.OneBlood.Activity;

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

import com.example.OneBlood.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

public class HospitalViewNoticeDetails extends AppCompatActivity {

    public static final String EXTRA_NOTICE_TITLE = "noticeTitle";
    public static final String EXTRA_NOTICE_DATE = "noticeDate";
    public static final String EXTRA_NOTICE_DESCRIPTION = "noticeDescription";
    public static final String EXTRA_NOTICE_HOSPITAL = "hospitalName";
    public static final String EXTRA_NOTICE_ID = "noticeID";

    Button btnDeleteNotice;
    ImageView backToHospitalMenu;
    TextInputLayout etViewNoticeTitle, etViewNoticeDescription, etViewHospitalName, etNoticeDate;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String noticeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_view_notice_details);

        etViewHospitalName = findViewById(R.id.etHospitalName);
        etViewNoticeDescription = findViewById(R.id.etHospitalViewNoticeDescription);
        etViewNoticeTitle = findViewById(R.id.etHospitalViewNoticeTitle);
        etNoticeDate = findViewById(R.id.etHospitalViewNoticeDate);
        btnDeleteNotice = findViewById(R.id.btnDeleteNotice);
        backToHospitalMenu =findViewById(R.id.ivBackToNoticeMenu);
        noticeID = getIntent().getStringExtra(EXTRA_NOTICE_ID);
        Log.d("TAG", "onCreate: " + noticeID);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        etViewNoticeTitle.getEditText().setText((String)b.get(EXTRA_NOTICE_TITLE));
        etViewNoticeTitle.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etViewNoticeDescription.getEditText().setText((String)b.get(EXTRA_NOTICE_DESCRIPTION));
        etViewNoticeDescription.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etNoticeDate.getEditText().setText((String)b.get(EXTRA_NOTICE_DATE));
        etNoticeDate.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etViewHospitalName.getEditText().setText((String)b.get(EXTRA_NOTICE_HOSPITAL));
        etViewHospitalName.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));

        btnDeleteNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRequest();
            }
        });

        backToHospitalMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void deleteRequest() {
        new AlertDialog.Builder(HospitalViewNoticeDetails.this)
                .setMessage("Confirm Deletion of Notice?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //Delete Notice in the FireStore Database
                        db.collection("notice").document(noticeID).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(HospitalViewNoticeDetails.this, "Request Deleted Successfully!", Toast.LENGTH_SHORT).show();
                                        Log.d("Document ID", noticeID);
                                        Intent i = new Intent(HospitalViewNoticeDetails.this, HospitalViewNotice.class);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(HospitalViewNoticeDetails.this, "Fail to Delete Request ! " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
        Intent i = new Intent(HospitalViewNoticeDetails.this, HospitalViewNotice.class);
        startActivity(i);
        finish();
    }
}