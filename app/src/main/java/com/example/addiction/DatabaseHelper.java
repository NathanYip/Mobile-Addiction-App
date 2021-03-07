package com.example.addiction;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "progress.db";
    public static final String TABLE_NAME = "progress_table";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_DATE = "DATE";
    public static final String COLUMN_TIME = "TIME";
    public static final String COLUMN_TIMER = "TIMER";
    public static final String COLUMN_NEGATIVE = "NEGATIVE";
    public static final String COLUMN_TOTAL = "TOTAL";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,DATE TEXT, TIME TEXT, TIMER INTEGER, NEGATIVE INTEGER, TOTAL INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertData(long startingTime, long remainingTime, long negative) {
        ContentValues contentValues = new ContentValues();
        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date = simpleDateFormat.format(d);

        DateFormat df = new SimpleDateFormat("h:mm a");
        String currentTime = df.format(Calendar.getInstance().getTime());

        long total = startingTime - remainingTime;

        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_TIME, currentTime);
        contentValues.put(COLUMN_TIMER, startingTime);
        contentValues.put(COLUMN_NEGATIVE, negative);
        contentValues.put(COLUMN_TOTAL, total);

        Log.d("times", "starting " + startingTime + " negative " + negative + " total" + total);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }


    public long getTotal() {

        long total = 0;

        String query = "SELECT SUM(" + COLUMN_TOTAL + ") FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            return c.getInt(0);
        } else {
            return total;
        }

    }


    public long getTotalToday() {

        long total = 0;

        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date = simpleDateFormat.format(d);

        String query = "SELECT SUM(" + COLUMN_TOTAL + ") FROM " + TABLE_NAME + " WHERE " + COLUMN_DATE + " = " + "'" + date + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            return c.getInt(0);
        } else {
            return total;
        }

    }

    public long getNegativeToday() {

        long total = 0;

        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date = simpleDateFormat.format(d);

        String query = "SELECT SUM(" + COLUMN_NEGATIVE + ") FROM " + TABLE_NAME + " WHERE " + COLUMN_DATE + " = " + "'" + date + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            return c.getInt(0);
        } else {
            return total;
        }

    }

    public void dropTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE " + TABLE_NAME);
        Log.d("times", "dropped");
    }


}
