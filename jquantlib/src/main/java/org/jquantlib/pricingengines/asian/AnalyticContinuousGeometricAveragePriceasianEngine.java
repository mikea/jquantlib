

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


import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.exercise.Exercise;
import org.jquantlib.instruments.AverageType;
import org.jquantlib.instruments.PlainVanillaPayoff;
import org.jquantlib.pricingengines.BlackCalculator;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;
import org.jquantlib.termstructures.Compounding;
import org.jquantlib.time.Frequency;
import org.jquantlib.util.Date;


//TODO add reference to original paper, clewlow strickland
/**
 * @author <Richard Gomes>
 */

public class AnalyticContinuousGeometricAveragePriceasianEngine extends ContinuousAveragingAsianOptionEngine{

	@Override
	public void calculate() /*@ReadOnly*/ {

		if (!(arguments.averageType==AverageType.Geometric)){
			throw new IllegalArgumentException("not a geometric average option");
		}

		if (!(arguments.exercise.type()==Exercise.Type.EUROPEAN)){
			throw new IllegalArgumentException("not an European Option");
		}

		Date exercise = arguments.exercise.lastDate();
		
		if (!(arguments.payoff instanceof PlainVanillaPayoff)){
			throw new IllegalArgumentException("non-plain payoff given");
		}
		PlainVanillaPayoff payoff = (PlainVanillaPayoff)arguments.payoff;
		
		if (!(arguments.stochasticProcess instanceof GeneralizedBlackScholesProcess)){
			throw new IllegalArgumentException("Black-Scholes process required");
		}

		GeneralizedBlackScholesProcess process = (GeneralizedBlackScholesProcess)arguments.stochasticProcess;
		/*@Volatility*/ double volatility = process.blackVolatility().getLink().blackVol(exercise, payoff.strike());
		/*@Real*/ double variance = process.blackVolatility().getLink().blackVariance(exercise, payoff.strike());
		/*@DiscountFactor*/ double  riskFreeDiscount = process.riskFreeRate().getLink().discount(exercise);
		DayCounter rfdc  = process.riskFreeRate().getLink().dayCounter();
		DayCounter divdc = process.dividendYield().getLink().dayCounter();
		DayCounter voldc = process.blackVolatility().getLink().dayCounter();

		/*@Spread*/ double dividendYield = 0.5 * (
				process.riskFreeRate().getLink().zeroRate(exercise, rfdc,
						Compounding.CONTINUOUS, Frequency.NO_FREQUENCY).rate() +
						process.dividendYield().getLink().zeroRate(exercise, divdc,
									Compounding.CONTINUOUS, Frequency.NO_FREQUENCY).rate() +
									volatility*volatility/6.0);

		/*@Time*/ double t_q = divdc.yearFraction(
				process.dividendYield().getLink().referenceDate(), exercise);
		/*@DiscountFactor*/ double dividendDiscount = Math.exp(-dividendYield*t_q);
		/*@Real*/ double spot = process.stateVariable().getLink().evaluate();
		/*@Real*/ double forward = spot * dividendDiscount / riskFreeDiscount;

		BlackCalculator black = new BlackCalculator(payoff, forward, Math.sqrt(variance/3.0),riskFreeDiscount);
		results.value = black.value();
		results.delta = black.delta(spot);
		results.gamma = black.gamma(spot);
		results.dividendRho = black.dividendRho(t_q)/2.0;

		/*@Time*/ double t_r = rfdc.yearFraction(process.riskFreeRate().getLink().referenceDate(),
				arguments.exercise.lastDate());
		results.rho = black.rho(t_r) + 0.5 * black.dividendRho(t_q);

		/*@Time*/ double t_v = voldc.yearFraction(
				process.blackVolatility().getLink().referenceDate(),
				arguments.exercise.lastDate());
		results.vega = black.vega(t_v)/Math.sqrt(3.0) +
						black.dividendRho(t_q)*volatility/6.0;
		results.theta = black.theta(spot, t_v);
		//results_.theta = Null<Real>();
	}

}
