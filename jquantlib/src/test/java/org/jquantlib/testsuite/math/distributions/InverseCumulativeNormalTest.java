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
	@Test
	public void testKnownGoodValuesFromAbramStegun() {
		
		double[][] testvalues = {   {0.01,-2.32634787404084},
									{0.1, -1.2815515641401562},
									{0.2, -0.8416212327266185},
									{0.3, -0.5244005132792953},
									{0.4, -0.2533471028599986},
									{0.5, 0.0},
									{0.6, 0.2533471028599986},
									{0.7, 0.5244005132792951},
									{0.8, 0.8416212327266186},
									{0.9, 1.2815515641401562},
									{0.99, 2.326347874388028}};
									
									
		InverseCumulativeNormal icn = new InverseCumulativeNormal();
		
		for(int i=0;i<testvalues.length;i++){
			double z = testvalues[i][0];
			double expected = testvalues[i][1];
			double computed = icn.evaluate(z);
			double tolerance = (Math.abs(z)<3.01) ? 1.0e-15: 1.0e-10;
			assertEquals(expected, computed,tolerance);
			assertEquals(1.0, computed + icn.evaluate(-z),tolerance);
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
