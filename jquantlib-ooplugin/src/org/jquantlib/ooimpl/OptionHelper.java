/*
 Copyright (C) 2008 Praneet Tiwari

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
package org.jquantlib.ooimpl;

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
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.termstructures.BlackVolTermStructure;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.termstructures.volatilities.BlackConstantVol;
import org.jquantlib.termstructures.yieldcurves.FlatForward;
import org.jquantlib.time.Date;
import org.jquantlib.time.calendars.NullCalendar;

/**
 *
 * @author Praneet Tiwari
 */
public class OptionHelper {

 public static double europeanBlackScholes(final double strike, final double underlying, final double riskFreeRate, final double volatility, final double dividendYield, final String optionType, final int settlementDay, final int settlementMonth, final int settlementYear, final int maturityDay, final int maturityMonth, final int maturityYear) {


       final Date settlementDate = new Date(settlementDay, settlementMonth, settlementYear);
        final Date maturityDate = new Date(maturityDay, maturityMonth, maturityYear);
        Configuration.getSystemConfiguration(null).getGlobalSettings().setEvaluationDate(settlementDate);
        final DayCounter dayCounter = new Actual365Fixed();
        //let's find the option type
        Option.Type type;

        if(optionType.equalsIgnoreCase("call") || optionType.equalsIgnoreCase("c"))
            type = Option.Type.Call;
        else   if(optionType.equalsIgnoreCase("put") || optionType.equalsIgnoreCase("p"))
            type = Option.Type.Put;
        else throw new IllegalArgumentException("Invalid option type");


     //   type = Option.Type.CALL;
        final Exercise europeanExercise = new EuropeanExercise(maturityDate);
        final Payoff payoff = new PlainVanillaPayoff(type, strike);

        final Handle<Quote> underlyingH = new Handle<Quote>(new SimpleQuote(underlying));
        final Handle<YieldTermStructure> flatDividendTS = new Handle<YieldTermStructure>(new FlatForward(settlementDate, dividendYield, dayCounter));
        final Handle<YieldTermStructure> flatTermStructure = new Handle<YieldTermStructure>(new FlatForward(settlementDate, riskFreeRate, dayCounter));
        final Handle<BlackVolTermStructure> flatVolTS = new Handle<BlackVolTermStructure>(new BlackConstantVol(settlementDate, new NullCalendar(), volatility, dayCounter));

        final BlackScholesMertonProcess bsmProcess = new BlackScholesMertonProcess(underlyingH, flatDividendTS, flatTermStructure, flatVolTS);

        final VanillaOption europeanOption = new EuropeanOption(payoff, europeanExercise);

        europeanOption.setPricingEngine(new AnalyticEuropeanEngine(bsmProcess));

        return europeanOption.NPV();

 }

// public static void main(final String args[]){
//  System.out.println(europeanBlackScholes(30, 4030, 0.05, 0.2, 0.0, "put", 1, 1, 2011, 1, 2, 2011))   ;
// }

}