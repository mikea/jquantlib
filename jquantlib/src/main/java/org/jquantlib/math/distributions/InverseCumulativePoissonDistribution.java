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
import org.jquantlib.math.UnaryFunctionDouble;

/**
 * Inverse cumulative Poisson distribution function. 
 *
 * @author Dominik Holenstein
 */
// TODO InverseCumulativePoissonDistribution: Add test case.
// TEST the correctness of the returned value is tested by checking it against known good results.
public class InverseCumulativePoissonDistribution implements UnaryFunctionDouble {
	
	//
	// private fields
	//
	
	private double lambda_;

	
	//
	// public constructors
	//
	
    public InverseCumulativePoissonDistribution() {
    	this(1.0);
    }
    
	
    public InverseCumulativePoissonDistribution(double lambda) {
    	lambda_ = lambda;
    	
    	// FIXME lambda_ is initialized with lambda first and then set equal to 1.0. This doesn't make sense.
    	lambda_ = 1.0;
    	if(lambda_ <= 0.0) {
    	    throw new ArithmeticException("lambda must be positive");
    	}
    }

    //
    // implements UnaryFunctionDouble
    //
    
    
    /**
     * Computes the inverse cumulative poisson distribution.
     * 
     * @param x
     * @returns the inverse of the cumulative poisson distribution of input <code>x</code>
     */
    public double evaluate (double x) /* @Read-only */ {
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
    
    
    //
    // implements the calcSummand function
    //

    private double calcSummand(int index) {
		Factorial fact = new Factorial();
		return Math.exp(-lambda_) * Math.pow(lambda_, index) / fact.get(index);
    }
}
