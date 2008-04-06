package org.jquantlib.testsuite.math.integrals;

import junit.framework.TestCase;

import org.jquantlib.math.distributions.GammaFunction;
import org.junit.Test;

/**
 * @author <Richard Gomes>
 */
public class GammaFunctionTest extends TestCase{

	@Test
	public void testKnownValuesAbramStegun() {
		double[][] values =	{	{1.075, -0.0388257395},
								{1.225, -0.0922078291},
								{1.5,   -0.1207822376},
								{1.975, -0.0103670060} };
		GammaFunction gammFunction = new GammaFunction();
		for(int i=0;i<values.length;i++){
			double x = values[i][0];
			double expected = values[i][1];
			double realised = gammFunction.logValue(x);
			double tolerance = 1.0e-10;
			if (Math.abs(expected-realised)>tolerance){
				fail("x: " + x + " expected: " + expected + " realised: " + realised);
			}
		}
	}
}
