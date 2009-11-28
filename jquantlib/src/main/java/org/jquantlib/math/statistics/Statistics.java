/*
 Copyright (C) 2007 Richard Gomes

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
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

import org.jquantlib.math.Ops;
import org.jquantlib.util.Pair;

/**
 * @author Richard Gomes
 */
public interface Statistics {

    public double mean();

    public Pair<Double, Integer> expectationValue(Ops.DoubleOp f, Ops.DoublePredicate inRange);

    public int getSampleSize();

    public double percentile(double y);

    public double standardDeviation();

    public void add(double data, double value);

    public int samples();

    public void reset();

    public double weightSum();

    public double min();

    public double max();

    public double variance();

    public double skewness();

    public double kurtosis();

    public double downsideVariance();

    public double downsideDeviation();

    public double semiVariance();

    public double semiDeviation();

    public double errorEstimate();

    public double gaussianPercentile(double percentile);

    public double gaussianPotentialUpside(double percentile);

    public double potentialUpside(double percentile);

    public double gaussianValueAtRisk(double percentile);

    public double valueAtRisk(double percentile);

    public double gaussianExpectedShortfall(double percentile);

    public double expectedShortfall(double percentile);

    public double regret(double target);

    public double gaussianShortfall(double target);

    public double shortfall(double target);

    public double gaussianAverageShortfall(double target);

    public double averageShortfall(double target);

    public List<Pair<Double, Double>>  data();


//	public double mean(){ return 0.0; }
//	public double standardDeviation(){ return 0.0; }
//
//	public double  skewness(){ return 0.0;}
//	public double  kurtosis(){ return 0.0;}
//    @Override
//    public void add(double data, double value) {
//        // TODO Auto-generated method stub
//
//    }
//    @Override
//    public Pair<Double, Integer> expectationValue(final Ops.DoubleOp f, final Ops.DoublePredicate inRange) {
//        // TODO Auto-generated method stub
//        return null;
//    }
//    @Override
//    public int getSampleSize() {
//        // TODO Auto-generated method stub
//        return 0;
//    }
//    @Override
//    public double max() {
//        // TODO Auto-generated method stub
//        return 0;
//    }
//    @Override
//    public double min() {
//        // TODO Auto-generated method stub
//        return 0;
//    }
//    @Override
//    public double percentile(double y) {
//        // TODO Auto-generated method stub
//        return 0;
//    }
//    @Override
//    public void reset() {
//        // TODO Auto-generated method stub
//
//    }
//    @Override
//    public int samples() {
//        // TODO Auto-generated method stub
//        return 0;
//    }
//    @Override
//    public double variance() {
//        // TODO Auto-generated method stub
//        return 0;
//    }
//    @Override
//    public double weightSum() {
//        // TODO Auto-generated method stub
//        return 0;
//    }
//    @Override
//    public double averageShortfall(double target) {
//        // TODO Auto-generated method stub
//        return 0;
//    }
//    @Override
//    public double downsideDeviation() {
//        // TODO Auto-generated method stub
//        return 0;
//    }
//    @Override
//    public double downsideVariance() {
//        // TODO Auto-generated method stub
//        return 0;
//    }
//    @Override
//    public double errorEstimate() {
//        // TODO Auto-generated method stub
//        return 0;
//    }
//    @Override
//    public double expectedShortfall(double percentile) {
//        // TODO Auto-generated method stub
//        return 0;
//    }
//    @Override
//    public double gaussianAverageShortfall(double target) {
//        // TODO Auto-generated method stub
//        return 0;
//    }
//    @Override
//    public double gaussianExpectedShortfall(double percentile) {
//        // TODO Auto-generated method stub
//        return 0;
//    }
//    @Override
//    public double gaussianPercentile(double percentile) {
//        // TODO Auto-generated method stub
//        return 0;
//    }
//    @Override
//    public double gaussianPotentialUpside(double percentile) {
//        // TODO Auto-generated method stub
//        return 0;
//    }
//    @Override
//    public double gaussianShortfall(double target) {
//        // TODO Auto-generated method stub
//        return 0;
//    }
//    @Override
//    public double gaussianValueAtRisk(double percentile) {
//        // TODO Auto-generated method stub
//        return 0;
//    }
//    @Override
//    public double potentialUpside(double percentile) {
//        // TODO Auto-generated method stub
//        return 0;
//    }
//    @Override
//    public double regret(double target) {
//        // TODO Auto-generated method stub
//        return 0;
//    }
//    @Override
//    public double semiDeviation() {
//        // TODO Auto-generated method stub
//        return 0;
//    }
//    @Override
//    public double semiVariance() {
//        // TODO Auto-generated method stub
//        return 0;
//    }
//    @Override
//    public double shortfall(double target) {
//        // TODO Auto-generated method stub
//        return 0;
//    }
//    @Override
//    public double valueAtRisk(double percentile) {
//        // TODO Auto-generated method stub
//        return 0;
//    }
//    @Override
//    public List<Pair<Double, Double>> data() {
//        // TODO Auto-generated method stub
//        return null;
//    }

}
