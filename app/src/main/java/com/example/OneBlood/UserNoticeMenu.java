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
import android.widget.ImageView;

import com.example.OneBlood.Adapters.NoticeAdapter;
import com.example.OneBlood.Labs.NoticeLab;
import com.example.OneBlood.Models.Notice;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

public class UserNoticeMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView rv;
    NoticeAdapter mNoticeAdapter;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    Toolbar mToolbar;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_notice_menu);

        rv = findViewById(R.id.rvUserNotice);
        mDrawerLayout = findViewById(R.id.notice_drawer_layout);
        mNavigationView = findViewById(R.id.notice_navigation_view);
        mToolbar = (Toolbar) findViewById(R.id.notice_toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Notice");

        mNavigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);

        mNavigationView.setCheckedItem(R.id.nav_notice);


        NoticeLab noticeLab = NoticeLab.get(this);
        List<Notice> notice = noticeLab.getNoticeList();

        ProgressDialog dialog = ProgressDialog.show(UserNoticeMenu.this, "",
                "Loading. Please wait...", true);   //show loading dialog
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();   //remove loading Dialog
                rv.setLayoutManager(new LinearLayoutManager(UserNoticeMenu.this));
                mNoticeAdapter = new NoticeAdapter(notice, UserNoticeMenu.this, false);
                rv.setAdapter(mNoticeAdapter);
            }
        }, 2000);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UserNoticeMenu.this, UserDashBoard.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_notice:
                break;

            case R.id.map_view:
                Intent z = new Intent(UserNoticeMenu.this, UserMainMenu.class);
                startActivity(z);
                finish();
                break;

            case R.id.nav_profile:
                Intent i = new Intent(UserNoticeMenu.this, EditProfile.class);
                startActivity(i);
                finish();
                break;

            case R.id.nav_available_donors:
                Intent x = new Intent(UserNoticeMenu.this, UserAvailableDonorMainMenu.class);
                startActivity(x);
                finish();
                break;

            case R.id.nav_home:
                Intent y = new Intent(UserNoticeMenu.this, UserDashBoard.class);
                startActivity(y);
                finish();
                break;

            case R.id.nav_request:
                Intent a = new Intent(UserNoticeMenu.this, UserBloodRequestMainMenu.class);
                startActivity(a);
                finish();
                break;

            case R.id.nav_events:
                Intent w = new Intent(UserNoticeMenu.this, UserEvent.class);
                startActivity(w);
                finish();
                break;

            case R.id.nav_appointment:
                Intent e = new Intent(UserNoticeMenu.this, UserAppointmentsMenu.class);
                startActivity(e);
                finish();
                break;

            case R.id.nav_info:
                Intent f = new Intent(UserNoticeMenu.this, UserBloodDonationInfoPage.class);
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
                Intent q = new Intent(UserNoticeMenu.this, UserLogin.class);
                startActivity(q);
                finish();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}