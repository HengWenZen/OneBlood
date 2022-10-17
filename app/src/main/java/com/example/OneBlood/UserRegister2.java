package com.example.OneBlood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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
    Firebase db = new Firebase();
    String userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register2);

        btnNext = findViewById(R.id.btnNext);
        radioGroup = findViewById(R.id.rgUserGender);
        datePicker = findViewById(R.id.dpDateOfBirth);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        userName = (String)b.get(EXTRA_USER_NAME);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterUser();
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
}