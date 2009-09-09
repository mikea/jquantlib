package org.jquantlib.samples;

import java.awt.Color;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;
import org.jquantlib.math.randomnumbers.SobolRSG;

/**
 *  
 * @author Zahid Hussain
 * @Changes: Fixed compilation error 
 */

public class SobolChartSample extends ApplicationFrame {

    private final SobolRSG sobol;
    private final int samples;
    private final int dimension;
    private final int seed;

    public SobolChartSample(final String title) {
        this(title, 500);
    }
    
    public SobolChartSample(final String title, final /*@NonNegative*/ int samples) {
        super(title);
        this.samples = samples;
        this.dimension = 5;
        this.seed = 42;
        this.sobol = new SobolRSG(this.dimension, this.seed);

        final JFreeChart chart = createDefaultChart();
        final ChartPanel panel = new ChartPanel(chart, true, true, true, false, true);
        panel.setPreferredSize(new java.awt.Dimension(800, 570));
        setContentPane(panel);
    }
    
    private JFreeChart createDefaultChart() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        JFreeChart chart = ChartFactory.createScatterPlot(this.getTitle(), "x", "y", dataset, PlotOrientation.VERTICAL, true, true, false);
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(4, 4, 4, 4));
        
        dataset.removeAllSeries();
        XYSeries series = new XYSeries("Series");
        for (int count=0; count<samples; count++) {
            for (int i=0; i<dimension; i++) {
                double[] sequence1 = sobol.nextSequence().value();
                double[] sequence2 = sobol.nextSequence().value();
                series.add(sequence1[i], sequence2[i]);            
            }
        }
        dataset.addSeries(series);
        return chart;
    }
    
    
    public static void main(final String[] args) {
        final SobolChartSample demo = new SobolChartSample("Sobol Chart Sample");
        demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
    
}
