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
 * Integral of a one-dimensional function
 * 
 * <p>
 * Given a target accuracy {@latex$ \epsilon }, the integral of a function {@latex$ f } between {@latex$ a } and {@latex$ b }
 * is calculated by means of the trapezoid formula
 * 
 * {@latex[
 *    \int_{a}^{b} f \mathrm{d}x = \frac{1}{2} f(x_{0}) + f(x_{1}) + f(x_{2}) + \dots + f(x_{N-1}) + \frac{1}{2} f(x_{N}) 
 * }
 * 
 * where {@latex$ x_0 = a }, {@latex$ x_N = b }, and {@latex$ x_i = a+i \Delta x } with {@latex$ \Delta x = (b-a)/N }.
 * The number {@latex$ N } of intervals is repeatedly increased until the target accuracy is reached. \test the correctness of the
 * result is tested by checking it against known good values.
 * 
 * @author Dominik Holenstein
 * @author Richard Gomes
 */
public class TrapezoidIntegral extends Integrator {
	
	static public enum Method { Default, MidPoint };
	
	//
	// protected fields
	//
	
	protected Method method;
	
	//
	// public constructors
	//
	
	public TrapezoidIntegral(double accuracy) {
		this(accuracy, 1000); // FIXME: code review :: what's the default "maxEvaluations" value???
	}
		
	public TrapezoidIntegral(double accuracy, int maxEvaluations) {
		this(accuracy, TrapezoidIntegral.Method.Default, maxEvaluations);
	}
		
	public TrapezoidIntegral(double accuracy, final TrapezoidIntegral.Method method, int maxEvaluations) {
		super(accuracy, maxEvaluations);
		this.method = method;
	}


	//
	// protected final methods
	//
	
	protected final double defaultIteration(final UnaryFunctionDouble f, double a, double b, double I, int N) /* @ReadOnly */ {
	    double sum = 0.0;
	    double dx = (b-a)/N;
	    double x = a + dx/2.0;
	    for (int i=0; i<N; x += dx, ++i) {
	    	sum += f.evaluate(x);
	    }
	    return (I + dx*sum)/2.0;
	}

	protected final double midPointIteration(final UnaryFunctionDouble f, double a, double b, double I, double N) /* @ReadOnly */ {
		double sum = 0.0;
	    double dx = (b-a)/N;
	    double x = a + dx/6.0;
	    double D = 2.0*dx/3.0;
	    for (int i=0; i<N; x += dx, ++i)
	    	sum += f.evaluate(x) + f.evaluate(x+D);
	    return (I + dx*sum)/3.0;
	}
	

	//
	// protected virtual methods
	//
	
	// TODO: code review:: why this method is virtual??
	protected Method getMethod() /* @ReadOnly */ {
		return method;

		// FIXME: code review :: C++ code	
		//		protected:
		//	        // calculation parameters
		//	        Method method() const { return method_; }
		//	        Method& method() { return method_; }
	}

	@Override
	protected double integrate(final UnaryFunctionDouble f, double a, double b) /* @ReadOnly */ {

	    // start from the coarsest trapezoid...
	    int N = 1;
	    double I = (f.evaluate(a)+f.evaluate(b))*(b-a)/2.0;
	    double newI;
	      
	    // ...and refine it
	    int i = 1;
	    
	    do {
		switch (method) {
	    case MidPoint:
		newI = midPointIteration(f, a, b, I, N);
		N *= 3;
		break;
	    default:
		newI = defaultIteration(f, a, b, I, N);
		N *= 2;
		break;
	    }
	    // good enough? Also, don't run away immediately
	    if (Math.abs(I-newI) <= getAbsoluteAccuracy() && i > 5) {
		return newI; // ok, exit
	    }
		    
	    // oh well. Another step.
	    I = newI;
	    i++;
	 } while (i < this.getMaxEvaluations());
	 throw new ArithmeticException("max number of iterations reached");
	}
	        
}