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
 Copyright (C) 2003 Ferdinando Ametrano

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

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.exercise.AmericanExercise;
import org.jquantlib.exercise.Exercise;
import org.jquantlib.instruments.Option;
import org.jquantlib.instruments.PlainVanillaPayoff;
import org.jquantlib.math.distributions.CumulativeNormalDistribution;
import org.jquantlib.pricingengines.BlackCalculator;
import org.jquantlib.pricingengines.VanillaOptionEngine;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;

/**
 * 
 * Ported from 
 * <ul>
 * <li>ql/pricingengines/vanilla/bjerksundstenslandengine.cpp</li>
 * <li>ql/pricingengines/vanilla/bjerksundstenslandengine.hpp</li>
 * </ul>
 * 
 * @author <Richard Gomes>
 *
 */

public class BjerksundStenslandApproximationEngine extends VanillaOptionEngine{

	@Override
	public void calculate() /*@ReadOnly*/{
		
		if (!(arguments.exercise.type()==Exercise.Type.AMERICAN)){
			throw new ArithmeticException("not an American Option");
		}

		if (!(arguments.exercise instanceof AmericanExercise)){
			throw new ArithmeticException("non-American exercise given");
		}
		AmericanExercise ex = (AmericanExercise)arguments.exercise;
		if (ex.payoffAtExpiry()){
			throw new ArithmeticException("payoff at expiry not handled");
		}

		if (!(arguments.payoff instanceof PlainVanillaPayoff)){
			throw new ArithmeticException("non-plain payoff given");
		}
		PlainVanillaPayoff payoff = (PlainVanillaPayoff)arguments.payoff;

		if (!(arguments.stochasticProcess instanceof GeneralizedBlackScholesProcess)){
			throw new ArithmeticException("Black-Scholes process required");
		}
		GeneralizedBlackScholesProcess process = (GeneralizedBlackScholesProcess)arguments.stochasticProcess;

		double /*@Real*/ variance =
			process.blackVolatility().getLink().blackVariance(ex.lastDate(),
                                           payoff.getStrike());
		double /*@DiscountFactor*/ dividendDiscount =
			process.dividendYield().getLink().discount(ex.lastDate());
		double /*@DiscountFactor*/ riskFreeDiscount =
			process.riskFreeRate().getLink().discount(ex.lastDate());
		double /*@Real*/ spot = process.stateVariable().getLink().evaluate();
		double /*@Real*/ strike = payoff.getStrike();

		if (payoff.getOptionType()==Option.Type.PUT) {
			// use put-call simmetry
			//std::swap(spot, strike);
			///argh!!! cant swap primtives, do it by hand
			double tmp = spot; spot = strike; strike = tmp;
			
			//std::swap(riskFreeDiscount, dividendDiscount);
			tmp = riskFreeDiscount; riskFreeDiscount = dividendDiscount; dividendDiscount = tmp;

			
			payoff = new PlainVanillaPayoff(Option.Type.CALL, strike);
		}

		if (dividendDiscount>=1.0) {
			// early exercise is never optimal - use Black formula
			double /*@Real*/ forwardPrice = spot * dividendDiscount / riskFreeDiscount;
			BlackCalculator black = new BlackCalculator(payoff, forwardPrice, Math.sqrt(variance),
                       riskFreeDiscount);

			results.value        = black.value();
			results.delta        = black.delta(spot);
			results.deltaForward = black.deltaForward();
			results.elasticity   = black.elasticity(spot);
			results.gamma        = black.gamma(spot);

			DayCounter rfdc  = process.riskFreeRate().getLink().dayCounter();
			DayCounter divdc = process.dividendYield().getLink().dayCounter();
			DayCounter voldc = process.blackVolatility().getLink().dayCounter();
			double /*@Time*/ t = rfdc.yearFraction(process.riskFreeRate().getLink().referenceDate(),
                            arguments.exercise.lastDate());
			results.rho = black.rho(t);

			t = divdc.yearFraction(process.dividendYield().getLink().referenceDate(),
                        arguments.exercise.lastDate());
			results.dividendRho = black.dividendRho(t);

			t = voldc.yearFraction(process.blackVolatility().getLink().referenceDate(),
                        arguments.exercise.lastDate());
			results.vega        = black.vega(t);
			results.theta       = black.theta(spot, t);
			results.thetaPerDay = black.thetaPerDay(spot, t);

			results.strikeSensitivity  = black.strikeSensitivity();
			results.itmCashProbability = black.itmCashProbability();
		} else {
			// early exercise can be optimal - use approximation
			results.value = americanCallApproximation(spot,
                                            strike,
                                            riskFreeDiscount,
                                            dividendDiscount,
                                            variance);
		}

	}

	
	
