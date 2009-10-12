

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
 Copyright (C) 2003, 2004 Ferdinando Ametrano
 Copyright (C) 2005 Gary Kennedy

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

package org.jquantlib.pricingengines.asian;


import org.jquantlib.QL;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.exercise.Exercise;
import org.jquantlib.instruments.AverageType;
import org.jquantlib.instruments.PlainVanillaPayoff;
import org.jquantlib.pricingengines.BlackCalculator;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;
import org.jquantlib.termstructures.Compounding;
import org.jquantlib.time.Date;
import org.jquantlib.time.Frequency;


/**
 * @author <Richard Gomes>
 */
//TODO class comments
//TODO add reference to original paper, clewlow strickland
public class AnalyticContinuousGeometricAveragePriceasianEngine extends ContinuousAveragingAsianOptionEngine{

    //
    // implements PricingEngine
    //

    @Override
    public void calculate() /*@ReadOnly*/ {
        QL.require(arguments.averageType==AverageType.Geometric , "not a geometric average option"); // QA:[RG]::verified // TODO: message
        QL.require(arguments.exercise.type()==Exercise.Type.EUROPEAN , "not an European Option"); // QA:[RG]::verified // TODO: message
        final Date exercise = arguments.exercise.lastDate();
        QL.require(arguments.payoff instanceof PlainVanillaPayoff , "non-plain payoff given"); // QA:[RG]::verified // TODO: message
        final PlainVanillaPayoff payoff = (PlainVanillaPayoff)arguments.payoff;
        QL.require(arguments.stochasticProcess instanceof GeneralizedBlackScholesProcess , "Black-Scholes process required"); // QA:[RG]::verified // TODO: message

        final GeneralizedBlackScholesProcess process = (GeneralizedBlackScholesProcess)arguments.stochasticProcess;
        /*@Volatility*/ final double volatility = process.blackVolatility().currentLink().blackVol(exercise, payoff.strike());
        /*@Real*/ final double variance = process.blackVolatility().currentLink().blackVariance(exercise, payoff.strike());
        /*@DiscountFactor*/ final double  riskFreeDiscount = process.riskFreeRate().currentLink().discount(exercise);
        final DayCounter rfdc  = process.riskFreeRate().currentLink().dayCounter();
        final DayCounter divdc = process.dividendYield().currentLink().dayCounter();
        final DayCounter voldc = process.blackVolatility().currentLink().dayCounter();

        /*@Spread*/ final double dividendYield = 0.5 * (
                process.riskFreeRate().currentLink().zeroRate(
                        exercise,
                        rfdc,
                        Compounding.CONTINUOUS,
                        Frequency.NO_FREQUENCY).rate() + process.dividendYield().currentLink().zeroRate(
                                exercise,
                                divdc,
                                Compounding.CONTINUOUS,
                                Frequency.NO_FREQUENCY).rate() + volatility*volatility/6.0);

        /*@Time*/ final double t_q = divdc.yearFraction(
                process.dividendYield().currentLink().referenceDate(), exercise);
        /*@DiscountFactor*/ final double dividendDiscount = Math.exp(-dividendYield*t_q);
        /*@Real*/ final double spot = process.stateVariable().currentLink().value();
        QL.require(spot > 0.0, "negative or null underlying given"); // QA:[RG]::verified // TODO: message
        /*@Real*/ final double forward = spot * dividendDiscount / riskFreeDiscount;

        final BlackCalculator black = new BlackCalculator(payoff, forward, Math.sqrt(variance/3.0),riskFreeDiscount);
        results.value = black.value();
        results.delta = black.delta(spot);
        results.gamma = black.gamma(spot);
        results.dividendRho = black.dividendRho(t_q)/2.0;

        /*@Time*/ final double t_r = rfdc.yearFraction(process.riskFreeRate().currentLink().referenceDate(),
                arguments.exercise.lastDate());
        results.rho = black.rho(t_r) + 0.5 * black.dividendRho(t_q);

        /*@Time*/ final double t_v = voldc.yearFraction(
                process.blackVolatility().currentLink().referenceDate(),
                arguments.exercise.lastDate());
        results.vega = black.vega(t_v)/Math.sqrt(3.0) +
        black.dividendRho(t_q)*volatility/6.0;
        results.theta = black.theta(spot, t_v);
        //results_.theta = Null<Real>();
    }

}
