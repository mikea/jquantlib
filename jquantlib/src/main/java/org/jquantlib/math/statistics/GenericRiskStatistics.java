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

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.math.E_BinaryFunction;
import org.jquantlib.math.E_ClippedFunction;
import org.jquantlib.math.E_ComposedFunction;
import org.jquantlib.math.E_IUnaryFunction;
import org.jquantlib.math.E_UnaryFunction;
import org.jquantlib.util.Pair;
import org.jquantlib.util.stdlibc.Std;

public class GenericRiskStatistics /*mimic inheritence using delgate*/ {
    
    private static final String no_data_below_the_target = "no data below the target";
    private static final String empty_sample_set = "empty sample set";
    private static final String unsufficient_samples_under_target = "samples under target <=1, unsufficient";
    
    IStatistics statistics = null;
    
    public GenericRiskStatistics(IStatistics statistics){
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
        this.statistics = statistics;
    }

    /*! returns the variance of observations below the mean,
        \f[ \frac{N}{N-1}
            \mathrm{E}\left[ (x-\langle x \rangle)^2 \;|\;
                              x < \langle x \rangle \right]. \f]

        See Markowitz (1959).
    */
    public double semiVariance(){
       return regret(statistics.mean());
    }

    /*! returns the semi deviation, defined as the
        square root of the semi variance.
    */
    public double semiDeviation() {
        return Math.sqrt(semiVariance());
    }

    /*! returns the variance of observations below 0.0,
        \f[ \frac{N}{N-1}
            \mathrm{E}\left[ x^2 \;|\; x < 0\right]. \f]
    */
    public double downsideVariance(){
        return regret(0.0);
    }

    /*! returns the downside deviation, defined as the
        square root of the downside variance.
    */
    public double downsideDeviation(){
        return Math.sqrt(downsideVariance());
    }

    /*! returns the variance of observations below target,
        \f[ \frac{N}{N-1}
            \mathrm{E}\left[ (x-t)^2 \;|\;
                              x < t \right]. \f]

        See Dembo and Freeman, "The Rules Of Risk", Wiley (2001).
    */
    public double regret(double target){
        // average over the range below the target
        
        E_UnaryFunction<Double, Double> square = new E_UnaryFunction<Double, Double>(){
            @Override
            public Double evaluate(Double x) {
                return x*x;
            }
        };
        
        List<E_IUnaryFunction<Double, Double>> functions = new ArrayList<E_IUnaryFunction<Double,Double>>();
        functions.add(square);
        
        E_BinaryFunction<Double, Double> minus = new E_BinaryFunction<Double, Double>(){
            @Override
            public Double evaluate(Double x_1, Double x_2) {
                return x_1 - x_2;
            }   
        };
        
        E_IUnaryFunction<Double, Double> bounded_minus = Std.getInstance().bind2nd(minus, new Double(target));
        functions.add(bounded_minus);
        
        E_ComposedFunction<Double> comp = new E_ComposedFunction<Double>(functions);
        E_BinaryFunction<Double, Boolean> less = new E_BinaryFunction<Double, Boolean>(){
            @Override
            public Boolean evaluate(Double x_1, Double x_2) {
               return x_1 < x_2;
            } 
        };
        
        E_IUnaryFunction<Double, Boolean> bounded_less = Std.getInstance().bind2nd(less, target);
        Pair<Double, Integer> result = statistics.expectationValue(comp, bounded_less);
        
        double x = result.getFirst();
        //argh.....
        int N = result.getSecond().intValue();
        
        if(N<2){
            throw new IllegalArgumentException(unsufficient_samples_under_target);
        }
        return (new Double(N)/(new Double(N)-1.0))*x;
    }

    //! potential upside (the reciprocal of VAR) at a given percentile
    public double potentialUpside(double centile){
        if(centile<0.9 || centile>=1.0){
            throw new IllegalArgumentException("percentile (" + centile + ") out of range [0.9, 1.0)");
        }
        // potential upside must be a gain, i.e., floored at 0.0
        return Math.max(statistics.percentile(centile), 0.0);
    }

    //! value-at-risk at a given percentile
    public double valueAtRisk(double centile){
        if(centile<0.9 || centile>=1.0){
            throw new IllegalArgumentException("percentile (" + centile + ") out of range [0.9, 1.0)");
        }
        return - Math.min(statistics.percentile(1.0-centile), 0.0);
    }

