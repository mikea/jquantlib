package org.jquantlib.testsuite.math.distributions;

import junit.framework.TestCase;

import org.jquantlib.math.distributions.NormalDistribution;

/**
 * @author <Richard Gomes>
 */
public class TestNormalDistribution extends TestCase {
	public void testKnownGoodValuesFromAbramStegun() {
		
		double[][] testvalues = {	{0.0, 0.398942280401433},
									{1.0, 0.241970724519143},
									{2.0, 0.053990966513188},
									{3.0, 0.004431848411938},
									{4.0, 1.338302258e-4},
									{5.0, 1.486719515e-6}};
									
		
		NormalDistribution normal = new NormalDistribution();
		
		for(int i=0;i<testvalues.length;i++){
			double z = testvalues[i][0];
			double expected = testvalues[i][1];
			double computed = normal.evaluate(z);
			double tolerance = (Math.abs(z)<3.01) ? 1.0e-15: 1.0e-10;
			assertEquals(expected, computed,tolerance);
			assertEquals(expected, normal.evaluate(-z),tolerance);
			
		}
	}

}
