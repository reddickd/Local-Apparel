package com.example.cmsc436.localapparel.Objects;

import java.util.ArrayList;
import java.util.List;

public class Chat {

    private String reciever;
    private String sender;
    private String itemName;
    private List<Message> messages;

    public Chat(String from, String to, String itemName){
        this.sender = from;
        this.reciever = to;
        this.itemName = itemName;
        messages = new ArrayList<Message>();
        messages.add(new Message("test1", "is it working"));
    }

    public Chat() {
        messages = new ArrayList<Message>();
    }

    public List<Message> getMessages(){
        return this.messages;
    }

    public String getSender(){
        return this.sender;
    }

    public String getReciever(){
        return this.reciever;

    }
    public String getItemName(){
        return this.itemName;
    }

}
