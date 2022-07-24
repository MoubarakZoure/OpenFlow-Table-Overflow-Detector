/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;

/**
 *
 * @author Moubarak
 */
public class SwitchStateVizualisation {

    private String switchName;
    private Map<String, TimeSeries> timeSeriesMap = new HashMap<>();
    private Map<String, JFreeChart> chartsMap = new HashMap<>();
    public static String[] INDICATORS = {Indicators.TABLE_FULL_COUNT, Indicators.TABLE_OCCUPATION, Indicators.MISS_FLOW_COUNT};

    public SwitchStateVizualisation(String switchName) {
        this.switchName = switchName;
    }
    
    

    public void init() {
        for (String s : INDICATORS) {

            TimeSeries ts = new TimeSeries(s, Millisecond.class);

            timeSeriesMap.put(s, ts);

            TimeSeriesCollection dataset = new TimeSeriesCollection();
            dataset.addSeries(ts);

            DateAxis domain = new DateAxis("Time"); // Axe X
            NumberAxis range = new NumberAxis(s); // Axe Y
            domain.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));
            domain.setLabelFont(new Font("SansSerif", Font.PLAIN, 14));
            range.setLabelFont(new Font("SansSerif", Font.PLAIN, 14));
            range.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));

            XYItemRenderer renderer = new XYLineAndShapeRenderer(true, false);
            renderer.setSeriesPaint(0, getColor(s)); // on récupère la couleur de la courbe en fonction de la variable

            renderer.setStroke(new BasicStroke(3f, BasicStroke.JOIN_MITER, BasicStroke.JOIN_MITER));
            XYPlot plot = new XYPlot(dataset, domain, range, renderer);
            plot.setBackgroundPaint(Color.white);
            plot.setDomainGridlinePaint(Color.white);
            plot.setRangeGridlinePaint(Color.white);
            plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
            domain.setAutoRange(true);
            domain.setLowerMargin(0.0);
            domain.setUpperMargin(0.0);
            domain.setTickLabelsVisible(true);
            range.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

            range.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

            JFreeChart chart = new JFreeChart(s, new Font("SansSerif", Font.BOLD, 24), plot, true);
            chart.setBackgroundPaint(Color.white);
            chartsMap.put(s, chart);

        }

    }

    public void updateIndicator(String indicator, double value, long time) {

        TimeSeries ts = timeSeriesMap.get(indicator);

        if (ts != null) {
            Millisecond millis = new Millisecond(new Date(time));
            ts.add(millis, value);

        }

    }

    public static Color getColor(String s) {
        switch (s) {
            case Indicators.TABLE_FULL_COUNT:
                return Color.RED;
            case Indicators.TABLE_OCCUPATION:
                return Color.BLUE;
            case Indicators.MISS_FLOW_COUNT:
                return Color.MAGENTA;

        }
        return Color.BLACK;
    }

    public String getSwitchName() {
        return switchName;
    }

    public void setSwitchName(String switchName) {
        this.switchName = switchName;
    }

    public Map<String, TimeSeries> getTimeSeriesMap() {
        return timeSeriesMap;
    }

    public void setTimeSeriesMap(Map<String, TimeSeries> timeSeriesMap) {
        this.timeSeriesMap = timeSeriesMap;
    }

    public Map<String, JFreeChart> getChartsMap() {
        return chartsMap;
    }

    public void setChartsMap(Map<String, JFreeChart> chartsMap) {
        this.chartsMap = chartsMap;
    }

    public static String[] getINDICATORS() {
        return INDICATORS;
    }

    public static void setINDICATORS(String[] INDICATORS) {
        SwitchStateVizualisation.INDICATORS = INDICATORS;
    }

}
