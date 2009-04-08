/*
 Copyright (C) 2008 Praneet Tiwari

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

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.math.ComposedFunction;
import org.jquantlib.math.UnaryFunction;
import org.jquantlib.math.UnaryFunctionDouble;
import org.jquantlib.util.Pair;
 

/**
 *
 * @author Praneet Tiwari
 */

//! Statistics tool
    /*! This class accumulates a set of data and returns their
statistics (e.g: mean, variance, skewness, kurtosis,
error estimation, percentile, etc.) based on the empirical
distribution (no gaussian assumption)

It doesn't suffer the numerical instability problem of
IncrementalStatistics. The downside is that it stores all
samples, thus increasing the memory requirements.
 */

//FIXME: changed to extending base class rather then implementing interface
public class GeneralStatistics /*extends Statistics*/ {
    
    private final static String empty_sample_set =  "empty sample set";
    private final static String unsufficient_sample_size = "sample number <=1, unsufficient";

    private ArrayList<Pair<Double, Double>> samples;
    private boolean sorted = false;

    public GeneralStatistics() {
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
        reset();
    }

    private void reset() {
        samples = new ArrayList<Pair<Double, Double>>();
        sorted = true;
    }

    private void sort() {
        if (!sorted) {
            samples = new PairSortingAlgorithms().insertionSort(samples);
        }
        sorted = true;
    }

    //! \name Inspectors
    //@{
    //! number of samples collected
    private int getSampleSize() {
        return samples.size();
    }

    //! collected data
    public ArrayList<Pair<Double, Double>> data() {
        return samples;
    }

    
    //reviewed!
    //! sum of data weights
    public double weightSum() {
        double result = 0.0;
        for (Pair<Double, Double> element : samples) {
            // casting required, re-visit
            result += element.getSecond();
        }
        return result;
    }

    //reviewed/ refactored!
    /*! returns the mean, defined as
    \f[ \langle x \rangle = \frac{\sum w_i x_i}{\sum w_i}. \f]
     */
    public double mean() {
        int size = getSampleSize();
        if (size == 0) {
            throw new IllegalArgumentException(empty_sample_set);
        }
        
        //some boilerplate code ...
        UnaryFunction<Double, Boolean> everyWhere = new UnaryFunction<Double, Boolean>(){
            @Override
            public Boolean evaluate(Double x) {
                return true;
            }
        };
        
        UnaryFunctionDouble identity = new UnaryFunctionDouble(){
            @Override
            public double evaluate(double x) {
                return x;
            }
        };
        return (Double) expectationValue(identity,everyWhere).getFirst();
    }

    //reviewed/refactored
    /*! Expectation value of a function \f$ f \f$ on a given
    range \f$ \mathcal{R} \f$, i.e.,
    \f[ \mathrm{E}\left[f \;|\; \mathcal{R}\right] =
        \frac{\sum_{x_i \in \mathcal{R}} f(x_i) w_i}{
              \sum_{x_i \in \mathcal{R}} w_i}. \f]
    The range is passed as a boolean function returning
    <tt>true</tt> if the argument belongs to the range
    or <tt>false</tt> otherwise.

    The function returns a pair made of the result and
    the number of observations in the given range.
    */
    public Pair<Double, Integer> expectationValue(UnaryFunctionDouble f, UnaryFunction<Double, Boolean> inRange) {
        double num = 0.0;
        double den = 0.0;
        int N = 0;
        for (Pair<Double, Double> element : samples) {
            double x = (Double) element.getFirst();
            double w = (Double) element.getSecond();
            if (inRange.evaluate(x)) {
                num += x * w;
                den += w;
                N++;
            }
        }
        if (N == 0) {
            return new Pair<Double, Integer>(0.0, 0);
        } else {
            return new Pair<Double, Integer>(0.0, 0);
        }
    }
    
