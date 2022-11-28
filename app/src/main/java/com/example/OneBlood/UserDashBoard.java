package com.example.OneBlood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.OneBlood.Adapters.DashboardEventAdapter;
import com.example.OneBlood.Adapters.ViewBloodRequestAdapter;
import com.example.OneBlood.Models.Booking;
import com.example.OneBlood.Models.DashboardEvents;
import com.example.OneBlood.Models.ViewBloodRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

public class UserDashBoard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static SharedPreferences mPreferences;
    private final String SHARED_PREF = "myPreferences";
    private final String KEY_USER_ID = "userID";
    private final String KEY_USER_NAME = "userName";
    private final String KEY_PASSWORD = "password";
    private final String KEY_USER_EMAIL = "userEmail";
    private final String KEY_USER_BLOOD_TYPE = "userStatus";

    DrawerLayout mDrawerLayout;
    RecyclerView rv;
    List<DashboardEvents> mDashboardEvents;
    DashboardEventAdapter mDashboardEventsAdapter;
    NavigationView mNavigationView;
    RelativeLayout cardViewRequest, cardViewAppointment, cardViewDonors, cardViewEvents;
    ImageView ivLogout;
    TextView tvSearch, nextAppointment, bloodType, totalDonors, tvSetUserName;
    Toolbar mToolbar;
    int counter = 0;
    int livesSaved = 0;
    String user, userName, userBloodType, livesCounter, nextDate,status, email;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dash_board);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.navigation_view);
        cardViewRequest = findViewById(R.id.cardViewBloodRequest);
        cardViewAppointment = findViewById(R.id.cardViewAppointment);
        cardViewEvents = findViewById(R.id.cardViewUpcomingEvents);
        cardViewDonors = findViewById(R.id.cardViewAvailableDonors);
        nextAppointment = findViewById(R.id.tvDateOfBooking);
        tvSetUserName = findViewById(R.id.tvSetUserName);
        totalDonors = findViewById(R.id.tvTotalDonors);
        rv = findViewById(R.id.rvDashboardEvent);
        mToolbar = findViewById(R.id.toolbar);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        user = prefs.getString(KEY_USER_NAME,"");
        email = prefs.getString(KEY_USER_EMAIL,"");
        userBloodType = prefs.getString(KEY_USER_BLOOD_TYPE,"");
        Log.d("TAG", "onCreate: " + user + userBloodType + email);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("One Blood");
        tvSetUserName.setText(user);

        View headerView = mNavigationView.getHeaderView(0);
        TextView mTvHeaderProfileName = (TextView) headerView.findViewById(R.id.tvUserName);
        mTvHeaderProfileName.setText(user);
        TextView mTvHeaderEmail = (TextView) headerView.findViewById(R.id.tvUserMenuEmail);
        mTvHeaderEmail.setText(email);

        mNavigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);

        mNavigationView.setCheckedItem(R.id.nav_home);

        cardViewAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserDashBoard.this, UserAppointmentsMenu.class);
                startActivity(i);
                finish();
            }
        });

        cardViewDonors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserDashBoard.this, UserAvailableDonorMainMenu.class);
                startActivity(i);
                finish();
            }
        });

        cardViewEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserDashBoard.this, UserEvent.class);
                startActivity(i);
                finish();
            }
        });

        cardViewRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(UserDashBoard.this, UserBloodRequestMainMenu.class);
                startActivity(a);
                finish();
            }
        });

        loadEventList();
        retrieveData();


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                break;

            case R.id.map_view:
                Intent z = new Intent(UserDashBoard.this, UserMainMenu.class);
                startActivity(z);
                finish();
                break;

            case R.id.nav_profile:
                Intent i = new Intent(UserDashBoard.this, EditProfile.class);
                startActivity(i);
                finish();
                break;

            case R.id.nav_appointment:
                Intent x = new Intent(UserDashBoard.this, UserAppointmentsMenu.class);
                startActivity(x);
                finish();
                break;

            case R.id.nav_available_donors:
                Intent y = new Intent(UserDashBoard.this, UserAvailableDonorMainMenu.class);
                startActivity(y);
                finish();
                break;

            case R.id.nav_request:
                Intent a = new Intent(UserDashBoard.this, UserBloodRequestMainMenu.class);
                startActivity(a);
                finish();
                break;

            case R.id.nav_events:
                Intent w = new Intent(UserDashBoard.this, UserEvent.class);
                startActivity(w);
                finish();
                break;

            case R.id.nav_notice:
                Intent e = new Intent(UserDashBoard.this, UserNoticeMenu.class);
                startActivity(e);
                finish();
                break;

            case R.id.nav_info:
                Intent f = new Intent(UserDashBoard.this, UserBloodDonationInfoPage.class);
                startActivity(f);
                finish();
                break;

            case R.id.nav_logout:
                SharedPreferences.Editor spEditor = UserLogin.mPreferences.edit();
                spEditor.clear().commit();
                Intent q = new Intent(UserDashBoard.this, UserLogin.class);
                startActivity(q);
                finish();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadEventList() {
       mDashboardEvents = new ArrayList<>();

        ProgressDialog dialog = ProgressDialog.show(UserDashBoard.this, "",
                "Loading. Please wait...", true);   //show loading dialog

        db.collection("events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot result = task.getResult();
                            if (!result.isEmpty()) {
                                for (QueryDocumentSnapshot document : result) {

                                    String eventDate = document.get("startDate").toString();

                                       DashboardEvents dashboardEvents = new DashboardEvents(document.get("title").toString(),
                                               document.getId(),
                                               document.get("startDate").toString(),
                                               document.get("startTime").toString());
                                       mDashboardEvents.add(dashboardEvents);

                                    Log.d("data", eventDate );
                                }
                            }
                        }

                    }
                });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();   //remove loading Dialog
                if (mDashboardEvents.size() == 0) {
                    Log.d("TAG", "run: error");
                } else {
                    rv.hasFixedSize();
                    rv.setLayoutManager(new LinearLayoutManager(UserDashBoard.this, LinearLayoutManager.HORIZONTAL, true));
                    mDashboardEventsAdapter = new DashboardEventAdapter(mDashboardEvents, UserDashBoard.this);
                    rv.setAdapter(mDashboardEventsAdapter);

                    LinearSnapHelper snapHelper = new LinearSnapHelper();
                    snapHelper.attachToRecyclerView(rv);
                }
            }
        }, 1000);
    }

    private void retrieveData(){
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot result = task.getResult();
                        if(!result.isEmpty()){
                            int NoOfDonors = result.size();
                            String donors = String.valueOf(NoOfDonors);
                            totalDonors.setText(donors);
                        }
                    }
                });

        db.collection("latestAppointment")
                .whereEqualTo("user", user)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot result = task.getResult();
                        if(!result.isEmpty()) {
                            for (QueryDocumentSnapshot document : result) {
                                nextDate = document.get("date").toString();
                                status = document.get("status").toString();
                                if(status.equals("confirmed")){
                                    nextAppointment.setText(nextDate);
                                }else if(status.equals("available")){
                                    nextAppointment.setText("-");
                                }
                            }
                        }else if(result.isEmpty()){
                            nextAppointment.setText("-");
                        }
                    }
                });
    }


    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setMessage("Log Out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences.Editor spEditor = UserLogin.mPreferences.edit();
                        spEditor.clear().commit();
                        Intent intent = new Intent(UserDashBoard.this, UserLogin.class);
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