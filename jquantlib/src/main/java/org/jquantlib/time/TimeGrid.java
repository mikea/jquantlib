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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.jquantlib.lang.annotation.NonNegative;
import org.jquantlib.lang.annotation.Time;
import org.jquantlib.math.Closeness;
import org.jquantlib.math.DoubleComparatorImpl;
import org.jquantlib.util.stdlibc.DoubleForwardIterator;
import org.jquantlib.util.stdlibc.DoubleReverseIterator;
import org.jquantlib.util.stdlibc.Std;

import cern.colt.Sorting;

/**
 * TimeGrid class.
 * 
 * @author Dominik Holenstein
 * @author Richard Gomes
 */
// TODO: Taken over from QuantLib: What was the rationale for limiting the grid to positive times? 
// Investigate and see whether we can use it for negative ones as well.
public class TimeGrid {
    
    //
    // private fields
    //  
    private final double[] times;
    private final double[] dt;
    private final double[] mandatoryTimes;

    

    // 
    // Constructors
    //
    
    /**
     * Regularly spaced time-grid
     * 
     * @param end
     * @param steps
     */
    public TimeGrid(@Time @NonNegative final double end, @NonNegative final int steps) {
        // We seem to assume that the grid begins at 0.
        // Let's enforce the assumption for the time being
        // (even though I'm not sure that I agree.)
        if (end <= 0.0) throw new IllegalArgumentException("negative times not allowed"); // FIXME: message
        
        /*@Time*/ double dt = end/steps;
        this.times = new double[steps+1];
        for (int i=0; i<=steps; i++){
            times[i] = dt*i;
        }
        this.mandatoryTimes = new double[1];
        this.mandatoryTimes[0] = end;
        
        this.dt = new double[steps];
        for (int i=0; i<steps; i++){
            this.dt[i] = dt;
        }
    }

    
    
