package org.jquantlib.testsuite.math.integrals;

import org.jquantlib.math.UnaryFunctionDouble;

/**
 * @author <Richard Gomes>
 */
class FourthFunction implements UnaryFunctionDouble {

	public double evaluate(double x) {
		return (x*x)*(x*x);
	}

}
