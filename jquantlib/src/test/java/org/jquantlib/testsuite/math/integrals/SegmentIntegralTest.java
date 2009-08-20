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

import org.jquantlib.QL;
import org.jquantlib.math.Ops.DoubleOp;
import org.jquantlib.math.distributions.NormalDistribution;
import org.jquantlib.math.functions.Constant;
import org.jquantlib.math.functions.Cos;
import org.jquantlib.math.functions.Identity;
import org.jquantlib.math.functions.Sin;
import org.jquantlib.math.functions.Sqr;
import org.jquantlib.math.integrals.Integrator;
import org.jquantlib.math.integrals.SegmentIntegral;
import org.junit.Test;

/**
 *
 * @author Dominik Holenstein
 *
 */

public class SegmentIntegralTest {

	public SegmentIntegralTest() {
		QL.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}

	@Test
	public void testSegmentIntegral() {

			final double tolerance = 1.0e-6;
			//f(x) = 1
			testSingle(new SegmentIntegral(10000), new Constant(1.0), 0.0, 1.0, 1.0, tolerance, "f(x) = 1" );

			//f(x) = x
			testSingle(new SegmentIntegral(10000), new Identity(), 0.0, 1.0, 0.5, tolerance, "f(x) = x" );

			//f(x) = x^2
			testSingle(new SegmentIntegral(10000), new Sqr(), 0.0, 1.0, 1.0/3.0, tolerance, "f(x) = x^2" );

			//f(x) = sin(x)
			testSingle(new SegmentIntegral(10000), new Sin(), 0.0, Math.PI, 2.0, tolerance, "f(x) = sin(x)" );

			//f(x) = cos(x)
			testSingle(new SegmentIntegral(10000), new Cos(), 0.0, Math.PI, 0.0, tolerance, "f(x) = cos(x)" );

			//f(x) = Gaussian(x)
			testSingle(new SegmentIntegral(10000), new NormalDistribution(0.0, 1.0), -10.0, 10.0, 1.0, tolerance, "f(x) = Gaussian(x)" );

			//f(x) = Abcd2(x)
			/* FIXME: Method not implemented yet.
			testSingle(new SegmentIntegral(10000), new Ops.DoubleOp(){
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

		//Additional Test!
		@Test
		public void quickConvergenceTest() {
			// only intended to avoid failure during unit tests
			final DoubleOp f = new DoubleOp() {
				public double op(final double x) {
					return 1/(1+Math.pow(Math.tan(x),Math.sqrt(2)));
				}
			};

			// See Larson, "Problem-Solving Through Problems" pg.33 for solution
			final double expected = Math.PI / 4;
			double realised = 0;
			final double tolerance = 1.0e-4;

			try {
				final Integrator simpint = new SegmentIntegral(10000);
				realised = simpint.evaluate(f, 0, Math.PI/2);
			} catch (final ArithmeticException e) {
				fail("Desired tolerance not achieved while integrating f(x) = 1 / (1+tan(x)^sqrt(2)) within [0,6] using trapezoid-midpoint approximation.\n");
			}
		}



		private void testSingle(final Integrator integrator, final DoubleOp function, final double min, final double max, final double desired, final double precision, final String tag){
			final double realised = integrator.evaluate(function, min, max);
			if(Math.abs(realised - desired) > precision){
				fail("Integration failed for " + tag);
			}
		}
	}


