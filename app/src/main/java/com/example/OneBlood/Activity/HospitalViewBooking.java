package com.example.OneBlood.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

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

public class HospitalViewBooking extends AppCompatActivity {
    private ViewBookingAdapter mViewBookingAdapter;
    private RecyclerView rv;

    List<String> list = new ArrayList<>();
    List<Booking> mBookings;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_booking);

        rv=findViewById(R.id.rv_AdminViewBooking);

        loadBookingList();
    }

    private void loadBookingList() {
        mBookings = new ArrayList<>();

        ProgressDialog dialog = ProgressDialog.show(HospitalViewBooking.this, "",
                "Loading....", true);

        db.collection("userBooking")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot result = task.getResult();
                    if (!result.isEmpty()) {
                        for (QueryDocumentSnapshot document : result) {
                            boolean check = checkDate(document.get("date").toString(),document.get("slot").toString());
                            if(check) {
//                            Log.d("data",document.getData().toString());
                                Booking b = new Booking(document.getId(),
                                        document.get("location").toString(),
                                        document.get("date").toString(),
                                        document.get("slot").toString(),
                                        document.get("user").toString());
                                mBookings.add(b);
                            }
                        }
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
                    mViewBookingAdapter = new ViewBookingAdapter(HospitalViewBooking.this, mBookings, true);
                    rv.setLayoutManager(new LinearLayoutManager(HospitalViewBooking.this));
                    rv.setAdapter(mViewBookingAdapter);
                }
            }
        }, 2000);
    }

    private boolean checkDate(String result, String slot) {

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

    private void alertDataEmpty() {
        new AlertDialog.Builder(this)
                .setMessage("No Booking Existing!")
                .setPositiveButton("Return", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).create().show();
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
        finish();
        Intent intent = new Intent(HospitalViewBooking.this, HospitalMenu.class);
        startActivity(intent);
    }
}