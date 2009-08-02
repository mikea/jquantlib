/*
 Copyright (C) 2008 Dominik Holenstein
 
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
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  
 * @author Dominik Holenstein
 */

//TODO: working (Dominik)
public class FlatExtrapolation2DTest {
	
	private final static Logger logger = LoggerFactory.getLogger(FlatExtrapolation2DTest.class);

	private final double x[] = { 0.0, 1.0, 2.0, 3.0, 4.0 };
	private final double y[] = { 5.0, 4.0, 3.0, 2.0, 1.0 };
	private final Interpolation interpolation;
	private final int length;
	private final double tolerance;
	
	public FlatExtrapolation2DTest() {
		logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
		
		//FIXME values
		interpolation = null;
		length = 0;
		tolerance = 0;
	}
	
	
	@Test
	public void yourTestCaseHere() {
		//
		// It was failing because a *Test.java file is triggered automatically as a JUnit test file, which
		// must contain at least one @Test.
		// Also, static variables may cause failure to initialize test cases. This is another reason why
		// we need to avoid static data/code. 
		//
		System.out.println("*** TEST FAILED***");
	}
	
	
	
	
	
}