    /* Refactored!!!
    public Pair<Number, Number> expectationValue(String functype) {
        Double num = 0.0, den = 0.0;
        int n = 0;
        if (functype.equals("identity")) {
            for (Pair<Double, Double> element : samples) {
                Double x = (Double) element.getFirst();
                Double w = (Double) element.getSecond();
                num += x * w;
                den += w;
                n++;
            }
        } else if (functype.equals("square")) {
            // left for future implementation
        }
        if (n == 0) {
            return new Pair<Number, Number>();
        } else {
            return new Pair<Number, Number>(num / den, n);
        }

    }
    */
    
    public double variance() {
        int N = getSampleSize();
        if (N < 1) {
            throw new IllegalArgumentException(unsufficient_sample_size);
        }
        
        UnaryFunction<Double, Double> square = new UnaryFunction<Double, Double>(){
            @Override
            public Double evaluate(Double x) {
                return x*x;
            }
        };
        
        UnaryFunction<Double, Boolean> everyWhere = new UnaryFunction<Double, Boolean>(){
            @Override
            public Boolean evaluate(Double x) {
                return true;
            }
        };
        
        //argh no generic arrays :-(
        List<UnaryFunction<Double, Double>> functions = new ArrayList<UnaryFunction<Double,Double>>();
        functions.add(square);
        //functions.add(e)
        
        //TODO: implement std::bind2nd 
        
        ComposedFunction<Double> comp = new ComposedFunction<Double>(functions);
        
        //UnaryFunction<Double, Double> = new ComposedFunction<Double>(new UnaryFunction<ParameterType, ReturnType>);
        
        //double s2 = expectationValue(square, everyWhere).getFirst();
        
        return 0;
    }
    
    
    
    /*! returns the variance, defined as
    \f[ \sigma^2 = \frac{N}{N-1} \left\langle \left(
    x-\langle x \rangle \right)^2 \right\rangle. \f]
     */
    /*
    public double variance() {
        int N = getSampleSize();
        if (N <= 1) {
            throw new IllegalArgumentException("Sample size cannot be less than 1");
        }
        double mean = mean();
        //We don't have the benefit (read headache) of operator overloading

        double runningTotal = 0.0;
        for (Pair<Double, Double> element : samples) {
            Double x = (Double) element.getFirst();
            runningTotal += Math.pow((x - mean), 2);
        }
        return runningTotal / (n - 1);
    }
    */
    /*! returns the standard deviation \f$ \sigma \f$, defined as the
    square root of the variance.
     */
    public Double standardDeviation() {
        return Math.sqrt(variance());
    }

    /*! returns the error estimate on the mean value, defined as
    \f$ \epsilon = \sigma/\sqrt{N}. \f$
     */
    public Double errorEstimate() {
        return Math.sqrt((variance()) / getSampleSize());
    }

    /*! returns the skewness, defined as
    \f[ \frac{N^2}{(N-1)(N-2)} \frac{\left\langle \left(
    x-\langle x \rangle \right)^3 \right\rangle}{\sigma^3}. \f]
    The above evaluates to 0 for a Gaussian distribution.
     */
    public Double skewness() {
        int n = getSampleSize();
        if (n <= 2) {
            throw new IllegalArgumentException("Sample size cannot be less than 2");
        }
        double mean = mean();
        double runningTotal = 0.0;
        for (Pair<Double, Double> element : samples) {
            Double x = (Double) element.getFirst();
            runningTotal += Math.pow((x - mean), 3);
        }
        double sigma = standardDeviation();

        return (n / ((n - 1) * (n - 2))) * runningTotal / (Math.pow(sigma, 3));
    }

    /*! returns the excess kurtosis, defined as
    \f[ \frac{N^2(N+1)}{(N-1)(N-2)(N-3)}
    \frac{\left\langle \left(x-\langle x \rangle \right)^4
    \right\rangle}{\sigma^4} - \frac{3(N-1)^2}{(N-2)(N-3)}. \f]
    The above evaluates to 0 for a Gaussian distribution.
     */
    public Double kurtosis() {
        int n = getSampleSize();
        if (n <= 3) {
            throw new IllegalArgumentException("Sample size cannot be less than 3");
        }
        double mean = mean();
        double runningTotal = 0.0;
        for (Pair<Double, Double> element : samples) {
            Double x = (Double) element.getFirst();
            runningTotal += Math.pow((x - mean), 4);
        }
        double sigma = standardDeviation();
        double unadjustedKurtosis = (((n) * (n + 1)) * (runningTotal)) / ((n - 1) * (n - 2) * (n - 3) * (Math.pow(sigma, 4)));
        return unadjustedKurtosis - (3 * (Math.pow(n - 1, 2)) / ((n - 1) * (n - 2)));
    }

