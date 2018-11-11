package com.example.cmsc436.localapparel.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cmsc436.localapparel.Objects.Chat;
import com.example.cmsc436.localapparel.Objects.FireBaseBackEnd;
import com.example.cmsc436.localapparel.Objects.User;
import com.example.cmsc436.localapparel.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MessageListActivity extends AppCompatActivity {

    FirebaseDatabase fire;
    FireBaseBackEnd backEnd;
    User mainUser;
    FirebaseAuth mAuth;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        //parent firebase database reference
        fire = FirebaseDatabase.getInstance("https://localapparel-96283.firebaseio.com/");
        backEnd = new FireBaseBackEnd(fire);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }

    public void addMessage(View view){
        mainUser = backEnd.getCurrentUser(user.getUid());
        mainUser.addChat(new Chat("user1", "user2"));
        backEnd.addUser(mainUser);
    }
}
