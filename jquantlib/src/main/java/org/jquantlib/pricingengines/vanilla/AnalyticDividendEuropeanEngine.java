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

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.exercise.Exercise;
import org.jquantlib.instruments.StrikedTypePayoff;
import org.jquantlib.pricingengines.BlackCalculator;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;
import org.jquantlib.termstructures.Compounding;
import org.jquantlib.time.Frequency;
import org.jquantlib.util.Date;

/**
 * 
 * @author <Richard Gomes>
 */
public class AnalyticDividendEuropeanEngine extends DividendVanillaOptionEngine {

    public AnalyticDividendEuropeanEngine() {
        super();
    }
    
    /* (non-Javadoc)
     * @see org.jquantlib.pricingengines.PricingEngine#calculate()
     */
    @Override
    public void calculate() {
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        if (!(arguments.exercise.type() == Exercise.Type.EUROPEAN)){
            throw new ArithmeticException("not an European option");
        }

        if (!(arguments.payoff instanceof StrikedTypePayoff)){
            throw new ArithmeticException("non-striked payoff given");
        }
        
        StrikedTypePayoff payoff = (StrikedTypePayoff)arguments.payoff;

        if (!(arguments.stochasticProcess instanceof GeneralizedBlackScholesProcess)){
            throw new ArithmeticException("Black-Scholes process required");
        }
        
        GeneralizedBlackScholesProcess process = (GeneralizedBlackScholesProcess)arguments.stochasticProcess;
        
        Date settlementDate = process.riskFreeRate().getLink().referenceDate();
        double /*@Real*/ riskless = 0.0;
        
        for (int i=0; i<arguments.cashFlow.size(); i++)
            if (arguments.cashFlow.get(i).date().ge(settlementDate)){
                riskless += arguments.cashFlow.get(i).amount() *
                    process.riskFreeRate().getLink().discount(arguments.cashFlow.get(i).date());
            }
        double /*@Real*/ spot = process.stateVariable().getLink().evaluate() - riskless;

        double /*@DiscountFactor*/ dividendDiscount =
            process.dividendYield().getLink().discount(arguments.exercise.lastDate());
        double /*@DiscountFactor*/ riskFreeDiscount =
            process.riskFreeRate().getLink().discount(arguments.exercise.lastDate());
        double /*@Real*/ forwardPrice = spot * dividendDiscount / riskFreeDiscount;

        double /*@Real*/ variance =
            process.blackVolatility().getLink().blackVariance(
                                   arguments.exercise.lastDate(),
                                   payoff.strike());

        BlackCalculator black = new BlackCalculator(payoff, forwardPrice, Math.sqrt(variance), riskFreeDiscount);

        results.value = black.value();
        results.delta = black.delta(spot);
        results.gamma = black.gamma(spot);

        DayCounter rfdc  = process.riskFreeRate().getLink().dayCounter();
        DayCounter voldc = process.blackVolatility().getLink().dayCounter();
        double /*@Time*/ t = voldc.yearFraction(
                process.blackVolatility().getLink().referenceDate(),
                       arguments.exercise.lastDate());
        results.vega = black.vega(t);

        double /*@Real*/ delta_theta = 0.0, delta_rho = 0.0;
        for (int i = 0; i < arguments.cashFlow.size(); i++) {
            Date d = arguments.cashFlow.get(i).date();
            if (d.ge(settlementDate)) {
                delta_theta -= arguments.cashFlow.get(i).amount() *
                process.riskFreeRate().getLink().zeroRate(d,rfdc,Compounding.CONTINUOUS,Frequency.ANNUAL).rate()*
                process.riskFreeRate().getLink().discount(d);
                double /*@Time*/ tt = process.getTime(d);
                delta_rho += arguments.cashFlow.get(i).amount() * tt *
                  process.riskFreeRate().getLink().discount(tt);
            }
        }
        t = process.getTime(arguments.exercise.lastDate());

        results.theta = black.theta(spot, t) +
                  delta_theta * black.delta(spot);

        results.rho = black.rho(t) +
            delta_rho * black.delta(spot);

    }

}
