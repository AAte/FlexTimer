package com.example.atanas.flextimer;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class ChessActivity extends AppCompatActivity {
    private long mStartTimeInMillis1;
    private TextView mTextViewCountDown1;
    private Button mButtonStartPause1;
    private Button mButtonReset1;

    private int hours1;
    private int minutes1;
    private int seconds1;

    private CountDownTimer mCountDownTimer1;

    private boolean mTimerRunning1;

    private long mTimeLeftInMillis1;
    private long mEndTime1;

    private long mStartTimeInMillis;
    private TextView mTextViewCountDown;
    private Button mButtonStartPause;
    private Button mButtonReset;

    private int hours;
    private int minutes;
    private int seconds;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mTimeLeftInMillis;
    private long mEndTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpPlayer1();
        setUpPlayer();
    }

    private void setUpPlayer1(){

        setContentView(R.layout.activity_chess);
        mStartTimeInMillis1 = 60000;
        mTimeLeftInMillis1 = mStartTimeInMillis1;
        hours1 = (int) (mTimeLeftInMillis1 / 1000) / 3600;
        minutes1 = (int) ((mTimeLeftInMillis1 / 1000) / 60) % 60;
        seconds1 = (int) (mTimeLeftInMillis1 / 1000) % 60;




        mTextViewCountDown1 = findViewById(R.id.text_view_countdown1);

        mButtonStartPause1 = findViewById(R.id.button_start_pause1);
        mButtonReset1 = findViewById(R.id.button_reset);

        mButtonStartPause1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning1) {
                    pauseTimer1();
                } else {
                    startTimer1();
                }
            }
        });

        mButtonReset1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer1();
               //resetTimer1();
            }
        });

        updateCountDownText1();

    }
    private void setUpPlayer(){

        mStartTimeInMillis = 60000;
        mTimeLeftInMillis = mStartTimeInMillis;
        hours   = (int) (mTimeLeftInMillis / 1000) / 3600;
        minutes = (int) ((mTimeLeftInMillis / 1000) / 60) % 60;
        seconds = (int) (mTimeLeftInMillis / 1000) % 60;




        mTextViewCountDown = findViewById(R.id.text_view_countdown);

        mButtonStartPause = findViewById(R.id.button_start_pause);
        mButtonReset = findViewById(R.id.button_reset);

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
                resetTimer1();
            }
        });

        updateCountDownText();

    }
    private void startTimer() {

        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                updateWatchInterface();
            }
        }.start();

        mTimerRunning = true;
        updateWatchInterface();

    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        updateWatchInterface();
    }


    private void resetTimer() {
        mTimeLeftInMillis = mStartTimeInMillis;
        updateCountDownText();
        updateWatchInterface();
    }

    private void changeStartTime() {
        mStartTimeInMillis=(hours*1000*3600)+(minutes*1000*60)+(seconds*1000);
        mTimeLeftInMillis = mStartTimeInMillis;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d",hours, minutes, seconds);
        mTextViewCountDown.setText(timeLeftFormatted);
        updateWatchInterface();
    }

    private void updateCountDownText() {
        hours   = (int) (mTimeLeftInMillis / 1000) / 3600;
        minutes = (int) ((mTimeLeftInMillis / 1000) / 60) % 60;
        seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d",hours, minutes, seconds);
        mTextViewCountDown.setText(timeLeftFormatted);
    }

    private void updateWatchInterface() {
        if (mTimerRunning) {
            mButtonReset.setVisibility(View.INVISIBLE);
            mButtonStartPause.setText("Pause");

        } else {
            mButtonStartPause.setText("Start");

            if (mTimeLeftInMillis < 10) {
                mButtonStartPause.setVisibility(View.INVISIBLE);
            } else {
                mButtonStartPause.setVisibility(View.VISIBLE);
            }

            if (mTimeLeftInMillis < mStartTimeInMillis) {
                mButtonReset.setVisibility(View.VISIBLE);
            } else {
                mButtonReset.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.about_us:
                // openTimerActivity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startTimer1() {

        mEndTime1 = System.currentTimeMillis() + mTimeLeftInMillis1;
        mCountDownTimer1 = new CountDownTimer(mTimeLeftInMillis1, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis1 = millisUntilFinished;
                updateCountDownText1();
            }

            @Override
            public void onFinish() {
                mTimerRunning1 = false;
                updateWatchInterface1();
            }
        }.start();

        mTimerRunning1 = true;
        updateWatchInterface1();

    }

    private void pauseTimer1() {
        mCountDownTimer1.cancel();
        mTimerRunning1 = false;
        updateWatchInterface1();
    }


    private void resetTimer1() {
        mTimeLeftInMillis1 = mStartTimeInMillis1;
        updateCountDownText1();
        updateWatchInterface1();
    }

    private void changeStartTime1() {
        mStartTimeInMillis1 =(hours1 *1000*3600)+(minutes1 *1000*60)+(seconds1 *1000);
        mTimeLeftInMillis1 = mStartTimeInMillis1;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours1, minutes1, seconds1);
        mTextViewCountDown1.setText(timeLeftFormatted);
        updateWatchInterface1();
    }

    private void updateCountDownText1() {
        hours1 = (int) (mTimeLeftInMillis1 / 1000) / 3600;
        minutes1 = (int) ((mTimeLeftInMillis1 / 1000) / 60) % 60;
        seconds1 = (int) (mTimeLeftInMillis1 / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours1, minutes1, seconds1);
        mTextViewCountDown1.setText(timeLeftFormatted);
    }

    private void updateWatchInterface1() {
        if (mTimerRunning1) {
            mButtonReset1.setVisibility(View.INVISIBLE);
            mButtonStartPause1.setText("Pause");

        } else {
            mButtonStartPause1.setText("Start");

            if (mTimeLeftInMillis1 < 10) {
                mButtonStartPause1.setVisibility(View.INVISIBLE);
            } else {
                mButtonStartPause1.setVisibility(View.VISIBLE);
            }

            if (mTimeLeftInMillis1 < mStartTimeInMillis1) {
                mButtonReset1.setVisibility(View.VISIBLE);
            } else {
                mButtonReset1.setVisibility(View.INVISIBLE);
            }
        }
    }

}
