package com.example.cmsc436.localapparel.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cmsc436.localapparel.Objects.Chat;
import com.example.cmsc436.localapparel.Objects.Message;
import com.example.cmsc436.localapparel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailField;
    private EditText passwordField;
    private String TAG = "LogInActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);

    }

    //Onclick method for when user trys to login
    public void logInPressed(View view) {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        if((!email.equals("")) && !password.equals("")) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //The sign in was successful we should go to next activity
                        Log.d(TAG, "Signed in successful");
                        //Get current user information
                        FirebaseUser user = mAuth.getCurrentUser();
                        //go to next activty

                        startActivity(new Intent(LoginActivity.this, MainPage.class));
                    } else {
                        //Sign in failed
                        Log.w(TAG, "Sign in failed");
                        Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    //Onclick method when the user clicks the Create New Account Text
    public void createNewAccount(View view){
        startActivity(new Intent(LoginActivity.this, CreateUserActivity.class));
    }

    public void resetPassword(View view){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Please Enter Your Email");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //make theres text in the box
                String email;
                if(input.getText().toString().trim().equals("")){
                    Toast.makeText(LoginActivity.this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
                }else{
                    email = input.getText().toString();
                    mAuth.sendPasswordResetEmail(email);
                    Toast.makeText(LoginActivity.this, "Password Reset Email Sent", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
            // Canceled.
        }
        });

        alert.show();
    }
}