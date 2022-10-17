package com.example.OneBlood.Labs;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.OneBlood.Models.BloodRequest;
import com.example.OneBlood.Models.Events;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EventLab {

    private static EventLab sEventLab;
    private List<Events> mEventsList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static EventLab get(Context context){
        sEventLab = new EventLab(context);
        return sEventLab;
    }

    public EventLab(Context context){
        mEventsList = new ArrayList<>();
        mEventsList.clear();

        db.collection("events")
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
                                    Events events = new Events();
                                    events.setId(document.getId());
                                    events.setTitle(document.get("title").toString());
                                    events.setDescription(document.get("description").toString());
                                    events.setStartTime(document.get("startTime").toString());
                                    events.setEndTime(document.get("endTime").toString());
                                    events.setImageURL(document.get("imageUri").toString());
                                    events.setLocation(document.get("location").toString());
                                    events.setStartDate(document.get("startDate").toString());
                                    events.setEndDate(document.get("endDate").toString());
                                    mEventsList.add(events);
                                }
                            }
                        }
                    }
                });
    }

    public List<Events> getEventsList(){
        return mEventsList;
    }

    public Events getEvent(UUID uuid){
        for(Events events : mEventsList){
            if(events.getId().equals(uuid)){
                return events;
            }
        }
        return null;
    }
}
