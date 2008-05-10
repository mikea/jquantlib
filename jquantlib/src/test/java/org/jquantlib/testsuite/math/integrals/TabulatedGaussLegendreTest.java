package org.jquantlib.testsuite.math.integrals;

import static org.junit.Assert.fail;

import org.jquantlib.math.UnaryFunctionDouble;
import org.jquantlib.math.integrals.TabulatedGaussLegendre;
import org.junit.Test;

/**
 * @author <Richard Gomes>
 */
public class TabulatedGaussLegendreTest {

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
	    final int order[] = { 6, 7, 12, 20 };
	    TabulatedGaussLegendre quad = new TabulatedGaussLegendre();
	    for (int i=0; i<order.length; i++) {
	        quad.setOrder(order[i]);
	        double realised = quad.evaluate(f);
	        if (Math.abs(realised-expected) > tolerance)
            fail(" integrating " + tag + "\n"
                        + "    order " + order[i] + "\n"
                        + "    realised: " + realised + "\n"
                        + "    expected: " + expected);
	    }
	}
}
