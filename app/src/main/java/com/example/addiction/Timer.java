package com.example.addiction;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Timer extends AppCompatActivity {

    private long timeRemaining;
    private long negativeCount;
    private int time;
    private String countdown;
    private boolean timerRunning;
    private int breakLeft = 3;
    private int startTimeSeconds;
    private String countMax;

    DatabaseHelper myDB;
    CountDownTimer timer;
    Chronometer mChronometer;
    TextView countdownTimer, limit;
    ConstraintLayout constraintLayout;
    Button breakButton, quitButton, endButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        countdownTimer = findViewById(R.id.countdownTimer);
        breakButton = findViewById(R.id.breakButton);
        quitButton = findViewById(R.id.quitButton);
        endButton = findViewById(R.id.endButton);
        constraintLayout = findViewById(R.id.timerLayout);
        limit = findViewById(R.id.limit);
        mChronometer = findViewById(R.id.chronometer);

        myDB = new DatabaseHelper(this);


        Intent intent = getIntent();
        time = intent.getIntExtra("time", 0);

        timeRemaining = convTime(time);
        startTimeSeconds = time * 60;

        buttonPressed();
        setLimit(time);

        Log.d("times", String.valueOf(time));


        //If negative countdown reaches the limit, timer starts again and user has no more breaks
        mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {

                Log.d("times", String.valueOf(mChronometer.getText()));
                Log.d("times", countMax);

                if (mChronometer.getText().toString().equalsIgnoreCase(countMax)) {
                    pauseNegative();
                    startTimer();
                    breakButton.setAlpha(0);
                    breakButton.setEnabled(false);

                    Toast.makeText(Timer.this, "You have reached your break limit", Toast.LENGTH_SHORT).show();

                }

            }
        });

        breakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed();
            }
        });
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                breakTimer();
            }
        });
    }

    public void buttonPressed() {
        if (timerRunning) {
            pauseTimer();

        } else {
            startTimer();
            pauseNegative();
        }
    }


    public void startTimer() {

        //Initiate countdown
        timer = new CountDownTimer(timeRemaining, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemaining = millisUntilFinished;
                updateTimer();

            }

            @Override
            public void onFinish() {

                countdown = "0:00";

                endButton.setText("End");
                endButton.setAlpha(1);
                endButton.setEnabled(true);

                breakButton.setAlpha((float) 0.5);
                breakButton.setEnabled(false);

                quitButton.setAlpha((float) 0.5);
                quitButton.setEnabled(false);

                endButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Timer.this, Results.class);
                        startActivity(i);

                        try {
                            insertData(startTimeSeconds, countdown, (String) mChronometer.getText());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }
        }
                .start();

        timerRunning = true;
        breakButton.setText("Break");

        if (breakLeft == 0) {
            breakButton.setAlpha(Float.parseFloat("0.5"));
            breakButton.setEnabled(false);
        }

    }

    public void startPenaltyTimer() {


        if (negativeCount != 0) {
            mChronometer.setBase(mChronometer.getBase() + SystemClock.elapsedRealtime() - negativeCount);
        } else {
            mChronometer.setBase(SystemClock.elapsedRealtime());
        }

        mChronometer.setAlpha(1);
        limit.setAlpha(1);

        mChronometer.start();
    }

    public void updateTimer() {

        //Constantly updates the timer
        int minutes = (int) timeRemaining / 60000;
        int seconds = (int) timeRemaining % 60000 / 1000;

        countdown = " " + minutes;
        countdown += ":";
        if (seconds < 10) countdown += "0";
        countdown += seconds;

        countdownTimer.setText(countdown);

    }

    public void pauseTimer() {

        //Pause main countdown if user wants a break
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure?");
        builder.setMessage("Taking a break will affect your final score. You have " + breakLeft + " left!");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                timer.cancel();
                timerRunning = false;
                breakButton.setText("Resume");
                breakLeft--;
                startPenaltyTimer();


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public void pauseNegative() {
        //Pause countdown for break time
        negativeCount = SystemClock.elapsedRealtime();
        mChronometer.stop();
    }

    public void breakTimer() {

        //Dialog pops up to confirm user wants to take a break

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure?");
        builder.setMessage("You will end your focus time");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                timer.cancel();
                endButton.setText("End");
                endButton.setAlpha(1);
                endButton.setEnabled(true);

                breakButton.setAlpha((float) 0.5);
                breakButton.setEnabled(false);

                quitButton.setAlpha((float) 0.5);
                quitButton.setEnabled(false);

                endButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Timer.this, Results.class);
                        startActivity(i);

                        try {
                            insertData(startTimeSeconds, countdown, (String) mChronometer.getText());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public long convTime(int time) {

        long remainingTime = time * 60000;

        return remainingTime;

    }

    public void onBackPressed() {
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onUserLeaveHint() {

        //notification pops up if user closes the app

        super.onUserLeaveHint();
        Intent intent = new Intent();
        PendingIntent pi = PendingIntent.getActivity(Timer.this, 0, intent, 0);
        Notification n = new Notification.Builder(Timer.this)
                .setTicker("Ticker")
                .setContentTitle("Phone Addiction")
                .setContentText("Leaving the app will end your progress!")
                .setVibrate(new long[]{Notification.DEFAULT_VIBRATE})
                .setPriority(Notification.PRIORITY_MAX)
                .setSmallIcon(R.drawable.icontimer)
                .setContentIntent(pi).getNotification();


        n.flags = Notification.FLAG_AUTO_CANCEL;

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(0, n);

        timer.cancel();
        startPenaltyTimer();

        endButton.setAlpha(1);
        endButton.setEnabled(true);

        breakButton.setAlpha(0);
        breakButton.setEnabled(false);

        quitButton.setAlpha(0);
        quitButton.setEnabled(false);


        // If user doesnt click on resume within time count, countdown will stop and end
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                constraintLayout.setBackgroundColor(getResources().getColor(R.color.red));
                pauseNegative();

                endButton.setAlpha(1);
                endButton.setEnabled(true);

                breakButton.setAlpha(0);
                breakButton.setEnabled(false);

                quitButton.setAlpha(0);
                quitButton.setEnabled(false);

                endButton.setText("Results");
                endButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(Timer.this, Results.class);
                        startActivity(i);

                        try {
                            insertData(startTimeSeconds, countdown, (String) mChronometer.getText());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });


            }

        };

        Handler handler = new Handler();
        handler.postDelayed(runnable, 30000);

        // if user clicks on resume, goes back to normal

//        endButton.setText("Resume");
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startTimer();
                pauseNegative();

                endButton.setAlpha(0);
                endButton.setEnabled(false);

                breakButton.setAlpha(1);
                breakButton.setEnabled(true);

                quitButton.setAlpha(1);
                quitButton.setEnabled(true);
            }
        });

    }

    public void insertData(int startingTime, String endingTime, String negativeTime) throws ParseException {

        //insert record to database

        long remainingTime;
        long negative;

        if (endingTime == "00:00") {
            remainingTime = 0;
        } else {
            remainingTime = convertSeconds(endingTime);

        }

        negative = convertSeconds(negativeTime);

        myDB.insertData(startingTime, remainingTime, negative);

        Log.d("times", "starting " + startingTime + " remaining " + remainingTime + " negative " + negative);

    }

    public long convertSeconds(String time) throws ParseException {

        //convert format mm:dss to seconds

        DateFormat dateFormat = new SimpleDateFormat("mm:ss");
        Date reference = dateFormat.parse("00:00");
        Date date = dateFormat.parse(time);
        long seconds = (date.getTime() - reference.getTime()) / 1000L;

        return seconds;
    }

    public void setLimit(int time) {

        Log.d("times", "inside func " + String.valueOf(time));
        // Set time limit for breaks depending on how long
        switch (time) {
            case 5:
                limit.setText("/01:00");
                countMax = "01:00";
                break;
            case 10:
                limit.setText("/02:00");
                countMax = "02:00";
                break;
            case 15:
                limit.setText("/03:00");
                countMax = "03:00";
                break;
            case 20:
                limit.setText("/04:00");
                countMax = "04:00";
                break;
            case 30:
                limit.setText("/06:00");
                countMax = "06:00";
                break;
            case 45:
                limit.setText("/09:00");
                countMax = "09:00";
                break;
            case 60:
                limit.setText("/12:00");
                countMax = "12:00";
                break;
            case 120:
                limit.setText("/24:00");
                countMax = "24:00";
                break;


        }

    }
}