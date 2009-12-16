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

package org.jquantlib.samples;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.Settings;
import org.jquantlib.daycounters.Actual365Fixed;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.exercise.AmericanExercise;
import org.jquantlib.exercise.EuropeanExercise;
import org.jquantlib.exercise.Exercise;
import org.jquantlib.helpers.AmericanDividendOptionHelper;
import org.jquantlib.helpers.EuropeanDividendOptionHelper;
import org.jquantlib.helpers.FDAmericanDividendOptionHelper;
import org.jquantlib.helpers.FDEuropeanDividendOptionHelper;
import org.jquantlib.instruments.Option;
import org.jquantlib.instruments.Payoff;
import org.jquantlib.instruments.PlainVanillaPayoff;
import org.jquantlib.instruments.VanillaOption;
import org.jquantlib.methods.lattices.CoxRossRubinstein;
import org.jquantlib.pricingengines.AnalyticEuropeanEngine;
import org.jquantlib.pricingengines.PricingEngine;
import org.jquantlib.pricingengines.vanilla.BinomialVanillaEngine;
import org.jquantlib.processes.BlackScholesMertonProcess;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.termstructures.BlackVolTermStructure;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.termstructures.volatilities.BlackConstantVol;
import org.jquantlib.termstructures.yieldcurves.FlatForward;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.Month;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.time.calendars.Target;

/**
 * This sample demonstrates how european options and american options can be calculated and
 * also employs helper classes in order to perform these tasks.
 */
public class HelloOptions implements Runnable {

    public static void main(final String[] args) {
        new HelloOptions().run();
    }

    final private Calendar calendar;
    final private Date today;
    final private Date settlementDate;
    final private Option.Type type;
    final private double strike;
    final private double underlying;
    final private /*@Rate*/ double riskFreeRate;
    final private /*@Volatility*/ double volatility;
    final private double dividendYield;
    final private Date maturityDate;
    final private DayCounter dc;
    final private List<Date> divDates;
    final private List<Double> divAmounts;


    public HelloOptions() {
        this.calendar = new Target();
        this.today = new Date(15, Month.May, 1998);
        this.settlementDate = new Date(17, Month.May, 1998);

        this.type = Option.Type.Put;
        this.strike = 40.0;
        this.underlying = 36.0;
        this.riskFreeRate = 0.06;
        this.volatility = 0.2;
        this.dividendYield = 0.00;

        this.maturityDate = new Date(17, Month.May, 1999);
        this.dc = new Actual365Fixed();

        this.divDates = new ArrayList<Date>();
        this.divAmounts = new ArrayList<Double>();
        for (int i=1; i<=3; i++) {
            final Date divDate = today.add(new Period(i*3, TimeUnit.Months)).add(new Period(15, TimeUnit.Days));
            divDates.add(divDate);
            divAmounts.add(2.06);
        }
    }

    public void runEuropeanFD() {
        System.out.println("===== runEuropeanFD :: FDEuropeanDividendOptionHelper =====");

        new Settings().setEvaluationDate(today);

        final FDEuropeanDividendOptionHelper option = new FDEuropeanDividendOptionHelper(
                type, underlying, strike, riskFreeRate, dividendYield, volatility,
                settlementDate, maturityDate,
                divDates, divAmounts,
                calendar, dc);

        final double value  = option.NPV();
        final double delta  = option.delta();
        final double gamma  = option.gamma();
        final double theta  = option.theta();
        final double vega   = option.vega();
        final double rho    = option.rho();

        System.out.println(String.format("value       = %13.9f", value));
        System.out.println(String.format("delta       = %13.9f", delta));
        System.out.println(String.format("gamma       = %13.9f", gamma));
        System.out.println(String.format("theta       = %13.9f", theta));
        System.out.println(String.format("vega        = %13.9f", vega));
        System.out.println(String.format("rho         = %13.9f", rho));

        // market price: simply guess something 10% higher than theoretical
        // final double ivol = option.impliedVolatility(value*1.10);
        // System.out.println(String.format("implied vol = %13.9f", ivol));
    }


    public void runAmericanFD() {
        System.out.println("===== runAmericanFD :: FDAmericanDividendOptionHelper =====");

        new Settings().setEvaluationDate(today);

        final FDAmericanDividendOptionHelper option = new FDAmericanDividendOptionHelper(
                type, underlying, strike, riskFreeRate, dividendYield, volatility,
                settlementDate, maturityDate,
                divDates, divAmounts,
                calendar, dc);

        final double value  = option.NPV();
        final double delta  = option.delta();
        final double gamma  = option.gamma();
        final double theta  = option.theta();
        final double vega   = option.vega();
        final double rho    = option.rho();

        System.out.println(String.format("value       = %13.9f", value));
        System.out.println(String.format("delta       = %13.9f", delta));
        System.out.println(String.format("gamma       = %13.9f", gamma));
        System.out.println(String.format("theta       = %13.9f", theta));
        System.out.println(String.format("vega        = %13.9f", vega));
        System.out.println(String.format("rho         = %13.9f", rho));

        // market price: simply guess something 10% higher than theoretical
        // final double ivol = option.impliedVolatility(value*1.10);
        // System.out.println(String.format("implied vol = %13.9f", ivol));
    }


