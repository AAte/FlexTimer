package com.example.atanas.flextimer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Handler;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;


public class StopwatchActivity extends AppCompatActivity {

    private TextView tvStopwatch ;

    private Button btnStopwatchStart, btnStopwatchPause, btnStopwatchReset, btnStopwatchLap ;

    private long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;

    private Handler handler;

    private int Seconds, Minutes, MilliSeconds ;

    private ListView listView ;

    private String[] ListElements = new String[] {  };

    private List<String> ListElementsArrayList ;

    private ArrayAdapter<String> adapter ;

    private boolean stopwatchRunning;
    private boolean stopwatchPaused;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);

        tvStopwatch = (TextView)findViewById(R.id.tvStopwatch);
        btnStopwatchStart = (Button)findViewById(R.id.button);
        btnStopwatchPause = (Button)findViewById(R.id.button2);
        btnStopwatchReset = (Button)findViewById(R.id.button3);
        btnStopwatchLap = (Button)findViewById(R.id.button4) ;
        listView = (ListView)findViewById(R.id.listview1);

        handler = new Handler() ;

        ListElementsArrayList = new ArrayList<String>(Arrays.asList(ListElements));

        adapter = new ArrayAdapter<String>(StopwatchActivity.this,
                android.R.layout.simple_list_item_1,
                ListElementsArrayList
        );

        listView.setAdapter(adapter);
        stopwatchRunning=false;
        stopwatchPaused=false;
        btnStopwatchStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartTime = SystemClock.uptimeMillis();

                stopwatchRunning=true;
                stopwatchPaused=false;

                handler.postDelayed(runnable, 0);


                btnStopwatchReset.setVisibility(View.INVISIBLE);
                btnStopwatchStart.setVisibility(View.INVISIBLE);
                btnStopwatchPause.setVisibility(View.VISIBLE);
                btnStopwatchLap.setVisibility(View.VISIBLE);
            }
        });

        btnStopwatchPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimeBuff += MillisecondTime;

                handler.removeCallbacks(runnable);
                stopwatchPaused=true;
                stopwatchRunning=false;
                btnStopwatchReset.setVisibility(View.VISIBLE);
                btnStopwatchStart.setVisibility(View.VISIBLE);
                btnStopwatchPause.setVisibility(View.INVISIBLE);
                btnStopwatchLap.setVisibility(View.INVISIBLE);
            }
        });

        btnStopwatchReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MillisecondTime = 0L ;
                StartTime = 0L ;
                TimeBuff = 0L ;
                UpdateTime = 0L ;
                Seconds = 0 ;
                Minutes = 0 ;
                MilliSeconds = 0 ;

                tvStopwatch.setText("00:00:000");

                ListElementsArrayList.clear();

                adapter.notifyDataSetChanged();
                stopwatchRunning=false;
                stopwatchPaused=false;
                btnStopwatchReset.setVisibility(View.INVISIBLE);
                btnStopwatchStart.setVisibility(View.VISIBLE);
                btnStopwatchPause.setVisibility(View.INVISIBLE);
            }
        });

        btnStopwatchLap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int lapNum=ListElementsArrayList.size()+1;
                ListElementsArrayList.add(lapNum+": "+tvStopwatch.getText().toString());

                adapter.notifyDataSetChanged();
                listView.smoothScrollToPosition(lapNum-1);

            }
        });

    }

    public Runnable runnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;

            Seconds = (int) (UpdateTime / 1000);

            Minutes = Seconds / 60;

            Seconds = Seconds % 60;

            MilliSeconds = (int) (UpdateTime % 1000);

            tvStopwatch.setText(String.format("%02d:%02d:%03d", Minutes,Seconds,MilliSeconds));
            handler.postDelayed(this, 0);
        }

    };

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        TimeBuff += MillisecondTime;
        handler.removeCallbacks(runnable);
        editor.putLong("startTime", StartTime);
        editor.putLong("timeBuff", TimeBuff);
        editor.putLong("millisecondTime", MillisecondTime);
        editor.putLong("updateTime", UpdateTime);
        editor.putInt("milliseconds",MilliSeconds);
        editor.putBoolean("isRunning", stopwatchRunning);
        editor.putBoolean(("isPaused"),stopwatchPaused);
        editor.apply();
        editor.commit();

    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        stopwatchRunning=prefs.getBoolean("isRunning",false);
        stopwatchPaused= prefs.getBoolean("isPaused",false);
        StartTime = prefs.getLong("startTime",0);
        TimeBuff=prefs.getLong("timeBuff",0);
        MillisecondTime=prefs.getLong("millisecondTime",0);
        UpdateTime=prefs.getLong("updateTime",0);
        MilliSeconds=prefs.getInt("milliseconds",0);


        if(stopwatchRunning){
            btnStopwatchStart.performClick();
        }else if(stopwatchPaused){
            btnStopwatchPause.performClick();
        }

    }


}
