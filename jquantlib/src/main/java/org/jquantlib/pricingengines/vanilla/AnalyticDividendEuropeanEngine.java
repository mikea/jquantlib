/*
 Copyright (C) 2009 Richard Gomes

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
/*
 Copyright (C) 2004 StatPro Italia srl

 This file is part of QuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://quantlib.org/

 QuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <quantlib-dev@lists.sf.net>. The license is also available online at
 <http://quantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 */


package org.jquantlib.pricingengines.vanilla;

import org.jquantlib.QL;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.exercise.Exercise;
import org.jquantlib.instruments.StrikedTypePayoff;
import org.jquantlib.pricingengines.BlackCalculator;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;
import org.jquantlib.termstructures.Compounding;
import org.jquantlib.time.Date;
import org.jquantlib.time.Frequency;

/**
 *
 * @author <Richard Gomes>
 */
public class AnalyticDividendEuropeanEngine extends DividendVanillaOptionEngine {

    public AnalyticDividendEuropeanEngine() {
        super();
    }

    /* (non-Javadoc)
     * @see org.jquantlib.pricingengines.PricingEngine#calculate()
     */
    @Override
    public void calculate() {
        QL.require(arguments.exercise.type() == Exercise.Type.EUROPEAN , "not an European option"); // QA:[RG]::verified // TODO: message
        QL.require(arguments.payoff instanceof StrikedTypePayoff , "non-striked payoff given"); // QA:[RG]::verified // TODO: message
        final StrikedTypePayoff payoff = (StrikedTypePayoff)arguments.payoff;
        QL.require(arguments.stochasticProcess instanceof GeneralizedBlackScholesProcess , "Black-Scholes process required"); // QA:[RG]::verified // TODO: message
        final GeneralizedBlackScholesProcess process = (GeneralizedBlackScholesProcess)arguments.stochasticProcess;

        final Date settlementDate = process.riskFreeRate().getLink().referenceDate();
        double /*@Real*/ riskless = 0.0;

        for (int i=0; i<arguments.cashFlow.size(); i++)
            if (arguments.cashFlow.get(i).date().ge(settlementDate))
                riskless += arguments.cashFlow.get(i).amount() *
                process.riskFreeRate().getLink().discount(arguments.cashFlow.get(i).date());
        final double /*@Real*/ spot = process.stateVariable().getLink().op() - riskless;
        QL.require(spot > 0.0, "negative or null underlying given"); // QA:[RG]::verified // TODO: message

        final double /*@DiscountFactor*/ dividendDiscount = process.dividendYield().getLink().discount(arguments.exercise.lastDate());
        final double /*@DiscountFactor*/ riskFreeDiscount = process.riskFreeRate().getLink().discount(arguments.exercise.lastDate());
        final double /*@Real*/ forwardPrice = spot * dividendDiscount / riskFreeDiscount;

        final double /*@Real*/ variance =
            process.blackVolatility().getLink().blackVariance(arguments.exercise.lastDate(), payoff.strike());

        final BlackCalculator black = new BlackCalculator(payoff, forwardPrice, Math.sqrt(variance), riskFreeDiscount);

        results.value = black.value();
        results.delta = black.delta(spot);
        results.gamma = black.gamma(spot);

        final DayCounter rfdc  = process.riskFreeRate().getLink().dayCounter();
        final DayCounter voldc = process.blackVolatility().getLink().dayCounter();
        double /*@Time*/ t = voldc.yearFraction(
                process.blackVolatility().getLink().referenceDate(),
                arguments.exercise.lastDate());
        results.vega = black.vega(t);

        double /*@Real*/ delta_theta = 0.0, delta_rho = 0.0;
        for (int i = 0; i < arguments.cashFlow.size(); i++) {
            final Date d = arguments.cashFlow.get(i).date();
            if (d.ge(settlementDate)) {
                delta_theta -= arguments.cashFlow.get(i).amount() *
                process.riskFreeRate().getLink().zeroRate(d,rfdc,Compounding.CONTINUOUS,Frequency.ANNUAL).rate()*
                process.riskFreeRate().getLink().discount(d);
                final double /*@Time*/ tt = process.time(d);
                delta_rho += arguments.cashFlow.get(i).amount() * tt *
                process.riskFreeRate().getLink().discount(tt);
            }
        }
        t = process.time(arguments.exercise.lastDate());

        results.theta = black.theta(spot, t) +
        delta_theta * black.delta(spot);

        results.rho = black.rho(t) +
        delta_rho * black.delta(spot);
    }

}
