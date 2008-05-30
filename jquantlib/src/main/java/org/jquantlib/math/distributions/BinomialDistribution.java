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
 * @author Richard Gomes
 * @author Dominik Holenstein
 * <p>
 *
 * @see
 * <strong>Binomial Distribution</strong><br>
 * (source: <a href="http://en.wikipedia.org/wiki/Binomial_distribution">Binomial Distribution on Wikipedia</a>)<br>
 * In probability theory and statistics, the binomial distribution <br>
 * is the discrete probability distribution of the number of successes <br>
 * in a sequence of n independent yes/no experiments, each of which <br>
 * yields success with probability p. Such a success/failure experiment <br>
 * is also called a Bernoulli experiment or Bernoulli trial. In fact, when <br>
 * n = 1, the binomial distribution is a Bernoulli distribution. The binomial <br>
 * distribution is the basis for the popular binomial test of statistical <br>
 * significance. A binomial distribution should not be confused with a <br>
 * bimodal distribution.<p>
 *
 */

public class BinomialDistribution {

	private static final Factorial factorial_ = new Factorial();
	
	/**
	 * @param n_ sequence of independent yes/no experiments
	 */
    private int n_;
    
    /**
     * @param logP_ Natural logarithm of p
     * @param logOneMinusP_ Natural logarithm of 1-p
     */
    private double logP_, logOneMinusP_;


    /**
     * <b>public BinomilalDistribution(final double p, final int n)</b> <br>
     * Constructor to initialize the private p and n and to ensure that 
     * p is within the range of [0..1]. <br>
     * If p = 0.0 then logMinusP_ is set to 0.0. <br>
     * If p = 1.0 then logP is set to 1.0. <p>
     * 
     * @param p	Probability
     * @param n Sequence of independent yes/no experiments
     */
	public BinomialDistribution(final double p, final int n) {
		n_=n;

		if (p == 0.0) {
			logOneMinusP_ = 0.0;
		} else if (p == 1.0) {
			logP_ = 0.0;
		} else {
			if (!(p > 0)){
				throw new ArithmeticException("negative p not allowed");
			}
			if (!(p < 1.0)){
				throw new ArithmeticException("p>1.0 not allowed");
			}
		
			logP_ = Math.log(p);
			logOneMinusP_ = Math.log(1.0 - p);

		}

	}
	
	// TODO Consider developing an UnaryFunctionSomething for int vars.
	public double evaluate(final int k) {

        if (k > n_) return 0.0;

        // p == 1.0
        if (logP_ == 0.0) {
            return (k == n_ ? 1.0 : 0.0);
        }
        // p==0.0
        if (logOneMinusP_ == 0.0){
            return (k == 0 ? 1.0 : 0.0);
        }
        return Math.exp(binomialCoefficientLn(n_, k) +
                            k * logP_ + (n_-k) * logOneMinusP_);
	}

	/**
	 * 
	 * @param n
	 * @param k
	 * @return Natural logartihm of the binomial coefficient
	 */
	private static double binomialCoefficientLn(final int n, final int k) {

		if (!(n >= 0)) {
			throw new ArithmeticException("n<0 not allowed, " + n);
		}
		if (!(k >= 0)) {
			throw new ArithmeticException("k<0 not allowed, " + k);
		}
		if (!(n >= k)){
			throw new ArithmeticException("n<k not allowed");
		}

        return factorial_.ln(n) - factorial_.ln(k) - factorial_.ln(n - k);
    }
	
	/**
	 * 
	 * @param n
	 * @param k
	 * @return Binomial Coefficient
	 */
	private static double binomialCoefficient(final int n, final int k) {
		return Math.floor(0.5 + Math.exp(binomialCoefficientLn(n, k)));
	}
}
