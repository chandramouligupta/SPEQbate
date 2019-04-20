package com.qbate;

import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class TopicsDisplay extends AppCompatActivity implements AddTopicDialogFragment.InputTopicDetails {

    protected static Context topicDisplayContext;

    private String categoryId;
    private String categoryName;
    private Toolbar topicToolbar;
    private ListView listView;
    private ArrayList<TopicItem> topicsList;
    private TopicListAdapter topicsListAdapter;
    private GoogleSignInAccount googleSignInAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics_display);
        categoryId = getIntent().getStringExtra("categoryId");
        categoryName = getIntent().getStringExtra("categoryName");

        /*
        String userid = PreferenceManager.getDefaultSharedPreferences(this).getString("USERIDKEY", "defaultStringIfNothingFound")PreferenceManager.getDefaultSharedPreferences(this).getString("USERIDKEY", "defaultStringIfNothingFound");
        Log.d("testing2", userid);
        */

        //googleSignInAccount = (GoogleSignInAccount) getIntent().getSerializableExtra("signInObject");
        SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("googleSignInObject", "");
        googleSignInAccount = gson.fromJson(json, GoogleSignInAccount.class);

        topicToolbar = findViewById(R.id.topics_toolbar);
        topicToolbar.setTitleTextColor(Color.parseColor("#ECE8E8"));
        topicToolbar.setTitle(categoryName);
        setSupportActionBar(topicToolbar);

        listView = findViewById(R.id.topics_list);
        topicDisplayContext = this;
        topicsList = new ArrayList<TopicItem>();


        Query query = FirebaseDatabase.getInstance().getReference("topics").child(categoryId)
                .orderByChild("topicCategoryId").equalTo(categoryId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                topicsList.clear();
                for(DataSnapshot item : dataSnapshot.getChildren()){
                    TopicItem ti = item.getValue(TopicItem.class);
                    topicsList.add(ti);
                    Log.d("testing20",ti.getTopicTitle());
                }
                topicsListAdapter = new TopicListAdapter(topicDisplayContext,topicsList);
                listView.setAdapter(topicsListAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Intent intent = new Intent(topicDisplayContext,CommentDisplay.class);
                        intent.putExtra("categoryId",categoryId);
                        intent.putExtra("topicId",topicsList.get(position).getTopicId());
                        intent.putExtra("topicTitle",topicsList.get(position).getTopicTitle());
                        intent.putExtra("signInObject",googleSignInAccount);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.topic_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.add_topic:
                    new AddTopicDialogFragment().show(getSupportFragmentManager(),"Add Topic");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getInputTopicTitle(String inputTopicTitle) {
        Log.d("testing","In Topic Display:" + inputTopicTitle);
        TestDataTableCreatorFirebase.addTopic(categoryId,inputTopicTitle);
    }
}
