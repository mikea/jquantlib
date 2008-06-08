package org.jquantlib.testsuite.math.integrals;

import org.jquantlib.math.UnaryFunctionDouble;

/**
 * @author <Richard Gomes>
 */
class CubeFunction implements UnaryFunctionDouble {

	public double evaluate(double x) {
		return x*x*x;
	}

}
