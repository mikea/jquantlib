/*
 Copyright (C) 2007 Richard Gomes

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
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
 Quantlib license

 QuantLib is
 Copyright (C) 2002, 2003, 2004, 2005 Ferdinando Ametrano
 Copyright (C) 2000, 2001, 2002, 2003, 2004, 2005 StatPro Italia srl

 Copyright (C) 2002, 2003, 2004 Decillion Pty(Ltd)
 Copyright (C) 2001, 2002, 2003 Nicolas Di C�sar�
 Copyright (C) 2003, 2004 Neil Firth
 Copyright (C) 2001, 2002, 2003 Sadruddin Rejeb
 Copyright (C) 2003 Niels Elken S�nderby

 Copyright (C) 2004 FIMAT Group
 Copyright (C) 2003, 2004 Roman Gitlin
 Copyright (C) 2004 M-Dimension Consulting Inc.
 Copyright (C) 2004 Mike Parker
 Copyright (C) 2004 Walter Penschke
 Copyright (C) 2004 Gianni Piolanti
 Copyright (C) 2003 Kawanishi Tomoya
 Copyright (C) 2004 Jeff Yu

 Copyright (C) 2005 Serkan Atalik
 Copyright (C) 2005 Gary Kennedy
 Copyright (C) 2005 Klaus Spanderen
 Copyright (C) 2005 Joseph Wang
 Copyright (C) 2005 Charles Whitmore

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

 Redistributions of source code must retain the above copyright notice,
 this list of conditions and the following disclaimer.

 Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.

 Neither the names of the copyright holders nor the names of the QuantLib
 Group and its contributors may be used to endorse or promote products
 derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE LIABLE
 FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 **/

package org.jquantlib.examples;

import org.jquantlib.Configuration;
import org.jquantlib.daycounters.Actual365Fixed;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.exercise.EuropeanExercise;
import org.jquantlib.exercise.Exercise;
import org.jquantlib.instruments.EuropeanOption;
import org.jquantlib.instruments.Option;
import org.jquantlib.instruments.Payoff;
import org.jquantlib.instruments.PlainVanillaPayoff;
import org.jquantlib.instruments.VanillaOption;
import org.jquantlib.pricingengines.AnalyticEuropeanEngine;
import org.jquantlib.processes.BlackScholesMertonProcess;
import org.jquantlib.processes.StochasticProcess;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.termstructures.BlackVolTermStructure;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.termstructures.volatilities.BlackConstantVol;
import org.jquantlib.termstructures.yieldcurves.FlatForward;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.jquantlib.util.Month;
import org.jquantlib.util.StopClock;

/**
 * Calculates equity option values with a number of methods
 * 
 * @see http://quantlib.org/reference/_equity_option_8cpp-example.html
 * 
 * @author Richard Gomes
 */
public class EquityOptions {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		System.out.println("\n\n::::: "+EquityOptions.class.getSimpleName()+" :::::");

		StopClock clock = new StopClock();
		clock.startClock();

		Option.Type type = Option.Type.PUT;
		double strike = 40.0;
		double underlying = 36.0;
		/*@Rate*/double riskFreeRate = 0.06;
		double volatility = 0.2;
		double dividendYield = 0.00;

		Date todaysDate = DateFactory.getFactory().getDate(15, Month.MAY, 1998);
		Date settlementDate = DateFactory.getFactory().getDate(17, Month.MAY, 1998);
		Configuration.getSystemConfiguration(null).getGlobalSettings().setEvaluationDate(todaysDate);

		Date maturity = DateFactory.getFactory().getDate(17, Month.MAY, 1999);
		DayCounter dayCounter = Actual365Fixed.getDayCounter();

		// write column headings
		//                 "         1         2         3         4         5         6         7         8"
		//                 "12345678901234567890123456789012345678901234567890123456789012345678901234567890"
		System.out.println("                            Method      European      Bermudan      American");
		System.out.println("================================== ============= ============= =============");

		// Define exercise for European Options
		Exercise europeanExercise = new EuropeanExercise(maturity);

		//    // Define exercise for Bermudan Options
		//    int bermudanForwards = 4;
		//    Date[] exerciseDates = new Date[bermudanForwards];
		//    for (int i = 1; i <= bermudanForwards; i++) {
		//        Date forward = settlementDate.add(new Period(3 * i, TimeUnit.Months));
		//        exerciseDates[i] = forward;
		//    }
		//    Exercise bermudanExercise = new BermudanExercise(exerciseDates);

		//    // Define exercise for American Options
		//    Exercise americanExercise = new AmericanExercise(settlementDate, maturity);

		// bootstrap the yield/dividend/volatility curves
		Handle<Quote> underlyingH = new Handle<Quote>(new SimpleQuote(underlying));
		Handle<YieldTermStructure> flatDividendTS = new Handle<YieldTermStructure>(new FlatForward(settlementDate, dividendYield,
				dayCounter));
		Handle<YieldTermStructure> flatTermStructure = new Handle<YieldTermStructure>(new FlatForward(settlementDate, riskFreeRate,
				dayCounter));
		Handle<BlackVolTermStructure> flatVolTS = new Handle<BlackVolTermStructure>(new BlackConstantVol(settlementDate,
				volatility, dayCounter));

