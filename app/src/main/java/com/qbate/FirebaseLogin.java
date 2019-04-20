package com.qbate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class FirebaseLogin extends AppCompatActivity {

    static final int RC_SIGN_IN = 1234;
    private FirebaseAuth mAuth;
    private SignInButton googleSignInButton;
    private Button logoutButton;
    private GoogleSignInClient mGoogleSignInClient;
    private ProgressBar progressBar;

    //for firebase database
    final private DatabaseReference usersTableRef = FirebaseDatabase.getInstance().getReference("users");
    final private Query query = FirebaseDatabase.getInstance().getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firebase_login);

        FirebaseApp.initializeApp(this);

        googleSignInButton = findViewById(R.id.google_login_button);
        logoutButton = findViewById(R.id.logout_button);
        progressBar = findViewById(R.id.progress_circular);

        mAuth = FirebaseAuth.getInstance();

        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                            .requestEmail()
                                            .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.google_login_button:
                            signIn();
                            break;
                        // ...
                    }
                }
        });

    }

    private void signIn() {
        progressBar.setVisibility(View.VISIBLE);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        final GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
            Toast.makeText(this,"Sign In to google Account " + account.getEmail(),Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, CategoryDisplay.class);
            //intent.putExtra("signInObject",account); // in case of designing navigation drawer in future

            //creating shared preference for GoogleSignInObject
            SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(account);
            prefsEditor.putString("googleSignInObject", json);
            prefsEditor.commit();

            //checking if data is already stored in firebase
            Query result = query.orderByChild("email").equalTo(account.getEmail());
            result.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getChildrenCount() > 0){
                        //user data is already present
                        for(DataSnapshot item:dataSnapshot.getChildren()){
                            User user = item.getValue(User.class);
                            Log.d("testing2","OnStart:User Already present:"+"userid:" + user.getUserid() + " email:" + user.getEmail());
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("USERIDKEY", user.getUserid()).apply();
                        }
                    }else{
                        //user data is not present
                        String userid = usersTableRef.push().getKey();
                        String email = account.getEmail();
                        usersTableRef.child(userid).setValue(new User(userid,email));
                        Log.d("testing2","onStart:User created:"+"userid:" + userid + " email:" + email);
                        //saving useridKey to a shared preference will be used at comments like/dislike/irrelevant information
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("USERIDKEY", userid).apply();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            //starting Activity
            startActivity(intent);
            finish();
            }
        }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            final GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Toast.makeText(this,"Sign In to google Account " + account.getEmail(),Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, CategoryDisplay.class);
            //intent.putExtra("signInObject",account);

            //creating shared preference for GoogleSignInObject
            SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(account);
            prefsEditor.putString("googleSignInObject", json);
            prefsEditor.commit();

            //checking if data is already stored in firebase
            Query result = query.orderByChild("email").equalTo(account.getEmail());
            result.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getChildrenCount() > 0){
                        //user data is already present
                        for(DataSnapshot item:dataSnapshot.getChildren()){
                            User user = item.getValue(User.class);
                            Log.d("testing2","HandleSignInResult:User Already present:"+"userid:" + user.getUserid() + " email:" + user.getEmail());
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("USERIDKEY", user.getUserid()).apply();
                        }
                    }else{
                        //user data is not present in firebase
                        String userid = usersTableRef.push().getKey();
                        String email = account.getEmail();
                        usersTableRef.child(userid).setValue(new User(userid,email));
                        Log.d("testing2","handleSignINResult:User created:"+"userid:" + userid + " email:" + email);
                        //saving useridKey to a shared preference will be used at comments like/dislike/irrelevant information
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("USERIDKEY", userid).apply();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            //starting activity
            startActivity(intent);
            finish();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("SignInTag", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this,"Sign In Failed",Toast.LENGTH_LONG).show();
        }
    }
}
