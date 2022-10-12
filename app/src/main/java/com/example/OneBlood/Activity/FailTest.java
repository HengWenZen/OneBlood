package com.example.OneBlood.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.OneBlood.R;

public class FailTest extends AppCompatActivity {

    Button btnBackToInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fail_test);

        btnBackToInfo = findViewById(R.id.btnBackToInfoMenu);

        btnBackToInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FailTest.this, SelfEvaluation.class);
                startActivity(i);
                finish();
            }
        });
    }
}