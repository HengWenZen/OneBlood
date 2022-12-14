package com.example.OneBlood;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.OneBlood.Models.TimeSlot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
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
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class TimeSlotBooking extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static SharedPreferences mPreferences;
    private final String SHARED_PREF = "myPreferences";
    private final String KEY_USER = "user";
    private final String KEY_USER_NAME = "userName";
    private final String KEY_PASSWORD = "password";
    private final String KEY_USER_EMAIL = "userEmail";
    private final String KEY_USER_STATUS = "userStatus";

    public static final String EXTRA_LOCATION_TITLE = "location_name";
    public static final String EXTRA_LOCATION_ADDRESS = "location_address";
    public static final String EXTRA_LOCATION_CONTACT = "location_contact";
    public static final String EXTRA_LOCATION_OPERATION_HOUR = "location_operation_hours";
    public static final String EXTRA_LOCATION_IMAGE_URI = "imageUri";

    private List<TimeSlot> mTimeSlots;
    private TimeSlotAdapter mTimeSlotAdapter;
    Button btnBook;
    ImageView ivBackToLocation;
    TextInputLayout etLocationTitle, etLocationAddress, etLocationContact, etLocationOperationHours;
    String LocationContact, LocationOperationHours, userStatus, bookingDate, user, appointmentStatus;
    String dateSelected, locationImageUri;
    Date completeDate, selectedDate;
    TextView tvDateViewed,tvShowLocation, tvBookingStatus;
    ImageView ivLocationImage;
    ImageButton mDatePicker;
    private RecyclerView rv;
    List<TimeSlot> mTimeSlotList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Firebase mFirebase = new Firebase();
    StorageReference storageReference;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_slot_booking);
        SharedPreferences prefs = getSharedPreferences("myPreferences", MODE_PRIVATE);
        user = prefs.getString(KEY_USER_NAME, null);

        rv = (RecyclerView) findViewById(R.id.rvTimeSlot);
        tvDateViewed = (TextView) findViewById(R.id.tvShowDate);
        tvBookingStatus = findViewById(R.id.tvBookingStatus);
        btnBook = (Button) findViewById((R.id.btnBook));
        etLocationAddress = findViewById(R.id.etHospitalAddress);
        etLocationTitle = findViewById(R.id.etHospitalTitle);
        etLocationContact = findViewById(R.id.etHospitalContact);
        etLocationOperationHours = findViewById(R.id.etOperationHours);
        ivBackToLocation = findViewById(R.id.ivBackToLocation);
        ivLocationImage = findViewById(R.id.ivLocationImage);

        String locationName = getIntent().getStringExtra(EXTRA_LOCATION_TITLE);
        String LocationAddress = getIntent().getStringExtra(EXTRA_LOCATION_ADDRESS);
        String locationContact = getIntent().getStringExtra(EXTRA_LOCATION_CONTACT);
        String locationOperationHrs = getIntent().getStringExtra(EXTRA_LOCATION_OPERATION_HOUR);
        locationImageUri = getIntent().getStringExtra(EXTRA_LOCATION_IMAGE_URI);
        Log.d("TAG", "onCreate: " + locationImageUri);

        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference ref
                = storageReference
                .child(
                        "images/"
                                + locationImageUri);
        try {
            File localfile = File.createTempFile("tempfile", ".jpg");
            ref.getFile(localfile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                            ivLocationImage.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        setListener();

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date = dateFormat.format(currentDate);

        //Set text input layout to display location detail
        etLocationTitle.getEditText().setText(locationName);
        etLocationTitle.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etLocationAddress.getEditText().setText(LocationAddress);
        etLocationAddress.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etLocationContact.getEditText().setText(locationContact);
        etLocationContact.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));
        etLocationOperationHours.getEditText().setText(locationOperationHrs);
        etLocationOperationHours.getEditText().setTextColor(ContextCompat.getColor(this, R.color.black));


        db.collection("latestAppointment")
                .whereEqualTo("user", user)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot queryDocumentSnapshots = task.getResult();
                        if (!queryDocumentSnapshots.isEmpty()){
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                Log.d("Document ID:", document.getId() + " => " + document.getData());
                                userStatus = document.get("userStatus").toString();
                                bookingDate = document.get("date").toString();
                                appointmentStatus = document.get("status").toString();
                                Log.d("Booking Status", "onComplete: " + userStatus + bookingDate + appointmentStatus);

                                Date currentDate = new Date();
                                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                try {
                                    completeDate = df.parse(bookingDate);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                long diff = currentDate.getTime() - completeDate.getTime();
                                long days = TimeUnit.MILLISECONDS.toDays(diff);
                                int dayDiff = (int) days;
                                Log.d("Day Difference", "onComplete: " + dayDiff);

                                if (userStatus.equals("inactive") || appointmentStatus.equals("confirmed")){
                                    tvDateViewed.setClickable(false);
                                    tvDateViewed.setText("Not Available");
                                    tvBookingStatus.setVisibility(View.VISIBLE);

                                    if((dayDiff >= 90)){
                                        mFirebase.getData("latestAppointment", null, new MyCallback() {
                                            @Override
                                            public void returnData(ArrayList<Map<String, Object>> docList) {
                                                Log.d("firebase example", docList.toString());
                                                ArrayList<String> list = new ArrayList<>();

                                                for (Map<String, Object> map : docList) {
                                                    if (map.get("user").toString().equals(user)) {
                                                        Map<String, Object> user = new HashMap<>();
                                                        user.put("userStatus", "active");
                                                        user.put("status", "available");
                                                        mFirebase.updData("latestAppointment", user, map.get("id").toString());
                                                        Log.d("TAG", "returnData: User Status Changed to Active");
                                                    }
                                                }
                                            }
                                        });
                                        btnBook.setEnabled(true);
                                        tvDateViewed.setClickable(true);
                                        tvBookingStatus.setVisibility(View.INVISIBLE);
                                    }

                                }else{
                                    btnBook.setEnabled(true);
                                    tvDateViewed.setClickable(true);
                                    tvDateViewed.setText("Click Me to Choose A Date");
                                    tvBookingStatus.setVisibility(View.INVISIBLE);

                                }
                            }
                        }else if(queryDocumentSnapshots.isEmpty()){
                            btnBook.setEnabled(true);
                            tvDateViewed.setClickable(true);
                            tvDateViewed.setText("Click Me to Choose A Date");
                            tvBookingStatus.setVisibility(View.INVISIBLE);
                        }
                    }
                });


        tvDateViewed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialize Date picker dialog after textView is clicked
                showDatePickerDialog();
            }
        });


        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Validate if date is null
                if (dateSelected == null) {

                    Toast.makeText(TimeSlotBooking.this, "Please select a date...", Toast.LENGTH_SHORT).show();

                } else {

                    String locationName = getIntent().getStringExtra(EXTRA_LOCATION_TITLE);
                    String slot = String.valueOf(mTimeSlotAdapter.selectedSlot);

                    //Validate if there is any slot selected
                    if (slot == "") {
                        Toast.makeText(TimeSlotBooking.this, "Please select available time slot", Toast.LENGTH_SHORT).show();
                    } else {
                        //Toast alert dialog
                        String msg = "Confirm Booking" + locationName + " on " + dateSelected + " " + mTimeSlotAdapter.timeSlot(Integer.valueOf(slot));
                        AlertDialog.Builder alert = new AlertDialog.Builder(TimeSlotBooking.this);
                        alert.setTitle("Booking Confirmation");
                        alert.setMessage(msg);
                        alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences prefs = getSharedPreferences("myPreferences", MODE_PRIVATE);
                                String userName = prefs.getString(KEY_USER_NAME, null);

                                //Put data into hash map
                                Map<String, Object> data = new HashMap<>();
                                data.put("slot", slot);
                                data.put("user", userName);
                                data.put("date", dateSelected);
                                data.put("location", locationName);
                                data.put("status", "confirmed");
                                data.put("userStatus", "active");

                                db.collection("latestAppointment")
                                        .whereEqualTo("user", userName)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                QuerySnapshot queryDocumentSnapshots = task.getResult();
                                                if (queryDocumentSnapshots.isEmpty()) {
                                                    db.collection("latestAppointment").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                        }
                                                    });

                                                } else if (!queryDocumentSnapshots.isEmpty()) {
                                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                                        Log.d("Document ID:", document.getId() + " => " + document.getData());
                                                        db.collection("latestAppointment").document(document.getId()).update(data);

                                                    }
                                                }
                                            }
                                        });

                                //Add Confirmed Appointment into userBooking document in Firebase
                                db.collection("userBooking").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(TimeSlotBooking.this, "Booking Success", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(TimeSlotBooking.this, "Booking Failure", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        alert.show();
                    }
                }
            }
        });
    }

    private void showDatePickerDialog() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(TimeSlotBooking.this ,
                this, year, month, day);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, +7);
        Date minDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, +30);
        Date newDate = calendar.getTime();

        //Set minimum date for date picker dialog
        datePickerDialog.getDatePicker().setMinDate(minDate.getTime());
        datePickerDialog.getDatePicker().setMaxDate(newDate.getTime());
        datePickerDialog.show();
    }



    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Date setDate = new GregorianCalendar(year, month, dayOfMonth).getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String date = formatter.format(setDate);

        availableSlot(date);
        tvDateViewed.setText(year + "\n " + dayOfMonth + " " + new DateFormatSymbols().getMonths()[month]);
