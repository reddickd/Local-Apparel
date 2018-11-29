package com.example.cmsc436.localapparel.Activities;

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

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.content.Context;

import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Switch;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import android.app.Notification;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import com.google.firebase.storage.StorageException;

import android.content.Intent;
import android.os.Debug;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.view.LayoutInflater;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;
import android.net.Uri;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import java.io.FileNotFoundException;
import com.google.android.play.core.tasks.OnFailureListener;
import com.google.android.play.core.tasks.OnSuccessListener;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class MainPage extends AppCompatActivity implements LocationListener{

    private Object dropdownSelected,sizeDropDownSelected;
    String downloadURL = "",conditionString = "", latitude, longitude;
    FirebaseDatabase fire;
    FireBaseBackEnd backEnd;
    FirebaseStorage storage;
    StorageReference storeRef;
    Button sendMessageButton, listItemButton;
    List<User> allUsers;
    FirebaseAuth mAuth;
    FirebaseUser user;
    User currNonFirebaseUser;
    public static final int GET_FROM_GALLERY = 3;
    ImageView testProfilePic;
    protected LocationManager locationManager;
    protected LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //parent firebase database reference
        fire = FirebaseDatabase.getInstance("https://localapparel-96283.firebaseio.com/");
        storage = FirebaseStorage.getInstance("gs://localapparel-96283.appspot.com");
        storeRef = storage.getReference();
        backEnd = new FireBaseBackEnd(fire);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();


        sendMessageButton = (Button) findViewById(R.id.send_message);
        listItemButton = (Button) findViewById(R.id.list_item);

        testProfilePic = findViewById(R.id.display_profile_picture);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        }

    }

    public void sendMessagePressed(View view){
        startActivity(new Intent(MainPage.this, MessageListActivity.class));
    }

    public void goToMarketplace(View view){
        startActivity(new Intent(MainPage.this, MarketPlaceActivity.class));
    }

    public void listItemPressed(View view){
        //latitude and longitude
        //make it so clicking off doesnt close popup

        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window,null);
        final Button submitItemButton  = (Button) popupView.findViewById(R.id.submitItem);
        final EditText itemName = (EditText)popupView.findViewById(R.id.itemName);
        final EditText brandName = (EditText)popupView.findViewById(R.id.brand);
        final EditText description = (EditText)popupView.findViewById(R.id.description);
        final EditText price = (EditText) popupView.findViewById(R.id.price);
        final Spinner spinner = (Spinner) popupView.findViewById(R.id.category);
        final Spinner sizeSpinner = (Spinner) popupView.findViewById(R.id.size);
        final Switch conidtionSwitch = (Switch) popupView.findViewById(R.id.condition);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(this,
                R.array.sizes, android.R.layout.simple_spinner_item);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(sizeAdapter);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.setOutsideTouchable(false);
        popupWindow.update();


        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dropdownSelected = adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //make someone select
            }
        });

        sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sizeDropDownSelected = adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //make someone select
            }
        });


        if(conidtionSwitch.isChecked()){
            conditionString = "Used";
        }else {
            conditionString = "New";
        }

        submitItemButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                if(itemName.getText().toString().equals("")||brandName.getText().toString().equals("")||description.getText().toString().equals("")||price.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Please fill out all fields",Toast.LENGTH_LONG).show();
                }else {
                    backEnd.listItem(itemName.getText().toString(), storeRef.child("images").child(user.getUid()).getDownloadUrl().toString(),itemName.getText().toString(), user.getUid(), brandName.getText().toString(),
                            description.getText().toString(), dropdownSelected.toString(), conditionString, sizeDropDownSelected.toString(), price.getText().toString(),latitude,longitude);
                    popupWindow.dismiss();
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] byteData = baos.toByteArray();
            currNonFirebaseUser = backEnd.getCurrentUser(user.getUid());
            currNonFirebaseUser.incrementNumItems();
            Integer numItems = currNonFirebaseUser.getnumItems();
            UploadTask uploadTask = storeRef.child("images").child(user.getUid()).child(numItems.toString()).putBytes(byteData);
            //uploadTask.
            if(uploadTask.isComplete()){

            }
