/*
Copyright (C) 2008 Praneet Tiwari

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
package org.jquantlib.model.shortrate.onefactormodels;

import org.jquantlib.math.Array;
import org.jquantlib.model.Parameter;
import org.jquantlib.model.shortrate.TermStructureFittingParameter;
import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.Compounding;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Frequency;

/**
 * 
 * @author Praneet Tiwari
 */

// ! Analytical term-structure fitting parameter \f$ \varphi(t) \f$.
/*
 * ! \f$ \varphi(t) \f$ is analytically defined by \f[ \varphi(t) = f(t) + \frac{1}{2}[\frac{\sigma(1-e^{-at})}{a}]^2, \f] where \f$
 * f(t) \f$ is the instantaneous forward rate at \f$ t \f$.
 */
public class FittingParameter extends TermStructureFittingParameter {
    // need permanent solution for this one

    public static double QL_EPSILON = 1e-10;
    static private Handle<YieldTermStructure> termStructure_;
    static private Double /* @Real */a_, sigma_;

    static protected class Impl extends Parameter.Impl {

        public Impl(final Handle<YieldTermStructure> termStructure, Double /* @Real */a, Double /* @Real */sigma) {
            termStructure_ = (termStructure);
            a_ = (a);
            sigma_ = (sigma);
        }

        @Override
        public double /* @Real */value(final Array a, double /* @Time */t) {
            Double /* @Rate */forwardRate =
            // termStructure_->forwardRate(t, t, Continuous, NoFrequency);
            termStructure_.getLink().forwardRate(t, t, Compounding.CONTINUOUS, Frequency.NO_FREQUENCY).rate();
            Double /* @Real */temp = a_ < Math.sqrt(QL_EPSILON) ? sigma_ * t : sigma_ * (1.0 - Math.exp(-a_ * t)) / a_;
            return (forwardRate + 0.5 * temp * temp);
        }
    }

    public FittingParameter(final Handle<YieldTermStructure> termStructure, Double /* @Real */a, Double /* @Real */sigma) {
        super(new FittingParameter.Impl(termStructure, a, sigma));
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
        // new FittingParameter.Impl());
    }
}