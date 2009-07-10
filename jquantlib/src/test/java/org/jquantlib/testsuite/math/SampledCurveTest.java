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
		double expected = 100.0;
		if(Math.abs(curve.value(0)-expected)> 1e-5){
			fail("function sampling failed at 0");
		}

		curve.values().set(0, 2);
	    if (Math.abs(curve.value(0) - 2.0) > 1e-5) {
	        fail("curve value setting failed");
	    }

	    Array value = curve.values();
	    value.set(1, 3.0);
	    if (Math.abs(curve.value(1) - 3.0) > 1e-5) {
	        fail("curve value grid failed");
	    }

	    curve.shiftGrid(10.0);
	    if (Math.abs(curve.gridValue(0) - 0.0) > 1e-5) {
	        //BOOST_ERROR("sample curve shift grid failed");
	    }
	    
	    
	    if (Math.abs(curve.value(0) - 2.0) > 1e-5) {
            fail("sample curve shift grid - value failed");
	    }

	    curve.sample(f2);
	    curve.regrid(BoundedGrid(0.0,20.0,200));
	    double tolerance = 1.0e-2;
	    for (int i=0; i < curve.size(); i++) {
	        double grid = curve.gridValue(i);
	        expected = f2.evaluate(grid);
	        if (Math.abs(curve.value(i) - expected) > tolerance) {
	            fail("sample curve regriding failed");
	        }
	    }
		
	}
	
	private Array BoundedGrid(double xMin, double xMax, int steps) {
		Array result = new Array(steps+1);
		
		double x=xMin, dx=(xMax-xMin)/steps;
		for (int i=0; i<steps+1; i++, x+=dx)
			result.set(i, x);
		
		return result;
	}
	
}