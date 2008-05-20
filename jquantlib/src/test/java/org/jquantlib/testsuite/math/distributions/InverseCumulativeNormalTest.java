package org.jquantlib.testsuite.math.distributions;

// import static org.junit.Assert.assertEquals; --> not JUnit 4.4 conform

import org.jquantlib.math.distributions.InverseCumulativeNormal;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * @author Dominik Holenstein
 */

public class InverseCumulativeNormalTest {
	
	@Test
	public void testInverseCumulativNormal() {
		
		//FIXME: Add better 'good values' for this test. 
		//FIXME: The test values are ok up to the 9th position compared to QuantLib. We need 15 positions.
		double[][] testvalues = {   {0.01,-2.326347874214435},
									{0.1, -1.2815515648423783},
									{0.2, -0.8416212331497663},
									{0.3, -0.5244005129936681},
									{0.4, -0.2533471029978992},
									{0.5, 0.0},
									{0.6, 0.25334710299789914},
									{0.7, 0.5244005129936679},
									{0.8, 0.8416212331497664},
									{0.9, 1.2815515648423783},
									{0.99, 2.326347874214435}};
									
									
		InverseCumulativeNormal icn = new InverseCumulativeNormal();
		
		for(int i=0;i<testvalues.length;i++){
			double z = testvalues[i][0];
			double expected = testvalues[i][1];
			double computed = icn.evaluate(z);
			double tolerance = (Math.abs(z)<3.01) ? 1.0e-15: 1.0e-10;
			
			// assertEquals(expected, computed,tolerance); --> not JUnit 4.4 conform
			if (Math.abs(expected-computed)>tolerance) {
				fail("z: " + z + " expected: " + expected + " computed: " + computed);
			}
			
			//assertEquals(0.0, computed + icn.evaluate(z)*(-1.0), tolerance); --> not JUnit 4.4 conform
			double realized = computed + icn.evaluate(z)*(-1.0);
			if (Math.abs(realized) > tolerance) {
				fail("z: " + z + " expected: " + 0.0 + " realized: " + realized);
			}
		}
	}
	
	@Test 
	public void testExtremes(){
		double z = -40;
		double tolerance = 1.0e-15;
		
		InverseCumulativeNormal icn = new InverseCumulativeNormal();
		
		// assertEquals(0, icn.evaluate(z),tolerance); --> not JUnit 4.4 conform
		if (Math.abs(icn.evaluate(z)) > tolerance) {
			fail("z: " + z + " expected: " + 0.0 + " realized: " + icn.evaluate(z));
		}
		
		z = -10;
		// assertEquals(0, icn.evaluate(z),tolerance); --> not JUnit 4.4 conform
		if (Math.abs(icn.evaluate(z)) > tolerance) {
			fail("z: " + z + " expected: " + 0.0 + " realized: " + icn.evaluate(z));
		}
		
		z = 10;
		//assertEquals(1.0, icn.evaluate(z),tolerance); --> not JUnit 4.4 conform
		if (Math.abs(icn.evaluate(z)) > (tolerance + 1.0)) {
			fail("z: " + z + " expected: " + 1.0 + " realized: " + icn.evaluate(z));
		}
		
		z = 40;
		// assertEquals(1.0, icn.evaluate(z),tolerance); --> not JUnit 4.4 conform
		if (Math.abs(icn.evaluate(z)) > (tolerance + 1.0)) {
			fail("z: " + z + " expected: " + 1.0 + " realized: " + icn.evaluate(z));
		}		
	}
}
