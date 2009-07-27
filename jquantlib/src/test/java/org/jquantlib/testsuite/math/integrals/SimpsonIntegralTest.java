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

import org.jquantlib.math.Ops.DoubleOp;
import org.jquantlib.math.distributions.NormalDistribution;
import org.jquantlib.math.functions.Constant;
import org.jquantlib.math.functions.Cos;
import org.jquantlib.math.functions.Identity;
import org.jquantlib.math.functions.Sin;
import org.jquantlib.math.functions.Sqr;
import org.jquantlib.math.integrals.Integrator;
import org.jquantlib.math.integrals.SimpsonIntegral;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Dominik Holenstein
 */
//TODO: Write SimpsonIntegral test case.
// TODO: code review :: Please complete this class and perform another code review.
public class SimpsonIntegralTest {
	
    private final static Logger logger = LoggerFactory.getLogger(SimpsonIntegralTest.class);

	public SimpsonIntegralTest() {
		logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
		
    @Ignore
	@Test
	public void testSimpsonIntegral(){
		double tolerance = 1.0e-6;
		//f(x) = 1
		testSingle(new SimpsonIntegral(tolerance), new Constant(1.0), 0.0, 1.0, 1.0, tolerance, "f(x) = 1" );
		
		//f(x) = x
		testSingle(new SimpsonIntegral(tolerance), new Identity(), 0.0, 1.0, 0.5, tolerance, "f(x) = x" );
		
		//f(x) = x^2
		testSingle(new SimpsonIntegral(tolerance), new Sqr(), 0.0, 1.0, 1.0/3.0, tolerance, "f(x) = x^2" );
		
		//f(x) = sin(x)
		testSingle(new SimpsonIntegral(tolerance), new Sin(), 0.0, Math.PI, 2.0, tolerance, "f(x) = sin(x)" );
		
		//f(x) = cos(x)
		testSingle(new SimpsonIntegral(tolerance), new Cos(), 0.0, Math.PI, 0.0, tolerance, "f(x) = cos(x)" );
		
		//f(x) = Gaussian(x)
		testSingle(new SimpsonIntegral(tolerance), new NormalDistribution(0.0, 1.0), -10.0, 10.0, 1.0, tolerance, "f(x) = Gaussian(x)" );
		
		
		//f(x) = Abcd2(x)
		/* FIXME: Method not implemented yet.
		testSingle(new SimpsonIntegral(tolerance), new Ops.DoubleOp(){
			public double evaluate(double x) {
				return new (0.0, 1.0).evaluate(x);
			}
		}, -10.0, 10.0, 1.0, tolerance, "f(x) = Abcd2(x)" );
		
		testSingle(I, "f(x) = Gaussian(x)",
		           NormalDistribution(), -10.0, 10.0, 1.0);
		testSingle(I, "f(x) = Abcd2(x)",
		           AbcdSquared(0.07, 0.07, 0.5, 0.1, 8.0, 10.0), 5.0, 6.0,
		           Abcd(0.07, 0.07, 0.5, 0.1).covariance(5.0, 6.0, 8.0, 10.0));
		
		*/		
	}
	
	@Ignore
	@Test
	public void quickConvergenceTest() {
		// only intended to avoid failure during unit tests
		DoubleOp f = new DoubleOp() {
			public double op(double x) {
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
	
	
	
	private void testSingle(Integrator integrator, DoubleOp function, double min, double max, double desired, double precision, String tag){
		double realised = integrator.evaluate(function, min, max);
		if(Math.abs(realised - desired) > precision){
			fail("Integration failed for " + tag);
		}
	}
}
	
