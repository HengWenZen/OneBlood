package com.example.OneBlood.Labs;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.OneBlood.Models.BloodRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BloodRequestLab {
    private static BloodRequestLab sBloodRequestLab;
    private List<BloodRequest> mBloodRequestList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static BloodRequestLab get(Context context) {
        sBloodRequestLab = new BloodRequestLab(context);
        return sBloodRequestLab;
    }

    public BloodRequestLab(Context context) {
        mBloodRequestList = new ArrayList<>();
        mBloodRequestList.clear();

        db.collection("emergencyRequest")
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
                                    BloodRequest bloodRequest = new BloodRequest();
                                    bloodRequest.setID(document.getId());
                                    bloodRequest.setTitle(document.get("title").toString());
                                    bloodRequest.setDescription(document.get("description").toString());
                                    bloodRequest.setRecipient(document.get("postedBy").toString());
                                    bloodRequest.setBloodType(document.get("blood type").toString());
                                    bloodRequest.setLocation(document.get("location").toString());
                                    bloodRequest.setDate(document.get("date").toString());
                                    bloodRequest.setContact(document.get("contact").toString());
                                    mBloodRequestList.add(bloodRequest);
                                }
                            }
                        }
                    }
                });
    }

    public List<BloodRequest> getBloodRequest(){
        return mBloodRequestList;
    }

    public BloodRequest getBloodRequests(UUID uuid){
        for(BloodRequest bloodRequest: mBloodRequestList){
            if(bloodRequest.getID().equals(uuid)){
                return bloodRequest;
            }
        }
        return null;
    }
}
