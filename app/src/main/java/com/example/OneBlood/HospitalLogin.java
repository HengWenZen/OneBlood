package com.example.OneBlood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.OneBlood.Activity.HospitalMenu;

import java.util.ArrayList;
import java.util.Map;

public class HospitalLogin extends AppCompatActivity {

    EditText etPassword;
    Spinner spHospital;
    Button btnLogin;
    TextView tvAdminLogin;
    Firebase db = new Firebase();
    boolean loginSuccess = false;
    public static SharedPreferences hospitalPreferences;
    private final String SHARED_PREFERENCE = "hospitalPreferences";
    private final String KEY_HOSPITAL_NAME = "hospitalName";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_login);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        spHospital = findViewById(R.id.spHospital);
        etPassword = findViewById(R.id.etHospitalPassword);
        tvAdminLogin = (TextView) findViewById(R.id.tvAdminLogin);

        hospitalPreferences = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);


        ArrayAdapter<String> hospitalAdapter = new ArrayAdapter<>(HospitalLogin.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.hospital));
        hospitalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spHospital.setAdapter(hospitalAdapter);

        btnLogin.setOnClickListener(view ->{
            Login();
        });
    }

    private void Login() {

            db.getData("hospitals",null, new com.example.OneBlood.MyCallback(){
                @Override
                public void returnData(ArrayList<Map<String, Object>> docList) {
                    Log.d("Documents Retrieved", docList.toString());
                    ArrayList<String> list = new ArrayList<>();

                    for(Map<String, Object> map : docList){
                        if (map.get("name").toString().equals(spHospital.getSelectedItem().toString()) && map.get("password").toString().equals(etPassword.getText().toString())) {
                            Toast.makeText(HospitalLogin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(HospitalLogin.this, HospitalMenu.class);
                            startActivity(i);
                            finish();
                            loginSuccess = true;

                            SharedPreferences.Editor editor = hospitalPreferences.edit();
                            editor.putString(KEY_HOSPITAL_NAME, spHospital.getSelectedItem().toString());
                            editor.apply();
                        }
                    }
                    if(loginSuccess == false){
                        Toast.makeText(HospitalLogin.this, "Error" , Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
}




