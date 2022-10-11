package com.example.OneBlood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.OneBlood.Activity.HospitalMenu;
import com.example.OneBlood.Labs.BloodRequestLab;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.net.URI;
import java.security.PublicKey;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.SSLEngineResult;

import static java.util.Calendar.*;

public class HospitalEvents extends AppCompatActivity {

   public static final String START_DATE = "startDate";
   public static final String END_DATE = "endDate";

   private static int mYear1, mMonth1, mDay1, mYear2, mMonth2, mDay2;
    EditText etHospitalEventStartDate, etHospitalEventEndDate;
    TextInputLayout etHospitalEventTitle, etHospitalEventLocation, etHospitalEventDescription, etHospitalEventOperationHrs;
    Button btnPostEvent, btnChoosePic;
    ImageView ivBackToHome, ivEventPic, ivStartDate, ivEndDate;
    String randomNum, generatedFilePath, startDate, endDate;
    Uri imageUri;
    EditText clickedEditText;
    private final int GALLERY_REQ_CODE = 1000;
    private StorageReference mStorageReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_events);

        etHospitalEventLocation = findViewById(R.id.etHospitalEventLocation);
        etHospitalEventStartDate = findViewById(R.id.etEventStartDate);
        etHospitalEventEndDate = findViewById(R.id.etEventEndDate);
        etHospitalEventTitle = findViewById(R.id.etHospitalEventTitle);
        etHospitalEventDescription = findViewById(R.id.etHospitalEventDescription);
        etHospitalEventOperationHrs = findViewById(R.id.etHospitalEventOperationHours);
        btnPostEvent = findViewById(R.id.btnPostEvent);
        btnChoosePic = findViewById(R.id.btnChoosePic);
        ivStartDate = findViewById(R.id.ivStartDate);
        ivEndDate = findViewById(R.id.ivEndDate);
        ivBackToHome = findViewById(R.id.ivBackToHome);
        ivEventPic= findViewById(R.id.ivEventPic);

        etHospitalEventStartDate.setCursorVisible(false);
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

       etHospitalEventStartDate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               clickedEditText = (EditText) v;

               DatePickerDialog datePickerDialog = new DatePickerDialog(HospitalEvents.this,date ,myCalendar.get(YEAR),myCalendar.get(MONTH),myCalendar.get(DAY_OF_MONTH));

               Calendar calendar = getInstance();
               calendar.add(DAY_OF_MONTH, 14);
               Date StartDate = calendar.getTime();

//        mDateView.setText(year +"\n " + day +" " + new DateFormatSymbols().getMonths()[month]);

               datePickerDialog.getDatePicker().setMinDate(StartDate.getTime());
               calendar.add(DAY_OF_MONTH, 60);
               Date EndDate = calendar.getTime();
               datePickerDialog.getDatePicker().setMaxDate(EndDate.getTime());

               datePickerDialog.show();
           }
       });

       etHospitalEventEndDate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               clickedEditText = (EditText) v;

               DatePickerDialog datePickerDialog = new DatePickerDialog(HospitalEvents.this,date ,myCalendar.get(YEAR),myCalendar.get(MONTH),myCalendar.get(DAY_OF_MONTH));

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

    private void postEvent(){
        String eventLocation, eventOperationHrs, eventStartDate, eventEndDate, eventTitle, eventDescription;
        eventLocation = etHospitalEventLocation.getEditText().getText().toString().trim();
        eventTitle = etHospitalEventTitle.getEditText().getText().toString().trim();
        eventDescription = etHospitalEventDescription.getEditText().getText().toString().trim();
        eventOperationHrs = etHospitalEventOperationHrs.getEditText().getText().toString().trim();
        eventStartDate = etHospitalEventStartDate.getText().toString().trim();
        eventEndDate = etHospitalEventEndDate.getText().toString().trim();

        if(imageUri == null){
            Toast.makeText(this, "Please Select an Image!", Toast.LENGTH_SHORT).show();
        }else {
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
                etHospitalEventStartDate.requestFocus();

            } else if (TextUtils.isEmpty(eventOperationHrs)) {
                etHospitalEventOperationHrs.setError("Please fill in Request Location!");
                etHospitalEventOperationHrs.requestFocus();

            } else {
                uploadPicture();
                Map<String, Object> event = new HashMap<>();
                event.put("title", eventTitle);
                event.put("description", eventDescription);
                event.put("location", eventLocation);
                event.put("imageUri", randomNum);
                event.put(START_DATE, eventStartDate);
                event.put(END_DATE, eventEndDate);
                event.put("time", eventOperationHrs);

                db.collection("events")
                        .add(event)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(HospitalEvents.this, "Event Posted Successfully !", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(HospitalEvents.this, HospitalMenu.class);
                                startActivity(i);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HospitalEvents.this, "Fail to post Event. Please Try Again.", Toast.LENGTH_SHORT).show();
                    }
                });
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
                    Toast.makeText(HospitalEvents.this, "Fail to upload image.." + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(HospitalEvents.this, "Image Selected Successfully !", Toast.LENGTH_SHORT).show();
            }
        }
    }
}