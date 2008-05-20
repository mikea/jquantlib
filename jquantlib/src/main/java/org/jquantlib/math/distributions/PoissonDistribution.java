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
 */

// TODO Poisson Distribution: Translate the C++ code
public class PoissonDistribution {
 /*
  * //! Normal distribution function
    /*! Given an integer \f$ k \f$, it returns its probability
        in a Poisson distribution.

        \test the correctness of the returned value is tested by
              checking it against known good results.
    
    class PoissonDistribution : public std::unary_function<Real,Real> {
      public:
        PoissonDistribution(Real mu);
        // function
        Real operator()(BigNatural k) const;
      private:
        Real mu_, logMu_;
    };
    
    // inline definitions

    inline PoissonDistribution::PoissonDistribution(Real mu)
    : mu_(mu) {

        QL_REQUIRE(mu_>=0.0,
                   "mu must be non negative (" << mu_ << " not allowed)");

        if (mu_!=0.0) logMu_ = std::log(mu_);
    }

    inline Real PoissonDistribution::operator()(BigNatural k) const {
        if (mu_==0.0) {
            if (k==0) return 1.0;
            else      return 0.0;
        }
        Real logFactorial = Factorial::ln(k);
        return std::exp(k*std::log(mu_) - logFactorial - mu_);
    }


  * 
  */
	
}
