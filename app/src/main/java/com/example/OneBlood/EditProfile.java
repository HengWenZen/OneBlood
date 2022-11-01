package com.example.OneBlood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.OneBlood.Activity.HospitalLogin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.core.Tag;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class EditProfile extends AppCompatActivity {

    public static SharedPreferences mPreferences;
    private final String SHARED_PREF = "myPreferences";
    private final String KEY_USER = "user";
    private final String KEY_USER_NAME = "userName";
    private final String KEY_PASSWORD = "password";
    private final String KEY_USER_EMAIL = "userEmail";
    private final String KEY_USER_BLOOD_TYPE = "userBloodType";
    private final String KEY_USER_CONTACT = "userContact";

    FirebaseUser userData = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Firebase mFirebase = new Firebase();
    TextView tvName,tvEmail, tvLivesSaved, tvUserBloodType;
    int counter = 0, livesSaved = 0;
    TextInputLayout etFullName, etEmail , etPhone , etPassword, etStatus, etGender, etDOB;
    Button btnChangePassword;
    String FullName, NewEmail, NewPhone, NewPassword;
    String user, email, userPhone, status, dateOfBirth, gender, userBloodType;
    String existingName, existingPassword, existingEmail, existingPhone;
    String livesCounter, userName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etFullName = findViewById(R.id.etEditFullName);
        etEmail = findViewById(R.id.etEditEmail);
        etPhone = findViewById(R.id.etEditPhone);
        etStatus = findViewById(R.id.etStatus);
        tvEmail = findViewById(R.id.tvUserEmail);
        tvUserBloodType = findViewById(R.id.tvUserBloodType);
        tvName = findViewById(R.id.tvUserName);
        etGender = findViewById(R.id.etGender);
        tvLivesSaved = findViewById(R.id.tvLivesSaved);
        etDOB = findViewById(R.id.etDOB);
        btnChangePassword = findViewById(R.id.btnChangePassword);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        user = prefs.getString(KEY_USER_NAME, "");
        email = prefs.getString(KEY_USER_EMAIL, "");
        userPhone = prefs.getString(KEY_USER_CONTACT, "");
        userBloodType = prefs.getString(KEY_USER_BLOOD_TYPE,"");
        tvUserBloodType.setText(userBloodType);

        retrieveData();
        db.collection("users")
                .whereEqualTo("FullName", user)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot result = task.getResult();
                            if (!result.isEmpty()) {
                                Log.d("Data Retrieved", result.toString());
                                for (QueryDocumentSnapshot document : result) {
                                    Log.d("Document ID:", document.getId() + " => " + document.getData());
                                    if (document.get("FullName").toString().equals(user)) {
                                        status = document.get("status").toString();
                                        dateOfBirth = document.get("Date of Birth").toString();
                                        gender = document.get("Gender").toString();
                                        etStatus.getEditText().setText(status);
                                        etStatus.getEditText().setTextColor(ContextCompat.getColor(EditProfile.this, R.color.black));
                                        etGender.getEditText().setText(gender);
                                        etGender.getEditText().setTextColor(ContextCompat.getColor(EditProfile.this, R.color.black));
                                        etDOB.getEditText().setText(dateOfBirth);
                                        etDOB.getEditText().setTextColor(ContextCompat.getColor(EditProfile.this, R.color.black));

                                        existingName = (document.get("FullName").toString());
                                        existingPassword = (document.get("Password").toString());
                                        existingEmail = (document.get("Email").toString());
                                        existingPhone = (document.get("phone number").toString());
                                    }
                                }
                            }
                        }
                    }
                });

        tvName.setText(user);
        tvEmail.setText(email);
        etEmail.getEditText().setText(email);
        etEmail.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etFullName.getEditText().setText(user);
        etFullName.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etPhone.getEditText().setText(userPhone);
        etPhone.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));


        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditProfile.this, UserUpdatePassword.class);
                startActivity(i);
                finish();
            }
        });

        etEmail.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditProfile.this, UserUpdateEmail.class);
                startActivity(i);
                finish();
            }
        });

        etFullName.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditProfile.this, UserUpdateName.class);
                startActivity(i);
                finish();
            }
        });

        etPhone.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditProfile.this, UserUpdateContact.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void update(View view){
        FullName = etFullName.getEditText().getText().toString();
        NewEmail = etEmail.getEditText().getText().toString();
        NewPhone = etPhone.getEditText().getText().toString();

        if (isNameChanged()) {
            Toast.makeText(this, "Info Updated Successfully", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(this, "No Info Changed !", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNameChanged(){
        SharedPreferences prefs = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        String user = prefs.getString(KEY_USER_NAME,"");

        if(!FullName.equals(existingName)) {
            mFirebase.getData("users", null, new MyCallback() {
                @Override
                public void returnData(ArrayList<Map<String, Object>> docList) {
                    Log.d("firebase example", docList.toString());
                    ArrayList<String> list = new ArrayList<>();

                    for (Map<String, Object> map : docList) {
                        if (map.get("FullName").toString().equals(user)) {
                            Map<String, Object> user = new HashMap<>();
                            user.put("FullName", FullName);
                            mFirebase.updData("users", user, map.get("id").toString());
                            SharedPreferences.Editor editor = UserLogin.mPreferences.edit();
                            editor.putString(KEY_USER_NAME, FullName);
                            editor.apply();
                        }
                    }
                }
            });

            return true;
        }
        return false;
    }

    private void retrieveData() {
        db.collection("completedAppointments")
                .whereEqualTo("user", user)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot result = task.getResult();
                        if (!result.isEmpty()) {
                            for (QueryDocumentSnapshot document : result) {
                                userName = document.get("user").toString();
                                Log.d("TAG", "onComplete: " + userName + user);
                                if (user.equals(userName)) {
                                    counter++;
                                }
                            }
                            livesSaved = counter * 3;
                            livesCounter = Integer.toString(livesSaved);
                            Log.d("TAG", "onComplete: " + livesCounter);
                            tvLivesSaved.setText(livesCounter);
                        }else if(result.isEmpty()){
                            tvLivesSaved.setText("0");
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(EditProfile.this, UserDashBoard.class);
        startActivity(i);
        finish();
    }
}

