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
 Copyright (C) 2003 Neil Firth
 Copyright (C) 2002, 2003 Ferdinando Ametrano
 Copyright (C) 2002, 2003 Sadruddin Rejeb
 Copyright (C) 2000, 2001, 2002, 2003 RiskMap srl

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

package org.jquantlib.pricingengines.barrier;

import org.jquantlib.instruments.BarrierType;
import org.jquantlib.instruments.PlainVanillaPayoff;
import org.jquantlib.math.distributions.CumulativeNormalDistribution;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;
import org.jquantlib.termstructures.Compounding;
import org.jquantlib.termstructures.InterestRate;
import org.jquantlib.time.Frequency;

/**
 * <p>
 * Ported from 
 * <ul>
 * <li>ql/pricingengines/barrier/analyticbarrierengine.hpp</li>
 * <li>ql/pricingengines/barrier/analyticbarrierengine.cpp</li>
 * </ul>
 * @author <Richard Gomes>
 *
 */
@SuppressWarnings("PMD.TooManyMethods")
public class AnalyticBarrierOptionEngine extends BarrierOptionEngine {
	
	private static final String bsprocessrequired = "Black-Scholes process required";

	@Override
	public void calculate() {

        if (!(getArguments().payoff instanceof PlainVanillaPayoff)){
        	throw new ArithmeticException("non-plain payoff given");
        }
        PlainVanillaPayoff payoff = (PlainVanillaPayoff)getArguments().payoff;
        if(!(payoff.strike()>0.0)){
        	throw new ArithmeticException("strike must be positive");
        }

        if (!(arguments.stochasticProcess instanceof GeneralizedBlackScholesProcess)){
        	throw new ArithmeticException(bsprocessrequired);
        }
        //not needed
        //GeneralizedBlackScholesProcess process = (GeneralizedBlackScholesProcess)arguments.stochasticProcess;


        final double strike = payoff.strike();

        final BarrierType barrierType = arguments.barrierType;

        switch (payoff.optionType()) {
          case CALL:
            switch (barrierType) {
              case DownIn:
                if (strike >= barrier())
                    results.value = C(1,1) + E(1);
                else
                    results.value = A(1) - B(1) + D(1,1) + E(1);
                break;
              case  UpIn:
                if (strike >= barrier())
                    results.value = A(1) + E(-1);
                else
                    results.value = B(1) - C(-1,1) + D(-1,1) + E(-1);
                break;
              case  DownOut:
                if (strike >= barrier())
                    results.value = A(1) - C(1,1) + F(1);
                else
                    results.value = B(1) - D(1,1) + F(1);
                break;
              case  UpOut:
                if (strike >= barrier())
                    results.value = F(-1);
                else
                    results.value = A(1) - B(1) + C(-1,1) - D(-1,1) + F(-1);
                break;
            }
            break;
          case PUT:
            switch (barrierType) {
              case  DownIn:
                if (strike >= barrier())
                    results.value = B(-1) - C(1,-1) + D(1,-1) + E(1);
                else
                    results.value = A(-1) + E(1);
                break;
              case  UpIn:
                if (strike >= barrier())
                    results.value = A(-1) - B(-1) + D(-1,-1) + E(-1);
                else
                    results.value = C(-1,-1) + E(-1);
                break;
              case  DownOut:
                if (strike >= barrier())
                    results.value = A(-1) - B(-1) + C(1,-1) - D(1,-1) + F(1);
                else
                    results.value = F(1);
                break;
              case  UpOut:
                if (strike >= barrier())
                    results.value = B(-1) - D(-1,-1) + F(-1);
                else
                    results.value = A(-1) - C(-1,-1) + F(-1);
                break;
            }
            break;
          default:
            throw new ArithmeticException("unknown type");
        }
		
	}

	
    double  underlying()  {
        return arguments.stochasticProcess.initialValues()[0];
    }

    double strike()  {
        if (!(getArguments().payoff instanceof PlainVanillaPayoff)){
        	throw new ArithmeticException("non-plain payoff given");
        }
        PlainVanillaPayoff payoff = (PlainVanillaPayoff)getArguments().payoff;
        return payoff.strike();
    }

    double /*@Time*/  residualTime()  {
        return arguments.stochasticProcess.getTime(
                                             arguments.exercise.lastDate());
    }

    double /*@Volatility*/  volatility()  {
        if (!(arguments.stochasticProcess instanceof GeneralizedBlackScholesProcess)){
        	throw new ArithmeticException(bsprocessrequired);
        }
        GeneralizedBlackScholesProcess process = (GeneralizedBlackScholesProcess)arguments.stochasticProcess;
        return process.blackVolatility().getLink().blackVol(residualTime(), strike());
    }

    double  stdDeviation()  {
        return volatility() * Math.sqrt(residualTime());
    }

    double  barrier()  {
        return arguments.barrier;
    }

    double  rebate()  {
        return arguments.rebate;
    }

    double /*@Rate*/  riskFreeRate()  {
        if (!(arguments.stochasticProcess instanceof GeneralizedBlackScholesProcess)){
        	throw new ArithmeticException(bsprocessrequired);
        }
        GeneralizedBlackScholesProcess process = (GeneralizedBlackScholesProcess)arguments.stochasticProcess;
        
        InterestRate rate =  process.riskFreeRate().getLink().zeroRate(residualTime(), Compounding.CONTINUOUS,
                                                 Frequency.NO_FREQUENCY, false);//TODO add zeroRate method with extrpalote set to false
        return rate.rate();
    }

