/*
 Copyright (C) 2007 Richard Gomes

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquantlib-dev@lists.sf.net>. The license is also available online at
 <http://jquantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the originating copyright notice follows below.
 */

package org.jquantlib.examples;


/**
 * This class proves that floating point rounding errors may lead to bugs difficult to find.
 *
 * @see org.jquantlib.lang.Number
 * 
 * @author Richard Gomes
 */
public class Main implements Runnable {

	/**
	 * This method calculates {@latex$ x = 0.1 \times 3 } and proves that {@latex$ x \ne 0.3 }
	 */
	public void run() {
		double x = 0.0;
		x += 0.1;
		x += 0.1;
		x += 0.1;
		if (x!=0.3) {
			throw new ArithmeticException("Floating point rounding error: 0.1 * 3 != 0.3");
		}
	}
	
	
	public static void main(String[] args) {
		Main m = new Main();
		m.run();
	}

}
