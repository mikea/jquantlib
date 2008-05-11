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

import org.jquantlib.math.distributions.GammaDistribution;
import org.junit.Test;
// import org.junit.Ignore; --> for excluding from tests

/**
 * @author Dominik Holenstein
 **/

public class GammaDistributionTest {
	
	@Test
	public void testGammaDisribution() {
		double[][] testvalues = {	{1.0, 0.6321205487807914},
									{2.0, 0.1353352832366127},
									{3.0, 0.04978706836786395},
									{4.0, 0.018315638888734182},
									{5.0, 0.006737946999085468},
									{6.0, 0.002478752176666357},
									{7.0, 9.118819655545164E-4},
									{8.0, 3.354626279025119E-4},
									{9.0, 1.2340980408667962E-4},
									{10.0, 4.539992976248486E-5},
									{11.0, 1.6701700790245663E-5},
									{12.0, 6.144212353328207E-6},
									{13.0, 2.2603294069810534E-6},
									{14.0, 8.315287191035681E-7},
									{15.0, 3.0590232050182594E-7}};

		double a = 1.0; // alpha
		
		GammaDistribution gammDistribution = new GammaDistribution(a);
		for (int i=0;i<testvalues.length;i++) {
			double expected = testvalues[i][1];
			double x = testvalues[i][0];
			double realised = gammDistribution.evaluate(x);
			System.out.println(realised);
			double tolerance = 1.0e-10;
			if (Math.abs(expected-realised)>tolerance) {
				fail("x: " + x + " expected: " + expected + " realised: " + realised);
			}
		}
	}
	
}

