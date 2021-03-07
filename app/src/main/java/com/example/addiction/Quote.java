package com.example.addiction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class Quote extends AppCompatActivity {

    String preferences_name = "tutorial";
    TextView quote, author;
    Button next;

    ArrayList<ArrayList<String>> motivation = new ArrayList<>();

    String quoteLibrary[][] = {
            {"'It does not matter how slowly you go as long as you do not stop.'", "Confucius"},
            {"'The most effective way to do it, is to do it.'", "Amelia Earhart"},
            {"'Our greatest weakness lies in giving up. The most certain way to succeed is always to try just one more time.'", "Thomas A. Edison"},
            {"'If you're going through hell, keep going'", "Winston Churchill"},
            {"'Nobody realizes that some people expend tremendous energy merely to be normal.'", "Albert Camus"},
            {"'You have to be odd to be number one'", "Dr. Seuss"},
            {"'Imagination is more important than knowledge.'", "Albert Einstein"},
            {"'All our dreams can come true, if we have the courage to pursue them.'", "Walt Disney"},
            {"'I never dreamed about success, I worked for it.'", "Estée Lauder"},
            {"'Never bend your head. Always hold it high. Look the world straight in the eye'", "Helen Keller"},
            {"'Victory is not always winning the battle...but rising every time you fall.'", "Napoleon Bonaparte"}

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);

        quote = findViewById(R.id.quote);
        author = findViewById(R.id.author);
        next = findViewById(R.id.button);


        for (int i = 0; i < quoteLibrary.length; i++) {
            ArrayList<String> tempArray = new ArrayList<>();
            tempArray.add(quoteLibrary[i][0]);
            tempArray.add(quoteLibrary[i][1]);

            motivation.add(tempArray);
        }

        randomQuote();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                randomQuote();
            }
        });

    }

    public void randomQuote() {
        Random random = new Random();

        int rand = random.nextInt(motivation.size());

        ArrayList<String> set = motivation.get(rand);

        quote.setText(set.get(0));
        author.setText(set.get(1));

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
