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

import junit.framework.TestCase;

import org.jquantlib.math.distributions.GammaDistribution;
import org.jquantlib.math.distributions.GammaFunction;
import org.junit.Test;
// import org.junit.Ignore; --> for excluding from tests

/**
 * @author Dominik Holenstein
 **/

public class GammaDistributionTest {
	
	@Test
	public void testGammaDisribution() {
		double[][] testvalues = {	{1.0, 0.398942280401433},
									{2.0, 0.241970724519143},
									{3.0, 0.053990966513188},
									{4.0, 0.004431848411938},
									{5.0, 1.338302258e-4},
									{6.0, 1.486719515e-6},
									{7.0, 0.398942280401433},
									{8.0, 0.241970724519143},
									{9.0, 0.053990966513188},
									{10.0, 0.004431848411938},
									{11.0, 1.338302258e-4},
									{12.0, 1.486719515e-6},
									{13.0, 1.486719515e-6},
									{14.0, 1.486719515e-6},
									{15.0, 1.486719515e-6}};

		
		
		double a = 1.0; // alpha
		// double x = 0.0; // 
		
		GammaDistribution gammDistribution = new GammaDistribution(a);
		for(int i=0;i<testvalues.length;i++){
		//	double expected = values[i][1];
			double x = testvalues[i][0];
		//	System.out.println(x);
			double realised = gammDistribution.evaluate(x);
			System.out.println(realised);
	//		double tolerance = 1.0e-10;
	//		if (Math.abs(expected-realised)>tolerance){
	//			TestCase.fail("x: " + x + " expected: " + expected + " realised: " + realised);
	//		}
		}
	}
	
}

