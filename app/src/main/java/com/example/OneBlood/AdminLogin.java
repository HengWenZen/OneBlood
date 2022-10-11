package com.example.OneBlood;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.OneBlood.Activity.AdminMenu;

import java.util.ArrayList;
import java.util.Map;

public class AdminLogin extends AppCompatActivity {
    EditText etAdminPassword;
    Button btnAdminLogin;
    TextView tvUserLogin;
    boolean loginSuccess = false;
    String adminPassword;
    Firebase db = new Firebase();

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        etAdminPassword = findViewById(R.id.etPassword);
        btnAdminLogin = findViewById(R.id.btnAdminLogin);
        tvUserLogin = findViewById(R.id.tvUserLogin);

        tvUserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminLogin.this, UserLogin.class);
                startActivity(i);
                finish();
            }
        });

        btnAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginValidation();
                }
        });

    }

    private void LoginValidation() {
        adminPassword = etAdminPassword.getText().toString();
        if (TextUtils.isEmpty(adminPassword))
        {
            etAdminPassword.setError("Please fill in Password!");
            etAdminPassword.requestFocus();

        } else{
            db.getData("admin", null, new com.example.OneBlood.MyCallback() {
                @Override
                public void returnData(ArrayList<Map<String, Object>> docList) {
                    Log.d("Documents Retrieved", docList.toString());
                    ArrayList<String> list = new ArrayList<>();

                    for (Map<String, Object> map : docList) {
                        if (map.get("password").toString().equals(etAdminPassword.getText().toString())) {
                            Toast.makeText(AdminLogin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(AdminLogin.this, AdminMenu.class);
                            startActivity(i);
                            finish();
                            loginSuccess = true;
                        }
                        if (loginSuccess == false) {
                            Toast.makeText(AdminLogin.this, "Wrong Password !", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }
}

