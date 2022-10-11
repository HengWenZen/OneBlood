package com.example.OneBlood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.OneBlood.Activity.HospitalMenu;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class HospitalNewNotice extends AppCompatActivity {

    public static SharedPreferences hospitalPreferences;
    private final String SHARED_PREFERENCE = "hospitalPreferences";
    private final String KEY_HOSPITAL_ID = "hospitalID";
    private final String KEY_HOSPITAL_NAME = "hospitalName";
    TextInputLayout etNoticeDescription, etNoticeTitle,etHospitalName;
    Button btnHospitalSubmitNotice;
    ImageView ivBackToMenu;
    TextView tvNoticeDate;
    String noticeTitle, noticeDescription, hospitalName, noticePostedBy;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Firebase database = new Firebase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_new_notice);
        etNoticeDescription = findViewById(R.id.etNoticeDescription);
        etNoticeTitle = findViewById(R.id.etNoticeTitle);
        tvNoticeDate = findViewById(R.id.tvNoticeDate);
        ivBackToMenu = findViewById(R.id.ivBackToMenu);
        btnHospitalSubmitNotice = findViewById(R.id.btnHospitalPostNotice);
        etHospitalName = findViewById(R.id.etHospitalName);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        String hospital = prefs.getString(KEY_HOSPITAL_NAME, "");

        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        //Set the format of the date
        if (day < 10) {
            tvNoticeDate.setText("Date of Request : " + "0" + day + " " + new DateFormatSymbols().getMonths()[month] + " " + year);
        } else {
            tvNoticeDate.setText("Date of Request : " + day + " " + new DateFormatSymbols().getMonths()[month] + " " + year);
        }

        etHospitalName.getEditText().setText(hospital);

        btnHospitalSubmitNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitNotice();
            }
        });

    }

    private void submitNotice() {
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        String hospital = prefs.getString(KEY_HOSPITAL_NAME, "");

        noticeTitle = etNoticeTitle.getEditText().getText().toString().trim();
        noticeDescription = etNoticeDescription.getEditText().getText().toString().trim();

        //Get current day
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        Date setDate = new GregorianCalendar(year, month, day).getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String date = formatter.format(setDate);

        if(TextUtils.isEmpty(noticeTitle)){
            etNoticeTitle.setError("Please fill in title!");
            etNoticeTitle.requestFocus();
        }else if(TextUtils.isEmpty(noticeDescription)){
            etNoticeDescription.setError("Please fill in description!");
            etNoticeDescription.requestFocus();
        }else{
            Map<String, Object> data = new HashMap<>();
            data.put("title", noticeTitle);
            data.put("description", noticeDescription);
            data.put("date", date);
            data.put("hospitalName", hospital);

            db.collection("notice")
                    .add(data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(HospitalNewNotice.this, "Notice Posted Successfully!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(HospitalNewNotice.this, HospitalMenu.class);
                            startActivity(i);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(HospitalNewNotice.this, "Fail to Submit Request!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}