
package org.jquantlib.util.stdlib;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jquantlib.util.stdlibc.Std;

/**
 * This class is just for testing the adjcent_difference implementation in the class TimeGrid and 
 * will be removed soon.
 * @author Dominik Holenstein
 *
 */
// FIXME: move to test suite
public class AdjDiff {
	
    private final static Logger logger = LoggerFactory.getLogger(AdjDiff.class);
    
    private static List<Double> times_ = new DoubleArrayList();
    private static List<Double> dt_1 = new DoubleArrayList();
    private static List<Double> dt_0 = new DoubleArrayList();

	public static void main(String[] args) {
	    
		int steps = 5;
		double end = 10;
		
		double dt = end/steps;
		
		times_ = new DoubleArrayList(steps);
		
		for (int i=0; i<=steps; i++)
			times_.add(dt*i);
		
		dt_1 = Std.adjacent_difference(times_, times_.indexOf(begin())+1, dt_1);
		dt_0 = Std.adjacent_difference(times_, times_.indexOf(begin()), dt_0);
		
		logger.debug("time_: " + times_);
		logger.debug("dt_0: " + dt_0);
		logger.debug("dt_1: " + dt_1);
		
/*		for (int i=1; i<times_.size(); i++) {
    		double curr = times_.get(i);
    		if (i == 0) {
    			dt_.add(times_.get(i));
    		}
    		else {
    			double prev = times_.get(i-1);
    			dt_.add(curr-prev);
    		}
    	} */
		
		
		

	}
	
	public static double begin() /*@Readonly*/ { 
    	return times_.get(0); 
    }

}
