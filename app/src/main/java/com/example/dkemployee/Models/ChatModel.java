package com.example.dkemployee.Models;

public class ChatModel {
    String message_id,message,sender_id,timestamp;

    public ChatModel(String message, String sender_id,String timestamp) {
        this.message = message;
        this.sender_id = sender_id;
        this.timestamp = timestamp;
    }
    public ChatModel(){
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
