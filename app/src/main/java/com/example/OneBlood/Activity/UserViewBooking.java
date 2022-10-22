package com.example.OneBlood.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.OneBlood.Adapters.ViewBookingAdapter;
import com.example.OneBlood.Models.Booking;
import com.example.OneBlood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserViewBooking extends AppCompatActivity {

    public static SharedPreferences mPreferences;
    private final String SHARED_PREF = "myPreferences";
    private final String KEY_USER = "user";
    private final String KEY_USER_NAME = "userName";

    private ViewBookingAdapter mViewBookingAdapter;
    private RecyclerView rv;
    Button btnCancelAppointment;

    List<String> date;
    List<Booking> mBookings;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_booking);

        SharedPreferences prefs = getSharedPreferences("myPreferences", MODE_PRIVATE);
        String user = prefs.getString(KEY_USER_NAME, null);

        rv = findViewById(R.id.rvViewBooking);
        btnCancelAppointment = findViewById(R.id.btn_view_booking);

        loadExistingAppointment(user);
    }

    private void loadExistingAppointment(String user) {
        mBookings = new ArrayList<>();
        ProgressDialog dialog = ProgressDialog.show(UserViewBooking.this, "",
                "Loading....", true);

        db.collection("userBooking").whereEqualTo("user", user).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot result = task.getResult();
                            if (!result.isEmpty()) {
                                for (QueryDocumentSnapshot document : result) {
                                    boolean check = checkDate(document.get("date").toString(), document.get("slot").toString());
                                    if (check) {
                                        Booking b = new Booking(document.getId(),
                                                document.get("location").toString(),
                                                document.get("date").toString(),
                                                document.get("slot").toString(),
                                                document.get("user").toString());
                                        mBookings.add(b);
                                    }else {


                                    }
                                }
                                Log.d("data", mBookings.toString());
                            }
                        }
                    }
                });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();
                if(mBookings.size() == 0) {
                    alertDataEmpty();
                }
                else {
                    mViewBookingAdapter = new ViewBookingAdapter(UserViewBooking.this, mBookings, false);
                    rv.setLayoutManager(new LinearLayoutManager(UserViewBooking.this));
                    rv.setAdapter(mViewBookingAdapter);
                }
            }
        }, 1000);

    }

    private void alertDataEmpty() {
        new AlertDialog.Builder(this)
                .setMessage("No Existing Appointment Made..")
                .setPositiveButton("Return", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent y = new Intent(UserViewBooking.this, Appointments.class);
                        startActivity(y);
                        finish();
                    }
                }).create().show();
    }

    private boolean checkDate(String result, String slot){
        slot = timeSlot(Integer.valueOf(slot));
        result = result.concat(" " + slot);
        Date strDate = null;
        try {
            strDate = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (new Date().getTime() > strDate.getTime()) {
            return false;
        }
        else{
            return true;
        }
    }

    public String timeSlot(int position) {
        switch (position) {
            case 0:
                return "9:00-10:00";
            case 1:
                return "10:00-11:00";
            case 2:
                return "11:00-12:00";
            case 3:
                return "12:00-13:00";
            case 4:
                return "13:00-14:00";
            case 5:
                return "14:00-15:00";
            case 6:
                return "15:00-16:00";
            case 7:
                return "16:00-17:00";
            case 8:
                return "17:00-18:00";
            default:
                return "Closed";
        }
    }

    public void adapterChange(int position){
        mBookings.remove(position);
        mViewBookingAdapter.notifyItemRemoved(position);
        if(mBookings.size() == 0) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UserViewBooking.this, Appointments.class);
        startActivity(intent);
        finish();
    }
}