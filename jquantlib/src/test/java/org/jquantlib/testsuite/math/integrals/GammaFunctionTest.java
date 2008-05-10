package org.jquantlib.testsuite.math.integrals;

import static org.junit.Assert.fail;

import org.jquantlib.math.distributions.GammaFunction;
import org.junit.Test;

/**
 * @author <Richard Gomes>
 */
public class GammaFunctionTest {

	@Test
	public void testGammaFunction() {

//	    BOOST_MESSAGE("Testing Gamma function...");
//
//	    Real expected = 0.0;
//	    Real calculated = GammaFunction().logValue(1);
//	    if (std::fabs(calculated) > 1.0e-15)
//	        BOOST_ERROR("GammaFunction(1)\n"
//	                    << std::setprecision(16) << QL_SCIENTIFIC
//	                    << "    calculated: " << calculated << "\n"
//	                    << "    expected:   " << expected);
//
//	    for (Size i=2; i<9000; i++) {
//	        expected  += std::log(Real(i));
//	        calculated = GammaFunction().logValue(i+1);
//	        if (std::fabs(calculated-expected)/expected > 1.0e-9)
//	            BOOST_ERROR("GammaFunction(" << i << ")\n"
//	                        << std::setprecision(16) << QL_SCIENTIFIC
//	                        << "    calculated: " << calculated << "\n"
//	                        << "    expected:   " << expected << "\n"
//	                        << "    rel. error: "
//	                        << std::fabs(calculated-expected)/expected);
//	    }

	    System.out.println("Testing Gamma function...");

	    GammaFunction gfn = new GammaFunction();
	    
	    double expected = 0.0;
	    double calculated = gfn.logValue(1);
	    if (Math.abs(calculated) > 1.0e-15)
	        fail("GammaFunction(1)\n"
	                    + "    calculated: " + calculated + "\n"
	                    + "    expected:   " + expected);

	    for (int i=2; i<9000; i++) {
	        expected  += Math.log(new Double(i));
	        calculated = gfn.logValue(i+1);
	        if (Math.abs(calculated-expected)/expected > 1.0e-9)
	            fail("GammaFunction(" + i + ")\n"
	                        + "    calculated: " + calculated + "\n"
	                        + "    expected:   " + expected + "\n"
	                        + "    rel. error: " + Math.abs(calculated-expected)/expected);
	    }
	
	}

	
	@Test
	public void testKnownValuesAbramStegun() {
		double[][] values =	{	{1.075, -0.0388257395},
								{1.225, -0.0922078291},
								{1.5,   -0.1207822376},
								{1.975, -0.0103670060} };
		
		GammaFunction gammaFunction = new GammaFunction();
		for(int i=0;i<values.length;i++){
			double x = values[i][0];
			double expected = values[i][1];
			double realised = gammaFunction.logValue(x);
			double tolerance = 1.0e-10;
			if (Math.abs(expected-realised)>tolerance)
			    fail("x: " + x + " expected: " + expected + " realised: " + realised);
		}
	}
}
