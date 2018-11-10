package com.example.cmsc436.localapparel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.app.Activity;
import android.widget.LinearLayout;

import android.view.LayoutInflater;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class MainPage extends AppCompatActivity{

    FirebaseDatabase fire;
    FireBaseBackEnd backEnd;
    Button sendMessageButton, listItemButton;
    FirebaseAuth mAuth;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //parent firebase database reference
        fire = FirebaseDatabase.getInstance("https://localapparel-96283.firebaseio.com/");
        backEnd = new FireBaseBackEnd(fire);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        sendMessageButton = (Button) findViewById(R.id.send_message);
        listItemButton = (Button) findViewById(R.id.list_item);



    }

    public void sendMessagePressed(View view){
        //backEnd.sendMessage();
    }

    public void listItemPressed(View view){
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window,null);
        final Button submitItemButton  = (Button) popupView.findViewById(R.id.submitItem);
        final EditText itemName = (EditText)popupView.findViewById(R.id.itemName);
        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


        submitItemButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                backEnd.listItem(itemName.getText().toString());
                popupWindow.dismiss();
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
//    @Override
//    protected void onStart(){
//        super.onStart();
//
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
//
//    }



}
