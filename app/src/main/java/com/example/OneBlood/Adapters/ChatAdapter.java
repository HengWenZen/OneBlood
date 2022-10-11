package com.example.OneBlood.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.OneBlood.Models.ChatMessage;
import com.example.OneBlood.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public static final int TYPE_SENDER = 1;
    public static final int TYPE_RECEIVER = 2;
    private Context mContext;
    private final List<ChatMessage> mChatMessages;
    private final String mSenderId;

    public ChatAdapter(List<ChatMessage> chatMessages, Context context, String senderId) {
        this.mContext = context;
        this.mChatMessages = chatMessages;
        this.mSenderId = senderId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        if(viewType == TYPE_SENDER){
            //Display Receiver's Message
            view = layoutInflater.inflate(R.layout.item_container_sent_message,parent,false);
            return new SenderMessage(view);
        }else{
            //Display Sender's Message
            view = layoutInflater.inflate(R.layout.item_container_received_message,parent,false);
            return new ReceiverMessage(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mChatMessages.get(position).senderId.equals(mSenderId)){
            return TYPE_SENDER;
        }else{
            return TYPE_RECEIVER;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_SENDER){
            ((SenderMessage) holder).setSenderChat(mChatMessages.get(position));
        }else {
            ((ReceiverMessage) holder).setReceiverChat(mChatMessages.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mChatMessages.size();
    }

    static class ReceiverMessage extends RecyclerView.ViewHolder{

        private TextView tvReceivedMessage,tvReceivedMessageDateTime;

        ReceiverMessage(@NonNull View itemView) {
            super(itemView);
            tvReceivedMessage = itemView.findViewById(R.id.tvReceivedMessage);
            tvReceivedMessageDateTime = itemView.findViewById(R.id.tvReceivedMessageDateTime);
        }

        void setReceiverChat(ChatMessage chatMessage){
            tvReceivedMessage.setText(chatMessage.getMessage());
            tvReceivedMessageDateTime.setText(chatMessage.getDateTime());
        }
    }

    static class SenderMessage extends RecyclerView.ViewHolder{
        private TextView tvSenderMessage,tvMessageTimeDate;

        SenderMessage(@NonNull View itemView) {
            super(itemView);
            tvSenderMessage = itemView.findViewById(R.id.tvSenderMessage);
            tvMessageTimeDate = itemView.findViewById(R.id.tvMessageTimeDate);
        }

        void setSenderChat(ChatMessage chatMessage){
            tvSenderMessage.setText(chatMessage.getMessage());
            tvMessageTimeDate.setText(chatMessage.getDateTime());
        }
    }
}
