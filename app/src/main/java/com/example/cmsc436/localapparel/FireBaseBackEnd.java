package com.example.cmsc436.localapparel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class FireBaseBackEnd {
    int id;
    FirebaseDatabase fire;
    DatabaseReference ref;

    public FireBaseBackEnd(FirebaseDatabase fire){
        this.fire = fire;

        ref = fire.getInstance().getReference();
        if(ref.child("items") != null){
            id = getItemCount();
        }else{
            id = 0;
        }

    }

    public void addUser(String email, String password, String uid){

        User userToAdd = new User(email,password,uid);
        ref.child("users").child(uid).setValue(userToAdd);


    }

    public void listItem(String item){

        if(ref.child("items") != null){
            id = getItemCount();
        }
        ref.child("items").child(item).setValue(new Item(id));

    }

    public int getItemCount(){
        ref.child("items").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                id = 0;
                for(DataSnapshot child : dataSnapshot.getChildren() ){
                    id++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        return id;
    }


}


