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
import com.example.OneBlood.UserViewBooking;
import com.example.OneBlood.Activity.HospitalViewBookingDetails;
import com.example.OneBlood.UserViewBookingDetails;
import com.example.OneBlood.Models.Booking;
import com.example.OneBlood.R;

import java.util.List;

public class ViewBookingAdapter extends RecyclerView.Adapter<ViewBookingAdapter.ViewBookingHolder> {
    private Context mContext;
    private List<Booking> mBookingList;
    boolean isHospital = false;
    String bookingHospital, bookingSlot, bookingUser, bookingDate, bookingId;


    public static final String EXTRA_BOOKING_DATE = "noticeDate";
    public static final String EXTRA_BOOKING_TIME = "noticeDescription";
    public static final String EXTRA_BOOKING_HOSPITAL = "hospitalName";
    public static final String EXTRA_USER_NAME = "userName";
    public static final String EXTRA_BOOKING_ID = "bookingId";

    public ViewBookingAdapter(Context context, List<Booking> viewAppointments, boolean isHospital) {
        this.mContext = context;
        this.mBookingList = viewAppointments;
        this.isHospital = isHospital;
    }

    @NonNull
    @Override
    public ViewBookingAdapter.ViewBookingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.layout_view_booking, parent, false);
        return new ViewBookingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewBookingAdapter.ViewBookingHolder holder, int position) {
        Booking booking = mBookingList.get(position);
        holder.locationName.setText(booking.getLocationName());
        holder.appointmentDate.setText(booking.getDate());
        holder.appointmentTime.setText(timeSlot(Integer.valueOf(booking.getSlot())));
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

    public class ViewBookingHolder extends RecyclerView.ViewHolder {

        TextView locationName, appointmentDate, appointmentTime, appointmentUser;
        Button btnViewAppointment;
        Booking mBooking;

        public ViewBookingHolder(@NonNull View itemView) {
            super(itemView);
            locationName = itemView.findViewById(R.id.tvBookingLocationName);
            appointmentDate = itemView.findViewById(R.id.tvBookingDate);
            appointmentTime = itemView.findViewById(R.id.tvBookingTime);
            appointmentUser = itemView.findViewById(R.id.tvBookingUser);
            btnViewAppointment = itemView.findViewById(R.id.btn_view_booking);

            btnViewAppointment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(isHospital == true) {

                        ((HospitalViewBooking) mContext).adapterChange(getAdapterPosition());
                        Intent y = new Intent(mContext, HospitalViewBookingDetails.class);
                        y.putExtra(EXTRA_USER_NAME, bookingUser);
                        y.putExtra(EXTRA_BOOKING_DATE, bookingDate);
                        y.putExtra(EXTRA_BOOKING_TIME, bookingSlot);
                        y.putExtra(EXTRA_BOOKING_ID, bookingId);
                        y.putExtra(EXTRA_BOOKING_HOSPITAL, bookingHospital);
                        mContext.startActivity(y);

                    }
                    else{
                        ((UserViewBooking) mContext).adapterChange(getAdapterPosition());
                        Intent i = new Intent(mContext, UserViewBookingDetails.class);
                        i.putExtra(EXTRA_BOOKING_HOSPITAL, bookingHospital);
                        i.putExtra(EXTRA_BOOKING_DATE, bookingDate);
                        i.putExtra(EXTRA_BOOKING_TIME, bookingSlot);
                        i.putExtra(EXTRA_USER_NAME, bookingUser);
                        i.putExtra(EXTRA_BOOKING_ID, bookingId);
                        mContext.startActivity(i);
                    }
                }
            });
        }


        public void bindBooking(Booking b) {
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



