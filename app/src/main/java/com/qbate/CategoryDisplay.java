package com.qbate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;


    public class CategoryDisplay extends AppCompatActivity {

    public static Context categoryDisplayContext;
    public static int counter = 1;

    private CategoryListAdapter categoryListAdapter;
    private ArrayList<CategoryItem> categoryItemsList;
    private ListView listView;
    private GoogleSignInAccount googleSignInAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_display);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(Color.parseColor("#ECE8E8"));
        myToolbar.setTitle("Category");
        setSupportActionBar(myToolbar);
        categoryDisplayContext =this;
        listView = findViewById(R.id.category_list);

        //Checking if Preference manager is storing correct data or not!!!
        //Sometime First startActivity is called then after that only it is storing data in default shared preference
        // That's why its not giving correct user id but still all other functionality working fine..!!
        String userid = PreferenceManager.getDefaultSharedPreferences(this).getString("USERIDKEY", "defaultStringIfNothingFound");
        Log.d("testing27",userid);


        //googleSignInAccount = (GoogleSignInAccount) getIntent().getSerializableExtra("signInObject");
        SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("googleSignInObject", "");
        googleSignInAccount = gson.fromJson(json, GoogleSignInAccount.class);


        /*Cursor result = new DatabaseHelper(this).getCategoryTableData();
        while(result.moveToNext()){
            categoryItemsList.add(new CategoryItem(Integer.parseInt(result.getString(0)),
                    result.getString(1)));
        }*/

        categoryItemsList = new ArrayList<CategoryItem>();
        getCategoryData();
        Log.d("testing","List Size:" + categoryItemsList.size());
        //categoryListAdapter = new CategoryListAdapter(this,categoryItemsList);
        //listView.setAdapter(categoryListAdapter);
    }

    private void getCategoryData(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("category");
        // Read from the database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(categoryItemsList != null)
                    categoryItemsList.clear();
                for(DataSnapshot item:dataSnapshot.getChildren()){
                    CategoryItem ci = item.getValue(CategoryItem.class);
                    Log.d("testing","CategoryId:"+ci.getCategoryId() + " CategoryName:"+ci.getCategoryName());
                    categoryItemsList.add(ci);
                }
                categoryListAdapter = new CategoryListAdapter(categoryDisplayContext,categoryItemsList);
                listView.setAdapter(categoryListAdapter);
                Log.d("testing", "Value is: " + dataSnapshot.toString());

                //Database code at Firebase end ----- BE CAREFUL OTHERWISE REDUNDACY WILL OCCUR
                /* //for Creating Testing Topics for the given category
                for(CategoryItem ci:categoryItemsList){
                    TestDataTableCreatorFirebase.creatingTopicsTable(ci.getCategoryId(),ci.getCategoryName());
                }*/


                /* // currently handeled at adapter class
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        Intent intent = new Intent(CategoryDisplay.categoryDisplayContext,TopicsDisplay.class);
                        intent.putExtra("categoryId","" + categoryItemsList.get(position).getCategoryId());
                        intent.putExtra("categoryName","" + categoryItemsList.get(position).getCategoryName());
                        intent.putExtra("signInObject",googleSignInAccount);
                        categoryDisplayContext.startActivity(intent);
                    }
                });*/
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("testing", "Failed to read value.", error.toException());
            }
        });
    }

    //Need to implement this method as we need to
    // minimize app when back is pressed in this activity
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.category_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sign_out:
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();
                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // ...
                                Log.d("testing2","Signed Out from account!!");
                            }
                        });
                SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                prefsEditor.clear().commit();
                PreferenceManager.getDefaultSharedPreferences(this).edit().clear().commit();
                Intent intent = new Intent(this,FirebaseLogin.class);
                startActivity(intent);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
