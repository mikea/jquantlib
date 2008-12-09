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
	private static Interpolation interpolation;

	
	public CubicSplineInterpolationTest() {
		logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
		logger.info("\n\n::::: Testing spline interpolation on generic values... :::::");
	}
	
	
	
	//TODO
	@Test
	public void testNaturalSpline(){
		
	}
	
	
//	void InterpolationTest::testSplineOnGenericValues() {
//	BOOST_MESSAGE("Testing spline interpolation on generic values...");
//
//    const Real generic_x[] = { 0.0, 1.0, 3.0, 4.0 };
//    const Real generic_y[] = { 0.0, 0.0, 2.0, 2.0 };
//    const Real generic_natural_y2[] = { 0.0, 1.5, -1.5, 0.0 };
//
//    Real interpolated, error;
//    Size i, n = LENGTH(generic_x);
//    std::vector<Real> x35(3);
//
//    // Natural spline
//    CubicSpline f(BEGIN(generic_x), END(generic_x),
//                  BEGIN(generic_y),
//                  CubicSpline::SecondDerivative,
//                  generic_natural_y2[0],
//                  CubicSpline::SecondDerivative,
//                  generic_natural_y2[n-1],
//                  false);
//    f.update();
//    checkValues("Natural spline", f,
//                BEGIN(generic_x), END(generic_x), BEGIN(generic_y));
//    // cached second derivative
//    for (i=0; i<n; i++) {
//        interpolated = f.secondDerivative(generic_x[i]);
//        error = interpolated - generic_natural_y2[i];
//        if (std::fabs(error)>3e-16) {
//            BOOST_ERROR("Natural spline interpolation "
//                        << "second derivative failed at x=" << generic_x[i]
//                        << "\ninterpolated value: " << interpolated
//                        << "\nexpected value:     " << generic_natural_y2[i]
//                        << "\nerror:              " << error);
//        }
//    }
//    x35[1] = f(3.5);
//
//
//    // Clamped spline
//    Real y1a = 0.0, y1b = 0.0;
//    f = CubicSpline(BEGIN(generic_x), END(generic_x), BEGIN(generic_y),
//                    CubicSpline::FirstDerivative, y1a,
//                    CubicSpline::FirstDerivative, y1b,
//                    false);
//    f.update();
//    checkValues("Clamped spline", f,
//                BEGIN(generic_x), END(generic_x), BEGIN(generic_y));
//    check1stDerivativeValue("Clamped spline", f,
//                            *BEGIN(generic_x), 0.0);
//    check1stDerivativeValue("Clamped spline", f,
//                            *(END(generic_x)-1), 0.0);
//    x35[0] = f(3.5);
//
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
