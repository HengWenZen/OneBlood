package com.example.OneBlood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.OneBlood.Labs.DonorLab;
import com.example.OneBlood.Adapters.DonorListAdapter;
import com.example.OneBlood.Models.Donor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class UserAvailableDonorMainMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static SharedPreferences mPreferences;
    private final String SHARED_PREF = "myPreferences";
    private final String KEY_USER = "user";
    private final String KEY_USER_NAME = "userName";
    private final String KEY_USER_EMAIL = "userEmail";
    private final String KEY_USER_STATUS = "userStatus";

    RecyclerView rvDonorList;
    Button btnFilter;
    DonorListAdapter mDonorListAdapter;
    Spinner spinnerBloodType;
    String selectedBloodType, user, email;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    Toolbar mToolbar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<Donor> mDonors = new ArrayList<>();
    ArrayList<Donor> allDonorList = new ArrayList<>();
    List<Donor> donors;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_available_donor_main_menu);

        rvDonorList = findViewById(R.id.rvDonorList);
        spinnerBloodType = findViewById(R.id.spinnerBloodType);
        btnFilter = findViewById(R.id.btnFilter);
        mDrawerLayout = findViewById(R.id.available_donors_drawer_layout);
        mNavigationView = findViewById(R.id.available_donors_navigation_view);
        mToolbar = (Toolbar) findViewById(R.id.available_donors_toolbar);

        SharedPreferences prefs = getSharedPreferences("myPreferences", MODE_PRIVATE);
        user = prefs.getString(KEY_USER_NAME, null);
        email = prefs.getString(KEY_USER_EMAIL,"");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Available Donors");

        mNavigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        View headerView = mNavigationView.getHeaderView(0);
        TextView mTvHeaderProfileName = (TextView) headerView.findViewById(R.id.tvUserName);
        mTvHeaderProfileName.setText(user);
        TextView mTvHeaderEmail = (TextView) headerView.findViewById(R.id.tvUserMenuEmail);
        mTvHeaderEmail.setText(email);

        mNavigationView.setNavigationItemSelectedListener(this);

        mNavigationView.setCheckedItem(R.id.nav_available_donors);

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
                                    String userBloodType = document.get("blood type").toString();

                                    if(!user.equals(userName) && status.equals("active") && !userBloodType.equals("rh null")) {
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


    @Override
    public void onBackPressed() {

        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else{
            Intent intent = new Intent(UserAvailableDonorMainMenu.this, UserDashBoard.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.nav_available_donors:
                    break;

                case R.id.map_view:
                    Intent z = new Intent(UserAvailableDonorMainMenu.this, UserMainMenu.class);
                    startActivity(z);
                    finish();
                    break;

                case R.id.nav_profile:
                    Intent i = new Intent(UserAvailableDonorMainMenu.this, EditProfile.class);
                    startActivity(i);
                    finish();
                    break;

                case R.id.nav_appointment:
                    Intent x = new Intent(UserAvailableDonorMainMenu.this, UserAppointmentsMenu.class);
                    startActivity(x);
                    finish();
                    break;

                case R.id.nav_home:
                    Intent y = new Intent(UserAvailableDonorMainMenu.this, UserDashBoard.class);
                    startActivity(y);
                    finish();
                    break;

                case R.id.nav_request:
                    Intent a = new Intent(UserAvailableDonorMainMenu.this, UserBloodRequestMainMenu.class);
                    startActivity(a);
                    finish();
                    break;

                case R.id.nav_events:
                    Intent w = new Intent(UserAvailableDonorMainMenu.this, UserEvent.class);
                    startActivity(w);
                    finish();
                    break;

                case R.id.nav_notice:
                    Intent e = new Intent(UserAvailableDonorMainMenu.this, UserNoticeMenu.class);
                    startActivity(e);
                    finish();
                    break;

                case R.id.nav_info:
                    Intent f = new Intent(UserAvailableDonorMainMenu.this, UserBloodDonationInfoPage.class);
                    startActivity(f);
                    finish();
                    break;

                case R.id.nav_logout:
                    SharedPreferences.Editor spEditor = UserLogin.mPreferences.edit();
                    spEditor.clear().commit();
                    Intent q = new Intent(UserAvailableDonorMainMenu.this, UserLogin.class);
                    startActivity(q);
                    finish();
                    break;
            }
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
}


