/*
 Copyright (C) 2008 Richard Gomes

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

/**
 * @author Richard Gomes
 * @author Dominik Holenstein
 */

package org.jquantlib.math.distributions;


import org.jquantlib.math.Beta;
import org.jquantlib.math.UnaryFunctionInteger;

/**
 * Cumulative binomial distribution function.
 * <p>
 * Given an integer k it provides the cumulative probability of observing kk<=k.
 * 
 * @author Richard Gomes
 *
 */
// TODO: review comments and formulas
public class CumulativeBinomialDistribution implements UnaryFunctionInteger {
	
    //
    // static private final fields
    //
    
    static private final double accuracy = 1e-16;
	static private final int maxIteration = 100;
	
	//
	// private final fields
	//
	
	private final int n;
    private final double p;
    
    //
    // public constructors
    //
    
    /**
     * This constructor initializes p and n
     * @param p is the probability of success of a single trial
     * @param n is the total number of trials
     */
    public CumulativeBinomialDistribution(double p, int n){  
    	this.n = n; // total number of trials
    	this.p = p; // probability of success on a single trial
    	
    	if ((p <= 0.0)) {
    	    throw new ArithmeticException("negative p not allowed");
    	}
    	if ((p > 1.0)) {
    	    throw new ArithmeticException("p>1.0 not allowed");
    	}
    }

    //
    // implements UnaryFunctionInteger
    //
    
    /**
     * @InheritDoc
     * 
     * Computes the Cumulative Binomial Distribution.
     * 
     * @param k 
     * @return 1.0 - Beta.incompleteBetaFunction(k+1, n_-k, p_, accuracy, maxIteration)
     */
    @Override
	public double evaluate(int k) {
	    if (k >= n) {
		return 1.0;
	} else
	    return 1.0 - Beta.incompleteBetaFunction(k + 1, n - k, p, accuracy, maxIteration);
	}
		
	//
    // static methods
    //
    
	/**
	 * Given an odd integer and a real number z it returns p such that
	 * <p>
	 * 1 - CumulativeBinomialDistribution((n-1/2, n, p) = CumulativeNormalDistribution(z)
	 * n must be odd.
	 * <p>
	 * This method delivers a high level of accuracy.
	 * 
	 * @param z Input value for the standard normal distribution N(z)
	 * @param n Number of steps in the binomial tree
	 * @return result
	 */
    // TODO: code review :: visibility
	//static private double PeizerPrattMethod2Inversion(double z, int n) {
	//	if (n%2 != 0) throw new ArithmeticException("n must be an odd number: " + n + " not allowed");
	//	
	//	double result = (z/(n+1.0/3.0+0.1/(n+1.0)));
	//	result *= result;
	//	result = Math.exp(-result*(n+1.0/6.0));
	//	result = 0.5 + (z>0 ? 1 : -1) * Math.sqrt((0.25*(1.0-result)));
	//	
	//	return result;	
	//}
}
