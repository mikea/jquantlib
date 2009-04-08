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

/**
 *
 * @author Praneet Tiwari
 */
public class OptionHelper {

 public static double europeanBlackScholes(double strike, double underlying, double riskFreeRate, double volatility, double dividendYield, String optionType, int settlementDay, int settlementMonth, int settlementYear, int maturityDay, int maturityMonth, int maturityYear) {


       Date settlementDate = DateFactory.getFactory().getDate(settlementDay, settlementMonth, settlementYear);
        Date maturityDate = DateFactory.getFactory().getDate(maturityDay, maturityMonth, maturityYear);
        Configuration.getSystemConfiguration(null).getGlobalSettings().setEvaluationDate(settlementDate);
        DayCounter dayCounter = Actual365Fixed.getDayCounter();
        //let's find the option type
        Option.Type type;

        if(optionType.equalsIgnoreCase("call") || optionType.equalsIgnoreCase("c")){
            type = Option.Type.CALL;
        }
        else   if(optionType.equalsIgnoreCase("put") || optionType.equalsIgnoreCase("p")){
            type = Option.Type.PUT;
        }
        else throw new IllegalArgumentException("Invalid option type");


     //   type = Option.Type.CALL;
        Exercise europeanExercise = new EuropeanExercise(maturityDate);
        Payoff payoff = new PlainVanillaPayoff(type, strike);

        Handle<Quote> underlyingH = new Handle<Quote>(new SimpleQuote(underlying));
        Handle<YieldTermStructure> flatDividendTS = new Handle<YieldTermStructure>(new FlatForward(settlementDate, dividendYield, dayCounter));
        Handle<YieldTermStructure> flatTermStructure = new Handle<YieldTermStructure>(new FlatForward(settlementDate, riskFreeRate, dayCounter));
        Handle<BlackVolTermStructure> flatVolTS = new Handle<BlackVolTermStructure>(new BlackConstantVol(settlementDate, volatility, dayCounter));

        StochasticProcess stochasticProcess = new BlackScholesMertonProcess(underlyingH, flatDividendTS, flatTermStructure, flatVolTS);

        VanillaOption europeanOption = new EuropeanOption(stochasticProcess, payoff, europeanExercise);

        europeanOption.setPricingEngine(new AnalyticEuropeanEngine());

        return europeanOption.getNPV();

 }
 public static void main(String args[]){
  System.out.println(europeanBlackScholes(30, 4030, 0.05, 0.2, 0.0, "put", 1, 1, 2011, 1, 2, 2011))   ;
 }
}