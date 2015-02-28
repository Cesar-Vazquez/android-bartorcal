package com.cesarvazquez.bartorcal;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Message;

import com.androidplot.pie.PieChart;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;
import com.androidplot.xy.XYSeries;
import com.cesarvazquez.bartorcal.helpers.BaseActivity;
import com.cesarvazquez.bartorcal.tools.DataManager;

import java.util.ArrayList;
import java.util.HashMap;

public class Goals extends BaseActivity {

    private PieChart pie;
    private DataManager dataManager;
    private float textSize;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goals);

        dataManager = DataManager.instance;
        textSize = getResources().getInteger(R.integer.goals_x_font_size);

        pie = (PieChart) findViewById(R.id.goals_chart);
        pie.getBackgroundPaint().setColor(getResources().getColor(R.color.Background));
        updateData();
    }

    private void updateData() {
        for (Segment serie: pie.getSeriesSet())
            pie.removeSeries(serie);

        ArrayList<HashMap<String, String>> players = dataManager.get(DataManager.players);

        Resources resources = getResources();
        int[][] colors = new int[][]{
            {resources.getColor(R.color.Pie1), resources.getColor(R.color.PieText1)},
            {resources.getColor(R.color.Pie2), resources.getColor(R.color.PieText2)},
        };
        int i = 0;
        for (HashMap<String, String> player : players) {
            String name = player.get(DataManager.players_nombre_corto);
            int goals = Integer.parseInt(player.get(DataManager.players_goles));
            Segment segment = new Segment(name, goals);

            int[] color = colors[i++ % colors.length];
            int borderColor = Color.DKGRAY;

            SegmentFormatter formatter = new SegmentFormatter(color[0], borderColor, borderColor, borderColor);
            Paint labelPaint = formatter.getLabelPaint();
            labelPaint.setColor(color[1]);
            labelPaint.setTextSize(textSize);
            labelPaint.setFakeBoldText(true);
            labelPaint.setTextAlign(Paint.Align.CENTER);
            pie.addSeries(segment, formatter);
        }
        pie.redraw();
    }

    @Override
    protected void updateDataHandler(Message msg) {
        updateData();
    }
}