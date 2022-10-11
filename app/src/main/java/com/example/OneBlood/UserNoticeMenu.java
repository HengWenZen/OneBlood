package com.example.OneBlood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;

import com.example.OneBlood.Activity.BloodRequestMainMenu;
import com.example.OneBlood.Adapters.BloodRequestAdapter;
import com.example.OneBlood.Adapters.NoticeAdapter;
import com.example.OneBlood.Labs.NoticeLab;
import com.example.OneBlood.Models.Notice;

import java.util.List;

public class UserNoticeMenu extends AppCompatActivity {

    RecyclerView rv;
    ImageView ivBackToHome;
    NoticeAdapter mNoticeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_notice_menu);

        rv = findViewById(R.id.rvUserNotice);
        ivBackToHome = findViewById(R.id.ivBackToHomePage);

        NoticeLab noticeLab = NoticeLab.get(this);
        List<Notice> notice = noticeLab.getNoticeList();

        ProgressDialog dialog = ProgressDialog.show(UserNoticeMenu.this, "",
                "Loading. Please wait...", true);   //show loading dialog
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();   //remove loading Dialog
                rv.setLayoutManager(new LinearLayoutManager(UserNoticeMenu.this));
                mNoticeAdapter = new NoticeAdapter(notice, UserNoticeMenu.this);
                rv.setAdapter(mNoticeAdapter);
            }
        }, 2000);
    }
}