package org.jquantlib.testsuite.math.distributions;

import static org.junit.Assert.assertEquals;

import org.jquantlib.math.distributions.InverseCumulativeNormal;
import org.junit.Test;

/**
 * @author Dominik Holenstein
 */

/*TODO Test is failing due to tolerance issues. Needs to be checked. */

public class InverseCumulativeNormalTest {
/*	
	// @Test
	public void testKnownGoodValuesFromAbramStegun() {
		
		double[][] testvalues = {   {0.01, -2.32634787634015},
									{0.1, -1.28155156597495},
									{0.2, -0.84162123128771},
									{0.3, -0.52440050989389},
									{0.4, -0.25334710627794},
									{0.5, 0.0},
									{0.6, 0.25334710627794},
									{0.7, 0.52440050989389},
									{0.8, 0.841621235013},
									{0.9, 1.28155155479908},
									{0.99, 2.32634790241718}};
									
									
		InverseCumulativeNormal icn = new InverseCumulativeNormal();
		
		for(int i=0;i<testvalues.length;i++){
			double z = testvalues[i][0];
			double expected = testvalues[i][1];
			double computed = icn.evaluate(z);
			double tolerance = (Math.abs(z)<3.01) ? 1.0e-15: 1.0e-10;
			assertEquals(expected, computed,tolerance);
			assertEquals(1.0, computed+ icn.evaluate(-z),tolerance);
			
		}
	}
	
	@Test
	public void testExtremes(){
		double z = -40;
		InverseCumulativeNormal icn = new InverseCumulativeNormal();
		
		assertEquals(0, icn.evaluate(z),1.0e-15);
		z = -10;
		assertEquals(0, icn.evaluate(z),1.0e-15);
		z = 10;
		assertEquals(1.0, icn.evaluate(z),1.0e-15);
		z = 40;
		assertEquals(1.0, icn.evaluate(z),1.0e-15);
		
	}

*/

}
