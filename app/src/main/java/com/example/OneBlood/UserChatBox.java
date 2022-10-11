package com.example.OneBlood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.OneBlood.Adapters.ChatAdapter;
import com.example.OneBlood.Labs.ChatMessageLab;
import com.example.OneBlood.Models.ChatMessage;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class UserChatBox extends AppCompatActivity {

    public static final String EXTRA_DONOR_NAME = "donor_name";
    public static final String EXTRA_DONOR_ID = "donor_id";
    public static final String KEY_CHAT_COLLECTION = "chat";
    public static final String KEY_CHAT_MESSAGE = "message";
    public static final String KEY_DATE_TIME = "dateTime";
    public static final String KEY_SENDER_ID = "senderID";
    public static final String KEY_RECEIVER_ID = "receiverID";
    public static SharedPreferences mPreferences;
    private final String SHARED_PREF = "myPreferences";
    private final String KEY_USER_ID = "userID";
    private final String KEY_USER_NAME = "userName";
    private final String KEY_PASSWORD = "password";
    private final String KEY_USER_EMAIL = "userEmail";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseFirestore database;
    ChatMessageLab chatMessageLab = ChatMessageLab.get(this);
    List<ChatMessage> chatMessages = chatMessageLab.getChatMessage();

    TextView tvUserName;
    EditText etInputMessage;
    ImageView ivBack, ivSendMessage;
    ProgressBar mProgressBar;
    String receiver, sender, senderId;
    RecyclerView mRecyclerView;
    ChatAdapter chatAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat_box);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        tvUserName = findViewById(R.id.userName);
        ivBack = findViewById(R.id.ivBackToList);
        etInputMessage = findViewById(R.id.etInputMessage);
        mRecyclerView = findViewById(R.id.rvChat);
        ivSendMessage = findViewById(R.id.ivSendMessage);
        mProgressBar = findViewById(R.id.progressBar);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        senderId = prefs.getString(KEY_USER_ID, "");

        setListener();
        receiver = (String) b.get(EXTRA_DONOR_NAME);
        tvUserName.setText(receiver);
        init();
        listenMessage();

    }

    private void setListener() {
        ivBack.setOnClickListener(v -> onBackPressed());
        ivSendMessage.setOnClickListener(v -> sendMessage());
    }

    private void init() {
        SharedPreferences prefs = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        senderId = prefs.getString(KEY_USER_ID, "");

        ChatMessageLab chatMessageLab = ChatMessageLab.get(this);
        List<ChatMessage> chatMessages = chatMessageLab.getChatMessage();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(UserChatBox.this));
        chatAdapter = new ChatAdapter(chatMessages, UserChatBox.this, senderId);
        mRecyclerView.setAdapter(chatAdapter);
        database = FirebaseFirestore.getInstance();
    }

    private void sendMessage() {
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        String inputMessage = etInputMessage.getText().toString();

        if (inputMessage == null) {
            Toast.makeText(this, "Please input a message !", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, Object> message = new HashMap<>();
            message.put(KEY_SENDER_ID, senderId);
            message.put(KEY_RECEIVER_ID, (String)b.get(EXTRA_DONOR_ID));
            message.put(KEY_CHAT_MESSAGE, inputMessage);
            message.put(KEY_DATE_TIME, new Date());
            db.collection("chat").add(message);
            etInputMessage.setText(null);
        }
    }

    private void listenMessage(){
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        String receiverID = (String)b.get(EXTRA_DONOR_ID);
        SharedPreferences prefs = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        senderId = prefs.getString(KEY_USER_ID, "");

        database.collection("chat")
                .whereEqualTo(KEY_SENDER_ID, senderId)
                .whereEqualTo(KEY_RECEIVER_ID, receiverID)
                .addSnapshotListener(eventListener);
        database.collection("chat")
                .whereEqualTo(KEY_SENDER_ID, receiverID)
                .whereEqualTo(KEY_RECEIVER_ID, senderId)
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {

        if (error != null) {
            return;
        }
        if (value != null) {
            int count = chatMessages.size();
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if(documentChange.getType() == DocumentChange.Type.ADDED) {
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId = documentChange.getDocument().getString(KEY_SENDER_ID);
                    chatMessage.receiverId = documentChange.getDocument().getString(KEY_RECEIVER_ID);
                    chatMessage.message = documentChange.getDocument().getString(KEY_CHAT_MESSAGE);
                    chatMessage.dateTime = getReadableDateTime(documentChange.getDocument().getDate(KEY_DATE_TIME));
                    chatMessage.dateObject = documentChange.getDocument().getDate(KEY_DATE_TIME);
                    chatMessages.add(chatMessage);
                }
            }

            try {
                Collections.sort(chatMessages, new Comparator<ChatMessage>() {
                    @Override
                    public int compare(ChatMessage o1, ChatMessage o2) {
                        String date = getReadableDateTime(o1.dateObject);
                        String date2 = getReadableDateTime(o2.dateObject);
                        Toast.makeText(UserChatBox.this, date, Toast.LENGTH_SHORT).show();
                        Toast.makeText(UserChatBox.this, date2, Toast.LENGTH_SHORT).show();
                        return date.compareTo(date2);
                    }
                });
            }catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            if(count == 0){
                chatAdapter.notifyDataSetChanged();
            }else{
                chatAdapter.notifyItemRangeInserted(chatMessages.size(), chatMessages.size());
                mRecyclerView.smoothScrollToPosition(chatMessages.size()-1);
            }
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        mProgressBar.setVisibility(View.GONE);
    };


    private String getReadableDateTime(Date date){
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }

}