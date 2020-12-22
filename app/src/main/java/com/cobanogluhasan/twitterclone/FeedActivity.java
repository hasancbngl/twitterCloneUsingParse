package com.cobanogluhasan.twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {

    ListView feedListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        feedListView = findViewById(R.id.feedListView);

        final List<Map<String,String>> tweetData = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Tweet");
        query.whereContainedIn("username", ParseUser.getCurrentUser().getList("isFollowing"));

        query.orderByDescending("createdAt");
        query.setLimit(20);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null) {

                    for(ParseObject tweet:objects) {
                        Map<String,String> tweetInfo = new HashMap<>();
                        tweetInfo.put("content", tweet.getString("tweet"));
                        tweetInfo.put("username", tweet.getString("username"));
                        tweetData.add(tweetInfo);
                    }
                   SimpleAdapter simpleAdapter = new SimpleAdapter(FeedActivity.this,tweetData,android.R.layout.simple_list_item_2,
                           new String[]{"content","username"} ,new int[]{android.R.id.text1, android.R.id.text2});

                    simpleAdapter.notifyDataSetChanged();
                    feedListView.setAdapter(simpleAdapter);

                }
            }
        });


/*
        for (int i = 0; i < 10; i++) {

            Map<String, String> tweetInfo = new HashMap<>();
            tweetInfo.put("content", "tweet content:" + String.valueOf(i));
            tweetInfo.put("username", "user:" + Integer.toString(i*2));
            tweetData.add(tweetInfo);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, tweetData, android.R.layout.simple_list_item_2,
                new String[]{"content", "username"}, new int[]{android.R.id.text1, android.R.id.text2});

        feedListView.setAdapter(simpleAdapter);*/

    }
}