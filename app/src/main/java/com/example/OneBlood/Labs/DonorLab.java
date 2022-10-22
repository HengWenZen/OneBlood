package com.example.OneBlood.Labs;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.OneBlood.Models.Donor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DonorLab {

    private static DonorLab sDonorLab;
    private List<Donor> mDonorList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static DonorLab get(Context context){
        sDonorLab = new DonorLab(context);
        return sDonorLab;
    }

    private DonorLab(Context context){
        mDonorList = new ArrayList<>();
        mDonorList.clear();

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        QuerySnapshot result = task.getResult();

                        if(!result.isEmpty()){
                            for(QueryDocumentSnapshot documentSnapshot : result){
                                Donor donor = new Donor(documentSnapshot.getId(),
                                documentSnapshot.get("FullName").toString(),
                                documentSnapshot.get("phone number").toString(),
                                documentSnapshot.get("Email").toString(),
                                documentSnapshot.get("blood type").toString(),
                                documentSnapshot.get("status").toString());
                                mDonorList.add(donor);
                            }
                        }
                    }

            }
        });
    }

    public List<Donor> getDonorList(){
        return  mDonorList;
    }

    public Donor getDonorID(UUID uuid){
        for (Donor donor : mDonorList){
            if(donor.getId().equals(uuid)){
                return donor;
            }
        }
        return null;
    }



}
