package com.example.cmsc436.localapparel.Activities;
import com.example.cmsc436.localapparel.Objects.Chat;
import com.example.cmsc436.localapparel.Objects.FireBaseBackEnd;
import com.example.cmsc436.localapparel.Objects.User;
import com.example.cmsc436.localapparel.R;
import com.google.firebase.database.FirebaseDatabase;
import android.Manifest;
import android.content.Intent;

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

public class MarketPlaceActivity extends AppCompatActivity {
    TextView textViews,textViews2;
    FirebaseDatabase fire;
    FireBaseBackEnd backEnd;
    FirebaseStorage storage;
    StorageReference storeRef;
    ArrayList<Item> listingItems;
    ListView listView;
    CustomAdapter adapter;
    private DrawerLayout mDrayerlayout;
    private ActionBarDrawerToggle mToggle;




    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market_place);

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
        listingItems = new ArrayList<Item>();
        listView = (ListView)findViewById(R.id.listItem);
        //THIS HERE BELOW WAS A DUMMY TEST TO SEE IF MY FUNCTIONS WORK, JUST NEED TO ASSIGN LISTINGITEMS THE CORRECT ITEMS ARRAYLIST

        listingItems.add(new Item(54,"Gucci","UserID"));
        listingItems.add(new Item(60,"LV","USERID"));
        listingItems.add(new Item(64,"Prada","USERID"));

        adapter = new CustomAdapter(this,R.layout.customlistinglayout,listingItems);
        listView.setAdapter(adapter);
        //Button popUpB = (Button) findViewById(R.id.pop_window);

    }

    //Attempt to create Search Bar for on text submit and text change, needs to implement custom filter function
    // I need help with this.
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView)item.getActionView();


        //control enter
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    //Filter Button is utilized ONLY IF NAVIGATION BAR IS NOT IN PLACE. I implemented two approaches
    public void showFilterOptionButton(View view){
        Intent intent = new Intent(this,FeatureFilterPopWindow.class);
        startActivityForResult(intent, 2);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 2 && resultCode == RESULT_OK){
            // listingItems = filterItemList(data, listingItems);
            //CustomAdapter adapter = new CustomAdapter(this,R.layout.customlistinglayout,listingItems);
            //listView.setAdapter(adapter);

        }
    }
    /*
        // NEED TO COMMENT OUT ONCE THE ITEM CLASS HAS ALL THE NECESSARY FIELDS AND GET FUNCTIONS
        public ArrayList<Item> filterItemList(Intent data, ArrayList<Item>copiedOverList){
            String brand,city,lowRange,highRange, radius,size;


            brand = data.getStringExtra("brand");
            if(!brand.isEmpty()){
                for(Item element: copiedOverList){
                    if(element.getBrand() != brand){
                        copiedOverList.remove(element);
                    }
                }
            }
            city = data.getStringExtra("city");
            if(!city.isEmpty()){
                for(Item element: copiedOverList){
                    if(element.getCity() != city){
                        copiedOverList.remove(element);
                    }
                }
            }
            radius = data.getStringExtra("radius");
            if(!radius.isEmpty()){
                for(Item element: copiedOverList){

                }
            }

            lowRange = data.getStringExtra("lowRange");
            highRange = data.getStringExtra("highRange");
            if(!lowRange.isEmpty() && !highRange.isEmpty()){
                for(Item element: copiedOverList){
                    if(element.getPrice() < lowRange || element.getPrice() > highRange){
                        copiedOverList.remove(element);
                    }
                }
            }

            String smallSize = data.getStringExtra("checkS");
            if(!smallSize.isEmpty()){
                for(Item element: copiedOverList){
                    if(element.getSize() != smallSize){
                        copiedOverList.remove(element);
                    }
                }
            }

            String mediumSize = data.getStringExtra("checkM");
            if(!mediumSize.isEmpty()){
                for(Item element: copiedOverList){
                    if(element.getSize() != mediumSize){
                        copiedOverList.remove(element);
                    }
                }
            }
            String largeSize = data.getStringExtra("checkL");
            if(!largeSize.isEmpty()){
                for(Item element: copiedOverList){
                    if(element.getSize() != largeSize){
                        copiedOverList.remove(element);
                    }
                }
            }
            String xlargeSize = data.getStringExtra("checkXL");
            if(!smallSize.isEmpty()){
                for(Item element: copiedOverList){
                    if(element.getSize() != xlargeSize){
                        copiedOverList.remove(element);
                    }
                }
            }

            String conditionNew = data.getStringExtra("new");
            if(!conditionNew.isEmpty()){
                for(Item element: copiedOverList){
                    if(element.getCondition() != conditionNew){
                        copiedOverList.remove(element);
                    }
                }
            }
            //data.getStringExtra("used");

            String conditionUsed = data.getStringExtra("used");
            if(!conditionUsed.isEmpty()){
                for(Item element: copiedOverList){
                    if(element.getCondition() != conditionUsed){
                        copiedOverList.remove(element);
                    }
                }
            }
            ArrayList<String> categoryList = data.getExtras().getStringArrayList("category");
            if(!data.getExtras().getStringArrayList("category").isEmpty()){
                for(Item element: copiedOverList){
                    if(!element.getCategory().contains(categoryList)){
                        copiedOverList.remove(element);
                    }
                }
            }

            Log.d("TAG", brand);

            return copiedOverList;
        }
    */
    //Custom Adapter for List View
    class CustomAdapter extends ArrayAdapter<Item>{
        List<Item> itemList;
        Context context;
        int resource;

        public CustomAdapter(Context context,int resource, List<Item> itemList){
            super(context,resource,itemList);
            this.context=context;
            this.resource = resource;
            this.itemList = itemList;
        }
        //might take out
        public int getCount(){
            return itemList.size();
        }
        // @Override
        //public void getFilter(String text){

        //}
        public long getItemId(int i){
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(resource,null,false);
            ImageView imageView = view.findViewById(R.id.imageView);
            TextView name = view.findViewById(R.id.name);
            TextView size = view.findViewById(R.id.size);
            TextView brand = view.findViewById(R.id.brand);
            TextView price = view.findViewById(R.id.price);
            TextView condition = view.findViewById(R.id.condition);
            final Item item = itemList.get(position);


            name.setText(String.valueOf(item.getCount()));
            size.setText(item.getDownloadURL());
            //imageView.setImageURI();

            // GO TO ANOTHER ACTIVITY FOR FULL DETAIL OF THE ITEM CLICKED, SET A ONCLICK LISTENER
            // FOR THE IMAGEVIEW AND SENT THE CONTENTS OF ITEM OBJECT TO THE NEW ACTIVITY.
            // THIS MIGHT BE BROKEN GOING TO THE NEXT ACTIVITY. MIGHT BE A SMALL FIX
            imageView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent toFullItemPage = new Intent(context,SingleItemActivity.class);
                    toFullItemPage.putExtra("SingleItem",item);
                    startActivity(toFullItemPage);
                }
            });

            return view;
        }
    }



}
