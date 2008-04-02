package org.jquantlib.math.distributions;

import org.jquantlib.math.Constants;
import org.jquantlib.math.UnaryFunctionDouble;

/**
 * Provides the probability density function of the (unit) normal distribution
 *  
 * @author <Richard Gomes>
 */
public class NormalDistribution implements UnaryFunctionDouble {

	protected double average;
	protected double sigma;
	private double normalizationFactor;
	private double denominator;
	private double derNormalizationFactor;

	public NormalDistribution() {
		this(0.0, 1.0);
	}
	
	public NormalDistribution(double average, double sigma) {
        if (sigma <= 0.0) throw new IllegalArgumentException("sigma must be greater than 0.0 ("+sigma+" not allowed)");

		this.average = average;
		this.sigma = sigma;
		
	    this.normalizationFactor = Constants.M_SQRT_2*Constants.M_1_SQRTPI/sigma;
	    this.derNormalizationFactor = sigma*sigma;
	    this.denominator = 2.0*derNormalizationFactor;
	}
	
	
	public double evaluate(double x) /* @ReadOnly */ {
		double exponent = -0.5*x*x;
		if (exponent <= -690.0) {
			return 0.0;
		}
		return Constants.M_1_SQRT2PI*Math.exp(exponent);
	}

	public double derivative(double x) /* @ReadOnly */ {
	    return (evaluate(x) * (average - x)) / derNormalizationFactor;
	}
}
