package com.example.atanas.flextimer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.example.atanas.flextimer.R.id.svScrollSolveTimes;

public class ListTimesActivity extends AppCompatActivity {


    LinearLayout times ;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_times);



        File file = getApplicationContext().getFileStreamPath("solveTimes.txt");
        String lineFromFile ;
        times = (LinearLayout) findViewById(R.id.llScrollView);
        if(file.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput("solveTimes.txt")));

                int ctr = 1 ;
                while ((lineFromFile = reader.readLine()) != null) {


                    String[] strings = lineFromFile.split(" ");
                    String outTime   = "";
                    outTime += Integer.toString(ctr) +".Time : " +strings[1] + " \n SCRAMBLE : " ;
                    for(int i = 2 ; i < strings.length ;i++) {
                        outTime+= strings[i];

                    }
                    TextView tvForTime = new TextView(getApplicationContext());
                    tvForTime.setText(outTime);
                    if(tvForTime.getParent()!=null)
                        ((ViewGroup)tvForTime.getParent()).removeView(tvForTime);
                    times.addView(tvForTime  );
                    ctr ++ ;
                }

            } catch (IOException e){


            }
        }

    }





}
