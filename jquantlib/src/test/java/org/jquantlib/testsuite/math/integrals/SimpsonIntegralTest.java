/*
 Copyright (C) 2008 Richard Gomes

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

import org.apache.log4j.Logger;
import org.jquantlib.math.UnaryFunctionDouble;
import org.jquantlib.math.integrals.Integrator;
import org.jquantlib.math.integrals.SimpsonIntegral;
import org.junit.Test;

/**
 * 
 * @author Dominik Holenstein
 *
 */

/* 
 * Test not implemented yet.
 */

//TODO: Write SimpsonIntegral test case.
public class SimpsonIntegralTest {
	
    private final static Logger logger = Logger.getLogger(SimpsonIntegralTest.class);

	public SimpsonIntegralTest() {
		logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
	@Test
	public void quickConvergenceTest() {
		// only intended to avoid failure during unit tests
		UnaryFunctionDouble f = new UnaryFunctionDouble() {
			public double evaluate(double x) {
				return 1/(1+Math.pow(Math.tan(x),Math.sqrt(2)));
			}
		};

		// See Larson, "Problem-Solving Through Problems" pg.33 for solution
		double expected = Math.PI / 4;
		double realised = 0;
		double tolerance = 1.0e-4;
	
		try {
			Integrator simpint = new SimpsonIntegral(tolerance, 10);
			realised = simpint.evaluate(f, 0, Math.PI/2);
		} catch (ArithmeticException e) {
			fail("Desired tolerance not achieved while integrating f(x) = 1 / (1+tan(x)^sqrt(2)) within [0,6] using trapezoid-midpoint approximation.\n");
		}
	}
}

