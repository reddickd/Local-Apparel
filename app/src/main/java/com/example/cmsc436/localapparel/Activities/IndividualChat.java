package com.example.cmsc436.localapparel.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class IndividualChat extends AppCompatActivity {


    FirebaseDatabase fire;
    FireBaseBackEnd backEnd;
    DatabaseReference ref;
    StorageReference storeRef;
    FirebaseAuth mAuth;
    FirebaseUser user;
    User mainUser;
    List<User> allUsers;
    List<Message> allMessages;
    RecyclerView mMessageRecycler;
    MessageListAdapter mMessageAdapter;
    EditText message;
    Button sendMessageButton;
    Chat currentChat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.individual_chat);
        mAuth = FirebaseAuth.getInstance();
        user= mAuth.getCurrentUser();
        backEnd = new FireBaseBackEnd(FirebaseDatabase.getInstance());
        ref = FirebaseDatabase.getInstance().getReference();
        storeRef = FirebaseStorage.getInstance().getReference();
        message = findViewById(R.id.edittext_chatbox);
        sendMessageButton = findViewById(R.id.button_chatbox_send);

        currentChat = (Chat) getIntent().getSerializableExtra("Chat");

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

                for(User u : allUsers){
                    if(u.getUid().equals(user.getUid())){
                        mainUser = u;
                    }
                }

                for(Chat c : mainUser.getChats()){
                    if(currentChat.equals(c)){
                        currentChat = c;
                    }
                }

                mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);
                mMessageAdapter = new MessageListAdapter(IndividualChat.this, currentChat.getMessages());
                mMessageRecycler.setAdapter(mMessageAdapter);
                mMessageRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

     public void sendMessage(View view){
        String messageToBeSent = message.getText().toString();

        if(messageToBeSent.trim().equals("")){
            Toast.makeText(IndividualChat.this, "Please Enter a Message", Toast.LENGTH_SHORT).show();

            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            return;
        }

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
        if(minutes <= 9){
            timeStamp = Integer.toString(hours) + ":0" + Integer.toString(minutes) + " " + amOrPM;
        }else{
            timeStamp = Integer.toString(hours) + ":" + Integer.toString(minutes) + " " + amOrPM;
        }





        Message newMessage = new Message(user.getUid(), messageToBeSent, timeStamp);

        User otherUser = null;
        if(currentChat.getSender().equals(user.getUid())){
            for(User u : allUsers){
                if(u.getUid().equals(currentChat.getReciever())){
                    otherUser = u;
                    break;
                }
            }
        }else{
            for(User u : allUsers){
                if(u.getUid().equals(currentChat.getSender())){
                    otherUser = u;
                    break;
                }
            }
        }

        for(Chat c : otherUser.getChats()){
            if(currentChat.equals(c)){
                c.addMessage(newMessage);
            }
        }

        for(Chat c : mainUser.getChats()){
            if(currentChat.equals(c)){
                c.addMessage(newMessage);
            }
        }


        message.setText("");
         InputMethodManager inputManager = (InputMethodManager)
                 getSystemService(Context.INPUT_METHOD_SERVICE);

         inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                 InputMethodManager.HIDE_NOT_ALWAYS);

         backEnd.addUser(otherUser);
         backEnd.addUser(mainUser);
     }

//    private void testMessage(){
//        allMessages = new ArrayList<Message>();
//        Message m = new Message("t9qujixydjhMiaBgDEfHR25vxpo1", "test");
//
//        Message m1 = new Message("UMrlwETJz1cz8fskv5aCcP9R7al2", "test");
//
//
//        allMessages.add(m);
//        allMessages.add(m1);
//        allMessages.add(m1);
//        allMessages.add(m);
//        allMessages.add(m1);
//        allMessages.add(m);
//        allMessages.add(m);
//        allMessages.add(m);
//        allMessages.add(m1);
//        allMessages.add(m);
//        allMessages.add(m1);
//        allMessages.add(m1);
//        allMessages.add(m);
//    }

    public class MessageListAdapter extends RecyclerView.Adapter {
        private static final int VIEW_TYPE_MESSAGE_SENT = 1;
        private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

        private Context mContext;
        private List<Message> mMessageList;

        public MessageListAdapter(Context context, List<Message> messageList) {
            mContext = context;
            mMessageList = messageList;
        }

        @Override
        public int getItemCount() {
            return mMessageList.size();
        }

        // Determines the appropriate ViewType according to the sender of the message.
        @Override
        public int getItemViewType(int position) {
            Message message = (Message) mMessageList.get(position);

            if (message.getSender().equals(user.getUid())) {
                // If the current user is the sender of the message
                return VIEW_TYPE_MESSAGE_SENT;
            } else {
                // If some other user sent the message
                return VIEW_TYPE_MESSAGE_RECEIVED;
            }
        }


        // Inflates the appropriate layout according to the ViewType.
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;

            if (viewType == VIEW_TYPE_MESSAGE_SENT) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.message_sent, parent, false);
                return new SentMessageHolder(view);
            } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.message_recieved, parent, false);
                return new ReceivedMessageHolder(view);
            }

            return null;
        }

        // Passes the message object to a ViewHolder so that the contents can be bound to UI.
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Message message = (Message) mMessageList.get(position);

            switch (holder.getItemViewType()) {
                case VIEW_TYPE_MESSAGE_SENT:
                    ((SentMessageHolder) holder).bind(message);
                    break;
                case VIEW_TYPE_MESSAGE_RECEIVED:
                    ((ReceivedMessageHolder) holder).bind(message);
            }
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
        }

        void bind(Message message) {
            messageText.setText(message.getMessage());
            timeText.setText(message.getTimeStamp());
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;
        ImageView profileImage;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            nameText = (TextView) itemView.findViewById(R.id.text_message_name);
            profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
        }

        void bind(Message message) {
            messageText.setText(message.getMessage());
            timeText.setText(message.getTimeStamp());

            // Format the stored timestamp into a readable String using method.
            //timeText.setText(Utils.formatDateTime(message.getCreatedAt()));

            User otherUser = null;
            for(User u : allUsers){
                if(message.getSender().equals(u.getUid())){
                    otherUser = u;
                    break;
                }
            }
            nameText.setText(otherUser.getEmail());

            // Insert the profile image from the URL into the ImageView.
            storeRef.child("profilePictures").child(otherUser.getUid()).getBytes(1024*1024).addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    profileImage.setImageBitmap(bmp);
                    Log.d("TEST", "downloaded picture");
                }
            });
        }
    }


}
