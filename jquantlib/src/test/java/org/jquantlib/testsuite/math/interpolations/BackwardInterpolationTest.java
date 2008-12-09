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

import org.jquantlib.math.interpolations.Interpolation;
import org.jquantlib.math.interpolations.factories.BackwardFlat;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Daniel Kong
 **/

public class BackwardInterpolationTest {
	
	private final static Logger logger = LoggerFactory.getLogger(BackwardInterpolationTest.class);
	
	private static final double x[] = { 0.0, 1.0, 2.0, 3.0, 4.0 };
	private static final double y[] = { 5.0, 4.0, 3.0, 2.0, 1.0 };
	private static Interpolation interpolation;
	private static int length;
	private static double tolerance;
	
	public BackwardInterpolationTest() {
		logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
	@BeforeClass
	public static void setUpBackwardInterpolation(){
		logger.info("\n\n::::: Testing backward-flat interpolation... :::::");

		interpolation = new BackwardFlat().interpolate(x, y);
		interpolation.reload();		  
	    length = x.length;
	    tolerance = 1.0e-12;
	}
	
	@Test
	public void checkAtOriginalPoints(){
		for(int i=0; i<length; i++){
			double d = x[i];
			double calculated = interpolation.evaluate(d);
			double expected = y[i];
			assertFalse("failed to reproduce "+i+" datum"
						+"\n expected:     "+expected
						+"\n calculated:   "+calculated
						+"\n error:        "+abs(expected-calculated),
						abs(expected-calculated) > tolerance);
		}
	}
	
	
	//TODO: check the locate() method in AbstractInterpolation which leads to java.lang.ArrayIndexOutOfBoundsException. 
//	@Test
//	public void checkAtMiddlePoints(){
//		for(int i=0; i<length-1; i++){
//			double d = (x[i]+x[i+1])/2;
//			double calculated = interpolation.evaluate(d);
//			double expected = y[i+1];
//			
//			assertFalse("failed to interpolate correctly at "+d
//						+"\n expected:     "+expected
//						+"\n calculated:   "+calculated
//						+"\n error:        "+abs(expected-calculated),
//						abs(expected-calculated) > tolerance);
//		}
//	}
	
	@Test
	public void checkOutsideOriginalRange(){
		interpolation.enableExtrapolation();
		double d = x[0] - 0.5;
		double calculated = interpolation.evaluate(d);
		double expected = y[0];
		assertFalse("failed to extrapolate correctly at "+d
					+"\n expected:     "+expected
					+"\n calculated:   "+calculated
					+"\n error:        "+abs(expected-calculated),
					abs(expected-calculated) > tolerance);
		d= x[length-1]+0.5;
		calculated = interpolation.evaluate(d);
		expected = y[length-1];
		assertFalse("failed to extrapolate correctly at "+d
				    +"\n expected:     "+expected
			    	+"\n calculated:   "+calculated
			    	+"\n error:        "+abs(expected-calculated),
			    	abs(expected-calculated) > tolerance);
		
	}
	
	@Test
	public void checkPrimitiveAtOriginalPoints(){
		double calculated = interpolation.primitive(x[0]);
		double expected = 0.0;
		assertFalse("failed to calculate primitive at "+x[0]
				    +"\n expected:     "+expected
			    	+"\n calculated:   "+calculated
			    	+"\n error:        "+abs(expected-calculated),
			    	abs(expected-calculated) > tolerance);
		double sum = 0.0;
		for(int i=1; i<length; i++){
			sum += (x[i]-x[i-1])*y[i];
			calculated = interpolation.primitive(x[i]);
			expected=sum;
			assertFalse("failed to calculate primitive at "+x[i]
			            +"\n expected:     "+expected
			            +"\n calculated:   "+calculated
			            +"\n error:        "+abs(expected-calculated),
			            abs(expected-calculated) > tolerance);
		}
	}
	
	//TODO: check the locate() method in AbstractInterpolation which leads to java.lang.ArrayIndexOutOfBoundsException. 
//	@Test
//	public void checkPrimitiveAtMiddlePoints(){
//		double sum = 0.0;
//		for(int i=0; i<length-1; i++){
//			double d = (x[i]+x[i+1])/2;
//			sum += (x[i+1]-x[i])*y[i+1]/2;
//			double calculated = interpolation.primitive(d);
//			double expected=sum;
//			sum += (x[i+1]-x[i])*y[i+1]/2;
//			assertFalse("failed to calculate primitive at "+x[i]
//			            +"\n expected:     "+expected
//			            +"\n calculated:   "+calculated
//			            +"\n error:        "+abs(expected-calculated),
//			            abs(expected-calculated) > tolerance);
//		}
//	}
		
}
