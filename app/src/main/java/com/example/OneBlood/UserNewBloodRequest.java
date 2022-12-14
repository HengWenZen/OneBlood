package com.example.OneBlood;

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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.OneBlood.Activity.HospitalBloodRequestMenu;
import com.example.OneBlood.Activity.HospitalNewBloodRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class UserNewBloodRequest extends AppCompatActivity {

    public static SharedPreferences mPreferences;
    private final String SHARED_PREF = "myPreferences";
    private final String KEY_USER = "user";
    private final String KEY_USER_NAME = "userName";

    TextView tvRequestDate;
    TextInputLayout etRecipientName, etDescription, etLocation, etTitle, etRequiredBloodType, etRecipientContact, etRecipientEmail;
    AutoCompleteTextView actvRequiredBloodType;
    ImageView ivBackToRequestMenu;
    Button btnSubmit;
    String getRequiredBloodType, recipientName, requestTitle, requestLocation, requestDescription, recipientEmail, recipientContact;
    String phoneFormat,getUserPhoneNo;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Firebase database = new Firebase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_new_blood_request);

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
        ivBackToRequestMenu = findViewById(R.id.ivBackToRequestMenu);

        SharedPreferences prefs = getSharedPreferences("myPreferences", MODE_PRIVATE);
        recipientName = prefs.getString(KEY_USER_NAME, null);

        etRecipientName.getEditText().setText(recipientName);

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
                UserNewBloodRequest.this,
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

        ivBackToRequestMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void submitBloodRequest() {
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
            etRecipientContact.setError("Please fill in Recipient's Contact!");
            etRecipientContact.requestFocus();
        }else if (TextUtils.isEmpty(recipientEmail)) {
            etRecipientEmail.setError("Please fill in Recipient's Email!");
            etRecipientEmail.requestFocus();
        }else if (TextUtils.isEmpty(requestLocation)) {
            etLocation.setError("Please fill in Location!");
            etLocation.requestFocus();
        }else if (getRequiredBloodType == null) {
            etRequiredBloodType.setError("Please Select Required Blood Type!");
            etRequiredBloodType.requestFocus();
        }else{

            getUserPhoneNo = "+6" + recipientContact;
            phoneFormat = "(01)[0-46-9]*[0-9]{7,8}$";
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

            if (!recipientContact.matches(phoneFormat)) {
                //Validate phone number
                Toast.makeText(this, "Invalid Phone Number!", Toast.LENGTH_SHORT).show();
            }else if(!recipientEmail.matches(emailPattern)){
                Toast.makeText(this, "Email is badly formatted!", Toast.LENGTH_SHORT).show();
            }else {
                Map<String, Object> data = new HashMap<>();
                data.put("location", requestLocation);
                data.put("date", date);
                data.put("description", requestDescription);
                data.put("blood type", getRequiredBloodType);
                data.put("postedBy", recipientName);
                data.put("contact", recipientContact);
                data.put("title", requestTitle);

                if (getRequiredBloodType.equals("A+")) {
                    getRequiredBloodType = "A_Positive";
                } else if (getRequiredBloodType.equals("B+")) {
                    getRequiredBloodType = "B_Positive";
                } else if (getRequiredBloodType.equals("AB+")) {
                    getRequiredBloodType = "AB_Positive";
                } else if (getRequiredBloodType.equals("O+")) {
                    getRequiredBloodType = "O_Positive";
                } else if (getRequiredBloodType.equals("A-")) {
                    getRequiredBloodType = "A_Negative";
                } else if (getRequiredBloodType.equals("B-")) {
                    getRequiredBloodType = "B_Negative";
                } else if (getRequiredBloodType.equals("AB-")) {
                    getRequiredBloodType = "AB_Negative";
                } else if (getRequiredBloodType.equals("O-")) {
                    getRequiredBloodType = "O_Negative";
                }

                //Insert data into database
                db.collection("emergencyRequest").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(UserNewBloodRequest.this, "Request Submitted Successfully!", Toast.LENGTH_SHORT).show();
                        sentPush(requestTitle);
                        Intent intent = new Intent(UserNewBloodRequest.this, UserViewBloodRequest.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserNewBloodRequest.this, "Fail to Submit Request!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
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
            notificationObj.put("title", "Emergency Request From " + recipientName);
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
        new AlertDialog.Builder(this)
                .setMessage("Cancel Blood Request Posting?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(UserNewBloodRequest.this, UserBloodRequestMainMenu.class);
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