package com.example.OneBlood;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.OneBlood.Labs.DonorLab;
import com.example.OneBlood.Adapters.DonorListAdapter;
import com.example.OneBlood.Models.Donor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserAvailableDonorMainMenu extends AppCompatActivity {

    public static SharedPreferences mPreferences;
    private final String SHARED_PREF = "myPreferences";
    private final String KEY_USER = "user";
    private final String KEY_USER_NAME = "userName";
    private final String KEY_USER_STATUS = "userStatus";

    RecyclerView rvDonorList;
    Button btnFilter;
    DonorListAdapter mDonorListAdapter;
    Spinner spinnerBloodType;
    String selectedBloodType, user, userStatus;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<Donor> mDonors = new ArrayList<>();
    ArrayList<Donor> allDonorList = new ArrayList<>();
    DonorLab donorLab = DonorLab.get(this);
    List<Donor> donors;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_available_donor_main_menu);

        rvDonorList = findViewById(R.id.rvDonorList);
        spinnerBloodType = findViewById(R.id.spinnerBloodType);
        btnFilter = findViewById(R.id.btnFilter);

        SharedPreferences prefs = getSharedPreferences("myPreferences", MODE_PRIVATE);
        user = prefs.getString(KEY_USER_NAME, null);

        loadBloodTypeList();
        loadDonorList();

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donors.clear();
                selectedBloodType = spinnerBloodType.getSelectedItem().toString();
                Log.d("TAG", "onClick: " + selectedBloodType);
                if(selectedBloodType.equals("All")){
                    donors.addAll(allDonorList);
                }else{
                    for(Donor donor : allDonorList){
                        if(donor.getBloodType().equals(selectedBloodType) && donor.getUserStatus().equals("active")){
                            donors.add(donor);
                        }
                    }
                }
                mDonorListAdapter.notifyDataSetChanged();
                if (donors.size() == 0){
                    Toast.makeText(UserAvailableDonorMainMenu.this, "No User found" , Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void loadBloodTypeList(){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(UserAvailableDonorMainMenu.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.filterBloodType));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBloodType.setAdapter(arrayAdapter);
    }

    private void loadDonorList(){
        donors = new ArrayList<>();

        ProgressDialog dialog = ProgressDialog.show(UserAvailableDonorMainMenu.this, "",
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

                                    if(!user.equals(userName) && status.equals("active")) {
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
                                        rvDonorList.setLayoutManager(new LinearLayoutManager(UserAvailableDonorMainMenu.this));
                                        mDonorListAdapter = new DonorListAdapter(donors, UserAvailableDonorMainMenu.this);
                                        rvDonorList.setAdapter(mDonorListAdapter);
                                    }
                                }, 1000);
                            }
                        }
                    }
                });
    }

}
