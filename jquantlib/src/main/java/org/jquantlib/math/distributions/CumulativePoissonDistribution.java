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

import org.jquantlib.math.distributions.IncompleteGamma;

/**
 * 
 * @author Dominik Holenstein
 *
 */

//TODO CumulativePoissonDistribution: Translate the C++ code
public class CumulativePoissonDistribution {
	
/*

//! Cumulative Poisson distribution function
    /*! This function provides an approximation of the
        integral of the Poisson distribution.

        For this implementation see
        "Numerical Recipes in C", 2nd edition,
        Press, Teukolsky, Vetterling, Flannery, chapter 6

        \test the correctness of the returned value is tested by
              checking it against known good results.
   
    class CumulativePoissonDistribution
        : public std::unary_function<Real,Real> {
      public:
        CumulativePoissonDistribution(Real mu) : mu_(mu) {}
        Real operator()(BigNatural k) const {
            return 1.0 - incompleteGammaFunction(k+1, mu_);
        }
      private:
        Real mu_;
    };
*/
}
