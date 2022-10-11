package com.example.OneBlood.Labs;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.OneBlood.Models.DonateLocation;
import com.example.OneBlood.Models.TimeSlot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TimeSlotLab {
    private static TimeSlotLab sTimeSlotLab;
    private List<TimeSlot> mTimeSlotList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static TimeSlotLab get(Context context){
        sTimeSlotLab = new TimeSlotLab(context);
        return sTimeSlotLab;
    }

    public TimeSlotLab(Context context) {
        mTimeSlotList = new ArrayList<>();
        mTimeSlotList.clear();

        db.collection("userBooking")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot result = task.getResult();
                            if (!result.isEmpty()) {
                                Log.d("Data Retrieved", result.toString());
                                for (QueryDocumentSnapshot document : result) {
                                    DonateLocation donateLocation = new DonateLocation();
                                    donateLocation.setID(document.getId());
                                    donateLocation.setTitle(document.get("name").toString());
                                    donateLocation.setLatitude(document.get("latitude").toString());
                                    donateLocation.setAddress(document.get("address").toString());
                                    donateLocation.setLongitude(document.get("longitude").toString());
                                    donateLocation.setImageUrl(document.get("imageURL").toString());

                                }
                            }
                        }
                    }
                });

    }
}