    /**
     * Time grid with mandatory time points.
     * <p>
     * Mandatory points are guaranteed to belong to the grid. No additional points are added.
     * 
     * @note This constructor is not available yet
     * 
     * @param list
     */
    //TODO: needs code review when integrated to callers. Fix adjacent_difference before using 
    public TimeGrid(@Time @NonNegative final double[] array) {
 
        if (System.getProperty("EXPERIMENTAL")==null) throw new UnsupportedOperationException("This constructor is not available yet");

        this.mandatoryTimes = Arrays.copyOf(array, array.length);
        Sorting.quickSort(mandatoryTimes, 0, mandatoryTimes.length, new DoubleComparatorImpl()); // should use a 'default' comparator
    
        // We seem to assume that the grid begins at 0.
        // Let's enforce the assumption for the time being
        // (even though I'm not sure that I agree.)

        if (mandatoryTimes[0] < 0.0){
            throw new ArithmeticException("negative times not allowed");
        }

        List<Double> e = new DoubleArrayList(mandatoryTimes);
        double prev = mandatoryTimes[0];
        e.add(prev);
        for (int i=1; i<mandatoryTimes.length; i++) {
            double curr = mandatoryTimes[i];
            if (! Closeness.isCloseEnough(prev, curr)) {
                e.add(curr);
            }
            prev = curr;
        }
        
        DoubleArrayList tmp = new DoubleArrayList();
        if (mandatoryTimes[0] > 0.00) {
            tmp.add(0.0);
            tmp.addElements(1, mandatoryTimes);
        }
        else{
            tmp.addElements(0, mandatoryTimes);
        }
        times = tmp.toDoubleArray();
        //FIXME: Review when adjacent_difference is fixed. null is wrong.
        //dt = Std.adjacent_difference(times, 1, null);
    }


    
    /**
     * Time grid with mandatory time points
     * <p>
     * Mandatory points are guaranteed to belong to the grid.
     * Additional points are then added with regular spacing between pairs of mandatory times in order
     * to reach the desired number of steps.
     * 
     * @note This constructor is not available yet - fix adjacent_difference before using 
     */
    //TODO: needs code review when integrated to callers.
    public TimeGrid(@Time final double[] array, final int steps) {
        
        if (System.getProperty("EXPERIMENTAL")==null) throw new UnsupportedOperationException("This constructor is not available yet");
        
        mandatoryTimes = Arrays.copyOf(array, array.length);
        Sorting.quickSort(mandatoryTimes, 0, array.length, null); // should use a 'default' comparator
          
        // We seem to assume that the grid begins at 0.
        // Let's enforce the assumption for the time being
        // (even though I'm not sure that I agree.)
        if (mandatoryTimes[0] < 0.0) throw new ArithmeticException("negative times not allowed");
         
        double last = mandatoryTimes[mandatoryTimes.length - 1];
        
        double dtMax;
        // The resulting timegrid have points at times listed in the input
        // list. Between these points, there are inner-points which are
        // regularly spaced.
        
        if(steps == 0){
            List<Double> diff = new ArrayList<Double>();
            List templist = Arrays.asList(mandatoryTimes);
            //FIXME: Review when adjacent_difference is fixed. 
            Std.adjacent_difference(templist, 1, diff);
            if(diff.get(0)==0.0){
                diff.remove(0.0);
            }
            dtMax = Std.min_element(0, diff.size()-1, diff);
        }
        else{
            dtMax = last/steps;
        }
        
        double periodBegin = 0.0;
       
        DoubleArrayList temp_times_ = new DoubleArrayList();
        temp_times_.add(periodBegin);
        int m_length = mandatoryTimes.length;
        for(int i = 0; i<m_length; i++){
            double periodEnd = mandatoryTimes[i];
            if(periodEnd != 0){
                //the nearest integer
                int nSteps = (int) Math.round((periodBegin - periodEnd)/dtMax + 0.5);
                // at least one time step!
                nSteps = (nSteps!=0 ? nSteps : 1);
                double dt = (periodEnd - periodBegin)/nSteps;
                for (int n=1; n<=nSteps; ++n){
                    temp_times_.add(periodBegin + n*dt);
                }
            }
            periodBegin = periodEnd;
        }
        times = temp_times_.toDoubleArray();
        
        //FIXME: Review when adjacent_difference is fixed. null is wrong
        //dt = Std.adjacent_difference(times, 1, null);
    }
        
