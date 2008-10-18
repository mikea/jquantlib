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

package org.jquantlib.testsuite.math.integrals;


import static org.junit.Assert.fail;

import org.jquantlib.math.UnaryFunctionDouble;
import org.jquantlib.math.integrals.Integrator;
import org.jquantlib.math.integrals.TrapezoidIntegral;
import org.junit.Test;


public class TrapezoidIntegralTest {

	public TrapezoidIntegralTest() {
		System.out.println("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
	@Test
	public void testExp() {
		UnaryFunctionDouble exp = new UnaryFunctionDouble() {

			public double evaluate(double x) {
				return Math.exp(x);
			}
		};

		// Integral[e^x, 0, 6] = e^6 - e^0
		double expected = Math.exp(6) - 1;
		double realised = 0;
		double tolerance = 1.0e-4;
		
		try {
			Integrator trapint = new TrapezoidIntegral(tolerance, TrapezoidIntegral.Method.MidPoint, 1000);
			realised = trapint.evaluate(exp, 0, 6);
		} catch (ArithmeticException e) {
			fail("Desired tolerance not achieved while integrating f(x)=e^x within [0,6] using trapezoid-midpoint approximation.\n");
		}
	}
}
