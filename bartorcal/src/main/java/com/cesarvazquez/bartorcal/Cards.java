package com.cesarvazquez.bartorcal;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Message;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.androidplot.LineRegion;
import com.androidplot.ui.AnchorPosition;
import com.androidplot.ui.SeriesRenderer;
import com.androidplot.ui.SizeLayoutType;
import com.androidplot.ui.SizeMetrics;
import com.androidplot.ui.TextOrientationType;
import com.androidplot.ui.XLayoutStyle;
import com.androidplot.ui.YLayoutStyle;
import com.androidplot.ui.widget.TextLabelWidget;
import com.androidplot.util.PixelUtils;
import com.androidplot.xy.BarFormatter;
import com.androidplot.xy.BarRenderer;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.cesarvazquez.bartorcal.helpers.BaseActivity;
import com.cesarvazquez.bartorcal.tools.DataManager;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Cards extends BaseActivity {

    private static final String NO_SELECTION_TXT = "Pulsa para ver";
    private static final String[] seriesNames = {DataManager.players_amarillas, DataManager.players_rojas};
    private static final int[] seriesColors = {Color.YELLOW, Color.RED};
    private static final int[] seriesCheckBoxIds = {R.id.s1CheckBox, R.id.s2CheckBox};

    private ArrayList<HashMap<String, String>> players;

    private HashMap<String, List> series;
    private CheckBox[] seriesCheckBox;
    private XYSeries[] seriesXY;
    private CardsBarFormatter[] formatter;

    private XYPlot plot;
    private CardsBarFormatter selectionFormatter;
    private TextLabelWidget selectionWidget;
    private Pair<Integer, XYSeries> selection;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cards);

        series = new HashMap<String, List>();
        selectionFormatter = new CardsBarFormatter(Color.YELLOW, Color.WHITE);
        seriesCheckBox = new CheckBox[seriesNames.length];
        seriesXY = new XYSeries[seriesNames.length];
        formatter = new CardsBarFormatter[seriesNames.length];

        for (int i=0; i<seriesNames.length; i++){
            series.put(seriesNames[i], new ArrayList());
            formatter[i] = new CardsBarFormatter(seriesColors[i], Color.LTGRAY);
            seriesCheckBox[i] = (CheckBox) findViewById(seriesCheckBoxIds[i]);
            seriesCheckBox[i].setText(seriesNames[i]);
            seriesCheckBox[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    int index = -1;
                    for (int i=0; i<seriesNames.length && index<0; i++)
                        if (compoundButton.equals(seriesCheckBox[i]))
                            index = i;
                    if (checked)
                        plot.addSeries(seriesXY[index], formatter[index]);
                    else
                        plot.removeSeries(seriesXY[index]);
                    plot.redraw();
                }
            });
        }


        plot = (XYPlot) findViewById(R.id.mySimpleXYPlot);
        plot.getLegendWidget().setVisible(false);
        plot.setTicksPerRangeLabel(3);
        plot.setRangeLowerBoundary(0, BoundaryMode.FIXED);
        plot.getGraphWidget().setGridPadding(30, 10, 30, 0);
        plot.setTicksPerDomainLabel(2);
        plot.getBackgroundPaint().setColor(getResources().getColor(R.color.Background));
        plot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    onPlotClicked(new PointF(motionEvent.getX(), motionEvent.getY()));
                }
                return true;
            }
        });

        selectionWidget = new TextLabelWidget(plot.getLayoutManager(), NO_SELECTION_TXT,
                new SizeMetrics(
                        PixelUtils.dpToPix(100), SizeLayoutType.ABSOLUTE,
                        PixelUtils.dpToPix(100), SizeLayoutType.ABSOLUTE),
                TextOrientationType.HORIZONTAL);
        selectionWidget.getLabelPaint().setTextSize(PixelUtils.dpToPix(16));
        selectionWidget.position(0, XLayoutStyle.RELATIVE_TO_CENTER, PixelUtils.dpToPix(45), YLayoutStyle.ABSOLUTE_FROM_TOP, AnchorPosition.TOP_MIDDLE);
        selectionWidget.pack();

        updatePlot();
    }

    private void updatePlot() {
        players = DataManager.instance.get(DataManager.players);

        String serie_amarillas = seriesNames[0];
        String serie_rojas = seriesNames[1];

        List<Integer> amarillas = series.get(serie_amarillas);
        List<Integer> rojas = series.get(serie_rojas);
        amarillas.clear();
        rojas.clear();

        for (HashMap<String, String> player: players){
            int a = 0, r = 0;
            try{
                a = Integer.parseInt(player.get(DataManager.players_amarillas));
                r = Integer.parseInt(player.get(DataManager.players_rojas));
            }
            catch(Exception e){}
            amarillas.add(a);
            rojas.add(r);
        }

        series.put(serie_amarillas, amarillas);
        series.put(serie_rojas, rojas);

        for (XYSeries serie: plot.getSeriesSet())
            plot.removeSeries(serie);

        for (int i=0; i<seriesNames.length; i++){
            seriesXY[i] = new SimpleXYSeries(series.get(seriesNames[i]), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, seriesNames[i]);
            if (seriesCheckBox[i].isChecked())
                plot.addSeries(seriesXY[i], formatter[i]);
        }

        CardsBarRenderer renderer = ((CardsBarRenderer)plot.getRenderer(CardsBarRenderer.class));
        renderer.setBarRenderStyle(BarRenderer.BarRenderStyle.SIDE_BY_SIDE);
        renderer.setBarWidthStyle(BarRenderer.BarWidthStyle.VARIABLE_WIDTH);
        renderer.setBarWidth(50);
        renderer.setBarGap(10);

        plot.setRangeTopMin(0);
        plot.setDomainValueFormat(new XLabelFormat(players));
        plot.redraw();
    }

    private void onPlotClicked(PointF point) {
        selection = null;
        if (plot.getGraphWidget().getGridRect().contains(point.x, point.y)) {
            Number x = plot.getXVal(point);
            Number y = plot.getYVal(point);

            double xDistance = 0;
            double yDistance = 0;

            for (XYSeries series : plot.getSeriesSet()) {
                for (int i = 0; i < series.size(); i++) {
                    Number thisX = series.getX(i);
                    Number thisY = series.getY(i);
                    if (thisX != null && thisY != null) {
                        double thisXDistance = LineRegion.measure(x, thisX).doubleValue();
                        double thisYDistance = LineRegion.measure(y, thisY).doubleValue();
                        if (selection == null) {
                            selection = new Pair<Integer, XYSeries>(i, series);
                            xDistance = thisXDistance;
                            yDistance = thisYDistance;
                        } else if (thisXDistance < xDistance) {
                            selection = new Pair<Integer, XYSeries>(i, series);
                            xDistance = thisXDistance;
                            yDistance = thisYDistance;
                        } else if (thisXDistance == xDistance && thisYDistance < yDistance && thisY.doubleValue() >= y.doubleValue()) {
                            selection = new Pair<Integer, XYSeries>(i, series);
                            xDistance = thisXDistance;
                            yDistance = thisYDistance;
                        }
                    }
                }
            }
        }

        if(selection == null)
            selectionWidget.setText(NO_SELECTION_TXT);
        else{
            selectionWidget.setText(selection.second.getX(selection.first) + ": " + selection.second.getY(selection.first) + " " + selection.second.getTitle());
        }
        plot.redraw();
    }


    private class XLabelFormat extends Format {

        private String[] domain;

        public XLabelFormat(ArrayList<HashMap<String, String>> players){
            this.domain = new String[players.size()];
            for (int i=0; i<players.size(); i++)
                this.domain[i] = players.get(i).get(DataManager.players_nombre_corto);
        }

        @Override
        public StringBuffer format(Object object, StringBuffer buffer, FieldPosition field) {
            int parsedInt = Math.round(Float.parseFloat(object.toString()));
            String label = this.domain[parsedInt];
            if (label.length()>5)
                label = label.substring(0, 5) + "..";
            buffer.append(label);
            return buffer;
        }

        @Override
        public Object parseObject(String string, ParsePosition position) {
            return java.util.Arrays.asList(this.domain).indexOf(string);
        }
    }

    class CardsBarFormatter extends BarFormatter {
        public CardsBarFormatter(int fillColor, int borderColor) {
            super(fillColor, borderColor);
        }

        @Override
        public Class<? extends SeriesRenderer> getRendererClass() {
            return CardsBarRenderer.class;
        }

        @Override
        public SeriesRenderer getRendererInstance(XYPlot plot) {
            return new CardsBarRenderer(plot);
        }
    }

    class CardsBarRenderer extends BarRenderer<CardsBarFormatter> {
        public CardsBarRenderer(XYPlot plot) {
            super(plot);
        }

        public CardsBarFormatter getFormatter(int index, XYSeries series) {
            if (selection != null && selection.second == series && selection.first == index)
                return selectionFormatter;
            else
                return getFormatter(series);
        }
    }

    @Override
    protected void updateDataHandler(Message msg) {
        updatePlot();
    }
}