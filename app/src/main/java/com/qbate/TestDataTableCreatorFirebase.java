package com.qbate;

import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

public class TestDataTableCreatorFirebase {
    /* Create topics Table and topic category Maping at firebase*/


    static void createCategoryTable(ArrayList<String> list){ // if list of categories
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("category");
        for(String cName:list){
            String id = dbRef.push().getKey();
            CategoryItem obj = new CategoryItem(id,cName);
            dbRef.child(id).setValue(obj);
            //creatingTopicsTable(id,cName); //for Dummy Topics
        }
    }

    static void addCategory(String categoryName){
        DatabaseReference categoryTableRef = FirebaseDatabase.getInstance().getReference("category");
        String categoryId = categoryTableRef.push().getKey();
        CategoryItem obj = new CategoryItem(categoryId,categoryName);
        categoryTableRef.child(categoryId).setValue(obj);
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

    static void addComment(String categoryId, String topicId, String commentTitle){
        DatabaseReference commentTableRef = FirebaseDatabase.getInstance().getReference("comments")
                .child(categoryId).child(topicId);
        String commentId = commentTableRef.push().getKey();
        String username = GoogleSignIn.getLastSignedInAccount(CommentDisplay.commentsDisplayContext).getDisplayName();
        String creatorId = PreferenceManager.getDefaultSharedPreferences(TopicsDisplay.topicDisplayContext).getString("USERIDKEY", "defaultStringIfNothingFound");
        String email =  GoogleSignIn.getLastSignedInAccount(CommentDisplay.commentsDisplayContext).getEmail();
        long timestamp = Calendar.getInstance().getTimeInMillis();
        Uri url = GoogleSignIn.getLastSignedInAccount(CommentDisplay.commentsDisplayContext).getPhotoUrl();
        String myPhoto = null;
        if(url != null)
            myPhoto = url.toString();
        else
            myPhoto = "NO_PROFILE_PIC";
        CommentItem commentObj = new CommentItem(commentId,topicId,categoryId, username,
                creatorId, email, timestamp, myPhoto, commentTitle);
        commentTableRef.child(commentId).setValue(commentObj);
    }

    static void addReplyComment(String replyToCommentId, String categoryId, String topicId, String commentTitle){
        DatabaseReference replyTableRef = FirebaseDatabase.getInstance().getReference("replies")
                .child(replyToCommentId);
        String id = replyTableRef.push().getKey();
        String username = GoogleSignIn.getLastSignedInAccount(CommentDisplay.commentsDisplayContext).getDisplayName();
        String creatorId = PreferenceManager.getDefaultSharedPreferences(TopicsDisplay.topicDisplayContext).getString("USERIDKEY", "defaultStringIfNothingFound");
        String email =  GoogleSignIn.getLastSignedInAccount(CommentDisplay.commentsDisplayContext).getEmail();
        long timestamp = Calendar.getInstance().getTimeInMillis();
        Uri url = GoogleSignIn.getLastSignedInAccount(CommentDisplay.commentsDisplayContext).getPhotoUrl();
        String myPhoto = null;
        if(url != null)
            myPhoto = url.toString();
        else
            myPhoto = "NO_PROFILE_PIC";
        CommentItem commentObj = new CommentItem(id,topicId,categoryId, username,
                creatorId, email, timestamp, myPhoto, commentTitle);
        replyTableRef.child(id).setValue(commentObj);
    }
}
