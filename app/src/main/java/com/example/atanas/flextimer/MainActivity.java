package com.example.atanas.flextimer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    private ImageButton btnTimer;
    private ImageButton btnStopWatch;
    private ImageButton btnInterval;
    private ImageButton btnRest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnTimer= findViewById(R.id.btnTimer);
        btnStopWatch=findViewById(R.id.btnStopwatch);
        btnInterval=findViewById(R.id.btnIntervalTimer);
       // btnRest.findViewById(R.id.btnRestTimer);

        btnStopWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStopwatchActivity();
            }
        });

        btnTimer.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               openTimerActivity();
            }
        });

        btnInterval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openIntervalActivity();
            }
        });

       /* btnRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRestActivity();
            }
        });*/
    }


    public void openTimerActivity(){
        Intent i=new Intent(this, TimerActivity.class);
        startActivity(i);
    }
    public void openStopwatchActivity(){
        Intent i=new Intent(this, StopwatchActivity.class);
        startActivity(i);
    }

    public void openIntervalActivity(){
        Intent i=new Intent(this, IntervalActivity.class);
        startActivity(i);
    }
    public void openRestActivity(){
        //Intent i=new Intent(this, RestActivity.class);
       // startActivity(i);
    }

}
