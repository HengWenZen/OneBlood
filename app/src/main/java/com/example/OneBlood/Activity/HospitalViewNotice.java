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

import com.example.OneBlood.Adapters.NoticeAdapter;
import com.example.OneBlood.Adapters.ViewEventBookingAdapter;
import com.example.OneBlood.Models.BookingEvent;
import com.example.OneBlood.Models.Notice;
import com.example.OneBlood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HospitalViewNotice extends AppCompatActivity {

    public static SharedPreferences hospitalPreferences;
    private final String SHARED_PREFERENCE = "hospitalPreferences";
    private final String KEY_HOSPITAL_ID = "hospitalID";
    private final String KEY_HOSPITAL_NAME = "hospitalName";
    private final String KEY_HOSPITAL_CONTACT = "hospitalContact";

    public static final String EXTRA_NOTICE_TITLE = "noticeTitle";
    public static final String EXTRA_NOTICE_DATE = "noticeDate";
    public static final String EXTRA_NOTICE_DESCRIPTION = "noticeDescription";
    public static final String EXTRA_NOTICE_HOSPITAL = "hospitalName";
    public static final String EXTRA_NOTICE_ID = "noticeID";

    private NoticeAdapter mNoticeAdapter;
    private RecyclerView rv;
    String hospital, noticeId, noticeDate, noticeDescription, noticeTitle;

    List<String> date;
    List<Notice> mNotices;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_view_notice);

        rv = findViewById(R.id.rvHospitalViewNotice);

        noticeId = getIntent().getStringExtra(EXTRA_NOTICE_ID);
        noticeTitle = getIntent().getStringExtra(EXTRA_NOTICE_TITLE);
        noticeDate = getIntent().getStringExtra(EXTRA_NOTICE_DATE);
        noticeDescription = getIntent().getStringExtra(EXTRA_NOTICE_DESCRIPTION);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        hospital = prefs.getString(KEY_HOSPITAL_NAME, "");

        loadNotice();
    }

    private void loadNotice(){
        mNotices = new ArrayList<>();
        ProgressDialog dialog = ProgressDialog.show(HospitalViewNotice.this, "",
                "Loading....", true);

        db.collection("notice")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot result = task.getResult();
                            if (!result.isEmpty()) {
                                for (QueryDocumentSnapshot document : result) {
                                        String hospitalName = document.get("hospitalName").toString();
                                        if(hospital.equals(hospitalName)) {
                                            Notice notice = new Notice(document.getId(),
                                                    document.get("title").toString(),
                                                    document.get("description").toString(),
                                                    document.get("hospitalName").toString(),
                                                    document.get("date").toString());
                                            mNotices.add(notice);
                                        }
                                    }
                                }
                                Log.d("data", mNotices.toString());
                            }
                        }
                    });

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    dialog.dismiss();
                    if(mNotices.size() == 0) {
                        alertDataEmpty();
                    }
                    else {
                        mNoticeAdapter = new NoticeAdapter(mNotices, HospitalViewNotice.this, true);
                        rv.setLayoutManager(new LinearLayoutManager(HospitalViewNotice.this));
                        rv.setAdapter(mNoticeAdapter);
                    }
                }
            }, 1000);
    }

    private void alertDataEmpty() {
        new AlertDialog.Builder(this)
                .setMessage("No Existing Notice Made...")
                .setPositiveButton("Return", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent y = new Intent(HospitalViewNotice.this, HospitalNoticeMenu.class);
                        startActivity(y);
                        finish();
                    }
                }).create().show();
    }

    public void adapterChange(int position){
        mNotices.remove(position);
        mNoticeAdapter.notifyItemRemoved(position);
        if(mNotices.size() == 0) {
            finish();
        }
    }
}
