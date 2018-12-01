package com.example.cmsc436.localapparel.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cmsc436.localapparel.Objects.Chat;
import com.example.cmsc436.localapparel.Objects.FireBaseBackEnd;
import com.example.cmsc436.localapparel.Objects.Item;
import com.example.cmsc436.localapparel.Objects.Message;
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

import java.util.ArrayList;
import java.util.List;

public class MessageListActivity extends AppCompatActivity {

    FirebaseDatabase fire;
    FireBaseBackEnd backEnd;
    DatabaseReference ref;
    StorageReference storeRef;
    User mainUser;
    FirebaseAuth mAuth;
    FirebaseUser user;
    ListView listView;
    CustomAdapter mAdapter;
    List<Chat> chatList;
    List<User> allUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        //parent firebase database reference

        mAuth = FirebaseAuth.getInstance();
        user= mAuth.getCurrentUser();
        backEnd = new FireBaseBackEnd(FirebaseDatabase.getInstance());
        ref = FirebaseDatabase.getInstance().getReference();
        storeRef = FirebaseStorage.getInstance().getReference();
        allUsers = new ArrayList<User>();

        ref.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                allUsers.clear();
                for (DataSnapshot child : children) {
                    User user = child.getValue(User.class);
                    allUsers.add(user);
                }

                mainUser = backEnd.getCurrentUser(user.getUid());

                listView = (ListView)findViewById(R.id.messageListItem);

                if(mainUser.getChats() == null){
                    chatList = new ArrayList<Chat>();
                }else{
                    chatList = mainUser.getChats();
                }


                mAdapter = new MessageListActivity.CustomAdapter(MessageListActivity.this,R.layout.individual_message,chatList);
                listView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    class CustomAdapter extends ArrayAdapter<Chat> {
        List<Chat> chatList;
        Context context;
        int resource;

        public CustomAdapter(Context context, int resource, List<Chat> chatList) {
            super(context, resource, chatList);
            this.context = context;
            this.resource = resource;
            this.chatList = chatList;
        }

        //might take out
        public int getCount() {
           return chatList.size();
        }

        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(resource, null, false);

            final ImageView senderImage = view.findViewById(R.id.senderPicture);
            TextView senderEmail = view.findViewById(R.id.senderEmail);
            TextView date = view.findViewById(R.id.dateOfConversation);
            TextView chatItemName= view.findViewById(R.id.chatItemName);

            final Chat chat = chatList.get(position);

            String userID = user.getUid();

            if(chat.getReciever().equals(userID)){
                User otherUser = null;
                for(User u : allUsers){
                    if(u.getUid().equals(chat.getSender())){
                        otherUser = u;
                    }
                }

                senderEmail.setText(otherUser.getEmail());
                date.setText("1/1/2010");

                storeRef.child("profilePictures").child(otherUser.getUid()).getBytes(1024*1024).addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        senderImage.setImageBitmap(bmp);
                        Log.d("TEST", "downloaded picture");
                    }
                });
            }else{
                User otherUser = null;
                for(User u : allUsers){
                    if(u.getUid().equals(chat.getReciever())){
                        otherUser = u;
                    }
                }

                senderEmail.setText(otherUser.getEmail());
                date.setText(chat.getDate());
                chatItemName.setText(chat.getItemName());

                storeRef.child("profilePictures").child(otherUser.getUid()).getBytes(1024*1024).addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        senderImage.setImageBitmap(bmp);
                        Log.d("TEST", "downloaded picture");
                    }
                });
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toIndividualChat = new Intent(context, IndividualChat.class);

                    toIndividualChat.putExtra("Chat", chat);
                    startActivity(toIndividualChat);
                }
            });

            return view;
        }
    }

}

