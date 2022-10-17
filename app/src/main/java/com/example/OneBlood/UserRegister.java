package com.example.OneBlood;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class UserRegister extends AppCompatActivity {

    public static final String EXTRA_USER_NAME = "userName";

    Button btnRegisterAcc;
    Button btnExistingUser;
    TextInputLayout etFullName, etUserPhone, etUserEmail, etUserPassword,etUserRace;
    String userName, userEmail, userPhone, userPassword,userRace, token;
    ImageView ivBack;
    boolean loginSuccess = false;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        btnRegisterAcc = (Button) findViewById(R.id.btnRegisterAcc);
        ivBack = findViewById(R.id.ivBack);
        etFullName = findViewById(R.id.etName);
        etUserEmail =  findViewById(R.id.etUserEmail);
        etUserPhone =  findViewById(R.id.etUserPhone);
        etUserPassword =findViewById(R.id.etUserPassword);
        etUserRace = findViewById(R.id.etUserRace);
        btnExistingUser = (Button) findViewById(R.id.btnExistingUser);

        mAuth = FirebaseAuth.getInstance();

        btnRegisterAcc.setOnClickListener(view -> {
            RegisterUser();
        });

        btnExistingUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserRegister.this, UserLogin.class);
                startActivity(i);
                finish();
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserRegister.this, OnStart.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void RegisterUser() {
        userName = etFullName.getEditText().getText().toString().trim();
        userEmail = etUserEmail.getEditText().getText().toString().trim();
        userPassword = etUserPassword.getEditText().getText().toString().trim();
        userRace = etUserRace.getEditText().getText().toString().trim();


        if (TextUtils.isEmpty(userName)) {
            etFullName.setError("Please fill in your name!");
            etFullName.requestFocus();
        } else if (TextUtils.isEmpty(userEmail)) {
            etUserEmail.setError("Please fill in Email!");
            etUserEmail.requestFocus();
        }else if (TextUtils.isEmpty(userPassword)) {
            etUserPassword.setError("Please fill in Password!");
            etUserPassword.requestFocus();
        }else if(TextUtils.isEmpty(userRace)){
            etUserRace.setError("Please fill in Race!");
            etUserRace.requestFocus();
        } else {
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

            mAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Map<String, Object> user = new HashMap<>();
                        user.put("FullName", userName);
                        user.put("Email", userEmail);
                        user.put("Password", userPassword);
                        user.put("Race", userRace);
                        user.put("Token", token);

                        db.collection("users")
                                .add(user)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Intent i = new Intent(UserRegister.this, UserRegister2.class);
                                        i.putExtra(EXTRA_USER_NAME, userName);
                                        startActivity(i);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UserRegister.this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(UserRegister.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}




