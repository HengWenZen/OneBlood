package com.example.OneBlood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.OneBlood.Adapters.EventsAdapter;
import com.example.OneBlood.Models.Events;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserEvent extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static SharedPreferences mPreferences;
    private final String SHARED_PREF = "myPreferences";
    private final String KEY_USER_NAME = "userName";
    private final String KEY_USER_EMAIL = "userEmail";

    Button btnViewEventAppointment;
    RecyclerView rv;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    Toolbar mToolbar;
    String token,user, email;
    Date mEndDate, mCurrentDate;
    EventsAdapter mEventsAdapter;
    List<Events> mEventsList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_event);

        rv = findViewById(R.id.rvEventList);
        btnViewEventAppointment = findViewById(R.id.btnViewEventAppointment);
        mDrawerLayout = findViewById(R.id.events_drawer_layout);
        mNavigationView = findViewById(R.id.events_navigation_view);
        mToolbar = (Toolbar) findViewById(R.id.events_toolbar);

        SharedPreferences prefs = getSharedPreferences("myPreferences", MODE_PRIVATE);
        user = prefs.getString(KEY_USER_NAME, null);
        email = prefs.getString(KEY_USER_EMAIL,"");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Events");

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

        mNavigationView.setCheckedItem(R.id.nav_events);

        loadEventList();

        btnViewEventAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserEvent.this, UserViewEventBooking.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadEventList(){
        mEventsList = new ArrayList<>();

        db.collection("events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot result = task.getResult();
                            if (!result.isEmpty()) {
                                Log.d("Data Retrieved", result.toString());
                                for (QueryDocumentSnapshot document : result) {
                                    Log.d("Document ID:", document.getId() + " => " + document.getData());
                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                    String currentDate = df.format(new Date());

                                    try {
                                        mEndDate = df.parse(document.get("endDate").toString());
                                        mCurrentDate = df.parse(currentDate);

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    long diff = mCurrentDate.getTime() - mEndDate.getTime();
                                    int numberOfDays = (int) diff;

                                    if(numberOfDays < 0) {
                                        Events events = new Events(document.get("startDate").toString(),
                                                document.get("endDate").toString(),
                                                document.get("location").toString(),
                                                document.get("imageUri").toString(),
                                                document.getId(),
                                                document.get("title").toString(),
                                                document.get("description").toString(),
                                                document.get("startTime").toString(),
                                                document.get("endTime").toString(),
                                                document.get("postedBy").toString());
                                        mEventsList.add(events);
                                    }
                                }
                            }
                        }
                    }
                });


        ProgressDialog dialog = ProgressDialog.show(UserEvent.this, "",
                "Loading. Please wait...", true);   //show loading dialog
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();   //remove loading Dialog
                rv.setLayoutManager(new LinearLayoutManager(UserEvent.this));
                mEventsAdapter = new EventsAdapter( mEventsList,UserEvent.this, false);
                rv.setAdapter(mEventsAdapter);
            }
        }, 1000);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UserEvent.this, UserDashBoard.class);
        startActivity(intent);
        finish();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_events:
                break;

            case R.id.map_view:
                Intent z = new Intent(UserEvent.this, UserMainMenu.class);
                startActivity(z);
                finish();
                break;

            case R.id.nav_profile:
                Intent i = new Intent(UserEvent.this, EditProfile.class);
                startActivity(i);
                finish();
                break;

            case R.id.nav_available_donors:
                Intent x = new Intent(UserEvent.this, UserAvailableDonorMainMenu.class);
                startActivity(x);
                finish();
                break;

            case R.id.nav_home:
                Intent y = new Intent(UserEvent.this, UserDashBoard.class);
                startActivity(y);
                finish();
                break;

            case R.id.nav_request:
                Intent a = new Intent(UserEvent.this, UserBloodRequestMainMenu.class);
                startActivity(a);
                finish();
                break;

            case R.id.nav_appointment:
                Intent w = new Intent(UserEvent.this, UserAppointmentsMenu.class);
                startActivity(w);
                finish();
                break;

            case R.id.nav_notice:
                Intent e = new Intent(UserEvent.this, UserNoticeMenu.class);
                startActivity(e);
                finish();
                break;

            case R.id.nav_info:
                Intent f = new Intent(UserEvent.this, UserBloodDonationInfoPage.class);
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
                Intent q = new Intent(UserEvent.this, UserLogin.class);
                startActivity(q);
                finish();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
