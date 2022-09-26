package com.example.OneBlood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserRegister3 extends AppCompatActivity {

    Button btnRegisterUser3;
    TextInputLayout etUserPhone, etBloodType;
    AutoCompleteTextView actvBloodType;
    String userPhoneNo, getUserPhoneNo, getSelectedBloodType;
    Firebase db = new Firebase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register3);

        btnRegisterUser3 = findViewById(R.id.btnRegisterAcc3);
        etUserPhone = findViewById(R.id.etUserPhone);
        etBloodType = findViewById(R.id.spBloodType);
        actvBloodType = findViewById(R.id.actvBloodType);
        final String[] bloodType = getResources().getStringArray(R.array.bloodType);

        ArrayAdapter<String> hospitalAdapter = new ArrayAdapter<>(
                UserRegister3.this,
                R.layout.drop_down_item,
                bloodType);
        actvBloodType.setAdapter(hospitalAdapter);


        btnRegisterUser3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterUser();
            }
        });

        ((AutoCompleteTextView) etBloodType.getEditText()).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get selected item
               hospitalAdapter.getItem(position);
               getSelectedBloodType =  ((AutoCompleteTextView) etBloodType.getEditText()).getText().toString();
            }
        });

    }

    private void RegisterUser() {
        //validate phone number and blood type
        if (!validatePhoneNumber() | !validateBloodType()) {
            return;
        } else {
            Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(UserRegister3.this, UserLogin.class);
            startActivity(i);
            finish();
        }
    }

    private boolean validatePhoneNumber() {
        userPhoneNo = etUserPhone.getEditText().getText().toString().trim();
        getUserPhoneNo = "+6" + userPhoneNo;
        String phoneFormat = "(01)[0-46-9]*[0-9]{7,8}$";

        if (TextUtils.isEmpty(userPhoneNo)) {
            //Check if phone number is empty
            Toast.makeText(this, "Please fill in Phone number", Toast.LENGTH_SHORT).show();
            return false;

        } else if (!userPhoneNo.matches(phoneFormat)) {
            //Validate phone number
            Toast.makeText(this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
            return false;

        } else {
            //insert Data into Database
            db.getData("users", null, new MyCallback() {
                @Override
                public void returnData(ArrayList<Map<String, Object>> docList) {
                    Log.d("firebase example", docList.toString());
                    ArrayList<String> list = new ArrayList<>();

                    for (Map<String, Object> map : docList) {
                        Map<String, Object> user = new HashMap<>();
                        user.put("phone number", getUserPhoneNo);
                        db.updData("users", user, map.get("id").toString());
                    }
                }
            });
            return true;
        }
    }

    private boolean validateBloodType() {
        //Check if the user has selected any item
        if (getSelectedBloodType == null){
            Toast.makeText(this, "Please select Blood Type", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            //insert Data into database
            db.getData("users", null, new MyCallback() {
                @Override
                public void returnData(ArrayList<Map<String, Object>> docList) {
                    Log.d("firebase example", docList.toString());
                    ArrayList<String> list = new ArrayList<>();

                    for (Map<String, Object> map : docList) {
                        Map<String, Object> user = new HashMap<>();
                        user.put("blood type", getSelectedBloodType);
                        db.updData("users", user, map.get("id").toString());
                    }
                }
            });
            return true;
        }
    }
}