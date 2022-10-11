package com.example.OneBlood.Labs;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.OneBlood.Models.DonateLocation;
import com.example.OneBlood.Models.EventTimeSlot;
import com.example.OneBlood.Models.Events;
import com.example.OneBlood.Models.TimeSlot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EventTimeSlotLab {
    private static EventTimeSlotLab sEventTimeSlotLab;
    private List<EventTimeSlot> mEventTimeSlots;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static EventTimeSlotLab get(Context context){
        sEventTimeSlotLab = new EventTimeSlotLab(context);
        return sEventTimeSlotLab;
    }

    public EventTimeSlotLab(Context context) {
        mEventTimeSlots = new ArrayList<>();
        mEventTimeSlots.clear();

        db.collection("userEventBooking")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot result = task.getResult();
                            if (!result.isEmpty()) {
                                Log.d("Data Retrieved", result.toString());
                                for (QueryDocumentSnapshot document : result) {
                                    Events events = new Events();
                                    events.setId(document.getId());
                                    events.setTitle(document.get("title").toString());
                                    events.setEndDate(document.get("endDate").toString());
                                    events.setStartDate(document.get("startDate").toString());
                                    events.setOperationHrs(document.get("time").toString());
                                    events.setImageURL(document.get("imageUri").toString());
                                    events.setDescription(document.get("description").toString());
                                    events.setLocation(document.get("location").toString());
                                }
                            }
                        }
                    }
                });

    }
}
