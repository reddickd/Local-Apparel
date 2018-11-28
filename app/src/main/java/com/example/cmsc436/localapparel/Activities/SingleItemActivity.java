package com.example.cmsc436.localapparel.Activities;
import com.example.cmsc436.localapparel.Objects.Chat;
import com.example.cmsc436.localapparel.Objects.FireBaseBackEnd;
import com.example.cmsc436.localapparel.Objects.User;
import com.example.cmsc436.localapparel.R;
import com.google.firebase.database.FirebaseDatabase;
import android.Manifest;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import java.util.*;

import android.support.v7.app.ActionBarDrawerToggle;
import android.widget.ArrayAdapter;
import android.view.ViewGroup;
import android.support.v7.app.AppCompatActivity;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_item_page);
        Intent receivedIntent = getIntent();
        Item item = (Item)receivedIntent.getSerializableExtra("SingleItem");
        storeRef = FirebaseStorage.getInstance().getReference();
        Log.i(TAG, "We've entered the view where we want to display the details of the item");

        name = findViewById(R.id.name);
        brand = findViewById(R.id.brand);
        description = findViewById(R.id.description);
        price = findViewById(R.id.price);
        picOfItem = findViewById(R.id.detailDisplayPic);

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
        
    }


}
