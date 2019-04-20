package com.qbate;

import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

public class TestDataTableCreatorFirebase {
    /* Create topics Table and topic category Maping at firebase*/


    static void createCategoryTable(ArrayList<String> list){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("category");
        for(String cName:list){
            String id = dbRef.push().getKey();
            CategoryItem obj = new CategoryItem(id,cName);
            dbRef.child(id).setValue(obj);
            //creatingTopicsTable(id,cName); //for Dummy Topics
        }
    }

    static void creatingTopicsTable(String categoryId){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("topics").child(categoryId);
        for(int i=1;i<=10;i++){
            String topicId = dbRef.push().getKey();
            long timestamp = Calendar.getInstance().getTimeInMillis();
            String topicTitle = "Dummy Topic with topic Id :" + topicId;
            String topicCategoryId = categoryId;
            String creatorId = PreferenceManager.getDefaultSharedPreferences(TopicsDisplay.topicDisplayContext).getString("USERIDKEY", "defaultStringIfNothingFound");
            TopicItem topicItem = new TopicItem(topicId, topicCategoryId, topicTitle, timestamp, creatorId);
            Log.d("testing","" + topicId + " " + " " + topicCategoryId + " " + topicTitle + " " + timestamp + " Creator" + topicCategoryId);
            dbRef.child(topicId).setValue(topicItem);
            createCommentsTable(topicId,categoryId);
        }
    }

    static void addTopic(String categoryId,String topicTitle){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("topics").child(categoryId);
        String topicId = dbRef.push().getKey();
        long timestamp = Calendar.getInstance().getTimeInMillis();
        String creatorId = PreferenceManager.getDefaultSharedPreferences(TopicsDisplay.topicDisplayContext).getString("USERIDKEY", "defaultStringIfNothingFound");
        String topicCategoryId = categoryId;
        TopicItem topicItem = new TopicItem(topicId, topicCategoryId, topicTitle, timestamp, creatorId);
        Log.d("testing","" + topicId + " " + " " + topicCategoryId + " " + topicTitle + " " + timestamp + " Creator" + topicCategoryId);
        dbRef.child(topicId).setValue(topicItem);
    }

    static void createCommentsTable(String topicId,String categoryId){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("comments").child(categoryId).child(topicId);
        for(int i=1;i<=10;i++){
            String commentId = dbRef.push().getKey();
            //String topicId;
            //String categoryId;
            String username = "testing";
            String creatorId = "testing";
            String email = "testing@email.com";
            long timestamp = Calendar.getInstance().getTimeInMillis();
            String photoUrl = null;
            String commentTitle = "Comment " + i;
            CommentItem ci = new CommentItem(commentId,topicId,categoryId,username,
                    creatorId,email,timestamp,photoUrl,commentTitle);
            dbRef.child(commentId).setValue(ci);
        }
    }

}
