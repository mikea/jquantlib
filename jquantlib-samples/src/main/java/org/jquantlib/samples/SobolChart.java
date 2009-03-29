package org.jquantlib.samples;

import javax.swing.JFrame;

import org.jquantlib.math.randomnumbers.SobolRSG;


public class SobolChart extends JFrame {

    private final SobolRSG sobol;
    
    public SobolChart() {
        super("SobolChart Demo");
        this.sobol = new SobolRSG(20000); // 100x100 (x,y)
    }
    
    public void draw() {
        double[] sequence = sobol.nextSequence().getValue();
    }

    
    public static void main(String args[]) {
        JFrame frame = new SobolChart();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
