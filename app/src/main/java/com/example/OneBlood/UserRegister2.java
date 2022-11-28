package com.example.OneBlood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserRegister2 extends AppCompatActivity {

    public static final String EXTRA_USER_NAME = "userName";

    Button btnNext;
    RadioButton selectedGender;
    RadioGroup radioGroup;
    DatePicker datePicker;
    ImageView ivBack;
    Firebase db = new Firebase();
    FirebaseFirestore mFirebase = FirebaseFirestore.getInstance();
    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mFirebaseAuth.getCurrentUser();
    String userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register2);

        btnNext = findViewById(R.id.btnNext);
        radioGroup = findViewById(R.id.rgUserGender);
        datePicker = findViewById(R.id.dpDateOfBirth);
        ivBack = findViewById(R.id.ivBack2);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        userName = (String)b.get(EXTRA_USER_NAME);
        String userEmail = user.getEmail();
        Log.d("TAG", "onClick: " + userEmail);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterUser();
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void RegisterUser() {
        if(!validateGender() | !validateAge()){
            return;
        }else {
            Intent i = new Intent(UserRegister2.this, UserRegister3.class);
            i.putExtra(EXTRA_USER_NAME, userName);
            startActivity(i);
            finish();
        }
    }

    private boolean validateGender(){
        //Check if user has checked one of the button
        if(radioGroup.getCheckedRadioButtonId() ==-1){
            Toast.makeText(this, "Please select Gender", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            selectedGender = findViewById(radioGroup.getCheckedRadioButtonId());
            String gender = selectedGender.getText().toString();

            //insert data into database
            db.getData("users", null, new MyCallback() {
                @Override
                public void returnData(ArrayList<Map<String, Object>> docList) {
                    Log.d("firebase example", docList.toString());
                    ArrayList<String> list = new ArrayList<>();

                    for (Map<String, Object> map : docList) {
                        if(map.get("FullName").toString().equals(userName)) {
                            Map<String, Object> user = new HashMap<>();
                            user.put("Gender", gender);
                            db.updData("users", user, map.get("id").toString());
                        }
                    }
                }
            });
            return true;
        }
    }

    private boolean validateAge(){
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int userAge = datePicker.getYear();
        int isAgeValid = currentYear - userAge;

        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        String DOB = day + " / " + month + " / " + year;

        //Check if user is aged 18 or above
        if(isAgeValid < 18){

            Toast.makeText(this, "User must be age 18 or above!", Toast.LENGTH_SHORT).show();
            return false;

        }else{
            //Insert data into database
            db.getData("users", null, new MyCallback() {
                @Override
                public void returnData(ArrayList<Map<String, Object>> docList) {
                    Log.d("firebase example", docList.toString());
                    ArrayList<String> list = new ArrayList<>();

                    for (Map<String, Object> map : docList) {
                        if(map.get("FullName").toString().equals(userName)) {
                            Map<String, Object> user = new HashMap<>();
                            user.put("Date of Birth", DOB);
                            db.updData("users", user, map.get("id").toString());
                        }
                    }
                }
            });

            return true;
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(UserRegister2.this)
                .setMessage("Cancel Registration?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("TAG", "User account deleted.");

                                            mFirebase.collection("users")
                                                    .whereEqualTo("FullName", userName)
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            QuerySnapshot queryDocumentSnapshots = task.getResult();
                                                            if (!queryDocumentSnapshots.isEmpty()) {
                                                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                                                    Log.d("Document ID:", document.getId() + " => " + document.getData());
                                                                    mFirebase.collection("users").document(document.getId()).delete();

                                                                }
                                                            }
                                                        }
                                                    });
                                        }
                                        else{
                                            Log.d("TAG", "Fail To Delete.");
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("TAG", "Fail To Delete." + e.getMessage());
                                    }
                                });

                        Intent intent = new Intent(UserRegister2.this, UserRegister.class);
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