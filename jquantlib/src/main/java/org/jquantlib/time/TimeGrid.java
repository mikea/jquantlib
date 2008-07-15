/*
 Copyright (C) 2007 Richard Gomes

 This source code is release under the BSD License.
 
 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the JQuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquant-devel@lists.sourceforge.net>. The license is also available online at
 <http://www.jquantlib.org/index.php/LICENSE.TXT>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */


package org.jquantlib.time;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleBidirectionalIterator;
import it.unimi.dsi.fastutil.doubles.DoubleIterators;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.jquantlib.math.Closeness;
import org.jquantlib.util.ReverseIterator;

import cern.colt.Sorting;

/**
 * TimeGrid class.
 * 
 * @author Dominik Holenstein
 * @author Richard Gomes
 */
// TODO: Taken over from QuantLib: What was the rationale for limiting the grid to positive times? 
// Investigate and see whether we can use it for negative ones as well.
public class TimeGrid <T extends List<Double>> {
	
	//
	// private fields
	//	
    private List<Double> times_ = new DoubleArrayList();
    private List<Double> dt_ = new DoubleArrayList();
    private List<Double> mandatoryTimes_ = new DoubleArrayList();

// XXX    
//	private double end_;
//	private int steps_;
	
	
	// 
	// Constructors
	//
	public TimeGrid() {
	}

	/**
     * Regularly spaced time-grid
     * 
     * @param end
     * @param steps
     */
    public TimeGrid(/*@Time*/ double end, /*@NonNegative*/ int steps) {
        // We seem to assume that the grid begins at 0.
        // Let's enforce the assumption for the time being
        // (even though I'm not sure that I agree.)
        if (end <= 0.0) throw new IllegalArgumentException("negative times not allowed"); // FIXME: message
        /*@Time*/ double dt = end/steps;
        this.times_ = new DoubleArrayList(steps);
        for (int i=0; i<=steps; i++)
            times_.add(dt*i);

        this.mandatoryTimes_ = new DoubleArrayList(1);
        this.mandatoryTimes_.add(end);
        this.dt_ = new DoubleArrayList(steps);
        this.dt_.set(steps, dt);
    }

    
    
