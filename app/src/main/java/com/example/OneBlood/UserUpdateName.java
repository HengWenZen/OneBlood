package com.example.OneBlood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class UserUpdateName extends AppCompatActivity {

    public static SharedPreferences mPreferences;
    private final String SHARED_PREF = "myPreferences";
    private final String KEY_USER_ID = "userID";
    private final String KEY_USER_NAME = "userName";
    private final String KEY_PASSWORD = "password";
    private final String KEY_USER_EMAIL = "userEmail";
    private final String KEY_USER_STATUS = "userStatus";
    private final String KEY_USER_BLOOD_TYPE = "userStatus";

    TextInputLayout etUpdateDOB, etUpdateContact, etUpdateName;
    Button btnSaveDetailsChanges, btnCancelUpdate;
    String user, userName;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details);

        etUpdateContact = findViewById(R.id.etUpdateContact);
        etUpdateDOB = findViewById(R.id.etUpdateDOB);
        etUpdateName = findViewById(R.id.etUpdateName);
        btnSaveDetailsChanges = findViewById(R.id.btnSaveDetailsChanges);

        etUpdateDOB.setVisibility(View.GONE);
        etUpdateContact.setVisibility(View.GONE);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        user = prefs.getString(KEY_USER_NAME, "");

        etUpdateName.getEditText().setText(user);
        etUpdateName.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));


        btnSaveDetailsChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = etUpdateName.getEditText().getText().toString();
                if(TextUtils.isEmpty(userName)){
                    Toast.makeText(UserUpdateName.this, "Please input new Name to make changes.", Toast.LENGTH_SHORT).show();

                }else if(userName.equals(user)){
                    Toast.makeText(UserUpdateName.this, "Please input another new Name to make changes.", Toast.LENGTH_SHORT).show();

                }else{
                    db.collection("latestAppointment")
                            .whereEqualTo("user", user)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    QuerySnapshot result = task.getResult();
                                    if(!result.isEmpty()){
                                        for(QueryDocumentSnapshot document :result) {
                                            Map<String, Object> userDetail = new HashMap<>();
                                            userDetail.put("user", userName);
                                            db.collection("latestAppointment").document(document.getId()).update(userDetail);
                                            Log.d("TAG", "onComplete: Data Updated Successfully!");
                                        }
                                    }
                                }
                            });

                    db.collection("completedAppointments")
                            .whereEqualTo("user", user)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    QuerySnapshot result = task.getResult();
                                    if(!result.isEmpty()){
                                        for(QueryDocumentSnapshot document :result) {
                                            Map<String, Object> userDetail = new HashMap<>();
                                            userDetail.put("user", userName);
                                            db.collection("completedAppointments").document(document.getId()).update(userDetail);
                                            Log.d("TAG", "onComplete: Data Updated Successfully!");
                                        }
                                    }
                                }
                            });

                    db.collection("userEventBooking")
                            .whereEqualTo("user", user)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    QuerySnapshot result = task.getResult();
                                    if(!result.isEmpty()){
                                        for(QueryDocumentSnapshot document :result) {
                                            Map<String, Object> userDetail = new HashMap<>();
                                            userDetail.put("user", userName);
                                            db.collection("userEventBooking").document(document.getId()).update(userDetail);
                                            Log.d("TAG", "onComplete: Data Updated Successfully!");
                                        }
                                    }
                                }
                            });

                    db.collection("emergencyRequest")
                            .whereEqualTo("user", user)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    QuerySnapshot result = task.getResult();
                                    if(!result.isEmpty()){
                                        for(QueryDocumentSnapshot document :result) {
                                            Map<String, Object> userDetail = new HashMap<>();
                                            userDetail.put("postedBy", userName);
                                            db.collection("emergencyRequest").document(document.getId()).update(userDetail);
                                            Log.d("TAG", "onComplete: Data Updated Successfully!");
                                        }
                                    }
                                }
                            });

                    db.collection("userBooking")
                            .whereEqualTo("user", user)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    QuerySnapshot result = task.getResult();
                                    if(!result.isEmpty()){
                                        for(QueryDocumentSnapshot document :result) {
                                            Map<String, Object> userDetail = new HashMap<>();
                                            userDetail.put("user", userName);
                                            db.collection("userBooking").document(document.getId()).update(userDetail);
                                            Log.d("TAG", "onComplete: Data Updated Successfully!");
                                        }
                                    }
                                }
                            });

                    db.collection("users")
                            .whereEqualTo("FullName", user)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    QuerySnapshot result = task.getResult();
                                    if(!result.isEmpty()){
                                        for(QueryDocumentSnapshot document :result){
                                            Map<String, Object> userDetail = new HashMap<>();
                                            userDetail.put("FullName", userName);
                                            db.collection("users").document(document.getId()).update(userDetail);
                                            Intent intent = new Intent(UserUpdateName.this, EditProfile.class);
                                            startActivity(intent);
                                            SharedPreferences.Editor editor = UserLogin.mPreferences.edit();
                                            editor.putString(KEY_USER_NAME, userName);
                                            editor.apply();
                                            finish();
                                            Toast.makeText(UserUpdateName.this, "Name Updated Successfully! ", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UserUpdateName.this, "Fail to Update Name" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}