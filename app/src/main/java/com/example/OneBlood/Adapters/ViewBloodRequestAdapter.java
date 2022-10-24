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

import com.example.OneBlood.UserBloodRequestDetails;
import com.example.OneBlood.Activity.HospitalViewOwnBloodRequestDetails;
import com.example.OneBlood.Activity.HospitalViewOwnBloodRequest;
import com.example.OneBlood.UserViewBloodRequest;
import com.example.OneBlood.Models.ViewBloodRequest;
import com.example.OneBlood.R;

import java.util.List;

public class ViewBloodRequestAdapter extends RecyclerView.Adapter<ViewBloodRequestAdapter.ViewBloodRequestHolder>{
    private Context mContext;
    private List<ViewBloodRequest> mBloodRequestList;
    boolean isHospital = false;

    String requestTitle, requestDescription, requestBloodType, requestDate, requestPostedBy, requestContact, requestLocation, requestId;

    public static final String EXTRA_OWN_REQUEST_ID= "requestId";
    public static final String EXTRA_OWN_REQUEST_DATE = "requestDate";
    public static final String EXTRA_OWN_REQUEST_TITLE = "requestTitle";
    public static final String EXTRA_OWN_REQUEST_DESCRIPTION = "requestDescription";
    public static final String EXTRA_OWN_REQUEST_BLOOD = "requestBloodType";
    public static final String EXTRA_OWN_REQUEST_POSTED_BY = "requestPostedBy";
    public static final String EXTRA_OWN_REQUEST_CONTACT= "requestContact";
    public static final String EXTRA_OWN_REQUEST_LOCATION = "requestLocation";

    public ViewBloodRequestAdapter(List<ViewBloodRequest> viewBloodRequests, Context context, boolean isHospital) {
        this.mContext = context;
        this.mBloodRequestList = viewBloodRequests;
        this.isHospital = isHospital;
    }

    @NonNull
    @Override
    public ViewBloodRequestAdapter.ViewBloodRequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.list_own_request, parent, false);
        return new ViewBloodRequestAdapter.ViewBloodRequestHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewBloodRequestAdapter.ViewBloodRequestHolder holder, int position) {
        ViewBloodRequest viewBloodRequest = mBloodRequestList.get(position);
        requestBloodType = viewBloodRequest.getBloodType();
        requestDate = viewBloodRequest.getDate();
        requestContact = viewBloodRequest.getContact();
        requestTitle = viewBloodRequest.getTitle();
        requestDescription = viewBloodRequest.getDescription();
        requestPostedBy = viewBloodRequest.getPostedBy();
        requestLocation = viewBloodRequest.getLocation();
        requestId = viewBloodRequest.getID();

        holder.bindBloodRequest(viewBloodRequest);
    }

    @Override
    public int getItemCount() {
        return mBloodRequestList.size();
    }

    public class ViewBloodRequestHolder extends RecyclerView.ViewHolder {

        TextView bloodRequestTitle, bloodRequestBloodType;
        Button btnViewOwnRequest;
        ViewBloodRequest mViewBloodRequest;

        public void bindBloodRequest(ViewBloodRequest viewBloodRequest) {
            mViewBloodRequest = viewBloodRequest;
            bloodRequestTitle.setText(requestTitle);
            bloodRequestBloodType.setText(requestBloodType);

        }

        public ViewBloodRequestHolder(@NonNull View itemView) {
            super(itemView);
            bloodRequestTitle = itemView.findViewById(R.id.list_blood_request_title);
            bloodRequestBloodType = itemView.findViewById(R.id.list_request_blood_type);
            btnViewOwnRequest = itemView.findViewById(R.id.btn_view_own_request_detail);

            btnViewOwnRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(isHospital == false) {

                        ((UserViewBloodRequest) mContext).adapterChange(getAdapterPosition());
                        Intent y = new Intent(mContext, UserBloodRequestDetails.class);
                        y.putExtra(EXTRA_OWN_REQUEST_ID, requestId);
                        y.putExtra(EXTRA_OWN_REQUEST_DATE, requestDate);
                        y.putExtra(EXTRA_OWN_REQUEST_TITLE, requestTitle);
                        y.putExtra(EXTRA_OWN_REQUEST_DESCRIPTION, requestDescription);
                        y.putExtra(EXTRA_OWN_REQUEST_LOCATION, requestLocation);
                        y.putExtra(EXTRA_OWN_REQUEST_BLOOD, requestBloodType);
                        y.putExtra(EXTRA_OWN_REQUEST_CONTACT, requestContact);
                        y.putExtra(EXTRA_OWN_REQUEST_POSTED_BY, requestPostedBy);
                        mContext.startActivity(y);

                    }else{

                        ((HospitalViewOwnBloodRequest) mContext).adapterChange(getAdapterPosition());
                        Intent intent = new Intent(mContext, HospitalViewOwnBloodRequestDetails.class);
                        intent.putExtra(EXTRA_OWN_REQUEST_ID, requestId);
                        intent.putExtra(EXTRA_OWN_REQUEST_DATE, requestDate);
                        intent.putExtra(EXTRA_OWN_REQUEST_TITLE, requestTitle);
                        intent.putExtra(EXTRA_OWN_REQUEST_DESCRIPTION, requestDescription);
                        intent.putExtra(EXTRA_OWN_REQUEST_LOCATION, requestLocation);
                        intent.putExtra(EXTRA_OWN_REQUEST_BLOOD, requestBloodType);
                        intent.putExtra(EXTRA_OWN_REQUEST_CONTACT, requestContact);
                        intent.putExtra(EXTRA_OWN_REQUEST_POSTED_BY, requestPostedBy);
                        mContext.startActivity(intent);

                    }
                }

            });
        }
    }
}






