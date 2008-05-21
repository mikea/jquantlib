package org.jquantlib.math.distributions;

import org.jquantlib.math.UnaryFunctionDouble;

public interface Derivative extends UnaryFunctionDouble {

	public double derivative(double x) /* ReadOnly */;
	
}