    private CumulativeNormalDistribution cumNormalDist = new CumulativeNormalDistribution();

    private double /*@Real*/ phi(double /*@Real*/ S, double /*@Real*/ gamma, double /*@Real*/ H, 
    		double /*@Real*/ I, double /*@Real*/ rT, double /*Real*/ bT, double /*@Real*/ variance) {

        double /*@Real*/ lambda = (-rT + gamma * bT + 0.5 * gamma * (gamma - 1.0)
            * variance);
        double /*@Real*/ d = -(Math.log(S / H) + (bT + (gamma - 0.5) * variance) )
            / Math.sqrt(variance);
        double /*@Real*/ kappa = 2.0 * bT / variance + (2.0 * gamma - 1.0);
        return Math.exp(lambda) * Math.pow(S, gamma) * (cumNormalDist.evaluate(d)
            - Math.pow((I / S), kappa) *
            cumNormalDist.evaluate(d - 2.0 * Math.log(I/S) / Math.sqrt(variance)));
    }

    private double /*@Real*/ americanCallApproximation(double /*@Real*/ S, double /*@Real*/ X,
            	double /*@Real*/ rfD, double /*@Real*/ dD, double /*@Real*/ variance) {

    	double /*@Real*/ bT = Math.log(dD/rfD);
    	double /*@Real*/ rT = Math.log(1.0/rfD);

    	double /*@Real*/ beta = (0.5 - bT/variance) +
    		Math.sqrt(Math.pow((bT/variance - 0.5), (double)(2.0))
    				+ 2.0 * rT/variance);
    	double /*@Real*/ BInfinity = beta / (beta - 1.0) * X;
    	// Real B0 = std::max(X, std::log(rfD) / std::log(dD) * X);
    	double /*@Real*/ B0 = Math.max(X, rT / (rT - bT) * X);
    	double /*@Real*/ ht = -(bT + 2.0*Math.sqrt(variance)) * B0 / (BInfinity - B0);

    	// investigate what happen to I for dD->0.0
    	double /*@Real*/ I = B0 + (BInfinity - B0) * (1 - Math.exp(ht));
//    	QL_REQUIRE(I >= X,
//    			"Bjerksund-Stensland approximation not applicable "
//    	"to this set of parameters");
    	if (!(I>=X)){
    		throw new ArithmeticException("Bjerksund-Stensland approximation " +
    				"not applicable to this set of parameters");
    	}
    	if (S >= I) {
    		return S - X;
    	} else {
    		// investigate what happen to alpha for dD->0.0
    		double /*@Real*/ alpha = (I - X) * Math.pow(I, (-beta));
    		return alpha * Math.pow(S, beta)
    				- alpha * phi(S, beta, I, I, rT, bT, variance)
    				+         phi(S,  1.0, I, I, rT, bT, variance)
    				-         phi(S,  1.0, X, I, rT, bT, variance)
    				-    X *  phi(S,  0.0, I, I, rT, bT, variance)
    				+    X *  phi(S,  0.0, X, I, rT, bT, variance);
    	}
    }

	


}