		Payoff payoff = new PlainVanillaPayoff(type, strike);
		StochasticProcess stochasticProcess = new BlackScholesMertonProcess(underlyingH, flatDividendTS, flatTermStructure,
				flatVolTS);

		// options
		VanillaOption europeanOption = new EuropeanOption(stochasticProcess, payoff, europeanExercise);
		// VanillaOption bermudanOption = new BermudanOption(stochasticProcess, payoff, bermudanExercise);
		// VanillaOption americanOption = new AmericanOption(stochasticProcess, payoff, americanExercise);

		// define line formatting
		//              "         0         1         2         3         4         5         6         7         8"
		//              "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"
		//              "Method                                       European      Bermudan      American      ";
		//              "12345678901234567890123456789012345678901234 123.567890123 123.567890123 123.567890123";
		String fmt    = "%34s %13.9f %13.9f %13.9f\n";
		String fmttbd = "%34s %13.9f %13.9f %13.9f  (TO BE DONE)\n";

		// Analytic formulas:

		// Black-Scholes for European
		String method = "Black-Scholes";
		europeanOption.setPricingEngine(new AnalyticEuropeanEngine());
		System.out.printf(fmt, new Object[] { method, europeanOption.getNPV(), Double.NaN, Double.NaN });

		        // Barone-Adesi and Whaley approximation for American
		        method = "Barone-Adesi/Whaley";
		        System.out.printf(fmttbd, new Object[] { method, Double.NaN, Double.NaN, Double.NaN });
		//        americanOption.setPricingEngine(new BaroneAdesiWhaleyEngine());
		//        System.out.printf(fmt, new Object[] { method, Double.NaN, Double.NaN, americanOption.NPV() } );
		
		        // Bjerksund and Stensland approximation for American
		        method = "Bjerksund/Stensland";
                System.out.printf(fmttbd, new Object[] { method, Double.NaN, Double.NaN, Double.NaN });
		//        americanOption.setPricingEngine(new BjerksundStenslandEngine());
		//        System.out.printf(fmt, new Object[] { method, Double.NaN, Double.NaN, americanOption.NPV() } );
		
		        // Integral
		        method = "Integral";
                System.out.printf(fmttbd, new Object[] { method, Double.NaN, Double.NaN, Double.NaN });
		//        europeanOption.setPricingEngine(new IntegralEngine());
		//        System.out.printf(fmt, new Object[] { method, europeanOption.NPV(), inline Double.NaN, Double.NaN } );
		
		        // Finite differences
		        method = "Finite differences";
                System.out.printf(fmttbd, new Object[] { method, Double.NaN, Double.NaN, Double.NaN });
		//        int timeSteps = 801;
		//        europeanOption.setPricingEngine(new FDEuropeanEngine(timeSteps,timeSteps-1));
		//        bermudanOption.setPricingEngine(new FDBermudanEngine(timeSteps,timeSteps-1));
		//        americanOption.setPricingEngine(new FDAmericanEngine(timeSteps,timeSteps-1));
		//        System.out.printf(fmt, new Object[] { method, europeanOption.NPV(), bermudanOption.NPV(), americanOption.NPV() });
		        
		        // Binomial method
		        method = "Binomial Jarrow-Rudd";
                System.out.printf(fmttbd, new Object[] { method, Double.NaN, Double.NaN, Double.NaN });
		//        europeanOption.setPricingEngine(new BinomialVanillaEngine("JarrowRudd", timeSteps));
		//        bermudanOption.setPricingEngine(new BinomialVanillaEngine("JarrowRudd", timeSteps));
		//        americanOption.setPricingEngine(new BinomialVanillaEngine("JarrowRudd", timeSteps));
		//        System.out.printf(fmt, new Object[] { method, europeanOption.NPV(), bermudanOption.NPV(), americanOption.NPV() } );
		
		        method = "Binomial Cox-Ross-Rubinstein";
                System.out.printf(fmttbd, new Object[] { method, Double.NaN, Double.NaN, Double.NaN });
		//        europeanOption.setPricingEngine(new BinomialVanillaEngine("CoxRossRubinstein", timeSteps));
		//        bermudanOption.setPricingEngine(new BinomialVanillaEngine("CoxRossRubinstein", timeSteps));
		//        americanOption.setPricingEngine(new BinomialVanillaEngine("CoxRossRubinstein", timeSteps));
		//        System.out.printf(fmt, new Object[] { method, europeanOption.NPV(), bermudanOption.NPV(), americanOption.NPV() } );
		
