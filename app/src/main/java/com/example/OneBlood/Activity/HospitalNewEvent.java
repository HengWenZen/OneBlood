package com.example.OneBlood.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.OneBlood.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static java.util.Calendar.*;

public class HospitalNewEvent extends AppCompatActivity {

    public static SharedPreferences hospitalPreferences;
    private final String SHARED_PREFERENCE = "hospitalPreferences";
    private final String KEY_HOSPITAL_ID = "hospitalID";
    private final String KEY_HOSPITAL_NAME = "hospitalName";

    public static final String START_DATE = "startDate";
    public static final String END_DATE = "endDate";

    TextInputLayout etHospitalEventStartDate, etHospitalEventEndDate;
    TextInputLayout etHospitalEventTitle, etHospitalEventLocation, etHospitalEventDescription, etHospitalEventOperationHrs, etHospitalEventStartTime, etHospitalEventEndTime;
    TextInputEditText etStartTime, etEndTime, etStartDate, etEndDate;
    Button btnPostEvent, btnChoosePic;
    ImageView ivBackToHome, ivEventPic, ivStartDate, ivEndDate;
    String randomNum, generatedFilePath, hospital;
    int startHour, startMinute, endHour, endMinute;
    Date mStartDate, mEndDate, startTime, endTime;
    Uri imageUri;
    EditText clickedEditText;
    private final int GALLERY_REQ_CODE = 1000;
    private StorageReference mStorageReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_new_event);

        etHospitalEventLocation = findViewById(R.id.etHospitalEventLocation);
        etHospitalEventStartDate = findViewById(R.id.etHospitalEventStartDate);
        etHospitalEventEndDate = findViewById(R.id.etHospitalEventEndDate);
        etHospitalEventTitle = findViewById(R.id.etHospitalEventTitle);
        etHospitalEventDescription = findViewById(R.id.etHospitalEventDescription);
        etHospitalEventStartTime = findViewById(R.id.etHospitalEventStartTime);
        etHospitalEventEndTime = findViewById(R.id.etHospitalEventEndTime);
        etStartDate = findViewById(R.id.startDate);
        etEndDate = findViewById(R.id.endDate);
        etStartTime = findViewById(R.id.startTime);
        etEndTime = findViewById(R.id.endTime);
        btnPostEvent = findViewById(R.id.btnPostEvent);
        btnChoosePic = findViewById(R.id.btnChoosePic);
        ivBackToHome = findViewById(R.id.ivBackToEventHome);
        ivEventPic= findViewById(R.id.ivEventPic);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        hospital = prefs.getString(KEY_HOSPITAL_NAME, "");

        etHospitalEventLocation.getEditText().setText(hospital);

        setListener();

       final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                update();
            }
        };

       etStartDate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               clickedEditText = (EditText) v;

               DatePickerDialog datePickerDialog = new DatePickerDialog(HospitalNewEvent.this,date ,myCalendar.get(YEAR),myCalendar.get(MONTH),myCalendar.get(DAY_OF_MONTH));

               Calendar calendar = getInstance();
               calendar.add(DAY_OF_MONTH, 14);
               Date StartDate = calendar.getTime();

               datePickerDialog.getDatePicker().setMinDate(StartDate.getTime());
               calendar.add(DAY_OF_MONTH, 60);
               Date EndDate = calendar.getTime();
               datePickerDialog.getDatePicker().setMaxDate(EndDate.getTime());

               datePickerDialog.show();
           }
       });

       etEndDate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               clickedEditText = (EditText) v;

               DatePickerDialog datePickerDialog = new DatePickerDialog(HospitalNewEvent.this,date ,myCalendar.get(YEAR),myCalendar.get(MONTH),myCalendar.get(DAY_OF_MONTH));

               Calendar calendar = getInstance();
               calendar.add(DAY_OF_MONTH, 14);
               Date StartDate = calendar.getTime();
               datePickerDialog.getDatePicker().setMinDate(StartDate.getTime());
               calendar.add(DAY_OF_MONTH, 60);
               Date EndDate = calendar.getTime();
               datePickerDialog.getDatePicker().setMaxDate(EndDate.getTime());

               datePickerDialog.show();
           }
       });

       etStartTime.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                //Initialize time pickerdialog
               TimePickerDialog timePickerDialog = new TimePickerDialog(HospitalNewEvent.this, new TimePickerDialog.OnTimeSetListener() {
                   @Override
                   public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                       startHour = hourOfDay;
                       startMinute = minute;

                       Calendar calendar = Calendar.getInstance();
                       calendar.set(0,0,0,startHour,startMinute);
                       startTime = calendar.getTime();
                       etStartTime.setText(DateFormat.format("hh:mm aa", calendar));

                   }
               }, 12, 0, false);
               timePickerDialog.updateTime(startHour, startMinute);
               timePickerDialog.show();
           }
       });

        etEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialize time pickerdialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(HospitalNewEvent.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        endHour = hourOfDay;
                        endMinute = minute;

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0,0,0,endHour,endMinute);
                        endTime = calendar.getTime();
                        etEndTime.setText(DateFormat.format("hh:mm aa", calendar));

                    }
                }, 12, 0, false);
                timePickerDialog.updateTime(endHour, endMinute);
                timePickerDialog.show();
            }
        });

        ivBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setListener() {
        ivBackToHome.setOnClickListener(v -> onBackPressed());
        btnPostEvent.setOnClickListener(v -> postEvent());
        btnChoosePic.setOnClickListener(v -> choosePic());
    }

    private void update() {
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        clickedEditText.setText(sdf.format(myCalendar.getTime()));
    }

    private void postEvent() {
        String eventLocation, eventStartDate, eventEndDate, eventTitle, eventDescription, eventStartTime, eventEndTime;
        eventLocation = etHospitalEventLocation.getEditText().getText().toString().trim();
        eventTitle = etHospitalEventTitle.getEditText().getText().toString().trim();
        eventDescription = etHospitalEventDescription.getEditText().getText().toString().trim();
        eventStartTime = etHospitalEventStartTime.getEditText().getText().toString().trim();
        eventEndTime = etHospitalEventEndTime.getEditText().getText().toString().trim();
        eventStartDate = etStartDate.getText().toString().trim();
        eventEndDate = etEndDate.getText().toString().trim();

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");


        try {
            //Parse String using Simple Date Format
            mStartDate = df.parse(eventStartDate);
            mEndDate = df.parse(eventEndDate);
            startTime = simpleDateFormat.parse(eventStartTime);
            endTime = simpleDateFormat.parse(eventEndTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (imageUri == null) {
            //Check if there is any image selected
            Toast.makeText(this, "Please Select an Image!", Toast.LENGTH_SHORT).show();
        } else {
            //Validate user input to make sure no empty field
            if (TextUtils.isEmpty(eventTitle)) {
                etHospitalEventTitle.setError("Please fill in Request Title!");
                etHospitalEventTitle.requestFocus();

            } else if (TextUtils.isEmpty(eventDescription)) {
                etHospitalEventDescription.setError("Please fill in Recipient's Name!");
                etHospitalEventDescription.requestFocus();

            } else if (TextUtils.isEmpty(eventLocation)) {
                etHospitalEventLocation.setError("Please fill in Recipient's Contact!");
                etHospitalEventLocation.requestFocus();

            } else if (TextUtils.isEmpty(eventStartDate)) {
                Toast.makeText(this, "Please select the start day!", Toast.LENGTH_SHORT).show();
                etHospitalEventStartDate.requestFocus();

            } else if (TextUtils.isEmpty(eventEndDate)) {
                Toast.makeText(this, "Please select the end day!", Toast.LENGTH_SHORT).show();
                etHospitalEventEndDate.requestFocus();

            } else if (TextUtils.isEmpty(eventStartTime)) {
                etHospitalEventStartTime.setError("Please fill in Start Time!");
                etHospitalEventStartTime.requestFocus();

            }else if (TextUtils.isEmpty(eventEndTime)) {
                etHospitalEventEndTime.setError("Please fill in Start Time!");
                etHospitalEventEndTime.requestFocus();
            } else {

                long diff = mEndDate.getTime() - mStartDate.getTime();
                long time = endTime.getTime() - startTime.getTime();
                int daysDiff = (int) (time / (1000*60*60*24));
                int hours = (int) ((time - (1000 * 60 * 60 * 24 * daysDiff)) / (1000 * 60 * 60));
                long days = TimeUnit.MILLISECONDS.toDays(diff);
                int numberOfDays = (int) days;


                if (numberOfDays < 0) {
                    //Check if End Date is greater than Start Date
                    Toast.makeText(this, "End Date cannot be greater than Start Date!", Toast.LENGTH_SHORT).show();
                    etHospitalEventStartDate.requestFocus();
                    etHospitalEventEndDate.requestFocus();

                }else if(hours < 6){
                    //Check if End Time is greater than Start Time
                    Toast.makeText(this, "The event must have at least a duration of 6 hours", Toast.LENGTH_SHORT).show();
                    etHospitalEventStartTime.requestFocus();
                    etHospitalEventEndTime.requestFocus();

                } else {
                    uploadPicture();
                    Map<String, Object> event = new HashMap<>();
                    event.put("title", eventTitle);
                    event.put("description", eventDescription);
                    event.put("location", eventLocation);
                    event.put("imageUri", randomNum);
                    event.put(START_DATE, eventStartDate);
                    event.put(END_DATE, eventEndDate);
                    event.put("startTime", eventStartTime);
                    event.put("endTime", eventEndTime);
                    event.put("postedBy", hospital);

                    //Save event details into Firebase Database
                    db.collection("events")
                            .add(event)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(HospitalNewEvent.this, "Event Posted Successfully !", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(HospitalNewEvent.this, HospitalViewEvent.class);
                                    startActivity(i);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(HospitalNewEvent.this, "Fail to post Event. Please Try Again.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }
    }

    private void uploadPicture() {
        if(imageUri != null){
            randomNum = UUID.randomUUID().toString();

            mStorageReference = FirebaseStorage.getInstance().getReference();
            StorageReference reference
                    = mStorageReference
                    .child(
                            "images/" + randomNum);

            reference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl();
                            if(downloadUri.isSuccessful()){
                                generatedFilePath = downloadUri.getResult().toString();
                            }else{
                                generatedFilePath = "Fail";
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(HospitalNewEvent.this, "Fail to upload image.." + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void choosePic(){
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, GALLERY_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQ_CODE) {
                imageUri = data.getData();
                ivEventPic.setImageURI(imageUri);
                Toast.makeText(HospitalNewEvent.this, "Image Selected Successfully !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Cancel Event Posting?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(HospitalNewEvent.this, HospitalViewEvent.class);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).create().show();
    }
}