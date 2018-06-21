package com.example.atanas.flextimer;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.app.Activity;
import java.util.Locale;

public class FocusActivity extends AppCompatActivity {


    private long mStartTimeInMillis;

    private TextView mTextViewCountDown;
    private Button mButtonStartPause;
    private Button mButtonReset;

    private int numberIntervals;
    private int restTimeSeconds;
    private int intervalTimeSeconds;
    private boolean restIntervalSwitcher;
    private boolean intervalPreparation;
    private TextView tvIntervalStatus;


    private int hours;
    private int minutes;
    private int seconds;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mTimeLeftInMillis;
    private long mEndTime;
    private ProgressBar countdownProgress;

    private MediaPlayer goSound;
    private MediaPlayer restSound;
    private MediaPlayer getReadySound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus);
        setUpIntervals();

        hours   = (int) (mTimeLeftInMillis / 1000) / 3600;
        minutes = (int) ((mTimeLeftInMillis / 1000) / 60) % 60;
        seconds = (int) (mTimeLeftInMillis / 1000) % 60;



        goSound=MediaPlayer.create(this,R.raw.focusbeep);
        restSound=MediaPlayer.create(this,R.raw.alarmrest);
        getReadySound=MediaPlayer.create(this,R.raw.getready);

        tvIntervalStatus = findViewById(R.id.tvIntervalStatus);


        mTextViewCountDown = findViewById(R.id.text_view_countdown);

        mButtonStartPause = findViewById(R.id.button_start_pause);
        mButtonReset = findViewById(R.id.button_reset);
        countdownProgress=findViewById(R.id.progressBar);

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    stopTimer();
                } else {
                    resetTimer();
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
        updateCountDownText();
    }

    private void startTimer() {
        tvIntervalStatus.setVisibility(View.VISIBLE);


        if(intervalPreparation){
            tvIntervalStatus.setText("Get ready");
            intervalPreparation=false;
            getReadySound.start();
        }
        else if(restIntervalSwitcher){
            tvIntervalStatus.setText("Rest");
            restSound.start();
        }else{
            tvIntervalStatus.setText("Focus");
            goSound.start();
        }
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                if(numberIntervals>0 && restIntervalSwitcher){
                    mStartTimeInMillis=intervalTimeSeconds*1000;
                    restIntervalSwitcher=false;
                    softResetTimer();
                    startTimer();

                }else if(numberIntervals>0 && !restIntervalSwitcher){
                    mStartTimeInMillis=restTimeSeconds*1000;
                    restIntervalSwitcher=true;
                    numberIntervals--;
                    softResetTimer();
                    startTimer();

                } else{
                    mTimerRunning = false;
                    updateWatchInterface();
                    countdownProgress.setProgress(0);
                    setUpIntervals();
                    tvIntervalStatus.setVisibility(View.INVISIBLE);

                }

            }
        }.start();

        mTimerRunning = true;
        updateWatchInterface();

    }

    private void stopTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        updateWatchInterface();
        mButtonStartPause.setVisibility(View.INVISIBLE);

    }
    private void resetTimer() {
        softResetTimer();
        setUpIntervals();
    }

    private void softResetTimer() {
        mTimeLeftInMillis = mStartTimeInMillis;
        updateCountDownText();
        updateWatchInterface();
        tvIntervalStatus.setVisibility(View.INVISIBLE);

    }

    private void setUpIntervals(){
        numberIntervals=1;
        restTimeSeconds=30;
        intervalTimeSeconds=90;
        restIntervalSwitcher=true;
        intervalPreparation=true;
        mStartTimeInMillis = 5000;
        mTimeLeftInMillis = mStartTimeInMillis;
    }

    private void updateCountDownText() {
        hours   = (int) (mTimeLeftInMillis / 1000) / 3600;
        minutes = (int) ((mTimeLeftInMillis / 1000) / 60) % 60;
        seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted;
        if(hours>0){
            timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d",hours, minutes, seconds);
        }else{
            timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        }

        double percentage= (mTimeLeftInMillis/(double)mStartTimeInMillis)*10000;
        Log.d("Percentage","value: " + percentage);
        countdownProgress.setProgress((int)percentage);
        mTextViewCountDown.setText(timeLeftFormatted);
    }

    private void updateWatchInterface() {
        if (mTimerRunning) {
            mButtonReset.setVisibility(View.INVISIBLE);
            mButtonStartPause.setText("Stop");

        } else {
            mButtonStartPause.setText("Start");

            if (mTimeLeftInMillis < 100) {
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



 /*   @Override
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
*/
}
