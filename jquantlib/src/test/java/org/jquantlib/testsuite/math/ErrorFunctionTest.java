package org.jquantlib.testsuite.math;

import junit.framework.TestCase;

import org.jquantlib.math.ErrorFunction;
import org.junit.Test;

/**
 * @author <Richard Gomes>
 */
public class ErrorFunctionTest extends TestCase{

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
