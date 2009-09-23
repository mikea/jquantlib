/*
 Copyright (C) 2009 Daniel Kong

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

package org.jquantlib.examples;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.QL;
import org.jquantlib.Settings;
import org.jquantlib.cashflow.Callability;
import org.jquantlib.cashflow.Dividend;
import org.jquantlib.cashflow.FixedDividend;
import org.jquantlib.daycounters.Actual365Fixed;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.daycounters.Thirty360;
import org.jquantlib.exercise.AmericanExercise;
import org.jquantlib.exercise.EuropeanExercise;
import org.jquantlib.exercise.Exercise;
import org.jquantlib.instruments.ConvertibleFixedCouponBond;
import org.jquantlib.instruments.Option;
import org.jquantlib.instruments.SoftCallability;
import org.jquantlib.methods.lattices.JarrowRudd;
import org.jquantlib.pricingengines.BinomialConvertibleEngine;
import org.jquantlib.pricingengines.PricingEngine;
import org.jquantlib.processes.BlackScholesMertonProcess;
import org.jquantlib.processes.StochasticProcess;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.termstructures.BlackVolTermStructure;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.termstructures.volatilities.BlackConstantVol;
import org.jquantlib.termstructures.yieldcurves.FlatForward;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.DateGenerationRule;
import org.jquantlib.time.Frequency;
import org.jquantlib.time.Period;
import org.jquantlib.time.Schedule;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.time.calendars.Target;
import org.jquantlib.util.StopClock;

/**
 * This example evaluates convertible bond prices.
 *
 * @see http://quantlib.org/reference/_convertible_bonds_8cpp-example.html
 *
 * @author Daniel Kong
 */
//TODO: Work in progress ---- PLEASE INDICATE WHEN WORKING ON THIS EXAMPLE - Ueli
public class ConvertibleBonds {

    public ConvertibleBonds(){
        if (System.getProperty("EXPERIMENTAL")==null) {
            throw new UnsupportedOperationException("Work in progress");
        }
        QL.info("\n\n::::: "+ConvertibleBonds.class.getSimpleName()+" :::::");
    }

