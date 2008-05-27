/*
 Copyright (C) 2008 Richard Gomes

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

package org.jquantlib.math.integrals;

import org.jquantlib.math.UnaryFunctionDouble;
import org.jquantlib.math.integrals.Integrator;

/**
 * 
 * @author Dominik Holenstein
 *
 */

//Integral of a one-dimensional function
//Test the correctness of the result is tested by checking it
//against known good values.

// TODO: Add test case.  
public class SimpsonIntegral extends TrapezoidIntegral implements Integrator {
	
	public SimpsonIntegral (double accuracy, int NumberOfEvaluations) {
		super(accuracy,NumberOfEvaluations, Method.Default);
	}
	/*
    class SimpsonIntegral : public TrapezoidIntegral {
      public:
        SimpsonIntegral(Real accuracy,
                        Size maxIterations = Null<Size>())
        : TrapezoidIntegral(accuracy, Default, maxIterations) {}
      
        	
        } */
      public double integrate (UnaryFunctionDouble f, double a, double b) {
        
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
           if (Math.abs(adjI-newAdjI) <= getAccuracy() && i > 5)
             // ok, exit
             return newAdjI;
             // oh well. Another step.
             I = newI;
             adjI = newAdjI;
             i++;
         } while (i < getNumberOfEvaluations());
         throw new ArithmeticException("max number of iterations reached");
    }
}
