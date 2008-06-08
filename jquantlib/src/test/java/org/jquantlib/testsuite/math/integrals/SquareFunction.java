package org.jquantlib.testsuite.math.integrals;

import org.jquantlib.math.UnaryFunctionDouble;

/**
 * @author <Richard Gomes>
 */
public class SquareFunction implements UnaryFunctionDouble{

	public double evaluate(double x) {
		return x*x;
	}

}
