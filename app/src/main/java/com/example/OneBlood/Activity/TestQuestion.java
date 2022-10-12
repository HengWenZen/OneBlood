package com.example.OneBlood.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.OneBlood.Models.QuestionList;
import com.example.OneBlood.R;

import java.util.ArrayList;
import java.util.List;

public class TestQuestion extends AppCompatActivity {

    private TextView questionNumber;
    private TextView questions;
    private AppCompatButton btnOption1, btnOption2;
    private int mCurrentIndex = 0;

    private QuestionList[] mQuestionLists = new QuestionList[]
            {
                    new QuestionList(R.string.question_age,false),
                    new QuestionList(R.string.question_donate_permanent,false),
                    new QuestionList(R.string.question_uk,false),
                    new QuestionList(R.string.question_ireland,false),
                    new QuestionList(R.string.question_treatment,false),
                    new QuestionList(R.string.question_vaccine,false),
                    new QuestionList(R.string.question_sexual_activity,false),
                    new QuestionList(R.string.question_health,false),
                    new QuestionList(R.string.question_tattoo,false),
            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_question);

        questionNumber = findViewById(R.id.tvQuestionNumber);
        questions = findViewById(R.id.tvQuestion);
        btnOption1 = findViewById(R.id.btnOption1);
        btnOption2 = findViewById(R.id.btnOption2);
        updateQuestion();


        btnOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        btnOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

    }

    private void checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue = mQuestionLists[mCurrentIndex].isAnswerTrue();

        if(userPressedTrue == answerIsTrue){
            mCurrentIndex = mCurrentIndex +1;
            updateQuestion();
        }
        else{
            Intent i = new Intent(TestQuestion.this, FailTest.class);
            startActivity(i);
            finish();
        }

    }

    private void updateQuestion() {
        int question = mQuestionLists[mCurrentIndex].getTextResId();
        questions.setText(question);
        questionNumber.setText((mCurrentIndex + 1) + " / " + mQuestionLists.length);

        if (mCurrentIndex == mQuestionLists.length - 1){
            Intent i = new Intent(TestQuestion.this, PassTest.class);
            startActivity(i);
            finish();
        }

    }

}