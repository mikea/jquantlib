/*
 Copyright (C) 2008 Richard Gomes

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquantlib-dev@lists.sf.net>. The license is also available online at
 <http://jquantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the originating copyright notice follows below.
 */

/**
 * @author Richard Gomes
 * @author Dominik Holenstein
 */

package org.jquantlib.math.distributions;


import org.jquantlib.math.Beta;

/**
 * Cumulative binomial distribution function.<br>
 * Given an integer k it provides the cumulative probability of observing kk<=k.
 * 
 * @author Richard Gomes
 *
 */

public class CumulativeBinomialDistribution {
	static private double accuracy = 1e-16;
	static private int maxIteration = 100;
	
    private int n_;
    private double p_;
    
    /**
     * This constructor initializes p and n
     * @param p Probability of success of a single trial
     * @param n The total number of trials
     */
    public CumulativeBinomialDistribution(double p, int n){  
    	n_ = n; // total number of trials
    	p_ = p; // probability of success on a single trial
    	
    	if ((p<=0.0)) {
			throw new ArithmeticException("negative p not allowed");
		}
		if ((p>1.0)){
			throw new ArithmeticException("p>1.0 not allowed");
		}
    }

    /**
     * Computes the Cumulative Binomial Distribution.
     * @param k
     * @return 1.0 - Beta.incompleteBetaFunction(k+1, n_-k, p_, accuracy, maxIteration)
     */
	public double evaluate(int k){
		 if (k >= n_)return 1.0;
         else return 1.0 - Beta.incompleteBetaFunction(k+1, n_-k, p_, accuracy, maxIteration);
	}
		
	
	/**
	 * Given an odd integer and a real number z it returns p such that:<br>
	 * 1 - CumulativeBinomialDistribution((n-1/2, n, p) = CumulativeNormalDistribution(z)
	 * n must be odd.
	 * @param z
	 * @param n
	 * @return result
	 */
	static private double PeizerPrattMethod2Inversion(double z, int n) {
		if (n%2 != 0) throw new ArithmeticException("n must be an odd number: " + n + " not allowed");
		
		double result = (z/(n+1.0/3.0+0.1/(n+1.0)));
		result *= result;
		result = Math.exp(-result*(n+1.0/6.0));
		result = 0.5 + (z>0 ? 1 : -1) * Math.sqrt((0.25*(1.0-result)));
		
		return result;	
	}
}
