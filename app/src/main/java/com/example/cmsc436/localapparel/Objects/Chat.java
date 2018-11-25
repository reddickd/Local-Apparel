package com.example.cmsc436.localapparel.Objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Chat implements Serializable {

    private String reciever;
    private String sender;
    private String itemName;
    private List<Message> messages;

    public Chat(String from, String to, String itemName){
        this.sender = from;
        this.reciever = to;
        this.itemName = itemName;
        messages = new ArrayList<Message>();
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

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }

        if(!(obj instanceof Chat)){
            return false;
        }else{
            Chat other = (Chat) obj;
            return (this.sender.equals(other.sender)) && (this.reciever.equals(other.reciever)) && (this.itemName.equals(other.itemName));
        }

    }

    public void addMessage(Message m){
        messages.add(m);
    }
}
