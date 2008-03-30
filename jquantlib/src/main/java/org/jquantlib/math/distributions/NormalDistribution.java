package org.jquantlib.math.distributions;

import org.jquantlib.math.Constants;

/**
 * Provides the probability density function of the (unit) normal distribution
 *  
 * @author <Richard Gomes>
 */
public class NormalDistribution {

	public double evaluate(double x){
		double exponent = -0.5*x*x;
		if (exponent<=-690.0){
			return 0.0;
		}
		return Constants.M_1_SQRT2PI*Math.exp(exponent);
	}
}
