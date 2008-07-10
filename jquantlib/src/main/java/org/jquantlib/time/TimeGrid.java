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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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
    * @param end
    * @param steps
    */

    public TimeGrid(double end, int steps){
    	this.end_ = end;
    	this.steps_ = steps;
    }
   	
    public TimeGrid(T list) {
        // RICHARD: You can assume QL_FULL_ITERATOR_SUPPORT is true in Java because we have JCF interfaces
        //XXX #if defined(QL_FULL_ITERATOR_SUPPORT) --> not necessary in Java
        
        mandatoryTimes_.addAll(list);
        
        //XXX #else

        
        
        
    	
    	// std::sort(mandatoryTimes_.begin(),mandatoryTimes_.end());
    	Collections.sort(mandatoryTimes_);
    	   	
    	// We seem to assume that the grid begins at 0.
        // Let's enforce the assumption for the time being
        // (even though I'm not sure that I agree.)
        if (mandatoryTimes_.get(0) < 0.0) {
        	throw new ArithmeticException("negative times not allowed");
        }
    	
        // TODO: Currently working here.
        // ArrayList<Double> e = 
        //     std::vector<Time>::iterator e =
        //         std::unique(mandatoryTimes_.begin(),mandatoryTimes_.end(),
        //                     std::ptr_fun(close_enough));
        ///    mandatoryTimes_.resize(e - mandatoryTimes_.begin());
    	
        List<Double> e = new DoubleArrayList();
        
    	
    	// if (mandatoryTimes_[0] > 0.0)
        //     times_.push_back(0.0);
    	if(mandatoryTimes_.get(0) > 0.00){
    		times_.add(0, 0.00);
    	}
    	
    	// TODO: Translate
        // times_.insert(times_.end(), mandatoryTimes_.begin(), mandatoryTimes_.end());

    	// TODO: Translate
        // std::adjacent_difference(times_.begin()+1,times_.end(), std::back_inserter(dt_));

        }
        
        
        
        // Time grid with mandatory time points
        // Mandatory points are guaranteed to belong to the grid.
        //    Additional points are then added with regular spacing
        //    between pairs of mandatory times in order to reach the
        //    desired number of steps.
        //
    
    	// TODO: Translate
    	/*
        public TimeGrid(MyIterator begin, MyIterator end, int steps)
        // #if defined(QL_FULL_ITERATOR_SUPPORT) --> not necessary in Java 
        : mandatoryTimes_(begin, end) {
        #else
        {
            while (begin != end)
                mandatoryTimes_.push_back(*(begin++));
        #endif
            std::sort(mandatoryTimes_.begin(),mandatoryTimes_.end());
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
    
    
        //
        //name Time grid interface
        //
    
    	// TODO: Translations
        //returns the index i such that grid[i] = t
        // public int index /*Read-only*/ (double t){
        //}
        
        // returns the index i such that grid[i] is closest to t
        // public int closestIndex /*Read-only*/ (double t){
        // }
        
        // returns the time on the grid closest to the given t
        
    	// TODO: Translate
        //public int closestTime /*Read-only*/(double t)  {
        //    return times_[closestIndex(t)];
        //}
        
        public final List<Double> mandatoryTimes() /*Read-only*/ {
            return mandatoryTimes_;
        }
        
        // TODO: Translate
        // public double dt (/* Read-only */(int i) { 
        // return dt_[i]; }
        //}
       
		// TODO: Translate
        //private double operator[] /*Read-only*/ (int i) { 
        //	return times_[i]; 
        //}
        
        private double at(int i) /*Read-only*/ { 
        	return times_.get(i); 
        }
        
        
        private int size() /*Read-only*/ { 
        	return times_.size(); 
        }
        
        private boolean empty() /*Read-only*/ { 
        	return times_.isEmpty(); 
        }
        
        private double begin() /*Read-only*/ { 
        	return times_.get(0); 
        }
        
        private double end() /*Read-only*/ {
        	return times_.lastIndexOf(mandatoryTimes_); 
        }
        

        // RICHARD: ==== Have a look at DoubleBidirectionalIterator in fastutil :) ====
        
        
        // TODO: Translate
        //private const_reverse_iterator rbegin() /*Read-only*/ { 
        //	return times_.rbegin(); 
        //}
        
        // private const_reverse_iterator rend() /*Read-only*/ { 
        // 	return times_.rend(); 
        // }
        
        // private double front() /*Read-only*/ { 
        // 	return times_.front();
        //}
        
        //private double back() /*Read-only*/ { 
        //	return times_.; 
        //}

}
