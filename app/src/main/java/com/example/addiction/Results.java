package com.example.addiction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Results extends AppCompatActivity {

    String preferences_name = "tutorial";
    DatabaseHelper myDB;
    Button daily, weekly;
    TextView totalTime, totalTimeToday, wall;

    public static final String DATABASE_NAME = "progress.db";
    public static final String TABLE_NAME = "progress_table";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_DATE = "DATE";
    public static final String COLUMN_TIME = "TIME";
    public static final String COLUMN_TIMER = "TIMER";
    public static final String COLUMN_NEGATIVE = "NEGATIVE";
    public static final String COLUMN_TOTAL = "TOTAL";


    @Override
    public void onBackPressed() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        myDB = new DatabaseHelper(this);
        List<progressClass> Progress = new ArrayList<>();


        totalTime = findViewById(R.id.totalTime);
        totalTimeToday = findViewById(R.id.totalTimeToday);
        wall = findViewById(R.id.textView3);
        daily = findViewById(R.id.dailyStat);
        weekly = findViewById(R.id.weeklyStat);

        getAllToday();

//        myDB.getAllToday();
        long total = myDB.getTotal();
        long todaysTotal = myDB.getTotalToday();

        totalTime.append(convTime(total));
        totalTimeToday.append(convTime(todaysTotal));


        long negative = myDB.getNegativeToday();
        long total2 = myDB.getTotalToday();


        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Results.this, dailyStat.class);
                startActivity(i);

            }
        });

        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Results.this, weeklyStat.class);
                startActivity(i);

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.result:
                Intent intentResult = new Intent(this, Results.class);
                startActivity(intentResult);
                return true;

            case R.id.home:
                Intent intentResult2 = new Intent(this, Start.class);
                startActivity(intentResult2);
                return true;

            case R.id.reset:
                SharedPreferences sharedPreferences = getSharedPreferences(preferences_name, 0);
                sharedPreferences.edit().putBoolean("firstTime", true).apply();
                return true;

            case R.id.quote:
                Intent intentResult3 = new Intent(this, Quote.class);
                startActivity(intentResult3);
                return true;


        }
        return super.onOptionsItemSelected(item);
    }


    public String convTime(long time) {

        time = time * 1000;

        int seconds = (int) (time / 1000) % 60;
        int minutes = (int) (time / (1000 * 60) % 60);
        int hours = (int) ((time / (1000 * 60 * 60)) % 24);

        String total;
        total = " " + hours + " h " + minutes + " m ";
        if (seconds < 10) total += "0";
        total += seconds + " s";

//        Log.d("times", "total times"+String.valueOf(time));
//
//        Log.d("times", total);

        return total;
    }

    public void getAllToday() {

        ArrayList<progressClass> Progress = new ArrayList<>();

        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date = simpleDateFormat.format(d);

        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor c = null;


        SQLiteDatabase db = myDB.getWritableDatabase();

        try {
            c = db.rawQuery(query, null);

            c.moveToFirst();
            progressClass p;

            if (c.moveToFirst()) {
                do {
                    p = new progressClass();
                    p.setDate(c.getString(c.getColumnIndex(COLUMN_DATE)));
                    p.setNegative(c.getString(c.getColumnIndex(COLUMN_NEGATIVE)));
                    p.setTime(c.getString(c.getColumnIndex(COLUMN_TIME)));
                    p.setTimer(c.getString(c.getColumnIndex(COLUMN_TIMER)));
                    p.setTotal(c.getString(c.getColumnIndex(COLUMN_TOTAL)));

                    Progress.add(p);

                    //convert string to long
                    long l = Long.parseLong(p.getTotal());
                    long l2 = Long.parseLong(p.getNegative());


                    //convert seconds to HH:mm:ss

                    String time = convTime(l);
                    String negative = convTime(l2);

                    wall.append(p.getDate() + " " + p.getTime() + "\nTime Focused: " + time + "\nBreak: " + negative + "   \n\n");


                } while (c.moveToNext());
            }
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }

    }


}
