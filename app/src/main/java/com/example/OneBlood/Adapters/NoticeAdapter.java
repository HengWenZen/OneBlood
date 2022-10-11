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

import com.example.OneBlood.ViewNoticeDetails;
import com.example.OneBlood.Models.Notice;
import com.example.OneBlood.R;

import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeHolder> {
    private List<Notice> mNotices;
    private Context mContext;

    public static final String EXTRA_NOTICE_TITLE = "noticeTitle";
    public static final String EXTRA_NOTICE_DATE = "noticeDate";
    public static final String EXTRA_NOTICE_DESCRIPTION = "noticeDescription";
    public static final String EXTRA_NOTICE_HOSPITAL = "hospitalName";
    public static final String EXTRA_NOTICE_ID = "noticeID";



    public NoticeAdapter(List<Notice> notice, Context context){
        mNotices = notice;
        mContext = context;
    }


    @NonNull
    @Override
    public NoticeAdapter.NoticeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.list_notice, parent, false);
        return new NoticeAdapter.NoticeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeAdapter.NoticeHolder holder, int position) {
        Notice notice = mNotices.get(position);
        holder.bindNotice(notice);
    }

    @Override
    public int getItemCount() {
        return mNotices.size();
    }

    public class NoticeHolder extends RecyclerView.ViewHolder{

        public TextView mTitle, mDate;
        private  Notice mNotice;
        public Button btnViewNotice;

        public void bindNotice(Notice notice){
            mNotice = notice;
            mTitle.setText(mNotice.getTitle());
            mDate.setText(mNotice.getDate());
        }
        public NoticeHolder(@NonNull View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.list_item_notice_title);
            mDate = itemView.findViewById(R.id.list_item_notice_date);
            btnViewNotice = itemView.findViewById(R.id.btnViewNotoice);

            btnViewNotice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, ViewNoticeDetails.class);
                    i.putExtra(EXTRA_NOTICE_TITLE, mNotice.getTitle());
                    i.putExtra(EXTRA_NOTICE_DATE, mNotice.getDate());
                    i.putExtra(EXTRA_NOTICE_DESCRIPTION, mNotice.getDescription());
                    i.putExtra(EXTRA_NOTICE_HOSPITAL, mNotice.getHospitalName());
                    mContext.startActivity(i);
                }
            });

        }
    }
}