		        method = "Additive equiprobabilities";
                System.out.printf(fmttbd, new Object[] { method, Double.NaN, Double.NaN, Double.NaN });
		//        europeanOption.setPricingEngine(new BinomialVanillaEngine("AdditiveEQPBinomialTree", timeSteps));
		//        bermudanOption.setPricingEngine(new BinomialVanillaEngine("AdditiveEQPBinomialTree", timeSteps));
		//        americanOption.setPricingEngine(new BinomialVanillaEngine("AdditiveEQPBinomialTree", timeSteps));
		//        System.out.printf(fmt, new Object[] { method, europeanOption.NPV(), bermudanOption.NPV(), americanOption.NPV() } );
		
		        method = "Binomial Trigeorgis";
                System.out.printf(fmttbd, new Object[] { method, Double.NaN, Double.NaN, Double.NaN });
		//        europeanOption.setPricingEngine(new BinomialVanillaEngine("Trigeorgis", timeSteps));
		//        bermudanOption.setPricingEngine(new BinomialVanillaEngine("Trigeorgis", timeSteps));
		//        americanOption.setPricingEngine(new BinomialVanillaEngine("Trigeorgis", timeSteps));
		//        System.out.printf(fmt, new Object[] { method, europeanOption.NPV(), bermudanOption.NPV(), americanOption.NPV() } );
		
		        method = "Binomial Tian";
                System.out.printf(fmttbd, new Object[] { method, Double.NaN, Double.NaN, Double.NaN });
		//        europeanOption.setPricingEngine(new BinomialVanillaEngine("Tian", timeSteps));
		//        bermudanOption.setPricingEngine(new BinomialVanillaEngine("Tian", timeSteps));
		//        americanOption.setPricingEngine(new BinomialVanillaEngine("Tian", timeSteps));
		//        System.out.printf(fmt, new Object[] { method, europeanOption.NPV(), bermudanOption.NPV(), americanOption.NPV() } );
		
		        method = "Binomial Leisen-Reimer";
                System.out.printf(fmttbd, new Object[] { method, Double.NaN, Double.NaN, Double.NaN });
		//        europeanOption.setPricingEngine(new BinomialVanillaEngine("LeisenReimer", timeSteps));
		//        bermudanOption.setPricingEngine(new BinomialVanillaEngine("LeisenReimer", timeSteps));
		//        americanOption.setPricingEngine(new BinomialVanillaEngine("LeisenReimer", timeSteps));
		//        System.out.printf(fmt, new Object[] { method, europeanOption.NPV(), bermudanOption.NPV(), americanOption.NPV() } );
		
		        method = "Binomial Joshi";
                System.out.printf(fmttbd, new Object[] { method, Double.NaN, Double.NaN, Double.NaN });
		//        europeanOption.setPricingEngine(new BinomialVanillaEngine("Joshi4", timeSteps));
		//        bermudanOption.setPricingEngine(new BinomialVanillaEngine("Joshi4", timeSteps));
		//        americanOption.setPricingEngine(new BinomialVanillaEngine("Joshi4", timeSteps));
		//        System.out.printf(fmt, new Object[] { method, europeanOption.NPV(), bermudanOption.NPV(), americanOption.NPV() } );
		//
		//
		//        // Monte Carlo Method
		//        timeSteps = 1;
		//        int mcSeed = 42;
		//        int nSamples = 32768; // 2^15
		//        int maxSamples = 1048576; // 2^20
		        
		        method = "Monte Carlo (crude)";
                System.out.printf(fmttbd, new Object[] { method, Double.NaN, Double.NaN, Double.NaN });
		//        europeanOption.setPricingEngine(
		//            new MCEuropeanEngine(
		//                "PseudoRandom", timeSteps, 252,
		//                false, false, false,
		//                nSamples, 0.02, maxSamples, mcSeed));
		//        System.out.printf(fmt, method, europeanOption.NPV(), Double.NaN, Double.NaN);
		
		        method = "Monte Carlo (Sobol)";
                System.out.printf(fmttbd, new Object[] { method, Double.NaN, Double.NaN, Double.NaN });
		//        europeanOption.setPricingEngine(
		//            new MCEuropeanEngine(
		//                "LowDiscrepancy", timeSteps, 252,
		//                false, false, false,
		//                nSamples, 0.02, maxSamples, mcSeed));
		//        System.out.printf(fmt, method, europeanOption.NPV(), Double.NaN, Double.NaN);
		//
		
		        method = "Monte Carlo (Longstaff Schwartz)";
                System.out.printf(fmttbd, new Object[] { method, Double.NaN, Double.NaN, Double.NaN });
		//        MakeMCAmericanEngine<PseudoRandom>().withSteps(100)
		//            .withAntitheticVariate()
		//            .withCalibrationSamples(4096)
		//            .withTolerance(0.02)
		// .           withSeed(mcSeed);
		//        System.out.printf(fmt, method, europeanOption.NPV(), Double.NaN, Double.NaN);

		clock.stopClock();
		clock.log();

	}

}
