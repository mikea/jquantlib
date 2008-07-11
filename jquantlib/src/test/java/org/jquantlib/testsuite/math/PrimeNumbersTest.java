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

import org.jquantlib.math.PrimeNumbers;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * @author <Q. Boiler>
 */
public class PrimeNumbersTest {

	public PrimeNumbersTest() {
		System.out.println("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}

	@Test
	public void testPrefetchOneMillion() {
		
		PrimeNumbers primes = new PrimeNumbers();
		
		//  From http://primes.utm.edu/lists/small/millions/
		long expected = 15485863L;
		// in a 0 indexed array the 1 Millionth element is at 1000000 -1
		long realized = primes.get(1000000-1);

		if (expected!=realized)
			fail("1Millionth Prime: Expected: " + expected + " realized: " + realized);

	}
}
