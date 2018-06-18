package com.example.atanas.flextimer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    private ImageButton btnTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnTimer= findViewById(R.id.btnTimer);
        btnTimer.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               openTimerActivity();
            }
        });
    }


    public void openTimerActivity(){
        Intent i=new Intent(this, TimerActivity.class);
        startActivity(i);
    }
}
