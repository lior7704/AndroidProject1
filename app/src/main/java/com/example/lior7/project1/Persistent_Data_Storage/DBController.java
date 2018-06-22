package com.example.lior7.project1.Persistent_Data_Storage;

import android.content.ContentValues;
import  android.content.Context;
import android.database.Cursor;
import  android.database.sqlite.SQLiteDatabase;
import  android.database.sqlite.SQLiteOpenHelper;

import com.example.lior7.project1.Object_Classes.UserDetails;

import java.util.ArrayList;
import java.util.List;


public class DBController extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME="UserDetailsDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USER_DETAILS = "userDetails";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_AGE = "age";
    private static final String KEY_SCORE = "score";
    private static final String KEY_DIFFICULTY = "difficulty";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";


    public DBController(Context context) {
        super(context, DATABASE_NAME, null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table to insert data
        String query = "CREATE TABLE " + TABLE_USER_DETAILS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_AGE + " INTEGER,"
                + KEY_LATITUDE + " REAL,"
                + KEY_LONGITUDE + " REAL,"
                + KEY_SCORE + " INTEGER,"
                + KEY_DIFFICULTY + " TEXT " + ")";
        db.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS " + TABLE_USER_DETAILS;
        db.execSQL(query);
        onCreate(db);
    }

    public void add(UserDetails userDetails) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_NAME, userDetails.getName());
        values.put(KEY_AGE, userDetails.getAge());
        values.put(KEY_SCORE, userDetails.getScore());
        values.put(KEY_DIFFICULTY, userDetails.getDifficulty());
        values.put(KEY_LATITUDE, userDetails.getLatitude());
        values.put(KEY_LONGITUDE, userDetails.getLongitude());

        db.insert(TABLE_USER_DETAILS, null, values);
        db.close();
    }

    public void delete(int delID){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER_DETAILS, KEY_ID + "=" + delID, null);
        db.close();
    }

    public UserDetails getUserDetails(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER_DETAILS,new String[]{KEY_ID,KEY_NAME, KEY_AGE, KEY_SCORE, KEY_DIFFICULTY,KEY_LATITUDE,KEY_LONGITUDE},
                KEY_ID+"=?", new String[]{String.valueOf(id)},null,null,null,null);
        if(cursor != null) {
            cursor.moveToFirst();
            UserDetails ud = new UserDetails(cursor.getInt(0),cursor.getString(1), cursor.getInt(2),cursor.getInt(3),cursor.getString(4),cursor.getDouble(5),cursor.getDouble(6));
            return ud;
        }
        else
            return null;
    }

    public List<UserDetails> getAllUsersDetails() {
        List<UserDetails> userDetailsList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        // Select All
        String selectQuery = "SELECT  * FROM " + TABLE_USER_DETAILS + " ORDER BY " + KEY_SCORE + " DESC";
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Add all rows to the list
        if (cursor.moveToFirst()) {
            do {
                UserDetails ud = new UserDetails();
                ud.setId(Integer.parseInt(cursor.getString(0)));
                ud.setName(cursor.getString(1));
                ud.setAge(Integer.parseInt(cursor.getString(2)));
                ud.setScore(cursor.getInt(3));
                ud.setDifficulty(cursor.getString(4));
                ud.setLatitude(Double.parseDouble(cursor.getString(5)));
                ud.setLongitude(Double.parseDouble(cursor.getString(6)));

                userDetailsList.add(ud);
            } while (cursor.moveToNext());
        }
        return userDetailsList;
    }
}
