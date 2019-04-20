package com.qbate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "AppLocal.db";
    public static final String TABLE_NAME = "cattable";
    public static final String COL1 = "category_id";
    public static final String COL2 = "category_name";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME,null,1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CATEGORY_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + COL1 + " INTEGER PRIMARY KEY," + COL2 + " Varchar(30)" + ")";
        Log.d("testing",CREATE_CATEGORY_TABLE);
        sqLiteDatabase.execSQL(CREATE_CATEGORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(int categoryId, String categoryName){
        SQLiteDatabase db = this.getWritableDatabase();
        /*Log.d("testing","\"INSERT INTO \"+ TABLE_NAME +\"(\"+ COL1 + \", \" + COL2 + \") \" +\n" +
                "                \"VALUES (\"+ categoryId +\", +'\" + categoryName + \"')\"");
        db.execSQL("INSERT INTO "+ TABLE_NAME +"("+ COL1 + ", " + COL2 + ") " +
                "VALUES ("+ categoryId +",'" + categoryName + "');");*/
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1,categoryId);
        contentValues.put(COL2,categoryName);
        if(db.insert(TABLE_NAME,null,contentValues) == -1)
            return false;
        return true;
    }

    public Cursor getCategoryTableData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + TABLE_NAME,null);
        Log.d("testing","Cursor count:" + result.getCount());
        Toast.makeText(CategoryDisplay.categoryDisplayContext,"Count in cursor:" + result.getCount(),Toast.LENGTH_LONG).show();
        return result;
    }
}
