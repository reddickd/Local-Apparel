package com.example.cmsc436.localapparel.Objects;

import java.io.Serializable;

public  class Item implements Serializable{
    int id;
    String downloadURL,userID;


    public Item(){

    }

    public Item(int id, String downloadURL, String userID){
        this.downloadURL = downloadURL;
        this.id = id;
        this.userID = userID;
    }

    public int getCount(){
        return this.id;
    }
    public void setCount(int id){
        this.id = id;
    }

    public String getDownloadURL() {
        return downloadURL;
    }

    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }
}