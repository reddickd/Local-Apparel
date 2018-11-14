package com.example.cmsc436.localapparel.Objects;

public  class Item{
    int id;
    String downloadURL;


    public Item(){

    }

    public Item(int id, String downloadURL){
        this.downloadURL = downloadURL;
        this.id = id;
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
}