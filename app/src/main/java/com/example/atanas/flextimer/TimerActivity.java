package com.example.atanas.flextimer;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.Locale;
import android.content.SharedPreferences;
import android.app.Activity;
public class TimerActivity extends AppCompatActivity {


    private long mStartTimeInMillis;

    private TextView mTextViewCountDown;
    private Button mButtonStartPause;
    private Button mButtonReset;

    private ImageButton btnHoursUp;
    private ImageButton btnHoursDown;
    private ImageButton btnMinutesUp;
    private ImageButton btnMinutesDown;
    private ImageButton btnSecondsUp;
    private ImageButton btnSecondsDown;

    private int hours;
    private int minutes;
    private int seconds;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mTimeLeftInMillis;
    private long mEndTime;
    private ProgressBar countdownProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        mStartTimeInMillis = 60000;
        mTimeLeftInMillis = mStartTimeInMillis;
        hours   = (int) (mTimeLeftInMillis / 1000) / 3600;
        minutes = (int) ((mTimeLeftInMillis / 1000) / 60) % 60;
        seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        btnHoursUp      =   findViewById(R.id.btnHoursUp);
        btnHoursDown    =   findViewById(R.id.btnHoursDown);
        btnMinutesUp    =   findViewById(R.id.btnMinutesUp);
        btnMinutesDown  =   findViewById(R.id.btnMinutesDown);
        btnSecondsUp    =   findViewById(R.id.btnSecondsUp);
        btnSecondsDown  =   findViewById(R.id.btnSecondsDown);


        mTextViewCountDown = findViewById(R.id.text_view_countdown);

        mButtonStartPause = findViewById(R.id.button_start_pause);
        mButtonReset = findViewById(R.id.button_reset);
        countdownProgress=findViewById(R.id.progressBar);
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
            }
        });

        btnHoursUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hours++;
                changeStartTime();
            }
        });
        btnHoursDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hours>0){
                    hours--;
                }
                changeStartTime();
            }
        });

        btnMinutesUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minutes++;
                changeStartTime();
            }
        });

        btnMinutesDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(minutes>0){
                    minutes--;
                }
                changeStartTime();
            }
        });
        btnSecondsUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seconds++;
                changeStartTime();
            }
        });

        btnSecondsDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(seconds>0){
                    seconds--;
                }
                changeStartTime();
            }
        });
        updateCountDownText();
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
                countdownProgress.setProgress(0);
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
        setArrowsVisible();

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
        double percentage= (mTimeLeftInMillis/(double)mStartTimeInMillis)*10000;
        Log.d("Percentage","value: " + percentage);
        countdownProgress.setProgress((int)percentage);
        mTextViewCountDown.setText(timeLeftFormatted);
    }

    private void updateWatchInterface() {
        if (mTimerRunning) {
            mButtonReset.setVisibility(View.INVISIBLE);
            mButtonStartPause.setText("Pause");
            setArrowsInvisible();
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

    private void setArrowsVisible(){
        mButtonStartPause.setVisibility(View.VISIBLE);
        btnHoursUp.setVisibility(View.VISIBLE);
        btnHoursDown.setVisibility(View.VISIBLE);
        btnMinutesUp.setVisibility(View.VISIBLE);
        btnMinutesDown.setVisibility(View.VISIBLE);
        btnSecondsUp.setVisibility(View.VISIBLE);
        btnSecondsDown.setVisibility(View.VISIBLE);
    }
    private  void setArrowsInvisible(){
        mButtonReset.setVisibility(View.INVISIBLE);
        btnHoursUp.setVisibility(View.INVISIBLE);
        btnHoursDown.setVisibility(View.INVISIBLE);
        btnMinutesUp.setVisibility(View.INVISIBLE);
        btnMinutesDown.setVisibility(View.INVISIBLE);
        btnSecondsUp.setVisibility(View.INVISIBLE);
        btnSecondsDown.setVisibility(View.INVISIBLE);
    }


    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("startTimeInMillis", mStartTimeInMillis);
        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);

        editor.apply();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        mStartTimeInMillis = prefs.getLong("startTimeInMillis", 60000);
        mTimeLeftInMillis = prefs.getLong("millisLeft", mStartTimeInMillis);
        mTimerRunning = prefs.getBoolean("timerRunning", false);

        updateCountDownText();
        updateWatchInterface();

        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();

            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                updateCountDownText();
                updateWatchInterface();
            } else {
                startTimer();
            }
        }
    }

}
