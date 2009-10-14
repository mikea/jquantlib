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
import org.jquantlib.cashflow.CashFlow;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.exercise.Exercise;
import org.jquantlib.instruments.StrikedTypePayoff;
import org.jquantlib.math.Constants;
import org.jquantlib.pricingengines.BlackCalculator;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;
import org.jquantlib.termstructures.Compounding;
import org.jquantlib.time.Date;
import org.jquantlib.time.Frequency;

/**
 * Analytic pricing engine for European options with discrete dividends
 * 
 * @category vanillaengines
 * 
 * @author <Richard Gomes>
 */
public class AnalyticDividendEuropeanEngine extends DividendVanillaOptionEngine {

    private final GeneralizedBlackScholesProcess process;

    public AnalyticDividendEuropeanEngine(final GeneralizedBlackScholesProcess process) {
        super();
        this.process = process;
        this.process.addObserver(this);
    }

    /* (non-Javadoc)
     * @see org.jquantlib.pricingengines.PricingEngine#calculate()
     */
    @Override
    public void calculate() /* @ReadOnly */ {

        QL.require(arguments.exercise.type() == Exercise.Type.EUROPEAN, "not an European option"); // TODO: message

        final StrikedTypePayoff payoff = (StrikedTypePayoff) arguments.payoff;
        QL.require(payoff!=null, "non-striked payoff given"); // TODO: message

        final Date settlementDate = process.riskFreeRate().currentLink().referenceDate();
        double riskless = 0.0;
        for (int i=0; i<arguments.cashFlow.size(); i++) {
            final CashFlow cashflow = arguments.cashFlow.get(i);
            if (cashflow.date().gt(settlementDate)) {
                riskless += cashflow.amount() * process.riskFreeRate().currentLink().discount(cashflow.date());
            }
        }

        final double spot = process.stateVariable().currentLink().value() - riskless;
        QL.require(spot > 0.0, "negative or null underlying after subtracting dividends"); // TODO: message

        final /*@DiscountFactor*/ double dividendDiscount = process.dividendYield().currentLink().discount(arguments.exercise.lastDate());
        final /*@DiscountFactor*/ double riskFreeDiscount = process.riskFreeRate().currentLink().discount(arguments.exercise.lastDate());
        final double forwardPrice = spot * dividendDiscount / riskFreeDiscount;

        final double variance = process.blackVolatility().currentLink().blackVariance(arguments.exercise.lastDate(), payoff.strike());
        final BlackCalculator black = new BlackCalculator(payoff, forwardPrice, Math.sqrt(variance), riskFreeDiscount);

        results.value = black.value();
        results.delta = black.delta(spot);
        results.gamma = black.gamma(spot);

        final DayCounter rfdc  = process.riskFreeRate().currentLink().dayCounter();
        final DayCounter voldc = process.blackVolatility().currentLink().dayCounter();
        /*@Time*/ double t = voldc.yearFraction(process.blackVolatility().currentLink().referenceDate(), arguments.exercise.lastDate());
        results.vega = black.vega(t);

        double delta_theta = 0.0, delta_rho = 0.0;
        for (int i = 0; i < arguments.cashFlow.size(); i++) {
            final CashFlow cashflow = arguments.cashFlow.get(i);
            final Date d = cashflow.date();
            if (d.gt(settlementDate)) {
                delta_theta -= cashflow.amount()
                * process.riskFreeRate().currentLink().zeroRate(d, rfdc, Compounding.CONTINUOUS, Frequency.ANNUAL).rate()
                * process.riskFreeRate().currentLink().discount(d);
                delta_rho += cashflow.amount() * process.time(d) * process.riskFreeRate().currentLink().discount(t);
            }
        }
        t = process.time(arguments.exercise.lastDate());
        try {
            results.theta = black.theta(spot, t) + delta_theta * black.delta(spot);
        } catch (final ArithmeticException e) {
            results.theta = Constants.NULL_REAL;
        }

        results.rho = black.rho(t) + delta_rho * black.delta(spot);
    }

}
