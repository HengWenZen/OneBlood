package com.example.OneBlood.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.OneBlood.Models.BloodRequest;
import com.example.OneBlood.R;
import com.example.OneBlood.ViewBloodRequestDetails;

import java.util.List;

public class BloodRequestAdapter extends RecyclerView.Adapter<BloodRequestAdapter.BloodRequestHolder> {

    private List<BloodRequest> mBloodRequestList;
    private Context mContext;
    public static final String EXTRA_REQUEST_TITLE = "request_title";
    public static final String EXTRA_REQUEST_DATE = "request_date";
    public static final String EXTRA_REQUEST_DESCRIPTION = "request_description";
    public static final String EXTRA_REQUEST_BLOOD_TYPE = "request_blood_type";
    public static final String EXTRA_REQUEST_ID = "request_id";
    public static final String EXTRA_REQUEST_LOCATION = "request_location";
    public static final String EXTRA_RECIPIENT_NAME = "recipient_name";
    public static final String EXTRA_RECIPIENT_EMAIL = "recipient_email";
    public static final String EXTRA_RECIPIENT_CONTACT = "recipient_contact";

    public BloodRequestAdapter(List<BloodRequest> bloodRequests, Context context) {
        mBloodRequestList = bloodRequests;
        mContext = context;
    }

    @NonNull
    @Override
    public BloodRequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.list_blood_request,parent,false);
        return new BloodRequestAdapter.BloodRequestHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BloodRequestAdapter.BloodRequestHolder holder, int position) {
        BloodRequest bloodRequest = mBloodRequestList.get(position);
        holder.bindBloodRequest(bloodRequest);
    }


    @Override
    public int getItemCount() {
        return mBloodRequestList.size();
    }

    public class BloodRequestHolder extends RecyclerView.ViewHolder{

        public TextView mTextView, mTitle, mBloodType;
        private BloodRequest mBloodRequest;

        public void bindBloodRequest(BloodRequest bloodRequest){
            mBloodRequest = bloodRequest;
            mTitle.setText(mBloodRequest.getTitle());
            mTextView.setText(mBloodRequest.getDate());
            mBloodType.setText(mBloodRequest.getBloodType());
        }

        public BloodRequestHolder(@NonNull View itemView) {
            super(itemView);
            mTitle= itemView.findViewById(R.id.list_item_blood_request_title);
            mTextView = itemView.findViewById(R.id.list_item_blood_request_date);
            mBloodType = itemView.findViewById(R.id.list_item_blood_type);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, ViewBloodRequestDetails.class);

                    i.putExtra(EXTRA_REQUEST_TITLE, mBloodRequest.getTitle());
                    i.putExtra(EXTRA_REQUEST_DATE, mBloodRequest.getDate());
                    i.putExtra(EXTRA_REQUEST_DESCRIPTION, mBloodRequest.getDescription());
                    i.putExtra(EXTRA_REQUEST_LOCATION, mBloodRequest.getLocation());
                    i.putExtra(EXTRA_REQUEST_BLOOD_TYPE, mBloodRequest.getBloodType());
                    i.putExtra(EXTRA_REQUEST_ID, mBloodRequest.getID());
                    i.putExtra(EXTRA_RECIPIENT_NAME, mBloodRequest.getRecipient());
                    i.putExtra(EXTRA_RECIPIENT_CONTACT, mBloodRequest.getContact());
                    i.putExtra(EXTRA_RECIPIENT_EMAIL, mBloodRequest.getEmail());

                    mContext.startActivity(i);

                }
            });
        }
    }
}
