/*
 Copyright (C) 2009 Q.Boiler, Ueli Hofstetter
 
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

package org.jquantlib.examples.utils;



import org.jquantlib.daycounters.Actual365Fixed;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.instruments.Option;
import org.jquantlib.instruments.Payoff;
import org.jquantlib.instruments.PlainVanillaPayoff;
import org.jquantlib.instruments.StrikedTypePayoff;
import org.jquantlib.instruments.TypePayoff;
import org.jquantlib.math.distributions.InverseCumulativeNormal;
import org.jquantlib.math.randomnumbers.InverseCumulative;
import org.jquantlib.math.randomnumbers.InverseCumulativeRsg;
import org.jquantlib.math.randomnumbers.MersenneTwisterUniformRng;
import org.jquantlib.math.randomnumbers.PseudoRandom;
import org.jquantlib.math.randomnumbers.RandomNumberGenerator;
import org.jquantlib.math.randomnumbers.RandomSequenceGenerator;
import org.jquantlib.math.randomnumbers.RandomSequenceGeneratorIntf;
import org.jquantlib.math.statistics.Statistics;
import org.jquantlib.methods.montecarlo.MonteCarloModel;
import org.jquantlib.methods.montecarlo.PathGenerator;
import org.jquantlib.methods.montecarlo.SingleVariate;
import org.jquantlib.pricingengines.BlackCalculator;
import org.jquantlib.processes.BlackScholesMertonProcess;
import org.jquantlib.processes.StochasticProcess1D;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.termstructures.BlackVolTermStructure;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.termstructures.volatilities.BlackConstantVol;
import org.jquantlib.termstructures.yieldcurves.FlatForward;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Target;
import org.jquantlib.util.Date;
import org.jquantlib.util.DefaultDate;

public class ReplicationError {
	
	private/* @Time */Number maturity_;
	private PlainVanillaPayoff payoff_;
	private/* @Price */Number s0_;
	private/* @Volatility */Number sigma_;
	private/* @Rate */Number r_;
	private/* @Real */Number vega_;

	public ReplicationError(final Option.Type type, final/* @Time */Number maturity,
			final/* @Price */Number strike, final/* @Price */Number s0,
			final/* @Volatility */Number sigma, final/* @Rate */Number r)
	{
		if (System.getProperty("EXPERIMENTAL") == null) {
			throw new UnsupportedOperationException("Work in progress");
		}
		
		
		this.maturity_ = maturity;
		payoff_ = new PlainVanillaPayoff(type, strike.doubleValue());
		this.s0_ = s0;
		this.sigma_ = sigma;
		this.r_ = r;
		
		double rDiscount = Math.exp(- (r.doubleValue()) * maturity_.doubleValue());
		double qDiscount = 1.0;
		double forward = s0_.doubleValue() * qDiscount/rDiscount;
		double stdDev = Math.sqrt(sigma_.doubleValue() * sigma_.doubleValue() * maturity_.doubleValue());
		//TODO:boost::shared_ptr<StrikedTypePayoff> payoff(new PlainVanillaPayoff(payoff_));
		BlackCalculator black = new BlackCalculator(payoff_,forward,stdDev,rDiscount);
		
		System.out.println("Option value: " + black.value());
		// store option's vega, since Derman and Kamal's formula needs it
		vega_ = black.vega(maturity.doubleValue());
		System.out.println("Vega: " + vega_);
	}



	public void compute(int nTimeSteps, int nSamples) {
		
		if (System.getProperty("EXPERIMENTAL") == null) {
			throw new UnsupportedOperationException("Work in progress");
		}
		
		Calendar calendar = Target.getCalendar();
		Date today = DefaultDate.getTodaysDate();
		DayCounter dayCount = Actual365Fixed.getDayCounter();
		Handle<Quote> stateVariable = new Handle(new SimpleQuote(s0_.doubleValue()));
		Handle<YieldTermStructure> riskFreeRate = new Handle(new FlatForward(today, r_.doubleValue(), dayCount));

		Handle<YieldTermStructure> dividendYield = new Handle(new FlatForward(today, 0.0, dayCount));

		Handle<BlackVolTermStructure> volatility = new Handle(new BlackConstantVol(today, calendar, sigma_.doubleValue(),dayCount));

		StochasticProcess1D diffusion = new BlackScholesMertonProcess(
				stateVariable, dividendYield, riskFreeRate, volatility);
		
		
		// Black Scholes equation rules the path generator:
		// at each step the log of the stock
		// will have drift and sigma^2 variance
		InverseCumulativeRsg<RandomSequenceGenerator<MersenneTwisterUniformRng>, InverseCumulativeNormal> 
		rsg = new PseudoRandom().makeSequenceGenerator(nTimeSteps, 0);
		
        boolean brownianBridge = false;
         
		MonteCarloModel<SingleVariate, RandomNumberGenerator, Statistics> MCSimulation = new MonteCarloModel<SingleVariate, RandomNumberGenerator, Statistics>();

		// the model simulates nSamples paths
		MCSimulation.addSamples(nSamples);

		// the sampleAccumulator method
		// gives access to all the methods of statisticsAccumulator
		Statistics s = MCSimulation.sampleAccumulator();

		/* @Real */double PLMean = s.mean();
		/* @Real */double PLStDev = MCSimulation.sampleAccumulator()
				.standardDeviation();
		/* @Real */double PLSkew = MCSimulation.sampleAccumulator().skewness();
		/* @Real */double PLKurt = MCSimulation.sampleAccumulator().kurtosis();

		// Derman and Kamal's formula
		/* @Real */double theorStD = Math.sqrt((Math.PI / 4 / nTimeSteps)
				* vega_.doubleValue() * sigma_.doubleValue());

		StringBuffer sb = new StringBuffer();
		sb.append(nSamples).append(" | ");
		sb.append(nTimeSteps).append(" | ");
		sb.append(PLMean).append(" | ");
		sb.append(PLStDev).append(" | ");
		sb.append(theorStD).append(" | ");
		sb.append(PLSkew).append(" | ");
		sb.append(PLKurt).append(" \n");

		System.out.println(sb.toString());
	}
}
