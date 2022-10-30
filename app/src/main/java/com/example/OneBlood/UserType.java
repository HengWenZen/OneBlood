package com.example.OneBlood;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.OneBlood.Activity.HospitalLogin;

public class UserType extends AppCompatActivity {

    RadioGroup mRadioGroup;
    RadioButton rbUser;
    RadioButton rbHospital;
    RadioButton rbAdmin;
    Button btnProceed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);

        mRadioGroup = findViewById(R.id.mRadioGroup);
        rbUser = findViewById(R.id.rbUser);
        rbHospital = findViewById(R.id.rbHospital);
        rbAdmin = findViewById(R.id.rbAdmin);
        btnProceed = findViewById(R.id.btnProceed);

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkedId = mRadioGroup.getCheckedRadioButtonId();
                if(checkedId == -1){
                    Toast.makeText(UserType.this, "Please select Type of Account !", Toast.LENGTH_SHORT).show();
                }else{
                    findRadioButton(checkedId);
                }
            }
        });
    }

    private void findRadioButton(int checkedId) {
        switch (checkedId){
            case R.id.rbUser:
                Intent i = new Intent(UserType.this, UserLogin.class);
                startActivity(i);
                finish();
                break;

            case R.id.rbHospital:
                Intent x = new Intent(UserType.this, HospitalLogin.class);
                startActivity(x);
                finish();
                break;

            case R.id.rbAdmin:
                Intent y = new Intent(UserType.this, AdminLogin.class);
                startActivity(y);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent y = new Intent(UserType.this, OnStart.class);
        startActivity(y);
        finish();
    }
}
