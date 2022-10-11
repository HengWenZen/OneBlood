package com.example.OneBlood.Models;

import java.util.Date;

public class ChatMessage {
    public String senderId, receiverId, message, dateTime, chatId;
    public Date dateObject;

    public ChatMessage() {
    }

    public String getMessage() {
        return message;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getChatId() {
        return chatId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
}
