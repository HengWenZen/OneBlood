package com.example.OneBlood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

public class UserDashBoard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static SharedPreferences mPreferences;
    private final String SHARED_PREF = "myPreferences";
    private final String KEY_USER_ID = "userID";
    private final String KEY_USER_NAME = "userName";
    private final String KEY_PASSWORD = "password";
    private final String KEY_USER_EMAIL = "userEmail";
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    ImageView ivLogout;
    TextView tvSearch;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_dash_board);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.navigation_view);
        mToolbar = findViewById(R.id.toolbar);
        ivLogout = findViewById(R.id.ivLogout);
        tvSearch = findViewById(R.id.tvSearch);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        String user = prefs.getString(KEY_USER_ID,"");
        tvSearch.setText(user);

        setSupportActionBar(mToolbar);

        mNavigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);

        mNavigationView.setCheckedItem(R.id.nav_home);

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor Editor = UserLogin.mPreferences.edit();
                Editor.clear();
                Editor.apply();
                Intent i = new Intent(UserDashBoard.this, UserLogin.class);
                startActivity(i);
                finish();
            }
        });

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

    @Override
    public void onBackPressed() {
        finish();
    }
}