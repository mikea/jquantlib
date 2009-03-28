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
 * 
 * Ported from 
 * <ul>
 * <li>ql/pricingengines/vanilla/integralengine.cpp</li>
 * <li>ql/pricingengines/vanilla/integralengine.hpp</li>
 * </ul>
 * 
 * @author Richard Gomes
 *
 */

// Pricing engine for European vanilla options using integral approach
// TODO: define tolerance for calculate()

public class IntegralEngine extends OneAssetStrikedOptionEngine {
//xxx    
//    private static final String not_a_European_Option = "not a European Option";
    
    private static final String NON_STRIKED_PAYOFF_GIVEN = "non-striked payoff given";
    private static final String BLACK_SCHOLES_PROCESS_GIVEN = "Black-Scholes process required";


	@Override
	public void calculate() {
//XXX this test is not needed
//    	if (!(arguments.exercise.type()==Exercise.Type.EUROPEAN)){
//			throw new ArithmeticException(not_a_European_Option);
//		}

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
	// static inner classes
	//
	
	private static class Integrand implements UnaryFunctionDouble {

        private Payoff payoff_;
        private double s0_;
        private double /* @Rate */drift_;
        private double variance_;

        public Integrand(Payoff payoff, double s0, double /* @Rate */drift, double variance) {
            payoff_ = payoff;
            s0_ = s0;
            drift_ = drift;
            variance_ = variance;
        }

        public double evaluate(double x) {
            double temp = s0_ * Math.exp(x);
            double result = payoff_.valueOf(temp);
            return result * Math.exp(-(x - drift_) * (x - drift_) / (2.0 * variance_));
        }

    }
    
}
