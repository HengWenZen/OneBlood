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

import com.example.OneBlood.Models.DonateLocation;
import com.example.OneBlood.R;
import com.example.OneBlood.TimeSlotBooking;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationHolder> {
    private List <DonateLocation> mDonateLocations;
    private Context mContext;

    public static final String EXTRA_LOCATION_TITLE = "location_name";
    public static final String EXTRA_LOCATION_ADDRESS = "location_address";
    public static final String EXTRA_LOCATION_CONTACT = "location_contact";
    public static final String EXTRA_LOCATION_OPERATION_HOUR = "location_operation_hours";
    public static SharedPreferences mPreferences;
    private final String SHARED_PREF = "myPreferences";
    private final String KEY_USER = "user";

    public LocationAdapter (List<DonateLocation> locations, Context context){
        mDonateLocations = locations;
        mContext = context;
    }

    @NonNull
    @Override
    public LocationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.list_item_location, parent, false);
        return new LocationAdapter.LocationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationHolder holder, int position) {
        DonateLocation location = mDonateLocations.get(position);
        holder.bindLocation(location);
    }

    @Override
    public int getItemCount() {
        return mDonateLocations.size();
    }


    public class LocationHolder extends RecyclerView.ViewHolder {
        public TextView mTextView, mAddress;
        public Button btnBook;
        private DonateLocation mDonateLocations;

        public void bindLocation(DonateLocation location) {
            mDonateLocations = location;
            mTextView.setText(mDonateLocations.getTitle());
            mAddress.setText(mDonateLocations.getAddress());
        }

        public LocationHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.list_item_location_title);
            mAddress = itemView.findViewById(R.id.list_item_location_address);
            btnBook = itemView.findViewById(R.id.btnBookAppointment);

            btnBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, TimeSlotBooking.class);
                    i.putExtra(EXTRA_LOCATION_TITLE,mDonateLocations.getTitle());
                    i.putExtra(EXTRA_LOCATION_ADDRESS,mDonateLocations.getAddress());
                    i.putExtra(EXTRA_LOCATION_CONTACT,mDonateLocations.getContact());
                    i.putExtra(EXTRA_LOCATION_OPERATION_HOUR,mDonateLocations.getOperationHrs());
                    mContext.startActivity(i);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, TimeSlotBooking.class);
                    i.putExtra(EXTRA_LOCATION_TITLE,mDonateLocations.getTitle());
                    i.putExtra(EXTRA_LOCATION_ADDRESS,mDonateLocations.getAddress());
                    i.putExtra(EXTRA_LOCATION_CONTACT,mDonateLocations.getContact());
                    i.putExtra(EXTRA_LOCATION_OPERATION_HOUR,mDonateLocations.getOperationHrs());
                    mContext.startActivity(i);
                }
            });
        }
    }
}
