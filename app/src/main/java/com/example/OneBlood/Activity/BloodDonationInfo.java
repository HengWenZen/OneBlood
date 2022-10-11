package com.example.OneBlood.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.OneBlood.Adapters.DonationInfoAdapters;
import com.example.OneBlood.Models.DonationInfo;
import com.example.OneBlood.R;

import java.util.ArrayList;
import java.util.List;

public class BloodDonationInfo extends AppCompatActivity {

    RecyclerView rv;
    List<DonationInfo> mDonationInfo;
    Button btnEvaluation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_donation_info);

        rv = findViewById(R.id.rvBloodDonationInfo);
        btnEvaluation = findViewById(R.id.btnEvaluation);

        initData();
        setRecyclerView();

        btnEvaluation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BloodDonationInfo.this, SelfEvaluation.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void setRecyclerView() {
        DonationInfoAdapters donationInfoAdapters = new DonationInfoAdapters(mDonationInfo);
        rv.setAdapter(donationInfoAdapters);
        rv.setHasFixedSize(true);
    }

    private void initData() {
        mDonationInfo = new ArrayList<>();

        mDonationInfo.add(new DonationInfo("Blood Donation Criteria", "You can donate blood if:-\n" +
                "\n" +
                " - Well and healthy\n\n" +
                " - Age :\n\n" +
                "   1) First time donor: 18-60 years old\n\n" +
                "   2) Regular donor: 18-65 years old\n\n" +
                " - Prospective donor aged 17 years old must provide written consent from his or her parents / guardian\n\n" +
                " - Weight : 45kg and above\n\n" +
                " - Had minimum of 5 hours sleep\n\n" +
                " - Had a meal before blood donation\n\n" +
                " - No medical illness\n\n" +
                " - Not involved in any high risk activities such as :-\n\n" +
                "   1) Same gender sex (homosexual)\n\n" +
                "   2) Bisexual\n\n" +
                "   3) Had sex with commercial sex worker\n\n" +
                "   4) Change in sexual partner\n\n" +
                "   5) Took intravenous drug\n\n" +
                "   6) A sexual partner of any of the above\n\n" +
                " - Previous whole blood donation was 3 months ago\n\n" +
                " - For female donors : not pregnant, last menstrual period was more than 3 days ago, and not breastfeeding\n\n" +
                " - Do not donate blood if you had:\n\n" +
                "   1) Stayed in United Kingdom (England, Northern, Ireland, Scotland, Wales, Isle of Man or Channel Island) or Republic Ireland from year 1980 until 1996 for period of 6 months or more.\n\n" +
                "   2) Stayed in Europe from year 1980 until now, for a period of 5 years or more.\n\n"));

        mDonationInfo.add(new DonationInfo("Before Donation", " - Be well rested with at least five hours of sleep.\n\n" +
                " - Stay hydrated by drinking at least eight glasses\n of water a day.\n\n" +
                " - Former Covid-19 patient will only be allowed to donate blood 14 days after recovery.\n\n" +
                " - Vaccine recipients without adverse events following immunisation (AEFI) may donate blood seven days after their vaccination appointment. Those who experience AEFI can donate after seven days of recovery."));

        mDonationInfo.add(new DonationInfo("On Donation", " - Have a meal four hours before your appointment.\n\n" +
                " - Wear comfortable clothing. For those wearing long sleeves, ensure they can be folded up until the upper part of your arm.\n\n" +
                " - Bring along your identity card, driver’s licence or passport for registration and identification purposes.\n\n" +
                " - Drink enough water while waiting your turn to donate blood. You will have your finger pricked to test your blood group and hemoglobin levels.\n\n" +
                " - Any questions can be directed to a health care professional during your consultation session before the donation. The actual blood drawing process will only take up to 10 minutes."));

        mDonationInfo.add(new DonationInfo("Blood Donation Flow", "Step 1 : Registration \n" +
                "Step 2 : Examine Blood Group and Haemoglobin Level \n\n" +
                "Step 3 : Pre-donation Counselling by Nurse/ Medical Officer\n\n" +
                "Step 4 : Blood Donation\n\n" + "Step 6 : Take a Rest"));

        mDonationInfo.add(new DonationInfo("After Donation", "- Be sure to stay hydrated after leaving the blood donation site, with at least eight glasses of water a day.\n\n" +
                " - Get plenty of rest, especially within the first four hours after donating.\n\n" +
                " - Donors are discouraged from lifting heavy weights or engaging in strenuous exercise in the next six to eight hours to avoid bruising or swelling at the injection site.\n\n" +
                " - Avoid consuming alcoholic beverages and direct exposure to sunlight in the next 24 hours to avoid dehydration.\n\n" +
                " - If you start to feel faint or experience a headache, put all activities on hold and either sit or lie down until you feel better..\n\n" +
                " - If you continue to feel unwell, seek immediate medical attention at the closest health clinic or hospital"));
    }
}