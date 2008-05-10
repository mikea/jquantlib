package org.jquantlib.testsuite.math.integrals;

import static org.junit.Assert.fail;

import org.jquantlib.math.UnaryFunctionDouble;
import org.jquantlib.math.integrals.GaussKonrodPattersonIntegrator;
import org.jquantlib.math.integrals.Integrator;
import org.junit.Test;

/**
 * @author <Richard Gomes>
 */
public class GaussKonrodPattersonIntegratorTest {

	
	@Test
	public void testPolynomials() {
		checkSingleTabulated(new ConstantFunction(), "f(x)=1", 2.0, 1.0e-13);
		checkSingleTabulated(new LinearFunction(), "f(x)=x", 0.0, 1.0e-13);
		checkSingleTabulated(new SquareFunction(), "f(x)=x^2", 2.0/3.0, 1.0e-13);
		checkSingleTabulated(new CubeFunction(), "f(x)=x^3", 0.0, 1.0e-13);
		checkSingleTabulated(new FourthFunction(), "f(x)=x^4", 2.0/5.0, 1.0e-13);		
	}
	
	@Test
	public void checkSingleTabulated(UnaryFunctionDouble f, String tag,
	                         double expected, double tolerance) {
		
		Integrator quad = new GaussKonrodPattersonIntegrator();
	    double realised = quad.integrate(f,-1,1);
	        
        if (Math.abs(realised-expected) > tolerance)
        	fail(" integrating " + tag + "\n"
                    + "    realised: " + realised + "\n"
                    + "    expected: " + expected);
	    
	}
	
	@Test
	public void testExp() {
		UnaryFunctionDouble exp = new UnaryFunctionDouble() {

			public double evaluate(double x) {
				
				return Math.exp(x);
			}
			
		};
		
		Integrator quad = new GaussKonrodPattersonIntegrator(0,0);
		double realised = quad.integrate(exp, 0, 6);
		double expected = Math.exp(6) - 1.0;
		double tolerance = 1.0e-10;
		
		if (Math.abs(realised-expected)>tolerance)
			fail("Expected: " + expected + " Realised: " + realised);
	}
}
