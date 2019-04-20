package com.qbate;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class CommentListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<CommentItem> commentsItemList;

    public CommentListAdapter(Context context, ArrayList<CommentItem> commentsItemList) {
        this.context = context;
        this.commentsItemList = commentsItemList;
    }

    @Override
    public int getCount() {
        return commentsItemList.size();
    }

    @Override
    public Object getItem(int pos) {
        return commentsItemList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final String creatorId = PreferenceManager.getDefaultSharedPreferences(context).getString("USERIDKEY", "defaultStringIfNothingFound");
        final DatabaseReference likesTableRef = FirebaseDatabase.getInstance().getReference("likes");
        final DatabaseReference dislikesTableRef = FirebaseDatabase.getInstance().getReference("dislikes");
        final DatabaseReference irrelevantTableRef = FirebaseDatabase.getInstance().getReference("irrelevants");
        final Query checkIfLiked = FirebaseDatabase.getInstance().getReference("likes")
                .child(commentsItemList.get(position).getCommentId()).orderByKey().equalTo(creatorId);
        final Query checkIfDisliked = FirebaseDatabase.getInstance().getReference("dislikes")
                .child(commentsItemList.get(position).getCommentId()).orderByKey().equalTo(creatorId);
        final Query checkIfIrrelevantClicked = FirebaseDatabase.getInstance().getReference("irrelevants")
                .child(commentsItemList.get(position).getCommentId()).orderByKey().equalTo(creatorId);

        final String email = commentsItemList.get(position).getEmail();
        Log.d("testing","UID:" + email);
        final TextView commentTitle;
        TextView userName;
        TextView dateTime;
        final TextView replyText;
        final TextView viewAllReply;
        final TextView upVoteTextView;
        final TextView downVoteTextView;
        final TextView irrelevantTextView;
        final GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(context);
        Log.d("testing",googleSignInAccount.getEmail());
        View v;
        if(email.equalsIgnoreCase(googleSignInAccount.getEmail())) {
            v = View.inflate(context, R.layout.my_message, null);
            commentTitle = v.findViewById(R.id.my_message_body);
            dateTime = v.findViewById(R.id.my_comment_time);

            viewAllReply = v.findViewById(R.id.view_all_reply_my_message);

            FirebaseDatabase.getInstance().getReference("replies")
                    .orderByKey().equalTo(commentsItemList.get(position).getCommentId())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(!dataSnapshot.exists()){
                                //No Replies till now
                            }else{
                                viewAllReply.setVisibility(View.VISIBLE);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

            viewAllReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,ReplyCommentDisplay.class);
                    intent.putExtra("topicId",commentsItemList.get(position).getTopicId());
                    intent.putExtra("categoryId",commentsItemList.get(position).getCategoryId());
                    intent.putExtra("commentId",commentsItemList.get(position).getCommentId());
                    context.startActivity(intent);
                }
            });

            commentTitle.setText(commentsItemList.get(position).getCommentTitle());
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy' 'hh:mm a", Locale.US);
            dateTime.setText(sdf.format(commentsItemList.get(position).getTimestamp()));
            upVoteTextView = v.findViewById(R.id.my_upvote);
            downVoteTextView = v.findViewById(R.id.my_downvote);
            irrelevantTextView = v.findViewById(R.id.my_irrelevant);

        }
        else {
            v = View.inflate(context, R.layout.their_message, null);
            ImageView avatar = v.findViewById(R.id.avatar);

            replyText = v.findViewById(R.id.add_reply_text_their_message);
            viewAllReply = v.findViewById(R.id.view_all_reply_their_message);

            replyText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,ReplyCommentDisplay.class);
                    intent.putExtra("topicId",commentsItemList.get(position).getTopicId());
                    intent.putExtra("categoryId",commentsItemList.get(position).getCategoryId());
                    intent.putExtra("commentId",commentsItemList.get(position).getCommentId());
                    context.startActivity(intent);
                }
            });

            viewAllReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,ReplyCommentDisplay.class);
                    intent.putExtra("topicId",commentsItemList.get(position).getTopicId());
                    intent.putExtra("categoryId",commentsItemList.get(position).getCategoryId());
                    intent.putExtra("commentId",commentsItemList.get(position).getCommentId());
                    context.startActivity(intent);
                }
            });

            final TextView like = v.findViewById(R.id.thumbs_up);
            final TextView dislike = v.findViewById(R.id.thumbs_down);
            final TextView irrelevant = v.findViewById(R.id.irrelevant);
            commentTitle = v.findViewById(R.id.their_message_body);
            userName = v.findViewById(R.id.uname);
            dateTime = v.findViewById(R.id.their_comment_time);
            upVoteTextView = v.findViewById(R.id.their_upvote);
            downVoteTextView = v.findViewById(R.id.their_downvote);
            irrelevantTextView = v.findViewById(R.id.their_irrelevant);
            commentTitle.setText(commentsItemList.get(position).getCommentTitle());
            String photoUrl = commentsItemList.get(position).getPhotoUrl();
            if(!photoUrl.equalsIgnoreCase("NO_PROFILE_PIC")) {
                Log.d("testing",commentsItemList.get(position).getPhotoUrl());

                new DownloadImageTask((ImageView) v.findViewById(R.id.avatar))
                        .execute(photoUrl);
            }
            userName.setText(commentsItemList.get(position).getUsername());
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy' 'hh:mm a", Locale.US);
            dateTime.setText(sdf.format(commentsItemList.get(position).getTimestamp()));

            //Setting Textview Liked,Disliked,Irrelevant
            checkIfLiked.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        like.setText("Liked");
                        dislike.setVisibility(View.INVISIBLE);
                        irrelevant.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            checkIfDisliked.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        like.setText("Unliked");
                        dislike.setVisibility(View.INVISIBLE);
                        irrelevant.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            checkIfIrrelevantClicked.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        like.setText("Irrelevant");
                        dislike.setVisibility(View.INVISIBLE);
                        irrelevant.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            //Handling likes/dislikes/irrelevant clicks
            dislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //i.e inserting an item in dislike table with key as current USERKEY
                    ILDItem obj = new ILDItem(googleSignInAccount.getEmail());
                    dislikesTableRef.child(commentsItemList.get(position).getCommentId()).child(creatorId).setValue(obj);
                    like.setTextColor(Color.parseColor("#4267b2"));
                    like.setText("Unliked");
                    dislike.setVisibility(View.INVISIBLE);
                    irrelevant.setVisibility(View.INVISIBLE);
                }
            });
            irrelevant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //i.e insetring an item in irrelevant table with key as current USERKEY
                    ILDItem obj = new ILDItem(googleSignInAccount.getEmail());
                    irrelevantTableRef.child(commentsItemList.get(position).getCommentId()).child(creatorId).setValue(obj);
                    like.setTextColor(Color.parseColor("#4267b2"));
                    like.setText("Irrelevant");
                    dislike.setVisibility(View.INVISIBLE);
                    irrelevant.setVisibility(View.INVISIBLE);
                }
            });
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //as we have to check for like button if already liked or if disliked or
                    // if already irrelevant pressed or if user is pressing like itself
                    if("Unliked".equalsIgnoreCase(like.getText().toString())){
                        //i.e User has already disliked this but now he changed his mind and want
                        // set it back to neutral
                        dislikesTableRef.child(commentsItemList.get(position).getCommentId()).child(creatorId).removeValue();
                        like.setTextColor(Color.parseColor("#231F20"));
                        like.setText("Like");
                        dislike.setVisibility(View.VISIBLE);
                        irrelevant.setVisibility(View.VISIBLE);
                    }else if("Irrelevant".equalsIgnoreCase(like.getText().toString())){
                        //i.e user has  marked it irrelevant but now making it neutral
                        irrelevantTableRef.child(commentsItemList.get(position).getCommentId()).child(creatorId).removeValue();
                        like.setTextColor(Color.parseColor("#231F20"));
                        like.setText("Like");
                        dislike.setVisibility(View.VISIBLE);
                        irrelevant.setVisibility(View.VISIBLE);
                    }else if("Liked".equalsIgnoreCase(like.getText().toString())) {
                        // i.e User has already Liked it but now he maiy be setting it to neutral
                        likesTableRef.child(commentsItemList.get(position).getCommentId()).child(creatorId).removeValue();
                        like.setTextColor(Color.parseColor("#231F20"));
                        like.setText("Like");
                        dislike.setVisibility(View.VISIBLE);
                        irrelevant.setVisibility(View.VISIBLE);
                    }else{
                        //i.e User is clicking on like button
                        ILDItem obj = new ILDItem(googleSignInAccount.getEmail());
                        like.setText("Liked");
                        like.setTextColor(Color.parseColor("#4267b2"));
                        likesTableRef.child(commentsItemList.get(position).getCommentId()).child(creatorId).setValue(obj);
                        dislike.setVisibility(View.INVISIBLE);
                        irrelevant.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }

        //Displaying Upvote,Downvote,Irrelevant Counts

        likesTableRef.child(commentsItemList.get(position).getCommentId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //here dataSnapShot object will define how many likes does that post has
                // if the post has 0 likes then this will give us a null object
                if(dataSnapshot == null){
                    // No any likes on this post
                    Log.d("testing2","No any likes for this comment");
                    upVoteTextView.setText("Upvote:0");
                }else{
                    upVoteTextView.setText("Upvotes:" + dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dislikesTableRef.child(commentsItemList.get(position).getCommentId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //here dataSnapShot object will define how many likes does that post has
                // if the post has 0 likes then this will give us a null object
                if(dataSnapshot == null){
                    // No any likes on this post
                    Log.d("testing2","No any likes for this comment");
                    downVoteTextView.setText("Downvote:0");
                }else{
                    downVoteTextView.setText("Downvotes:" + dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        irrelevantTableRef.child(commentsItemList.get(position).getCommentId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //here dataSnapShot object will define how many likes does that post has
                // if the post has 0 likes then this will give us a null object
                if(dataSnapshot == null){
                    // No any likes on this post
                    Log.d("testing2","No any likes for this comment");
                    irrelevantTextView.setText("Irrelevant:0");
                }else{
                    irrelevantTextView.setText("Irrelevant:" + dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //saving product id to the tag
        v.setTag(commentsItemList.get(position).getCommentId());
        return v;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
