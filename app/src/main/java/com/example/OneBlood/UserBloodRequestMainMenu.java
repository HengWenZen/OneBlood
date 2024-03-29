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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.OneBlood.Activity.HospitalViewOwnBloodRequest;
import com.example.OneBlood.Adapters.BloodRequestAdapters;
import com.example.OneBlood.Models.EmergencyNotice;
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

public class UserBloodRequestMainMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static SharedPreferences mPreferences;
    private final String SHARED_PREF = "myPreferences";
    private final String KEY_USER = "user";
    private final String KEY_USER_NAME = "userName";
    private final String KEY_USER_EMAIL = "userEmail";

    Button btnViewYourRequest, btnNewRequest;
    RecyclerView rv;
    BloodRequestAdapters mBloodRequestAdapters;
    List<EmergencyNotice> mEmergencyNotices;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String postedBy ,user,token, email;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_blood_request_main_menu);

        btnNewRequest = findViewById(R.id.btnNewRequest);
        btnViewYourRequest = findViewById(R.id.btnViewYourRequest);
        rv = findViewById(R.id.rvBloodRequestList);
        mDrawerLayout = findViewById(R.id.blood_request_drawer_layout);
        mNavigationView = findViewById(R.id.blood_request_navigation_view);
        mToolbar = (Toolbar) findViewById(R.id.blood_request_toolbar);

        SharedPreferences prefs = getSharedPreferences("myPreferences", MODE_PRIVATE);
        user = prefs.getString(KEY_USER_NAME, null);
        email = prefs.getString(KEY_USER_EMAIL,"");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Blood Request");

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

        mNavigationView.setCheckedItem(R.id.nav_request);

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
                Intent i = new Intent(UserBloodRequestMainMenu.this, UserViewBloodRequest.class);
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
                mBloodRequestAdapters = new BloodRequestAdapters(mEmergencyNotices, UserBloodRequestMainMenu.this, false, postedBy);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_request:
                break;

            case R.id.map_view:
                Intent z = new Intent(UserBloodRequestMainMenu.this, UserMainMenu.class);
                startActivity(z);
                finish();
                break;

            case R.id.nav_profile:
                Intent i = new Intent(UserBloodRequestMainMenu.this, EditProfile.class);
                startActivity(i);
                finish();
                break;

            case R.id.nav_available_donors:
                Intent x = new Intent(UserBloodRequestMainMenu.this, UserAvailableDonorMainMenu.class);
                startActivity(x);
                finish();
                break;

            case R.id.nav_home:
                Intent y = new Intent(UserBloodRequestMainMenu.this, UserDashBoard.class);
                startActivity(y);
                finish();
                break;

            case R.id.nav_appointment:
                Intent a = new Intent(UserBloodRequestMainMenu.this, UserAppointmentsMenu.class);
                startActivity(a);
                finish();
                break;

            case R.id.nav_events:
                Intent w = new Intent(UserBloodRequestMainMenu.this, UserEvent.class);
                startActivity(w);
                finish();
                break;

            case R.id.nav_notice:
                Intent e = new Intent(UserBloodRequestMainMenu.this, UserNoticeMenu.class);
                startActivity(e);
                finish();
                break;

            case R.id.nav_info:
                Intent f = new Intent(UserBloodRequestMainMenu.this, UserBloodDonationInfoPage.class);
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
                Intent q = new Intent(UserBloodRequestMainMenu.this, UserLogin.class);
                startActivity(q);
                finish();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
