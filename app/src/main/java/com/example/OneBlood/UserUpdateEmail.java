package com.example.OneBlood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

import java.util.HashMap;
import java.util.Map;

public class UserUpdateEmail extends AppCompatActivity {

    TextInputLayout etNewPassword, etAuthenticatePassword, etAuthenticateEmail, etConfirmPassword, etNewEmail;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String user, email, password, newPassword, confirmPassword, userEmail, userName, authenticateEmail;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView tvChangePassword, ChangePassword;
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
        tvChangePassword = findViewById(R.id.tvChangePassword);
        ChangePassword = findViewById(R.id.ChangePassword);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);

        tvChangePassword.setText("Change Your Email");
        ChangePassword.setText("You can change your email after authenticating your profile by entering your current password");

        SharedPreferences prefs = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        userName = prefs.getString(KEY_USER_NAME, "");
        authenticateEmail = prefs.getString(KEY_USER_EMAIL, "");

        etAuthenticateEmail.getEditText().setText(authenticateEmail);
        etAuthenticateEmail.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));

        etNewPassword.setVisibility(View.GONE);
        etConfirmPassword.setVisibility(View.GONE);

        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etAuthenticateEmail.getEditText().getText().toString();
                password = etAuthenticatePassword.getEditText().getText().toString();
                userEmail = etNewEmail.getEditText().getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if(TextUtils.isEmpty(password)){

                    Toast.makeText(UserUpdateEmail.this, "Please input current password!", Toast.LENGTH_SHORT).show();
                    Log.d("TAG", "onCreate: " + email);

                } else if(TextUtils.isEmpty(userEmail)){
                    Toast.makeText(UserUpdateEmail.this, "Please Input New Email!", Toast.LENGTH_SHORT).show();

                } else if(!emailValidator(etNewEmail)){

                    Toast.makeText(UserUpdateEmail.this, "Please Input a Valid Email!", Toast.LENGTH_SHORT).show();

                }else{
                    FirebaseUser user = mAuth.getCurrentUser();
                    AuthCredential credential = EmailAuthProvider.getCredential(email, password);
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                FirebaseUser user = mAuth.getCurrentUser();
                                user.updateEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                                                                        userDetail.put("Email", userEmail);
                                                                        db.collection("users").document(queryDocumentSnapshots.getId()).update(userDetail);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    });

                                            SharedPreferences.Editor editor = UserLogin.mPreferences.edit();
                                            editor.putString(KEY_USER_EMAIL, userEmail);
                                            editor.apply();

                                            Intent intent = new Intent(UserUpdateEmail.this, EditProfile.class);
                                            startActivity(intent);
                                            finish();
                                            Toast.makeText(UserUpdateEmail.this, "Email " +
                                                    " updated successfully!", Toast.LENGTH_LONG).show();
                                        }else{
                                            Toast.makeText(UserUpdateEmail.this, "Failed to update password!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UserUpdateEmail.this, "Failed to authenticate user " + e.getMessage(), Toast.LENGTH_LONG).show();
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

    private boolean emailValidator(TextInputLayout etNewEmail) {

        // extract the entered data from the EditText
        String emailToText = etNewEmail.getEditText().getText().toString();

        //compared with new email with the EMAIL_ADDRESS to check if the email format is correct
        if (!emailToText.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailToText).matches()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UserUpdateEmail.this, EditProfile.class);
        startActivity(intent);
        finish();
    }
}

