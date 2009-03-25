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
import org.jquantlib.instruments.StrikedTypePayoff;
import org.jquantlib.math.distributions.CumulativeNormalDistribution;
import org.jquantlib.pricingengines.BlackCalculator;
import org.jquantlib.pricingengines.BlackFormula;
import org.jquantlib.pricingengines.VanillaOptionEngine;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;
import org.jquantlib.instruments.Option;

/**
 * Barone-Adesi and Whaley pricing engine for American options (1987)
 * <p>
 * Ported from 
 * <ul>
 * <li>ql/pricingengines/vanilla/baroneadesiwhaleyengine.cpp</li>
 * <li>ql/pricingengines/vanilla/baroneadesiwhaleyengine.hpp</li>
 * </ul>
 * 
 * @author <Richard Gomes>
 *
 */
public class BaroneAdesiWhaleyApproximationEngine extends VanillaOptionEngine {
    
    private static final String not_an_American_Option = "not an American Option";
    private static final String non_American_exercise_given = "non-American exercise given";
    private static final String payoff_at_expiry_not_handled = "payoff at expiry not handled";
    private static final String non_striked_payoff_given = "non-striked payoff given";
    private static final String black_scholes_process_required = "Black-Scholes process required";
    private static final String unknown_option_type = "unknown Option type";

	@Override
	public void calculate() {

		if (!(arguments.exercise.type()==Exercise.Type.AMERICAN)){
			throw new ArithmeticException(not_an_American_Option);
		}

		if (!(arguments.exercise instanceof AmericanExercise)){
			throw new ArithmeticException(non_American_exercise_given);
		}
		AmericanExercise ex = (AmericanExercise)arguments.exercise;
		if (ex.payoffAtExpiry()){
			throw new ArithmeticException(payoff_at_expiry_not_handled);
		}

		if (!(arguments.payoff instanceof StrikedTypePayoff)){
			throw new ArithmeticException(non_striked_payoff_given);
		}
		StrikedTypePayoff payoff = (StrikedTypePayoff)arguments.payoff;

		if (!(arguments.stochasticProcess instanceof GeneralizedBlackScholesProcess)){
			throw new ArithmeticException(black_scholes_process_required);
		}
		GeneralizedBlackScholesProcess process = (GeneralizedBlackScholesProcess)arguments.stochasticProcess;

        
        double /*@Real*/ variance = process.blackVolatility().getLink().blackVariance(
            ex.lastDate(), payoff.strike());
        double /*@DiscountFactor*/ dividendDiscount = process.dividendYield().getLink().discount(
            ex.lastDate());
        double /*@DiscountFactor*/ riskFreeDiscount = process.riskFreeRate().getLink().discount(
            ex.lastDate());
        double /*@Real*/ spot = process.stateVariable().getLink().evaluate();
        double /*@Real*/ forwardPrice = spot * dividendDiscount / riskFreeDiscount;
        BlackCalculator black = new BlackCalculator(payoff, forwardPrice, Math.sqrt(variance),
                              riskFreeDiscount);

        if (dividendDiscount>=1.0 && payoff.optionType()==Option.Type.CALL) {
            // early exercise never optimal
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
            // early exercise can be optimal
            CumulativeNormalDistribution cumNormalDist = new CumulativeNormalDistribution();
            double /*@Real*/ tolerance = 1e-6;
            double /*@Real*/ Sk = criticalPrice(payoff, riskFreeDiscount,
                dividendDiscount, variance, tolerance);
            double /*@Real*/ forwardSk = Sk * dividendDiscount / riskFreeDiscount;
            double /*@Real*/ d1 = (Math.log(forwardSk/payoff.strike()) + 0.5*variance)
                /Math.sqrt(variance);
            double /*@Real*/ n = 2.0*Math.log(dividendDiscount/riskFreeDiscount)/variance;
            double /*@Real*/ K = -2.0*Math.log(riskFreeDiscount)/
                (variance*(1.0-riskFreeDiscount));
            double /*@Real*/ Q, a;
            switch (payoff.optionType()) {
                case CALL:
                    Q = (-(n-1.0) + Math.sqrt(((n-1.0)*(n-1.0))+4.0*K))/2.0;
                    a =  (Sk/Q) * (1.0 - dividendDiscount * cumNormalDist.evaluate(d1));
                    if (spot<Sk) {
                        results.value = black.value() +
                            a * Math.pow((spot/Sk), Q);
                    } else {
                        results.value = spot - payoff.strike();
                    }
                    break;
                case PUT:
                    Q = (-(n-1.0) - Math.sqrt(((n-1.0)*(n-1.0))+4.0*K))/2.0;
                    a = -(Sk/Q) *
                        (1.0 - dividendDiscount * cumNormalDist.evaluate(-d1));
                    if (spot>Sk) {
                        results.value = black.value() +
                            a * Math.pow((spot/Sk), Q);
                    } else {
                        results.value = payoff.strike() - spot;
                    }
                    break;
                default:
                  throw new ArithmeticException(unknown_option_type);
            }
        } // end of "early exercise can be optimal"
		
	}
	
