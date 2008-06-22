/*
 Copyright (C) 2007 Richard Gomes

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

package org.jquantlib.testsuite.math.integrals;

import static org.junit.Assert.fail;

import org.jquantlib.math.UnaryFunctionDouble;
import org.jquantlib.math.integrals.TabulatedGaussLegendre;
import org.junit.Test;

/**
 * @author <Richard Gomes>
 */
public class TabulatedGaussLegendreTest {

	public TabulatedGaussLegendreTest() {
		System.out.println("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
	@Test
	public void testPolynomials() {
		checkSingleTabulated(new ConstantFunction(), "f(x)=1", 2.0, 1.0e-13);
		checkSingleTabulated(new LinearFunction(), "f(x)=x", 0.0, 1.0e-13);
		checkSingleTabulated(new SquareFunction(), "f(x)=x^2", 2.0/3.0, 1.0e-13);
		checkSingleTabulated(new CubeFunction(), "f(x)=x^3", 0.0, 1.0e-13);
		checkSingleTabulated(new FourthFunction(), "f(x)=x^4", 2.0/5.0, 1.0e-13);		
	}
	
	public void checkSingleTabulated(UnaryFunctionDouble f, String tag,
	                         double expected, double tolerance) {
	    final int order[] = { 6, 7, 12, 20 };
	    TabulatedGaussLegendre quad = new TabulatedGaussLegendre();
	    for (int i=0; i<order.length; i++) {
	        quad.setOrder(order[i]);
	        double realised = quad.evaluate(f);
	        if (Math.abs(realised-expected) > tolerance)
            fail(" integrating " + tag + "\n"
                        + "    order " + order[i] + "\n"
                        + "    realised: " + realised + "\n"
                        + "    expected: " + expected);
	    }
	}
}
