package com.example.cmsc436.localapparel.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Message implements Serializable {

    private String sender;
    private String message;
    private String timeStamp;

    public Message(String sender, String message, String timeStamp){
        this.sender = sender;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public Message(){}

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public String getTimeStamp() { return  timeStamp; }

}
