
package org.jquantlib.time;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleBidirectionalIterator;
import it.unimi.dsi.fastutil.doubles.DoubleIterators;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.jquantlib.math.Closeness;
import org.jquantlib.util.ReverseIterator;
import org.jquantlib.util.Std;

import cern.colt.Sorting;

/**
 * This class is just for testing the adjcent_difference implementation in the class TimeGrid and 
 * will be removed soon.
 * @author Dominik Holenstein
 *
 */
public class AdjDiff {
	
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
		
		System.out.println("time_: " + times_);
		System.out.println("dt_0: " + dt_0);
		System.out.println("dt_1: " + dt_1);
		
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
