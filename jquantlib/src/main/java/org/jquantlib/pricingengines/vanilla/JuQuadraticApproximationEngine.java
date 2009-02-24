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
 Copyright (C) 2004 Neil Firth

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
import org.jquantlib.instruments.StrikedTypePayoff;
import org.jquantlib.math.distributions.CumulativeNormalDistribution;
import org.jquantlib.math.distributions.NormalDistribution;
import org.jquantlib.pricingengines.BlackCalculator;
import org.jquantlib.pricingengines.BlackFormula;
import org.jquantlib.pricingengines.VanillaOptionEngine;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;

/**
 * An Approximate Formula for Pricing American Options, 
 * Journal of Derivatives Winter 1999,  Ju, N.
 * <p>
 * Known issue, the case of zero interest rates causes a division by zero     
 *      
 * <p>
 * Ported from 
 * <ul>
 * <li>ql/pricingengines/vanilla/juquadraticengine.cpp</li>
 * <li>ql/pricingengines/vanilla/juquadraticengine.hpp</li>
 * </ul>
 * 
 * @author <Richard Gomes>
 *
 */

public class JuQuadraticApproximationEngine extends VanillaOptionEngine {

	@Override
	public void calculate() {
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

		if (!(arguments.payoff instanceof StrikedTypePayoff)){
			throw new ArithmeticException("non-striked payoff given");
		}
		StrikedTypePayoff payoff = (StrikedTypePayoff)arguments.payoff;


		if (!(arguments.stochasticProcess instanceof GeneralizedBlackScholesProcess)){
			throw new ArithmeticException("Black-Scholes process required");
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
		BlackCalculator black = new BlackCalculator(payoff, forwardPrice, Math.sqrt(variance), riskFreeDiscount);

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
			NormalDistribution normalDist = new NormalDistribution();	

			double /*@Real*/ tolerance = 1e-6;
			double /*@Real*/ Sk = BaroneAdesiWhaleyApproximationEngine.criticalPrice(
					payoff, riskFreeDiscount, dividendDiscount, variance,
					tolerance);

			double /*@Real*/ forwardSk = Sk * dividendDiscount / riskFreeDiscount;

			double /*@Real*/ alpha = -2.0*Math.log(riskFreeDiscount)/(variance);
			double /*@Real*/ beta = 2.0*Math.log(dividendDiscount/riskFreeDiscount)/
									(variance);
			double /*@Real*/ h = 1 - riskFreeDiscount;
			double /*@Real*/ phi;
			
			switch (payoff.optionType()) {
			case CALL:
				phi = 1;
				break;
			case PUT:
				phi = -1;
				break;
			default:
				throw new ArithmeticException("unknown option type");
			}
			//it can throw: to be fixed
			//FIXME div by zero can occur here where zero interest rates (h=0)
			double /*@Real*/ temp_root = Math.sqrt ((beta-1)*(beta-1) + (4*alpha)/h);
			double /*@Real*/ lambda = (-(beta-1) + phi * temp_root) / 2;
			double /*@Real*/ lambda_prime = - phi * alpha / (h*h * temp_root);

			double /*@Real*/ black_Sk = BlackFormula.blackFormula(payoff.optionType(), payoff.strike(),
                              forwardSk, Math.sqrt(variance)) * riskFreeDiscount;
			double /*@Real*/ hA = phi * (Sk - payoff.strike()) - black_Sk;

			double /*@Real*/ d1_Sk = (Math.log(forwardSk/payoff.strike()) + 0.5*variance)
											/Math.sqrt(variance);
			double /*@Real*/ d2_Sk = d1_Sk - Math.sqrt(variance);
			double /*@Real*/ part1 = forwardSk * normalDist.evaluate(d1_Sk) /
                             (alpha * Math.sqrt(variance));
			double /*@Real*/ part2 = - phi * forwardSk * cumNormalDist.evaluate(phi * d1_Sk) *
									Math.log(dividendDiscount) / Math.log(riskFreeDiscount);
			double /*@Real*/ part3 = + phi * payoff.strike() * cumNormalDist.evaluate(phi * d2_Sk);
			double /*@Real*/ V_E_h = part1 + part2 + part3;

			double /*@Real*/ b = (1-h) * alpha * lambda_prime / (2*(2*lambda + beta - 1));
			double /*@Real*/ c = - ((1 - h) * alpha / (2 * lambda + beta - 1)) *
								(V_E_h / (hA) + 1 / h + lambda_prime / (2*lambda + beta - 1));
			double /*@Real*/ temp_spot_ratio = Math.log(spot / Sk);
			double /*@Real*/ chi = temp_spot_ratio * (b * temp_spot_ratio + c);

			if (phi*(Sk-spot) > 0) {
				results.value = black.value() +
				hA * Math.pow((spot/Sk), lambda) / (1 - chi);
			} else {
				results.value = phi * (spot - payoff.strike());
			}

			if (Double.isNaN(results.value)){
				double hh = 0.0;
				double gg = hh;
			}
			double /*@Real*/ temp_chi_prime = (2 * b / spot) * Math.log(spot/Sk);
			double /*@Real*/ chi_prime = temp_chi_prime + c / spot;
			double /*@Real*/ chi_double_prime = 2*b/(spot*spot)
													- temp_chi_prime / spot
													- c / (spot*spot);
			results.delta = phi * dividendDiscount * cumNormalDist.evaluate(phi * d1_Sk)
							+ (lambda / (spot * (1 - chi)) + chi_prime / ((1 - chi)*(1 - chi))) *
							(phi * (Sk - payoff.strike()) - black_Sk) * Math.pow((spot/Sk), lambda);

			results.gamma = phi * dividendDiscount * normalDist.evaluate(phi*d1_Sk) /
                             (spot * Math.sqrt(variance))
                             + (2 * lambda * chi_prime / (spot * (1 - chi) * (1 - chi))
                            		 + 2 * chi_prime * chi_prime / ((1 - chi) * (1 - chi) * (1 - chi))
                            		 + chi_double_prime / ((1 - chi) * (1 - chi))
                            		 + lambda * (1 - lambda) / (spot * spot * (1 - chi)))
                            		 * (phi * (Sk - payoff.strike()) - black_Sk)
                            		 * Math.pow((spot/Sk), lambda);

		} // end of "early exercise can be optimal"

	}

}
