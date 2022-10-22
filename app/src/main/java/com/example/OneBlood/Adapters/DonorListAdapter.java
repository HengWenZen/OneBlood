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

import com.example.OneBlood.Models.Donor;
import com.example.OneBlood.R;
import com.example.OneBlood.ViewDonorDetails;

import java.util.List;

public class DonorListAdapter extends RecyclerView.Adapter<DonorListAdapter.DonorListHolder> {

    private List<Donor> mDonorList;
    private Context mContext;
    public static final String EXTRA_DONOR_NAME = "donor_name";
    public static final String EXTRA_DONOR_CONTACT = "donor_contact";
    public static final String EXTRA_DONOR_BLOOD_TYPE = "donor_blood_type";
    public static final String EXTRA_DONOR_ID = "donor_id";
    public static final String EXTRA_DONOR_EMAIL = "donor_email";

    public DonorListAdapter(List<Donor> donors, Context context){
        mDonorList = donors;
        mContext = context;
    }

    public void addNewList(List<Donor>donors){
        mDonorList = donors;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public DonorListAdapter.DonorListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.list_donors,parent,false);
        return new DonorListAdapter.DonorListHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull DonorListAdapter.DonorListHolder holder, int position) {
        Donor donor = mDonorList.get(position);
        String status = donor.getUserStatus();

        if (status.equals("inactive")){
            holder.itemView.setVisibility(View.GONE);
        }else{
            holder.itemView.setVisibility(View.VISIBLE);
        }
        holder.bindDonor(donor);
    }

    @Override
    public int getItemCount() {
        return mDonorList.size();
    }

    public class DonorListHolder  extends RecyclerView.ViewHolder{

        public TextView tvBloodType, tvName;
        public Button btnViewDonorDetails;
        private Donor mDonorList;

        public void bindDonor(Donor donor) {
            mDonorList = donor;
            tvBloodType.setText(mDonorList.getBloodType());
            tvName.setText(mDonorList.getName());
        }

        public DonorListHolder(@NonNull View itemView) {
            super(itemView);
            tvBloodType = itemView.findViewById(R.id.list_item_donor_blood_type);
            tvName = itemView.findViewById(R.id.list_item_donors_name);
            btnViewDonorDetails = itemView.findViewById(R.id.btnViewDonorDetails);


            btnViewDonorDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, ViewDonorDetails.class);
                    i.putExtra(EXTRA_DONOR_NAME,mDonorList.getName());
                    i.putExtra(EXTRA_DONOR_ID, mDonorList.getId());
                    i.putExtra(EXTRA_DONOR_BLOOD_TYPE, mDonorList.getBloodType());
                    i.putExtra(EXTRA_DONOR_CONTACT, mDonorList.getContact());
                    i.putExtra(EXTRA_DONOR_EMAIL, mDonorList.getEmail());
                    mContext.startActivity(i);
                }
            });


        }

    }
}
