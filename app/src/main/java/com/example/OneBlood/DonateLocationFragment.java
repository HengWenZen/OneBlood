package com.example.OneBlood;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class DonateLocationFragment extends Fragment {
    private RecyclerView rvLocationRecyclerView;
    private LocationAdapter mLocationAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvLocationRecyclerView = view.findViewById(R.id.rvLocation);
        rvLocationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private class LocationHolder extends RecyclerView.ViewHolder{

        public TextView mTextView;
        private DonateLocation mDonateLocation;

        public void bindLocation(DonateLocation location){
            mDonateLocation = location;
            mTextView.setText(mDonateLocation.getTitle());
        }

        public LocationHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = (TextView) itemView;
        }
    }

    private class LocationAdapter extends RecyclerView.Adapter<LocationHolder>{

        public List<DonateLocation> mDonateLocations;

        public LocationAdapter(List<DonateLocation> donateLocations){
            mDonateLocations = donateLocations;
        }
        @NonNull
        @Override
        public LocationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                    .inflate(R.layout.list_item_location, parent, false);
            return new LocationHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull LocationHolder holder, int position) {
            DonateLocation donateLocation = mDonateLocations.get(position);
            holder.bindLocation(donateLocation);
        }

        @Override
        public int getItemCount() {
            return mDonateLocations.size();
        }
    }


}
