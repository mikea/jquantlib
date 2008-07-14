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

import it.unimi.dsi.fastutil.BidirectionalIterator;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.jquantlib.math.Closeness;
import org.jquantlib.util.ReverseIterator;
import org.jquantlib.util.UnmodifiableIterator;

/**
 * TimeGrid class.
 * 
 * 
 * @author Dominik Holenstein
 *
 */
public class TimeGrid <T extends List<Double>> {
	
	// Typical C++ stuff...
	// typedef std::vector<Time>::const_iterator const_iterator;
    // typedef std::vector<Time>::const_reverse_iterator const_reverse_iterator;
	
	// time grid class
    // TODO: Taken over from QuantLib: What was the rationale for limiting the grid to positive times? Investigate and see whether we can use it for negative ones as well.
   
	//
	// private fields
	//	
	
	private double end_;
	private int steps_;
	
// XXX
//	private MyIterator endIterator_;
//	private MyIterator beginIterator_;
	
	private List<Double> times_ = new DoubleArrayList();
    private List<Double> dt_ = new DoubleArrayList();
    private List<Double> mandatoryTimes_ = new DoubleArrayList();
	
	
	// 
	// Constructors
	//
	public TimeGrid() {
	}

	/** Regularly spaced time-grid
	* Time grid with mandatory time points
    * Mandatory points are guaranteed to belong to the grid.
    * No additional points are added.
    * 
    * @param end
    * @param steps
    */
    public TimeGrid(double end, int steps){
    	this.end_ = end;
    	this.steps_ = steps;
    }
   	
    public TimeGrid(T list) {
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

    
    
        
        
        // Time grid with mandatory time points
        // Mandatory points are guaranteed to belong to the grid.
        //    Additional points are then added with regular spacing
        //    between pairs of mandatory times in order to reach the
        //    desired number of steps.
        //
// TODO: Translate
	    public TimeGrid(final T list, final int steps) {
	        mandatoryTimes_.addAll(list);
	    	Collections.sort(mandatoryTimes_); // FIXME: performance

	    	/*
            // We seem to assume that the grid begins at 0.
            // Let's enforce the assumption for the time being
            // (even though I'm not sure that I agree.)
            QL_REQUIRE(mandatoryTimes_.front() >= 0.0,
                       "negative times not allowed");
            std::vector<Double>::iterator e =
                std::unique(mandatoryTimes_.begin(),mandatoryTimes_.end(),
                            std::ptr_fun(close_enough));
            mandatoryTimes_.resize(e - mandatoryTimes_.begin());

            double last = mandatoryTimes_.back();
            double dtMax;
            // The resulting timegrid have points at times listed in the input
            // list. Between these points, there are inner-points which are
            // regularly spaced.
            if (steps == 0) {
                ArrayList<Double> diff;
                std::adjacent_difference(mandatoryTimes_.begin(),
                                         mandatoryTimes_.end(),
                                         std::back_inserter(diff));
                if (diff.front()==0.0)
                    diff.erase(diff.begin());
                dtMax = *(std::min_element(diff.begin(), diff.end()));
            } else {
                dtMax = last/steps;
            }
            
                      
            double periodBegin = 0.0;
            times_.push_back(periodBegin);
            for (std::vector<Double>::const_iterator t=mandatoryTimes_.begin();
                                                   t<mandatoryTimes_.end();
                                                   t++) {
                double periodEnd = t;
                if (periodEnd != 0.0) {
                    // the nearest integer
                    int nSteps = (int)((periodEnd - periodBegin)/dtMax+0.5);
                    // at least one time step!
                    nSteps = (nSteps!=0 ? nSteps : 1);
                    Time dt = (periodEnd - periodBegin)/nSteps;
                    times_.reserve(nSteps);
                    for (int n=1; n<=nSteps; ++n)
                        times_.push_back(periodBegin + n*dt);
                }
                periodBegin = periodEnd;
            }

            std::adjacent_difference(times_.begin()+1,times_.end(),
                                     std::back_inserter(dt_));
        }
        
        }
        
        */ 
	    
	    
	    }
    
    
    
        //
        //name Time grid interface
        //
    
    	// TODO: Translations
        //returns the index i such that grid[i] = t
        // public int index /*Read-only*/ (double t){
        // ?????
        //}
        
        // returns the index i such that grid[i] is closest to t
        // public int closestIndex /*Read-only*/ (double t){
    	// ???????
        // }
        
        // returns the time on the grid closest to the given t
        
//        public int closestTime (final double t) /*@Readonly*/ {
//            return times_.get(closestIndex(t));
//        }
        
        public final List<Double> mandatoryTimes() /*@Readonly*/ {
            return mandatoryTimes_;
        }
        
        public double dt (final int i) /*@Readonly*/ { 
           return dt_.get(i);
        }
       
        private double get(final int i) /*@Readonly*/ { 
        	return times_.get(i); 
        }
        
        private double at(final int i) /*@Readonly*/ { 
        	return times_.get(i); 
        }
        
        
        private int size() /*@Readonly*/ { 
        	return times_.size(); 
        }
        
        private boolean empty() /*@Readonly*/ { 
        	return times_.isEmpty(); 
        }
        
        private double begin() /*@Readonly*/ { 
        	return times_.get(0); 
        }
        
        private double end() /*@Readonly*/ {
        	return times_.lastIndexOf(mandatoryTimes_); 
        }
        

        // RICHARD: ==== Have a look at DoubleBidirectionalIterator in fastutil :) ====
        
        
        // TODO: Translate
        private Iterator<Double> reverseIterator() /*@Readonly*/ { 
        	BidirectionalIterator<Double> it = ((DoubleArrayList)times_).listIterator();
        	return new UnmodifiableIterator<Double>(new ReverseIterator<Double>(it)); 
        }
        
        // XXX: (RICHARD::) This method dissapears because reverseIterator returns an Iterator which implicitly 
        // XXX: has a begin and an end.
        //
        // private const_reverse_iterator rend() /*@Readonly*/ { 
        // 	return times_.rend(); 
        // }
        
        private double front() /*@Readonly*/ { 
         	return times_.get(0);
        }
        
        private double back() /*@Readonly*/ { 
        	return times_.get(times_.size()-1); 
        }

}
