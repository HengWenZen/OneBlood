package com.example.OneBlood.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.OneBlood.Models.DashboardEvents;
import com.example.OneBlood.Models.DonateLocation;
import com.example.OneBlood.Models.Events;
import com.example.OneBlood.R;
import com.example.OneBlood.UserEvent;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

import java.util.List;

public class DashboardEventAdapter extends RecyclerView.Adapter<DashboardEventAdapter.eventHolder> {
    List<DashboardEvents> mDashboardEvents;
    private Context mContext;

    public DashboardEventAdapter(List<DashboardEvents> dashboardEvents, Context context){
        mDashboardEvents = dashboardEvents;
        mContext = context;
    }

    @NonNull
    @Override
    public DashboardEventAdapter.eventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.upcoming_events, parent, false);
        return new DashboardEventAdapter.eventHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardEventAdapter.eventHolder holder, int position) {
        DashboardEvents dashboardEvents = mDashboardEvents.get(position);
        holder.bindEvent(dashboardEvents);
    }

    @Override
    public int getItemCount() {
        return mDashboardEvents.size();
    }

    public class eventHolder extends RecyclerView.ViewHolder{

        TextView title, date, time;
        private DashboardEvents events;

        public eventHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.eventTitle);
            date = itemView.findViewById(R.id.eventDate);
            time = itemView.findViewById(R.id.eventTime);

        }

        public void bindEvent (DashboardEvents dashboardEvents) {
            events = dashboardEvents;
            title.setText(dashboardEvents.getEventTitle());
            date.setText(dashboardEvents.getEventDate());
            time.setText(dashboardEvents.getEventTime());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, UserEvent.class);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
