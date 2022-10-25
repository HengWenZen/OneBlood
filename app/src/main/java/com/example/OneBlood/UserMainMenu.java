package com.example.OneBlood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.OneBlood.Labs.LocationLab;
import com.example.OneBlood.Models.DonateLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserMainMenu extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, NavigationView.OnNavigationItemSelectedListener {

    public static SharedPreferences mPreferences;
    private final String SHARED_PREF = "myPreferences";
    private final String KEY_USER_ID = "userID";
    private final String KEY_USER_NAME = "userName";
    private final String KEY_PASSWORD = "password";
    private final String KEY_USER_EMAIL = "userEmail";

    public static final String EXTRA_LOCATION_TITLE = "location_name";
    public static final String EXTRA_LOCATION_ADDRESS = "location_address";
    public static final String EXTRA_LOCATION_CONTACT = "location_contact";
    public static final String EXTRA_LOCATION_OPERATION_HOUR = "location_operation_hours";
    public static final String EXTRA_LOCATION_IMAGE_URI = "imageUri";

    boolean isPermissionGranted;
    GoogleMap mGoogleMap;
    FloatingActionButton fab;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    Toolbar mToolbar;
    StorageReference storageReference;
    ImageView mPopupImage;
    String imageId, locationTitle, locationAddress, locationContact,locationOperationHrs, locationLongitude, locationLatitude, token;
    private List<DonateLocation> mDonateLocations = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        LocationLab locationLab = LocationLab.get(UserMainMenu.this);
        mDonateLocations = locationLab.getLocation();

        fab = findViewById(R.id.float_btn);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.navigation_view);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mPreferences = getSharedPreferences("myPreferences", MODE_PRIVATE);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Map View");

        mNavigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);

        mNavigationView.setCheckedItem(R.id.map_view);

        checkMyPermission();
        initMap();
        mFusedLocationProviderClient = new FusedLocationProviderClient(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    private void initMap() {
        if(isPermissionGranted){
            SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
            .findFragmentById(R.id.Map);
            supportMapFragment.getMapAsync(this);
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Location location = task.getResult();
                gotoLocation(location.getLatitude(),location.getLongitude());
            }
        });
    }

    private void gotoLocation(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
        mGoogleMap.moveCamera(cameraUpdate);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    private void checkMyPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Toast.makeText(UserMainMenu.this,"Permission Granted", Toast.LENGTH_SHORT).show();
                isPermissionGranted = true;
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package",getPackageName(),"");
                intent.setData(uri);
                startActivity(intent);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * add markers or lines, add listeners or move the camera. In this case,
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        mGoogleMap.setMyLocationEnabled(true);

        getCurrentLocation();

        ProgressDialog dialog = ProgressDialog.show(UserMainMenu.this, "",
                "Loading......", true);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                for (DonateLocation location : mDonateLocations)
                {
                    LatLng newLatLng = new LatLng(Double.parseDouble(location.getLatitude()), Double.parseDouble(location.getLongitude()));
                    mGoogleMap.addMarker(new MarkerOptions().position(newLatLng).title(location.getID()));
                }
                dialog.dismiss();
            }
        }, 3000);


        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {

                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View popWindow = inflater.inflate(R.layout.popup_window, null);

                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int height = size.y;

                PopupWindow popupWindow = new PopupWindow(popWindow);
                popupWindow.setWidth(width-40);
                popupWindow.setHeight(height/2-100);
                popupWindow.setFocusable(true);

                popupWindow.showAtLocation(popWindow, Gravity.BOTTOM, 0, 150);

                //Close Popup Window once touched
                popWindow.setOnTouchListener(new View.OnTouchListener()
                {
                    @Override
                    public boolean onTouch(View v, MotionEvent event)
                    {
                        popupWindow.dismiss();
                        return true;
                    }
                });

                TextView mPopText = popWindow.findViewById(R.id.tvPopTitle);
                TextView mPopAddress = popWindow.findViewById(R.id.tvPopAddress);
                Button btnViewLocation = popWindow.findViewById(R.id.btnViewLocation);
                Button btnGetDirection = popWindow.findViewById(R.id.btnGetDirection);
                mPopupImage =  popWindow.findViewById(R.id.ivPopupImage);

                for (DonateLocation location : mDonateLocations)
                {
                    if (location.getID().equals(marker.getTitle())){
                    mPopText.setText(location.getTitle());
                    mPopAddress.setText(location.getAddress());
                    locationTitle = location.getTitle();
                    locationAddress = location.getAddress();
                    locationContact = location.getContact();
                    locationOperationHrs = location.getOperationHrs();
                    locationLongitude = location.getLongitude();
                    locationLatitude = location.getLatitude();
                    imageId = location.getImageUrl();
                    }
                }

                btnViewLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(UserMainMenu.this, TimeSlotBooking.class);
                        i.putExtra(EXTRA_LOCATION_TITLE, locationTitle);
                        i.putExtra(EXTRA_LOCATION_ADDRESS, locationAddress);
                        i.putExtra(EXTRA_LOCATION_CONTACT, locationContact);
                        i.putExtra(EXTRA_LOCATION_OPERATION_HOUR, locationOperationHrs);
                        i.putExtra(EXTRA_LOCATION_IMAGE_URI, imageId);
                        startActivity(i);
                        popupWindow.dismiss();
                    }
                });

                btnGetDirection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String msg = "Open Google Map?";
                        AlertDialog.Builder alert = new AlertDialog.Builder(UserMainMenu.this);
                        alert.setTitle("Redirect to Google Map");
                        alert.setMessage(msg);
                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Uri gmmIntentUri = Uri.parse("google.navigation:q="+locationLatitude+","+locationLongitude);
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps");
                                startActivity(mapIntent);
                                popupWindow.dismiss();
                            }
                        });
                        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        alert.show();
                    }
                });


                storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference ref
                        = storageReference
                        .child(
                                "images/"
                                        + imageId);
                try {
                    File localfile = File.createTempFile("tempfile", ".jpg");
                    ref.getFile(localfile)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                    mPopupImage.setImageBitmap(bitmap);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UserMainMenu.this, "FAIL TO RETRIEVE", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

                LatLng latLng = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
                mGoogleMap.moveCamera(cameraUpdate);
                return true;
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.map_view:
                break;

            case R.id.nav_home:
                Intent z = new Intent(UserMainMenu.this, UserDashBoard.class);
                startActivity(z);
                finish();
                break;

            case R.id.nav_profile:
                Intent i = new Intent(UserMainMenu.this, EditProfile.class);
                startActivity(i);
                finish();
                break;

            case R.id.nav_appointment:
                Intent x = new Intent(UserMainMenu.this, UserAppointmentsMenu.class);
                startActivity(x);
                finish();
                break;

            case R.id.nav_available_donors:
                Intent y = new Intent(UserMainMenu.this, UserAvailableDonorMainMenu.class);
                startActivity(y);
                finish();
                break;

            case R.id.nav_request:
                Intent a = new Intent(UserMainMenu.this, UserBloodRequestMainMenu.class);
                startActivity(a);
                finish();
                break;

            case R.id.nav_events:
                Intent w = new Intent(UserMainMenu.this, UserEvent.class);
                startActivity(w);
                finish();
                break;

            case R.id.nav_notice:
                Intent e = new Intent(UserMainMenu.this, UserNoticeMenu.class);
                startActivity(e);
                finish();
                break;

            case R.id.nav_info:
                Intent f = new Intent(UserMainMenu.this, UserBloodDonationInfoPage.class);
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
                Intent q = new Intent(UserMainMenu.this, UserLogin.class);
                startActivity(q);
                finish();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}