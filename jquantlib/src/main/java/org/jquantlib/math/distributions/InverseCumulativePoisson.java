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

package org.jquantlib.math.distributions;


import org.jquantlib.math.Constants;
import org.jquantlib.math.Factorial;
import org.jquantlib.math.UnaryFunction;

/**
 * Inverse cumulative Poisson distribution function. 
 *
 * @author Dominik Holenstein
 */
// TEST the correctness of the returned value is tested by checking it against known good results.
public class InverseCumulativePoisson implements UnaryFunction<Double, Double> {
	
	//
	// private fields
	//
	
	private double lambda;

	
	//
	// public constructors
	//
	
//XXX
//	public InverseCumulativePoisson() {
//    	this(1.0);
//    }
    
	
    public InverseCumulativePoisson(final double lambda) {
    	this.lambda = lambda;
    	
    	// FIXME lambda_ is initialized with lambda first and then set equal to 1.0. This doesn't make sense.
    	this.lambda = 1.0;
    	if(this.lambda <= 0.0) {
    	    throw new ArithmeticException("lambda must be positive");
    	}
    }

    
    //
    // public methods
    //
    
    private double calcSummand(final int index) {
        Factorial fact = new Factorial();
        return Math.exp(-lambda) * Math.pow(lambda, index) / fact.get(index);
    }

    
    //
    // implements UnaryFunction
    //
    
    /**
     * Computes the inverse cumulative poisson distribution.
     * 
     * @param x
     * @returns the inverse of the cumulative poisson distribution of input <code>x</code>
     */
    @Override
    public Double evaluate (final Double x) /* @Read-only */ {
        if(x <= 0.0 && x >= 1.0) {
            throw new ArithmeticException("Inverse cumulative Poisson distribution is only defined on the interval [0,1]");
        }

        if (x == 1.0) {
            return Constants.QL_MAX_REAL;
        }

        double sum = 0.0;
        int index = 0;
        while (x > sum) {
            sum += calcSummand(index);
            index++;
        }
        return (double)(index-1);
    }
    
}
