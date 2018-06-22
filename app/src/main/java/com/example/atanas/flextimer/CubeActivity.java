package com.example.atanas.flextimer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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
import android.widget.Toast;


import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.StringTokenizer;
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
    TextView tvAvg12 ;
    TextView tvAvg5 ;
    TextView tvAvgAll ;
    TextView tvBestAll ;
    String currentScramble ;
    LinearLayout times ;

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
        tvAvg5 = findViewById(R.id.tvAvg5);
        tvAvg12 = findViewById(R.id.tvAvg12);
        tvAvgAll = findViewById(R.id.tvAvgAll);
        tvBestAll = findViewById(R.id.bestOfAll);
       currentScramble = Scramble.generateScramble();
       tvScramble.setText(currentScramble);

       times = (LinearLayout) findViewById(R.id.scrollView);


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

            FileOutputStream  fileOut = openFileOutput("solveTimes.txt" ,MODE_APPEND);
            OutputStreamWriter output = new  OutputStreamWriter(fileOut);

            output.write(Integer.toString((int) timer.getElapsedTime())+" "+time + " " + currentScramble + "\n");
            output.flush();
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file = getApplicationContext().getFileStreamPath("solveTimes.txt");
        String lineFromFile ;

        if(file.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput("solveTimes.txt")));
                int sum = 0;
                int min = 1000000000;
                int ctr = 1 ;
                while ((lineFromFile = reader.readLine()) != null) {


                    String [] strings = lineFromFile.split(" ");
                   // Toast.makeText(getApplicationContext(), strings[0], Toast.LENGTH_SHORT).show();
                    int c = Integer.parseInt(strings[0]);
                    sum+=c ;
                    if(c < min) {
                        min = c;
                        tvBestAll.setText(getString(R.string.bestAll)+"        " + getFormatMSM(c));
                    }
                    if (ctr == 5) {
                        int avg = sum / 5;
                        String dispSt = getString(R.string.avg5)+"      "+ getFormatMSM(avg);
                        tvAvg5.setText(dispSt);

                    }
                    if (ctr == 12) {
                        int avg = sum / 12;
                        tvAvg12.setText(getString(R.string.avg12)+"     " + getFormatMSM(avg));

                    }

                    int avg = sum / ctr;
                    tvAvgAll.setText(getString(R.string.avgAll) +"    "+ getFormatMSM(avg));




                    ctr++;
                }
            } catch (IOException e) {

            }
        }

    }







    public void toggleTimer(View v) {
        if(inspection){
            mHandler.sendEmptyMessage(MSG_START_INSPECTION);
            inspection = false ;
        }else {

            if (!timerRun) {
                mHandler.sendEmptyMessage(MSG_START_TIMER);
                timerRun = true ;
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

    public void openListActivity(){
        Intent i=new Intent(this, ListTimesActivity.class);
        startActivity(i);
    }
    public void openGraphActivity(){
        Intent i=new Intent(this, graphActivity.class);
        startActivity(i);
    }
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:

                    getApplicationContext().deleteFile("solveTimes.txt");

                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };
    public boolean onOptionsItemSelected(MenuItem item) {
       switch(item.getItemId()){

           case R.id.showGraph :
               openGraphActivity() ;
               return true ;

           case R.id.listTimes :
               openListActivity();
           return true ;
           case R.id.deleteTimes :

               AlertDialog.Builder builder = new AlertDialog.Builder(CubeActivity.this);
               builder.setMessage("Delete times?").setPositiveButton("Yes", dialogClickListener)
                       .setNegativeButton("No", dialogClickListener).show();



               return true ;
       }
       return true ;
    }


}