    double /*@DiscountFactor*/  riskFreeDiscount()  {
        if (!(arguments.stochasticProcess instanceof GeneralizedBlackScholesProcess)){
        	throw new ArithmeticException(bsprocessrequired);
        }
        GeneralizedBlackScholesProcess process = (GeneralizedBlackScholesProcess)arguments.stochasticProcess;
        return process.riskFreeRate().getLink().discount(residualTime());
    }

    double /*@Rate*/  dividendYield()  {
        if (!(arguments.stochasticProcess instanceof GeneralizedBlackScholesProcess)){
        	throw new ArithmeticException(bsprocessrequired);
        }
        GeneralizedBlackScholesProcess process = (GeneralizedBlackScholesProcess)arguments.stochasticProcess;
        
        InterestRate yield = process.dividendYield().getLink().zeroRate(residualTime(), Compounding.CONTINUOUS,
                Frequency.NO_FREQUENCY, false);
        return yield.rate();
    }

    double /*@DiscountFactor*/  dividendDiscount()  {
        if (!(arguments.stochasticProcess instanceof GeneralizedBlackScholesProcess)){
        	throw new ArithmeticException("Black-Scholes process required");
        }
        GeneralizedBlackScholesProcess process = (GeneralizedBlackScholesProcess)arguments.stochasticProcess;
        return process.dividendYield().getLink().discount(residualTime());
    }

    double /*@Rate*/  mu()  {
        double /*@Volatility*/ vol = volatility();
        return (riskFreeRate() - dividendYield())/(vol * vol) - 0.5;
    }

    double  muSigma()  {
        return (1 + mu()) * stdDeviation();
    }

    @SuppressWarnings("PMD")
    double  A(double phi)  {
        double x1 =
            Math.log(underlying()/strike())/stdDeviation() + muSigma();
        double N1 = f_.evaluate(phi*x1);
        double N2 = f_.evaluate(phi*(x1-stdDeviation()));
        return phi*(underlying() * dividendDiscount() * N1
                    - strike() * riskFreeDiscount() * N2);
    }

    @SuppressWarnings("PMD")
    double  B(final double phi)  {
        double x2 =
            Math.log(underlying()/barrier())/stdDeviation() + muSigma();
        double N1 = f_.evaluate(phi*x2);
        double N2 = f_.evaluate(phi*(x2-stdDeviation()));
        return phi*(underlying() * dividendDiscount() * N1
                    - strike() * riskFreeDiscount() * N2);
    }

    @SuppressWarnings("PMD.MethodNamingConventions")
    double  C(double eta, final double phi)  {
        double HS = barrier()/underlying();
        double powHS0 = Math.pow(HS, 2 * mu());
        double powHS1 = powHS0 * HS * HS;
        double y1 = Math.log(barrier()*HS/strike())/stdDeviation() + muSigma();
        double N1 = f_.evaluate(eta*y1);
        double N2 = f_.evaluate(eta*(y1-stdDeviation()));
        return phi*(underlying() * dividendDiscount() * powHS1 * N1
                    - strike() * riskFreeDiscount() * powHS0 * N2);
    }
    
    @SuppressWarnings("PMD.MethodNamingConventions")
    double  D(double eta, double phi)  {
        double HS = barrier()/underlying();
        double powHS0 = Math.pow(HS, 2 * mu());
        double powHS1 = powHS0 * HS * HS;
        double y2 = Math.log(barrier()/underlying())/stdDeviation() + muSigma();
        double N1 = f_.evaluate(eta*y2);
        double N2 = f_.evaluate(eta*(y2-stdDeviation()));
        return phi*(underlying() * dividendDiscount() * powHS1 * N1
                    - strike() * riskFreeDiscount() * powHS0 * N2);
    }
    
    @SuppressWarnings("PMD.MethodNamingConventions")
    double  E(double eta)  {
        if (rebate() > 0) {
            double powHS0 = Math.pow(barrier()/underlying(), 2 * mu());
            double x2 =
                Math.log(underlying()/barrier())/stdDeviation() + muSigma();
            double y2 =
                Math.log(barrier()/underlying())/stdDeviation() + muSigma();
            double N1 = f_.evaluate(eta*(x2 - stdDeviation()));
            double N2 = f_.evaluate(eta*(y2 - stdDeviation()));
            return rebate() * riskFreeDiscount() * (N1 - powHS0 * N2);
        } else {
            return 0.0;
        }
    }

    double  F(double eta)  {
        if (rebate() > 0) {
            double /*@Rate*/ m = mu();
            double /*@Volatility*/ vol = volatility();
            double lambda = Math.sqrt(m*m + 2.0*riskFreeRate()/(vol * vol));
            double HS = barrier()/underlying();
            double powHSplus = Math.pow(HS, m + lambda);
            double powHSminus = Math.pow(HS, m - lambda);

            double sigmaSqrtT = stdDeviation();
            double z = Math.log(barrier()/underlying())/sigmaSqrtT
                + lambda * sigmaSqrtT;

            double N1 = f_.evaluate(eta * z);
            double N2 = f_.evaluate(eta * (z - 2.0 * lambda * sigmaSqrtT));
            return rebate() * (powHSplus * N1 + powHSminus * N2);
        } else {
            return 0.0;
        }
    }

    CumulativeNormalDistribution f_ = new CumulativeNormalDistribution();

}
