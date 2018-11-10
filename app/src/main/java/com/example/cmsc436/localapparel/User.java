package com.example.cmsc436.localapparel;

public class User {

    String email,password;
    //String name


    public User(){

    }

    public User(String email,String password){

        //this.name = name;
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

}
