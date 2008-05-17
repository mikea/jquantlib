/*
 Copyright (C) 2008 Richard Gomes

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquantlib-dev@lists.sf.net>. The license is also available online at
 <http://jquantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the originating copyright notice follows below.
 */




package org.jquantlib.math.distributions;

import org.jquantlib.math.UnaryFunctionDouble;
import org.jquantlib.math.ErrorFunction;
import org.jquantlib.math.Constants;
import org.jquantlib.math.distributions.NormalDistribution;




/**
 * 
 * @author Dominik Holenstein
 *
 */


public class InverseCumulativeNormal extends NormalDistribution implements UnaryFunctionDouble {
	
	//TODO: check whether this code is correctly translated.
	//C++:
	//const CumulativeNormalDistribution InverseCumulativeNormal::f_;
	static CumulativeNormalDistribution f_;
	
	 // Coefficients for the rational approximation.
    static double a1_ = -3.969683028665376e+01;
    static double a2_ =  2.209460984245205e+02;
    static double a3_ = -2.759285104469687e+02;
    static double a4_ =  1.383577518672690e+02;
    static double a5_ = -3.066479806614716e+01;
    static double a6_ =  2.506628277459239e+00;

    static double b1_ = -5.447609879822406e+01;
    static double b2_ =  1.615858368580409e+02;
    static double b3_ = -1.556989798598866e+02;
    static double b4_ =  6.680131188771972e+01;
    static double b5_ = -1.328068155288572e+01;

    static double c1_ = -7.784894002430293e-03;
    static double c2_ = -3.223964580411365e-01;
    static double c3_ = -2.400758277161838e+00;
    static double c4_ = -2.549732539343734e+00;
    static double c5_ =  4.374664141464968e+00;
    static double c6_ =  2.938163982698783e+00;

    static double d1_ =  7.784695709041462e-03;
    static double d2_ =  3.224671290700398e-01;
    static double d3_ =  2.445134137142996e+00;
    static double d4_ =  3.754408661907416e+00;

    // Limits of the approximation regions
    static double x_low_ = 0.02425;
    static double x_high_= 1.0 - x_low_;
	
	public InverseCumulativeNormal() {
		super();
	}

	public InverseCumulativeNormal(double average, double sigma) {
		
	}
	
	
	public double evaluate (double x){
		if (sigma <= 0.0) throw new IllegalArgumentException("sigma must be greater than 0.0 ("+sigma+" not allowed)");

		double z;
		double r;

		if (x < x_low_) {
			// Rational approximation for the lower region 0<x<u_low
			z = Math.sqrt(-2.0*Math.log(x));
         z = (((((c1_*z+c2_)*z+c3_)*z+c4_)*z+c5_)*z+c6_) /
             ((((d1_*z+d2_)*z+d3_)*z+d4_)*z+1.0);
     } else if (x <= x_high_) {
         // Rational approximation for the central region u_low<=x<=u_high
         z = x - 0.5;
         r = z*z;
         z = (((((a1_*r+a2_)*r+a3_)*r+a4_)*r+a5_)*r+a6_)*z /
             (((((b1_*r+b2_)*r+b3_)*r+b4_)*r+b5_)*r+1.0);
     } else {
         // Rational approximation for the upper region u_high<x<1
         z = Math.sqrt(-2.0*Math.log(1.0-x));
         z = -(((((c1_*z+c2_)*z+c3_)*z+c4_)*z+c5_)*z+c6_) /
             ((((d1_*z+d2_)*z+d3_)*z+d4_)*z+1.0);
     }


     // The relative error of the approximation has absolute value less
     // than 1.15e-9.  One iteration of Halley's rational method (third
     // order) gives full machine precision.
     // #define REFINE_TO_FULL_MACHINE_PRECISION_USING_HALLEYS_METHOD
     // error (f_(z) - x) divided by the cumulative's derivative
		
	//TODO: check whether this code is correctly translated.
	// C++:
	// r = (f_(z) - x) * M_SQRT2 * M_SQRTPI * exp(0.5 * z*z);
	CumulativeNormalDistribution cumnormdist = new CumulativeNormalDistribution(sigma, average);
    r = (cumnormdist.evaluate(z) - x) * Constants.M_SQRT_2 * Constants.M_SQRTPI * Math.exp(0.5 * z*z);
     
    //  Halley's method
    z -= r/(1+0.5*z*r);
    
    return average + z*sigma;

	}

}
