package com.example.cmsc436.localapparel.Objects;

public  class Item{
    int id;


    public Item(){

    }

    public Item(int id){

        this.id = id;
    }

    public int getCount(){
        return this.id;
    }
    public void setCount(int id){
        this.id = id;
    }

}