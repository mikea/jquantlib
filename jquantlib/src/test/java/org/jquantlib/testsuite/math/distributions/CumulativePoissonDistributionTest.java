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

import static org.junit.Assert.fail;

import org.jquantlib.math.distributions.CumulativePoissonDistribution;
import org.junit.Test;


/**
 * 
 * @author Dominik Holenstein
 *
 */

public class CumulativePoissonDistributionTest {
	
	public CumulativePoissonDistributionTest() {
		System.out.println("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
	@Test
	public void testCumulativePoissonDistribution() {
		
		// Expected values with n = 15 trials and mean = 0.5
		double[]testvalues = {	0.60653065971263,
								0.90979598956895,
								0.98561232203303,
								0.99824837744371,
								0.99982788437004,
								0.99998583506268,
								0.9999989976204,
								0.99999993780309,
								0.99999999656451,
								0.99999999982903,
								0.99999999999226,
								0.99999999999968,
								0.99999999999999,
								1,
								1,
								}; 
		
		// Expected values with n = 15 trials and mean = 5.0
		double[]testvalues2 = {	0.00673794699909,
								0.040427681994512854,
								0.12465201948308113,
								0.26502591529736175,
								0.4404932850652127,
								0.61596065483306,
								0.76218346297294,
								0.86662832592999,
								0.93190636527815,
								0.9681719426938,
								0.98630473140162,
								0.99454690808699,
								0.99798114837256,
								0.99930201002086,
								0.9997737463333305,
								}; 
		
		double mu = 0.5; 		   // mu = mean
		int n = testvalues.length; // n = number of trials
		
		CumulativePoissonDistribution cumpoissondist = new CumulativePoissonDistribution(mu);
		
		for(int i=0;i<n;i++){
			int z = i;
			double expected = testvalues[i];
			double computed = cumpoissondist.evaluate(z);
			// double tolerance = (z<6 ) ? 1.0e-15: 1.0e-10;
			double tolerance = 1.0e-15; // try to to get 1.0e-15 accuracy whenever possible
			
			//assertEquals(expected, computed, tolerance);
			if(computed - expected > tolerance){
				fail("expected: " +  expected + " but was: " + computed);
			}
		}
		
		// test with mu (mean) = 5.0
		mu = 5.0;
		
		CumulativePoissonDistribution cumpoissondist2 = new CumulativePoissonDistribution(mu);
		
		for(int i=0;i<n;i++){
			int z = i;
			double expected = testvalues2[i];
			double computed = cumpoissondist2.evaluate(z);
			System.out.println(computed);
			// double tolerance = (z<6 ) ? 1.0e-15: 1.0e-10;
			double tolerance = 1.0e-15; // try to to get 1.0e-15 accuracy whenever possible
			
			//assertEquals(expected, computed, tolerance);
			if(computed - expected > tolerance){
				fail("expected: " +  expected + " but was: " + computed);
			}
		}
	}
	
}
