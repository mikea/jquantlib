package org.jquantlib.samples;

import java.awt.Color;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;
import org.jquantlib.math.randomnumbers.SobolRSG;


public class SobolChart extends JFrame implements Runnable {

    private final SobolRSG sobol;
    private final JFreeChart chart;
    private final XYSeriesCollection dataset;
    private final int samples;

    public SobolChart() {
        this(10000);
    }
    
    public SobolChart(final /*@NonNegative*/ int samples) {
        super("SobolChart Demo");
        this.samples = samples;
        this.sobol = new SobolRSG(2*this.samples);
        this.dataset = new XYSeriesCollection();
        this.chart = ChartFactory.createScatterPlot("SobolRSG", "x", "y", dataset, PlotOrientation.VERTICAL, true, true, false);
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(4, 4, 4, 4));
    }
    
    private JFreeChart createDefaultChart() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        JFreeChart chart = ChartFactory.createScatterPlot("SobolRSG", "x", "y", dataset, PlotOrientation.VERTICAL, true, true, false);
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(4, 4, 4, 4));
        return chart;
    }
    
    public void run() {
        double[] sequence = sobol.nextSequence().getValue();
        dataset.removeAllSeries();
        XYSeries series = new XYSeries("Series");
        final int mean = samples/2;
        for (int i=0; i<mean; i++) {
            series.add(sequence[i], sequence[i+mean]);            
        }
        dataset.addSeries(series);
    }

    
    public static void main(String args[]) {
        SobolChart frame = new SobolChart();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.run();
    }

}
