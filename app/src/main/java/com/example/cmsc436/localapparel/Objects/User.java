package com.example.cmsc436.localapparel.Objects;

import java.util.ArrayList;
import java.util.List;

public class User {

    String email,password,uid,phoneNumber;
    List<Chat> chats;

    //String name


    public User(){

    }

    public User(String email,String password,String uid,String phoneNumber){

        //this.name = name;
        this.uid = uid;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.chats = new ArrayList<Chat>();
        chats.add(new Chat("test1", "test2"));
    }

//    public User(String name,String email,String password){
//
//        this.name = name;
//        this.email = email;
//        this.password = password;
//    }

    public String getEmail(){
        return this.email;
    }

    public String getPassword(){
        return this.password;
    }

    public void setEmail(String email){
        this.email = email;
    }
    public String getPhoneNumber() {return this.phoneNumber; }

    public void setPassword(String password){
        this.password = password;
    }
    public String getUid(){
        return this.uid;
    }
    public void setUid(String uid){
        this.uid = uid;
    }
    public void addChat(Chat chat) {this.chats.add(chat);}
    public List<Chat> getChats() { return  this.chats;}

}