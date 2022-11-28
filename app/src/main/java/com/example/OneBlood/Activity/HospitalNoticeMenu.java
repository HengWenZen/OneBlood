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
import android.widget.ImageView;

import com.example.OneBlood.Adapters.NoticeAdapter;
import com.example.OneBlood.Labs.NoticeLab;
import com.example.OneBlood.Models.Notice;
import com.example.OneBlood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HospitalNoticeMenu extends AppCompatActivity {
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
    RecyclerView rv;
    NoticeAdapter mNoticeAdapter;
    Button btnNewNotice, btnMyNotice;
    ImageView ivBackToMenu;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String hospital;

    List<Notice> mNotices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_notice_menu);

        rv = findViewById(R.id.rvHospitalNoticeList);
        btnNewNotice = findViewById(R.id.btnNewNotice);
        btnMyNotice = findViewById(R.id.btnMyNotice);
        ivBackToMenu = findViewById(R.id.ivBackToMenu);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        hospital = prefs.getString(KEY_HOSPITAL_NAME, "");

        loadNotices();

        btnNewNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HospitalNoticeMenu.this, HospitalNewNotice.class);
                startActivity(i);
                finish();
            }
        });

        btnMyNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HospitalNoticeMenu.this, HospitalViewNotice.class);
                startActivity(i);
                finish();
            }
        });

        ivBackToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadNotices()
    {
        mNotices = new ArrayList<>();
        ProgressDialog dialog = ProgressDialog.show(HospitalNoticeMenu.this, "",
                "Loading....", true);

        //Retrieve Data from Database
        db.collection("notice")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot result = task.getResult();
                            if (!result.isEmpty()) {
                                for (QueryDocumentSnapshot document : result) {
                                    String noticeHospital = document.get("hospitalName").toString();
                                    if(!hospital.equals(noticeHospital)) {
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
                //Dismiss dialog
                dialog.dismiss();
                    mNoticeAdapter = new NoticeAdapter(mNotices, HospitalNoticeMenu.this, false);
                    rv.setLayoutManager(new LinearLayoutManager(HospitalNoticeMenu.this));
                    rv.setAdapter(mNoticeAdapter);
                }
        }, 1000);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(HospitalNoticeMenu.this, HospitalMenu.class);
        startActivity(i);
        finish();
    }
}