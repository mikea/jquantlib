/*
 Copyright (C) 2007 Richard Gomes

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

package org.jquantlib.testsuite.math;

import static org.junit.Assert.assertEquals;

import org.jquantlib.math.ErrorFunction;
import org.junit.Test;

/**
 * @author <Richard Gomes>
 */
public class ErrorFunctionTest {

	public ErrorFunctionTest() {
		System.out.println("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
	@Test
	public void testRegressionExtremeValuesForCoverage(){
		
		double[][] testvalues = { 	{-2*Double.MIN_NORMAL, 0.0},
									{3.0e-9,3.3851375e-9},
									{5.0, 0.999999999998463},
									{6.0, 1.0} };

		ErrorFunction erf = new ErrorFunction();

		for(int i=0;i<testvalues.length;i++){
			double z = testvalues[i][0];
			double expected = testvalues[i][1];
			double computed = erf.evaluate(z);
			double tolerance = 1.0e-15;
			assertEquals(expected, computed,tolerance);			
			assertEquals(expected, -erf.evaluate(-z),tolerance);			
		}

	}
	
	@Test
	public void testKnownValuesAbramStegun(){
		double[][] testvalues = { 	{0.0, 0.0},
									{0.5, 0.5204998778},
									{1.0, 0.8427007929},
									{2.0, 0.9953222650}};

		ErrorFunction erf = new ErrorFunction();
		for(int i=0;i<testvalues.length;i++){
			double z = testvalues[i][0];
			double expected = testvalues[i][1];
			double computed = erf.evaluate(z);
			double tolerance = 1.0e-10;
			assertEquals(expected, computed,tolerance);			
		}

	}
}
