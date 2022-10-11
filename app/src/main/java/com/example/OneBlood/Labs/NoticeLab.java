package com.example.OneBlood.Labs;

import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.example.OneBlood.Firebase;
import com.example.OneBlood.Models.Notice;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NoticeLab {

    private static NoticeLab sNoticeLab;
    private List<Notice> mNoticeList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static NoticeLab get(Context context){
        sNoticeLab = new NoticeLab(context);
        return sNoticeLab;
    }

    public NoticeLab(Context context){
        mNoticeList = new ArrayList<>();
        mNoticeList.clear();

        db.collection("notice")
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
                                    Notice notice = new Notice();
                                    notice.setId(document.getId());
                                    notice.setTitle(document.get("title").toString());
                                    notice.setDescription(document.get("description").toString());
                                    notice.setDate(document.get("date").toString());
                                    notice.setHospitalName(document.get("hospitalName").toString());
                                    mNoticeList.add(notice);
                                }
                            }
                        }
                    }
                });
    }

    public List<Notice> getNoticeList(){
        return mNoticeList;
    }

    public Notice getNotice(UUID uuid){
        for(Notice notice:mNoticeList){
            if(notice.getId().equals(uuid)){
                return notice;
            }
        }
        return null;
    }
}