//        Toast.makeText(TimeSlotBooking.this, String.valueOf(month+1), Toast.LENGTH_SHORT).show();

        dateSelected = "";

        if(dayOfMonth<10) {
            dateSelected = dateSelected + "0" + dayOfMonth;
        }else{
            dateSelected = dateSelected + dayOfMonth;
        }

        if( month + 1 < 10){
            dateSelected = dateSelected + "-0" + (month + 1);
        }else{
            dateSelected = dateSelected +"-"+ (month + 1);
        }

        dateSelected = dateSelected + "-" + year;
    }

    private void availableSlot(String date) {
        mTimeSlotList = new ArrayList<>();
        String location = getIntent().getStringExtra(EXTRA_LOCATION_TITLE);

        ProgressDialog dialog = ProgressDialog.show(TimeSlotBooking.this, "",
                "Loading......", true);

        db.collection("userBooking").whereEqualTo("date",date).whereEqualTo("location", location)
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
                                    TimeSlot slot = new TimeSlot();
                                    slot.setSlot((document.get("slot").toString()));
                                    mTimeSlotList.add(slot);
                                }
                            }
                        } else{
                            Log.d( "Cached get failed: ", String.valueOf(task.getException()));
                        }
                            dialog.dismiss();
                            rv.setHasFixedSize(true);
                            mTimeSlotAdapter = new TimeSlotAdapter(TimeSlotBooking.this, mTimeSlotList);
                            rv.setAdapter(mTimeSlotAdapter);
                            rv.setLayoutManager(new GridLayoutManager(TimeSlotBooking.this, 3));
                    }
                });
    }

    private void setListener(){
        ivBackToLocation.setOnClickListener(v -> onBackPressed());
    }
}
