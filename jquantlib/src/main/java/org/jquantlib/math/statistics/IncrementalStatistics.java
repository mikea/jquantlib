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

public class IncrementalStatistics /*implements IStatistics*/ {
    
    private static final String unsufficient_sample_weight = "sampleWeight_=0, unsufficient";
    private static final String unsufficient_sample_number = "sample number <=1, unsufficient";
    private static final String unsufficient_sample_number_2 = "sample number <=2, unsufficient";
    private static final String unsufficient_sample_number_3 = "sample number <=3, unsufficient";
    private static final String negative_variance = "negative variance";
    private static final String empty_sample_set = "empty sample set";
    private static final String max_number_of_samples_reached = "maximum number of samples reached";
    
    protected int sampleNumber_, downsideSampleNumber_;
    protected double sampleWeight_, downsideSampleWeight_;
    protected double sum_, quadraticSum_, downsideQuadraticSum_,
        cubicSum_, fourthPowerSum_;
    protected double min_, max_;
    
    
    
    public IncrementalStatistics(){
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
        reset();
    }
    
    public void addSequence(List<Double> data, int beginData, List<Double> weight, int beginWeight, int lenght){
        for(int i = 0; i<lenght; i++){
            add(data.get(beginData+i), weight.get(beginWeight+i));
        }
    }
    
    public void addSequence(List<Double> data, int beginData, int lenght){
        for(int i=0; i<lenght; i++){
            add(data.get(beginData + i));
        }
    }
    
    
    public void add(double value, double weight) {
        if (weight < 0.0) {
            throw new IllegalArgumentException("negative weight (" + weight + ") not allowed");
        }

        int oldSamples = sampleNumber_;
        sampleNumber_++;
        if (sampleNumber_ <= oldSamples) {
            throw new IllegalArgumentException(max_number_of_samples_reached);
        }

        sampleWeight_ += weight;

        double temp = weight * value;
        sum_ += temp;
        temp *= value;
        quadraticSum_ += temp;
        if (value < 0.0) {
            downsideQuadraticSum_ += temp;
            downsideSampleNumber_++;
            downsideSampleWeight_ += weight;
        }
        temp *= value;
        cubicSum_ += temp;
        temp *= value;
        fourthPowerSum_ += temp;
        if (oldSamples == 0) {
            min_ = max_ = value;
        } else {
            min_ = Math.min(value, min_);
            max_ = Math.max(value, max_);
        }
    }
    
    public void add(double data){
        add(data, 1.0);
    }

    public double kurtosis() {
        if (sampleNumber_ <= 3) {
            throw new IllegalArgumentException(unsufficient_sample_number_3);
        }

        double m = mean();
        double v = variance();

        double c = (sampleNumber_ - 1.0) / (sampleNumber_ - 2.0);
        c *= (sampleNumber_ - 1.0) / (sampleNumber_ - 3.0);
        c *= 3.0;

        if (v == 0){
            return c;
        }
        double result = fourthPowerSum_ / sampleWeight_;
        result -= 4.0 * m * (cubicSum_ / sampleWeight_);
        result += 6.0 * m * m * (quadraticSum_ / sampleWeight_);
        result -= 3.0 * m * m * m * m;
        result /= v * v;
        result *= sampleNumber_ / (sampleNumber_ - 1.0);
        result *= sampleNumber_ / (sampleNumber_ - 2.0);
        result *= (sampleNumber_ + 1.0) / (sampleNumber_ - 3.0);
        return result-c;
    }


    public double max() {
       if(samples()<=0){
           throw new IllegalArgumentException(empty_sample_set);
       }
       return max_;
    }

    
    public double mean() {
       if(sampleWeight_<=0.0){
           throw new IllegalArgumentException(unsufficient_sample_weight);
       }
       return sum_/sampleWeight_;
    }

    
    public double min() {
        if(samples()<=0){
            throw new IllegalArgumentException(empty_sample_set);
        }
        return min_;
    }

    public void reset() {
        min_ = Double.MAX_VALUE;
        max_ = Double.MIN_VALUE;
        sampleNumber_ = 0;
        downsideSampleNumber_ = 0;
        sampleWeight_ = 0.0;
        downsideSampleWeight_ = 0.0;
        sum_ = 0.0;
        quadraticSum_ = 0.0;
        downsideQuadraticSum_ = 0.0;
        cubicSum_ = 0.0;
        fourthPowerSum_ = 0.0;
    }

    
    public int samples() {
        return sampleNumber_;
    }

    
    public double skewness() {
        if(sampleNumber_ <= 2){
            throw new IllegalArgumentException(unsufficient_sample_number_2);
        }
        double s = standardDeviation();
        
        if(s==0.0){
            return 0.0;
        }
        
        double m = mean();
        double result = cubicSum_/sampleWeight_;
        result -= 3.0*m*(quadraticSum_/sampleWeight_);
        result += 2.0*m*m*m;
        result /= s*s*s;
        result *= sampleNumber_/(sampleNumber_-1.0);
        result *= sampleNumber_/(sampleNumber_-2.0);
        return result;
    }

    
    public double standardDeviation() {
        return Math.sqrt(variance());
    }

    public double variance() {
        if(sampleWeight_<=0.0){
            throw new IllegalArgumentException(unsufficient_sample_weight);
        }
        if(sampleNumber_<=1){
            throw new IllegalArgumentException(unsufficient_sample_number);
        }
        double m = mean();
        double v = quadraticSum_/sampleWeight_;
        v -=m*m;
        v *= sampleNumber_ / (sampleNumber_ - 1.0);
        
        if(v<0.0){
            throw new IllegalArgumentException(negative_variance + v);
        }
        
        return v;
        
    }

    
    public double weightSum() {
        return sampleWeight_;
    }
    
    public double downsideVariance(){
        if(downsideSampleWeight_ == 0.0){
            if(sampleWeight_<= 0.0){
                throw new IllegalArgumentException(unsufficient_sample_weight);
            }
            return 0.0;
        }
        if(downsideSampleNumber_ <= 1){
            throw new IllegalArgumentException(unsufficient_sample_number);
        }
        
        return (downsideSampleNumber_/(downsideSampleNumber_-1.0))*
        (downsideQuadraticSum_ /downsideSampleWeight_);
    }
    
    public double downsideDeviation(){
        return Math.sqrt(downsideVariance());
    }
    
    public double errorEstimate(){
        double var = variance();
        if(samples() <= 0){
            throw new IllegalArgumentException(empty_sample_set);
        }
        return Math.sqrt(var/samples());
    }
    
    
    
    

}