    /**
     * Time grid with mandatory time points
     * Mandatory points are guaranteed to belong to the grid.
     * No additional points are added.
     * @param list
     */
    public TimeGrid(final T list) {
        mandatoryTimes_.addAll(list);
    	Collections.sort(mandatoryTimes_); // FIXME: performance
    	   	
    	// We seem to assume that the grid begins at 0.
        // Let's enforce the assumption for the time being
        // (even though I'm not sure that I agree.)
        if (mandatoryTimes_.get(0) < 0.0) {
        	throw new ArithmeticException("negative times not allowed");
        }
    	
        List<Double> e = new DoubleArrayList(mandatoryTimes_.size());
        double prev = mandatoryTimes_.get(0); 
        e.add(prev);
        int size = 1;
        for (int i=1; i<mandatoryTimes_.size(); i++) {
        	double curr = mandatoryTimes_.get(i);
        	if (! Closeness.isCloseEnough(prev, curr)) {
        		e.add(curr); size++;
        	}
        	prev = curr;
        }
        // resize array, discarding unneeded memory
        ((DoubleArrayList) e).size(size);
    	
    	if(mandatoryTimes_.get(0) > 0.00){
    		times_.add(0, 0.00);
    	}
    	times_.addAll(mandatoryTimes_);

    	// TODO: (RICHARD::) It's necessary to understand what std::adjacent_difference does !! :(
    	// :::::::::::::::  http://www.sgi.com/tech/stl/adjacent_difference.html :::::::::::::::::::
        // std::adjacent_difference(times_.begin()+1,times_.end(), std::back_inserter(dt_));

        }


    
        /**
         * Time grid with mandatory time points
         * <p>
         * Mandatory points are guaranteed to belong to the grid.
         * Additional points are then added with regular spacing
         * between pairs of mandatory times in order to reach the
         * desired number of steps.
         */
        public TimeGrid(final T list, final int steps) {
          mandatoryTimes_.addAll(list);
          Collections.sort(mandatoryTimes_); // FIXME: performance
          
          // We seem to assume that the grid begins at 0.
          // Let's enforce the assumption for the time being
          // (even though I'm not sure that I agree.)
          if (mandatoryTimes_.get(0) < 0.0) {
          	throw new ArithmeticException("negative times not allowed");
          }
          
          List<Double> e = new DoubleArrayList(mandatoryTimes_.size());
          
          //TODO: Translation.
//        mandatoryTimes_.resize(e - mandatoryTimes_.begin());
          
          // TODO: Check the translation.
          double last = mandatoryTimes_.get(mandatoryTimes_.indexOf(back())); 
          
          // TODO: remove this assignment as soon as we have translated the commented code below.
          double dtMax = 0.00;
          
          // The resulting timegrid have points at times listed in the input
          // list. Between these points, there are inner-points which are
          // regularly spaced.
          if (steps == 0) {
        	  List<Double> diff = new DoubleArrayList();
        	  
        	  // TODO: Understand what std::adjacent_difference and std::back_inserter(diff) are doing
//       	  std::adjacent_difference(mandatoryTimes_.begin(),
//                      mandatoryTimes_.end(),
//                      std::back_inserter(diff));
        	  
//              if (diff.front()==0.0)
//              diff.erase(diff.begin());
//          dtMax = *(std::min_element(diff.begin(), diff.end()));
//      } else {
//          dtMax = last/steps;
//      }
        	  double periodBegin = 0.00;
        	  times_.add(periodBegin);
        	  
        	  for (int i=1; i<mandatoryTimes_.size(); i++) {
              	double periodEnd = i;
              	
              	if (periodEnd != 0.0) {
              	// the nearest integer
              	int nSteps = (int)((periodEnd - periodBegin)/dtMax+0.5);
              	
              	// at least one time step!
              	nSteps = (nSteps!=0 ? nSteps : 1);
              	
                double dt = (periodEnd - periodBegin)/nSteps;
                
                // TODO: Is this necessary in Java? See here for vector::reserve : http://www.cplusplus.com/reference/stl/vector/reserve.html
                // times_.reserve(nSteps);
                
                for (int n=1; n<=nSteps; ++n)
                    times_.add(periodBegin + n*dt);
              	}
              	periodBegin = periodEnd;
              }
        	  // TODO: Understand what adjacent_difference does.
//	            std::adjacent_difference(times_.begin()+1,times_.end(),
//              std::back_inserter(dt_));
        	  
          }
        }

    
	    public /*@NonNegative*/ int index(/*@Time*/ double t) /* @ReadOnly */ {
	        /*@NonNegative*/ int i = closestIndex(t);
	        if (Closeness.isCloseEnough(t, times_.get(i))) {
	            return i;
	        } else {
	            if (t < front()) {
	                throw new IllegalArgumentException(
	                        "using inadequate time grid: all nodes are later than the required time t = "
	                        + t + " (earliest node is t1 = " + times_.get(0) + ")" );
	            } else if (t > back()) {
	                throw new IllegalArgumentException(
	                        "using inadequate time grid: all nodes are earlier than the required time t = "
	                        + t + " (latest node is t1 = " + back() + ")" );
	            } else {
	                /*@NonNegative*/ int j, k;
	                if (t > times_.get(i)) {
	                    j = i;
	                    k = i+1;
	                } else {
	                    j = i-1;
	                    k = i;
	                }
	                throw new IllegalArgumentException(
	                        "using inadequate time grid: the nodes closest to the required time t = "
	                        + t + " are t1 = " + times_.get(j) + " and t2 = " + times_.get(k) );
	            }
	        }
	    }

        
	    public /*@NonNegative*/ int closestIndex(final /*@Time*/ double t) /* @ReadOnly */ {
	        int size = times_.size();
	        int result = Sorting.binarySearchFromTo( ((DoubleArrayList)times_).toDoubleArray(), t, 0, size-1) /*-1*/;
	        
	        if (result == 0) {
	            return 0;
	        } else if (result == size) {
	            return size-1;
	        } else {
	            /*@Time*/ double dt1 = times_.get(result) - t;
	            /*@Time*/ double dt2 = t - times_.get(result-1);
	            if (dt1 < dt2)
	                return result;
	            else
	                return (result)-1;
	        }
	    }
        
	    /**
	     * @return the time on the grid closest to the given t
	     */
        public /*@Time*/ double closestTime (final /*@Time*/ double t) /*@Readonly*/ {
            return times_.get(closestIndex(t));
        }
        
        public final List<Double> mandatoryTimes() /*@Readonly*/ {
            return mandatoryTimes_;
        }
        
        public double dt (final int i) /*@Readonly*/ { 
           return dt_.get(i);
        }
       
        public double get(final int i) /*@Readonly*/ { 
        	return times_.get(i); 
        }
        
        public double at(final int i) /*@Readonly*/ { 
        	return times_.get(i); 
        }
        
        
        public int size() /*@Readonly*/ { 
        	return times_.size(); 
        }
        
        public boolean empty() /*@Readonly*/ { 
        	return times_.isEmpty(); 
        }
        
        public double begin() /*@Readonly*/ { 
        	return times_.get(0); 
        }
        
        public double end() /*@Readonly*/ {
        	return times_.lastIndexOf(mandatoryTimes_); 
        }

        public Iterator<Double> reverseIterator() /*@Readonly*/ { 
        	DoubleBidirectionalIterator it = ((DoubleArrayList)times_).listIterator();
        	return new ReverseIterator<Double>(new DoubleIterators.UnmodifiableBidirectionalIterator(it));
        }
        
        // XXX: (RICHARD::) This method dissapears because reverseIterator returns an Iterator which implicitly 
        // XXX: has a begin and an end.
        //
        // private const_reverse_iterator rend() /*@Readonly*/ { 
        // 	return times_.rend(); 
        // }
        
        public double front() /*@Readonly*/ { 
         	return times_.get(0);
        }
        
        public double back() /*@Readonly*/ { 
        	return times_.get(times_.size()-1); 
        }

}
