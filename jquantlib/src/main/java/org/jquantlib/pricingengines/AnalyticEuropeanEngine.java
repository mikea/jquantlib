/*
 Copyright (C) 2008 Richard Gomes

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
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

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.exercise.Exercise;
import org.jquantlib.instruments.StrikedTypePayoff;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;

/**
 * Pricing engine for European vanilla options using analytical formulae
 * 
 * @note <b>TEST:</b> the correctness of the returned value is tested by reproducing
 *       results available in literature. - the correctness of the returned
 *       greeks is tested by reproducing results available in literature. - the
 *       correctness of the returned greeks is tested by reproducing numerical
 *       derivatives. - the correctness of the returned implied volatility is
 *       tested by using it for reproducing the target value. - the
 *       implied-volatility calculation is tested by checking that it does not
 *       modify the option. - the correctness of the returned value in case of
 *       cash-or-nothing digital payoff is tested by reproducing results
 *       available in literature. - the correctness of the returned value in
 *       case of asset-or-nothing digital payoff is tested by reproducing
 *       results available in literature. - the correctness of the returned
 *       value in case of gap digital payoff is tested by reproducing results
 *       available in literature. - the correctness of the returned greeks in
 *       case of cash-or-nothing digital payoff is tested by reproducing
 *       numerical derivatives.
 */
public class AnalyticEuropeanEngine extends VanillaOptionEngine {

	public void calculate() /* @ReadOnly */{
		if (arguments.exercise.getType() != Exercise.Type.EUROPEAN)
			throw new IllegalArgumentException("not an European option");

		StrikedTypePayoff payoff = (StrikedTypePayoff) arguments.payoff;
		if (payoff == null)
			throw new NullPointerException("non-striked payoff given");

		GeneralizedBlackScholesProcess process = (GeneralizedBlackScholesProcess) arguments.stochasticProcess;
		if (process == null)
			throw new NullPointerException("Black-Scholes process required");

		/* @Variance */double variance = process.blackVolatility().getLink().blackVariance(arguments.exercise.getLastDate(), payoff.getStrike());

		/* @DiscountFactor */double dividendDiscount = process.dividendYield().getLink().getDiscount(arguments.exercise.getLastDate());
		/* @DiscountFactor */double riskFreeDiscount = process.riskFreeRate().getLink().getDiscount(arguments.exercise.getLastDate());
		/* @Price */double spot = process.stateVariable().getLink().doubleValue();
		/* @Price */double forwardPrice = spot * dividendDiscount / riskFreeDiscount;
		BlackCalculator black = new BlackCalculator(payoff, forwardPrice, Math.sqrt(variance), riskFreeDiscount);

		results.value = black.value();
		results.delta = black.delta(spot);
		results.deltaForward = black.deltaForward();
		results.elasticity = black.elasticity(spot);
		results.gamma = black.gamma(spot);

		DayCounter rfdc = process.riskFreeRate().getLink().getDayCounter();
		DayCounter divdc = process.dividendYield().getLink().getDayCounter();
		DayCounter voldc = process.blackVolatility().getLink().getDayCounter();
		/* @Time */double t = rfdc.getYearFraction(process.riskFreeRate().getLink().getReferenceDate(), arguments.exercise.getLastDate());
		results.rho = black.rho(t);

		t = divdc.getYearFraction(process.dividendYield().getLink().getReferenceDate(), arguments.exercise.getLastDate());
		results.dividendRho = black.dividendRho(t);

		t = voldc.getYearFraction(process.blackVolatility().getLink().getReferenceDate(), arguments.exercise.getLastDate());
		results.vega = black.vega(t);
		try {
			results.theta = black.theta(spot, t);
			results.thetaPerDay = black.thetaPerDay(spot, t);
		} catch (Exception e) {
			results.theta = Double.NaN;
			results.thetaPerDay = Double.NaN;
		}

		results.strikeSensitivity = black.strikeSensitivity();
		results.itmCashProbability = black.itmCashProbability();
	}
}
