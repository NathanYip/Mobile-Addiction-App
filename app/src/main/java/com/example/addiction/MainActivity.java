package com.example.addiction;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    ViewPager mViewPager;
    LinearLayout mDot;
    SlideAdapter sliderAdapter;

    TextView[] mDots;
    Button next, prev;

    private int mCurrentPage;
    String preferences_name = "tutorial";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mViewPager = findViewById(R.id.viewPager);
        mDot = findViewById(R.id.dots);
        prev = findViewById(R.id.prevButton);
        next = findViewById(R.id.nextButton);

        sliderAdapter = new SlideAdapter(this);
        mViewPager.setAdapter(sliderAdapter);

        startTutorial();

    }

    public void dotsIndicator(int position) {
        //Add dots at the bottom of the page
        mDots = new TextView[6];
        mDot.removeAllViews();

        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.white));

            mDot.addView(mDots[i]);
        }
        //Change current page dot to white
        if (mDots.length > 0) {
            mDots[position].setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    public void startTutorial() {
        //SharedPreferences to check if user has seen tutorial
        final SharedPreferences sharedPreferences = getSharedPreferences(preferences_name, 0);
        if (sharedPreferences.getBoolean("firstTime", true)) {

            dotsIndicator(0);
            mViewPager.addOnPageChangeListener(viewListener);

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentPage = mViewPager.getCurrentItem() + 1;

                    if (currentPage < mDots.length) {
                        mViewPager.setCurrentItem(currentPage);
                    } else {
                        Intent i = new Intent(MainActivity.this, Start.class);
                        startActivity(i);
                        sharedPreferences.edit().putBoolean("firstTime", false).apply();

                    }
                }
            });
            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(mCurrentPage - 1);
                }
            });


        } else {
            Intent i = new Intent(MainActivity.this, Start.class);
            startActivity(i);

        }
    }


    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            dotsIndicator(i);

            mCurrentPage = i;
            if (i == 0) {
                next.setEnabled(true);
                prev.setEnabled(false);
                prev.setAlpha(0);

                next.setText("Next");
                prev.setText("");

            } else if (i == 5) {
                next.setEnabled(true);
                prev.setEnabled(true);
                prev.setAlpha(1);

                next.setText("Finish");
                prev.setText("Back");

            } else {
                next.setEnabled(true);
                prev.setEnabled(true);
                prev.setAlpha(1);

                next.setText("Next");
                prev.setText("Back");
            }

        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };
}