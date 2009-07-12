/*
 Copyright (C) 2009 Ueli Hofstetter

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
package org.jquantlib.testsuite.math;

import static org.junit.Assert.fail;

import org.jquantlib.math.Array;
import org.jquantlib.math.SampledCurve;
import org.jquantlib.math.UnaryFunctionDouble;
import org.jquantlib.math.functions.Sqr;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TODO: SampledCurve implementation not finished yet.
public class SampledCurveTest {
	
	private final static Logger logger = LoggerFactory.getLogger(SampledCurveTest.class);	

	public SampledCurveTest() {
		logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
	@Test
	public void testConstruction() {
		logger.info("Testing sampled curve construction...");

		final SampledCurve curve = new SampledCurve(BoundedGrid(-10.0, 10.0, 100));
		final UnaryFunctionDouble f2 = new Sqr();
		
        curve.sample(f2);
        if (Math.abs(curve.value(0) - 100.0) > 1e-5) {
            fail("function sampling failed at 0");
        }

        curve.values().set(0, 2);
        if (Math.abs(curve.value(0) - 2.0) > 1e-5) {
            fail("curve value setting failed");
        }

        curve.values().set(1, 3.0);
        if (Math.abs(curve.value(1) - 3.0) > 1e-5) {
            fail("curve value grid failed");
        }

        curve.shiftGrid(10.0);
        if (Math.abs(curve.gridValue(0) - 0.0) > 1e-5) {
            fail("sample curve shift grid failed");
        }

        if (Math.abs(curve.value(0) - 2.0) > 1e-5) {
            fail("sample curve shift grid - value failed");
        }

        SampledCurve testCurve;
        double tolerance = 1.0e-2;

        testCurve = curve.clone();
        testCurve.sample(f2);
        testCurve.regrid(BoundedGrid(0.0, 20.0, 200));
        for (int i=0; i < testCurve.size(); i++) {
            double grid  = testCurve.gridValue(i);
            double value = testCurve.value(i);
            double expected = f2.evaluate(grid);
            if (Math.abs(value - expected) > tolerance) {
                fail("sample curve regriding failed");
            }
        }
        
        
        //TODO: study how this test case could be performed
//        testCurve = curve.clone();
//        testCurve.regrid(BoundedGrid(0.0, 20.0, 200), f2);
//        for (int i=0; i < testCurve.size(); i++) {
//            double grid  = testCurve.gridValue(i);
//            double value = testCurve.value(i);
//            double expected = f2.evaluate(grid);
//            if (Math.abs(value - expected) > tolerance) {
//                fail("sample curve regriding failed");
//            }
//        }
        
	}
	
	private Array BoundedGrid(double xMin, double xMax, int steps) {
		Array result = new Array(steps+1);
		
		double x=xMin, dx=(xMax-xMin)/steps;
		for (int i=0; i<steps+1; i++, x+=dx)
			result.set(i, x);
		
		return result;
	}
	
}