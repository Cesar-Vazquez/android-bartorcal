package com.cesarvazquez.bartorcal;

import java.text.DecimalFormat;
import com.cesarvazquez.bartorcal.helpers.BaseActivity;
import com.cesarvazquez.bartorcal.tools.DataManager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Cards extends BaseActivity {

    private static final String[] seriesNames = {DataManager.players_amarillas, DataManager.players_rojas};
    private static final String minValue = "minValue";
    private static final String maxValue = "maxValue";

    private HashMap<String, List> series;
    private ArrayList<HashMap<String, String>> players;
    private BarChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cards);

        mChart = (BarChart) findViewById(R.id.chart1);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.setDescription("");
        mChart.setMaxVisibleValueCount(60);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);
        mChart.setTouchEnabled(false);
        mChart.getLegend().setEnabled(false);

        HashMap<String, Integer> dataParameters = setData();

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(0);
        xAxis.setLabelRotationAngle(45f);
        xAxis.setTextSize(5);

        YAxisValueFormatter custom = new CardsYAxisValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setValueFormatter(custom);
        leftAxis.setAxisMinValue(dataParameters.get(minValue));
        leftAxis.setAxisMaxValue(dataParameters.get(maxValue));
        leftAxis.setLabelCount(dataParameters.get(maxValue) + 1, true);
        leftAxis.setPosition(YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setTextSize(5);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);


    }

    private HashMap<String, Integer> setData() {
        HashMap<String, Integer> dataParameters = new HashMap<String, Integer>();
        dataParameters.put(minValue, 0);
        dataParameters.put(maxValue, 0);

        players = DataManager.instance.get(DataManager.players);

        String amarillas_title = seriesNames[0];
        String rojas_title = seriesNames[1];

        ArrayList<String> xVals = new ArrayList<String>();
        for (HashMap<String, String> player: players) {
            xVals.add(player.get(DataManager.players_nombre_corto));
        }

        ArrayList<BarEntry> amarillas = new ArrayList<BarEntry>();
        ArrayList<BarEntry> rojas= new ArrayList<BarEntry>();
        int i=0;
        for (HashMap<String, String> player: players){
            int a = 0, r = 0;
            try{
                a = Integer.parseInt(player.get(DataManager.players_amarillas));
                r = Integer.parseInt(player.get(DataManager.players_rojas));

                int posibleMaxValue = Math.max(a,r);
                int currentMaxValue = dataParameters.get(maxValue);
                if (posibleMaxValue>currentMaxValue)
                    dataParameters.put(maxValue, posibleMaxValue);
            }
            catch(Exception e){}
            amarillas.add(new BarEntry(a, i));
            rojas.add(new BarEntry(r, i));
            i++;
        }

        BarDataSet amarillas_set = new BarDataSet(amarillas, amarillas_title);
        BarDataSet rojas_set = new BarDataSet(rojas, rojas_title);
        amarillas_set.setBarSpacePercent(0f);
        rojas_set.setBarSpacePercent(0f);
        amarillas_set.setColor(Color.YELLOW);
        rojas_set.setColor(Color.RED);


        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(amarillas_set);
        dataSets.add(rojas_set);

        BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(10f);
        data.setDrawValues(false);

        mChart.setData(data);

        return dataParameters;
    }

    public class CardsYAxisValueFormatter implements YAxisValueFormatter {

        private DecimalFormat mFormat;

        public CardsYAxisValueFormatter() {
            mFormat = new DecimalFormat("0");
        }

        @Override
        public String getFormattedValue(float value, YAxis yAxis) {
            return mFormat.format(value);
        }
    }


}