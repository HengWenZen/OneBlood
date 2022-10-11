package com.example.OneBlood.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.example.OneBlood.Adapters.NoticeAdapter;
import com.example.OneBlood.HospitalNewNotice;
import com.example.OneBlood.Labs.NoticeLab;
import com.example.OneBlood.Models.Notice;
import com.example.OneBlood.R;

import java.util.List;

public class HospitalNoticeMenu extends AppCompatActivity {
    RecyclerView rv;
    NoticeAdapter mNoticeAdapter;
    Button btnNewNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_notice_menu);

        rv = findViewById(R.id.rvHospitalNoticeList);
        btnNewNotice = findViewById(R.id.btnNewNotice);

        btnNewNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HospitalNoticeMenu.this, HospitalNewNotice.class);
                startActivity(i);
                finish();
            }
        });

        NoticeLab noticeLab = NoticeLab.get(this);
        List<Notice> notice = noticeLab.getNoticeList();

        ProgressDialog dialog = ProgressDialog.show(HospitalNoticeMenu.this, "",
                "Loading. Please wait...", true);   //show loading dialog
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();   //remove loading Dialog
                rv.setLayoutManager(new LinearLayoutManager(HospitalNoticeMenu.this));
                mNoticeAdapter = new NoticeAdapter(notice, HospitalNoticeMenu.this);
                rv.setAdapter(mNoticeAdapter);
            }
        }, 2000);
    }
}