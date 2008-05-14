package org.jquantlib.testsuite.math.distributions;


import static org.junit.Assert.fail;

import org.jquantlib.math.distributions.CumulativeBinomialDistribution;
import org.junit.Test;

/**
 * @author Dominik Holenstein
 */

public class CumulativeBinomialDistrubutionTest {
	
	@Test
	public void testCumulativeBinomialDistribution() {
		
	double[] testvalues = {		1.52587890625E-5,
								2.593994140625E-4,
								0.00209045410156,
								0.01063537597656,
								0.03840637207031,
								0.10505676269531,
								0.22724914550781,
								0.40180969238281,
								0.59819030761719,
								0.77275085449219,
								0.89494323730469,
								0.96159362792969,
								0.98936462402344,
								0.99790954589844,
								0.99974060058594,
								0.99998474121094, };

		double p = 0.5; 					// probability that an event occurs
		int n = testvalues.length;			// number of trials
		
		CumulativeBinomialDistribution cumbinomdist = new CumulativeBinomialDistribution(p,n);
		for (int i=0;i<testvalues.length;i++) {
			double expected = testvalues[i];
			double realised = cumbinomdist.evaluate(i); // i = number of successful events
			System.out.println(realised);
			double tolerance = 1.0e-11;
			if (Math.abs(expected-realised)>tolerance) {
				fail("x: " + i + " expected: " + expected + " realised: " + realised);
			}	
		}
	
	}
}

