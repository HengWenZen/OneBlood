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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

public class UserUpdatePassword extends AppCompatActivity {

    TextInputLayout etNewPassword, etAuthenticatePassword, etAuthenticateEmail, etConfirmPassword, etNewEmail;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String user, email, password, newPassword, confirmPassword, userName, userEmail;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressBar mProgressBar;
    Button btnSaveChanges, btnCancelEdit;

    public static SharedPreferences mPreferences;
    private final String SHARED_PREF = "myPreferences";
    private final String KEY_USER_ID = "userID";
    private final String KEY_USER_NAME = "userName";
    private final String KEY_PASSWORD = "password";
    private final String KEY_USER_EMAIL = "userEmail";
    private final String KEY_USER_STATUS = "userStatus";
    private final String KEY_USER_BLOOD_TYPE = "userStatus";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_update_password);

        etAuthenticateEmail = findViewById(R.id.etAuthenticateUserEmail);
        etAuthenticatePassword = findViewById(R.id.etAuthenticateUserPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etNewEmail = findViewById(R.id.etNewEmail);
        btnCancelEdit = findViewById(R.id.btnCancelEdit);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        userName = prefs.getString(KEY_USER_NAME, "");
        userEmail = prefs.getString(KEY_USER_EMAIL, "");

        etAuthenticateEmail.getEditText().setText(userEmail);
        etAuthenticateEmail.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));


        Log.d("TAG", "onCreate: " + email + password);

        etNewEmail.setVisibility(View.GONE);

        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etAuthenticateEmail.getEditText().getText().toString();
                password = etAuthenticatePassword.getEditText().getText().toString();
                newPassword = etNewPassword.getEditText().getText().toString().trim();
                confirmPassword = etConfirmPassword.getEditText().getText().toString().trim();

                if(TextUtils.isEmpty(password)){

                    Toast.makeText(UserUpdatePassword.this, "Please input current password!", Toast.LENGTH_SHORT).show();
                    Log.d("TAG", "onCreate: " + email);

                }else if((TextUtils.isEmpty(newPassword) || newPassword.length()<6)){
                    Toast.makeText(UserUpdatePassword.this, "Password must be more than 6 characters!", Toast.LENGTH_SHORT).show();

                }else if(!newPassword.equals(confirmPassword)) {

                    Toast.makeText(UserUpdatePassword.this, "Please input the new password correctly", Toast.LENGTH_SHORT).show();
                    etConfirmPassword.requestFocus();

                }else{
                    FirebaseUser user = mAuth.getCurrentUser();
                    AuthCredential credential = EmailAuthProvider.getCredential(email, password);
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                FirebaseUser user = mAuth.getCurrentUser();
                                user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){

                                            db.collection("users")
                                                    .whereEqualTo("FullName", userName)
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            QuerySnapshot result = task.getResult();
                                                            if(!result.isEmpty()){
                                                                for(QueryDocumentSnapshot queryDocumentSnapshots : result){
                                                                    String name = queryDocumentSnapshots.get("FullName").toString();
                                                                    if(userName.equals(name)){
                                                                        Map<String, Object> userDetail = new HashMap<>();
                                                                        userDetail.put("Password", newPassword);
                                                                        db.collection("users").document(queryDocumentSnapshots.getId()).update(userDetail);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    });
                                            SharedPreferences.Editor editor = UserLogin.mPreferences.edit();
                                            editor.putString(KEY_PASSWORD, newPassword);
                                            editor.apply();

                                            Intent intent = new Intent(UserUpdatePassword.this, EditProfile.class);
                                            startActivity(intent);
                                            finish();
                                            Toast.makeText(UserUpdatePassword.this, "Password " +
                                                    " updated successfully!", Toast.LENGTH_LONG).show();
                                        }else{
                                            Toast.makeText(UserUpdatePassword.this, "Failed to update password!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UserUpdatePassword.this, "Failed to authenticate user " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        btnCancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UserUpdatePassword.this, EditProfile.class);
        startActivity(intent);
        finish();
    }
}
