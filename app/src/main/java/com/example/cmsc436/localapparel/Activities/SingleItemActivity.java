package com.example.cmsc436.localapparel.Activities;
import com.example.cmsc436.localapparel.Objects.Chat;
import com.example.cmsc436.localapparel.Objects.FireBaseBackEnd;
import com.example.cmsc436.localapparel.Objects.User;
import com.example.cmsc436.localapparel.R;
import com.google.firebase.database.FirebaseDatabase;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import java.util.*;
import com.example.cmsc436.localapparel.Objects.Chat;
import com.example.cmsc436.localapparel.Objects.FireBaseBackEnd;
import com.example.cmsc436.localapparel.Objects.Item;
import com.example.cmsc436.localapparel.Objects.Message;
import com.example.cmsc436.localapparel.Objects.User;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.view.ViewGroup;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.ImageView;
import android.widget.Toast;
import com.example.cmsc436.localapparel.Objects.Item;
import android.content.Context;


import com.example.cmsc436.localapparel.Objects.FireBaseBackEnd;
import com.example.cmsc436.localapparel.Objects.User;
import com.example.cmsc436.localapparel.R;
import com.google.firebase.database.FirebaseDatabase;
import android.widget.ListView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.cmsc436.localapparel.Objects.FireBaseBackEnd;
import com.example.cmsc436.localapparel.Objects.User;
import com.example.cmsc436.localapparel.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.support.v4.widget.DrawerLayout;
//import android.support.v7.widget.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.app.AlertDialog;

import com.example.cmsc436.localapparel.R;

public class SingleItemActivity extends AppCompatActivity {

    /*
    NOT YET FULLY IMPLEMENTED BUT ALL IT NEEDS TO DO IS GET THE ITEM OBJECT AND SET ALL THE TEXTVIEW WIDGETS
    that was sent from the markeplaceActivity.
     */

    private String TAG = "SingleItemActivity";
    private TextView name, brand, description, price;
    private ImageView picOfItem;
    StorageReference storeRef;
    DatabaseReference ref;
    List<User> allUsers;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FireBaseBackEnd backEnd;
    User temp;
    Item item;
    User seller;
    List<Chat> chatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_item_page);
        Intent receivedIntent = getIntent();
        item = (Item)receivedIntent.getSerializableExtra("SingleItem");
        storeRef = FirebaseStorage.getInstance().getReference();
        backEnd = new FireBaseBackEnd(FirebaseDatabase.getInstance());
        Log.i(TAG, "We've entered the view where we want to display the details of the item");

        ref = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        name = findViewById(R.id.name);
        brand = findViewById(R.id.brand);
        description = findViewById(R.id.description);
        price = findViewById(R.id.price);
        picOfItem = findViewById(R.id.detailDisplayPic);

        allUsers = new ArrayList<User>();
        FirebaseUser seller;

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



        storeRef.child("images").child(item.getDownloadURL()).getBytes(1024*1024).addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                picOfItem.setImageBitmap(bmp);
                Log.d("TEST", "downloaded picture");
            }
        });

        name.setText("Seller: " + String.valueOf(item.getName()));
        brand.setText(item.getBrand());
        description.setText("Description of item: " + item.getDescription());
        price.setText(item.getPrice());

    }

    public void sendMessageToBuyItem(View view){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        for(User u : allUsers){
            if(u.getUid().equals(item.getUserID())){
                seller = u;
            }
            if (u.getUid().equals(user.getUid())) {
                temp = u;
            }
        }
        alert.setTitle("Send message to " + seller.getEmail());


        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //make theres text in the box

                int hours, minutes;
                String amOrPM, timeStamp = null;

                hours = java.time.LocalTime.now().getHour();
                if(hours > 12){
                    hours = hours - 12;
                    amOrPM = "PM";
                }else{
                    amOrPM = "AM";
                }
                minutes = java.time.LocalTime.now().getMinute();

                timeStamp = Integer.toString(hours) + ":" + Integer.toString(minutes) + " " + amOrPM;

                Message m = new Message(user.getUid(), input.getText().toString(), timeStamp);
                List<Message> messages = new ArrayList<Message>();
                messages.add(m);
                Chat newChat = new Chat(user.getUid(), seller.getUid(), item.getName());

                newChat.addMessage(m);

                temp.addChat(newChat);
                seller.addChat(newChat);

                backEnd.addUser(temp);
                backEnd.addUser(seller);

                // Do something with value!
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }


}