    public void runEuropeanCRR() {
        System.out.println("===== runEuropeanCRR :: EuropeanDividendOptionHelper =====");

        new Settings().setEvaluationDate(today);

        final EuropeanDividendOptionHelper option = new EuropeanDividendOptionHelper(
                type, underlying, strike, riskFreeRate, dividendYield, volatility,
                settlementDate, maturityDate,
                divDates, divAmounts,
                calendar, dc);

        final double value  = option.NPV();
        final double delta  = option.delta();
        final double gamma  = option.gamma();
        final double theta  = option.theta();
        // final double vega   = option.vega(); // to be implemented
        // final double rho    = option.rho();  // to be implemented

        System.out.println(String.format("value       = %13.9f", value));
        System.out.println(String.format("delta       = %13.9f", delta));
        System.out.println(String.format("gamma       = %13.9f", gamma));
        System.out.println(String.format("theta       = %13.9f", theta));
        // System.out.println(String.format("vega        = %13.9f", vega));
        // System.out.println(String.format("rho         = %13.9f", rho));

        // market price: simply guess something 10% higher than theoretical
        // final double ivol = option.impliedVolatility(value*1.10);
        // System.out.println(String.format("implied vol = %13.9f", ivol));
    }

    public void runAmericanCRR() {
        System.out.println("===== runAmericanCRR :: AmericanDividendOptionHelper =====");

        new Settings().setEvaluationDate(today);

        final AmericanDividendOptionHelper option = new AmericanDividendOptionHelper(
                type, underlying, strike, riskFreeRate, dividendYield, volatility,
                settlementDate, maturityDate,
                divDates, divAmounts,
                calendar, dc);

        final double value  = option.NPV();
        final double delta  = option.delta();
        final double gamma  = option.gamma();
        final double theta  = option.theta();
        // final double vega   = option.vega(); // to be implemented
        // final double rho    = option.rho();  // to be implemented

        System.out.println(String.format("value       = %13.9f", value));
        System.out.println(String.format("delta       = %13.9f", delta));
        System.out.println(String.format("gamma       = %13.9f", gamma));
        System.out.println(String.format("theta       = %13.9f", theta));
        // System.out.println(String.format("vega        = %13.9f", vega));
        // System.out.println(String.format("rho         = %13.9f", rho));

        // market price: simply guess something 10% higher than theoretical
        // final double ivol = option.impliedVolatility(value*1.10);
        // System.out.println(String.format("implied vol = %13.9f", ivol));
    }

    public void runByHand() {
        System.out.println("===== runByHand =====");

        new Settings().setEvaluationDate(today);

        final Payoff payoff = new PlainVanillaPayoff(type, strike);

        // bootstrap the yield/dividend/volatility curves
        final SimpleQuote spot = new SimpleQuote(0.0);
        final Handle<YieldTermStructure> qTS = new Handle<YieldTermStructure>(flatRate(settlementDate, dividendYield, dc));
        final Handle<YieldTermStructure> rTS = new Handle<YieldTermStructure>(flatRate(settlementDate, riskFreeRate, dc));
        final Handle<BlackVolTermStructure> volTS = new Handle<BlackVolTermStructure>(flatVol(settlementDate, volatility, calendar, dc));

        // Black Scholes Merton stochastic process
        final BlackScholesMertonProcess bsmProcess = new BlackScholesMertonProcess(new Handle<Quote>(spot), qTS, rTS, volTS);

        // European Options
        final Exercise europeanExercise = new EuropeanExercise(maturityDate);
        final VanillaOption europeanOption = new VanillaOption(payoff, europeanExercise);
        final PricingEngine europeanEngine = new AnalyticEuropeanEngine(bsmProcess);
        europeanOption.setPricingEngine(europeanEngine);

        // American Options
        final Exercise americanExercise = new AmericanExercise(settlementDate, maturityDate);
        final VanillaOption americanOption = new VanillaOption(payoff, americanExercise);
        // obtain a pricing engine and assign to this option :: 3 intervals a day
        final int timeSteps = maturityDate.sub(settlementDate) * 3;
        final PricingEngine americanEngine = new BinomialVanillaEngine<CoxRossRubinstein>(bsmProcess, timeSteps) { /* anonymous class */ };
        americanOption.setPricingEngine(americanEngine);

        // change underlying, which triggers the notification process
        spot.setValue(underlying);

        // FIXME: call engine.name() instead of engine.getClass().getSimpleName() :: http://bugs.jquantlib.org/view.php?id=448
        System.out.printf("European Option = %13.9f  (%s)\n", europeanOption.NPV(), europeanEngine.getClass().getSimpleName());
        System.out.printf("American Option = %13.9f  (%s)\n", americanOption.NPV(), americanEngine.getClass().getSimpleName());
    }


    public void run() {

        runEuropeanFD();     System.out.println();
        runEuropeanCRR();    System.out.println();

        runAmericanFD();     System.out.println();
        runAmericanCRR();    System.out.println();

        runByHand();         System.out.println();
    }


    //
    // private methods
    //

    private YieldTermStructure flatRate(final Date referenceDate, final double rate, final DayCounter dc) {
        return new FlatForward(referenceDate, rate, dc);
    }


    private BlackVolTermStructure flatVol(final Date referenceDate, final double vol, final Calendar cal, final DayCounter dc) {
        return new BlackConstantVol(referenceDate, cal, vol, dc);
    }

}
