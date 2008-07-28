/*
 Copyright (C) 2008 Srinivas Hasti
 Copyright (C) 2008 Tim Swetonic

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
package org.jquantlib.methods.lattices;

import java.math.BigInteger;

import org.jquantlib.processes.StochasticProcess1D;

/**
 * @author Srinivas Hasti
 * @author Tim Swetonic
 *
 */
public class LeisenReimer extends BinomialTree<LeisenReimer> {

    protected double up_, down_, pu_, pd_;

    public LeisenReimer(final StochasticProcess1D process,
                     /*Time*/ double end,
                     /*Size*/ int steps,
                     /*Real*/ double strike) {
        if(strike <= 0.0)
            throw new IllegalArgumentException("strike must be positive");

        int oddSteps = (steps%2 > 0 ? steps : steps+1);
        double variance = process.variance(0.0, x0_, end);
        double ermqdt = Math.exp(driftPerStep_ + 0.5*variance/oddSteps);
        double d2 = (Math.log(x0_/strike) + driftPerStep_*oddSteps ) /
                                                          Math.sqrt(variance);
        pu_ = PeizerPrattMethod2Inversion(d2, oddSteps);
        pd_ = 1.0 - pu_;
        double pdash = PeizerPrattMethod2Inversion(d2+Math.sqrt(variance),
                                                 oddSteps);
        up_ = ermqdt * pdash / pu_;
        down_ = (ermqdt - pu_ * up_) / (1.0 - pu_);
    }
    
    public double underlying(int i, int index) {
        double d = BigInteger.valueOf((long)i)
            .subtract(BigInteger.valueOf((long)index))
                .doubleValue();
        
        return x0_ * Math.pow(down_, d) * Math.pow(up_, (index));
    }
        
    public double probability(int n, int m, int branch) {
        return (branch == 1 ? pu_ : pd_);
    }

    /*! Given an odd integer n and a real number z it returns p such that:
    1 - CumulativeBinomialDistribution((n-1)/2, n, p) =
                           CumulativeNormalDistribution(z)

    \pre n must be odd
     */
    private double PeizerPrattMethod2Inversion(double z, int n) {

        if(n%2 !=1)
            throw new IllegalArgumentException("n must be an odd number: " + n + " not allowed");

        double result = (z/(n+1.0/3.0+0.1/(n+1.0)));
        result *= result;
        result = Math.exp(-result*(n+1.0/6.0));
        result = 0.5 + (z>0 ? 1 : -1) * Math.sqrt((0.25 * (1.0-result)));
        return result;
    }

}
