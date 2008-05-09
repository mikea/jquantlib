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
	/*
	@Test
	public void testGammaDisribution() {
		double[][] values =	{	{1, -0.0388257395},
								{2, -0.0922078291},
								{5,   -0.1207822376},
								{6, -0.0103670060} };
		
		double a = 1.0; // alpha
		
		GammaDistribution gammDistribution = new GammaDistribution(a);
		for(int i=0;i<values.length;i++){
			double x = values[i][0];
			double expected = values[i][1];
			double realised = gammDistribution.GammaDistribution(i);
			double tolerance = 1.0e-10;
			if (Math.abs(expected-realised)>tolerance){
				TestCase.fail("x: " + x + " expected: " + expected + " realised: " + realised);
			}
		}
	}
	*/

}
