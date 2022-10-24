package com.example.OneBlood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.OneBlood.Activity.HospitalViewOwnBloodRequest;
import com.example.OneBlood.Adapters.BloodRequestAdapters;
import com.example.OneBlood.Models.EmergencyNotice;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserBloodRequestMainMenu extends AppCompatActivity {

    public static SharedPreferences mPreferences;
    private final String SHARED_PREF = "myPreferences";
    private final String KEY_USER = "user";
    private final String KEY_USER_NAME = "userName";

    Button btnViewYourRequest, btnNewRequest;
    RecyclerView rv;
    BloodRequestAdapters mBloodRequestAdapters;
    List<EmergencyNotice> mEmergencyNotices;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String postedBy ,user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_blood_request_main_menu);

        btnNewRequest = findViewById(R.id.btnNewRequest);
        btnViewYourRequest = findViewById(R.id.btnViewYourRequest);
        rv = findViewById(R.id.rvBloodRequestList);

        SharedPreferences prefs = getSharedPreferences("myPreferences", MODE_PRIVATE);
        user = prefs.getString(KEY_USER_NAME, null);

        loadRequestList();

        btnNewRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserBloodRequestMainMenu.this, UserNewBloodRequest.class);
                startActivity(i);
            }
        });

        btnViewYourRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserBloodRequestMainMenu.this, HospitalViewOwnBloodRequest.class);
                startActivity(i);
            }
        });
    }

    private void loadRequestList() {
        mEmergencyNotices = new ArrayList<>();

        ProgressDialog dialog = ProgressDialog.show(UserBloodRequestMainMenu.this, "",
                "Loading. Please wait...", true);   //show loading dialog


        db.collection("emergencyRequest")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot result = task.getResult();
                            if (!result.isEmpty()) {
                                for (QueryDocumentSnapshot document : result) {
                                    postedBy = document.get("postedBy").toString();

                                    if(!postedBy.equals(user)) {
                                        EmergencyNotice emergencyNotice = new EmergencyNotice(
                                                document.get("description").toString(),
                                                document.get("title").toString(),
                                                document.get("postedBy").toString(),
                                                document.get("date").toString(),
                                                document.getId(),
                                                document.get("blood type").toString(),
                                                document.get("contact").toString(),
                                                document.get("location").toString());
                                        mEmergencyNotices.add(emergencyNotice);

                                    }
                                }
                            }
                        }
                    }
                });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();   //remove loading Dialog
                if (mEmergencyNotices.size() == 0) {
                    Toast.makeText(UserBloodRequestMainMenu.this, "No Requests Found.", Toast.LENGTH_SHORT).show();
                }
                rv.setLayoutManager(new LinearLayoutManager(UserBloodRequestMainMenu.this));
                mBloodRequestAdapters = new BloodRequestAdapters(mEmergencyNotices, UserBloodRequestMainMenu.this, true, postedBy);
                rv.setAdapter(mBloodRequestAdapters);
            }
        }, 1000);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UserBloodRequestMainMenu.this, UserDashBoard.class);
        startActivity(intent);
        finish();
    }
}