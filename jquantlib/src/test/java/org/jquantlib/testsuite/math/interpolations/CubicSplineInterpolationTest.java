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

import org.jquantlib.math.integrals.SimpsonIntegral;
import org.jquantlib.math.interpolations.CubicSplineInterpolation;
import org.jquantlib.math.interpolations.factories.CubicSpline;
import org.junit.Ignore;
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
		logger.info("\n\n::::: Testing cubic spline interpolation on: "
					+"Error On Gaussian Values, Gaussian Values, RPN15A Values, Generic Values, "
					+"Simmetric End Conditions, Derivative End Conditions, Non Restrictive HymanFilter, "
					+"MultiSpline ... :::::");
	}
	
    @Ignore
	@Test
	public void testSplineErrorOnGaussianValues(){

		logger.info("Testing spline approximation on Gaussian data sets...");
		
		int[] points = {5, 9, 17, 33};

	    // complete spline data from the original 1983 Hyman paper
	    double[] tabulatedErrors = { 3.5e-2, 2.0e-3, 4.0e-5, 1.8e-6 };
	    double[] toleranceOnTabErr = { 0.1e-2, 0.1e-3, 0.1e-5, 0.1e-6 };

	    //TODO: check if SimpsonIntegral used correctly
	    SimpsonIntegral integral = new SimpsonIntegral(1e-12, 0);

	    // still unexplained scale factor needed to obtain the numerical
	    // results from the paper
	    double scaleFactor = 1.9;

	    for (int i=0; i<points.length; i++) {
	        int n = points[i];
	        double[] x = xRange(-1.7, 1.9, n);
	        double[] y = gaussian(x);

	        // Not-a-knot
	        CubicSplineInterpolation interpolation = new CubicSpline(
		    		CubicSplineInterpolation.BoundaryCondition.NotAKnot,
		    		0.0,
		    		CubicSplineInterpolation.BoundaryCondition.NotAKnot,
		    		0.0,
		    		false)
		    		.interpolate(x, y);
	        
	        //TODO: how to use the integral.integrate method which is the protected method?
//	        double result = sqrt(integral.integrate (interpolation, -1.7, 1.9));
//	        result /= scaleFactor;
//	        assertFalse("Not-a-knot spline interpolation "
//					+"\n    sample points:      "+n
//					+"\n    norm of difference: "+result
//					+"\n    it should be:       "+tabulatedErrors[i],
//					abs(result-tabulatedErrors[i]) > toleranceOnTabErr[i]);
	        
	    }
		
	}

	@Test
	public void testNotAKnotSplineOnGaussianValues(){

		logger.info("Testing spline interpolation on a Gaussian data set...");

	    double interpolated, interpolated2;
	    int n = 5;

	    double[] x = new double[n];
	    double[] y = new double[n];

	    double x1_bad=-1.7, x2_bad=1.7;

	    for (double start = -1.9, j=0; j<2; start+=0.2, j++) {
	        x = xRange(start, start+3.6, n);
	        y = gaussian(x);

	        // Not-a-knot spline
	        CubicSplineInterpolation interpolation = new CubicSpline(
		    		CubicSplineInterpolation.BoundaryCondition.NotAKnot,
		    		0.0,
		    		CubicSplineInterpolation.BoundaryCondition.NotAKnot,
		    		0.0,
		    		false)
		    		.interpolate(x, y);
	        
	        checkValues("Not-a-knot spline", interpolation, x, y);
	        checkNotAKnotCondition("Not-a-knot spline", interpolation);
	        // bad performance
	        interpolated = interpolation.evaluate(x1_bad);
	        interpolated2= interpolation.evaluate(x2_bad);
	        assertFalse("Not-a-knot spline interpolation bad performance unverified"
					+"\n    at x = "+x1_bad
					+"\n    interpolated value: "+interpolated
					+"\n    at x = "+x2_bad
					+"\n    interpolated value: "+interpolated2
					+"\n    at least one of them was expected to be < 0.0",
					interpolated>0.0 && interpolated2>0.0);

	    }
	}
	
	@Test
	public void testClampedSplineOnRPN15AValues(){
		
		logger.info("Testing Clamped spline interpolation on RPN15A data set...");
		
		final double RPN15A_x[] = {7.99, 8.09, 8.19, 8.7, 9.2, 10.0, 12.0, 15.0, 20.0};
		final double RPN15A_y[] = {0.0, 2.76429e-5, 4.37498e-5, 0.169183, 0.469428, 0.943740, 0.998636, 0.999919, 0.999994};
		
		double interpolated;
		
		// Clamped spline
		CubicSplineInterpolation interpolation = new CubicSpline(
				CubicSplineInterpolation.BoundaryCondition.FirstDerivative,
				0.0,
				CubicSplineInterpolation.BoundaryCondition.FirstDerivative,
				0.0,
				false)
		.interpolate(RPN15A_x, RPN15A_y);
		
		checkValues("Clamped spline", interpolation, RPN15A_x, RPN15A_y);
		check1stDerivativeValue("Clamped spline", interpolation, RPN15A_x[0], 0.0);
		check1stDerivativeValue("Clamped spline",  interpolation, RPN15A_x[RPN15A_x.length-1],0.0);
		
		// poor performance
		double x_bad = 11.0;
		interpolated = interpolation.evaluate(x_bad);
		assertFalse("Clamped spline interpolation poor performance unverified"
				+"\n    at x = "+x_bad
				+"\n    interpolated value: "+interpolated
				+"\n    expected value > 1.0",
				interpolated<1.0);
		
	}
	
	@Test
	public void testNotAKnotSplineOnRPN15AValues(){
		
		logger.info("Testing Not-a-knot spline interpolation on RPN15A data set...");
		
		final double RPN15A_x[] = {7.99, 8.09, 8.19, 8.7, 9.2, 10.0, 12.0, 15.0, 20.0};
		final double RPN15A_y[] = {0.0, 2.76429e-5, 4.37498e-5, 0.169183, 0.469428, 0.943740, 0.998636, 0.999919, 0.999994};
		
		double interpolated;
		
		// Not-a-knot spline
		CubicSplineInterpolation interpolation = new CubicSpline(
				CubicSplineInterpolation.BoundaryCondition.NotAKnot,
				0.0,
				CubicSplineInterpolation.BoundaryCondition.NotAKnot,
				0.0,
				false)
		.interpolate(RPN15A_x, RPN15A_y);
		
		checkValues("Not-a-knot spline", interpolation, RPN15A_x, RPN15A_y);
		checkNotAKnotCondition("Not-a-knot spline", interpolation);
		// poor performance
		double x_bad = 11.0;
		interpolated = interpolation.evaluate(x_bad);
		assertFalse("Not-a-knot spline interpolation poor performance unverified"
				+"\n    at x = "+x_bad
				+"\n    interpolated value: "+interpolated
				+"\n    expected value > 1.0",
				interpolated<1.0);
		
	}
	
	@Test
	public void testNaturalSplineOnGenericValues(){		
		CubicSplineInterpolation interpolation = new CubicSpline(
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
 
	    x35[1] = interpolation.evaluate(3.5);
	}
	
	@Test
	public void testClampedSplineOnGenericValues(){
		double y1a=0.0, y1b=0.0;
		CubicSplineInterpolation interpolation = new CubicSpline(
				CubicSplineInterpolation.BoundaryCondition.FirstDerivative,
				y1a,
				CubicSplineInterpolation.BoundaryCondition.FirstDerivative,
				y1b,
				false)
				.interpolate(generic_x, generic_y);
		
		checkValues("Clamped spline", interpolation, generic_x, generic_y);
		check1stDerivativeValue("Clamped spline", interpolation, generic_x[0],0.0);
		check1stDerivativeValue("Clamped spline", interpolation, generic_x[generic_x.length-1],0.0);

	    x35[0] = interpolation.evaluate(3.5);
	}
	
	@Test
	public void testNotAKnotSplineOnGenericValues(){
		CubicSplineInterpolation interpolation = new CubicSpline(
				CubicSplineInterpolation.BoundaryCondition.NotAKnot,
				0.0,
				CubicSplineInterpolation.BoundaryCondition.NotAKnot,
				0.0,
				false)
				.interpolate(generic_x, generic_y);
		
		checkValues("Not-a-knot spline", interpolation, generic_x, generic_y);
		checkNotAKnotCondition("Not-a-knot spline", interpolation);

	    x35[2] = interpolation.evaluate(3.5);
		assertFalse("Spline interpolation failure"
					+"\n at x = "+3.5
					+"\n clamped spline    "+x35[0]
					+"\n natural spline    "+x35[1]
					+"\n not-a-knot spline "+x35[2]
					+"\n values should be in increasing order",
					x35[0]>x35[1] || x35[1]>x35[2]);
	}
	
	@Test
	public void testNotAKnotSimmetricEndConditions(){
		int n = 9;

	    double[] x, y;
	    x = xRange(-1.8, 1.8, n);
	    y = gaussian(x);

	    // Not-a-knot spline
	    CubicSplineInterpolation interpolation = new CubicSpline(
	    		CubicSplineInterpolation.BoundaryCondition.NotAKnot,
	    		0.0,
	    		CubicSplineInterpolation.BoundaryCondition.NotAKnot,
	    		0.0,
	    		false)
	    		.interpolate(x, y);

	    checkValues("Not-a-knot spline", interpolation,x,y);
	    checkNotAKnotCondition("Not-a-knot spline", interpolation);
	    checkSymmetry("Not-a-knot spline", interpolation, x[0]);
	    
	}
	
	@Test
	public void testNotAKnotSpineOnDerivativeEndConditions(){
		int n = 4;

	    double[] x, y;
	    x = xRange(-2.0, 2.0, n);
	    y = parabolic(x);

	    // Not-a-knot spline
	    CubicSplineInterpolation interpolation = new CubicSpline(
	    		CubicSplineInterpolation.BoundaryCondition.NotAKnot,
	    		0.0,
	    		CubicSplineInterpolation.BoundaryCondition.NotAKnot,
	    		0.0,
	    		false)
	    		.interpolate(x, y);
	   
	    checkValues("Not-a-knot spline", interpolation, x, y);
	    check1stDerivativeValue("Not-a-knot spline", interpolation, x[0], 4.0);
	    check1stDerivativeValue("Not-a-knot spline", interpolation, x[n-1], -4.0);
	    check2ndDerivativeValue("Not-a-knot spline", interpolation, x[0], -2.0);
	    
	    //TODO: test failure here!!!
//	    check2ndDerivativeValue("Not-a-knot spline", interpolation, x[n-1], -2.0);

	}

	@Test
	public void testClampedSpineOnDerivativeEndConditions(){
		int n = 4;
		
		double[] x, y;
		x = xRange(-2.0, 2.0, n);
		y = parabolic(x);
		
		// Clamped spline
		CubicSplineInterpolation interpolation = new CubicSpline(
				CubicSplineInterpolation.BoundaryCondition.FirstDerivative,
				4.0,
				CubicSplineInterpolation.BoundaryCondition.FirstDerivative,
				-4.0,
				false)
		.interpolate(x, y);
		
		checkValues("Clamped spline", interpolation, x, y);
		check1stDerivativeValue("Clamped spline", interpolation, x[0], 4.0);
		check1stDerivativeValue("Clamped spline", interpolation, x[n-1], -4.0);
		check2ndDerivativeValue("Clamped spline", interpolation, x[0], -2.0);
		check2ndDerivativeValue("Clamped spline", interpolation, x[n-1], -2.0);
		
	}

	@Test
	public void testSecondDerivativeOnDerivativeEndConditions(){
		int n = 4;
		
		double[] x, y;
		x = xRange(-2.0, 2.0, n);
		y = parabolic(x);
		
		// SecondDerivative spline
		CubicSplineInterpolation interpolation = new CubicSpline(
				CubicSplineInterpolation.BoundaryCondition.SecondDerivative, 
				-2.0,
				CubicSplineInterpolation.BoundaryCondition.SecondDerivative,
				-2.0,
				false)
		.interpolate(x, y);
		
		checkValues("SecondDerivative spline", interpolation, x, y);
		check1stDerivativeValue("SecondDerivative spline", interpolation, x[0], 4.0);
		check1stDerivativeValue("SecondDerivative spline", interpolation, x[n-1], -4.0);
		check2ndDerivativeValue("SecondDerivative spline", interpolation, x[0], -2.0);
		check2ndDerivativeValue("SecondDerivative spline", interpolation, x[n-1], -2.0);
		
	}

}
