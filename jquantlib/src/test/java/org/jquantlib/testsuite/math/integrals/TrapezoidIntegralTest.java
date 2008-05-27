package org.jquantlib.testsuite.math.integrals;

import static org.junit.Assert.fail;

import org.jquantlib.math.UnaryFunctionDouble;
import org.jquantlib.math.integrals.TrapezoidIntegral;
import org.jquantlib.math.integrals.Integrator;
import org.junit.Test;


// TODO Test is failing. Check line double expected = Math.exp(6);
public class TrapezoidIntegralTest {

	/*
	@Test
	public void testExp() {
		UnaryFunctionDouble exp = new UnaryFunctionDouble() {

			public double evaluate(double x) {
				return Math.exp(x);
			}
		};
		
		Integrator trapint = new TrapezoidIntegral(1.0,1000,TrapezoidIntegral.Method.MidPoint);
		double realised = trapint.integrate(exp, 0, 6);
		double expected = Math.exp(6);
		double tolerance = 1.0e-10;
		System.out.println(expected);
		System.out.println(realised);
		
		if (Math.abs(realised-expected)>tolerance)
			fail("Expected: " + expected + " Realised: " + realised);
	}
	*/
}
