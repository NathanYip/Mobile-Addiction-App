package com.example.addiction;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class weeklyStat extends AppCompatActivity {

    public static final String DATABASE_NAME = "progress.db";
    public static final String TABLE_NAME = "progress_table";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_DATE = "DATE";
    public static final String COLUMN_TIME = "TIME";
    public static final String COLUMN_TIMER = "TIMER";
    public static final String COLUMN_NEGATIVE = "NEGATIVE";
    public static final String COLUMN_TOTAL = "TOTAL";
    DatabaseHelper myDB;
    Button back, daily;
    BarChart barChart;
    //Save last 6 days onto a string to compare
    String day1;
    String day2;
    String day3;
    String day4;
    String day5;
    String day6;
    String day7;

    ArrayList<String> sevenDays = new ArrayList<>();
    ArrayList<Float> sumOfEachDays = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_stat);


        myDB = new DatabaseHelper(this);
        back = findViewById(R.id.backButton);
        daily = findViewById(R.id.dailyButton);
        barChart = findViewById(R.id.barChart);


        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date = simpleDateFormat.format(d);

        sevenDays.add(date);
        //Save the last 7 days onto each variable
        try {
            day1 = date;
            day2 = saveDates(day1);
            day3 = saveDates(day2);
            day4 = saveDates(day3);
            day5 = saveDates(day4);
            day6 = saveDates(day5);
            day7 = saveDates(day6);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        //Add the total of each day to an ArrayList

        sumOfEachDays.add(getSumPrevious(day1));
        sumOfEachDays.add(getSumPrevious(day2));
        sumOfEachDays.add(getSumPrevious(day3));
        sumOfEachDays.add(getSumPrevious(day4));
        sumOfEachDays.add(getSumPrevious(day5));
        sumOfEachDays.add(getSumPrevious(day6));
        sumOfEachDays.add(getSumPrevious(day7));

        buildChart();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(weeklyStat.this, Results.class);
                startActivity(i);
            }
        });

        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(weeklyStat.this, dailyStat.class);
                startActivity(i);
            }
        });
    }

    public void buildChart() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();

        //Add total of each day to the BarEntry

        barEntries.add(new BarEntry(2f, sumOfEachDays.get(0)));
        barEntries.add(new BarEntry(4f, sumOfEachDays.get(1)));
        barEntries.add(new BarEntry(6f, sumOfEachDays.get(2)));
        barEntries.add(new BarEntry(8f, sumOfEachDays.get(3)));
        barEntries.add(new BarEntry(10f, sumOfEachDays.get(4)));
        barEntries.add(new BarEntry(12f, sumOfEachDays.get(5)));
        barEntries.add(new BarEntry(14f, sumOfEachDays.get(6)));

        Log.d("times", String.valueOf(sevenDays));

        BarDataSet barDataSet = new BarDataSet(barEntries, "Total Focused Time in the last Week");

        dataSets.add((IBarDataSet) barDataSet);
        BarData Data = new BarData(dataSets);

        //Set up barchart

        barChart.setTouchEnabled(false);
        barChart.setDragEnabled(false);
        barChart.setScaleEnabled(false);
        barChart.animateXY(1000, 1000);
        barChart.setHorizontalScrollBarEnabled(true);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.invalidate();
        barChart.setFitBars(true);
        barChart.setBackgroundColor(getResources().getColor(R.color.textWhite));
        barDataSet.setValueTextColor(R.color.textWhite);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        YAxis rightYAxis = barChart.getAxisRight();
        rightYAxis.setEnabled(false);
        barChart.setData(Data);


    }
    public float getSumPrevious(String dates) {

        //Calculate the day before the given date

        float returnValue = 0;

        String query = "SELECT SUM(" + COLUMN_TOTAL + ") FROM " + TABLE_NAME + " WHERE " + COLUMN_DATE + " = " + "'" + dates + "'";
        SQLiteDatabase db = myDB.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            return c.getInt(0);
        } else {
            return returnValue;
        }

    }

    public String saveDates(String dates) throws ParseException {

        //Get the last 7 days

        DateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        Date date = simpleDateFormat.parse(dates);
        Calendar c = Calendar.getInstance();

        c.setTime(date);
        c.add(Calendar.DATE, -1);
        Date dayBefore = c.getTime();


        String returnDate = simpleDateFormat.format(dayBefore);
        sevenDays.add(returnDate);

        return returnDate;

    }
}






