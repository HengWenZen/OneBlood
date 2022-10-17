package com.example.OneBlood.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.OneBlood.Activity.ViewBooking;
import com.example.OneBlood.Models.Booking;
import com.example.OneBlood.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ViewBookingAdapter extends RecyclerView.Adapter<ViewBookingAdapter.ViewBookingHolder> {
    private Context mContext;
    private List<Booking> mBookingList;
    boolean isHospital = false;

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

        if(isHospital == true){
           holder.locationName.setVisibility(View.INVISIBLE);
           holder.appointmentUser.setVisibility(View.VISIBLE);
           holder.appointmentUser.setText(booking.getUser());
           holder.btnCancelAppointment.setVisibility(View.INVISIBLE);
        }else{
            holder.btnCancelAppointment.setVisibility(View.VISIBLE);
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
        Button btnCancelAppointment;
        Booking mBooking;

        public ViewBookingHolder(@NonNull View itemView) {
            super(itemView);
            locationName = itemView.findViewById(R.id.tvBookingLocationName);
            appointmentDate = itemView.findViewById(R.id.tvBookingDate);
            appointmentTime = itemView.findViewById(R.id.tvBookingTime);
            appointmentUser = itemView.findViewById(R.id.tvBookingUser);
            btnCancelAppointment = itemView.findViewById(R.id.btn_Cancel_Booking);

            btnCancelAppointment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(mContext)
                            .setMessage("Confirm Appointment Cancellation?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                                    db.collection("userBooking").document(mBooking.getId()).delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(mContext, "Appointment Cancelled Successfully!", Toast.LENGTH_SHORT).show();
                                                    ((ViewBooking) mContext).adapterChange(getAdapterPosition());
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(mContext, "Cancel Fail Due to Unexpected reason", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            }).create().show();
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


