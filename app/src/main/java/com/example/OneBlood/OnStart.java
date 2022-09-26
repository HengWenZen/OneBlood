package com.example.OneBlood;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class OnStart extends AppCompatActivity {

    Button btnSignIn;
    Button btnCreateAcc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnCreateAcc = findViewById(R.id.btnCreateAcc);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OnStart.this, UserType.class );
                startActivity(i);
                finish();
            }
        });

        btnCreateAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OnStart.this, UserRegister.class);
                startActivity(i);
                finish();
            }
        });
    }
}
