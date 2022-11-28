package com.example.OneBlood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.OneBlood.Adapters.DonationInfoAdapters;
import com.example.OneBlood.Models.DonationInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class UserBloodDonationInfoPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static SharedPreferences mPreferences;
    private final String SHARED_PREF = "myPreferences";
    private final String KEY_USER_NAME = "userName";
    private final String KEY_USER_EMAIL = "userEmail";

    RecyclerView rv;
    List<DonationInfo> mDonationInfo;
    Button btnEvaluation;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    Toolbar mToolbar;
    String token, user, email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_blood_donation_info);

        rv = findViewById(R.id.rvBloodDonationInfo);
        btnEvaluation = findViewById(R.id.btnEvaluation);
        mDrawerLayout = findViewById(R.id.info_drawer_layout);
        mNavigationView = findViewById(R.id.info_navigation_view);
        mToolbar = (Toolbar) findViewById(R.id.info_toolbar);

        SharedPreferences prefs = getSharedPreferences("myPreferences", MODE_PRIVATE);
        user = prefs.getString(KEY_USER_NAME, null);
        email = prefs.getString(KEY_USER_EMAIL,"");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Info Page");

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

        mNavigationView.setCheckedItem(R.id.nav_info);

        initData();
        setRecyclerView();

        btnEvaluation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserBloodDonationInfoPage.this, SelfEvaluation.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void setRecyclerView() {
        DonationInfoAdapters donationInfoAdapters = new DonationInfoAdapters(mDonationInfo);
        rv.setAdapter(donationInfoAdapters);
        rv.setHasFixedSize(true);
    }

    private void initData() {
        mDonationInfo = new ArrayList<>();

        mDonationInfo.add(new DonationInfo("Blood Donation Criteria", "You can donate blood if:-\n" +
                "\n" +
                " - Well and healthy\n\n" +
                " - Age :\n\n" +
                "   1) First time donor: 18-60 years old\n\n" +
                "   2) Regular donor: 18-65 years old\n\n" +
                " - Prospective donor aged 17 years old must provide written consent from his or her parents / guardian\n\n" +
                " - Weight : 45kg and above\n\n" +
                " - Had minimum of 5 hours sleep\n\n" +
                " - Had a meal before blood donation\n\n" +
                " - No medical illness\n\n" +
                " - Not involved in any high risk activities such as :-\n\n" +
                "   1) Same gender sex (homosexual)\n\n" +
                "   2) Bisexual\n\n" +
                "   3) Had sex with commercial sex worker\n\n" +
                "   4) Change in sexual partner\n\n" +
                "   5) Took intravenous drug\n\n" +
                "   6) A sexual partner of any of the above\n\n" +
                " - Previous whole blood donation was 3 months ago\n\n" +
                " - For female donors : not pregnant, last menstrual period was more than 3 days ago, and not breastfeeding\n\n" +
                " - Do not donate blood if you had:\n\n" +
                "   1) Stayed in United Kingdom (England, Northern, Ireland, Scotland, Wales, Isle of Man or Channel Island) or Republic Ireland from year 1980 until 1996 for period of 6 months or more.\n\n" +
                "   2) Stayed in Europe from year 1980 until now, for a period of 5 years or more.\n\n"));

        mDonationInfo.add(new DonationInfo("Before Donation", " - Be well rested with at least five hours of sleep.\n\n" +
                " - Stay hydrated by drinking at least eight glasses\n of water a day.\n\n" +
                " - Former Covid-19 patient will only be allowed to donate blood 14 days after recovery.\n\n" +
                " - Vaccine recipients without adverse events following immunisation (AEFI) may donate blood seven days after their vaccination appointment. Those who experience AEFI can donate after seven days of recovery."));

        mDonationInfo.add(new DonationInfo("On Donation", " - Have a meal four hours before your appointment.\n\n" +
                " - Wear comfortable clothing. For those wearing long sleeves, ensure they can be folded up until the upper part of your arm.\n\n" +
                " - Bring along your identity card, driver’s licence or passport for registration and identification purposes.\n\n" +
                " - Drink enough water while waiting your turn to donate blood. You will have your finger pricked to test your blood group and hemoglobin levels.\n\n" +
                " - Any questions can be directed to a health care professional during your consultation session before the donation. The actual blood drawing process will only take up to 10 minutes."));

        mDonationInfo.add(new DonationInfo("Blood Donation Flow", "Step 1 : Registration \n" +
                "Step 2 : Examine Blood Group and Haemoglobin Level \n\n" +
                "Step 3 : Pre-donation Counselling by Nurse/ Medical Officer\n\n" +
                "Step 4 : Blood Donation\n\n" + "Step 6 : Take a Rest"));

        mDonationInfo.add(new DonationInfo("After Donation", "- Be sure to stay hydrated after leaving the blood donation site, with at least eight glasses of water a day.\n\n" +
                " - Get plenty of rest, especially within the first four hours after donating.\n\n" +
                " - Donors are discouraged from lifting heavy weights or engaging in strenuous exercise in the next six to eight hours to avoid bruising or swelling at the injection site.\n\n" +
                " - Avoid consuming alcoholic beverages and direct exposure to sunlight in the next 24 hours to avoid dehydration.\n\n" +
                " - If you start to feel faint or experience a headache, put all activities on hold and either sit or lie down until you feel better..\n\n" +
                " - If you continue to feel unwell, seek immediate medical attention at the closest health clinic or hospital"));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_info:
                break;

            case R.id.map_view:
                Intent z = new Intent(UserBloodDonationInfoPage.this, UserMainMenu.class);
                startActivity(z);
                finish();
                break;

            case R.id.nav_profile:
                Intent i = new Intent(UserBloodDonationInfoPage.this, EditProfile.class);
                startActivity(i);
                finish();
                break;

            case R.id.nav_appointment:
                Intent x = new Intent(UserBloodDonationInfoPage.this, UserAppointmentsMenu.class);
                startActivity(x);
                finish();
                break;

            case R.id.nav_home:
                Intent y = new Intent(UserBloodDonationInfoPage.this, UserDashBoard.class);
                startActivity(y);
                finish();
                break;

            case R.id.nav_request:
                Intent a = new Intent(UserBloodDonationInfoPage.this, UserBloodRequestMainMenu.class);
                startActivity(a);
                finish();
                break;

            case R.id.nav_events:
                Intent w = new Intent(UserBloodDonationInfoPage.this, UserEvent.class);
                startActivity(w);
                finish();
                break;

            case R.id.nav_notice:
                Intent e = new Intent(UserBloodDonationInfoPage.this, UserNoticeMenu.class);
                startActivity(e);
                finish();
                break;

            case R.id.nav_available_donors:
                Intent f = new Intent(UserBloodDonationInfoPage.this,UserAvailableDonorMainMenu.class);
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
                Intent q = new Intent(UserBloodDonationInfoPage.this, UserLogin.class);
                startActivity(q);
                finish();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent q = new Intent(UserBloodDonationInfoPage.this, UserDashBoard.class);
        startActivity(q);
        finish();
    }
}