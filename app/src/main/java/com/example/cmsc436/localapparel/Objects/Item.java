package com.example.cmsc436.localapparel.Objects;

import java.io.Serializable;
import java.util.ArrayList;

public class Item implements Serializable{
    int id;
    String downloadURL,userID, name, brand, description, category, condition, size, price;
    double latitude, longitude;

    ArrayList<String> otherImages;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Item(){}

    public Item(int id, String downloadURL, String userID, String name, String brand, String description, String category, String condition, String size, String price, double latitude, double longitude){
        this.downloadURL = downloadURL;
        this.id = id;
        this.userID = userID;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.category = category;
        this.condition = condition;
        this.size = size;
        this.latitude = latitude;
        this.longitude = longitude;
        this.price = price;

        otherImages = new ArrayList<String>();

    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void addImage(String imageName){
        otherImages.add(imageName);
    }
}