    /*! returns the minimum sample value */
    public Double min() {

        /*
         * return std::min_element(samples_.begin(),
        samples_.end())->first;
         */
        if (getSampleSize() <= 1) {
            //throw some exception
            }
        return MathUtil.min(samples);
    }

    /*! returns the maximum sample value */
    public Double max() {

        if (getSampleSize() <= 1) {
            throw new IllegalArgumentException("Sample size needs to be more tha 1");
        }
        return MathUtil.max(samples);
    }

    /*! \f$ y \f$-th percentile, defined as the value \f$ \bar{x} \f$
    such that
    \f[ y = \frac{\sum_{x_i < \bar{x}} w_i}{
    \sum_i w_i} \f]

    \pre \f$ y \f$ must be in the range \f$ (0-1]. \f$
     */
    public Double percentile(Double percent) {

        if (percent < 0.0 || percent > 1.0) {
            throw new IllegalArgumentException("percentile (" + percent + ") must be in (0.0, 1.0]");
        }

        double wt = weightSum();

        if (wt < 0.0) {
            throw new IllegalArgumentException("empty sample set");
        }
        sort();
        double target = percent * wt;
        double integral = 0.0;


        int k = 0;
        while (integral < target && k < samples.size()) {
            Pair<Double, Double> element = samples.get(k);
            integral += element.getSecond().doubleValue();
            k++;
        }

        return samples.get(k).getFirst().doubleValue();
    }

    /*! \f$ y \f$-th top percentile, defined as the value
    \f$ \bar{x} \f$ such that
    \f[ y = \frac{\sum_{x_i > \bar{x}} w_i}{
    \sum_i w_i} \f]

    \pre \f$ y \f$ must be in the range \f$ (0-1]. \f$
     */
    public Double topPercentile(Double percent) {
        if (percent < 0.0 || percent > 1.0) {
            throw new IllegalArgumentException("percentile (" + percent + ") must be in (0.0, 1.0]");
        }

        double wt = weightSum();

        if (wt < 0.0) {
            throw new IllegalArgumentException("empty sample set");
        }
        sort();
        //there is no reverse iterator, do a manual copy instead
        ArrayList<Pair<Double, Double>> samplesReverse = new ArrayList<Pair<Double, Double>>(samples.size());
        //dropping this approach for now approaching this another way

        double target = percent * wt;
        double integral = wt;


        int k = 0;
        while (integral < target && k < samples.size()) {
            Pair<Double, Double> element = samples.get(k);
            integral -= element.getSecond().doubleValue();
            k++;
        }
        return samples.get(k).getFirst().doubleValue();

    }

    //! \name Modifiers
    //@{
    //! adds a datum to the set, possibly with a weight
    public void add(Double value, Double weight/* = 1.0*/) {
        if (weight == null) {
            weight = 1.0;
        }
        samples.add(new Pair<Double, Double>(value, weight));
    }

    /**commented for now
    //! adds a sequence of data to the set, with default weight
    template <class DataIterator>
    void addSequence(DataIterator begin, DataIterator end) {
    for (;begin!=end;++begin)
    add(*begin);
    }

    //! adds a sequence of data to the set, each with its weight
    public     template <class DataIterator, class WeightIterator>
    void addSequence(DataIterator begin, DataIterator end,
    WeightIterator wbegin) {
    for (;begin!=end;++begin,++wbegin)
    add(*begin, *wbegin);
    }
     * **/
    //! resets the data to a null set
    /********PRELIMINARY TEST CASE;
     * TO BE MOVED TO THE TEST PACKAGE EVENTUALLY
     * TEMPRORARY FIX
     */

