/*
 Copyright (C) 2008 Richard Gomes

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquant-devel@lists.sourceforge.net>. The license is also available online at
 <http://www.jquantlib.org/index.php/LICENSE.TXT>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */

package org.jquantlib.math.distributions;

import org.jquantlib.math.Constants;
import org.jquantlib.math.ErrorFunction;
import org.jquantlib.math.UnaryFunctionDouble;

/**
 * Cumulative normal distribution function (cdf).
 * <p>
 * Given x it provides an approximation to the integral of the gaussian normal distribution.
 * 
 * {@latex[
 * 	\frac12 \left(1+\mathrm{erf}\left( \frac{x-\mu}{\sigma\sqrt2}\right) \right)
 * }
 * @see <i>M. Abramowitz and I. Stegun, Handbook of Mathematical Functions, Dover Publications, New York (1972)</i>
 * @see <a href="http://en.wikipedia.org/wiki/Normal_distribution">Cumulative Normal Distribution on Wikipedia</a>
 * 
 * @author Richard Gomes
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

	/** 
	 * Computes the cumulative normal distribution.
	 * See Jaeckels book "Monte Carlo Methods in Finance", ISBN-13: 978-0471497417 
	 * Asymptotic expansion for very negative z following (26.2.12)
         * on page 408 in M. Abramowitz and A. Stegun,
         * Pocketbook of Mathematical Functions, ISBN 3-87144818-4.
	 * @param z
	 * @return result
	 */
	public double evaluate(double z) /* @Read-only */ {
		
        double result = 0.5 * ( 1.0 + errorFunction.evaluate( z*Constants.M_SQRT_2 ) );
        if (result<=1e-8) { 
            // See Jaeckels book "Monte Carlo Methods in Finance", ISBN-13: 978-0471497417 
            // TODO: investigate the threshold level
            // Asymptotic expansion for very negative z following (26.2.12)
            // on page 408 in M. Abramowitz and A. Stegun,
            // Pocketbook of Mathematical Functions, ISBN 3-87144818-4.
            double sum=1.0, zsqr=z*z, i=1.0, g=1.0, x, y,
                 a=Constants.QL_MAX_REAL, lasta;
            do {
		lasta = a;
		x = (4.0 * i - 3.0) / zsqr;
		y = x * ((4.0 * i - 1) / zsqr);
		a = g * (x - y);
		sum -= a;
		g *= y;
		++i;
		a = Math.abs(a);
	    } while (lasta > a && a >= Math.abs(sum * Constants.QL_EPSILON));
            result = -gaussian.evaluate(z)/z*sum;
        }
        return result;
	}
	
	/**
	 * Computes the derivative.
	 * @param x
	 * @return <code>gaussian.evaluate(xn) / sigma</code>
	 */
	public double derivative(double x) /* @ReadOnly */ {
	    double xn = (x - average) / sigma;
	    return gaussian.evaluate(xn) / sigma;
	}
}



