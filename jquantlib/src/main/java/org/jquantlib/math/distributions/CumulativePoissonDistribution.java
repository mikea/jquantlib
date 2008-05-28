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
 * 
 * @author Dominik Holenstein
 * <p>
 * 
 * <strong>Cumulative Poisson distribution function</strong><br>
 * 
    This function provides an approximation of the
    integral of the Poisson distribution.<p>

     For this implementation see
     "Numerical Recipes in C", 2nd edition,
     Press, Teukolsky, Vetterling, Flannery, chapter 6<p>

     Test the correctness of the returned value is tested by
     checking it against known good results
 *
 */

//TODO CumulativePoissonDistribution: Write a test case.
public class CumulativePoissonDistribution {
		
		private double mu_;
		
		private double accuracy = 1.0e-15;
		private int maxIteration = 100;
            
		public CumulativePoissonDistribution(double mu) {
			mu_ = mu;
		}
        
		// TODO Check double k_ = (double)k; -> is this cast a good idea?
       public  double evaluate (int k) /* @Read-only */ {
        	
        	// this cast is necessary because the incompleteGammaFunction requires double,double,double,int parameters.
        	double k_ = (double)k;
        	IncompleteGamma incmplgamma = new IncompleteGamma();
            return 1.0 - incmplgamma.incompleteGammaFunction(k_+1, mu_, accuracy, maxIteration);
        } 
}
