package com.example.OneBlood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.OneBlood.R;
import com.google.android.material.textfield.TextInputLayout;

public class ViewNoticeDetails extends AppCompatActivity {

    public static final String EXTRA_NOTICE_TITLE = "noticeTitle";
    public static final String EXTRA_NOTICE_DATE = "noticeDate";
    public static final String EXTRA_NOTICE_DESCRIPTION = "noticeDescription";
    public static final String EXTRA_NOTICE_HOSPITAL = "hospitalName";
    public static final String EXTRA_NOTICE_ID = "noticeID";

    TextInputLayout etViewNoticeTitle, etViewNoticeDescription, etViewHospitalName, etNoticeDate;
    ImageView backToNoticeMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notice_details);

        etViewHospitalName = findViewById(R.id.etViewHospitalName);
        etViewNoticeDescription = findViewById(R.id.etViewNoticeDescription);
        etViewNoticeTitle = findViewById(R.id.etViewNoticeTitle);
        etNoticeDate = findViewById(R.id.etViewDateOfNotice);
        backToNoticeMenu = findViewById(R.id.backToNoticeMenu);

        
        backToNoticeMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        etViewNoticeTitle.getEditText().setText((String)b.get(EXTRA_NOTICE_TITLE));
        etViewNoticeTitle.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etViewNoticeDescription.getEditText().setText((String)b.get(EXTRA_NOTICE_DESCRIPTION));
        etViewNoticeDescription.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etNoticeDate.getEditText().setText((String)b.get(EXTRA_NOTICE_DATE));
        etNoticeDate.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etViewHospitalName.getEditText().setText((String)b.get(EXTRA_NOTICE_HOSPITAL));
        etViewHospitalName.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}