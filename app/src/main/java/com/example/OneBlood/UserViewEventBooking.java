package com.example.OneBlood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.OneBlood.Adapters.ViewEventBookingAdapter;
import com.example.OneBlood.Models.BookingEvent;
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

public class UserViewEventBooking extends AppCompatActivity {

    public static SharedPreferences mPreferences;
    private final String SHARED_PREF = "myPreferences";
    private final String KEY_USER = "user";
    private final String KEY_USER_NAME = "userName";

    private ViewEventBookingAdapter mViewEventBookingAdapter;
    private RecyclerView rv;
    ImageView ivBackToEventList;
    Button btnCancelAppointment;

    List<String> date;
    List<BookingEvent> mBookings;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_event_booking);



        SharedPreferences prefs = getSharedPreferences("myPreferences", MODE_PRIVATE);
        String user = prefs.getString(KEY_USER_NAME, null);

        rv = findViewById(R.id.rvViewEventBooking);
        btnCancelAppointment = findViewById(R.id.btn_view_event_booking);
        ivBackToEventList= findViewById(R.id.backToEventList);

        ivBackToEventList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        loadExistingAppointment(user);
    }

    private void loadExistingAppointment(String user) {
        mBookings = new ArrayList<>();
        ProgressDialog dialog = ProgressDialog.show(UserViewEventBooking.this, "",
                "Loading....", true);

        db.collection("userEventBooking").whereEqualTo("user", user).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot result = task.getResult();
                            if (!result.isEmpty()) {
                                for (QueryDocumentSnapshot document : result) {
                                    boolean check = checkDate(document.get("date").toString(), document.get("time").toString());
                                    if (check) {
                                        BookingEvent b = new BookingEvent(document.getId(),
                                                document.get("location").toString(),
                                                document.get("date").toString(),
                                                document.get("time").toString(),
                                                document.get("user").toString());
                                        mBookings.add(b);
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
                    mViewEventBookingAdapter = new ViewEventBookingAdapter(UserViewEventBooking.this, mBookings, false);
                    rv.setLayoutManager(new LinearLayoutManager(UserViewEventBooking.this));
                    rv.setAdapter(mViewEventBookingAdapter);
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
                        Intent y = new Intent(UserViewEventBooking.this, UserEvent.class);
                        startActivity(y);
                        finish();
                    }
                }).create().show();
    }

    private boolean checkDate(String result, String slot){
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

    public void adapterChange(int position){
        mBookings.remove(position);
        mViewEventBookingAdapter.notifyItemRemoved(position);
        if(mBookings.size() == 0) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UserViewEventBooking.this, UserEvent.class);
        startActivity(intent);
        finish();
    }
}
