package com.example.OneBlood.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.OneBlood.Adapters.DonorListAdapter;
import com.example.OneBlood.Labs.DonorLab;
import com.example.OneBlood.Models.Donor;
import com.example.OneBlood.R;
import com.example.OneBlood.UserAvailableDonorMainMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HospitalDonorList extends AppCompatActivity {

    RecyclerView rvDonorList;
    Button btnFilter;
    DonorListAdapter mDonorListAdapter;
    Spinner spinnerBloodType;
    ImageView backToHospitalHome;
    String selectedBloodType, user, userStatus;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<Donor> mDonors = new ArrayList<>();
    ArrayList<Donor> allDonorList = new ArrayList<>();
    List<Donor> donors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_donor_list);

        rvDonorList = findViewById(R.id.rvHospitalDonorList);
        spinnerBloodType = findViewById(R.id.spinnerHospitalFilterBloodType);
        btnFilter = findViewById(R.id.btnHospitalFilter);
        backToHospitalHome = findViewById(R.id.backToHospitalHome);

        loadBloodTypeList();
        loadDonorList();

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                donors.clear();
                //get selected item
                selectedBloodType = spinnerBloodType.getSelectedItem().toString();
                Log.d("TAG", "onClick: " + selectedBloodType);

                if(selectedBloodType.equals("All")){
                    donors.addAll(allDonorList);
                }else{
                    //filter the users based on the selected blood type
                    for(Donor donor : allDonorList){
                        if(donor.getBloodType().equals(selectedBloodType) && donor.getUserStatus().equals("active")){
                            donors.add(donor);
                        }
                    }
                }
                mDonorListAdapter.notifyDataSetChanged();
                //toast message if there is no user with the selected blood type
                if (donors.size() == 0){
                    Toast.makeText(HospitalDonorList.this, "No User found" , Toast.LENGTH_SHORT).show();
                }
            }
        });

        backToHospitalHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void loadBloodTypeList(){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(HospitalDonorList.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.hospitalFilterBloodType));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBloodType.setAdapter(arrayAdapter);
    }

    private void loadDonorList(){
        donors = new ArrayList<>();

        ProgressDialog dialog = ProgressDialog.show(HospitalDonorList.this, "",
                "Loading. Please wait...", true);   //show loading dialog

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot result = task.getResult();
                            if (!result.isEmpty()) {
                                for (QueryDocumentSnapshot document : result) {
                                    String userName = document.get("FullName").toString();
                                    String status = document.get("status").toString();

                                    //get user details
                                    if(status.equals("active")) {
                                        Donor donor = new Donor(document.getId(),
                                                document.get("FullName").toString(),
                                                document.get("phone number").toString(),
                                                document.get("Email").toString(),
                                                document.get("blood type").toString(),
                                                document.get("status").toString());

                                        String bloodType = document.get("FullName").toString();
                                        Log.d("TAG", "onComplete: " + bloodType);
                                        donors.add(donor);
                                    }
                                }

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        dialog.dismiss();   //remove loading Dialog
                                        allDonorList.addAll(donors);
                                        rvDonorList.setLayoutManager(new LinearLayoutManager(HospitalDonorList.this));
                                        mDonorListAdapter = new DonorListAdapter(donors, HospitalDonorList.this);
                                        rvDonorList.setAdapter(mDonorListAdapter);
                                    }
                                }, 1000);
                            }
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(HospitalDonorList.this, HospitalMenu.class);
        startActivity(i);
        finish();
    }
}