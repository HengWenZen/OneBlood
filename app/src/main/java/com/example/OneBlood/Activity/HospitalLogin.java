package com.example.OneBlood.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.OneBlood.Firebase;
import com.example.OneBlood.R;
import com.example.OneBlood.UserLogin;
import com.example.OneBlood.UserType;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class HospitalLogin extends AppCompatActivity {

    TextInputLayout etPassword;
    Spinner spHospital;
    Button btnLogin;
    TextView tvUserLogin;
    Firebase db = new Firebase();
    FirebaseFirestore database = FirebaseFirestore.getInstance();

    boolean loginSuccess = false;
    public static SharedPreferences hospitalPreferences;
    private final String SHARED_PREFERENCE = "hospitalPreferences";
    private final String KEY_HOSPITAL_NAME = "hospitalName";
    private final String KEY_HOSPITAL_CONTACT = "hospitalContact";
    String hospitalContact, hospitalName, hospital;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_login);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        spHospital = findViewById(R.id.spHospital);
        etPassword = findViewById(R.id.etHospitalPassword);
        tvUserLogin = (TextView) findViewById(R.id.tvHospitalUserLogin);

        hospitalPreferences = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        hospital = hospitalPreferences.getString(KEY_HOSPITAL_NAME, "");

        ArrayAdapter<String> hospitalAdapter = new ArrayAdapter<>(HospitalLogin.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.hospital));
        hospitalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spHospital.setAdapter(hospitalAdapter);

        btnLogin.setOnClickListener(view ->{
            Login();
        });

        tvUserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HospitalLogin.this, UserLogin.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void Login() {

            db.getData("location",null, new com.example.OneBlood.MyCallback(){
                @Override
                public void returnData(ArrayList<Map<String, Object>> docList) {
                    Log.d("Documents Retrieved", docList.toString());
                    ArrayList<String> list = new ArrayList<>();

                    for(Map<String, Object> map : docList){
                        //validate user input
                        if (map.get("name").toString().equals(spHospital.getSelectedItem().toString()) && map.get("password").toString().equals(etPassword.getEditText().getText().toString())) {
                            Toast.makeText(HospitalLogin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            hospitalContact = map.get("contact").toString();
                            hospitalName = spHospital.getSelectedItem().toString();

                            Intent i = new Intent(HospitalLogin.this, HospitalMenu.class);
                            startActivity(i);
                            finish();
                            loginSuccess = true;

                            //store details into share preference
                            SharedPreferences.Editor editor = hospitalPreferences.edit();
                            editor.putString(KEY_HOSPITAL_NAME, hospitalName);
                            editor.putString(KEY_HOSPITAL_CONTACT, hospitalContact);
                            editor.apply();
                        }
                    }
                    if(loginSuccess == false){
                        Toast.makeText(HospitalLogin.this, "Error" , Toast.LENGTH_SHORT).show();
                    }
                }
            });

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(HospitalLogin.this, UserType.class);
        startActivity(i);
        finish();
    }
}




