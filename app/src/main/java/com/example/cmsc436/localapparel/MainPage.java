package com.example.cmsc436.localapparel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class MainPage extends AppCompatActivity{
    // Write a message to the database

    DatabaseReference myRef;
    EditText userName;
    TextView data;
    Button submitButton;

    //all this shit was just to see how to connect with database
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data = (TextView) findViewById(R.id.data);
        userName = (EditText) findViewById(R.id.name);
        submitButton = (Button) findViewById(R.id.submitData);
        myRef = FirebaseDatabase.getInstance().getReference("messages");
    }

    @Override
    protected void onStart(){
        super.onStart();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //myRef.setValue("Hello World!");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.setValue(userName.getText().toString());
            }
        });

    }



}
