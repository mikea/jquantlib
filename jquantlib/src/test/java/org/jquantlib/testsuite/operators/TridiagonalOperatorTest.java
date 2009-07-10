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

/**
 * 
 * @author Tim Swetonic
 *
 */

package org.jquantlib.testsuite.operators;

import static org.junit.Assert.fail;

import org.jquantlib.math.Array;
import org.jquantlib.methods.finitedifferences.TridiagonalOperator;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TridiagonalOperatorTest {
	
    private final static Logger logger = LoggerFactory.getLogger(TridiagonalOperatorTest.class);

	public TridiagonalOperatorTest() {
		logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
    @Test
	public void testSolveFor() {
		try {
			Array low = new Array(2);
			Array mid = new Array(3);
			Array high = new Array(2);
			
			low.set(0, 11);
			low.set(1, 1);

			mid.set(0, 12);
			mid.set(1, 10);
			mid.set(2, 3);

			high.set(0, 7);
			high.set(1, 9);

			
			TridiagonalOperator t = new TridiagonalOperator(low, mid, high);
			
			Array rhs = new Array(3);
			rhs.set(0, 7);
			rhs.set(1, 8);
			rhs.set(2, 7);
			
			Array solved = t.solveFor(rhs);
			
			Array expected = new Array(new double[] {20.0, -33.285714285714285, 13.428571428571429 });
			//Array expectedArray = new Array(expected);
			if(!solved.equals(expected))
	        	fail(" TridiagonalOperator expected:\n"
	                    + " { 20, -33.2857, 13.4286 } "   + "\n"
	                    + " got: "   + solved.get(0) + "\n"
	                    + "  "   + solved.get(1) + "\n"
	                    + "  " + solved.get(2) );
			
			} catch(Exception e) {
				logger.info(e.getMessage());
		}
	}
}
