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

import static org.junit.Assert.fail;

import org.apache.log4j.Logger;
import org.jquantlib.math.Factorial;
import org.jquantlib.testsuite.lang.TypeTokenTest;
import org.junit.Test;

/**
 * @author <Richard Gomes>
 */
public class FactorialTest {

    private final static Logger logger = Logger.getLogger(FactorialTest.class);

	public FactorialTest() {
		logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}

	@Test
	public void testCompareToDirect() {
		
		Factorial factorial = new Factorial();
		int n = 4;
		double expected = factorial(n);
		double realised = factorial.get(n);
		if (Math.abs(expected-realised)>1.0e-15)
			fail("n: " + n + " Expected: " + expected + " realised: " + realised);
		
		n = 30;
		expected = factorial(n);
		realised = factorial.get(n);
		if (Math.abs((expected-realised)/expected)>1.0e-10)
			fail("n: " + n + " Expected: " + expected + " realised: " + realised);

	}
	
	private double factorial(int n){
		double x = 1.0;
		for(int i=2;i<=n;i++){
			x*=i;
		}
		return x;
	}
}
