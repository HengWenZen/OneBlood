package com.example.OneBlood.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.OneBlood.R;
import com.example.OneBlood.UserDashBoard;

public class PassTest extends AppCompatActivity {

    Button btnBackToMenu, btnProceedToAppointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_test);

        btnBackToMenu = findViewById(R.id.btnBackToMenu);
        btnProceedToAppointment = findViewById(R.id.btnProceedToAppointment);

        btnBackToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PassTest.this, UserDashBoard.class);
                startActivity(i);
                finish();
            }
        });

        btnProceedToAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PassTest.this, Appointments.class);
                startActivity(i);
                finish();
            }
        });
    }
}