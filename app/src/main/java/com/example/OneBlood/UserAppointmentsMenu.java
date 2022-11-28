package com.example.OneBlood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.OneBlood.Adapters.LocationAdapter;
import com.example.OneBlood.Labs.LocationLab;
import com.example.OneBlood.Models.DonateLocation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

public class UserAppointmentsMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static SharedPreferences mPreferences;
    private final String SHARED_PREF = "myPreferences";
    private final String KEY_USER_ID = "userID";
    private final String KEY_USER_NAME = "userName";
    private final String KEY_PASSWORD = "password";
    private final String KEY_USER_EMAIL = "userEmail";
    private final String KEY_USER_BLOOD_TYPE = "userStatus";

    Button btnViewAppointment;
    RecyclerView mRecyclerView;
    LocationAdapter mAdapter;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    Toolbar mToolbar;
    String token, user, email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_make_appointment);

        btnViewAppointment = findViewById(R.id.btnViewAppointment);
        mRecyclerView = findViewById(R.id.myRecyclerView);
        mDrawerLayout = findViewById(R.id.appointments_drawer_layout);
        mNavigationView = findViewById(R.id.appointments_navigation_view);
        mToolbar = (Toolbar) findViewById(R.id.appointments_toolbar);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        user = prefs.getString(KEY_USER_NAME,"");
        email = prefs.getString(KEY_USER_EMAIL,"");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Available Donation Location");

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

        mNavigationView.setCheckedItem(R.id.nav_appointment);

        btnViewAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserAppointmentsMenu.this, UserViewBooking.class);
                startActivity(i);
                finish();
            }
        });

        LocationLab locationLab = LocationLab.get(UserAppointmentsMenu.this);
        List<DonateLocation> locations = locationLab.getLocation();

        ProgressDialog dialog = ProgressDialog.show(UserAppointmentsMenu.this, "",
                "Loading. Please wait...", true);   //show loading dialog
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();   //remove loading Dialog
                mRecyclerView.setLayoutManager(new LinearLayoutManager(UserAppointmentsMenu.this));
                mAdapter = new LocationAdapter(locations, UserAppointmentsMenu.this);
                mRecyclerView.setAdapter(mAdapter);
            }
        }, 1000);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UserAppointmentsMenu.this, UserDashBoard.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_appointment:
                break;

            case R.id.map_view:
                Intent z = new Intent(UserAppointmentsMenu.this, UserMainMenu.class);
                startActivity(z);
                finish();
                break;

            case R.id.nav_profile:
                Intent i = new Intent(UserAppointmentsMenu.this, EditProfile.class);
                startActivity(i);
                finish();
                break;

            case R.id.nav_available_donors:
                Intent x = new Intent(UserAppointmentsMenu.this, UserAvailableDonorMainMenu.class);
                startActivity(x);
                finish();
                break;

            case R.id.nav_home:
                Intent y = new Intent(UserAppointmentsMenu.this, UserDashBoard.class);
                startActivity(y);
                finish();
                break;

            case R.id.nav_request:
                Intent a = new Intent(UserAppointmentsMenu.this, UserBloodRequestMainMenu.class);
                startActivity(a);
                finish();
                break;

            case R.id.nav_events:
                Intent w = new Intent(UserAppointmentsMenu.this, UserEvent.class);
                startActivity(w);
                finish();
                break;

            case R.id.nav_notice:
                Intent e = new Intent(UserAppointmentsMenu.this, UserNoticeMenu.class);
                startActivity(e);
                finish();
                break;

            case R.id.nav_info:
                Intent f = new Intent(UserAppointmentsMenu.this, UserBloodDonationInfoPage.class);
                startActivity(f);
                finish();
                break;

            case R.id.nav_logout:
                SharedPreferences myPreferences = getSharedPreferences("myPreferences", MODE_PRIVATE);
                SharedPreferences.Editor spEditor = myPreferences.edit();
                spEditor.clear();
                spEditor.apply();
                spEditor.commit();

                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if(!task.isSuccessful()){
                                    Log.d("FCM", "Failed to retrieve Token!");
                                    return;
                                }

                                token = task.getResult();
                                Log.d("FCM", "FCM Token : " + token);
                            }
                        });

                FirebaseMessaging.getInstance().unsubscribeFromTopic("user").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "Unsubscribe Successfully!");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "Fail to Subscribe ");
                    }
                });
                Intent q = new Intent(UserAppointmentsMenu.this, UserLogin.class);
                startActivity(q);
                finish();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
