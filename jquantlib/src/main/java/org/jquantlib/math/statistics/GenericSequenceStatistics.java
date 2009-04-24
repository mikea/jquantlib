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

import org.jquantlib.math.Array;
import org.jquantlib.math.E_IUnaryFunction;
import org.jquantlib.math.Matrix;
import org.jquantlib.util.Pair;

public class GenericSequenceStatistics /* implements ISequenceStatistics */{

    private static final String unsufficient_sample_weight = "sampleWeight=0, unsufficient";
    private static final String unsufficient_sample_number = "sample number <=1, unsufficient";
    private static final String null_dimension = "null dimension";
    private static final String sample_size_mismatch = "sample size mismatch";

    protected int dimension_;

    protected IStatistics[] stats_;
    private double[] results_;
    private Matrix quadraticSum_;

    public GenericSequenceStatistics() {
        dimension_ = 0;
    };

    public GenericSequenceStatistics(int dimension) {
        dimension_ = 0;
        reset(dimension_);
    }
    
    public void add(double [] data){
        add(data, 1.0);
    }
    
    public void add(double [] data,
            double  weight) {
       if (dimension_ == 0) {
           // stat wasn't initialized yet
           reset(data.length);
       }

       if(data.length != dimension_){
           throw new IllegalArgumentException(sample_size_mismatch);
       }
           
       
       //TODO: implement this one
       /*
       quadraticSum_ += weight * outerProduct(begin, end,
                                              begin, end);*/

       for (int i=0; i<dimension_; i++){
           stats_[i].add(data[i], weight);
       }
       
       throw new UnsupportedOperationException("work in progress");
    }

    public int size() {
        return dimension_;
    }

    // start void method macro ....
    public double[] mean() {
        for (int i = 0; i < dimension_; i++) {
            results_[i] = stats_[i].mean();
        }
        return results_;
    }

    public double[] variance() {
        for (int i = 0; i < dimension_; i++) {
            results_[i] = stats_[i].variance();
        }
        return results_;
    }

    public double[] standardDeviation() {
        for (int i = 0; i < dimension_; i++) {
            results_[i] = stats_[i].standardDeviation();
        }
        return results_;
    }

    public double[] downsideVariance() {
        for (int i = 0; i < dimension_; i++) {
            results_[i] = stats_[i].downsideVariance();
        }
        return results_;
    }

    public double[] downsideDeviation() {
        for (int i = 0; i < dimension_; i++) {
            results_[i] = stats_[i].downsideDeviation();
        }
        return results_;
    }

    public double[] semiVariance() {
        for (int i = 0; i < dimension_; i++) {
            results_[i] = stats_[i].semiVariance();
        }
        return results_;
    }

    public double[] semiDeviation() {
        for (int i = 0; i < dimension_; i++) {
            results_[i] = stats_[i].semiDeviation();
        }
        return results_;
    }

    public double[] errorEstimate() {
        for (int i = 0; i < dimension_; i++) {
            results_[i] = stats_[i].errorEstimate();
        }
        return results_;
    }

    public double[] skewness() {
        for (int i = 0; i < dimension_; i++) {
            results_[i] = stats_[i].skewness();
        }
        return results_;
    }

    public double[] kurtosis() {
        for (int i = 0; i < dimension_; i++) {
            results_[i] = stats_[i].kurtosis();
        }
        return results_;
    }

    public double[] min() {
        for (int i = 0; i < dimension_; i++) {
            results_[i] = stats_[i].min();
        }
        return results_;
    }

    public double[] max() {
        for (int i = 0; i < dimension_; i++) {
            results_[i] = stats_[i].max();
        }
        return results_;
    }

    // start single argument method macros

    public double[] gaussianPercentile(double gaussianPercentile) {
        for (int i = 0; i < dimension_; i++) {
            results_[i] = stats_[i].gaussianPercentile(gaussianPercentile);
        }
        return results_;
    }

    public double[] gaussianPotentialUpside(double gaussianPotentialUpside) {
        for (int i = 0; i < dimension_; i++) {
            results_[i] = stats_[i].gaussianPotentialUpside(gaussianPotentialUpside);
        }
        return results_;
    }

    public double[] gaussianValueAtRisk(double gaussianValueAtRisk) {
        for (int i = 0; i < dimension_; i++) {
            results_[i] = stats_[i].gaussianPercentile(gaussianValueAtRisk);
        }
        return results_;
    }

