package com.example.cmsc436.localapparel;

public class User {

    String email,password,uid;
    //String name


    public User(){

    }

    public User(String email,String password,String uid){

        //this.name = name;
        this.uid = uid;
        this.email = email;
        this.password = password;
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

    public void setPassword(String password){
        this.password = password;
    }
    public String getUid(){
        return this.uid;
    }
    public void setUid(String uid){
        this.uid = uid;
    }

}
