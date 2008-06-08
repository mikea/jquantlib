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


/**
 * Cumulative Poisson distribution function.<br/>
 * This function provides an approximation of the integral of the Poisson
 * distribution.<br/>
 *
 * In probability theory and statistics, the Poisson distribution is a discrete
 * probability distribution that expresses the probability of a number of events
 * occurring in a fixed period of time if these events occur with a known
 * average rate and independently of the time since the last event. The Poisson
 * distribution can also be used for the number of events in other specified
 * intervals such as distance, area or volume.
 *
 * @see Wikipedia: <a href="http://en.wikipedia.org/wiki/Poisson_distribution">Poisson Distribution on Wikipedia</a>
 *
 * @see Book: <i>"Numerical Recipes in C", 2nd edition, Teukolsky, Vetterling, Flannery, chapter 6.</i>
 *
 * @author Dominik Holenstein
 */

//TODO Test the correctness of the returned value against known good results.
//TODO CumulativePoissonDistribution: Write a test case.
public class CumulativePoissonDistribution {

    	private double mu_;
	private final double accuracy = 1.0e-15;
	private final int maxIteration = 100;

	public CumulativePoissonDistribution(double mu) {
	    mu_ = mu;
	}

	/**
	 * Computes the cumulative poisson distribution by using the 
	 * incomplete gamma function.
	 * @param k is the number of occurrences of an event 
	 * @return 1.0 - incmplgamma.incompleteGammaFunction(k_+1, mu_, accuracy, maxIteration);
	 */
	// TODO Check double k_ = (double)k; -> is this cast a good idea?
	public  double evaluate (int k) /* @Read-only */ {
    	   if (k < 0) {
    	       throw new ArithmeticException("k must be >= 1, but is " + k);
    	   }

    	   // this cast is necessary because the incompleteGammaFunction requires double,double,double,int parameters.
    	   double k_ = (double)k;
    	   IncompleteGamma incmplgamma = new IncompleteGamma();
           return 1.0 - incmplgamma.incompleteGammaFunction(k_+1, mu_, accuracy, maxIteration);
	}
}
