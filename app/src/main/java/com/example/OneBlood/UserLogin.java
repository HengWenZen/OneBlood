package com.example.OneBlood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.OneBlood.Activity.HospitalLogin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Map;

public class UserLogin extends AppCompatActivity {

    TextInputLayout etEmail;
    TextInputLayout etPassword;
    Button btnLogin;
    Button btnRegister;
    TextView tvAdminLogin;
    TextView tvHospitalLogin;
    String userName, userEmail, userPhone, userPassword, userId, userBloodType, token, userStatus;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Firebase firebase = new Firebase();
    FirebaseAuth mAuth;

    public static SharedPreferences mPreferences;
    private final String SHARED_PREF = "myPreferences";
    private final String KEY_USER_ID = "userID";
    private final String KEY_USER_NAME = "userName";
    private final String KEY_PASSWORD = "password";
    private final String KEY_USER_EMAIL = "userEmail";
    private final String KEY_USER_STATUS = "userStatus";
    private final String KEY_USER_BLOOD_TYPE = "userStatus";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        btnLogin =  findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tvAdminLogin = findViewById(R.id.tvAdminLogin);
        tvHospitalLogin = findViewById(R.id.tvHospitalLogin);

        mPreferences = getSharedPreferences("myPreferences", MODE_PRIVATE);

        if (mPreferences.contains(KEY_USER_EMAIL) && mPreferences.contains(KEY_PASSWORD) && mPreferences.contains(KEY_USER_BLOOD_TYPE)) {

            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if(!task.isSuccessful()){
                                Log.d("FCM", "Failed to retrieve Token!");
                                return;
                            }

                            token = task.getResult();
                            Log.d("FCM", "FCM Token : " + token);
                        }
                    });
            FirebaseMessaging.getInstance().subscribeToTopic("user");
            Intent i = new Intent(UserLogin.this, UserDashBoard.class);
            startActivity(i);
            finish();
        }

        mAuth = FirebaseAuth.getInstance();

        tvAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserLogin.this, AdminLogin.class);
                startActivity(i);
                finish();
            }
        });

        tvHospitalLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserLogin.this, HospitalLogin.class);
                startActivity(i);
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserLogin.this, UserRegister.class);
                startActivity(i);
                finish();
            }
        });

        btnLogin.setOnClickListener(view -> {
            loginUser();
        });
    }

    private void loginUser() {
        userEmail = etEmail.getEditText().getText().toString();
        userPassword = etPassword.getEditText().getText().toString();

        if (TextUtils.isEmpty(userEmail)) {
            etEmail.setError("Please fill in Email!");
            etEmail.requestFocus();
        } else if (TextUtils.isEmpty(userPassword)) {
            etPassword.setError("Please fill in Password!");
            etPassword.requestFocus();
        } else {
            mAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        firebase.getData("users",null, new com.example.OneBlood.MyCallback() {
                            @Override
                            public void returnData(ArrayList<Map<String, Object>> docList) {
                                Log.d("Documents Retrieved", docList.toString());
                                ArrayList<String> list = new ArrayList<>();

                                for (Map<String, Object> map : docList) {
                                    if (map.get("Email").toString().equals(userEmail)) {
                                        userName = map.get("FullName").toString();
                                        userId = map.get("id").toString();
                                        userBloodType = map.get("blood type").toString();
                                        userStatus = map.get("status").toString();

                                        if(userBloodType.equals("A+")){
                                            userBloodType = "A_Positive";
                                        }else if(userBloodType.equals("B+")) {
                                            userBloodType = "B_Positive";
                                        }else if(userBloodType .equals("AB+")) {
                                            userBloodType = "AB_Positive";
                                        }else if(userBloodType.equals("O+")) {
                                            userBloodType = "O_Positive";
                                        }else if(userBloodType.equals("A-")) {
                                            userBloodType = "A_Negative";
                                        }else if(userBloodType.equals("B-")) {
                                            userBloodType = "B_Negative";
                                        }else if(userBloodType.equals("AB-")) {
                                            userBloodType = "AB_Negative";
                                        }else if(userBloodType.equals("O-")) {
                                            userBloodType = "O_Negative";
                                        }

                                        FirebaseMessaging.getInstance().getToken()
                                                .addOnCompleteListener(new OnCompleteListener<String>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<String> task) {
                                                        if(!task.isSuccessful()){
                                                            Log.d("FCM", "Failed to retrieve Token!");
                                                            return;
                                                        }

                                                        token = task.getResult();
                                                        Log.d("FCM", "FCM Token : " + token);
                                                    }
                                                });

                                        FirebaseMessaging.getInstance().subscribeToTopic("user");
                                        Toast.makeText(UserLogin.this, userBloodType + token, Toast.LENGTH_SHORT).show();

                                        Toast.makeText(UserLogin.this, "Logged in Successfully!", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(UserLogin.this, UserDashBoard.class);
                                        startActivity(i);
                                        finish();

                                        SharedPreferences.Editor editor = mPreferences.edit();
                                        editor.putString(KEY_USER_EMAIL, etEmail.getEditText().getText().toString());
                                        editor.putString(KEY_PASSWORD, etPassword.getEditText().getText().toString());
                                        editor.putString(KEY_USER_ID, userId);
                                        editor.putString(KEY_USER_NAME, userName);
                                        editor.putString(KEY_USER_STATUS, userStatus);
                                        editor.putString(KEY_USER_BLOOD_TYPE, userBloodType);
                                        editor.apply();
                                        editor.commit();
                                    }
                                }
                            }
                        });
                    } else {
                        Toast.makeText(UserLogin.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
