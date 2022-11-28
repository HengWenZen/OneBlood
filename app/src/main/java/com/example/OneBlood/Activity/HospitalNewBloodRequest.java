package com.example.OneBlood.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.OneBlood.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class HospitalNewBloodRequest extends AppCompatActivity {

    public static SharedPreferences hospitalPreferences;
    private final String SHARED_PREFERENCE = "hospitalPreferences";
    private final String KEY_HOSPITAL_ID = "hospitalID";
    private final String KEY_HOSPITAL_NAME = "hospitalName";
    private final String KEY_HOSPITAL_CONTACT = "hospitalContact";
    TextInputLayout etEmergencyDescription, etEmergencyTitle, etNameOfHospital, etEmergencyDate, etEmergencyBloodType;
    AutoCompleteTextView actvEmergencyBloodType;
    String requiredBloodType, title, description, date, nameOfHospital, hospitalContact, bloodType;
    Button btnHospitalPostEmergency;
    ImageView ivBackToHospitalRequestMenu2;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_new_blood_request);

        etEmergencyBloodType = findViewById(R.id.etEmergencyBloodType);
        etEmergencyDate = findViewById(R.id.etEmergencyDate);
        actvEmergencyBloodType = findViewById(R.id.actvEmergencyBloodType);
        etEmergencyDescription = findViewById(R.id.etEmergencyDescription);
        etEmergencyTitle = findViewById(R.id.etEmergencyTitle);
        ivBackToHospitalRequestMenu2 = findViewById(R.id.ivBackToHospitalRequestMenu2);
        etNameOfHospital = findViewById(R.id.etNameOfHospital);
        btnHospitalPostEmergency = findViewById(R.id.btnHospitalPostEmergency);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        String hospital = prefs.getString(KEY_HOSPITAL_NAME, "");
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        //Set the format of the date
        if (day < 10) {
            etEmergencyDate.getEditText().setText( "0" + day + " " + new DateFormatSymbols().getMonths()[month] + " " + year);
        } else {
            etEmergencyDate.getEditText().setText(day + " " + new DateFormatSymbols().getMonths()[month] + " " + year);
        }

        etNameOfHospital.getEditText().setText(hospital);

        final String[] emergencyBloodType = getResources().getStringArray(R.array.requiredBloodType);
        ArrayAdapter EmergencyBloodType = new ArrayAdapter(
                HospitalNewBloodRequest.this,
                R.layout.drop_down_item,
                emergencyBloodType);
        actvEmergencyBloodType.setAdapter(EmergencyBloodType);

        ((AutoCompleteTextView) etEmergencyBloodType.getEditText()).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EmergencyBloodType.getItem(position);
                requiredBloodType = ((AutoCompleteTextView) etEmergencyBloodType.getEditText()).getText().toString();
            }
        });

        btnHospitalPostEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitEmergencyBloodRequest();
            }
        });

        ivBackToHospitalRequestMenu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void submitEmergencyBloodRequest() {
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        String hospital = prefs.getString(KEY_HOSPITAL_NAME, "");
        hospitalContact = prefs.getString(KEY_HOSPITAL_CONTACT, "");

        title = etEmergencyTitle.getEditText().getText().toString();
        description = etEmergencyDescription.getEditText().getText().toString();
        date = etEmergencyDate.getEditText().getText().toString();
        nameOfHospital = etNameOfHospital.getEditText().getText().toString();
        requiredBloodType = ((AutoCompleteTextView) etEmergencyBloodType.getEditText()).getText().toString();

        //Validate user input
        if (TextUtils.isEmpty(title)) {
            etEmergencyTitle.setError("Please fill in Request Title!");
            etEmergencyTitle.requestFocus();
        } else if (TextUtils.isEmpty(description)) {
            etEmergencyDescription.setError("Please fill in Recipient's Name!");
            etEmergencyDescription.requestFocus();
        } else if (requiredBloodType == null) {
            etEmergencyBloodType.setError("Please Select Required Blood Type!");
            etEmergencyBloodType.requestFocus();
        } else {
            Map<String, Object> data = new HashMap<>();
            data.put("title", title);
            data.put("date", date);
            data.put("description", description);
            data.put("blood type", requiredBloodType);
            data.put("postedBy", nameOfHospital);
            data.put("location", nameOfHospital);
            data.put("contact", hospitalContact);

            if(requiredBloodType.equals("A+")){
                requiredBloodType = "A_Positive";
            }else if(requiredBloodType.equals("B+")) {
                requiredBloodType = "B_Positive";
            }else if(requiredBloodType.equals("AB+")) {
                requiredBloodType = "AB_Positive";
            }else if(requiredBloodType.equals("O+")) {
                requiredBloodType = "O_Positive";
            }else if(requiredBloodType.equals("A-")) {
                requiredBloodType = "A_Negative";
            }else if(requiredBloodType.equals("B-")) {
                requiredBloodType = "B_Negative";
            }else if(requiredBloodType.equals("AB-")) {
                requiredBloodType = "AB_Negative";
            }else if(requiredBloodType.equals("O-")) {
                requiredBloodType = "O_Negative";
            }

            //Insert data into database
            db.collection("emergencyRequest").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(HospitalNewBloodRequest.this, "Request Submitted Successfully!" + requiredBloodType, Toast.LENGTH_SHORT).show();
                    sentPush("Emergency Request");
                    Intent intent = new Intent(HospitalNewBloodRequest.this, HospitalBloodRequestMenu.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(HospitalNewBloodRequest.this, "Fail to Submit Request!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    public void sentPush(String title){

        RequestQueue mRequestQue = Volley.newRequestQueue(this);

        // Create the json object to store the notification details //
        JSONObject json = new JSONObject();
        try
        {
            // Set the topic, title and body of the notification //
            json.put("to", "/topics/" + "user");
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title", "Emergency Request From " + nameOfHospital);
            notificationObj.put("body", title);

            json.put("notification", notificationObj);

            // Set the credentials to send the notification API //
            String URL = "https://fcm.googleapis.com/fcm/send";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    response -> Log.d("MUR", "onResponse: " + response.toString()),
                    error -> Log.d("MUR", "onError: " + error.networkResponse)
            ) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=AAAA0RiHfKQ:APA91bGBj2PS5EEmgcFr5UBIRX0nxZLyCTbdYuhY1KikYOpyqqJLmzJtSdoNHJDcqPwehtNZjUNM1F-7V7o32Sp0yaVRacNO_7kNdp0fc4ylHGNeRJnrO5DWjGLOyJkYY5WjuGJmMWM4");
                    return header;
                }
            };
            mRequestQue.add(request);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        //Initialize alert dialog
        new AlertDialog.Builder(this)
                .setMessage("Cancel Blood Request Posting?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(HospitalNewBloodRequest.this, HospitalBloodRequestMenu.class);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).create().show();
    }
}