package com.example.atanas.flextimer;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

public class graphActivity extends AppCompatActivity {
    private static LineChart lineChart ;
   static int dataPoints = 0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        lineChart = (LineChart) findViewById(R.id.lineChart);
        ArrayList<String> xAxis = new ArrayList<>();
        ArrayList<Entry> yAxes = new ArrayList<>();

        String lineFromFile ;


            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput("solveTimes.txt")));


                while ((lineFromFile = reader.readLine()) != null) {

                    String[] s = lineFromFile.split(" ");
                    yAxes.add(new Entry(dataPoints,Integer.parseInt(s[0])));


                    dataPoints ++ ;
                }

            } catch (IOException e){


            }
        if(dataPoints!=0) {
            LineDataSet s = new LineDataSet(yAxes, "solves");
            LineData data = new LineData(s);
            lineChart.setData(data);
        }
    }
}
