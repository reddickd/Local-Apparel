package com.example.cmsc436.localapparel.Objects;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

public class User {

    String email,password,uid,phoneNumber;
    Double userLat, userLong;
    //int numItems;
    List<Chat> chats;

    //String name


    public User(){

    }

    public User(String email,String password,String uid,String phoneNumber, Double lat, Double longitude){
        //numItems = 0;
        //this.name = name;
        this.uid = uid;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.chats = new ArrayList<Chat>();
        this.userLat = lat;
        this.userLong = longitude;
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
    public void addChat(Chat chat) {
        if(chats == null){
            chats = new ArrayList<Chat>();
        }
        this.chats.add(chat);}
    public List<Chat> getChats() { return  this.chats;}
//    public int getnumItems(){
//        return this.numItems;
//    }
//    public void setNumItems(int num){
//        this.numItems = num;
//    }
//    public void incrementNumItems(){
//        this.numItems++;
//    }
    public double getLatitude(){
        return this.userLat;
    }
    public double getLongitude(){
        return this.userLong;
    }
    public void setLatitude(double lat){
        this.userLat = lat;
    }
    public void setLongitude(double longitude){
        this.userLong = longitude;
    }

}
