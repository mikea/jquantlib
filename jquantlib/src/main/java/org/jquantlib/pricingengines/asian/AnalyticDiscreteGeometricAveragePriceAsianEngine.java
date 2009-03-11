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

import java.util.List;

import org.joda.primitives.list.impl.ArrayDoubleList;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.exercise.Exercise;
import org.jquantlib.instruments.AverageType;
import org.jquantlib.instruments.Option;
import org.jquantlib.instruments.StrikedTypePayoff;
import org.jquantlib.math.Constants;
import org.jquantlib.math.distributions.CumulativeNormalDistribution;
import org.jquantlib.math.distributions.NormalDistribution;
import org.jquantlib.pricingengines.BlackCalculator;
import org.jquantlib.pricingengines.Greeks;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;
import org.jquantlib.termstructures.Compounding;
import org.jquantlib.time.Frequency;
import org.jquantlib.util.Date;


//TODO add reference to original paper, clewlow strickland
/**
 * @author <Richard Gomes>
 */
public class AnalyticDiscreteGeometricAveragePriceAsianEngine extends
							DiscreteAveragingAsianOptionEngine {

	
//	public AnalyticDiscreteGeometricAveragePriceAsianEngine(
//			DiscretAveragingAsianOptionArguments arguments,
//			OneAssetOptionResults results) {
//		super(arguments, results);
//	}

	@Override
	public void calculate() /*@ReadOnly*/{

		/* this engine cannot really check for the averageType==Geometric
        	since it can be used as control variate for the Arithmetic version
     		QL_REQUIRE(arguments_.averageType == Average::Geometric,
                "not a geometric average option");
		 */

		//QL_REQUIRE(arguments_.exercise->type() == Exercise::European,
		//"not an European Option");
		if (!(arguments.exercise.type()==Exercise.Type.EUROPEAN)){
			throw new IllegalArgumentException("not an European Option");
		}
		
		/*@Real*/ double runningLog;
		/*@Size*/ int pastFixings;
		if (arguments.averageType == AverageType.Geometric) {
			if (!(arguments.runningAccumulator>0.0)){
				throw new IllegalArgumentException(
                    "positive running product required: "
                    + arguments.runningAccumulator + " not allowed");
			}
			runningLog = Math.log(arguments.runningAccumulator);
			pastFixings = arguments.pastFixings;
		} else {  // it is being used as control variate
			runningLog = 1.0;
			pastFixings = 0;
		}

		//TODO compare to Analytic Euorpean, it checks for null instead of type...??
		StrikedTypePayoff payoff = null;
		if (!(arguments.payoff instanceof StrikedTypePayoff)) {
			throw new IllegalArgumentException("non-plain payoff given");
		}
		payoff = (StrikedTypePayoff) arguments.payoff;
		
		GeneralizedBlackScholesProcess process = (GeneralizedBlackScholesProcess) arguments.stochasticProcess;
		if (process == null)
			throw new NullPointerException("Black-Scholes process required");

		Date referenceDate = process.riskFreeRate().getLink().referenceDate();
		DayCounter rfdc  = process.riskFreeRate().getLink().dayCounter();
		DayCounter divdc = process.dividendYield().getLink().dayCounter();
		DayCounter voldc = process.blackVolatility().getLink().dayCounter();
	     

	    List<Double> fixingTimes = new ArrayDoubleList();
	    /*@Size*/ int i;
	    for (i=0; i<arguments.fixingDates.size(); i++) {
	    	if (arguments.fixingDates.get(i).ge(referenceDate)) {
	    		/*@Time*/ double t = voldc.yearFraction(referenceDate,
	    				arguments.fixingDates.get(i));
	    		fixingTimes.add(Double.valueOf(t));
	    	}
	    }

	    /*@Size*/ int remainingFixings = fixingTimes.size();
	    /*@Size*/ int numberOfFixings = pastFixings + remainingFixings;
	    /*@Real*/ double N = numberOfFixings;

	    /*@Real*/ double pastWeight = pastFixings/N;
	    /*@Real*/ double futureWeight = 1.0-pastWeight;

//	    Time timeSum = std::accumulate(fixingTimes.begin(),
//                                    fixingTimes.end(), 0.0);
	    //TODO add accumulate to std
	    double timeSum = 0.0;
	    for(int j=0;j<fixingTimes.size();j++){
	    	timeSum += fixingTimes.get(j);
	    }
	    
	    /*@Volatility*/ double vola = process.blackVolatility().getLink().blackVol(
                                           arguments.exercise.lastDate(),
                                           payoff.strike());
	    /*@Real*/ double temp = 0.0;
	    for (i=pastFixings+1; i<numberOfFixings; i++) {
	    	temp += fixingTimes.get(i-pastFixings-1)*(N-i);
	    }
	    
	    /*@Real*/ double variance = vola*vola /N/N * (timeSum+ 2.0*temp);
	    /*@Real*/ double dsigG_dsig = Math.sqrt((timeSum + 2.0*temp))/N;
	    /*@Real*/ double sigG = vola * dsigG_dsig;
	    /*@Real*/ double dmuG_dsig = -(vola * timeSum)/N;

	    Date exDate = arguments.exercise.lastDate();
	    /*@Rate*/ double dividendRate = process.dividendYield().getLink().
	    				zeroRate(exDate, divdc, Compounding.CONTINUOUS, Frequency.NO_FREQUENCY).rate();
	    /*@Rate*/ double riskFreeRate = process.riskFreeRate().getLink().
         					zeroRate(exDate, rfdc, Compounding.CONTINUOUS, Frequency.NO_FREQUENCY).rate();
	    /*@Rate*/ double nu = riskFreeRate - dividendRate - 0.5*vola*vola;
		
	    /*@Real*/ double  s = process.stateVariable().getLink().evaluate();
	    
	    /*@Real*/ double muG = pastWeight * runningLog +
         						futureWeight * Math.log(s) + nu*timeSum/N;
	    /*@Real*/ double forwardPrice = Math.exp(muG + variance / 2.0);

	    /*@DiscountFactor*/ double riskFreeDiscount = process.riskFreeRate().getLink().
	    								discount(arguments.exercise.lastDate());

	    BlackCalculator black = new BlackCalculator(payoff, forwardPrice, Math.sqrt(variance),
                           riskFreeDiscount);

	    results.value = black.value();
	    results.delta = futureWeight*black.delta(forwardPrice)*forwardPrice/s;
	    results.gamma = forwardPrice*futureWeight/(s*s)
				*(  black.gamma(forwardPrice)*futureWeight*forwardPrice
				  - pastWeight*black.delta(forwardPrice) );

		/*@Real*/ double Nx_1, nx_1;
		CumulativeNormalDistribution CND = new CumulativeNormalDistribution();
		NormalDistribution ND = new NormalDistribution();
		
		if (sigG > Constants.QL_EPSILON) {
			/*@Real*/ double x_1  = (muG-Math.log(payoff.strike())+variance)/sigG;
			Nx_1 = CND.evaluate(x_1);
			nx_1 = ND.evaluate(x_1);
		} else {
			Nx_1 = (muG > Math.log(payoff.strike()) ? 1.0 : 0.0);
			nx_1 = 0.0;
		}
		results.vega = forwardPrice * riskFreeDiscount *
                ( (dmuG_dsig + sigG * dsigG_dsig)*Nx_1 + nx_1*dsigG_dsig );

		if (payoff.optionType() == Option.Type.PUT)
			results.vega -= riskFreeDiscount * forwardPrice *
                                           (dmuG_dsig + sigG * dsigG_dsig);

		/*@Time*/ double tRho = rfdc.yearFraction(process.riskFreeRate().getLink().referenceDate(),
									  arguments.exercise.lastDate());
		results.rho = black.rho(tRho)*timeSum/(N*tRho) 
                   - (tRho-timeSum/N)*results.value;

		/*@Time*/ double tDiv = divdc.yearFraction(
                        process.dividendYield().getLink().referenceDate(),
                        arguments.exercise.lastDate());

     results.dividendRho = black.dividendRho(tDiv)*timeSum/(N*tDiv);

     results.strikeSensitivity = black.strikeSensitivity();

     results.theta = Greeks.blackScholesTheta(process,
                                        results.value,
                                        results.delta,
										results.gamma);
	}
	
}
