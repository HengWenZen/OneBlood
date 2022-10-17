package com.example.OneBlood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.OneBlood.Models.EventTimeSlot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventTimeSlotAdapter extends RecyclerView.Adapter<EventTimeSlotAdapter.EventsHolder> {

    private static final String DISABLE_TAG = "disable tag";
    Context mContext;
    List<EventTimeSlot> mEventsTimeSlot;
    List<CardView> mCardViewList;
    String selectedSlot = "";
    String toTime, startTime;
    Date mStartTime, mEndTime;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public EventTimeSlotAdapter(Context context, List<EventTimeSlot> eventsTimeSlots, Date startTime, Date endTime){
        this.mContext = context;
        mEventsTimeSlot = eventsTimeSlots;
        mCardViewList = new ArrayList<>();
        mStartTime = startTime;
        mEndTime = endTime;
    }


    @NonNull
    @Override
    public EventsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.list_time_slot, parent,false);
        return new EventsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsHolder holder, int position) {
            holder.event_time_slot.setText(new StringBuilder(timeSlot(position)));
            if(mEventsTimeSlot.size()==0){
                holder.event_time_slot_status.setText("Available");
                holder.event_time_slot_status.setTextColor(mContext.getResources().getColor(android.R.color.black));
                holder.event_time_slot.setTextColor(mContext.getResources().getColor(android.R.color.black));
                holder.event_card_time_slot.setCardBackgroundColor(mContext.getResources().getColor(android.R.color.white));
            } else{
                for(EventTimeSlot slotValue:mEventsTimeSlot){
                    int slot = Integer.parseInt(slotValue.getSlot());
                    if(slot == position){
                        holder.event_card_time_slot.setTag(DISABLE_TAG);
                        holder.event_card_time_slot.setEnabled(false);
                        holder.event_time_slot_status.setText("Full");
                        holder.event_time_slot_status.setTextColor(mContext.getResources().getColor(android.R.color.black));
                        holder.event_time_slot.setTextColor(mContext.getResources().getColor(android.R.color.black));
                        holder.event_card_time_slot.setCardBackgroundColor(mContext.getResources().getColor(R.color.gray));
                    }
                }
            }

        holder.bindSlot(position);
        if(!mCardViewList.contains(holder.event_card_time_slot)){
            mCardViewList.add(holder.event_card_time_slot);
        }
    }

    @Override
    public int getItemCount() {
        //Get Number of Slot Needed based on the event duration
        long diff = mEndTime.getTime() - mStartTime.getTime();
        long diffHours = diff / (60 * 60 * 1000) % 24;
        int timeDiff = (int) diffHours;
        return timeDiff;
    }

    public class EventsHolder extends RecyclerView.ViewHolder{
        TextView event_time_slot, event_time_slot_status;
        CardView event_card_time_slot;
        int slot;

        public EventsHolder(@NonNull View itemView) {
            super(itemView);
            event_time_slot = itemView.findViewById(R.id.event_time_slot);
            event_time_slot_status = itemView.findViewById(R.id.event_time_slot_status);
            event_card_time_slot = itemView.findViewById(R.id.event_card_time_slot);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    for(CardView cardView:mCardViewList)
                    {
                        //Set white background color when the item is not clicked
                        if(cardView.getTag() == null){
                            cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.white));
                        }
                    }
                    //Set red background color when the item is clicked
                    event_card_time_slot.setCardBackgroundColor(mContext.getResources().getColor(R.color.red));
                    selectedSlot = String.valueOf(slot);
                }
            });
        }
        public void bindSlot(int position) {
            slot = position;
        }

    }

    public String timeSlot(int position) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();

        //Set each slot has one hour duration
        switch (position) {
            case 0:
                startTime = sdf.format(mStartTime);
                calendar.setTime(mStartTime);
                calendar.add(Calendar.HOUR, 1);
                toTime = sdf.format(calendar.getTime());
                return (startTime + "-" + toTime);

            case 1:
                calendar.setTime(mStartTime);
                calendar.add(Calendar.HOUR, 2);
                startTime = sdf.format(calendar.getTime());
                calendar.add(Calendar.HOUR, 1);
                toTime = sdf.format(calendar.getTime());
                return (startTime + "-" + toTime);

            case 2:
                calendar.setTime(mStartTime);
                calendar.add(Calendar.HOUR, 3);
                startTime = sdf.format(calendar.getTime());
                calendar.add(Calendar.HOUR, 1);
                toTime = sdf.format(calendar.getTime());
                return (startTime + "-" + toTime);

            case 3:
                calendar.setTime(mStartTime);
                calendar.add(Calendar.HOUR, 4);
                startTime = sdf.format(calendar.getTime());
                calendar.add(Calendar.HOUR, 1);
                toTime = sdf.format(calendar.getTime());
                return (startTime + "-" + toTime);

            case 4:
                calendar.setTime(mStartTime);
                calendar.add(Calendar.HOUR, 5);
                startTime = sdf.format(calendar.getTime());
                calendar.add(Calendar.HOUR, 1);
                toTime = sdf.format(calendar.getTime());
                return (startTime + "-" + toTime);

            case 5:
                calendar.setTime(mStartTime);
                calendar.add(Calendar.HOUR, 6);
                startTime = sdf.format(calendar.getTime());
                calendar.add(Calendar.HOUR, 1);
                toTime = sdf.format(calendar.getTime());
                return (startTime + "-" + toTime);

            case 6:
                calendar.setTime(mStartTime);
                calendar.add(Calendar.HOUR, 7);
                startTime = sdf.format(calendar.getTime());
                calendar.add(Calendar.HOUR, 1);
                toTime = sdf.format(calendar.getTime());
                return (startTime + "-" + toTime);

            case 7:
                calendar.setTime(mStartTime);
                calendar.add(Calendar.HOUR, 8);
                startTime = sdf.format(calendar.getTime());
                calendar.add(Calendar.HOUR, 1);
                toTime = sdf.format(calendar.getTime());
                return (startTime + "-" + toTime);

            case 8:
                calendar.setTime(mStartTime);
                calendar.add(Calendar.HOUR, 9);
                startTime = sdf.format(calendar.getTime());
                calendar.add(Calendar.HOUR, 1);
                toTime = sdf.format(calendar.getTime());
                return (startTime + "-" + toTime);

            case 9:
                calendar.setTime(mStartTime);
                calendar.add(Calendar.HOUR, 10);
                startTime = sdf.format(calendar.getTime());
                calendar.add(Calendar.HOUR, 1);
                toTime = sdf.format(calendar.getTime());
                return (startTime + "-" + toTime);

            case 10:
                calendar.setTime(mStartTime);
                calendar.add(Calendar.HOUR, 11);
                startTime = sdf.format(calendar.getTime());
                calendar.add(Calendar.HOUR, 1);
                toTime = sdf.format(calendar.getTime());
                return (startTime + "-" + toTime);
            case 11:
                calendar.setTime(mStartTime);
                calendar.add(Calendar.HOUR, 12);
                startTime = sdf.format(calendar.getTime());
                calendar.add(Calendar.HOUR, 1);
                toTime = sdf.format(calendar.getTime());
                return (startTime + "-" + toTime);

            case 12:
                calendar.setTime(mStartTime);
                calendar.add(Calendar.HOUR, 13);
                startTime = sdf.format(calendar.getTime());
                calendar.add(Calendar.HOUR, 1);
                toTime = sdf.format(calendar.getTime());
                return (startTime + "-" + toTime);

            default:
                return "Closed";
        }
    }
}
