package org.jquantlib.testsuite.math.distributions;

import static org.junit.Assert.assertEquals;

import org.jquantlib.math.distributions.CumulativeNormalDistribution;
import org.junit.Test;

/**
 * @author <Richard Gomes>
 */
public class CumulativeNormalDistributionTest {

	@Test
	public void testKnownGoodValuesFromAbramStegun() {
		
		double[][] testvalues = {	{0.0, 0.5},
									{0.1, 0.539827837277029},
									{0.2, 0.579259709439103},
									{0.3, 0.617911422188953},
									{0.4, 0.655421741610324},
									{0.5, 0.691462461274013},
									{0.6, 0.725746882249927},
									{0.7, 0.758036347776927},
									{0.8, 0.788144601416604},
									{0.9, 0.815939874653241},
									{1.0, 0.841344746068543},
									{1.2, 0.884930329778292},
									{1.4, 0.919243340766229},
									{1.6, 0.945200708300442},
									{1.8, 0.964069680887074},
									{2.0, 0.977249868051821},
									{2.5, 0.993790334674224},
									{3.0, 0.998650101968370},
									{3.5, 0.9997673709},
									{4.0, 0.9999683288},
									{5.0, 0.9999997133}};
									
		
		CumulativeNormalDistribution cnd = new CumulativeNormalDistribution();
		
		for(int i=0;i<testvalues.length;i++){
			double z = testvalues[i][0];
			double expected = testvalues[i][1];
			double computed = cnd.evaluate(z);
			double tolerance = (Math.abs(z)<3.01) ? 1.0e-15: 1.0e-10;
			assertEquals(expected, computed,tolerance);
			assertEquals(1.0, computed+ cnd.evaluate(-z),tolerance);
			
		}
	}
	
	@Test
	public void testExtremes(){
		double z = -40;
		CumulativeNormalDistribution cnd = new CumulativeNormalDistribution();
		
		assertEquals(0, cnd.evaluate(z),1.0e-15);
		z = -10;
		assertEquals(0, cnd.evaluate(z),1.0e-15);
		z = 10;
		assertEquals(1.0, cnd.evaluate(z),1.0e-15);
		z = 40;
		assertEquals(1.0, cnd.evaluate(z),1.0e-15);
		
	}
	
}
