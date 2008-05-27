package org.jquantlib.math.integrals;

import org.jquantlib.math.UnaryFunctionDouble;

/**
 * @author Richard Gomes
 */
public interface Integrator {

	double integrate(UnaryFunctionDouble f, double a, double b);
	
	int getNumberOfEvaluations();
	
}
