package com.example.atanas.flextimer;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class CubeActivity extends AppCompatActivity {


    final int MSG_START_TIMER = 0;
    final int MSG_STOP_TIMER = 1;
    final int MSG_UPDATE_TIMER = 2;
    Stopwatch timer;
    final int REFRESH_RATE = 100;
    boolean timerRun;
    boolean inspection;
    TextView tvTimer ;
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
        inspection  = false ;
        tvTimer = (TextView) findViewById(R.id.tvTime);

    }



    Handler mHandler = new  Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String time =  String.format("%02d:%02d:%03d",
                    TimeUnit.MILLISECONDS.toMinutes(timer.getElapsedTime()),
                    TimeUnit.MILLISECONDS.toSeconds(timer.getElapsedTime())-TimeUnit.MILLISECONDS.toMinutes(timer.getElapsedTime())*60,
                    timer.getElapsedTime()-TimeUnit.MILLISECONDS.toSeconds(timer.getElapsedTime())*1000
            );
            switch (msg.what) {
                case MSG_START_TIMER:
                    timer.start(); //start timer
                    mHandler.sendEmptyMessage(MSG_UPDATE_TIMER);
                    break;

                case MSG_UPDATE_TIMER:

                    tvTimer.setText(time);
                    mHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIMER,REFRESH_RATE); //text view is updated every second,
                    break;                                  //though the timer is still running
                case MSG_STOP_TIMER:
                    mHandler.removeMessages(MSG_UPDATE_TIMER); // no more updates.
                    timer.stop();//stop timer

                    tvTimer.setText(time);
                    break;

                default:
                    break;
            }
        }
    };


    public void toggleTimer(View v) {
        if(!timerRun) {
            mHandler.sendEmptyMessage(MSG_START_TIMER);
            timerRun = true ;
        }
        else {
            mHandler.sendEmptyMessage(MSG_STOP_TIMER);
            timerRun = false ;
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





