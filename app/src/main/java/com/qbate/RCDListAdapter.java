package com.qbate;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class RCDListAdapter extends BaseAdapter { // ReplyCommentDisplayListAdapter class
    
    private Context context;
    private ArrayList<CommentItem> replyItemArrayList;

    public RCDListAdapter(Context context, ArrayList<CommentItem> replyItemArrayList) {
        this.context = context;
        this.replyItemArrayList = replyItemArrayList;
    }

    @Override
    public int getCount() {
        return replyItemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return replyItemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final String creatorId = PreferenceManager.getDefaultSharedPreferences(context).getString("USERIDKEY", "defaultStringIfNothingFound");
        final DatabaseReference likesTableRef = FirebaseDatabase.getInstance().getReference("likes");
        final DatabaseReference dislikesTableRef = FirebaseDatabase.getInstance().getReference("dislikes");
        final DatabaseReference irrelevantTableRef = FirebaseDatabase.getInstance().getReference("irrelevants");

        Log.d("testing27",replyItemArrayList.get(position).getCommentId());

        final Query checkIfLiked = FirebaseDatabase.getInstance().getReference("likes")
                .child(replyItemArrayList.get(position).getCommentId()).orderByKey().equalTo(creatorId);
        final Query checkIfDisliked = FirebaseDatabase.getInstance().getReference("dislikes")
                .child(replyItemArrayList.get(position).getCommentId()).orderByKey().equalTo(creatorId);
        final Query checkIfIrrelevantClicked = FirebaseDatabase.getInstance().getReference("irrelevants")
                .child(replyItemArrayList.get(position).getCommentId()).orderByKey().equalTo(creatorId);

        final String email = replyItemArrayList.get(position).getEmail();
        Log.d("testing","UID:" + email);
        final TextView commentTitle;
        TextView userName;
        TextView dateTime;
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
            TextView  viewAllReplyMyMessage = v.findViewById(R.id.view_all_reply_my_message);
            viewAllReplyMyMessage.setVisibility(View.GONE);
            commentTitle.setText(replyItemArrayList.get(position).getCommentTitle());
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy' 'hh:mm a", Locale.US);
            dateTime.setText(sdf.format(replyItemArrayList.get(position).getTimestamp()));
            upVoteTextView = v.findViewById(R.id.my_upvote);
            downVoteTextView = v.findViewById(R.id.my_downvote);
            irrelevantTextView = v.findViewById(R.id.my_irrelevant);

        }
        else {
            v = View.inflate(context, R.layout.their_message, null);
            ImageView avatar = v.findViewById(R.id.avatar);
            final TextView like = v.findViewById(R.id.thumbs_up);
            final TextView dislike = v.findViewById(R.id.thumbs_down);
            final TextView irrelevant = v.findViewById(R.id.irrelevant);

            final TextView replyTextView = v.findViewById(R.id.add_reply_text_their_message);
            final TextView viewAllReplyTextTheirMessage = v.findViewById(R.id.view_all_reply_their_message);
            replyTextView.setVisibility(View.GONE);
            viewAllReplyTextTheirMessage.setVisibility(View.GONE);

            commentTitle = v.findViewById(R.id.their_message_body);
            userName = v.findViewById(R.id.uname);
            dateTime = v.findViewById(R.id.their_comment_time);
            upVoteTextView = v.findViewById(R.id.their_upvote);
            downVoteTextView = v.findViewById(R.id.their_downvote);
            irrelevantTextView = v.findViewById(R.id.their_irrelevant);
            commentTitle.setText(replyItemArrayList.get(position).getCommentTitle());
            String photoUrl = replyItemArrayList.get(position).getPhotoUrl();
            if(!photoUrl.equalsIgnoreCase("NO_PROFILE_PIC")) {
                Log.d("testing",replyItemArrayList.get(position).getPhotoUrl());

                new RCDListAdapter.DownloadImageTask((ImageView) v.findViewById(R.id.avatar))
                        .execute(photoUrl);
            }
            userName.setText(replyItemArrayList.get(position).getUsername());
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy' 'hh:mm a", Locale.US);
            dateTime.setText(sdf.format(replyItemArrayList.get(position).getTimestamp()));

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
                    dislikesTableRef.child(replyItemArrayList.get(position).getCommentId()).child(creatorId).setValue(obj);
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
                    irrelevantTableRef.child(replyItemArrayList.get(position).getCommentId()).child(creatorId).setValue(obj);
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
                        dislikesTableRef.child(replyItemArrayList.get(position).getCommentId()).child(creatorId).removeValue();
                        like.setTextColor(Color.parseColor("#231F20"));
                        like.setText("Like");
                        dislike.setVisibility(View.VISIBLE);
                        irrelevant.setVisibility(View.VISIBLE);
                    }else if("Irrelevant".equalsIgnoreCase(like.getText().toString())){
                        //i.e user has  marked it irrelevant but now making it neutral
                        irrelevantTableRef.child(replyItemArrayList.get(position).getCommentId()).child(creatorId).removeValue();
                        like.setTextColor(Color.parseColor("#231F20"));
                        like.setText("Like");
                        dislike.setVisibility(View.VISIBLE);
                        irrelevant.setVisibility(View.VISIBLE);
                    }else if("Liked".equalsIgnoreCase(like.getText().toString())) {
                        // i.e User has already Liked it but now he maiy be setting it to neutral
                        likesTableRef.child(replyItemArrayList.get(position).getCommentId()).child(creatorId).removeValue();
                        like.setTextColor(Color.parseColor("#231F20"));
                        like.setText("Like");
                        dislike.setVisibility(View.VISIBLE);
                        irrelevant.setVisibility(View.VISIBLE);
                    }else{
                        //i.e User is clicking on like button
                        ILDItem obj = new ILDItem(googleSignInAccount.getEmail());
                        like.setText("Liked");
                        like.setTextColor(Color.parseColor("#4267b2"));
                        likesTableRef.child(replyItemArrayList.get(position).getCommentId()).child(creatorId).setValue(obj);
                        dislike.setVisibility(View.INVISIBLE);
                        irrelevant.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }

        //Displaying Upvote,Downvote,Irrelevant Counts

        likesTableRef.child(replyItemArrayList.get(position).getCommentId()).addValueEventListener(new ValueEventListener() {
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

        dislikesTableRef.child(replyItemArrayList.get(position).getCommentId()).addValueEventListener(new ValueEventListener() {
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

        irrelevantTableRef.child(replyItemArrayList.get(position).getCommentId()).addValueEventListener(new ValueEventListener() {
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
        v.setTag(replyItemArrayList.get(position).getCommentId());
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
