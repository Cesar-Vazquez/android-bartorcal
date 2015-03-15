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
import com.github.mikephil.charting.utils.PercentFormatter;
import com.github.mikephil.charting.utils.ValueFormatter;


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
        // mChart.setHoleColor(Color.rgb(235, 235, 235));
        mChart.setHoleColorTransparent(true);
        mChart.setHoleRadius(60f);
        mChart.setDescription("");
        mChart.setDrawCenterText(true);
        mChart.setDrawHoleEnabled(true);
        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        setData();

        mChart.animateXY(1500, 1500);
        // mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
        l.setPosition(LegendPosition.BELOW_CHART_LEFT);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
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
        dataSet.setSliceSpace(3f);

        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        dataSet.setColors(colors);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return ((int)(totalGoles * value / 100)) + "";
            }
        });
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }
}