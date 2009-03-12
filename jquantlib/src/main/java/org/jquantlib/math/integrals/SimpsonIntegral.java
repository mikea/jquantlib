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

/*
 Copyright (C) 2003 Roman Gitlin
 Copyright (C) 2003 StatPro Italia srl

 This file is part of QuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://quantlib.org/

 QuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <quantlib-dev@lists.sf.net>. The license is also available online at
 <http://quantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
*/

package org.jquantlib.math.integrals;

import org.jquantlib.math.UnaryFunctionDouble;

/**
 * Integral of a one-dimensional function using Simpson formula
 * 
 * @author Dominik Holenstein
 */
//TODO: Add test case.  
//TEST the correctness of the result is tested by checking it against known good values.
public class SimpsonIntegral extends TrapezoidIntegral {
	
	
	//
	// public constructors
	//
	
	public SimpsonIntegral(double accuracy) {
		super(accuracy); // FIXME: code review :: what's the default "maxEvaluations" value???
		if (System.getProperty("EXPERIMENTAL")==null) {
		    throw new UnsupportedOperationException("Work in progress");
		 }
	}
	
	public SimpsonIntegral (final double accuracy, final int maxIterations) {
		super(accuracy, Method.Default, maxIterations);
		if (System.getProperty("EXPERIMENTAL")==null) {
		    throw new UnsupportedOperationException("Work in progress");
		  }

	}
	
	//
	// private methods
	//
	
	@Override
	protected Method getMethod() {
		// This method reduces visibility of an ancestor method
		throw new UnsupportedOperationException(); // FIXME: message
	}
	
	//
	// protected virtual methods
	//
	
	@Override
    protected double integrate (UnaryFunctionDouble f, double a, double b) {
        // start from the coarsest trapezoid...
        int N = 1;
        double I = (f.evaluate(a)+f.evaluate(b))*(b-a)/2.0, newI;
        double adjI = I, newAdjI;
        // ...and refine it
        int i = 1;
        do {
           newI = defaultIteration(f,a,b,I,N);
           N *= 2;
           newAdjI = (4.0*newI-I)/3.0;
           
           // good enough? Also, don't run away immediately
           if (Math.abs(adjI-newAdjI) <= getAbsoluteAccuracy() && i > 5)
             // ok, exit
             return newAdjI;
           
           // oh well. Another step.
           I = newI;
           adjI = newAdjI;
           i++;
         } while (i < getMaxEvaluations());
         throw new ArithmeticException("max number of iterations reached");
    }
	
}
