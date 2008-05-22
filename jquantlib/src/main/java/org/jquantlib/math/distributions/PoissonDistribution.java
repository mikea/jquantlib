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

import org.jquantlib.math.Factorial;

/**
 * @author Dominik Holenstein
 * 
 * <p>
  * <strong>Normal distribution function </strong><br>
    Given an integer <strong> k </strong>, it returns its probability
    in a Poisson distribution.
	<p>
    Test the correctness of the returned value is tested by
    checking it against known good results.
 */

// TODO PoissonDistribution: Write test case.
public class PoissonDistribution {
 
	private double mu_;
	private double logMu_;
    
    
    public PoissonDistribution(double mu) {
       mu_ = mu;
       if(mu_< 0.0) throw new ArithmeticException("mu must be non negative (" + mu_ + " not allowed)");
       if(mu_ != 0.0) logMu_ = Math.log(mu_);
    }

    double evaluate(int k){
        if (mu_==0.0) {
            if (k==0) return 1.0;
            else      return 0.0;
        }
        Factorial fact = new Factorial();
        double logFactorial = fact.ln(k);
        return Math.exp(k*Math.log(mu_) - logFactorial - mu_);
    }
}
