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

    FirebaseUser userData = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Firebase mFirebase = new Firebase();
    TextView tvName,tvEmail;
    TextInputLayout etFullName, etEmail , etPhone , etPassword, etRace;
    Button btnUpdate;
    String FullName, NewEmail, NewPhone, NewPassword;
    String ExistingName, ExistingPassword, ExistingEmail,ExistingPhone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEditEmail);
        etPhone = findViewById(R.id.etEditPhone);
        etPassword = findViewById(R.id.etEditPassword);
        etRace = findViewById(R.id.etEditRace);
        btnUpdate = findViewById(R.id.btnUpdate);
        tvName = findViewById(R.id.tvName);
        SharedPreferences prefs = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        String user = prefs.getString(KEY_USER_NAME,"");
        String email = prefs.getString(KEY_USER_EMAIL,"");

        if(user != null) {
            tvName.setText(user);
        }

        mFirebase.getData("users", null, new MyCallback() {
            @Override
            public void returnData(ArrayList<Map<String, Object>> docList) {
                Log.d("firebase example", docList.toString());
                ArrayList<String> list = new ArrayList<>();
                for (Map<String, Object> map : docList) {
                    if(map.get("FullName").toString().equals(user)) {
                        Map<String, Object> userDetail = new HashMap<>();
                        etFullName.getEditText().setText(user);
                        etEmail.getEditText().setText(email);
                        etPhone.getEditText().setText(map.get("phone number").toString());
                        etPassword.getEditText().setText(map.get("Password").toString());

                        ExistingName = (map.get("FullName").toString());
                        ExistingPassword = (map.get("Password").toString());
                        ExistingEmail = (map.get("Email").toString());
                        ExistingPhone = (map.get("phone number").toString());

                        mFirebase.updData("users", userDetail, map.get("id").toString());
                    }
                }
            }
        });
    }

    public void update(View view){
        FullName = etFullName.getEditText().getText().toString();
        NewEmail = etEmail.getEditText().getText().toString();
        NewPassword = etPassword.getEditText().getText().toString();
        NewPhone = etPhone.getEditText().getText().toString();

        if (isNameChanged() || isPasswordChanged()) {
            Toast.makeText(this, "Info Updated Successfully", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(this, "No Info Changed !", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isPasswordChanged() {
        SharedPreferences prefs = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        String user = prefs.getString(KEY_USER_NAME,"");
        String email = prefs.getString(KEY_USER_EMAIL,"");

        if(!ExistingPassword.equals(etPassword.getEditText().getText().toString())) {
            mFirebase.getData("users", null, new MyCallback() {
                @Override
                public void returnData(ArrayList<Map<String, Object>> docList) {
                    Log.d("firebase example", docList.toString());
                    ArrayList<String> list = new ArrayList<>();

                    for (Map<String, Object> map : docList) {
                        if(map.get("FullName").toString().equals(user)) {
                            Map<String, Object> user = new HashMap<>();
                            user.put("Password", NewPassword);
                            mFirebase.updData("users", user, map.get("id").toString());

                            userData.updatePassword(NewPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("TAG", "User password updated." + NewPassword);
                                        Toast.makeText(EditProfile.this, "Password Updated Successfully!", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(EditProfile.this, "Update Fail", Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, "OnComplete :" + task.getException().getMessage().toString() );
                                    }
                                }
                            });

                            SharedPreferences.Editor editor = UserLogin.mPreferences.edit();
                            editor.putString(KEY_PASSWORD, NewPassword);
                            editor.apply();
                        }
                    }
                }
            });

            return true;
        }
        return false;
    }

    private boolean isNameChanged(){
        SharedPreferences prefs = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        String user = prefs.getString(KEY_USER_NAME,"");

        if(!FullName.equals(ExistingName)) {
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
}

