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

import org.jquantlib.math.Constants;
import org.jquantlib.math.UnaryFunctionDouble;
import org.jquantlib.math.integrals.GaussKronrodPatterson;
import org.jquantlib.math.integrals.Integrator;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dominik Holenstein
 * @author Richard Gomes
 */
public class GaussKonrodPattersonIntegratorTest {

    private final static Logger logger = LoggerFactory.getLogger(GaussKonrodPattersonIntegratorTest.class);

	public GaussKonrodPattersonIntegratorTest() {
		logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
	@Test
	public void testPolynomials() {
		checkSingleTabulated(new ConstantFunction(), "f(x)=1",   2.0,     1.0e-13);
		checkSingleTabulated(new LinearFunction(),   "f(x)=x",   0.0,     1.0e-13);
		checkSingleTabulated(new SquareFunction(),   "f(x)=x^2", 2.0/3.0, 1.0e-13);
		checkSingleTabulated(new CubeFunction(),     "f(x)=x^3", 0.0,     1.0e-13);
		checkSingleTabulated(new FourthFunction(),   "f(x)=x^4", 2.0/5.0, 1.0e-13);		
	}
	
	private void checkSingleTabulated(UnaryFunctionDouble f, String tag,
	                         double expected, double tolerance) {
		
		Integrator quad = new GaussKronrodPatterson();
	    double realised = quad.evaluate(f,-1,1);
	        
        if (Math.abs(realised-expected) > tolerance)
        	fail(" integrating " + tag + "\n"
                    + "    realised: " + realised + "\n"
                    + "    expected: " + expected);
	    
	}
	
	@Test
	public void testExp() {
		UnaryFunctionDouble exp = new UnaryFunctionDouble() {
			public double evaluate(double x) {
				return Math.exp(x);
			}
		};
		
		Integrator quad = new GaussKronrodPatterson(0, 1.1*Constants.QL_EPSILON);
		
		double realised = quad.evaluate(exp, 0, 6);
		double expected = Math.exp(6) - 1.0;
		double tolerance = 1.0e-10;
		
		if (Math.abs(realised-expected)>tolerance) {
			fail("Expected: " + expected + " Realised: " + realised);
		}
	}

}
