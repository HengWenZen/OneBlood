package com.example.OneBlood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.api.LogDescriptor;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserRegister3 extends AppCompatActivity {
    public static final String EXTRA_USER_NAME = "userName";

    Button btnRegisterUser3;
    TextInputLayout etUserPhone, etBloodType;
    AutoCompleteTextView actvBloodType;
    ImageView ivBack;
    String userPhoneNo, getUserPhoneNo, getSelectedBloodType, userName;
    Firebase db = new Firebase();
    FirebaseFirestore mFirebase = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register3);

        btnRegisterUser3 = findViewById(R.id.btnRegisterAcc3);
        etUserPhone = findViewById(R.id.etUserPhone);
        etBloodType = findViewById(R.id.spBloodType);
        actvBloodType = findViewById(R.id.actvBloodType);
        ivBack = findViewById(R.id.ivBack3);
        final String[] bloodType = getResources().getStringArray(R.array.bloodType);

        userName = getIntent().getStringExtra(EXTRA_USER_NAME);
        Log.d("TAG", "onCreate: " + userName);

        ArrayAdapter<String> hospitalAdapter = new ArrayAdapter<>(
                UserRegister3.this,
                R.layout.drop_down_item,
                bloodType);
        actvBloodType.setAdapter(hospitalAdapter);

        ((AutoCompleteTextView) etBloodType.getEditText()).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get selected item
                hospitalAdapter.getItem(position);
                getSelectedBloodType =  ((AutoCompleteTextView) etBloodType.getEditText()).getText().toString();
            }
        });

        btnRegisterUser3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterUser();
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UserRegister3.this)
                        .setMessage("You will need to input the credentials again if you wish to register new account. \n Cancel Registration?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                mFirebase.collection("users")
                                        .whereEqualTo("FullName", userName)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                QuerySnapshot queryDocumentSnapshots = task.getResult();
                                                if (!queryDocumentSnapshots.isEmpty()) {
                                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                                        Log.d("Document ID:", document.getId() + " => " + document.getData());
                                                        Map<String, Object> users = new HashMap<>();
                                                        mFirebase.collection("users").document(document.getId()).delete();

                                                    }
                                                }
                                            }
                                        });

                                Intent intent = new Intent(UserRegister3.this, UserRegister.class);
                                startActivity(intent);
                                finish();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create().show();
            }
        });
    }

    private void RegisterUser() {
        //validate phone number and blood type
        if (!validatePhoneNumber() || !validateBloodType()) {
            return;

        } else {
            Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(UserRegister3.this, UserLogin.class);
            startActivity(i);
            finish();
        }
    }

    private boolean validatePhoneNumber() {
        userPhoneNo = etUserPhone.getEditText().getText().toString().trim();
        getUserPhoneNo = "+6" + userPhoneNo;
        String phoneFormat = "(01)[0-46-9]*[0-9]{7,8}$";

        if (TextUtils.isEmpty(userPhoneNo)) {
            //Check if phone number is empty
            Toast.makeText(this, "Please fill in Phone number", Toast.LENGTH_SHORT).show();
            return false;

        } else if (!userPhoneNo.matches(phoneFormat)) {
            //Validate phone number
            Toast.makeText(this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
            return false;

        } else {
            //insert Data into Database
            db.getData("users", null, new MyCallback() {
                @Override
                public void returnData(ArrayList<Map<String, Object>> docList) {
                    Log.d("firebase example", docList.toString());
                    ArrayList<String> list = new ArrayList<>();

                    for (Map<String, Object> map : docList) {
                        if(map.get("FullName").toString().equals(userName)) {
                            Map<String, Object> user = new HashMap<>();
                            user.put("phone number", getUserPhoneNo);
                            db.updData("users", user, map.get("id").toString());
                        }
                    }
                }
            });
            return true;
        }
    }

    private boolean validateBloodType() {
        //Check if the user has selected any item
        if (getSelectedBloodType == null){
            Toast.makeText(this, "Please select Blood Type", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            //insert Data into database

            if (getSelectedBloodType.equals("Rh null") || getSelectedBloodType.equals("O-") || getSelectedBloodType.equals("AB-")) {
                Map<String, Object> user = new HashMap<>();
                user.put("blood type", getSelectedBloodType);
                user.put("name", userName);
                user.put("contact", userPhoneNo);

                mFirebase.collection("rareBloodUser")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("TAG", "DocumentSnapshot written with ID: " + documentReference.getId());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "Fail to add Document" + e.getMessage());
                    }
                });

                mFirebase.collection("users")
                        .whereEqualTo("FullName", userName)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                QuerySnapshot queryDocumentSnapshots = task.getResult();
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                        Log.d("Document ID:", "Document Updated Successfully" + document.getData());
                                        Map<String, Object> data = new HashMap<>();
                                        data.put("status", "active");
                                        data.put("blood type", getSelectedBloodType);

                                        mFirebase.collection("users").document(document.getId()).update(data);

                                    }
                                }
                            }
                        });
            }else{
                mFirebase.collection("users")
                        .whereEqualTo("FullName", userName)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                QuerySnapshot queryDocumentSnapshots = task.getResult();
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                        Log.d("Document ID:", document.getId() + " => " + document.getData());
                                        Map<String, Object> users = new HashMap<>();
                                        users.put("status", "active");
                                        users.put("blood type", getSelectedBloodType);
                                        mFirebase.collection("users").document(document.getId()).update(users);

                                    }
                                }
                            }
                        });
            }
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(UserRegister3.this)
                .setMessage("You will need to input the credentials again if you wish to register new account. \n Cancel Registration?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        mFirebase.collection("users")
                                .whereEqualTo("FullName", userName)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        QuerySnapshot queryDocumentSnapshots = task.getResult();
                                        if (!queryDocumentSnapshots.isEmpty()) {
                                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                                Log.d("Document ID:", document.getId() + " => " + document.getData());
                                                Map<String, Object> users = new HashMap<>();
                                                mFirebase.collection("users").document(document.getId()).delete();

                                            }
                                        }
                                    }
                                });

                        Intent intent = new Intent(UserRegister3.this, UserRegister.class);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).create().show();
    }
}