package com.example.OneBlood.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.OneBlood.Firebase;
import com.example.OneBlood.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class NewBloodRequest extends AppCompatActivity {

    TextView tvRequestDate;
    TextInputLayout etRecipientName, etDescription, etLocation, etTitle, etRequiredBloodType, etRecipientContact, etRecipientEmail;
    AutoCompleteTextView actvRequiredBloodType;
    Button btnSubmit;
    String getRequiredBloodType, recipientName, requestTitle, requestLocation, requestDescription, recipientEmail, recipientContact;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Firebase database = new Firebase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_blood_request);

        tvRequestDate = findViewById(R.id.tvRequestDate);
        etDescription = findViewById(R.id.etDescription);
        etRecipientName = findViewById(R.id.etRecipientName);
        etRecipientContact = findViewById(R.id.etRecipientContact);
        etRecipientEmail = findViewById(R.id.etRecipientEmail);
        etLocation = findViewById(R.id.etLocation);
        etTitle = findViewById(R.id.etTitle);
        etRequiredBloodType = findViewById(R.id.etRequiredBloodType);
        actvRequiredBloodType = findViewById(R.id.actvRequiredBloodType);
        btnSubmit = findViewById(R.id.btnSubmit);

        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        //Set the format of the date
        if (day < 10) {
            tvRequestDate.setText("Date of Request : " + "0" + day + " " + new DateFormatSymbols().getMonths()[month] + " " + year);
        } else {
            tvRequestDate.setText("Date of Request : " + day + " " + new DateFormatSymbols().getMonths()[month] + " " + year);
        }

        final String[] requiredBloodType = getResources().getStringArray(R.array.requiredBloodType);
        ArrayAdapter BloodType = new ArrayAdapter<>(
                NewBloodRequest.this,
                R.layout.drop_down_item,
                requiredBloodType);
        actvRequiredBloodType.setAdapter(BloodType);

        ((AutoCompleteTextView) etRequiredBloodType.getEditText()).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get selected blood type
                BloodType.getItem(position);
                getRequiredBloodType = ((AutoCompleteTextView) etRequiredBloodType.getEditText()).getText().toString();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validate Blood Request
                submitBloodRequest();
            }
        });
    }

    private void submitBloodRequest() {
        recipientName = etRecipientName.getEditText().getText().toString().trim();
        recipientContact = etRecipientContact.getEditText().getText().toString().trim();
        recipientEmail = etRecipientEmail.getEditText().getText().toString().trim();
        requestDescription = etDescription.getEditText().getText().toString().trim();
        requestLocation = etLocation.getEditText().getText().toString().trim();
        requestTitle = etTitle.getEditText().getText().toString().trim();

        //Get current day
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        Date setDate = new GregorianCalendar(year, month, day).getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String date = formatter.format(setDate);

        //Validate User Input
        if (TextUtils.isEmpty(requestTitle)) {
            etTitle.setError("Please fill in Request Title!");
            etTitle.requestFocus();
        }else if (TextUtils.isEmpty(recipientName)) {
            etRecipientName.setError("Please fill in Recipient's Name!");
            etRecipientName.requestFocus();
        } else if (TextUtils.isEmpty(recipientContact)) {
            etRecipientName.setError("Please fill in Recipient's Contact!");
            etRecipientName.requestFocus();
        }else if (TextUtils.isEmpty(recipientEmail)) {
            etRecipientEmail.setError("Please fill in Request Location!");
            etRecipientEmail.requestFocus();
        }else if (getRequiredBloodType == null) {
            etRequiredBloodType.setError("Please Select Required Blood Type!");
            etRequiredBloodType.requestFocus();
        }else{

            Map<String, Object> data = new HashMap<>();
            data.put("location", requestLocation);
            data.put("date", date);
            data.put("description", requestDescription);
            data.put("blood type" , getRequiredBloodType);
            data.put("recipient" , recipientName);
            data.put("contact" , recipientContact);
            data.put("email" , recipientEmail);
            data.put("title",requestTitle);

            //Insert data into database
            db.collection("bloodRequest").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(NewBloodRequest.this, "Request Submitted Successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(NewBloodRequest.this, "Fail to Submit Request!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}