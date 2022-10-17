package com.example.OneBlood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.OneBlood.Models.EventTimeSlot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class UserEventTimeSlot extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String EXTRA_EVENT_TITLE = "title";
    public static final String EXTRA_EVENT_LOCATION = "location";
    public static final String EXTRA_EVENT_DESCRIPTION = "description";
    public static final String EXTRA_EVENT_OPERATION_HOUR = "time";
    public static final String EXTRA_EVENT_START_DATE = "startDate";
    public static final String EXTRA_EVENT_END_DATE = "endDate";
    public static final String EXTRA_EVENT_START_TIME = "startTime";
    public static final String EXTRA_EVENT_END_TIME = "endTime";
    public static SharedPreferences mPreferences;
    private final String SHARED_PREF = "myPreferences";
    private final String KEY_USER = "user";

    List<EventTimeSlot> mEventTimeSlots;
    private EventTimeSlotAdapter mEventTimeSlotAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextInputLayout etEventTitle, etEventDescription, etEventLocation, etEventStartDate, etEventEndDate, etEventOperationHrs;
    ImageView ivBackToList, ivChooseDate, ivViewEventPic;
    TextView tvShowEventDate;
    Button btnBookEventTimeSlot;
    RecyclerView rv;
    Date mStartDate, mEndDate, mStartTime, mEndTime;
    String dateSelected, eventTitle, eventLocation, eventStartDate, eventEndDate, eventStartTime, eventEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_event_time_slot);

        etEventDescription = findViewById(R.id.etEventDescription);
        etEventTitle = findViewById(R.id.etEventTitle);
        etEventLocation = findViewById(R.id.etEventLocation);
        etEventStartDate = findViewById(R.id.etEventStartDate);
        etEventEndDate = findViewById(R.id.etEventEndDate);
        tvShowEventDate = findViewById(R.id.tvShowEventDate);
        btnBookEventTimeSlot = findViewById(R.id.btnBookEventTimeSlot);
        ivBackToList = findViewById(R.id.ivBackToEventList);
        ivViewEventPic = findViewById(R.id.ivViewEventPic);
        rv = findViewById(R.id.rvEventTimeSlot);

        eventEndDate = getIntent().getStringExtra(EXTRA_EVENT_END_DATE);
        eventStartDate = getIntent().getStringExtra(EXTRA_EVENT_START_DATE);
        eventTitle = getIntent().getStringExtra(EXTRA_EVENT_TITLE);
        eventStartTime = getIntent().getStringExtra(EXTRA_EVENT_START_TIME);
        eventEndTime = getIntent().getStringExtra(EXTRA_EVENT_END_TIME);
        tvShowEventDate.setText("Select Date");


        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        try {
            String eventStartDate = getIntent().getStringExtra(EXTRA_EVENT_START_DATE);
            mStartDate = df.parse(eventStartDate);
            String date = df.format(mStartDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat time = new SimpleDateFormat("hh:mm aa");
        try {
            eventStartTime = getIntent().getStringExtra(EXTRA_EVENT_START_TIME);
            eventEndTime = getIntent().getStringExtra(EXTRA_EVENT_END_TIME);
            mStartTime = time.parse(eventStartTime);
            mEndTime = time.parse(eventEndTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tvShowEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        btnBookEventTimeSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dateSelected == null) {
                    Toast.makeText(UserEventTimeSlot.this, "Please select date...", Toast.LENGTH_SHORT).show();
                } else {
                    String slot = String.valueOf(mEventTimeSlotAdapter.selectedSlot);
                    if (slot == "") {
                        Toast.makeText(UserEventTimeSlot.this, "Please select available slots...", Toast.LENGTH_SHORT).show();
                    } else {
                        String message = "Confirm Booking at  " + eventLocation + " on " + dateSelected + " " + mEventTimeSlotAdapter.timeSlot(Integer.valueOf(slot));
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserEventTimeSlot.this);
                        alertDialog.setTitle("Confirm Booking ");
                        alertDialog.setMessage(message);
                        alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences prefs = getSharedPreferences("myPreferences", MODE_PRIVATE);
                                String user = prefs.getString("userName", null);

                                Map<String, Object> data = new HashMap<>();
                                data.put("slot", slot);
                                data.put("user", user);
                                data.put("date", dateSelected);
                                data.put("location", eventLocation);

                                db.collection("userEventBooking").add(data)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(UserEventTimeSlot.this, "Slot booked successfully !", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(UserEventTimeSlot.this, "Error, Please Try Again..", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alertDialog.show();
                    }
                }
            }
        });
    }

    private void showDatePickerDialog(){

        try {
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            mStartDate = df.parse(eventStartDate);
            mEndDate = df.parse(eventEndDate);

            Calendar cal = Calendar.getInstance();
            Date currentDate = new Date();
            long startDate = mStartDate.getTime() - currentDate.getTime();
            long diff = mEndDate.getTime() - mStartDate.getTime();
            long initialDay = TimeUnit.MILLISECONDS.toDays(startDate);
            long days = TimeUnit.MILLISECONDS.toDays(diff);
            int numberOfDays = (int) days;
            int firstDay = (int) initialDay + 1;

            int year = cal.getInstance().get(Calendar.YEAR);
            int month = cal.getInstance().get(Calendar.MONTH);
            int day = cal.getInstance().get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(UserEventTimeSlot.this ,
                    this, year, month, day);

            cal.add(Calendar.DAY_OF_MONTH, firstDay);
            Date eventDate = cal.getTime();
            cal.add(Calendar.DAY_OF_MONTH, numberOfDays);
            Date newDate = cal.getTime();
            datePickerDialog.getDatePicker().setMinDate(eventDate.getTime());
            datePickerDialog.getDatePicker().setMaxDate(newDate.getTime());//set only can choose next 7 days
            datePickerDialog.show();

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Date setDate = new GregorianCalendar(year, month, dayOfMonth).getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String date = formatter.format(setDate);

        availableSlot(date);
        tvShowEventDate.setText(year + "\n " + dayOfMonth + " " + new DateFormatSymbols().getMonths()[month]);

        if(dayOfMonth<10) {
            dateSelected = ("0" + dayOfMonth + "-" + (month + 1) + "-" + year);

        }else if(month+1 <10){

            dateSelected = (dayOfMonth + "-0" + (month + 1) + "-" + year);
        }
        else if(dayOfMonth < 10 && month < 10){
            dateSelected = ("0" + dayOfMonth + "-0" + (month + 1) + "-" + year);
        }
        else{
            dateSelected = dayOfMonth + "-" + (month + 1) + "-" + year;
        }
    }

    private void availableSlot(String date) {
        mEventTimeSlots = new ArrayList<>();
        eventLocation = getIntent().getStringExtra(EXTRA_EVENT_LOCATION);
        ProgressDialog dialog = ProgressDialog.show(UserEventTimeSlot.this, "",
                "Loading......", true);
        db.collection("userEventBooking")
                .whereEqualTo("date", date)
                .whereEqualTo("location", eventLocation)
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
                                    EventTimeSlot slot = new EventTimeSlot();
                                    slot.setSlot((document.get("slot").toString()));
                                    mEventTimeSlots.add(slot);
                                }
                            }
                        }else{
                            Log.d( "Cached get failed: ", String.valueOf(task.getException()));
                        }
                        dialog.dismiss();
                        rv.setHasFixedSize(true);
                        mEventTimeSlotAdapter = new EventTimeSlotAdapter(UserEventTimeSlot.this, mEventTimeSlots, mStartTime, mEndTime);
                        rv.setAdapter(mEventTimeSlotAdapter);
                        rv.setLayoutManager(new GridLayoutManager(UserEventTimeSlot.this, 3));
                    }
                });
    }
}