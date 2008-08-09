/*
 Copyright (C) 2007 Richard Gomes
 Copyright (C) 2008 Q. Boiler

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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jquantlib.math.Array;
import org.jquantlib.math.Matrix;

import org.junit.Test;

/**
 * @author <Q. Boiler>
 */
public class MatrixTest {

	public MatrixTest() {
		System.out.println("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}

	@Test
	public void testPrintMatrix() {
		
		Matrix matrix = new Matrix(3,3,42.42);
		
		System.out.print(matrix.toString());
		
	//	if (expected!=realized)
	//		fail("1Millionth Prime: Expected: " + expected + " realized: " + realized);

	}
	@Test
	public void testArrayCrosProduct() {
		try {
			Array a1 = new Array(6, 1, 5);
			Array a2 = new Array(6, 23.23, 2.5);
			Matrix matrix = new Matrix();
			matrix = matrix.outerProduct(a1, a2);

			System.out.print(matrix.toString());
			Thread.sleep(7000);
		} catch (InterruptedException ex) {
			Logger.getLogger(MatrixTest.class.getName()).log(Level.SEVERE, null, ex);
		}

	}
}
