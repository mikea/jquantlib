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

/*
 Copyright (C) 2002, 2003, 2004 Ferdinando Ametrano
 Copyright (C) 2002, 2003 RiskMap srl
 Copyright (C) 2003, 2004 StatPro Italia srl

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

package org.jquantlib.pricingengines;

import org.jquantlib.QL;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.exercise.Exercise;
import org.jquantlib.instruments.StrikedTypePayoff;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;
import org.jquantlib.time.Date;

/**
 * Pricing engine for European vanilla options using analytical formulae
 * <p>
 * The correctness of the returned value is tested by reproducing results available in literature.
 * <li>the correctness of the returned <i>greeks</i> is tested by reproducing results available in literature.</li>
 * <li>the correctness of the returned greeks is tested by reproducing numerical derivatives.</li>
 * <li>the correctness of the returned implied volatility is tested by using it for reproducing the target value.</li>
 * <li>the <i>implied volatility</i> calculation is tested by checking that it does not modify the option.</li>
 * <li>the correctness of the returned value in case of <i>cash-or-nothing</i> binary payoff is tested by reproducing results
 *     available in literature.</li>
 * <li>the correctness of the returned value in case of <i>asset-or-nothing</i> binary payoff is tested by
 *     reproducing results available in literature.</li>
 * <li>the correctness of the returned value in case of <i>gap-or-nothing</i> binary payoff is tested by
 *     reproducing results available in literature.</li>
 * <li>the correctness of the returned <i>greeks</i> in case of <i>cash-or-nothing</i> binary payoff
 *     is tested by reproducing numerical derivatives.</li>
 *
 * @see PricingEngine
 *
 * @author <Richard Gomes>
 */
//TODO: write more test cases
public class AnalyticEuropeanEngine extends VanillaOptionEngine {

    // TODO: refactor messages
    private static final String NOT_AN_EUROPEAN_OPTION = "not an European Option";
    private static final String NON_STRIKED_PAYOFF_GIVEN = "non-striked payoff given";
    private static final String BLACK_SCHOLES_PROCESS_REQUIRED = "Black-Scholes process required";


    //
    // public constructors
    //

    public AnalyticEuropeanEngine() {
        super();
    }


    //
    // implements PricingEngine
    //

    @Override
    public void calculate() /* @ReadOnly */{
        QL.require(arguments.exercise.type() == Exercise.Type.EUROPEAN , NOT_AN_EUROPEAN_OPTION); // QA:[RG]::verified // TODO: message
        final StrikedTypePayoff payoff = (StrikedTypePayoff) arguments.payoff;
        QL.require(payoff != null , NON_STRIKED_PAYOFF_GIVEN); // QA:[RG]::verified // TODO: message
        final GeneralizedBlackScholesProcess process = (GeneralizedBlackScholesProcess) arguments.stochasticProcess;
        QL.require(process != null , BLACK_SCHOLES_PROCESS_REQUIRED); // QA:[RG]::verified // TODO: message

        /* @Variance */final double variance = process.blackVolatility().currentLink().blackVariance(arguments.exercise.lastDate(), payoff.strike());

        /* @DiscountFactor */final double dividendDiscount = process.dividendYield().currentLink().discount(arguments.exercise.lastDate());
        /* @DiscountFactor */final double riskFreeDiscount = process.riskFreeRate().currentLink().discount(arguments.exercise.lastDate());
        /* @Price */final double spot = process.stateVariable().currentLink().op();
        QL.require(spot > 0.0, "negative or null underlying given"); // QA:[RG]::verified // TODO: message
        /* @Price */final double forwardPrice = spot * dividendDiscount / riskFreeDiscount;
        final BlackCalculator black = new BlackCalculator(payoff, forwardPrice, Math.sqrt(variance), riskFreeDiscount);

        results.value = black.value();
        results.delta = black.delta(spot);
        results.deltaForward = black.deltaForward();
        results.elasticity = black.elasticity(spot);
        results.gamma = black.gamma(spot);

        final DayCounter rfdc = process.riskFreeRate().currentLink().dayCounter();
        final DayCounter divdc = process.dividendYield().currentLink().dayCounter();
        final DayCounter voldc = process.blackVolatility().currentLink().dayCounter();
        final Date refDate = process.riskFreeRate().currentLink().referenceDate();
        /* @Time */double t = rfdc.yearFraction(refDate, arguments.exercise.lastDate());
        results.rho = black.rho(t);

        t = divdc.yearFraction(process.dividendYield().currentLink().referenceDate(), arguments.exercise.lastDate());
        results.dividendRho = black.dividendRho(t);

        t = voldc.yearFraction(process.blackVolatility().currentLink().referenceDate(), arguments.exercise.lastDate());
        results.vega = black.vega(t);
        try {
            results.theta = black.theta(spot, t);
            results.thetaPerDay = black.thetaPerDay(spot, t);
        } catch (final Exception e) {
            results.theta = Double.NaN;
            results.thetaPerDay = Double.NaN;
        }

        results.strikeSensitivity = black.strikeSensitivity();
        results.itmCashProbability = black.itmCashProbability();
    }

}
