package com.example.addiction;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class dailyStat extends AppCompatActivity {

    DatabaseHelper myDB;
    Button back, weekly;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_stat);

        myDB = new DatabaseHelper(this);
        back = findViewById(R.id.backButton);
        weekly = findViewById(R.id.weeklyButton);

        buildChart();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dailyStat.this, Results.class);
                startActivity(i);
            }
        });

        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dailyStat.this, weeklyStat.class);
                startActivity(i);
            }
        });

    }


    public void buildChart() {

        List<PieEntry> pieEntryList = new ArrayList<>();

        long negative = myDB.getNegativeToday();
        long total = myDB.getTotalToday();

        //Check if there is any value in breaks if not dont add

        if (negative != 0) {
            pieEntryList.add(new PieEntry(negative, "Todays Total Break Took in seconds"));
        }


        pieEntryList.add(new PieEntry(total, "Todays Total Focus Time in seconds"));

        PieDataSet dataSet = new PieDataSet(pieEntryList, "Daily statistic");
        dataSet.setColors(ColorTemplate.PASTEL_COLORS);
        dataSet.setValueTextSize(20);
        PieData data = new PieData(dataSet);


        //Set and initialise pie chart
        PieChart pieChart = (PieChart) findViewById(R.id.chart);
        pieChart.setData(data);
        pieChart.animateY(1000);
        pieChart.invalidate();

    }


}
