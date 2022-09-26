package com.example.OneBlood;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LocationLab {
    private static LocationLab sLocationLab;
    private List<DonateLocation> mDonateLocations;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static LocationLab get(Context context) {
        sLocationLab = new LocationLab(context);
        return sLocationLab;
    }

    private LocationLab(Context context) {
        mDonateLocations = new ArrayList<>();
        mDonateLocations.clear();

        db.collection("location")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot result = task.getResult();
                            if (!result.isEmpty()) {
                                Log.d("Data Retrieved", result.toString());
                                for (QueryDocumentSnapshot document : result) {
                                    Log.d("Document ID:", document.getId() + " => " + document.getData());
                                    DonateLocation donateLocation = new DonateLocation();
                                    donateLocation.setID(document.getId());
                                    donateLocation.setTitle(document.get("name").toString());
                                    donateLocation.setLatitude(document.get("latitude").toString());
                                    donateLocation.setAddress(document.get("address").toString());
                                    donateLocation.setLongitude(document.get("longitude").toString());
                                    donateLocation.setImageUrl(document.get("imageURL").toString());
                                    donateLocation.setOperationHrs(document.get("operation hours").toString());
                                    donateLocation.setContact(document.get("contact").toString());
                                    mDonateLocations.add(donateLocation);
                                }
                            }
                        }
                    }
                });
    }

    public List<DonateLocation> getLocation(){
        return mDonateLocations;
    }

    public DonateLocation getLocations(UUID uuid){
        for(DonateLocation location : mDonateLocations){
            if(location.getID().equals(uuid)){
                return location;
            }
        }
        return null;
    }
}