    public void run(){
        // Debugging...

        final StopClock clock = new StopClock();
        clock.startClock();
        QL.info("Started calculation at: " + clock.getElapsedTime());

        // actually never used.....
        final Option.Type type = Option.Type.PUT;

        final double underlying = 36.0;
        final double spreadRate = 0.005;

        final double dividendYield = 0.02;
        final double riskFreeRate = 0.06;
        final double volatility = 0.20;

        final int settlementDays = 3;
        final int length = 5;
        final double redemption = 100.0;
        final double conversionRatio = redemption/underlying;

        final Calendar calendar = Target.getCalendar();
        //adjust today to the next business day...
        final Date today = calendar.adjust(new Date().statics().todaysDate());
        QL.info("Today's date is adjusted by the default business day convention is: " + today.shortDate());
        // set the evaluation date to the adjusted today's date
        new Settings().setEvaluationDate(today);
        QL.info("Set the global evaluation date to the adjusted today's date: " + today.shortDate());

        //Set up settlement, exercise and issue dates
        final Date settlementDate = calendar.advance(today, settlementDays, TimeUnit.DAYS);
        QL.info("SettlementDate is: " + settlementDate.shortDate());
        QL.info("Check that we haven't messed up with references --> today's date is still: " + today.shortDate());
        final Date exerciseDate = calendar.advance(settlementDate, length, TimeUnit.YEARS);
        QL.info("Excercise date is: " + exerciseDate.shortDate());
        final Date issueDate = calendar.advance(exerciseDate, -length, TimeUnit.YEARS);
        QL.info("Issue date is: " + issueDate.shortDate());

        //Fix business day convention and compounding?? frequency
        final BusinessDayConvention convention = BusinessDayConvention.MODIFIED_FOLLOWING;
        final Frequency frequency = Frequency.ANNUAL;

        final Schedule schedule = new Schedule(
                issueDate, exerciseDate,
                new Period(frequency), calendar,
                convention, convention,
                DateGenerationRule.BACKWARD, false);

        final List<Dividend> dividends = new ArrayList<Dividend>();
        final List<Callability> callability = new ArrayList<Callability>();

        final List<Double> coupons = new ArrayList<Double>();
        coupons.add(1.0);
        coupons.add(0.05);

        final DayCounter bondDayCount = Thirty360.getDayCounter();

        // Call dates, years 2, 4
        final int[] callLength = { 2, 4 };
        // Put dates year 3
        final int[] putLength = { 3 };

        final double[] callPrices = {101.5, 100.85};
        final double[] putPrices = { 105.0 };

        for(int i=0; i<callLength.length; i++){
            callability.add(new SoftCallability(new Callability.Price(callPrices[i], Callability.Price.Type.CLEAN),
                    schedule.date(callLength[i]),
                    1.20));
        }

        for (int j=0; j<putLength.length; j++) {
            callability.add(new Callability(new Callability.Price(putPrices[j],Callability.Price.Type.CLEAN),
                    Callability.Type.PUT,
                    schedule.date(putLength[j])));
        }

        // Assume dividends are paid every 6 months.
        for (final Date d = today.add(new Period(6, TimeUnit.MONTHS)); d.lt(exerciseDate); d.addAssign(new Period(6, TimeUnit.MONTHS))) {
            dividends.add(new FixedDividend(1.0, d));
        }

        final DayCounter dayCounter = Actual365Fixed.getDayCounter();
        /*@Time*/ final double maturity = dayCounter.yearFraction(settlementDate,exerciseDate);

        System.out.println("option type = "+type);
        System.out.println("Time to maturity = "+maturity);
        System.out.println("Underlying price = "+underlying);
        System.out.println("Risk-free interest rate = "+riskFreeRate);
        System.out.println("Dividend yield = "+dividendYield);
        System.out.println("Volatility = "+volatility);
        System.out.println("");
        System.out.println("");

        // write column headings
        final int widths[] = { 35, 14, 14 };
        final int totalWidth = widths[0] + widths[1] + widths[2];
        final StringBuilder ruleBuilder = new StringBuilder();
        final StringBuilder dblruleBuilder = new StringBuilder();
        for(int i=0; i<totalWidth; i++){
            ruleBuilder.append('-');
            dblruleBuilder.append('=');
        }
        final String rule = ruleBuilder.toString(), dblrule=dblruleBuilder.toString();

        System.out.println(dblrule);
        System.out.println("Tsiveriotis-Fernandes method");
        System.out.println(dblrule);
        //        std::cout << std::setw(widths[0]) << std::left << "Tree type"
        //                  << std::setw(widths[1]) << std::left << "European"
        //                  << std::setw(widths[1]) << std::left << "American"
        //                  << std::endl;
        System.out.println(rule);

        final Exercise exercise = new EuropeanExercise(exerciseDate);
        final Exercise amExercise = new AmericanExercise(settlementDate,exerciseDate);

        final Handle<Quote> underlyingH = new Handle (new SimpleQuote(underlying));
        final Handle<YieldTermStructure> flatTermStructure = new Handle( new FlatForward(settlementDate, riskFreeRate, dayCounter));
        final Handle<YieldTermStructure> flatDividendTS = new Handle(new FlatForward(settlementDate, dividendYield, dayCounter));
        final Handle<BlackVolTermStructure> flatVolTS = new Handle(new BlackConstantVol(settlementDate, volatility, dayCounter));

        final StochasticProcess stochasticProcess = new BlackScholesMertonProcess(underlyingH,
                flatDividendTS,
                flatTermStructure,
                flatVolTS);

        final int timeSteps = 801;

        final Handle<Quote> creditSpread = new Handle(new SimpleQuote(spreadRate));
        final Quote rate = new SimpleQuote(riskFreeRate);

        final Handle<YieldTermStructure> discountCurve = new Handle(new FlatForward(today, new Handle<Quote>(rate), dayCounter));

        final PricingEngine engine = new BinomialConvertibleEngine<JarrowRudd>(timeSteps);

        final ConvertibleFixedCouponBond europeanBond = new ConvertibleFixedCouponBond(
                stochasticProcess, exercise, engine,
                conversionRatio, dividends, callability,
                creditSpread, issueDate, settlementDays,
                coupons, bondDayCount, schedule, redemption);

        final ConvertibleFixedCouponBond americanBond = new ConvertibleFixedCouponBond(
                stochasticProcess, amExercise, engine,
                conversionRatio, dividends, callability,
                creditSpread, issueDate, settlementDays,
                coupons, bondDayCount, schedule, redemption);
        final String method = "Jarrow-Rudd";
        europeanBond.setPricingEngine(new BinomialConvertibleEngine<JarrowRudd>(timeSteps));
        americanBond.setPricingEngine(new BinomialConvertibleEngine<JarrowRudd>(timeSteps));
        //        std::cout << std::setw(widths[0]) << std::left << method
        //                  << std::fixed
        //                  << std::setw(widths[1]) << std::left << europeanBond.NPV()
        //                  << std::setw(widths[2]) << std::left << americanBond.NPV()
        //                  << std::endl;


        //TODO: Work in progress

        clock.stopClock();
        clock.log();
    }

    public static void main (final String [] args){
        new ConvertibleBonds().run();
    }

}
