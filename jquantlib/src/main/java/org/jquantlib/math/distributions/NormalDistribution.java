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
import org.jquantlib.math.UnaryFunctionDouble;

/**
 * Provides the probability density function of the (unit) normal distribution
 *  
 * @author Richard Gomes
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
