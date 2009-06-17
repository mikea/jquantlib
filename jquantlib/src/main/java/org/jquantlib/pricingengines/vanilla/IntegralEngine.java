/*
 Copyright (C) 2007 Richard Gomes

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


package org.jquantlib.pricingengines.vanilla;

import org.jquantlib.exercise.Exercise;
import org.jquantlib.instruments.Payoff;
import org.jquantlib.instruments.StrikedTypePayoff;
import org.jquantlib.math.UnaryFunctionDouble;
import org.jquantlib.math.integrals.SegmentIntegral;
import org.jquantlib.pricingengines.OneAssetStrikedOptionEngine;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;

/**
 * Pricing engine for European vanilla options using integral approach 
 * 
 * @author Richard Gomes
 */

public class IntegralEngine extends OneAssetStrikedOptionEngine {

    // TODO: refactor messages
    private static final String NOT_AN_AMERICAN_OPTION = "not an American Option";
    private static final String NON_STRIKED_PAYOFF_GIVEN = "non-striked payoff given";
    private static final String BLACK_SCHOLES_PROCESS_GIVEN = "Black-Scholes process required";


    //
    // implements PricingEngine
    //
    
    // TODO: define tolerance for calculate()
	@Override
	public void calculate() {
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291 
    	if (!(arguments.exercise.type()==Exercise.Type.EUROPEAN)){
			throw new ArithmeticException(NOT_AN_AMERICAN_OPTION);
		}

		if (!(arguments.payoff instanceof StrikedTypePayoff)){
			throw new ArithmeticException(NON_STRIKED_PAYOFF_GIVEN);
		}
		
		StrikedTypePayoff payoff = (StrikedTypePayoff) arguments.payoff;

		if (!(arguments.stochasticProcess instanceof GeneralizedBlackScholesProcess)){
			throw new ArithmeticException(BLACK_SCHOLES_PROCESS_GIVEN);
		}
		GeneralizedBlackScholesProcess process = (GeneralizedBlackScholesProcess)arguments.stochasticProcess;

		double variance = process.blackVolatility().getLink().blackVariance(arguments.exercise.lastDate(), payoff.strike());
		double /* @DiscountFactor */dividendDiscount = process.dividendYield().getLink().discount(arguments.exercise.lastDate());
        double /* @DiscountFactor */riskFreeDiscount = process.riskFreeRate().getLink().discount(arguments.exercise.lastDate());
        double /* @Rate */drift = Math.log(dividendDiscount / riskFreeDiscount) - 0.5 * variance;

		Integrand f = new Integrand(arguments.payoff, process.stateVariable().getLink().evaluate(), drift, variance);
        SegmentIntegral integrator = new SegmentIntegral(5000);

		double infinity = 10.0*Math.sqrt(variance);
		results.value =
			process.riskFreeRate().getLink().discount(arguments.exercise.lastDate()) /
			Math.sqrt(2.0*Math.PI*variance) *
			integrator.evaluate(f, drift-infinity, drift+infinity);

	}


	//
	// inner classes
	//
	
	private static class Integrand implements UnaryFunctionDouble {

        private Payoff payoff;
        private double s0;
        private double /* @Rate */ drift;
        private double variance;

        public Integrand(final Payoff payoff, final double s0, final double /* @Rate */drift, final double variance) {
            this.payoff = payoff;
            this.s0 = s0;
            this.drift = drift;
            this.variance = variance;
        }

        public double evaluate(final double x) {
            double temp = s0 * Math.exp(x);
            double result = payoff.valueOf(temp);
            return result * Math.exp(-(x - drift) * (x - drift) / (2.0 * variance));
        }

    }
    
}
