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

import com.example.OneBlood.Activity.HospitalViewBooking;
import com.example.OneBlood.Activity.HospitalViewEventBooking;
import com.example.OneBlood.Activity.HospitalViewEventBookingDetails;
import com.example.OneBlood.Activity.UserViewBookingDetails;
import com.example.OneBlood.Activity.UserViewBooking;
import com.example.OneBlood.Activity.UserViewEventBooking;
import com.example.OneBlood.Activity.UserViewEventBookingDetails;
import com.example.OneBlood.HospitalViewBookingDetails;
import com.example.OneBlood.Models.BookingEvent;
import com.example.OneBlood.R;

import java.util.List;

public class ViewEventBookingAdapter extends RecyclerView.Adapter<ViewEventBookingAdapter.ViewEventBookingHolder> {
    private Context mContext;
    private List<BookingEvent> mBookingList;
    boolean isHospital = false;
    String bookingHospital, bookingSlot, bookingUser, bookingDate, bookingId;


    public static final String EXTRA_EVENT_BOOKING_DATE = "eventBookingDate";
    public static final String EXTRA_EVENT_BOOKING_TIME = "eventBookingTime";
    public static final String EXTRA_EVENT_BOOKING_HOSPITAL = "eventBookingLocation";
    public static final String EXTRA_USER_NAME = "userName";
    public static final String EXTRA_EVENT_BOOKING_ID = "eventBookingId";

    public ViewEventBookingAdapter(Context context, List<BookingEvent> viewAppointments, boolean isHospital) {
        this.mContext = context;
        this.mBookingList = viewAppointments;
        this.isHospital = isHospital;
    }

    @NonNull
    @Override
    public ViewEventBookingAdapter.ViewEventBookingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.layout_view_event_booking, parent, false);
        return new ViewEventBookingAdapter.ViewEventBookingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewEventBookingAdapter.ViewEventBookingHolder holder, int position) {
        BookingEvent booking = mBookingList.get(position);
        holder.locationName.setText(booking.getLocationName());
        holder.appointmentDate.setText(booking.getDate());
        holder.appointmentTime.setText(booking.getSlot());
        bookingHospital = booking.getLocationName();
        bookingSlot = booking.getSlot();
        bookingDate = booking.getDate();
        bookingUser = booking.getUser();
        bookingId = booking.getId();


        if(isHospital == true){
            holder.locationName.setVisibility(View.INVISIBLE);
            holder.appointmentUser.setVisibility(View.VISIBLE);
            holder.appointmentUser.setText(bookingUser);

        }else{
            holder.locationName.setVisibility(View.VISIBLE);
            holder.appointmentUser.setVisibility(View.INVISIBLE);
        }
        holder.bindBooking(booking);
    }

    @Override
    public int getItemCount() {
        return mBookingList.size();
    }

    public class ViewEventBookingHolder extends RecyclerView.ViewHolder {

        TextView locationName, appointmentDate, appointmentTime, appointmentUser;
        Button btnViewEventAppointment;
        BookingEvent mBooking;

        public ViewEventBookingHolder(@NonNull View itemView) {
            super(itemView);
            locationName = itemView.findViewById(R.id.tvBookingEventLocationName);
            appointmentDate = itemView.findViewById(R.id.tvBookingEventDate);
            appointmentTime = itemView.findViewById(R.id.tvBookingEventTime);
            appointmentUser = itemView.findViewById(R.id.tvBookingEventUser);
            btnViewEventAppointment = itemView.findViewById(R.id.btn_view_event_booking);

            btnViewEventAppointment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(isHospital == true) {

                        //((HospitalViewEventBooking) mContext).adapterChange(getAdapterPosition());
                        Intent y = new Intent(mContext, HospitalViewEventBookingDetails.class);
                        y.putExtra(EXTRA_USER_NAME, bookingUser);
                        y.putExtra(EXTRA_EVENT_BOOKING_DATE, bookingDate);
                        y.putExtra(EXTRA_EVENT_BOOKING_TIME, bookingSlot);
                        y.putExtra(EXTRA_EVENT_BOOKING_ID, bookingId);
                        y.putExtra(EXTRA_EVENT_BOOKING_HOSPITAL, bookingHospital);
                        mContext.startActivity(y);

                    }
                    else{
                        ((UserViewEventBooking) mContext).adapterChange(getAdapterPosition());
                        Intent i = new Intent(mContext, UserViewEventBookingDetails.class);
                        i.putExtra(EXTRA_EVENT_BOOKING_HOSPITAL, bookingHospital);
                        i.putExtra(EXTRA_EVENT_BOOKING_DATE, bookingDate);
                        i.putExtra(EXTRA_EVENT_BOOKING_TIME, bookingSlot);
                        i.putExtra(EXTRA_USER_NAME, bookingUser);
                        i.putExtra(EXTRA_EVENT_BOOKING_ID, bookingId);
                        mContext.startActivity(i);
                    }
                }
            });
        }


        public void bindBooking(BookingEvent b) {
            mBooking = b;
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



