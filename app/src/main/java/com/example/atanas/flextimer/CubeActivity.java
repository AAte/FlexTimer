package com.example.atanas.flextimer;

import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.concurrent.TimeUnit;

public class CubeActivity extends AppCompatActivity {


    private SQLiteDatabase database;
    private static final int MSG_START_INSPECTION =4 ;
    final int MSG_START_TIMER = 0;
    final int MSG_STOP_TIMER = 1;
    final int MSG_UPDATE_TIMER = 2;
    final int MSG_UPDATE_INSPECTION = 3;
    Stopwatch timer;
    final int REFRESH_RATE = 100;
    boolean timerRun;
    boolean inspection;
    TextView tvTimer ;
    TextView tvScramble ;
    String currentScramble ;
    LinearLayout times ;
    FileOutputStream fileOut ;
    OutputStreamWriter output;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cube);
        ActionBar ab = getSupportActionBar() ;
        ab.setIcon(R.drawable.rubik);
        ab.setTitle(R.string.cube_timer);
        ab.setDisplayShowTitleEnabled(true);
        ab.setDisplayUseLogoEnabled(true);
        timer       = new Stopwatch();
        timerRun    = false ;
        inspection  = true ;
        tvTimer = (TextView) findViewById(R.id.tvTime);
       tvScramble = (TextView) findViewById(R.id.tvScramble);
       currentScramble = Scramble.generateScramble();
       tvScramble.setText(currentScramble);

       times = (LinearLayout) findViewById(R.id.scrollView);
        try {
            fileOut = openFileOutput("solveTimes.txt" , MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        output = new  OutputStreamWriter(fileOut);

    }


    private String getFormatMSM(long currentTime){

        String time =  String.format("%02d:%02d:%03d",
                TimeUnit.MILLISECONDS.toMinutes(currentTime),
                TimeUnit.MILLISECONDS.toSeconds(currentTime)-TimeUnit.MILLISECONDS.toMinutes(currentTime)*60,
                currentTime-TimeUnit.MILLISECONDS.toSeconds(currentTime)*1000
        );

        return time ;
    }
    Handler mHandler = new  Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String time = getFormatMSM(timer.getElapsedTime());
            switch (msg.what) {
                case MSG_START_TIMER:
                    timer.start(); //start timer
                    mHandler.removeMessages(MSG_UPDATE_INSPECTION);
                    mHandler.sendEmptyMessage(MSG_UPDATE_TIMER);

                    break;
                case MSG_START_INSPECTION:
                    timer.start();
                    mHandler.sendEmptyMessage(MSG_UPDATE_INSPECTION);
                    break ;
                case MSG_UPDATE_TIMER:

                    tvTimer.setText(time);
                    mHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIMER,REFRESH_RATE); //text view is updated every second,
                    break;
                case MSG_UPDATE_INSPECTION:

                    tvTimer.setText(String.format ("%d" ,  15-timer.getElapsedTimeSecs()));

                    mHandler.sendEmptyMessageDelayed(MSG_UPDATE_INSPECTION,REFRESH_RATE); //text view is updated every second,
                    if(15-timer.getElapsedTimeSecs() <=0 ) {
                        timer.stop();mHandler.removeMessages(MSG_UPDATE_INSPECTION);

                    }
                    break; //though the timer is still running
                case MSG_STOP_TIMER:
                    mHandler.removeMessages(MSG_UPDATE_TIMER); // no more updates.
                    timer.stop();//stop timer
                    onStopTimer(time);
                    break;

                default:
                    break;
            }
        }
    };

    public void onStopTimer(String time){

        TextView tv = new TextView(getApplicationContext()) ;
        tv.setText(time);
        tv.setTextSize(30);
        times.addView(tv , 0);
        tvTimer.setText(time);
        currentScramble = Scramble.generateScramble();
        tvScramble.setText(currentScramble);
        try {
            FileOutputStream times = openFileOutput("solveTimes.txt" , MODE_PRIVATE);
            OutputStreamWriter output = new  OutputStreamWriter(times);
            output.write((int) timer.getElapsedTime());
            output.write(" "+time + " " + currentScramble + "\n");
            output.flush();
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }







    public void toggleTimer(View v) {
        if(inspection){
            mHandler.sendEmptyMessage(MSG_START_INSPECTION);
            inspection = false ;
        }else {

            if (!timerRun) {
                mHandler.sendEmptyMessage(MSG_START_TIMER);
                timerRun = true;
            } else {
                mHandler.sendEmptyMessage(MSG_STOP_TIMER);
                timerRun = false;
                inspection = true;
            }


        }

    }



    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cubeactivity , menu);
        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true ;
    }


}




