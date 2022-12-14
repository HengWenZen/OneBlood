package com.example.OneBlood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.OneBlood.Models.TimeSlot;

import java.util.ArrayList;
import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.BookingHolder> {
    private static final String DISABLE_TAG = "disable_tag";

    Context context;
    List<TimeSlot> mTimeSlotList;
    List<CardView> mCardViewList;
    String selectedSlot = ""; //

    public TimeSlotAdapter(Context context, List<TimeSlot> timeSlotList) {
        this.context = context;
        mTimeSlotList = timeSlotList;
        mCardViewList = new ArrayList<>();
    }

    @NonNull
    @Override
    public BookingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_time_slot,parent,false);
        return new BookingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingHolder holder, int position) {
        holder.txt_time_slot.setText(new StringBuilder(timeSlot(position)));

        // Show List if All slot is available
        if(mTimeSlotList.size() == 0)
        {
            holder.txt_time_slot_status.setText("Available");
            holder.txt_time_slot_status.setTextColor(context.getResources().getColor(android.R.color.black));
            holder.txt_time_slot.setTextColor(context.getResources().getColor(android.R.color.black));
            holder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));

        } else {
            //If there is booked slot
            for(TimeSlot slotValue:mTimeSlotList){
                //Loop time slot from server
                int slot = Integer.parseInt(slotValue.getSlot().toString());
                if(slot == position)
                {
                    holder.card_time_slot.setTag(DISABLE_TAG);
                    holder.card_time_slot.setEnabled(false);
                    holder.txt_time_slot_status.setText("Full");
                    holder.txt_time_slot_status.setTextColor(context.getResources().getColor(android.R.color.black));
                    holder.txt_time_slot.setTextColor(context.getResources().getColor(android.R.color.black));
                    holder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(R.color.darkerGray));
                }
            }
        }

        holder.bindSlot(position);
        if(!mCardViewList.contains(holder.card_time_slot)) {
            mCardViewList.add(holder.card_time_slot);
        }
    }

    @Override
    public int getItemCount() {
     return 9;
    }

    public class BookingHolder extends RecyclerView.ViewHolder {
        TextView txt_time_slot,txt_time_slot_status;
        CardView card_time_slot;
        int slot;

        public BookingHolder(@NonNull View itemView) {
            super(itemView);
            card_time_slot = (CardView) itemView.findViewById(R.id.card_time_slot);
            txt_time_slot = (TextView) itemView.findViewById(R.id.txt_time_slot);
            txt_time_slot_status = (TextView) itemView.findViewById(R.id.txt_time_slot_status);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    for(CardView cardView:mCardViewList)
                    {
                        if(cardView.getTag() == null){
                            cardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));
                        }
                    }
                    card_time_slot.setCardBackgroundColor(context.getResources().getColor(R.color.red));
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
                return "9:00-10:00";
            case 1:
                return "10:00-11:00";
            case 2:
                return "11:00-12:00";
            case 3:
                return "12:00-13:00";
            case 4:
                return "13:00-14:00";
            case 5:
                return "14:00-15:00";
            case 6:
                return "15:00-16:00";
            case 7:
                return "16:00-17:00";
            case 8:
                return "17:00-18:00";
            default:
                return "Closed";
        }
    }

}
