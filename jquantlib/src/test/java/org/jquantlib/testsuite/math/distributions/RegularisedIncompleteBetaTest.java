package org.jquantlib.testsuite.math.distributions;

import static org.junit.Assert.fail;

import org.jquantlib.math.RegularisedIncompleteBeta;
import org.junit.Test;

/**
 * @author <Richard Gomes>
 */
public class RegularisedIncompleteBetaTest {

	public RegularisedIncompleteBetaTest() {
		System.out.println("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
	@Test
	public void testRegIncompleteBetaHartleyFitchExamples() {
		
		
		RegularisedIncompleteBeta beta = new RegularisedIncompleteBeta();
		
		// FIXME Is 1.0e-6 accuracy ok?
		double[][] values = { {30, 5, 0.7, 0.0116578},
							  {30, 5, 0.94, 0.94936},
							  {30, 5, 0.96, 0.989182},
							  {10, 16, 0.2, 0.0173319},
							  {16, 10, 0.8, 0.982668},
							  { 4,  2, 0.89, 0.903488},
							  { 4,  2, 0.42, 0.103308}};
	
		for(int i=0;i<values.length;i++){
			double a = values[i][0];
			double b = values[i][1];
			double x = values[i][2];
			double expected = values[i][3];
			double realised = beta.evaluate(x, a, b);
			if (Math.abs(expected-realised)>1.0e-6)
				fail("x: " + x + " expected: " + expected + " realised: " + realised);
		}
	}
}
