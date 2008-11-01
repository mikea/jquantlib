/*
 Copyright (C) 2008 Richard Gomes

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

package org.jquantlib.testsuite.math.solvers1D;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Test;

/**
 * 
 * @author Dominik Holenstein
 *
 */

/* 
 * Test not implemented yet.
 */

//TODO: Write FalsePosition test case.
public class FalsePositionTest {
	
    private final static Logger logger = LoggerFactory.getLogger(FalsePositionTest.class);

	public FalsePositionTest() {
		logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
	@Test
	public void fakeTest() {
		// only intended to avoid failure during unit tests
		logger.error("***** TEST FAILED *****");
	}
	
}
