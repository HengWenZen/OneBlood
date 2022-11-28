package com.example.OneBlood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class UserUpdateContact extends AppCompatActivity {

    public static SharedPreferences mPreferences;
    private final String SHARED_PREF = "myPreferences";
    private final String KEY_USER_ID = "userID";
    private final String KEY_USER_NAME = "userName";
    private final String KEY_PASSWORD = "password";
    private final String KEY_USER_EMAIL = "userEmail";
    private final String KEY_USER_STATUS = "userStatus";
    private final String KEY_USER_BLOOD_TYPE = "userStatus";
    private final String KEY_USER_CONTACT = "userContact";

    TextInputLayout etUpdateDOB, etUpdateContact, etUpdateName;
    Button btnSaveDetailsChanges, btnCancelUpdate;
    String userPhone, userName, user, phone;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details);

        etUpdateContact = findViewById(R.id.etUpdateContact);
        etUpdateDOB = findViewById(R.id.etUpdateDOB);
        etUpdateName = findViewById(R.id.etUpdateName);
        btnSaveDetailsChanges = findViewById(R.id.btnSaveDetailsChanges);
        btnCancelUpdate = findViewById(R.id.btnCancelUpdate);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        user = prefs.getString(KEY_USER_NAME, "");
        userPhone = prefs.getString(KEY_USER_CONTACT, "");

        etUpdateDOB.setVisibility(View.GONE);
        etUpdateName.setVisibility(View.GONE);

        etUpdateContact.getEditText().setText(userPhone);
        etUpdateContact.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));

        btnSaveDetailsChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = etUpdateContact.getEditText().getText().toString();
                String phoneFormat = "^(\\+?6?01)[02-46-9]-*[0-9]{7}$|^(\\+?6?01)[1]-*[0-9]{8}$";

                if(TextUtils.isEmpty(phone)){

                    Toast.makeText(UserUpdateContact.this, "Please input Contact.", Toast.LENGTH_SHORT).show();

                }else if(phone.equals(userPhone)){

                    Toast.makeText(UserUpdateContact.this, "Please input another Contact to make changes.", Toast.LENGTH_SHORT).show();

                }else if(!phone.matches(phoneFormat)){

                    Toast.makeText(UserUpdateContact.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();

                }else{
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
                                            userDetail.put("phone number", phone);
                                            Log.d("TAG", "onComplete: " + document.getId());
                                            db.collection("users").document(document.getId()).update(userDetail);
                                            Intent intent = new Intent(UserUpdateContact.this, EditProfile.class);
                                            startActivity(intent);
                                            SharedPreferences.Editor editor = UserLogin.mPreferences.edit();
                                            editor.putString(KEY_USER_CONTACT, phone);
                                            editor.apply();
                                            finish();
                                            Toast.makeText(UserUpdateContact.this, "Contact Updated Successfully! ", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UserUpdateContact.this, "Fail to Update Contact" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        btnCancelUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UserUpdateContact.this, EditProfile.class);
        startActivity(intent);
        finish();
    }
}
