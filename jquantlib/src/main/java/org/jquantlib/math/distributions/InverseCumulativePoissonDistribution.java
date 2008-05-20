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
 * 
 * @author Dominik Holenstein
 *
 */

//TODO InverseCumulativePoissonDistribution: Translate the C++ code
public class InverseCumulativePoissonDistribution {
	
/*
 //! Inverse cumulative Poisson distribution function
    /*! \test the correctness of the returned value is tested by
              checking it against known good results.
    
    class InverseCumulativePoisson : public std::unary_function<Real,Real> {
      public:
        InverseCumulativePoisson(Real lambda = 1.0);
        Real operator()(Real x) const;
      private:
        Real lambda_;
        Real calcSummand(BigNatural index) const;
    };
    
    inline InverseCumulativePoisson::InverseCumulativePoisson(Real lambda)
    : lambda_(lambda) {
        QL_REQUIRE(lambda_ > 0.0, "lambda must be positive");
    }

    inline Real InverseCumulativePoisson::operator()(Real x) const {
        QL_REQUIRE(x >= 0.0 && x <= 1.0,
                   "Inverse cumulative Poisson distribution is "
                   "only defined on the interval [0,1]");

        if (x == 1.0)
            return QL_MAX_REAL;

        Real sum = 0.0;
        BigNatural index = 0;
        while (x > sum) {
            sum += calcSummand(index);
            index++;
        }

        return Real(index-1);
    }

    inline Real InverseCumulativePoisson::calcSummand(BigNatural index) const {
        return std::exp(-lambda_) * std::pow(lambda_, Integer(index)) /
            Factorial::get(index);
    }

 */

}
