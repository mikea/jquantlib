/*
 Copyright (C) 2008 Daniel Kong
 
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
import static org.junit.Assert.assertFalse;

import org.jquantlib.math.interpolations.CubicSplineInterpolation;
import org.jquantlib.math.interpolations.Interpolation;
import org.jquantlib.math.interpolations.factories.BackwardFlat;
import org.jquantlib.math.interpolations.factories.CubicSpline;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Daniel Kong
 **/

public class CubicSplineInterpolationTest extends InterpolationTestBase{

private final static Logger logger = LoggerFactory.getLogger(CubicSplineInterpolationTest.class);
	
	private static final double generic_x[] = { 0.0, 1.0, 3.0, 4.0 };
	private static final double generic_y[] = { 0.0, 0.0, 2.0, 2.0 };
	private static final double generic_natural_y2[] = { 0.0, 1.5, -1.5, 0.0 };
	private static double x35[]= new double[3];

	
	public CubicSplineInterpolationTest() {
		logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
		logger.info("\n\n::::: Testing spline interpolation on generic values... :::::");
	}

	@Test
	public void testNaturalSpline(){
		Interpolation interpolation = new CubicSpline(
				CubicSplineInterpolation.BoundaryCondition.SecondDerivative,
				generic_natural_y2[0],
				CubicSplineInterpolation.BoundaryCondition.SecondDerivative,
				generic_natural_y2[generic_x.length-1],
				false)
				.interpolate(generic_x, generic_y);
		
		checkValues("Natural spline", interpolation, generic_x, generic_y);
		int n=generic_x.length;
		for (int i=0; i<n; i++) {
	        double interpolated = interpolation.secondDerivative(generic_x[i]);
	        double error = interpolated - generic_natural_y2[i];
	        assertFalse("Natural spline interpolation "
       					+"second derivative failed at x="+generic_x[i]
       					+"\n interpolated value: "+interpolated
       					+"\n expected value:     "+generic_natural_y2[i]
                        +"\n error:              "+error,
       					abs(error) > 3e-16);
	    }
		
		//TODO: check the locate() method in AbstractInterpolation which leads to java.lang.ArrayIndexOutOfBoundsException. 
//	    x35[1] = interpolation.evaluate(3.5);
	}
	
	@Test
	public void testClampedSpline(){
		double y1a=0.0, y1b=0.0;
		Interpolation interpolation = new CubicSpline(
				CubicSplineInterpolation.BoundaryCondition.FirstDerivative,
				y1a,
				CubicSplineInterpolation.BoundaryCondition.FirstDerivative,
				y1b,
				false)
				.interpolate(generic_x, generic_y);
		
		checkValues("Clamped spline", interpolation, generic_x, generic_y);
		check1stDerivativeValue("Clamped spline", interpolation, generic_x[0],0.0);
		check1stDerivativeValue("Clamped spline", interpolation, generic_x[generic_x.length-1],0.0);
			
		//TODO: check the locate() method in AbstractInterpolation which leads to java.lang.ArrayIndexOutOfBoundsException. 
//	    x35[0] = interpolation.evaluate(3.5);
	}
	
	

//
//    // Not-a-knot spline
//    f = CubicSpline(BEGIN(generic_x), END(generic_x), BEGIN(generic_y),
//                    CubicSpline::NotAKnot, Null<Real>(),
//                    CubicSpline::NotAKnot, Null<Real>(),
//                    false);
//    f.update();
//    checkValues("Not-a-knot spline", f,
//                BEGIN(generic_x), END(generic_x), BEGIN(generic_y));
//    checkNotAKnotCondition("Not-a-knot spline", f);
//
//    x35[2] = f(3.5);
//
//    if (x35[0]>x35[1] || x35[1]>x35[2]) {
//        BOOST_ERROR("Spline interpolation failure"
//                    << "\nat x = " << 3.5
//                    << "\nclamped spline    " << x35[0]
//                    << "\nnatural spline    " << x35[1]
//                    << "\nnot-a-knot spline " << x35[2]
//                    << "\nvalues should be in increasing order");
//    }
	
}