//            uploadTask.addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                    int errorCode = ((StorageException) exception).getErrorCode();
//                    String errorMessage = exception.getMessage();
//
//                    exception.printStackTrace();
//                }
//            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                   // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
//                    // ...
//
//                }
//            });
        }

    }


        //Just used this to see how getting images would work.  accesses storage and gets user profile picture
        private void displayProfilePicture(){
            storeRef.child("profilePictures").child(user.getUid()).getBytes(1024*1024).addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    testProfilePic.setImageBitmap(bmp);
                    Log.d("TEST", "downloaded picture");
                }
            });

        }

//    public class ShowPopUp extends Activity {
//
//        PopupWindow popUp;
//        LinearLayout layout;
//        TextView tv;
//        LayoutParams params;
//        LinearLayout mainLayout;
//        Button but;
//        boolean click = true;
//
//        @Override
//        public void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            popUp = new PopupWindow(this);
//            layout = new LinearLayout(this);
//            mainLayout = new LinearLayout(this);
//            tv = new TextView(this);
//            but = new Button(this);
//            but.setText("Click Me");
//            but.setOnClickListener(new OnClickListener() {
//
//                public void onClick(View v) {
//                    if (click) {
//                        popUp.showAtLocation(layout, Gravity.BOTTOM, 10, 10);
//                        popUp.update(50, 50, 300, 80);
//                        click = false;
//                    } else {
//                        popUp.dismiss();
//                        click = true;
//                    }
//                }
//
//            });
//            params = new LayoutParams(LayoutParams.WRAP_CONTENT,
//                    LayoutParams.WRAP_CONTENT);
//            layout.setOrientation(LinearLayout.VERTICAL);
//            tv.setText("Hi this is a sample text for popup window");
//            layout.addView(tv, params);
//            popUp.setContentView(layout);
//             popUp.showAtLocation(layout, Gravity.BOTTOM, 10, 10);
//            mainLayout.addView(but, params);
//            setContentView(mainLayout);
//        }
    //}
    @Override
    protected void onStart(){
        super.onStart();

        displayProfilePicture();
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                //myRef.setValue("Hello World!");
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        submitButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                myRef.setValue(userName.getText().toString());
//            }
//        });

    }
    public final static  int MY_PERMISSIONS_LOCATION= 4;
    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.ACCESS_FINE_LOCATION") != PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.ACCESS_COARSE_LOCATION") != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainPage.this,
                    new String[]{"android.permission.ACCESS_FINE_LOCATION",
                            "android.permission.ACCESS_COARSE_LOCATION"},
                    MY_PERMISSIONS_LOCATION);
        }else
            getLocationUpdates();

    }

    public void getLocationUpdates(){
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            String p1 = locationManager.getProvider(LocationManager.GPS_PROVIDER).getName();
           // String p2 = locationManager.getProvider(LocationManager.NETWORK_PROVIDER).getName();
            Location location = locationManager.getLastKnownLocation(p1);
            //Location location2 = locationManager.getLastKnownLocation(p2);
           // if (location != null || location2 != null) {
            if (location != null){
                Double latDouble = location.getLatitude();
                latitude = latDouble.toString();
                Double longDouble = location.getLongitude();
                longitude = longDouble.toString();
            }
        }
    }

    public void setOtherLocation(){
        //give user ability to set different location for item
    }
    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }
    @Override
    public void onLocationChanged(Location location) {
        Double latitudeDouble = location.getLatitude();
        Double longitudeDouble = location.getLongitude();
        latitude = latitudeDouble.toString();
        longitude = longitudeDouble.toString();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_LOCATION:
                int g = 0;
                //Log.d(TAG,"Perm?: "+permissions.length+" -? "+grantResults.length);
                for(String perm: permissions)
                    //Log.d(TAG,"Perm: "+perm+" --> "+grantResults[g++]);
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getLocationUpdates();
                }
                else {
                    //Log.i(TAG, "Permission was not granted to access location");
                    Toast.makeText(getApplicationContext(),"Permission was not granted to access location",Toast.LENGTH_LONG).show();
                    finish();
                }
        }
    }



}
