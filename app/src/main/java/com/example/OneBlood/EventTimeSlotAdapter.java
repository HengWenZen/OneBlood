package com.example.OneBlood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.OneBlood.Models.EventTimeSlot;

import java.util.ArrayList;
import java.util.List;

public class EventTimeSlotAdapter extends RecyclerView.Adapter<EventTimeSlotAdapter.EventsHolder> {

    private static final String DISABLE_TAG = "disable tag";
    Context mContext;
    List<EventTimeSlot> mEventsTimeSlot;
    List<CardView> mCardViewList;
    String selectedSlot = "";

    public EventTimeSlotAdapter(Context context, List<EventTimeSlot> eventsTimeSlots){
        this.mContext = context;
        mEventsTimeSlot = eventsTimeSlots;
        mCardViewList = new ArrayList<>();
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
        return 12;
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
//                    Toast.makeText(context, slot.getSlot()+"clicked", Toast.LENGTH_SHORT).show();
                    for(CardView cardView:mCardViewList)
                    {
                        if(cardView.getTag() == null){
                            cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.white));
                        }
                    }
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
        switch (position) {
            case 0:
                return "8:00-9:00";
            case 1:
                return "9:00-10:00";
            case 2:
                return "10:00-11:00";
            case 3:
                return "11:00-12:00";
            case 4:
                return "12:00-13:00";
            case 5:
                return "13:00-14:00";
            case 6:
                return "14:00-15:00";
            case 7:
                return "15:00-16:00";
            case 8:
                return "16:00-17:00";
            case 9:
                return "17:00-18:00";
            case 10:
                return "18:00-19:00";
            case 11:
                return "19:00-20:00";
            case 12:
                return "20:00-21:00";
            default:
                return "Closed";
        }
    }
}
