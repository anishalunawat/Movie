package com.nanodegree.anisha.movie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anisha on 21/2/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    //Static Variables
    //Database version
    private static final int DATABASE_VERSION = 1;

    //Databse name
    private static final String DATABASE_NAME = "favourite";

    //Table Name
    private static final String TABLE_NAME = "movies";

    // Movie Table Columns names
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String POSTER_PATH = "poster_path";
    private static final String RELEASE_DATE = "release_date";
    private static final String OVERVIEW = "overview";
    private static final String RATING = "rating";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY," + TITLE + " TEXT," + POSTER_PATH + " TEXT,"
                + RELEASE_DATE + " TEXT," + OVERVIEW + " TEXT," + RATING + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public void addMovie(GetMovieInfo info) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e("id", info.getID());
        ContentValues values = new ContentValues();
        values.put(ID, info.getID());
        values.put(TITLE, info.getTITLE());
        values.put(POSTER_PATH, info.getPOSTER_PATH());
        values.put(RELEASE_DATE, info.getRELEASE_DATE());
        values.put(OVERVIEW, info.getOVERVIEW());
        values.put(RATING, info.getRATING());

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }


    public List<GetMovieInfo> getAllFavourite() {
        List<GetMovieInfo> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                GetMovieInfo info = new GetMovieInfo(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));

                // Adding contact to list
                contactList.add(info);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

}
