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

package org.jquantlib.testsuite.math.solvers1D;

import static org.junit.Assert.fail;

import org.jquantlib.math.distributions.Derivative;
import org.jquantlib.math.solvers1D.NewtonSafe;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Dominik Holenstein
 *
 */

public class NewtonSafeTest {
	
    private final static Logger logger = LoggerFactory.getLogger(NewtonSafeTest.class);

	public NewtonSafeTest() {
		logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
	@Test
	public void testNewtonSafe() {
		
		double accuracy = 1.0e-15;
		double guess = 1.5;
		double xMin = 0.0;
		double xMax = 3.0;
		
		final Derivative f = new Derivative() {

			@Override
			public double op(double x) {
				return x*x-1;
			}
			
			@Override
			public double derivative (double x) {
				return 2*x;
			}
		};
		
		NewtonSafe newtonsafe = new NewtonSafe();
		
		double root = newtonsafe.solve(f, accuracy, guess, xMin, xMax);
		
		// assertEquals(1.0, root, accuracy);
		if (Math.abs(1.0-root)> accuracy) {
			fail("expected: 1.0" + " but root is: " + root);
		}
		
		// assertEquals(100, newtonsafe.getMaxEvaluations());
		if(newtonsafe.getMaxEvaluations() != 100){
			fail("expected: 100" + " but was: " + newtonsafe.getMaxEvaluations());
		}
		
		root = newtonsafe.solve(f, accuracy, 0.01, 0.1);

		// assertEquals(1.0, root, accuracy);
		if (Math.abs(1.0-root)> accuracy) {
			fail("expected: 1.0" + " but root is: " + root);
		}
		
		//assertEquals(10, newtonsafe.getNumEvaluations());
		if(newtonsafe.getNumEvaluations() != 10){
			fail("expected: 10" + " but was: " + newtonsafe.getNumEvaluations());
		}
	}
}
