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


import static org.junit.Assert.fail;
import static java.lang.Math.exp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jquantlib.math.interpolations.Interpolation;
import org.jquantlib.math.interpolations.LinearInterpolation;
import org.junit.Test;

/**
 * @author Daniel Kong
 * @author Richard Gomes
 **/

public abstract class InterpolationTestBase {

    private final static Logger logger = LoggerFactory.getLogger(InterpolationTestBase.class);

	public InterpolationTestBase() {
		logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
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


	
	
	
	
	
	
	@Test
	public void testAsFunctor() {

	    logger.info("Testing use of interpolations as functors...");

	    final double x[] = { 0.0, 1.0, 2.0, 3.0, 4.0 };
	    final double y[] = { 5.0, 4.0, 3.0, 2.0, 1.0 };

	    Interpolation f = LinearInterpolation.getInterpolator().interpolate(x, y);
	    f.reload();

	    final double x2[] = { -2.0, -1.0, 0.0, 1.0, 3.0, 4.0, 5.0, 6.0, 7.0 };
	    int len = x2.length;
	    double[] y2 = new double[len];
	    double tolerance = 1.0e-12;

	    // case 1: extrapolation not allowed
	    
	    try {
	    	for (int i=0; i<len; i++) {
		    	y2[i] = f.evaluate(x2[i]);
	    	}
	    	fail("failed to throw exception when trying to extrapolate");
	    } catch (IllegalArgumentException e1) {
	        // This exception was expected. It's OK! Do nothing.
	    } catch (Exception e2) {
	    	fail("Unexpected exception");
	    	e2.printStackTrace();
	    }
	    

	    // case 2: enable extrapolation
	    f.enableExtrapolation();
    	for (int i=0; i<len; i++) {
    		y2[i] = f.evaluate(x2[i]);
    	}
	    for (int i=0; i<len; i++) {
	        double expected = 5.0-x2[i];
	        if (Math.abs(y2[i]-expected) > tolerance) {
	            StringBuilder sb = new StringBuilder();
	            sb.append("failed to reproduce ").append(i+1).append("o. expected datum");
	            sb.append("\n    expected:   ").append(expected);
	            sb.append("\n    calculated: ").append(y2[i]);
	            sb.append("\n    error:      ").append(Math.abs(y2[i]-expected));
	            
	            if (Math.abs(y2[i]-expected) > tolerance)
	            	fail("failed to reproduce " + (i+1) + "o. expected datum\n"
	            			+ "    expected:   " + expected + "\n"
	            			+ "    calculated: " + y2[i] + "\n"
		                    + "    error:      " + Math.abs(y2[i]-expected) );
	        }
	    }
	}


}
