package com.example.OneBlood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelfEvaluation extends AppCompatActivity {
    Button btnStartQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_self_evaluation);

        btnStartQuiz = findViewById(R.id.btnStartQuiz);

        btnStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SelfEvaluation.this, TestQuestion.class);
                startActivity(i);
                finish();
            }
        });

    }
}