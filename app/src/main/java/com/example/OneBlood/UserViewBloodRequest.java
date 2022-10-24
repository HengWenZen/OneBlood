package com.example.OneBlood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.OneBlood.Adapters.ViewBloodRequestAdapter;
import com.example.OneBlood.Models.ViewBloodRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserViewBloodRequest extends AppCompatActivity {

    public static SharedPreferences mPreferences;
    private final String SHARED_PREF = "myPreferences";
    private final String KEY_USER = "user";
    private final String KEY_USER_NAME = "userName";


    RecyclerView rv;
    List<ViewBloodRequest> mViewBloodRequests;
    ViewBloodRequestAdapter mViewBloodRequestAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String postedBy ,user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_blood_request);

        rv = findViewById(R.id.rvUserRequest);

        SharedPreferences prefs = getSharedPreferences("myPreferences", MODE_PRIVATE);
        user = prefs.getString(KEY_USER_NAME, null);

        loadRequestList();

    }

    private void loadRequestList() {
        mViewBloodRequests = new ArrayList<>();

        ProgressDialog dialog = ProgressDialog.show(UserViewBloodRequest.this, "",
                "Loading. Please wait...", true);   //show loading dialog


        db.collection("emergencyRequest")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot result = task.getResult();
                            if (!result.isEmpty()) {
                                for (QueryDocumentSnapshot document : result) {
                                    postedBy = document.get("postedBy").toString();

                                    if(postedBy.equals(user)) {
                                        ViewBloodRequest viewBloodRequest = new ViewBloodRequest(document.getId(),
                                                document.get("title").toString(),
                                                document.get("blood type").toString(),
                                                document.get("location").toString(),
                                                document.get("description").toString(),
                                                document.get("postedBy").toString(),
                                                document.get("contact").toString(),
                                                document.get("date").toString());
                                        mViewBloodRequests.add(viewBloodRequest);
                                    }
                                }
                            }
                        }
                    }
                });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();   //remove loading Dialog
                if (mViewBloodRequests.size() == 0) {
                    alertDataEmpty();
                } else {
                    rv.setLayoutManager(new LinearLayoutManager(UserViewBloodRequest.this));
                    mViewBloodRequestAdapter = new ViewBloodRequestAdapter(mViewBloodRequests, UserViewBloodRequest.this, false);
                    rv.setAdapter(mViewBloodRequestAdapter);
                }
            }
        }, 1000);
    }

    public void adapterChange(int position){
        mViewBloodRequests.remove(position);
        mViewBloodRequestAdapter.notifyItemRemoved(position);
        if(mViewBloodRequests.size() == 0) {
            finish();
        }
    }

    private void alertDataEmpty() {
        new AlertDialog.Builder(this)
                .setMessage("No Existing Request Made..")
                .setPositiveButton("Return", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent y = new Intent(UserViewBloodRequest.this, UserBloodRequestMainMenu.class);
                        startActivity(y);
                        finish();
                    }
                }).create().show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UserViewBloodRequest.this, UserBloodRequestMainMenu.class);
        startActivity(intent);
        finish();
    }
}