
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

package org.jquantlib.testsuite.math.distributions;

import org.jquantlib.math.distributions.PoissonDistribution;
import static org.junit.Assert.fail;
import org.junit.Test;


/**
 * 
 * @author Dominik Holenstein
 *
 */
public class PoissonNormalTest {
	
	@Test
	public void testPoissonDistribution() {
		
		// Expected values with n = 15 trials and mean = 0.5
		double[]testvalues = {	0.6065306597126334,
								0.30326532985632,
								0.07581633246408,
								0.01263605541068,
								0.0015795069263349827,
								1.579506926334980E-4,
								1.316255771945820E-5,
								9.401826942470140E-7,
								5.876141839043840E-8,
								3.26452324391324E-9,
								1.63226162195662E-10,
								7.419371008893730E-12,
								3.09140458703905E-13,
								1.18900176424579E-14,
								4.246434872306390E-16,
								}; 
		
		double mu = 0.5; 		   // mu = mean
		int n = testvalues.length; // n = number of trials
		
		PoissonDistribution poissondist = new PoissonDistribution(mu);
		
		for(int i=0;i<n;i++){
			int z = i;
			double expected = testvalues[i];
			double computed = poissondist.evaluate(z);
			// double tolerance = (z<6 ) ? 1.0e-15: 1.0e-10;
			double tolerance = 1.0e-15; // try to to get 1.0e-15 accuracy whenever possible
			
			//assertEquals(expected, computed, tolerance);
			if(computed - expected > tolerance){
				fail("expected: " +  expected + " but was: " + computed);
			}
		}
	}
}