    static double  criticalPrice(
            StrikedTypePayoff payoff,
            double /*@DiscountFactor*/ riskFreeDiscount,
            double /*@DiscountFactor*/ dividendDiscount,
            double variance){
    	return criticalPrice(payoff, riskFreeDiscount, dividendDiscount, variance, 1.0e-6);
    }
    static double  criticalPrice(
            StrikedTypePayoff payoff,
            double /*@DiscountFactor*/ riskFreeDiscount,
            double /*@DiscountFactor*/ dividendDiscount,
            double variance,
            double tolerance){
    	
        // Calculation of seed value, Si
        double /*@Real*/ n= 2.0*Math.log(dividendDiscount/riskFreeDiscount)/(variance);
        double /*@Real*/ m=-2.0*Math.log(riskFreeDiscount)/(variance);
        double /*@Real*/ bT = Math.log(dividendDiscount/riskFreeDiscount);

        double /*@Real*/ qu, Su, h, Si;
        switch (payoff.optionType()) {
          case CALL:
            qu = (-(n-1.0) + Math.sqrt(((n-1.0)*(n-1.0)) + 4.0*m))/2.0;
            Su = payoff.strike() / (1.0 - 1.0/qu);
            h = -(bT + 2.0*Math.sqrt(variance)) * payoff.strike() /
                (Su - payoff.strike());
            Si = payoff.strike() + (Su - payoff.strike()) *
                (1.0 - Math.exp(h));
            break;
          case PUT:
            qu = (-(n-1.0) - Math.sqrt(((n-1.0)*(n-1.0)) + 4.0*m))/2.0;
            Su = payoff.strike() / (1.0 - 1.0/qu);
            h = (bT - 2.0*Math.sqrt(variance)) * payoff.strike() /
                (payoff.strike() - Su);
            Si = Su + (payoff.strike() - Su) * Math.exp(h);
            break;
          default:
            throw new ArithmeticException(unknown_option_type);
        }


        // Newton Raphson algorithm for finding critical price Si
        double /*@Real*/ Q, LHS, RHS, bi;
        double /*@Real*/ forwardSi = Si * dividendDiscount / riskFreeDiscount;
        double /*@Real*/ d1 = (Math.log(forwardSi/payoff.strike()) + 0.5*variance) /
            Math.sqrt(variance);
        CumulativeNormalDistribution cumNormalDist = new CumulativeNormalDistribution();
        double /*@Real*/ K = (riskFreeDiscount!=1.0 ? -2.0*Math.log(riskFreeDiscount)/
            (variance*(1.0-riskFreeDiscount)) : 0.0);
        double /*@Real*/ temp = BlackFormula.blackFormula(payoff.optionType(), payoff.strike(),
                forwardSi, Math.sqrt(variance))*riskFreeDiscount;
        switch (payoff.optionType()) {
          case CALL:
            Q = (-(n-1.0) + Math.sqrt(((n-1.0)*(n-1.0)) + 4 * K)) / 2;
            LHS = Si - payoff.strike();
            RHS = temp + (1 - dividendDiscount * cumNormalDist.evaluate(d1)) * Si / Q;
            bi =  dividendDiscount * cumNormalDist.evaluate(d1) * (1 - 1/Q) +
                (1 - dividendDiscount *
                 cumNormalDist.derivative(d1) / Math.sqrt(variance)) / Q;
            while (Math.abs(LHS - RHS)/payoff.strike() > tolerance) {
                Si = (payoff.strike() + RHS - bi * Si) / (1 - bi);
                forwardSi = Si * dividendDiscount / riskFreeDiscount;
                d1 = (Math.log(forwardSi/payoff.strike())+0.5*variance)
                    /Math.sqrt(variance);
                LHS = Si - payoff.strike();
                double /*@Real*/ temp2 = BlackFormula.blackFormula(payoff.optionType(), payoff.strike(),
                    forwardSi, Math.sqrt(variance))*riskFreeDiscount;
                RHS = temp2 + (1 - dividendDiscount * cumNormalDist.evaluate(d1)) * Si / Q;
                bi = dividendDiscount * cumNormalDist.evaluate(d1) * (1 - 1 / Q)
                    + (1 - dividendDiscount *
                       cumNormalDist.derivative(d1) / Math.sqrt(variance))
                    / Q;
            }
            break;
          case PUT:
            Q = (-(n-1.0) - Math.sqrt(((n-1.0)*(n-1.0)) + 4 * K)) / 2;
            LHS = payoff.strike() - Si;
            RHS = temp - (1 - dividendDiscount * cumNormalDist.evaluate(-d1)) * Si / Q;
            bi = -dividendDiscount * cumNormalDist.evaluate(-d1) * (1 - 1/Q)
                - (1 + dividendDiscount * cumNormalDist.derivative(-d1)
                   / Math.sqrt(variance)) / Q;
            while (Math.abs(LHS - RHS)/payoff.strike() > tolerance) {
                Si = (payoff.strike() - RHS + bi * Si) / (1 + bi);
                forwardSi = Si * dividendDiscount / riskFreeDiscount;
                d1 = (Math.log(forwardSi/payoff.strike())+0.5*variance)
                    /Math.sqrt(variance);
                LHS = payoff.strike() - Si;
                double /*@Real*/ temp2 = BlackFormula.blackFormula(payoff.optionType(), payoff.strike(),
                    forwardSi, Math.sqrt(variance))*riskFreeDiscount;
                RHS = temp2 - (1 - dividendDiscount * cumNormalDist.evaluate(-d1)) * Si / Q;
                bi = -dividendDiscount * cumNormalDist.evaluate(-d1) * (1 - 1 / Q)
                    - (1 + dividendDiscount * cumNormalDist.derivative(-d1)
                       / Math.sqrt(variance)) / Q;
            }
            break;
          default:
            throw new ArithmeticException(unknown_option_type);
        }

        return Si;
    }

}
