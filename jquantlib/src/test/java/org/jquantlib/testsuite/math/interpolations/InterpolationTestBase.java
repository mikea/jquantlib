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
import org.jquantlib.math.matrixutilities.Array;

/**
 * @author Daniel Kong
 * @author Richard Gomes
 **/

public abstract class InterpolationTestBase {

	public InterpolationTestBase() {
	}

	protected Array xRange(final double start, final double finish, final int size){
		final double[] x = new double [size];
		final double dx = (finish-start)/(size-1);
		for(int i=0; i<size-1; i++)
			x[i]=start+i*dx;
		x[size-1] = finish;
		return new Array(x);
	}

	protected Array gaussian(final Array x){
		final double[] y = new double [x.size()];
		for(int i=0; i<x.size(); i++) {
		    final double value = x.get(i);
			y[i] = exp(-value*value);
		}
		return new Array(y);
	}

	protected Array parabolic(final Array x){
		final double[] y = new double[x.size()];
		for(int i=0; i<x.size(); i++) {
		    final double value = x.get(i);
			y[i] = -value*value;
		}
		return new Array(y);
	}

	protected void checkValues(
			final String type,
			final CubicSplineInterpolation spline,
			final Array x, final Array y){
		final double tolerance = 2.0e-15;
		for(int i=0; i<x.size(); i++){
			final double interpolated = spline.op(x.get(i));
			assertFalse(type+" interpolation failed at x = "+x.get(i)
					+"\n interpolated value: "+interpolated
					+"\n expected value:     "+y.get(i)
					+"\n error:        "+abs(interpolated-y.get(i)),
					abs(interpolated-y.get(i)) > tolerance);
		}
	}

	protected void check1stDerivativeValue(
			final String type,
			final CubicSplineInterpolation spline,
            final double x,
            final double value) {
		final double tolerance = 1.0e-14;
		final double interpolated = spline.derivative(x);
		assertFalse(type+" interpolation first derivative failure at x = "+x
   					+"\n interpolated value: "+interpolated
   					+"\n expected value:     "+value
   					+"\n error:        "+abs(interpolated-value),
   					abs(interpolated-value) > tolerance);
	}

	protected void check2ndDerivativeValue(
			final String type,
			final CubicSplineInterpolation spline,
            final double x,
            final double value) {
		final double tolerance = 1.0e-14;
		final double interpolated = spline.secondDerivative(x);
		assertFalse(type+" interpolation second derivative failure at x = "+x
   					+"\n interpolated value: "+interpolated
   					+"\n expected value:     "+value
   					+"\n error:        "+abs(interpolated-value),
   					abs(interpolated-value) > tolerance);
	}

	protected void checkNotAKnotCondition(
			final String type,
			final CubicSplineInterpolation spline) {
		final double tolerance = 1.0e-14;

		final Array c = spline.getVc();
		assertFalse(type+" interpolation failure"
					+"\n    cubic coefficient of the first polinomial is "+c.get(0)
					+"\n    cubic coefficient of the second polinomial is "+c.get(1),
					abs(c.get(0)-c.get(1)) > tolerance);
		final int n=c.size();
		assertFalse(type+" interpolation failure"
				+"\n    cubic coefficient of the 2nd to last polinomial is "+c.get(n-2)
				+"\n    cubic coefficient of the last polinomial is "+c.get(n-1),
				abs(c.get(n-2)-c.get(n-1)) > tolerance);

	}

	protected void checkSymmetry(
			final String type,
			final CubicSplineInterpolation spline,
			final double xMin) {
		final double tolerance = 1.0e-15;
		for (double x = xMin; x < 0.0; x += 0.1){
			final double y1=spline.op(x);
			final double y2=spline.op(-x);
			assertFalse(type+" interpolation not symmetric"
   					+"\n    x = "+x
   					+"\n    g(x)  = "+y1
   					+"\n    g(-x) = "+y2
   					+"\n    error:  "+abs(y1-y2),
   					abs(y1-y2) > tolerance);
		}


	}

}
