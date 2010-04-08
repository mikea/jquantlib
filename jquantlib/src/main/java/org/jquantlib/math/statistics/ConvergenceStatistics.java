/*
 Copyright (C) 2009 Ueli Hofstetter

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


package org.jquantlib.math.statistics;

import java.util.List;

import org.jquantlib.util.Pair;


/*! This class decorates another statistics class adding a
convergence table calculation. The table tracks the
convergence of the mean.

It is possible to specify the number of samples at which the
mean should be stored by mean of the second template
parameter; the default is to store \f$ 2^{n-1} \f$ samples at
the \f$ n \f$-th step. Any passed class must implement the
following interface:
\code
Size initialSamples() const;
Size nextSamples(Size currentSamples) const;
\endcode
as well as a copy constructor.

\test results are tested against known good values.
*/
public class ConvergenceStatistics {

    private Statistics statistics;
    private final /*samplingRule*/ DoublingConvergenceSteps samplingRule_;
    private List<Pair<Integer,Double>> table_;
    private int nextSampleSize_;
    private int sampleSize;

    public ConvergenceStatistics(final Statistics T, final DoublingConvergenceSteps rule){
        if (System.getProperty("EXPERIMENTAL") == null)
            throw new UnsupportedOperationException("Work in progress");
        this.statistics = T;
        this.samplingRule_ = rule;
        reset();
    }

    public ConvergenceStatistics(final DoublingConvergenceSteps rule){
        if (System.getProperty("EXPERIMENTAL") == null)
            throw new UnsupportedOperationException("Work in progress");
        this.samplingRule_ = rule;
        reset();
    }

    public int initialSamples(){
        return 1;
    }

    public int nextSamples(final int current){
        return 2*current + 1;
    }


    public void add(final double value){
        add(value, 1.0);
    }

    public void add(final double value, final double weight){
        this.statistics.add(value, weight);
        if(this.statistics.samples() == nextSampleSize_){
            table_.add(new Pair<Integer, Double>(statistics.samples(), statistics.mean()));
            nextSampleSize_ = samplingRule_.nextSamples(nextSampleSize_);
        }
    }

    void addSequence(final double data []) {
        for (final double element : data) {
            add(element);
        }
    }


    public void addSequence(final double [] data, final double [] weight) {
        for (int i= 0; i<data.length; i++) {
            add(data[i], weight[i]);
        }
    }

    public void reset(){
        statistics.reset();
        nextSampleSize_ = samplingRule_.initialSamples();
        table_.clear();
    }

    public List<Pair<Integer,Double>> convergenceTable(){
        return table_;
    }

    class DoublingConvergenceSteps {
         public int initialSamples() { return 1; }
         public int nextSamples(final int current) { return 2 * current + 1; }
      };



}
