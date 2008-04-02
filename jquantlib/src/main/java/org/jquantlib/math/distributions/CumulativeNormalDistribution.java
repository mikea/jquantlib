package org.jquantlib.math.distributions;

import org.jquantlib.math.Constants;
import org.jquantlib.math.ErrorFunction;
import org.jquantlib.math.UnaryFunctionDouble;

/**
 * @author <Richard Gomes>
 */
public class CumulativeNormalDistribution extends NormalDistribution implements UnaryFunctionDouble {

	static private final ErrorFunction errorFunction = new ErrorFunction(); 
	static private final NormalDistribution gaussian = new NormalDistribution();

	public CumulativeNormalDistribution() {
		super();
	}

	public CumulativeNormalDistribution(double average, double sigma) {
		super(average, sigma);
	}

	public double evaluate(double z) {
		
        double result = 0.5 * ( 1.0 + errorFunction.evaluate( z*Constants.M_SQRT_2 ) );
        if (result<=1e-8) { 
        	//See Jackels book
        	//TODO: investigate the threshold level
            // Asymptotic expansion for very negative z following (26.2.12)
            // on page 408 in M. Abramowitz and A. Stegun,
            // Pocketbook of Mathematical Functions, ISBN 3-87144818-4.
            double sum=1.0, zsqr=z*z, i=1.0, g=1.0, x, y,
                 a=Constants.QL_MAX_REAL, lasta;
            do {
                lasta=a;
                x = (4.0*i-3.0)/zsqr;
                y = x*((4.0*i-1)/zsqr);
                a = g*(x-y);
                sum -= a;
                g *= y;
                ++i;
                a = Math.abs(a);
            } while (lasta>a && a>=Math.abs(sum*Constants.QL_EPSILON));
            result = -gaussian.evaluate(z)/z*sum;
        }
        return result;
	}
	
	public double derivative(double x) /* @ReadOnly */ {
		double xn = (x - average) / sigma;
		return gaussian.evaluate(xn) / sigma;
	}

}



