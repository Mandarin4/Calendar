package com.calendarszametkami.calendar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CalDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Cal.db";

    private static final int DATABASE_VERSION = 1;

    public CalDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the Calendar table
        String SQL_CREATE_CAL_TABLE =  "CREATE TABLE " + CalContract.CalEntry.TABLE_NAME + " ("
                + CalContract.CalEntry.COLUMN_DAY + " TEXT PRIMARY KEY NOT NULL , "
                + CalContract.CalEntry.COLUMN_NOTE + " TEXT NOT NULL);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_CAL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