    //! expected shortfall at a given percentile
    /*! returns the expected loss in case that the loss exceeded
        a VaR threshold,

        \f[ \mathrm{E}\left[ x \;|\; x < \mathrm{VaR}(p) \right], \f]

        that is the average of observations below the
        given percentile \f$ p \f$.
        Also know as conditional value-at-risk.

        See Artzner, Delbaen, Eber and Heath,
        "Coherent measures of risk", Mathematical Finance 9 (1999)
    */
    public double expectedShortfall(double centile){
        //Require...
        if(centile<0.9 || centile>=1.0){
            throw new IllegalArgumentException("percentile (" + centile + ") out of range [0.9, 1.0)");
        }
        //Ensure...
        if(statistics.getSampleSize() == 0){
            //not sure whether to throw an exception
            //throw new IllegalArgumentException(empty_sample_set);
        }
        double target = -valueAtRisk(centile);
        
        E_UnaryFunction<Double, Double> identity = new E_UnaryFunction<Double, Double>(){
            @Override
            public Double evaluate(Double x) {
                return x;
            }
        };
        
        E_BinaryFunction<Double, Boolean> less = new E_BinaryFunction<Double, Boolean>(){
            @Override
            public Boolean evaluate(Double x_1, Double x_2) {
               return x_1 < x_2;
            } 
        };
        
        E_IUnaryFunction<Double, Boolean> bounded = Std.getInstance().bind2nd(less, target);
        
        Pair<Double, Integer> result = statistics.expectationValue(identity, bounded);
        
        double x = result.getFirst();
        Integer N = result.getSecond();
        
        if(N.intValue() ==  0.0){
            throw new IllegalArgumentException(no_data_below_the_target);
        }
        // must be a loss, i.e., capped at 0.0 and negated
        return -Math.min(x, 0.0);
        
    }

    /*! probability of missing the given target, defined as
        \f[ \mathrm{E}\left[ \Theta \;|\; (-\infty,\infty) \right] \f]
        where
        \f[ \Theta(x) = \left\{
            \begin{array}{ll}
            1 & x < t \\
            0 & x \geq t
            \end{array}
            \right. \f]
    */
    public double shortfall(double target){
        if(statistics.getSampleSize()==0){
            throw new IllegalArgumentException(empty_sample_set);
        }
        
        E_UnaryFunction<Double, Boolean> everyWhere = new E_UnaryFunction<Double, Boolean>(){
            @Override
            public Boolean evaluate(Double x) {
                return true;
            }
        };
        
        E_UnaryFunction<Double, Double> constant = new E_UnaryFunction<Double, Double>(){
            @Override
            public Double evaluate(Double x) {
                return 1.0;
            }
        };
        E_BinaryFunction<Double, Boolean> less = new E_BinaryFunction<Double, Boolean>(){
            @Override
            public Boolean evaluate(Double x_1, Double x_2) {
               return x_1 < x_2;
            } 
        };
        
        E_IUnaryFunction<Double, Boolean> bounded_less = Std.getInstance().bind2nd(less, new Double(target));
        E_ClippedFunction<Double> clipped = new E_ClippedFunction<Double>(bounded_less, constant);
        return statistics.expectationValue(clipped, everyWhere).getFirst();
    }

    /*! averaged shortfallness, defined as
        \f[ \mathrm{E}\left[ t-x \;|\; x<t \right] \f]
    */
    public double averageShortfall(double target) {
       //First let's create the minus function
        E_BinaryFunction<Double, Double> minus = new E_BinaryFunction<Double, Double>(){
            @Override
            public Double evaluate(Double x_1, Double x_2) {
                return x_1 - x_2;
            }   
        };
        // Bind the first parameter
        E_IUnaryFunction<Double, Double> bounded_minus = Std.getInstance().bind1st(minus, new Double(target));
        
        // now bind the second one
        E_BinaryFunction<Double, Boolean> less = new E_BinaryFunction<Double, Boolean>(){
            @Override
            public Boolean evaluate(Double x_1, Double x_2) {
               return x_1 < x_2;
            } 
        };
        // Bind the second parameter
        E_IUnaryFunction<Double, Boolean> bounded_less = Std.getInstance().bind1st(less, new Double(target));
        
        Pair<Double, Integer> result = statistics.expectationValue(bounded_minus, bounded_less);
        double x = result.getFirst();
        //mmhh somewhere we have to change N to int
        Integer N = result.getSecond();
        if(N.intValue()==0){
            throw new IllegalArgumentException(no_data_below_the_target);
        }
        return x;
    }
}
