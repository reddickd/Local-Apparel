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
import com.google.firebase.database.Query;
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
    List<Item> allItems;
    Bitmap bmp; //used to store and return a bitmap for the image you want

    public FireBaseBackEnd(FirebaseDatabase fire){
        this.fire = fire;
        allUsers = new ArrayList<User>();
        allItems = new ArrayList<Item>();

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
        ref.child("items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                allItems.clear();
                for (DataSnapshot i : children) {
                    Item item = i.getValue(Item.class);
                    allItems.add(item);
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

    public Item getItem(String userId, String itemName){
        for(Item i: allItems){
            if(i.getUserID().equals(userId) && i.getName().equals(itemName)){
                return i;
            }
        }
        return null;
    }
//idk if either delete works right now
    public void deleteItem(String userId, String itemName){
        for(Item i: allItems) {
            if (i.getUserID().equals(userId) && i.getName().equals(itemName)) {
                allItems.remove(i);
                Query toDelete = ref.child("items").equalTo(itemName);
                toDelete.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        }
    }

    public void deleteItem(Item item){
        allItems.remove(item);
        Query toDelete = ref.child("items").equalTo(item.itemName);
        toDelete.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void listItem(String item, String url, String name, String userID, String brand, String description,String category,String condition,String size, String price, String latitude, String longitude){

        if(ref.child("items") != null){
           // id = getItemCount();
            id = allItems.size();
        }
       ref.child("items").child(item).setValue(new Item(id,url,item,userID,brand,description,category,condition,size,price,latitude,longitude,name));

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