    public double[] gaussianExpectedShortfall(double gaussianExpectedShortfall) {
        for (int i = 0; i < dimension_; i++) {
            results_[i] = stats_[i].gaussianExpectedShortfall(gaussianExpectedShortfall);
        }
        return results_;
    }

    public double[] gaussianShortfall(double gaussianShortfall) {
        for (int i = 0; i < dimension_; i++) {
            results_[i] = stats_[i].gaussianShortfall(gaussianShortfall);
        }
        return results_;
    }

    public double[] gaussianAverageShortfall(double gaussianAverageShortfall) {
        for (int i = 0; i < dimension_; i++) {
            results_[i] = stats_[i].gaussianPercentile(gaussianAverageShortfall);
        }
        return results_;
    }

    public double[] percentile(double percentile) {
        for (int i = 0; i < dimension_; i++) {
            results_[i] = stats_[i].gaussianPercentile(percentile);
        }
        return results_;
    }

    public double[] potentialUpside(double potentialUpside) {
        for (int i = 0; i < dimension_; i++) {
            results_[i] = stats_[i].potentialUpside(potentialUpside);
        }
        return results_;
    }

    public double[] valueAtRisk(double valueAtRisk) {
        for (int i = 0; i < dimension_; i++) {
            results_[i] = stats_[i].valueAtRisk(valueAtRisk);
        }
        return results_;
    }

    public double[] expectedShortfall(double expectedShortfall) {
        for (int i = 0; i < dimension_; i++) {
            results_[i] = stats_[i].expectedShortfall(expectedShortfall);
        }
        return results_;
    }

    public double[] regret(double regret) {
        for (int i = 0; i < dimension_; i++) {
            results_[i] = stats_[i].regret(regret);
        }
        return results_;
    }

    public double[] shortfall(double shortfall) {
        for (int i = 0; i < dimension_; i++) {
            results_[i] = stats_[i].gaussianPercentile(shortfall);
        }
        return results_;
    }

    public double[] averageShortfall(double averageShortfall) {
        for (int i = 0; i < dimension_; i++) {
            results_[i] = stats_[i].gaussianPercentile(averageShortfall);
        }
        return results_;
    }

    public Matrix correlation() {
        Matrix correlation = null;// covariance();
        double[] variances = correlation.diagonal();
        for (int i = 0; i < dimension_; i++) {
            for (int j = 0; j < dimension_; j++) {
                if (i == j) {
                    if (variances[i] == 0.0) {
                        correlation.set(i, j, 1.0);
                    } else {
                        correlation.set(i, j, correlation.get(i, j) * 1.0 / Math.sqrt(variances[i] * variances[j]));
                    }
                } else {
                    if (variances[i] == 0.0 && variances[j] == 0) {
                        correlation.set(i, j, 1.0);
                    } else if (variances[i] == 0.0 || variances[j] == 0.0) {
                        correlation.set(i, j, 1.0);
                    } else {
                        correlation.set(i, j, correlation.get(i, j) * 1.0 / Math.sqrt(variances[i] * variances[j]));
                    }
                }
            } // j for
        } // i for

        return correlation;
    }

    public Matrix covariance() {
        double sampleWeight = weightSum();
        if (sampleWeight <= 0.0) {
            throw new IllegalArgumentException(unsufficient_sample_weight);
        }

        double sampleNumber = samples();
        if (sampleNumber <= 1.0) {
            throw new IllegalArgumentException(unsufficient_sample_number);
        }

        double[] m = null;// mean();

        double inv = 1.0 / sampleWeight;

        // TODO: correct?
        Matrix result = quadraticSum_.operatorMultiply(inv, quadraticSum_);
        result = result.operatorMinus(result, result.outerProduct(new Array(m), new Array(m)));

        result = result.operatorMultiply(result, sampleNumber / (sampleNumber - 1.0));
        return result;
    }

    public void reset(int dimension) {
        if (dimension == 0) // if no size given,
            dimension = dimension_; // keep the current one
        if (dimension <= 0) {
            throw new IllegalArgumentException(null_dimension);
        }
        if (dimension == dimension_) {
            for (int i = 0; i < dimension_; i++)
                stats_[i].reset();
        } else {
            dimension_ = dimension;
            stats_ = new IStatistics[dimension];
            results_ = new double[dimension];
        }
        quadraticSum_ = new Matrix(dimension_, dimension_, 0.0);
    }

    public double weightSum() {
        return (stats_.length == 0) ? 0.0 : stats_[0].weightSum();
    }

    public int samples() {
        return (stats_.length == 0) ? 0 : stats_[0].samples();
    }

}
