package com.example.OneBlood.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.example.OneBlood.Adapters.ViewBloodRequestAdapter;
import com.example.OneBlood.Models.ViewBloodRequest;
import com.example.OneBlood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HospitalViewOwnBloodRequest extends AppCompatActivity {

    public static SharedPreferences hospitalPreferences;
    private final String SHARED_PREFERENCE = "hospitalPreferences";
    private final String KEY_HOSPITAL_ID = "hospitalID";
    private final String KEY_HOSPITAL_NAME = "hospitalName";
    private final String KEY_HOSPITAL_CONTACT = "hospitalContact";

    RecyclerView rv;
    List<ViewBloodRequest> mViewBloodRequests;
    ViewBloodRequestAdapter mViewBloodRequestAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ImageView backToRequestHospital;
    String postedBy , hospital;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_view_blood_request);
        rv = findViewById(R.id.rvHospitalRequest);
        backToRequestHospital = findViewById(R.id.backToRequestHospital);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        hospital = prefs.getString(KEY_HOSPITAL_NAME, "");

        loadRequestList();

        backToRequestHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void loadRequestList() {
        mViewBloodRequests = new ArrayList<>();

        ProgressDialog dialog = ProgressDialog.show(HospitalViewOwnBloodRequest.this, "",
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

                                    if(postedBy.equals(hospital)) {
                                        ViewBloodRequest viewBloodRequest = new ViewBloodRequest(document.getId(),
                                                document.get("title").toString(),
                                                document.get("blood type").toString(),
                                                document.get("location").toString(),
                                                document.get("description").toString(),
                                                document.get("postedBy").toString(),
                                                document.get("contact").toString(),
                                                document.get("date").toString());
                                        mViewBloodRequests.add(viewBloodRequest);
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
                if (mViewBloodRequests.size() == 0) {
                    alertDataEmpty();
                } else {
                    rv.setLayoutManager(new LinearLayoutManager(HospitalViewOwnBloodRequest.this));
                    mViewBloodRequestAdapter = new ViewBloodRequestAdapter(mViewBloodRequests, HospitalViewOwnBloodRequest.this, true);
                    rv.setAdapter(mViewBloodRequestAdapter);
                }
            }
        }, 1000);
    }

    public void adapterChange(int position){
        mViewBloodRequests.remove(position);
        mViewBloodRequestAdapter.notifyItemRemoved(position);
        if(mViewBloodRequests.size() == 0) {
            finish();
        }
    }

    private void alertDataEmpty() {
        new AlertDialog.Builder(this)
                .setMessage("No Existing Notice Made..")
                .setPositiveButton("Return", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent y = new Intent(HospitalViewOwnBloodRequest.this, HospitalBloodRequestMenu.class);
                        startActivity(y);
                        finish();
                    }
                }).create().show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HospitalViewOwnBloodRequest.this, HospitalBloodRequestMenu.class);
        startActivity(intent);
        finish();
    }
}