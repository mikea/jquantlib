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

/**
 * 
 * @author Dominik Holenstein
 *
 */

public class TrapezoidIntegral implements Integrator {
	
	static public enum Method { Default, MidPoint };
	private Method method_;
	private int numberOfEvaluations_;	
	private double accuracy_;
	
		
	public TrapezoidIntegral(double accuracy, int NumberOfEvaluations, TrapezoidIntegral.Method method) {
		accuracy_ = accuracy;
		numberOfEvaluations_ = NumberOfEvaluations;
		method_ = method;
	}
		  	        
	public double integrate(UnaryFunctionDouble f, double a, double b) {

		// start from the coarsest trapezoid...
		int N = 1;
		double I = (f.evaluate(a)+f.evaluate(b))*(b-a)/2.0, newI;
	      
		// ...and refine it
		int i = 1;
	    do {
	    	switch (method_) {
	        	case MidPoint:
	        		newI = midPointIteration(f,a,b,I,N);
	                N *= 3;
	                break;
	            default:
	            	newI = defaultIteration(f,a,b,I,N);
	                N *= 2;
	                break;
	    	}
	            
	    // good enough? Also, don't run away immediately
	    if (Math.abs(I-newI) <= getAccuracy() && i > 5)
	    	// ok, exit
	        return newI;
	        // oh well. Another step.
	        I = newI;
	        i++;
	    } while (i < getNumberOfEvaluations());
	    throw new ArithmeticException("max number of iterations reached");
	}
	        
	double defaultIteration(UnaryFunctionDouble f, double a, double b, double I, int N)  {
		double sum = 0.0;
	    double dx = (b-a)/N;
	    double x = a + dx/2.0;
	    for (int i=0; i<N; x += dx, ++i)
	    	sum += f.evaluate(x);
	    return (I + dx*sum)/2.0;
	}

	double midPointIteration(UnaryFunctionDouble f, double a, double b, double I, double N) {
		double sum = 0.0;
	    double dx = (b-a)/N;
	    double x = a + dx/6.0;
	    double D = 2.0*dx/3.0;
	    for (int i=0; i<N; x += dx, ++i)
	    	sum += f.evaluate(x) + f.evaluate(x+D);
	    return (I + dx*sum)/3.0;
	}
	
	public double getAccuracy() {
		return accuracy_;
	}
	  
	public int getNumberOfEvaluations() {
		return numberOfEvaluations_;
	}
	
}