    //use   rand() function to generate a sample
    public static void main(String args[]) {
        GeneralStatistics gs = new GeneralStatistics();
        System.out.println("******************************************************************************");
        System.out.println("A Small test case");
        double checkF[] = new double[100];
        double f, g;
        double checkG[] = new double[100];
        for (int i = 0; i < 100; i++) {
            f = Math.random();
            //g= Math.random();
            g = 1.0;
            gs.add(f, g);
            checkF[i] = f;
            checkG[i] = g;

        }
        //generate csv values so that they can be copied to a spreadsheet and verified against standard results
        for (int i = 0; i < 100; i++) {
            System.out.println(checkF[i] + "");
        }
        System.out.println("");
        for (int i = 0; i < 100; i++) {
            System.out.print(checkG[i] + ",");
        }
        System.out.println("Statistics output");
        System.out.println("Mean is " + gs.mean());
        System.out.println("Max is " + gs.max());
        System.out.println("Min is " + gs.min());
        System.out.println("Skewness is " + gs.skewness());
        System.out.println("Kurtosis is" + gs.kurtosis());
        System.out.println("Variance is " + gs.variance());
        System.out.println("Std. dev is " + gs.standardDeviation());
        System.out.println("Percentile 95 is " + gs.percentile(.95));
    }
}




/*
 * sample of 100
 *
 * A Small test case
0.6337600892751551
0.9018114429736623
0.984921007931251
0.46728665425061766
0.4501429064516096
0.7278408885949321
0.8227894874667164
0.9243115571526305
0.5611450653907324
0.21832488532439942
0.45912684976425056
0.4349924823152037
0.899488551465951
0.36444947397619654
0.38848192309639307
0.8673296661404367
0.05146086994368748
0.11065216420850388
0.6437789331483978
0.7797780342516598
0.25699150106954605
0.49584254059869126
0.586397278375895
0.9212615394765336
0.3850367021753295
0.9915573263039582
0.5272268403632419
0.28098139233132036
0.11049965556962327
0.9314900054437703
0.860491942232953
0.6669312089805156
0.09410785916711129
0.8223424128452743
0.9204576948761042
0.7656635376658184
0.6981153601325711
0.6277032201049692
0.9557023603512926
0.5157363246664272
0.6583924501485331
0.8246672466379137
0.4564600341540479
0.4133997424868534
0.17741098125492172
0.14716568217291914
0.5676080024803717
0.061467196162620974
0.10282864438612915
0.4504866281764681
0.47319365488210496
0.8709466704274857
0.212820781077245
0.05822218750826136
0.4479659178672535
0.3298919820618864
0.14200392995445132
0.285886588133888
0.7371966853025812
0.6650657973728659
0.15074022597780778
0.5747075807941711
0.5481480757834375
0.03938916317303953
0.7658074322815822
0.8146009299818193
0.21486112821439052
0.6517853635978371
0.30186425703833086
0.9870961909795894
0.03251290046101396
0.23590867778903657
0.4108366729125459
0.21698894259953416
0.3484080324896892
0.4903810450762288
0.10029529374025004
0.6857533697750933
0.24374205584993403
0.915542951800352
0.112089988746501
0.6302560158204182
0.5435937229112758
0.38035747272289133
0.1589527890564585
0.8270702868677258
0.9521737926532137
0.4979889409983592
0.27947573142076354
0.5456980462240786
0.8261782089351266
0.5497112866421887
0.490815144549838
0.9432634074673
0.4711641589210217
0.2641826642666173
0.9936698172748331
0.26875109045155343
0.8741869712135317
0.2327573549551024

 * program generated o/p
Mean is 0.5176119762091662
Max is 0.9936698172748331
Min is 0.0
Skewness is 0.0
Kurtosis is-1.1098736644231717
Variance is 0.08181597241455381
Std. dev is 0.28603491467748093
Percentile 95 is 0.2641826642666173

 *
 * open office o/p
Mean 	0.52
Variance	0.08
Std. Dev	0.29
skewness	0.03
kurtosis	-1.17
max	0.99
min	0.03
 */
