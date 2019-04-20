package com.qbate;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;


public class Splash extends AppCompatActivity {

    //private static final int REQUSET_INTERNET = 57;
    //private SharedPreferences permissionStatus;
    private Context splashContext;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        //dbHelper = new DatabaseHelper(this);
        //to download category JSON file
        splashContext = this;

        //Database code at Firebase end ----- BE CAREFUL OTHERWISE REDUNDACY WILL OCCUR
        //creating Category Table at Firebase
        //createCategoryTable();

        Intent intent = new Intent(splashContext,FirebaseLogin.class);
        startActivity(intent);


        //new getCategoryJSON().execute();

        /*permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        checkAndRequestPermissions();*/
        //Toast.makeText(this,"Permissions Over",Toast.LENGTH_LONG).show();


        finish();
    }

    void createCategoryTable(){
        ArrayList<String> categoryList = new ArrayList<String>();
        categoryList.add("Politics");
        categoryList.add("Science");
        categoryList.add("Philosophy");
        categoryList.add("Ethics");
        categoryList.add("Religion");
        categoryList.add("Technology");
        categoryList.add("Education");
        TestDataTableCreatorFirebase.createCategoryTable(categoryList);
    }

    /*

    class getCategoryJSON extends AsyncTask<String, Integer , Boolean> {
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            //Intent intent = new Intent(getApplicationContext(), FirebaseLogin.class);
            Intent intent = new Intent(splashContext,CategoryDisplay.class);
            startActivity(intent);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                //connection creation
                URL url = new URL("https://qbate.000webhostapp.com/qbate/get_category.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                //connection created
                //now we will download the json data line by line
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder result = new StringBuilder();
                String line;
                while((line = reader.readLine()) != null) {
                    result.append(line);
                }
                Log.d("tag",result.toString());
                JSONArray jsonArray = new JSONArray(result.toString());
                Log.d("categorywisedatatesting","Length of Json Array:" + jsonArray.length());
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    Log.d("categorywisedatatesting",""+jsonObject.get("category_id") +
                                " " + jsonObject.get("category_name"));
                    if(dbHelper.insertData(Integer.parseInt(jsonObject.get("category_id").toString()),jsonObject.get("category_name").toString())){
                        Log.d("categorywisedatatesting","Table Updated");
                    }
                }
                httpURLConnection.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.d("Json",e.toString());
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("Json",e.toString());
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("Json",e.toString());
            }


            return null;
        }
    }

     /*
    //checking internet permissions
    protected void checkAndRequestPermissions(){
        if(ActivityCompat.checkSelfPermission(CategoryDisplay.this,Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED){
            //permission is granted
        }else{
            //we have to request for permissions
            if(ActivityCompat.shouldShowRequestPermissionRationale(CategoryDisplay.this,Manifest.permission.INTERNET)){
                Toast.makeText(this,"Internet is needed to run this app!!!",Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(CategoryDisplay.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Call and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(CategoryDisplay.this, new String[]{Manifest.permission.INTERNET}, REQUSET_INTERNET);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }else if (permissionStatus.getBoolean(new String[]{Manifest.permission.INTERNET}[0], false)) {
                 //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(CategoryDisplay.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUSET_INTERNET);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant  Call and Location", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }else{
                ActivityCompat.requestPermissions(CategoryDisplay.this,new String[] {Manifest.permission.INTERNET},REQUSET_INTERNET);
            }
            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(new String[]{Manifest.permission.INTERNET}[0], true);
            editor.commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUSET_INTERNET){
            //received permission for Internet
            //checking if the permission is granted
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //Internet Permission Granted
                Log.d("Permission Status","Internet Permission Granted");
            }else{
                Toast.makeText(this,"Internet Permission is not granted",Toast.LENGTH_SHORT).show();
                Log.d("Permission Status","Internet Permission not Granted");
                AlertDialog.Builder builder = new AlertDialog.Builder(CategoryDisplay.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(CategoryDisplay.this, new String[]{Manifest.permission.INTERNET}, REQUSET_INTERNET);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        }else{
            super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        }
    }
    */
}
