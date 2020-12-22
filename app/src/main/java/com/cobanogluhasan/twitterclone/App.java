package com.cobanogluhasan.twitterclone;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class App extends Application {

    private static final String TAG = "App";

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);

        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("myappID")
                .clientKey("OYBtS9PrUYPR")
                .server("http://13.58.33.30/parse/")
                .build());

        /*
        ParseUser.enableAutomaticUser(); // login automatically
        ParseObject parseObject = new ParseObject("MyTestObject");
        parseObject.put("myNAme", "hasan");
        parseObject.put("sName", "çoban");
        parseObject.put("Telnumber","1351");
        parseObject.put("job","coder");
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null) Log.i(TAG, "done: "+ "saved successfully");
                else e.printStackTrace();
            }
        }); */



        ParseACL parseACL = new ParseACL();
        parseACL.setPublicReadAccess(true);
        parseACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(parseACL,true);


    }


}
