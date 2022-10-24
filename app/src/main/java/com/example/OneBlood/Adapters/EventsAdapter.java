package com.example.OneBlood.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.OneBlood.Activity.HospitalViewEventDetails;
import com.example.OneBlood.UserEventTimeSlot;
import com.example.OneBlood.Models.Events;
import com.example.OneBlood.R;

import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventHolder>{

    private List<Events> mEvents;
    private Context mContext;

    public static final String EXTRA_EVENT_TITLE = "title";
    public static final String EXTRA_EVENT_LOCATION = "location";
    public static final String EXTRA_EVENT_DESCRIPTION = "description";
    public static final String EXTRA_EVENT_START_DATE = "startDate";
    public static final String EXTRA_EVENT_END_DATE = "endDate";
    public static final String EXTRA_EVENT_START_TIME = "startTime";
    public static final String EXTRA_EVENT_END_TIME = "endTime";
    public static final String EXTRA_EVENT_POSTED_BY = "postedBy";
    public static final String EXTRA_EVENT_ID = "eventId";

    public static SharedPreferences mPreferences;
    private final String SHARED_PREF = "myPreferences";
    private final String KEY_USER = "user";
    boolean hospital;

    public EventsAdapter (List<Events> events, Context context, boolean isHospital){
        mEvents = events;
        mContext = context;
        hospital = isHospital;
    }


    @NonNull
    @Override
    public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.list_events, parent,false);
        return new EventsAdapter.EventHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsAdapter.EventHolder holder, int position) {
        Events events = mEvents.get(position);
        holder.bindEvents(events);
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    public class EventHolder extends RecyclerView.ViewHolder{

        private Events mEvents;
        public TextView mEventTitle, mEventLocation;
        public Button btnViewDetails;

        public void bindEvents(Events events){
            mEvents = events;
            mEventTitle.setText(mEvents.getTitle());
            mEventLocation.setText(mEvents.getLocation());
        }

        public EventHolder(@NonNull View itemView) {
            super(itemView);
            mEventTitle = itemView.findViewById(R.id.list_item_event_title);
            mEventLocation = itemView.findViewById(R.id.list_item_event_location);
            btnViewDetails = itemView.findViewById(R.id.btnBookTimeSlot);

            btnViewDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(hospital == false) {
                        Intent i = new Intent(mContext, UserEventTimeSlot.class);
                        i.putExtra(EXTRA_EVENT_TITLE, mEvents.getTitle());
                        i.putExtra(EXTRA_EVENT_LOCATION, mEvents.getLocation());
                        i.putExtra(EXTRA_EVENT_START_DATE, mEvents.getStartDate());
                        i.putExtra(EXTRA_EVENT_END_DATE, mEvents.getEndDate());
                        i.putExtra(EXTRA_EVENT_DESCRIPTION, mEvents.getDescription());
                        i.putExtra(EXTRA_EVENT_START_TIME, mEvents.getStartTime());
                        i.putExtra(EXTRA_EVENT_END_TIME, mEvents.getEndTime());
                        i.putExtra(EXTRA_EVENT_POSTED_BY, mEvents.getPostedBy());
                        i.putExtra(EXTRA_EVENT_ID, mEvents.getId());
                        mContext.startActivity(i);

                    }else{
                        Intent intent= new Intent(mContext, HospitalViewEventDetails.class);
                        intent.putExtra(EXTRA_EVENT_TITLE, mEvents.getTitle());
                        intent.putExtra(EXTRA_EVENT_LOCATION, mEvents.getLocation());
                        intent.putExtra(EXTRA_EVENT_START_DATE, mEvents.getStartDate());
                        intent.putExtra(EXTRA_EVENT_END_DATE, mEvents.getEndDate());
                        intent.putExtra(EXTRA_EVENT_DESCRIPTION, mEvents.getDescription());
                        intent.putExtra(EXTRA_EVENT_START_TIME, mEvents.getStartTime());
                        intent.putExtra(EXTRA_EVENT_END_TIME, mEvents.getEndTime());
                        intent.putExtra(EXTRA_EVENT_POSTED_BY, mEvents.getPostedBy());
                        intent.putExtra(EXTRA_EVENT_ID, mEvents.getId());
                        mContext.startActivity(intent);
                    }
                }
            });

        }
    }
}
