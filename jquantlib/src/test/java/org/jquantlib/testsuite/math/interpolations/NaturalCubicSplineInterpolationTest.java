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

import static org.junit.Assert.assertFalse;

import org.jquantlib.math.interpolations.CubicSplineInterpolation;
import org.jquantlib.math.interpolations.factories.NaturalCubicSpline;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Daniel Kong
 **/

public class NaturalCubicSplineInterpolationTest extends InterpolationTestBase {

	private final static Logger logger = LoggerFactory.getLogger(NaturalCubicSplineInterpolationTest.class);

	public NaturalCubicSplineInterpolationTest() {
		logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	

	//TODO: check the locate() method in AbstractInterpolation which leads to java.lang.ArrayIndexOutOfBoundsException. 
	//@Ignore("Not Ready to Run")
	@Test
	public void testNaturalSplineOnRPN15AValues(){
		
		logger.info("Testing Natural Spline interpolation on RPN15A data set...");

	    final double RPN15A_x[] = {7.99, 8.09, 8.19, 8.7, 9.2, 10.0, 12.0, 15.0, 20.0};
	    final double RPN15A_y[] = {0.0, 2.76429e-5, 4.37498e-5, 0.169183, 0.469428, 0.943740, 0.998636, 0.999919, 0.999994};

	    double interpolated;

	    // Natural spline
	    CubicSplineInterpolation interpolation = new NaturalCubicSpline().interpolate(RPN15A_x, RPN15A_y);
	    
	    checkValues("Natural spline", interpolation, RPN15A_x, RPN15A_y);
	    check2ndDerivativeValue("Natural spline", interpolation, RPN15A_x[0], 0.0);

	    check2ndDerivativeValue("Natural spline", interpolation, RPN15A_x[RPN15A_x.length-1],0.0);
	                            
	    // poor performance
	    double x_bad = 11.0;
	    interpolated = interpolation.evaluate(x_bad);
	    assertFalse("Natural spline interpolation poor performance unverified"
				+"\n    at x = "+x_bad
				+"\n    interpolated value: "+interpolated
				+"\n    expected value > 1.0",
				interpolated<1.0);

	}
	
	
}
