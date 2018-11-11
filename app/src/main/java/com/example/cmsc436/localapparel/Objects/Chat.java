package com.example.cmsc436.localapparel.Objects;

import java.util.ArrayList;
import java.util.List;

public class Chat {

    private String from;
    private String to;
    private List<Message> messages;

    public Chat(String from, String to){
        this.from = from;
        this.to = to;
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
        return this.from;
    }

    public String getReciever(){
        return this.to;

    }

}
