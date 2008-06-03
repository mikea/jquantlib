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

package org.jquantlib.math.distributions;

import org.jquantlib.math.Factorial;

/**
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Binomial_distribution">Binomial
 *      Distribution on Wikipedia</a>
 * 
 * @author Richard Gomes
 * @author Dominik Holenstein
 * 
 */

public class BinomialDistribution {

	private static final Factorial factorial = new Factorial();
    private int nExp;
    private double logP, logOneMinusP;

    /**
	 * Constructor of the Binomial Distribution taking two arguments for
	 * initialization.
	 * 
	 * @param p
	 *            Probability of success of each trial
	 * @param n
	 *            Sequence of independent yes/no experiments
	 */
	public BinomialDistribution(final double p, final int n) {
		nExp = n;

		if (p == 0.0) {
			logOneMinusP = 0.0;
		} else if (p == 1.0) {
			logP = 0.0;
		} else {
			if ((p < 0)) {
				throw new ArithmeticException("negative p not allowed");
			}
			if ((p > 1.0)) {
				throw new ArithmeticException("p > 1.0 not allowed");
			}
			logP = Math.log(p);
			logOneMinusP = Math.log(1.0 - p);
		}
	}
	
	// TODO Consider developing an UnaryFunctionSomething for integer variables.
	/**
	 * Computes the probability of <code>k</code> successful trials.
	 * 
	 * @param k
	 * @return Math.exp(binomialCoefficientLn(nExp, k) + k * logP + (nExp-k) *
	 *         logOneMinusP);
	 */
	public double evaluate(final int k) {

        if (k > nExp)
			return 0.0;

        // p == 1.0
        if (logP == 0.0) {
			return (k == nExp ? 1.0 : 0.0);
		}
        
        // p==0.0
        if (logOneMinusP == 0.0) {
			return (k == 0 ? 1.0 : 0.0);
		}
        
        return Math.exp(binomialCoefficientLn(nExp, k) + k * logP + (nExp - k)
				* logOneMinusP);
	}

	/**
	 * Computes the natural logarithm of the binoomial coefficient.
	 * 
	 * @param n
	 * @param k
	 * @return Natural logarithm of the binomial coefficient
	 */
	private static double binomialCoefficientLn(final int n, final int k) {

		if (!(n >= 0)) {
			throw new ArithmeticException("n < 0 not allowed, " + n);
		}
		if (!(k >= 0)) {
			throw new ArithmeticException("k < 0 not allowed, " + k);
		}
		if (!(n >= k)){
			throw new ArithmeticException("n < k not allowed");
		}

        return factorial.ln(n) - factorial.ln(k) - factorial.ln(n - k);
    }
	
	/**
	 * Computes the binomial coefficient.
	 * 
	 * @param n
	 * @param k
	 * @return Math.floor(0.5 + Math.exp(binomialCoefficientLn(n, k)))
	 */
	private static double binomialCoefficient(final int n, final int k) {
		return Math.floor(0.5 + Math.exp(binomialCoefficientLn(n, k)));
	}
}
