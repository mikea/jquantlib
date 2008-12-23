/*
 Copyright (C) 2008 Daniel Kong, Richard Gomes
 
 This source code is release under the BSD License.
 
 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the JQuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquant-devel@lists.sourceforge.net>. The license is also available online at
 <http://www.jquantlib.org/index.php/LICENSE.TXT>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */

package org.jquantlib.testsuite.math.interpolations;


import static java.lang.Math.abs;
import static java.lang.Math.exp;
import static org.junit.Assert.assertFalse;

import org.jquantlib.math.interpolations.CubicSplineInterpolation;
import org.jquantlib.math.interpolations.Interpolation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Daniel Kong
 * @author Richard Gomes
 **/

public abstract class InterpolationTestBase {

	public InterpolationTestBase() {
	}
	
	protected double[] xRange(double start, double finish, int size){
		double[] x = new double [size];
		double dx = (finish-start)/(size-1);
		for(int i=0; i<size-1; i++)
			x[i]=start+i*dx;
		x[size-1] = finish;
		return x;
	}
	
	protected double[] gaussian(final double[] x){
		double[] y = new double [x.length];
		for(int i=0; i<x.length; i++)
			y[i] = exp(-x[i]*x[i]);
		return y;
	}
	
	protected double[] parabolic(final double[] x){
		double[] y = new double[x.length];
		for(int i=0; i<x.length; i++)
			y[i] = -x[i]*x[i];
		return y;
	}
	
	protected void checkValues(
			final String type, 
			final CubicSplineInterpolation spline,
			double[] x, double[] y){
		double tolerance = 2.0e-15;
		for(int i=0; i<x.length; i++){
			double interpolated = spline.evaluate(x[i]);
			assertFalse(type+" interpolation failed at x = "+x[i]
					+"\n interpolated value: "+interpolated
					+"\n expected value:     "+y[i]
					+"\n error:        "+abs(interpolated-y[i]),
					abs(interpolated-y[i]) > tolerance);
		}		
	}
	
	protected void check1stDerivativeValue(
			final String type,
			final CubicSplineInterpolation spline,
            double x,
            double value) {
		double tolerance = 1.0e-14;
		double interpolated = spline.derivative(x);
		assertFalse(type+" interpolation first derivative failure at x = "+x
   					+"\n interpolated value: "+interpolated
   					+"\n expected value:     "+value
   					+"\n error:        "+abs(interpolated-value),
   					abs(interpolated-value) > tolerance);
	}

	protected void check2ndDerivativeValue(
			final String type,
			final CubicSplineInterpolation spline,
            double x,
            double value) {
		double tolerance = 1.0e-14;
		double interpolated = spline.secondDerivative(x);
		assertFalse(type+" interpolation second derivative failure at x = "+x
   					+"\n interpolated value: "+interpolated
   					+"\n expected value:     "+value
   					+"\n error:        "+abs(interpolated-value),
   					abs(interpolated-value) > tolerance);
	}

	protected void checkNotAKnotCondition(
			final String type,
			final CubicSplineInterpolation spline) {
		double tolerance = 1.0e-14;

		final double [] c = spline.getVc();
		assertFalse(type+" interpolation failure"
					+"\n    cubic coefficient of the first polinomial is "+c[0]
					+"\n    cubic coefficient of the second polinomial is "+c[1],
					abs(c[0]-c[1]) > tolerance);
		int n=c.length;
		assertFalse(type+" interpolation failure"
				+"\n    cubic coefficient of the 2nd to last polinomial is "+c[n-2]
				+"\n    cubic coefficient of the last polinomial is "+c[n-1],
				abs(c[n-2]-c[n-1]) > tolerance);
		
	}
	
	protected void checkSymmetry(
			final String type,
			final CubicSplineInterpolation spline,
			double xMin) {
		double tolerance = 1.0e-15;
		for (double x = xMin; x < 0.0; x += 0.1){
			double y1=spline.evaluate(x);
			double y2=spline.evaluate(-x);
			assertFalse(type+" interpolation not symmetric"
   					+"\n    x = "+x
   					+"\n    g(x)  = "+y1
   					+"\n    g(-x) = "+y2
   					+"\n    error:  "+abs(y1-y2),
   					abs(y1-y2) > tolerance);
		}
		
		
	}
	
}
