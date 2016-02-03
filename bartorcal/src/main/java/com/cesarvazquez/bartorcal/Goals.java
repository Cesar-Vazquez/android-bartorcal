package com.cesarvazquez.bartorcal;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;
import android.os.Bundle;

import com.cesarvazquez.bartorcal.helpers.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;

import com.cesarvazquez.bartorcal.tools.DataManager;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;


public class Goals extends BaseActivity {

    private PieChart mChart;
    private DataManager dataManager;
    private int totalGoles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataManager = DataManager.instance;

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.goals);

        mChart = (PieChart) findViewById(R.id.chart1);
        mChart.setUsePercentValues(true);
        //mChart.setHoleColorTransparent(true);
        mChart.setHoleRadius(0f);
        mChart.setTransparentCircleRadius(0f);
        mChart.setDescription("");
        mChart.setDrawCenterText(true);
        //mChart.setDrawHoleEnabled(true);
        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);

        setData();

        mChart.getLegend().setEnabled(false);
        mChart.animateXY(1500, 1500);
    }

    private void setData() {

        ArrayList<HashMap<String, String>> players = dataManager.get(DataManager.players);

        ArrayList<Entry> yVals = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();

        int i=0;
        totalGoles = 0;
        for (HashMap<String, String> player: players) {
            float goles = Float.parseFloat(player.get(DataManager.players_goles));
            totalGoles += goles;
            if (goles > 0){
                String titulo = player.get(DataManager.players_nombre_corto);
                yVals.add(new Entry(goles, i++));
                xVals.add(titulo);
            }
        }

        PieDataSet dataSet = new PieDataSet(yVals, "");
        dataSet.setSliceSpace(0f);

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.rgb(238,93,10));
        colors.add(Color.rgb(255,235,4));
        colors.add(Color.rgb(183,204,1));
        colors.add(Color.rgb(30,164,41));
        colors.add(Color.rgb(0,164,155));
        colors.add(Color.rgb(4,175,227));
        colors.add(Color.rgb(3,107,180));
        colors.add(Color.rgb(73,50,138));
        colors.add(Color.rgb(126,43,135));
        colors.add(Color.rgb(181,8,98));
        colors.add(Color.rgb(224,0,112));
        colors.add(Color.rgb(221,1,27));

        dataSet.setColors(colors);

        PieData data = new PieData(xVals, dataSet);

        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return ((int)(totalGoles * value / 100)) + "";
            }
        });
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.rgb(43,43,43));
        mChart.setData(data);

        mChart.invalidate();
    }
}