        /* NO IDEA WHAT THIS IS ! */
//          
//        // The resulting timegrid have points at times listed in the input
//        // list. Between these points, there are inner-points which are
//        // regularly spaced.
//        if (steps == 0) {
//          List<Double> diff = new DoubleArrayList();
//            
//     
//          std::adjacent_difference(mandatoryTimes_.begin(), mandatoryTimes_.end(), std::back_inserter(diff));       
//              if (diff.front()==0.0) diff.erase(diff.begin());
//              dtMax = *(std::min_element(diff.begin(), diff.end()));
//          } else {
//          dtMax = last/steps;
//      }
//
//          // diff = Std.adjacent_difference(mandatoryTimes_, mandatoryTimes_.indexOf(begin()), diff);
//          
//          
//          // The line above replaces the code commented out below:    
//          for (int i=0; i<mandatoryTimes_.size(); i++) {
//              double curr = mandatoryTimes_.get(i);
//                  if (i == 0) {
//                      diff.add(curr);
//                  }
//                  else {
//                      double prev = mandatoryTimes_.get(i-1);
//                      diff.add(curr-prev);
//                  }
//          }
//            
//          if (diff.get(0) == 0.00) diff.remove(begin());
//          dtMax = Collections.min(diff); // QuantLib: dtMax = *(std::min_element(diff.begin(), diff.end()));
//        }
//        else {
//          dtMax = last / steps;
//        }
//            
//        double periodBegin = 0.00;
//        times_.add(periodBegin);
//            
//        for (int i=1; i<mandatoryTimes_.size(); i++) {
//          double periodEnd = i;
//          if (periodEnd != 0.0) {
//              // the nearest integer
//                  int nSteps = (int)((periodEnd - periodBegin)/dtMax+0.5);
//                  
//                  // at least one time step!
//                  nSteps = (nSteps!=0 ? nSteps : 1);
//                  
//                  double dt = (periodEnd - periodBegin)/nSteps;
//                
//                  // TODO: Is this necessary in Java? See here for vector::reserve : http://www.cplusplus.com/reference/stl/vector/reserve.html
//                  // times_.reserve(nSteps);
//                
//                  for (int n=1; n<=nSteps; ++n)
//                      times_.add(periodBegin + n*dt);
//          }
//          periodBegin = periodEnd;
//        }
//       
////        std::adjacent_difference(times_.begin()+1,times_.end(),
////      std::back_inserter(dt_));
//        
//      // FIXME: needs code review             
//        //dt_ = Std.adjacent_difference(times_, times_.indexOf(begin())+1, dt_);
//        
//        
//        // The line above replaces the code commented out below:
////        for (int i=1; i<mandatoryTimes_.size(); i++) {
////            double curr = times_.get(i);
////            if (i == 0) {
////                dt_.add(curr);
////            }
////            else {
////                double prev = times_.get(i-1);
////                dt_.add(curr-prev);
////            }
////         }
    
    

    
        public @NonNegative int index(@Time @NonNegative final double t) /* @ReadOnly */ {
            @NonNegative int i = closestIndex(t);
            if (Closeness.isCloseEnough(t, times[i])) {
                return i;
            } else {
                if (t < front()) {
                    throw new IllegalArgumentException(
                            "using inadequate time grid: all nodes are later than the required time t = "
                            + t + " (earliest node is t1 = " + times[0] + ")" );
                } else if (t > back()) {
                    throw new IllegalArgumentException(
                            "using inadequate time grid: all nodes are earlier than the required time t = "
                            + t + " (latest node is t1 = " + back() + ")" );
                } else {
                    /*@NonNegative*/ int j, k;
                    if (t > times[i]) {
                        j = i;
                        k = i+1;
                    } else {
                        j = i-1;
                        k = i;
                    }
                    throw new IllegalArgumentException(
                            "using inadequate time grid: the nodes closest to the required time t = "
                            + t + " are t1 = " + times[j] + " and t2 = " + times[k] );
                }
            }
        }

        
        public @NonNegative int closestIndex(@Time @NonNegative final double t) /* @ReadOnly */ {
            int size = times.length;
            int result = Std.lower_bound(times, t);

            if (result == 0) {
                return 0;
            } else if (result == size) {
                return size-1;
            } else {
                @Time double dt1 = times[result] - t;
                @Time double dt2 = t - times[result-1];
                if (dt1 < dt2)
                    return result;
                else
                    return (result)-1;
            }
        }
        
        /**
         * @return the time on the grid closest to the given t
         */
        public @Time double closestTime (@Time @NonNegative final double t) /*@Readonly*/ {
            return times[closestIndex(t)];
        }
        
        public final double[] mandatoryTimes() /*@Readonly*/ {
            return mandatoryTimes;
        }
        
        public double dt (final int i) /*@Readonly*/ { 
           return dt[i];
        }
       
        public double get(final int i) /*@Readonly*/ { 
            return times[i]; 
        }
        
        public double at(final int i) /*@Readonly*/ { 
            return times[i]; 
        }
        
        
        public int size() /*@Readonly*/ { 
            return times.length; 
        }
        
        public boolean empty() /*@Readonly*/ { 
            return times.length == 0; 
        }
        
        public double begin() /*@Readonly*/ { 
            return times[0]; 
        }
        
        public double end() /*@Readonly*/ {
            return times[times.length-1]; 
        }

        public DoubleForwardIterator forwardIterator() /*@Readonly*/ { 
            return Std.forwardIterator(times);
        }
        
        public DoubleReverseIterator reverseIterator() /*@Readonly*/ { 
            return Std.reverseIterator(times);
        }
        
        public double front() /*@Readonly*/ { 
            return times[0];
        }
        
        public double back() /*@Readonly*/ { 
            return times[times.length-1]; 
        }
}
