package com.example.OneBlood.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.OneBlood.Activity.HospitalViewBloodRequestDetails;
import com.example.OneBlood.Models.EmergencyNotice;
import com.example.OneBlood.R;
import com.example.OneBlood.ViewBloodRequestDetails;

import java.util.List;

public class BloodRequestAdapters extends RecyclerView.Adapter<BloodRequestAdapters.EmergencyNoticeHolder> {
    private List<EmergencyNotice> mEmergencyNotice;
    private Context mContext;
    private boolean isHospital = false;

    public static final String EXTRA_EMERGENCY_NOTICE_TITLE = "noticeTitle";
    public static final String EXTRA_EMERGENCY_NOTICE_DATE = "noticeDate";
    public static final String EXTRA_EMERGENCY_NOTICE_DESCRIPTION = "noticeDescription";
    public static final String EXTRA_EMERGENCY_NOTICE_LOCATION = "locationName";
    public static final String EXTRA_EMERGENCY_NOTICE_ID = "noticeID";
    public static final String EXTRA_EMERGENCY_NOTICE_NAME = "postedBy";
    public static final String EXTRA_REQUEST_BLOOD_TYPE = "request_blood_type";
    public static final String EXTRA_EMERGENCY_NOTICE_CONTACT = "noticeContact";

    String ownerOfRequest;

    public BloodRequestAdapters(List<EmergencyNotice> emergencyNotices, Context context, boolean hospital, String postedBy){
        mEmergencyNotice = emergencyNotices;
        mContext = context;
        isHospital = hospital;
        ownerOfRequest = postedBy;
    }

    @NonNull
    @Override
    public BloodRequestAdapters.EmergencyNoticeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.list_emergency_notice, parent, false);
        return new BloodRequestAdapters.EmergencyNoticeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BloodRequestAdapters.EmergencyNoticeHolder holder, int position) {
        EmergencyNotice notice = mEmergencyNotice.get(position);
        holder.mTitle.setText(notice.getTitle());
        holder.mBloodType.setText(notice.getRequiredBloodType());

        holder.bindNotice(notice);
    }

    @Override
    public int getItemCount() {
        return mEmergencyNotice.size();
    }

    public class EmergencyNoticeHolder extends RecyclerView.ViewHolder{

        public TextView mTitle, mBloodType;
        private EmergencyNotice mEmergencyNotice;
        public Button btnViewEmergencyNotice;

        public void bindNotice(EmergencyNotice notice){
            mEmergencyNotice = notice;
            mTitle.setText(mEmergencyNotice.getTitle());
            mBloodType.setText(mEmergencyNotice.getRequiredBloodType());
        }
        public EmergencyNoticeHolder(@NonNull View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.list_item_emergency_notice_title);
            mBloodType = itemView.findViewById(R.id.list_item_emergency_notice_blood);
            btnViewEmergencyNotice = itemView.findViewById(R.id.btn_View_Emergency_Notice);

            btnViewEmergencyNotice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (isHospital == true) {

                        //Redirect to Hospital View Request Page if boolean is true
                        Intent i = new Intent(mContext, HospitalViewBloodRequestDetails.class);
                        i.putExtra(EXTRA_EMERGENCY_NOTICE_TITLE, mEmergencyNotice.getTitle());
                        i.putExtra(EXTRA_EMERGENCY_NOTICE_DATE, mEmergencyNotice.getDate());
                        i.putExtra(EXTRA_EMERGENCY_NOTICE_DESCRIPTION, mEmergencyNotice.getDescription());
                        i.putExtra(EXTRA_EMERGENCY_NOTICE_LOCATION, mEmergencyNotice.getLocation());
                        i.putExtra(EXTRA_EMERGENCY_NOTICE_ID, mEmergencyNotice.getId());
                        i.putExtra(EXTRA_REQUEST_BLOOD_TYPE, mEmergencyNotice.getRequiredBloodType());
                        i.putExtra(EXTRA_EMERGENCY_NOTICE_NAME, mEmergencyNotice.getPostedBy());
                        i.putExtra(EXTRA_EMERGENCY_NOTICE_CONTACT, mEmergencyNotice.getContact());
                        mContext.startActivity(i);

                    }else{

                        //Redirect to User View Request Page if boolean is false
                        Intent y = new Intent(mContext, ViewBloodRequestDetails.class);
                        y.putExtra(EXTRA_EMERGENCY_NOTICE_TITLE, mEmergencyNotice.getTitle());
                        y.putExtra(EXTRA_EMERGENCY_NOTICE_DATE, mEmergencyNotice.getDate());
                        y.putExtra(EXTRA_EMERGENCY_NOTICE_DESCRIPTION, mEmergencyNotice.getDescription());
                        y.putExtra(EXTRA_EMERGENCY_NOTICE_LOCATION, mEmergencyNotice.getLocation());
                        y.putExtra(EXTRA_EMERGENCY_NOTICE_ID, mEmergencyNotice.getId());
                        y.putExtra(EXTRA_REQUEST_BLOOD_TYPE, mEmergencyNotice.getRequiredBloodType());
                        y.putExtra(EXTRA_EMERGENCY_NOTICE_NAME, mEmergencyNotice.getPostedBy());
                        y.putExtra(EXTRA_EMERGENCY_NOTICE_CONTACT, mEmergencyNotice.getContact());
                        mContext.startActivity(y);
                    }
                }
            });
        }
    }
}
