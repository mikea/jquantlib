/*
 Copyright (C) 2007 Dominik Holenstein

 This source code is release under the BSD License.
 
 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the JQuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquant-devel@lists.sourceforge.net>. The license is also available online at
 <http://www.jquantlib.org/index.php/LICENSE.TXT>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */

package org.jquantlib.testsuite.math.distributions;

// import static org.junit.Assert.assertEquals; --> not JUnit 4.4 conform

import static org.junit.Assert.fail;

import org.jquantlib.math.distributions.InverseCumulativeNormal;
import org.junit.Test;

/**
 * @author Dominik Holenstein
 */

public class InverseCumulativeNormalTest {
	
	public InverseCumulativeNormalTest() {
		System.out.println("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
	@Test
	public void testInverseCumulativNormal() {
		
		// Test values have been compared with values produced by QuantLibXL in Excel.
		// The error is between 0 and 7.99361E-15 (absolute value).
		// This is the best result compared to Excel, Gnumeric and Quantrix 
		// TODO Fix the error compared to QuantLib
		double[][] testvalues = {   {0.01,-2.326347874388028},
									{0.1, -1.2815515641401563},
									{0.2, -0.8416212327266185},
									{0.3, -0.5244005132792953},
									{0.4, -0.2533471028599986},
									{0.5, 0.0},
									{0.6, 0.2533471028599986},
									{0.7, 0.5244005132792952},
									{0.8, 0.8416212327266186},
									{0.9, 1.2815515641401563},
									{0.99, 2.326347874388028}};
									
									
		InverseCumulativeNormal icn = new InverseCumulativeNormal();
		
		for(int i=0;i<testvalues.length;i++){
			double z = testvalues[i][0];
			double expected = testvalues[i][1];
			double computed = icn.evaluate(z);
			double tolerance = (Math.abs(z)<3.01) ? 1.0e-15: 1.0e-10;
			
			// assertEquals(expected, computed,tolerance); --> not JUnit 4.4 conform
			if (Math.abs(expected-computed)>tolerance) {
				fail("z: " + z + " expected: " + expected + " computed: " + computed);
			}
			
			//assertEquals(0.0, computed + icn.evaluate(z)*(-1.0), tolerance); --> not JUnit 4.4 conform
			double realized = computed + icn.evaluate(z)*(-1.0);
			if (Math.abs(realized) > tolerance) {
				fail("z: " + z + " expected: " + 0.0 + " realized: " + realized);
			}
		}
	}
	
	@Test 
	public void testExtremes(){
		double z = -40;
		double tolerance = 1.0e-15;
		
		InverseCumulativeNormal icn = new InverseCumulativeNormal();
		
		// assertEquals(0, icn.evaluate(z),tolerance); --> not JUnit 4.4 conform
		if (Math.abs(icn.evaluate(z)) > tolerance) {
			fail("z: " + z + " expected: " + 0.0 + " realized: " + icn.evaluate(z));
		}
		
		z = -10;
		// assertEquals(0, icn.evaluate(z),tolerance); --> not JUnit 4.4 conform
		if (Math.abs(icn.evaluate(z)) > tolerance) {
			fail("z: " + z + " expected: " + 0.0 + " realized: " + icn.evaluate(z));
		}
		
		z = 10;
		//assertEquals(1.0, icn.evaluate(z),tolerance); --> not JUnit 4.4 conform
		if (Math.abs(icn.evaluate(z)) > (tolerance + 1.0)) {
			fail("z: " + z + " expected: " + 1.0 + " realized: " + icn.evaluate(z));
		}
		
		z = 40;
		// assertEquals(1.0, icn.evaluate(z),tolerance); --> not JUnit 4.4 conform
		if (Math.abs(icn.evaluate(z)) > (tolerance + 1.0)) {
			fail("z: " + z + " expected: " + 1.0 + " realized: " + icn.evaluate(z));
		}		
	}
}
