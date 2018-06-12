package com.example.lior7.project1.Persistent_Data_Storage;

import  android.content.Context;
import  android.database.sqlite.SQLiteDatabase;
import  android.database.sqlite.SQLiteOpenHelper;


public class Controllerdb extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME="SqliteListviewDB";
    public Controllerdb(Context context) {
        super(context, DATABASE_NAME, null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table to insert data
        String query;
        query = "CREATE TABLE IF NOT EXISTS UserDetails(Id INTEGER PRIMARY KEY AUTOINCREMENT,Name VARCHAR,Age INTEGER,Score INTEGER);";
        db.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query ;
        query = "DROP TABLE IF EXISTS UserDetails";
        db.execSQL(query);
        onCreate(db);
    }
}
