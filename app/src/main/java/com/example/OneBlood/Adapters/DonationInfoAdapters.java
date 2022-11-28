package com.example.OneBlood.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.OneBlood.Models.DonationInfo;
import com.example.OneBlood.R;

import org.w3c.dom.Text;

import java.util.List;

public class DonationInfoAdapters extends RecyclerView.Adapter<DonationInfoAdapters.InfoHolder> {

    List<DonationInfo> mDonationInfo;

    public DonationInfoAdapters(List<DonationInfo> donationInfo) {
        mDonationInfo = donationInfo;
    }

    @NonNull
    @Override
    public DonationInfoAdapters.InfoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_blood_info, parent,false);
        return new InfoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonationInfoAdapters.InfoHolder holder, int position) {
        DonationInfo donationInfo = mDonationInfo.get(position);
        holder.infoTitle.setText(donationInfo.getInfoTitle());
        holder.infoDescription.setText(donationInfo.getInfoDescription());

        boolean isExpandable = mDonationInfo.get(position).isExpandable();
        holder.mRelativeLayout.setVisibility(isExpandable ?View.VISIBLE: View.GONE);

    }

    @Override
    public int getItemCount() {
        return mDonationInfo.size();
    }

    public class InfoHolder extends RecyclerView.ViewHolder {

        TextView infoTitle, infoDescription;
        LinearLayout mLinearLayout;
        RelativeLayout mRelativeLayout;


        public InfoHolder(@NonNull View itemView) {
            super(itemView);

            infoDescription = itemView.findViewById(R.id.infoDescription);
            infoTitle = itemView.findViewById(R.id.infoTitle);

            mLinearLayout = itemView.findViewById(R.id.linearLayout);
            mRelativeLayout = itemView.findViewById(R.id.expandableLayout);

            mLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DonationInfo donationInfo = mDonationInfo.get(getAdapterPosition());
                    donationInfo.setExpandable(!donationInfo.isExpandable());
                    notifyItemChanged(getAdapterPosition());
                }
            });

        }

    }
}
