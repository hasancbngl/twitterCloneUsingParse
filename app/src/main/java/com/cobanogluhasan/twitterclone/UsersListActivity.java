package com.cobanogluhasan.twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class UsersListActivity extends AppCompatActivity {

    private static final String TAG = "UsersListActivity";
    ListView listView;
    ArrayList<String> users;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch(item.getItemId()) {
            case R.id.tweet:
                tweet();
                return true;
            case R.id.feed:
                Intent feedIntent = new Intent(getApplicationContext(),FeedActivity.class);
                startActivity(feedIntent);
                return true;
            case R.id.logout:
                ParseUser.logOut();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        getSupportActionBar();
        getSupportActionBar().setTitle("Users List");


        listView = findViewById(R.id.listView);
        users = new ArrayList<String>();
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_checked,users);

        listView.setAdapter(arrayAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView checkedTextView = (CheckedTextView) view;
                if(checkedTextView.isChecked()) {
                    Log.i(TAG, "onItemClick: " + users.get(position) + "is Checked");
                    ParseUser.getCurrentUser().add("isFollowing", users.get(position));
                }
                else {
                    Log.i(TAG, "onItemClick: " + users.get(position)  + "is Not Checked");

                    ParseUser.getCurrentUser().getList("isFollowing").remove(users.get(position));
                    List tempUsers =  ParseUser.getCurrentUser().getList("isFollowing");
                    ParseUser.getCurrentUser().remove("isFollowing");
                    ParseUser.getCurrentUser().put("isFollowing", tempUsers);
                }

                ParseUser.getCurrentUser().saveInBackground();
            }
        });





        ParseQuery<ParseUser> query = ParseUser.getQuery();

       query.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());

   query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e==null && objects.size()>0) {

                    for(ParseUser object:objects) {
                        users.add(object.getUsername());
                    }
                    arrayAdapter.notifyDataSetChanged();

                    for(String username: users) {
                        if(ParseUser.getCurrentUser().getList("isFollowing").contains(username)) {
                            listView.setItemChecked(users.indexOf(username), true);
                        }
                    }

                }
            }
        });

    }


    private void tweet() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Send a tweet");

        final EditText tweetEditText = new EditText(this);

        builder.setView(tweetEditText);
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "onClick: " + tweetEditText.getText().toString());

                ParseObject tweet = new ParseObject("Tweet");
                tweet.put("username", ParseUser.getCurrentUser().getUsername());
                tweet.put("tweet",tweetEditText.getText().toString() );
                tweet.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e==null) {
                            Toast.makeText(UsersListActivity.this, "Tweet Shared", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(UsersListActivity.this, "tweet failed", Toast.LENGTH_SHORT).show();}
                    }
                });
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "onClick: " + "dont wanna tweet");
                dialog.cancel();
            }
        });

        builder.show();


      //  ParseObject object = ParseObject("Tweet");


    }


}