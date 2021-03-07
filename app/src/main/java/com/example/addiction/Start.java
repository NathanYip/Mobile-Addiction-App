package com.example.addiction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class Start extends AppCompatActivity {

    Spinner spinner;
    Button start;

    String preferences_name = "tutorial";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        spinner = findViewById(R.id.spinner);
        start = findViewById(R.id.button2);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.time_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        spinner.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ((TextView) spinner.getSelectedView()).setTextColor(Color.WHITE);

            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = spinner.getSelectedItem().toString();
                int time = getTime(text);
                Intent i = new Intent(Start.this, Timer.class);
                i.putExtra("time", time);
                startActivity(i);
                finish();
            }
        });

    }

    public int getTime(String selected) {

        String numberOnly = selected.replaceAll("[^0-9]", "");

        int time = Integer.parseInt(numberOnly);

        Log.d("parsing", String.valueOf(time));

        return time;
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
}
