package com.example.cmsc436.localapparel.Objects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Debug;
import android.util.Log;
import android.widget.Toast;

import com.example.cmsc436.localapparel.Activities.MainPage;
import com.example.cmsc436.localapparel.Objects.Item;
import com.example.cmsc436.localapparel.Objects.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class FireBaseBackEnd {
    int id;
    FirebaseDatabase fire;
    DatabaseReference ref;
    StorageReference storageReference;
    List<User> allUsers;
    Bitmap bmp; //used to store and return a bitmap for the image you want

    public FireBaseBackEnd(FirebaseDatabase fire){
        this.fire = fire;
        allUsers = new ArrayList<User>();

        ref = fire.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        if(ref.child("items") != null){
            id = getItemCount();
        }else{
            id = 0;
        }

        ref.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                allUsers.clear();
                for (DataSnapshot child : children) {
                    User user = child.getValue(User.class);
                    allUsers.add(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    public void addUser(User user){
        ref.child("users").child(user.getUid()).setValue(user);
    }

    public User getCurrentUser(final String userID){
        for(User u : allUsers){
            if(u.getUid().equals(userID)){
                return u;
            }
        }
        return null;
    }

    public void listItem(String item, String url, String userID, String brand, String description,String category,String condition,String size, String price){

        if(ref.child("items") != null){
            id = getItemCount();
        }
       ref.child("items").child(item).setValue(new Item(id,url,userID,brand,description,category,condition,size,price));

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

    public void saveProfilePicture(Uri imageURI, String fileName){

        storageReference.child("profilePictures").child(fileName).putFile(imageURI);
    }

    //Does not work due to synchroniztion.  will return null before onsuccess finishes
//    public Bitmap getProfilePicture(String fileName){
//
//        storageReference.child("profilePictures").child(fileName).getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//            @Override
//            public void onSuccess(byte[] bytes) {
//                bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                Log.d("TEST", "downloaded picture");
//            }
//        });
//
//        return bmp;
//    }
}


