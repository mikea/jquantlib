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


package org.jquantlib.math.distributions;

import org.jquantlib.math.UnaryFunctionDouble;
import org.jquantlib.math.Factorial;
import org.jquantlib.math.Constants;

/**
 * 
 * @author Dominik Holenstein
 * <p>
 * <strong>Inverse cumulative Poisson distribution function</strong>
   <p>
   Test the correctness of the returned value is tested by
   checking it against known good results.
 *
 */

// TODO InverseCumulativePoissonDistribution: Add test case.
public class InverseCumulativePoissonDistribution implements UnaryFunctionDouble {
	
	private double lambda_;

      // public:
      //   InverseCumulativePoisson(Real lambda = 1.0);
    
    public InverseCumulativePoissonDistribution(double lambda) {
    	lambda_ = lambda;
    	lambda_ = 1.0;
    	if(lambda_ <= 0.0) throw new ArithmeticException("lambda must be positive");
    }

    public double evaluate (double x)  {
        if(x <= 0.0 && x >= 1.0) throw new ArithmeticException(
                   "Inverse cumulative Poisson distribution is " +
                   "only defined on the interval [0,1]");

        if (x == 1.0) return Constants.QL_MAX_REAL;

        double sum = 0.0;
        int index = 0;
        while (x > sum) {
            sum += calcSummand(index);
            index++;
        }
        return (double)(index-1);
    }

	private double calcSummand(int index){
		Factorial fact = new Factorial();
		return Math.exp(-lambda_) * Math.pow(lambda_, index) / fact.get(index);
	}
}
