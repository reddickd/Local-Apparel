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
import java.math.*;
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

public class MarketPlaceActivity extends AppCompatActivity {
    TextView textViews, textViews2;
    DatabaseReference fire;
    FireBaseBackEnd backEnd;
    FirebaseStorage storage;
    StorageReference storeRef;
    ArrayList<Item> listingItems;
    User currentUser;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    ListView listView;
    CustomAdapter adapter;
    private DrawerLayout mDrayerlayout;
    private ActionBarDrawerToggle mToggle;
    SearchView search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market_place);
        backEnd = new FireBaseBackEnd(FirebaseDatabase.getInstance());
        fire = FirebaseDatabase.getInstance().getReference();
        storeRef = FirebaseStorage.getInstance().getReference();
        listingItems = new ArrayList<Item>();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        search = findViewById(R.id.searchb);

        //Whenever a new item is added, or changes in item properties
        fire.child("items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                listingItems.clear();
                for (DataSnapshot child : children) {
                    Item item = child.getValue(Item.class);
                    listingItems.add(item);
                }
                //ListView Custom Adapter
                listView = (ListView) findViewById(R.id.listItem);
                adapter = new CustomAdapter(MarketPlaceActivity.this, R.layout.customlistinglayout, listingItems);
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        fire.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot child : children) {
                    User user = child.getValue(User.class);
                    if(user.getUid().equals(firebaseUser.getUid())){
                        currentUser = user;
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                ArrayList<Item> tempList = new ArrayList<Item>();
                if (query.length() != 0) {
                    for (Item element : listingItems) {
                        if (element.getName().contains(query)) {
                            tempList.add(element);
                        }
                    }
                    adapter = new CustomAdapter(MarketPlaceActivity.this, R.layout.customlistinglayout, tempList);
                    listView.setAdapter(adapter);
                } else {
                    adapter = new CustomAdapter(MarketPlaceActivity.this, R.layout.customlistinglayout, listingItems);
                    listView.setAdapter(adapter);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
/*
        // DRAWER CODE PART IF YOU WANT TO USE COMMENT THIS OUT ADD IN THE DRAWER ID INTO THE LAYOUT FILE
        //Drawer Navigational Drawer
        mDrayerlayout = (DrawerLayout)findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this,mDrayerlayout,R.string.open,R.string.close);
        mDrayerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
*/

        //ListView Custom Adapter
        //listingItems = backEnd.getItems();
        //listView = (ListView)findViewById(R.id.listItem);
        //THIS HERE BELOW WAS A DUMMY TEST TO SEE IF MY FUNCTIONS WORK, JUST NEED TO ASSIGN LISTINGITEMS THE CORRECT ITEMS ARRAYLIST

        //adapter = new CustomAdapter(this,R.layout.customlistinglayout,listingItems);
        //listView.setAdapter(adapter);
        //Button popUpB = (Button) findViewById(R.id.pop_window);

    }

    //Attempt to create Search Bar for on text submit and text change, needs to implement custom filter function
    // I need help with this.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) item.getActionView();

        //Submit enter text input
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ArrayList<Item> tempList = new ArrayList<Item>();
                if (query.length() != 0) {
                    for (Item element : listingItems) {
                        if (element.getName().contains(query)) {
                            tempList.add(element);
                        }
                    }
                    adapter = new CustomAdapter(MarketPlaceActivity.this, R.layout.customlistinglayout, tempList);
                    listView.setAdapter(adapter);
                } else {
                    adapter = new CustomAdapter(MarketPlaceActivity.this, R.layout.customlistinglayout, listingItems);
                    listView.setAdapter(adapter);
                }
                return false;//maybe false
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        //return super.onCreateOptionsMenu(menu);
        return true;
    }

    //Filter Button is utilized ONLY IF NAVIGATION BAR IS NOT IN PLACE. I implemented two approaches
    public void showFilterOptionButton(View view) {
        Intent intent = new Intent(this, FeatureFilterPopWindow.class);
        startActivityForResult(intent, 2);
    }
    public void showResetButton(View view){
        CustomAdapter adapter = new CustomAdapter(this, R.layout.customlistinglayout, listingItems);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2 && resultCode == RESULT_OK) {
            ArrayList<Item> tempItemsList = new ArrayList<Item>();
            tempItemsList = filterItemList(data, listingItems);
            CustomAdapter adapter = new CustomAdapter(this, R.layout.customlistinglayout, tempItemsList);
            listView.setAdapter(adapter);
            Log.d("Hi","Went here");
        }
    }


    public ArrayList<Item> filterItemList(Intent data, ArrayList<Item> copiedOverList) {
        ArrayList<Item> tempNewList = new ArrayList<Item>();

        String brand, city, lowRange, highRange, radius, size;
        Boolean aCategoryClearedList = false;
        Boolean noFilterOptionClicked = true;
        brand = data.getStringExtra("brand");

        if (!brand.isEmpty()) {
            noFilterOptionClicked = false;
            Log.d("Hi","Reached Brand Section");
            for (Item element : copiedOverList) {
                if (element.getBrand().equals(brand)) {
                    //Log.d("Hi",element.getBrand().equals(brand)+element.getBrand()+" "+brand+" ");
                    tempNewList.add(element);
                }

            }
            if(tempNewList.isEmpty()){
                aCategoryClearedList = true;
            }
        }
        // Log.d("Hi","The Item list brand"+ tempNewList.get(0).getBrand());
            /*
            city = data.getStringExtra("city");
            if(!city.isEmpty()){
                for(Item element: copiedOverList){
                    if(element.getCity() != city){
                        copiedOverList.remove(element);
                    }
                }
            }
            */
        radius = data.getStringExtra("radius");
        if (radius != null && aCategoryClearedList == false) {
            double radiusVal = Double.parseDouble(radius);
            noFilterOptionClicked = false;
            if (tempNewList.isEmpty()) { //Assume nothing has been added to the temp list
                for (Item element:copiedOverList) {
                    double miles = Math.hypot(currentUser.getLatitude()-Double.parseDouble(element.getLatitude())
                            ,currentUser.getLongitude()-Double.parseDouble(element.getLatitude()));
                    if ( miles <= radiusVal){
                        //currentUser.getLatitude();
                        //currentUser.getLongitude();
                        tempNewList.add(element);
                    }
                }
                if (tempNewList.isEmpty()) {
                    aCategoryClearedList = true;
                }
            } else {
                for (Item element : tempNewList) {
                    double miles = Math.hypot(currentUser.getLatitude()-Double.parseDouble(element.getLatitude())
                            ,currentUser.getLongitude()-Double.parseDouble(element.getLongitude()));
                    if (miles > radiusVal){
                        //currentUser.getLatitude();
                        //currentUser.getLongitude();
                        tempNewList.remove(element);
                    }
                }
                if (tempNewList.isEmpty()) {
                    aCategoryClearedList = true;
                }
            }
        }


        lowRange = data.getStringExtra("lowRange");
        highRange = data.getStringExtra("highRange");
        if (!lowRange.isEmpty() && !highRange.isEmpty() && aCategoryClearedList == false) {
            noFilterOptionClicked = false;
            Log.d("Hi","REACHED THE RANGE SECTION");
            if (tempNewList.isEmpty()) { //Assume nothing has been added to the temp list
                for (Item element : copiedOverList) {
                    if (Integer.parseInt(element.getPrice()) >= Integer.parseInt(lowRange)
                            && Integer.parseInt(element.getPrice()) <= Integer.parseInt(highRange)) {
                        tempNewList.add(element);
                    }
                }
                if(tempNewList.isEmpty()){
                    aCategoryClearedList = true;
                }
            } else { //Already items in temp list
                for (Item element : tempNewList) {
                    if (Integer.parseInt(element.getPrice()) < Integer.parseInt(lowRange)
                            || Integer.parseInt(element.getPrice()) > Integer.parseInt(highRange)) {
                        tempNewList.remove(element);
                    }
                }
                if(tempNewList.isEmpty()) {
                    aCategoryClearedList = true;
                }
            }
        }

        //String smallSize = data.getStringExtra("checkS");
        //String mediumSize = data.getStringExtra("checkM");
        //String largeSize = data.getStringExtra("checkL");
        //String xlargeSize = data.getStringExtra("checkXL");
        ArrayList<String> listSizes = new ArrayList<String>();
        listSizes = data.getStringArrayListExtra("size");

        //if(!smallSize.isEmpty())
        //    listSizes.add(smallSize);
        //if(!mediumSize.isEmpty())
        //    listSizes.add(mediumSize);
        //if(!largeSize.isEmpty())
        //    listSizes.add(largeSize);
        //if(!xlargeSize.isEmpty())
        //    listSizes.add(xlargeSize);


        //Log.d("Hi","size of list" + String.valueOf(listSizes.size()));

        if (listSizes != null && aCategoryClearedList == false) {
            noFilterOptionClicked = false;
            Log.d("Hi","REACHed CheckSize");
            if (!tempNewList.isEmpty()) {
                for (Item element : tempNewList) {
                    if (!listSizes.contains(element.getSize())) {
                        tempNewList.remove(element);
                    }
                }

                if (tempNewList.isEmpty()) {
                    aCategoryClearedList = true;
                }
            } else { //Nothing has been adding to temp list
                for (Item element : copiedOverList) {
                    if (listSizes.contains(element.getSize())) {
                        tempNewList.add(element);
                    }
                }
                if(tempNewList.isEmpty()) {
                    aCategoryClearedList = true;
                }
            }
        }
        //Log.d("Hi","NewUsed");
        String conditionNew = data.getStringExtra("new");
        //Log.d("Hi","Hello" + conditionNew);
        String conditionUsed = data.getStringExtra("used");
        //Log.d("Hi","NewUsed3333333");

        ArrayList<String> conditionList = new ArrayList<String>();
        conditionList.clear();
        //Log.d("Hi","NewUsed4444");
        if(conditionNew != null)
            conditionList.add(conditionNew);
        //Log.d("Hi","NewUsed666666");
        if(conditionUsed != null)
            conditionList.add(conditionUsed);
        // Log.d("Hi","Condition1: " + conditionUsed);
        //Log.d("Hi","Condition1: " + conditionNew);
        if (!conditionList.isEmpty() && aCategoryClearedList == false) {
            noFilterOptionClicked = false;
            if (tempNewList.isEmpty()) {
                for (Item element : copiedOverList) {
                    if (conditionList.contains(element.getCondition())) {
                        Log.d("Hi","ConditionLoop: " + conditionUsed);
                        Log.d("Hi","ConditionLoop: " + conditionNew);
                        tempNewList.add(element);
                    }
                }
                if (tempNewList.isEmpty()) {
                    aCategoryClearedList = true;
                }
            } else {
                for (Item element : tempNewList) {
                    if (!conditionList.contains(element.getCondition())) {
                        tempNewList.remove(element);
                    }
                }
                if (tempNewList.isEmpty()) {
                    aCategoryClearedList = true;
                }
            }
        }
        //add reset button!!!
        ArrayList<String> categoryList = data.getExtras().getStringArrayList("category");
        if (categoryList != null && categoryList.size() != 0 && aCategoryClearedList == false) {
            noFilterOptionClicked = false;
            Log.d("Hi","List of item:" + categoryList.get(0));
            if (tempNewList.isEmpty()) { // Nothing has been added to the list
                for (Item element : copiedOverList) {
                    if (categoryList.contains(element.getCategory())) {
                        tempNewList.add(element);
                    }
                }
                if (tempNewList.isEmpty()) {
                    aCategoryClearedList = true;
                }
            } else { //Temp list is not empty
                for (Item element : tempNewList) {
                    if (!categoryList.contains(element.getCategory())) {
                        tempNewList.remove(element);
                    }
                }
                if (tempNewList.isEmpty()) {
                    aCategoryClearedList = true;
                }
            }

        }
        if(tempNewList.isEmpty() && noFilterOptionClicked == false){
            Toast.makeText(MarketPlaceActivity.this, "There are no items under these conditions.",
                    Toast.LENGTH_LONG).show();
            return copiedOverList;
        }
        if(noFilterOptionClicked == true) {// no filter option was clicked
           // Toast.makeText(MarketPlaceActivity.this, "NOOOOOOOO!!!", Toast.LENGTH_LONG).show();
            return copiedOverList;
        }
        //Log.d("Hi","The Item list brand"+ tempNewList.get(0).getBrand());
        //Toast.makeText(MarketPlaceActivity.this, "HERE ARE THE CURRENT ITEMS YOU REQUESTED!!!",Toast.LENGTH_LONG).show();
        return tempNewList;
    }

    //Custom Adapter for List View
    class CustomAdapter extends ArrayAdapter<Item> {
        List<Item> itemList;
        Context context;
        int resource;

        public CustomAdapter(Context context, int resource, List<Item> itemList) {
            super(context, resource, itemList);
            this.context = context;
            this.resource = resource;
            this.itemList = itemList;
        }

        //might take out
        public int getCount() {
            return itemList.size();
        }

        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(resource, null, false);
            final ImageView imageView = view.findViewById(R.id.imageView);
            TextView name = view.findViewById(R.id.name);
            TextView size = view.findViewById(R.id.size);
            TextView brand = view.findViewById(R.id.brand);
            TextView price = view.findViewById(R.id.price);
            TextView condition = view.findViewById(R.id.condition);
            final Item item = itemList.get(position);


            name.setText(String.valueOf(item.getName()));
            size.setText(item.getSize());
            brand.setText(item.getBrand());
            price.setText(item.getPrice());
            condition.setText(item.getCondition());

            storeRef.child("images").child(item.getUserID()+item.getId()).getBytes(1024 * 1024).addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imageView.setImageBitmap(bmp);
                    Log.d("TEST", "downloaded picture");
                }
            });


            // GO TO ANOTHER ACTIVITY FOR FULL DETAIL OF THE ITEM CLICKED, SET A ONCLICK LISTENER
            // FOR THE IMAGEVIEW AND SENT THE CONTENTS OF ITEM OBJECT TO THE NEW ACTIVITY.
            // THIS MIGHT BE BROKEN GOING TO THE NEXT ACTIVITY. MIGHT BE A SMALL FIX
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toFullItemPage = new Intent(context, SingleItemActivity.class);
                    toFullItemPage.putExtra("SingleItem", item);
                    startActivity(toFullItemPage);
                }
            });

            return view;
        }
    }


}
