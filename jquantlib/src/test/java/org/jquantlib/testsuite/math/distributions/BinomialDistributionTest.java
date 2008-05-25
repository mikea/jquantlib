/*
 Copyright (C) 2008 Dominik Holenstein
 
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
 When applicable, th

*/

package org.jquantlib.testsuite.math.distributions;

import static org.junit.Assert.fail;

import org.jquantlib.math.distributions.BinomialDistribution;
import org.junit.Test;
// import org.junit.Ignore; --> for excluding from tests

/**
 * @author Dominik Holenstein
 **/

public class BinomialDistributionTest {
	
	@Test
	public void testBinomialDistribution() {
				
		// Expected values with n = 16 trials and p = 0.5
		double[]testvalues = {	1.52587890625E-5,
								2.44140625E-4,
								0.0018310546875,
								0.008544921875,
								0.02777099609375,
								0.066650390625,
								0.1221923828125,
								0.174560546875,
								0.19638061523438,
								0.174560546875,
								0.1221923828125,
								0.066650390625,
								0.02777099609375,
								0.008544921875,
								0.0018310546875,
								2.44140625E-4,
								}; 
		
		double p = 0.5; 		   // p = probability
		int n = testvalues.length; // n = number of trials
		
		BinomialDistribution binomdist = new BinomialDistribution(p,n);
		
		for(int i=0;i<n;i++){
			int z = i;
			double expected = testvalues[i];
			double computed = binomdist.evaluate(z);
			
			// double tolerance = (z<6 ) ? 1.0e-15: 1.0e-10;
			double tolerance = 1.0e-15; // try to to get 1.0e-15 accuracy whenever possible
			
			//assertEquals(expected, computed, tolerance);
			if(computed - expected > tolerance){
				fail("expected: " +  expected + " but was: " + computed);
			}
		}
	}
}
