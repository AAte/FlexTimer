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
    private ImageButton btnFocus;
    private ImageButton btnCube;
    private ImageButton btnChess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnTimer= findViewById(R.id.btnTimer);
        btnStopWatch=findViewById(R.id.btnStopwatch);
        btnInterval=findViewById(R.id.btnIntervalTimer);
        btnFocus=findViewById(R.id.btnFocusTimer);
        btnCube=findViewById(R.id.btnRubikTimer);
        btnChess=findViewById(R.id.btnChessTimer);

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

        btnFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFocusActivity();
            }
        });
        btnCube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCubeActivity();
            }
        });
        btnChess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChessActivity();
            }
        });
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
    public void openFocusActivity(){
        Intent i=new Intent(this, FocusActivity.class);
        startActivity(i);
    }

    public void openCubeActivity(){
        Intent i=new Intent(this, CubeActivity.class);
        startActivity(i);
    }

    public void openChessActivity(){
        Intent i=new Intent(this, ChessActivity.class);
        startActivity(i);
    }

}
