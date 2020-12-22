package com.cobanogluhasan.twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    ImageView imageView;
    EditText usernameEdittext;
    EditText passwordEditText;
    Button signUpButton, loginButton;
    RelativeLayout myRelativeLayout;


    private void redirectUser(){

        if(ParseUser.getCurrentUser()!=null) {
            Intent intent = new Intent(getApplicationContext(), UsersListActivity.class);
            startActivity(intent);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpView();

        setTitle("Twitter");

        redirectUser();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=usernameEdittext.getText().toString();
                String password = passwordEditText.getText().toString();

                if(username.equals("") || password.equals("")) {
                    Toast.makeText(MainActivity.this, "Username and password cannot be emty!!", Toast.LENGTH_SHORT).show();
                }
                else  signUpToPArse(username,password);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=usernameEdittext.getText().toString();
                String password = passwordEditText.getText().toString();

                if(username.equals("") || password.equals("")) {
                    Toast.makeText(MainActivity.this, "Username and password cannot be emty!!", Toast.LENGTH_SHORT).show();
                }
               else loginParse(username,password);
            }
        });



        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    private void setUpView() {
        imageView = findViewById(R.id.imageview);
        passwordEditText = findViewById(R.id.passwordEditText);
        usernameEdittext = findViewById(R.id.usernameEditText);
        loginButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.signupButton);
        myRelativeLayout = findViewById(R.id.myRelativeLayout);
    }

    private void signUpToPArse(String username,String password) {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null) {
                    Toast.makeText(MainActivity.this,"You signed up!", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "done: Signup"+ "successfull");
                    redirectUser();
                }
                else {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void loginParse(String username,String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e==null) {
                    Toast.makeText(MainActivity.this,"login successfull!", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "done: login"+ "successfull");
                    redirectUser();
                }
                else {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }




}