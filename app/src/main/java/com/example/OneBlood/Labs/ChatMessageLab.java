package com.example.OneBlood.Labs;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.OneBlood.Models.ChatMessage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatMessageLab {
    private static ChatMessageLab sChatMessageLab;
    private List<ChatMessage> mChatMessages;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static ChatMessageLab get(Context context){
        sChatMessageLab = new ChatMessageLab(context);
        return sChatMessageLab;
    }

    public ChatMessageLab(Context context){
        mChatMessages = new ArrayList<>();
        mChatMessages.clear();

        db.collection("chat")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot result = task.getResult();
                            if (!result.isEmpty()) {
                                Log.d("Data Retrieved", result.toString());
                                for (QueryDocumentSnapshot document : result) {
                                    ChatMessage chatMessage = new ChatMessage();
                                    chatMessage.setChatId(document.getId());
                                    chatMessage.setMessage(document.get("message").toString());
                                    chatMessage.setDateTime(document.get("dateTime").toString());
                                    chatMessage.setSenderId(document.get("senderID").toString());
                                    chatMessage.setReceiverId(document.get("receiverID").toString());
                                   mChatMessages.add(chatMessage);
                                }
                            }
                        }
                    }
                });
    }

    public List<ChatMessage> getChatMessage() {
        return mChatMessages;
    }

    public ChatMessage getChatMessages(UUID uuid) {
        for(ChatMessage chatMessage: mChatMessages){
            if(chatMessage.getChatId().equals(uuid)){
                return chatMessage;
            }
        }
        return null;
    }
}
