package com.example.cmsc436.localapparel.Activities;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cmsc436.localapparel.Objects.FireBaseBackEnd;
import com.example.cmsc436.localapparel.Objects.User;
import com.example.cmsc436.localapparel.R;
import com.google.firebase.database.FirebaseDatabase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreateUserActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailField;
    private EditText passwordField;
    private EditText verifyPasswordField;
    private EditText phoneNumField;
    private String TAG = "CreateUserActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user);

        mAuth = FirebaseAuth.getInstance();
        emailField = findViewById(R.id.emailFieldCreateUser);
        passwordField = findViewById(R.id.passwordFieldCreateUser);
        verifyPasswordField = findViewById(R.id.verifyPasswordFieldCreateUser);
        phoneNumField = findViewById(R.id.phoneNumberField);
    }

    public void createUserPressed(View view) {
        /// now we gotta figure out what to do with the information once the button is rpessed
        // ie make sure he passwords match and the email address isnt used yet

        final String email = emailField.getText().toString();
        final String password = passwordField.getText().toString();
        String verifyPassword = verifyPasswordField.getText().toString();
        final String phoneNum = phoneNumField.getText().toString();

        //if no phone number was entered
        if(phoneNum.trim().equals("")){
            Toast.makeText(CreateUserActivity.this, "Please enter your phone number.", Toast.LENGTH_SHORT).show();
            return;
        }

        // if the passwords don't match then display an error message
        if (!password.equals(verifyPassword)) {
            Toast.makeText(CreateUserActivity.this, "The passwords must match.",
                    Toast.LENGTH_SHORT).show();
        } else {
            // so the passwords match
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this,
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            /* the email is not in use, the password is strong enough, and the email
                            address is valid, so creation should be successful */
                            if (task.isSuccessful()) {
                                Log.d(TAG, "successfully created user with email");
                                FirebaseDatabase fire = FirebaseDatabase.getInstance("https://localapparel-96283.firebaseio.com/");
                                FireBaseBackEnd backEnd  = new FireBaseBackEnd(fire);


                                FirebaseUser user = mAuth.getCurrentUser();
                                User tempUser = new User(email,password, user.getUid(), phoneNum);
                                backEnd.addUser(tempUser);

                                startActivity(new Intent(CreateUserActivity.this,
                                        MainPage.class));
                            } else {
                                /* email may already be in use, password is not strong enough, or
                                email address is not valid, so creation will fail. I'm still working
                                out how to catch these errors individually, so that I can display
                                the appropriate Toast message.*/
                                Log.w(TAG, "create user with email failed, email may already" +
                                        "be in use");
                                Toast.makeText(CreateUserActivity.this, "Create user " +
                                        "failed, email may already be in use.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
    }
}
