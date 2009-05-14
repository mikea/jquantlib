/*
 Copyright (C) 2009 Ueli Hofstetter

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

package org.jquantlib.model.equity;

//! Heston model for the stochastic volatility of an asset
/*! References:

 Heston, Steven L., 1993. A Closed-Form Solution for Options
 with Stochastic Volatility with Applications to Bond and
 Currency Options.  The review of Financial Studies, Volume 6,
 Issue 2, 327-343.

 \test calibration is tested against known good values.
 */

/**
 * Implementation of the Heston Model, see http://en.wikipedia.org/wiki/Heston_model
 */

import org.jquantlib.math.optimization.PositiveConstraint;
import org.jquantlib.model.CalibratedModel;
import org.jquantlib.model.ConstantParameter;
import org.jquantlib.processes.HestonProcess;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.RelinkableHandle;
import org.jquantlib.quotes.SimpleQuote;

public class HestonModel extends CalibratedModel {

    protected RelinkableHandle<Quote> v0_, kappa_, theta_, sigma_, rho_;;

    public HestonModel(final HestonProcess process) {
        super(5);
        this.v0_ = process.v0();
        this.kappa_ = process.kappa();
        this.theta_ = process.theta();
        this.sigma_ = process.sigma();
        this.rho_ = process.rho();
        arguments_.set(0, new ConstantParameter(process.theta().getLink().evaluate(), new PositiveConstraint()));
        arguments_.set(1, new ConstantParameter(process.kappa().getLink().evaluate(), new PositiveConstraint()));
        arguments_.set(2, new ConstantParameter(process.sigma().getLink().evaluate(), new PositiveConstraint()));
        arguments_.set(3, new ConstantParameter(process.rho().getLink().evaluate(), new PositiveConstraint()));
        arguments_.set(4, new ConstantParameter(process.v0().getLink().evaluate(), new PositiveConstraint()));

    }

    public void generateArguments() {
        v0_.setLink(new SimpleQuote((SimpleQuote) v0_.getLink()));
        kappa_.setLink(new SimpleQuote((SimpleQuote) kappa_.getLink()));
        theta_.setLink(new SimpleQuote((SimpleQuote) theta_.getLink()));
        sigma_.setLink(new SimpleQuote((SimpleQuote) sigma_.getLink()));
        rho_.setLink(new SimpleQuote((SimpleQuote) rho_.getLink()));
    }

    // variance mean version level
    public double theta() {
        return arguments_.get(0).getOperatorEq(0.0);
    }

    // variance mean reversion speed
    public double kappa() {
        return arguments_.get(1).getOperatorEq(0.0);
    }

    // volatility of the volatility
    public double sigma() {
        return arguments_.get(2).getOperatorEq(0.0);
    }

    // correlation
    public double rho() {
        return arguments_.get(3).getOperatorEq(0.0);
    }

    // spot variance
    public double v0() {
        return arguments_.get(4).getOperatorEq(0.0);
    }

}
