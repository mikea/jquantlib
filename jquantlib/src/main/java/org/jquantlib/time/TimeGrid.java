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

import java.util.List;

import org.joda.primitives.list.impl.ArrayDoubleList;
import org.jquantlib.QL;
import org.jquantlib.lang.annotation.NonNegative;
import org.jquantlib.lang.annotation.Time;
import org.jquantlib.math.Closeness;
import org.jquantlib.math.matrixutilities.Array;


/**
 * TimeGrid class.
 *
 * @author Dominik Holenstein
 */
// TODO: Taken over from QuantLib: What was the rationale for limiting the grid to positive times?
// Investigate and see whether we can use it for negative ones as well.
public class TimeGrid {

    //
    // private fields
    //
    private final Array times;
    private final Array dt;
    private final Array mandatoryTimes;



    //
    // Constructors
    //

    public TimeGrid() {
        this.times = null; //XXX  new Array();
        this.dt = null; //XXX: new Array();
        this.mandatoryTimes = null; //XXX: new Array();
    }

    /**
     * Regularly spaced time-grid
     *
     * @param end
     * @param steps
     */
    public TimeGrid(@Time @NonNegative final double end, @NonNegative final int steps) {

        // THIS COMMENT COMES FROM QuantLib/C++ code
        //
        // We seem to assume that the grid begins at 0.
        // Let's enforce the assumption for the time being
        // (even though I'm not sure that I agree.)
        QL.require(end > 0.0 , "negative times not allowed"); // QA:[RG]::verified // FIXME: message

        /*@Time*/ final double dt = end/steps;
        this.times = new Array(steps+1);
        for (int i=0; i<=steps; i++)
            times.set(i, dt*i);
        this.mandatoryTimes = new Array(1).fill(end);
        this.dt = new Array(steps).fill(dt);
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
    public TimeGrid(@Time @NonNegative final Array mandatoryTimes) {

        if (System.getProperty("EXPERIMENTAL")==null)
            throw new UnsupportedOperationException("This constructor is not available yet");

        // THIS COMMENT COMES FROM QuantLib/C++ code
        //
        // We seem to assume that the grid begins at 0.
        // Let's enforce the assumption for the time being
        // (even though I'm not sure that I agree.)
        QL.require(mandatoryTimes.first() < 0.0 , "negative times not allowed"); // QA:[RG]::verified // TODO: message

        this.mandatoryTimes = mandatoryTimes.clone();
        this.mandatoryTimes.sort();

        final List<Double> e = new ArrayDoubleList(this.mandatoryTimes.size());
        double prev = this.mandatoryTimes.get(0);
        e.add(prev);
        for (int i=1; i<this.mandatoryTimes.size(); i++) {
            final double curr = this.mandatoryTimes.get(i);
            if (! Closeness.isCloseEnough(prev, curr))
                e.add(curr);
            prev = curr;
        }

        final ArrayDoubleList tmp = new ArrayDoubleList();
        if (this.mandatoryTimes.first() > 0.00) {
            tmp.add(0.0);
            tmp.addAll(1, (double[])this.mandatoryTimes.toArray());
        } else
            tmp.addAll(0, (double[])this.mandatoryTimes.toArray());
        times = new Array(tmp.toDoubleArray());
        //FIXME: Review when adjacent_difference is fixed. null is wrong.
        //dt = Std.getInstance().adjacent_difference(times, 1, null);
        dt = null; /* Added to remove compile error - final fields must be initialised */
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
    public TimeGrid(@Time final Array mandatoryTimes, final int steps) {

        if (System.getProperty("EXPERIMENTAL")==null)
            throw new UnsupportedOperationException("This constructor is not available yet");

        // THIS COMMENT COMES FROM QuantLib/C++ code
        //
        // We seem to assume that the grid begins at 0.
        // Let's enforce the assumption for the time being
        // (even though I'm not sure that I agree.)
        QL.require(mandatoryTimes.first() >= 0.0 , "negative times not allowed"); // QA:[RG]::verified // TODO: message

        this.mandatoryTimes = mandatoryTimes.clone();
        this.mandatoryTimes.sort();

        // The resulting timegrid have points at times listed in the input
        // list. Between these points, there are inner-points which are
        // regularly spaced.

        double dtMax;
        if (steps == 0) {
            final Array diff = this.mandatoryTimes.adjacentDifference(1, this.mandatoryTimes.size());

            int idx_min = 0;
            final int idx_max = diff.size()-1;

            if (diff.first()==0.0)
                idx_min++;
            dtMax = diff.min(idx_min, idx_max);
        } else
            dtMax = this.mandatoryTimes.last() / steps;

        double periodBegin = 0.0;
        final ArrayDoubleList tempTimes = new ArrayDoubleList();
        tempTimes.add(periodBegin);
        final int m_length = this.mandatoryTimes.size();

        for(int i = 0; i<m_length; i++){
            final double periodEnd = this.mandatoryTimes.get(i);
            if(periodEnd != 0){
                //the nearest integer
                int nSteps = (int) Math.round((periodBegin - periodEnd)/dtMax + 0.5);
                // at least one time step!
                nSteps = (nSteps!=0 ? nSteps : 1);
                final double dt = (periodEnd - periodBegin)/nSteps;
                for (int n=1; n<=nSteps; ++n)
                    tempTimes.add(periodBegin + n*dt);
            }
            periodBegin = periodEnd;
        }
        times = new Array(tempTimes.toDoubleArray());

        //FIXME: Review when adjacent_difference is fixed. null is wrong
        //dt = times.adjacent_difference(1, null);
        dt = null; /* Added to remove compile error - final fields must be initialised */
    }


    //FIXME: code review :: compare against C++ code and eventually remove commented block below

    /* NO IDEA WHAT THIS IS ! */
    //
    //        // The resulting timegrid have points at times listed in the input
    //        // list. Between these points, there are inner-points which are
    //        // regularly spaced.
    //        if (steps == 0) {
    //          List<Double> diff = new ArrayDoubleList();
    //
    //
    //          std::adjacent_difference(mandatoryTimes_.begin(), mandatoryTimes_.end(), std::back_inserter(diff));
    //              if (diff.front()==0.0) diff.erase(diff.begin());
    //              dtMax = *(std::min_element(diff.begin(), diff.end()));
    //          } else {
    //          dtMax = last/steps;
    //      }
    //
    //          // diff = Std.getInstance().adjacent_difference(mandatoryTimes_, mandatoryTimes_.indexOf(begin()), diff);
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
    //        //dt_ = Std.getInstance().adjacent_difference(times_, times_.indexOf(begin())+1, dt_);
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
        final @NonNegative int i = closestIndex(t);
        if (Closeness.isCloseEnough(t, times.get(i)))
            return i;
        else if (t < front())
            throw new IllegalArgumentException(
                    "using inadequate time grid: all nodes are later than the required time t = "
                    + t + " (earliest node is t1 = " + times.first() + ")" );
        else if (t > back())
            throw new IllegalArgumentException(
                    "using inadequate time grid: all nodes are earlier than the required time t = "
                    + t + " (latest node is t1 = " + back() + ")" );
        else {
            /*@NonNegative*/ int j, k;
            if (t > times.get(i)) {
                j = i;
                k = i+1;
            } else {
                j = i-1;
                k = i;
            }
            throw new IllegalArgumentException(
                    "using inadequate time grid: the nodes closest to the required time t = "
                    + t + " are t1 = " + times.get(j) + " and t2 = " + times.get(k) );
        }
    }


    public @NonNegative int closestIndex(@Time @NonNegative final double t) /* @ReadOnly */ {
        final int size = times.size();
        final int result = times.lowerBound(t);

        if (result == 0)
            return 0;
        else if (result == size)
            return size-1;
        else {
            final @Time double dt1 = times.get(result) - t;
            final @Time double dt2 = t - times.get(result-1);
            if (dt1 < dt2)
                return result;
            else
                return result-1;
        }
    }

    /**
     * @return the time on the grid closest to the given t
     */
    public @Time double closestTime (@Time @NonNegative final double t) /*@Readonly*/ {
        return times.get(closestIndex(t));
    }

    public final Array mandatoryTimes() /*@Readonly*/ {
        // TODO: code review :: use of clone()
        return mandatoryTimes.clone();
    }

    public double dt (final int i) /*@Readonly*/ {
        return dt.get(i);
    }


    // TODO: code review :: get equivalent to at ???

    public double get(final int i) /*@Readonly*/ {
        return times.get(i);
    }

    public double at(final int i) /*@Readonly*/ {
        return times.get(i);
    }


    public int size() /*@Readonly*/ {
        return times.size();
    }

    public boolean empty() /*@Readonly*/ {
        return times.size() == 0;
    }

    public double begin() /*@Readonly*/ {
        return times.first();
    }

    public double end() /*@Readonly*/ {
        return times.last();
    }


    //TODO: remove old code below

    //        public DoubleForwardIterator forwardIterator() /*@Readonly*/ {
    //            return Std.getInstance().forwardIterator(times);
    //        }
    //
    //        public DoubleReverseIterator reverseIterator() /*@Readonly*/ {
    //            return Std.getInstance().reverseIterator(times);
    //        }


    //TODO: code review :: front equivalent to begin ??? back equivalent to end ???

    public double front() /*@Readonly*/ {
        return times.first();
    }

    public double back() /*@Readonly*/ {
        return times.last();
    }
}
