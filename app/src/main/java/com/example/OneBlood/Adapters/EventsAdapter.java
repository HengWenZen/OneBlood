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
    public static final String EXTRA_EVENT_OPERATION_HOUR = "time";
    public static final String EXTRA_EVENT_START_DATE = "startDate";
    public static final String EXTRA_EVENT_END_DATE = "endDate";
    public static final String EXTRA_EVENT_START_TIME = "startTime";
    public static final String EXTRA_EVENT_END_TIME = "endTime";
    public static SharedPreferences mPreferences;
    private final String SHARED_PREF = "myPreferences";
    private final String KEY_USER = "user";

    public EventsAdapter (List<Events> events, Context context){
        mEvents = events;
        mContext = context;
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
        public Button btnBookTimeSlot;

        public void bindEvents(Events events){
            mEvents = events;
            mEventTitle.setText(mEvents.getTitle());
            mEventLocation.setText(mEvents.getLocation());

        }

        public EventHolder(@NonNull View itemView) {
            super(itemView);
            mEventTitle = itemView.findViewById(R.id.list_item_event_title);
            mEventLocation = itemView.findViewById(R.id.list_item_event_location);
            btnBookTimeSlot = itemView.findViewById(R.id.btnBookTimeSlot);

            btnBookTimeSlot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, UserEventTimeSlot.class);
                    i.putExtra(EXTRA_EVENT_TITLE, mEvents.getTitle());
                    i.putExtra(EXTRA_EVENT_LOCATION, mEvents.getLocation());
                    i.putExtra(EXTRA_EVENT_START_DATE, mEvents.getStartDate());
                    i.putExtra(EXTRA_EVENT_END_DATE, mEvents.getEndDate());
                    i.putExtra(EXTRA_EVENT_DESCRIPTION, mEvents.getDescription());
                    i.putExtra(EXTRA_EVENT_START_TIME, mEvents.getStartTime());
                    i.putExtra(EXTRA_EVENT_END_TIME, mEvents.getEndTime());
                    mContext.startActivity(i);
                }
            });

        }
    }
}
