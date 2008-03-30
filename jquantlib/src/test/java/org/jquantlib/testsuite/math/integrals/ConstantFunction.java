package org.jquantlib.testsuite.math.integrals;

import org.jquantlib.math.UnaryFunctionDouble;

/**
 * @author <Richard Gomes>
 */
class ConstantFunction implements UnaryFunctionDouble {

	public double evaluate(double x) {
		return 1;
	}

}
