/*
 Copyright (C) 2008 Ueli Hofstetter

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
import static org.junit.Assert.fail;

import org.jquantlib.math.interpolations.Interpolation2D;
import org.jquantlib.math.interpolations.factories.Bilinear;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BilinearInterpolationTest {
private final static Logger logger = LoggerFactory.getLogger(BilinearInterpolationTest.class);
	
	private final double x[] = { 0.0, 1.0, 2.0, 3.0, 4.0 };
	private final double y[] = { 0.0, 1.0, 2.0, 3.0, 4.0 };
	
	private final double zz[][] = new double[x.length][y.length];
	
	private final double x_test[] = { -0.5 ,0, 0.5, 1.5, 2.5, 3.5, 4.5 };
	private final double y_test[] = { -0.5 ,0, 0.5, 1.5, 2.5, 3.5, 4.5 };
	
	private final Interpolation2D interpolation2d;
	private final double tolerance;
	
	public BilinearInterpolationTest() {
		logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
		logger.info("\n\n::::: Testing use of interpolations as functors... :::::");
		//fill zz using f(x,y) =  x + y;
		for(int i = 0; i<x.length; i++){
			for(int ii = 0; ii<y.length; ii++){
				zz[i][ii] = x[i] + y[ii]; 
			}
		}
		interpolation2d = new Bilinear().interpolate(x, y, zz);
		interpolation2d.reload();		  
	    tolerance = 1.0e-12;		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentExceptionWithoutEnableExtrapolation(){
		for (int i_x=0; i_x<x_test.length; i_x++) {
    		for(int i_y = 0; i_y<y_test.length; i_y++){
    			interpolation2d.evaluate(x_test[i_x],y_test[i_y]);
    		}
    	}
	}
	
	@Test
	public void testEnableExtrapolation(){
		interpolation2d.enableExtrapolation();		
    	for (int i_x=0; i_x<x_test.length; i_x++) {
    		for(int i_y = 0; i_y<y_test.length; i_y++){
    			double interpolated = interpolation2d.evaluate(x_test[i_x],y_test[i_y]);
    			double expected = x_test[i_x] + y_test[i_y];		
    			if(abs(interpolated-expected) > tolerance){
    				fail("failed to interpolate value at x = " + x_test[i_x]  + ", y = " + y_test[i_y]);
    			}	
    		}
    	}
    }
}
	
	
	


