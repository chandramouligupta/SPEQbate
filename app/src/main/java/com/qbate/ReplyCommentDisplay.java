package com.qbate;

import android.content.Context;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class ReplyCommentDisplay extends AppCompatActivity {

    protected static Context replyCommentDisplayContext;
    RCDListAdapter rcdListAdapter;

    final DatabaseReference replyTableRef = FirebaseDatabase.getInstance().getReference("replies");
    final Query replyTableQuery = FirebaseDatabase.getInstance().getReference("replies");

    ArrayList<CommentItem> replyCommentList;
    EditText replyEditText;
    ListView replyListView;
    ImageButton addReplyImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_comment_display);

        replyCommentDisplayContext = this;

        final String topicId = getIntent().getStringExtra("topicId");
        final String categoryId = getIntent().getStringExtra("categoryId");
        final String replyingTocommentId = getIntent().getStringExtra("commentId");
        Log.d("testing27",replyingTocommentId);

        replyListView = findViewById(R.id.reply_comment_list_view);
        replyEditText = findViewById(R.id.add_reply_editText);
        addReplyImageButton = findViewById(R.id.add_reply_image_button);

        replyCommentList = new ArrayList<CommentItem>();
        replyTableQuery.orderByKey().equalTo(replyingTocommentId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                replyCommentList.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        for(DataSnapshot item:ds.getChildren()){
                            CommentItem obj = item.getValue(CommentItem.class);
                            Log.d("testing27","Comment Found:" + obj.getCommentTitle());
                            replyCommentList.add(obj);
                        }
                }
                    rcdListAdapter = new RCDListAdapter(replyCommentDisplayContext,replyCommentList);
                    replyListView.setAdapter(rcdListAdapter);
                }else{
                    //No Comments Present
                    Log.d("testing27","No Comment Present For this post!!!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addReplyImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String commentTitle = replyEditText.getText().toString();
                replyEditText.setText("");
                if(commentTitle != null && !commentTitle.equalsIgnoreCase("")){
                    String replyCommentId = replyTableRef.child(replyingTocommentId).push().getKey();
                    //String topicId;
                    //String categoryId;
                    String username = GoogleSignIn.getLastSignedInAccount(replyCommentDisplayContext).getDisplayName();
                    String creatorId = PreferenceManager.getDefaultSharedPreferences(TopicsDisplay.topicDisplayContext).getString("USERIDKEY", "defaultStringIfNothingFound");
                    String email =  GoogleSignIn.getLastSignedInAccount(replyCommentDisplayContext).getEmail();
                    long timestamp = Calendar.getInstance().getTimeInMillis();
                    Uri url = GoogleSignIn.getLastSignedInAccount(replyCommentDisplayContext).getPhotoUrl();
                    String myPhoto = null;
                    if(url != null)
                        myPhoto = url.toString();
                    else
                        myPhoto = "NO_PROFILE_PIC";
                    CommentItem ci = new CommentItem(replyCommentId,topicId,categoryId, username,
                            creatorId, email, timestamp, myPhoto, commentTitle);
                    replyTableRef.child(replyingTocommentId).child(replyCommentId).setValue(ci);
                }
            }
        });
    }
}
