/*
 Copyright (C) 2008 Daniel Kong
 
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
import java.util.Arrays;
import java.util.List;

import org.jquantlib.Configuration;
import org.jquantlib.cashflow.Dividend;
import org.jquantlib.cashflow.Callability;
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
import org.jquantlib.time.DateGenerationRule;
import org.jquantlib.time.Frequency;
import org.jquantlib.time.Period;
import org.jquantlib.time.Schedule;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.time.calendars.NullCalendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.jquantlib.util.StopClock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This example evaluates convertible bond prices.
 * 
 * @see http://quantlib.org/reference/_convertible_bonds_8cpp-example.html
 * 
 * @author Daniel Kong
 */
//TODO: Work in progress
public class ConvertibleBonds {

	private final static Logger logger = LoggerFactory.getLogger(ConvertibleBonds.class);
	
	public ConvertibleBonds(){
        if (System.getProperty("EXPERIMENTAL")==null) {
            throw new UnsupportedOperationException("Work in progress");
        }
		logger.info("\n\n::::: "+ConvertibleBonds.class.getSimpleName()+" :::::");		
	}

	public void run(){
		StopClock clock = new StopClock();
		clock.startClock();
		
		Option.Type type = Option.Type.PUT;
		
		double underlying = 36.0;
        double spreadRate = 0.005;

        double dividendYield = 0.02;
        double riskFreeRate = 0.06;
        double volatility = 0.20;

        Integer settlementDays = 3;
        Integer length = 5;
        double redemption = 100.0;
        double conversionRatio = redemption/underlying; 
        
        Calendar calendar = new NullCalendar();
        Date today = calendar.adjust(DateFactory.getFactory().getTodaysDate(), BusinessDayConvention.FOLLOWING);
        
        Configuration.getSystemConfiguration(null).getGlobalSettings().setEvaluationDate(today);
        Date settlementDate = calendar.advance(today, settlementDays, TimeUnit.DAYS);
        Date exerciseDate = calendar.advance(settlementDate, length, TimeUnit.YEARS);
        Date issueDate = calendar.advance(exerciseDate, -length, TimeUnit.YEARS);
        
        BusinessDayConvention convention = BusinessDayConvention.MODIFIED_FOLLOWING;
        
        Frequency frequency = Frequency.ANNUAL;
        
//  ??      Schedule schedule = new Schedule(issueDate, exerciseDate,new Period(frequency),calendar, convention, convention, true, false);
        Schedule schedule = new Schedule(issueDate, exerciseDate,new Period(frequency),calendar, 
        									convention, convention, DateGenerationRule.BACKWARD, false, 
        									Date.NULL_DATE, Date.NULL_DATE);
        
        List<Dividend> dividends = new ArrayList<Dividend>();
        List<Callability> callability = new ArrayList<Callability>();
        
        List<Double> coupons = new ArrayList<Double>();
        coupons.add(1.0);
        coupons.add(0.05);

        DayCounter bondDayCount = Thirty360.getDayCounter();
        
        // Call dates, years 2, 4        
        int[] callLength = { 2, 4 };
        // Put dates year 3
        int[] putLength = { 3 };
        
        double[] callPrices = {101.5, 100.85};
        double[] putPrices = { 105.0 };
        
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
        for (Date d = today.increment(new Period(6, TimeUnit.MONTHS)); d.lt(exerciseDate); d.increment(new Period(6, TimeUnit.MONTHS))) {
            dividends.add(new FixedDividend(1.0, d));
        }

        DayCounter dayCounter = Actual365Fixed.getDayCounter();
        /*@Time*/ double maturity = dayCounter.yearFraction(settlementDate,exerciseDate);
        
        System.out.println("option type = "+type);
        System.out.println("Time to maturity = "+maturity);
        System.out.println("Underlying price = "+underlying);
        System.out.println("Risk-free interest rate = "+riskFreeRate);
        System.out.println("Dividend yield = "+dividendYield);
        System.out.println("Volatility = "+volatility);
        System.out.println("");
        System.out.println("");

        // write column headings
        int widths[] = { 35, 14, 14 };
        int totalWidth = widths[0] + widths[1] + widths[2];
        StringBuilder ruleBuilder = new StringBuilder();
        StringBuilder dblruleBuilder = new StringBuilder();
        for(int i=0; i<totalWidth; i++){
        	ruleBuilder.append('-');
        	dblruleBuilder.append('=');
        }
        String rule = ruleBuilder.toString(), dblrule=dblruleBuilder.toString();

        System.out.println(dblrule);
        System.out.println("Tsiveriotis-Fernandes method");
        System.out.println(dblrule);
//        std::cout << std::setw(widths[0]) << std::left << "Tree type"
//                  << std::setw(widths[1]) << std::left << "European"
//                  << std::setw(widths[1]) << std::left << "American"
//                  << std::endl;
        System.out.println(rule);
        
        Exercise exercise = new EuropeanExercise(exerciseDate);
        Exercise amExercise = new AmericanExercise(settlementDate,exerciseDate);

        Handle<Quote> underlyingH = new Handle (new SimpleQuote(underlying));
        Handle<YieldTermStructure> flatTermStructure = new Handle( new FlatForward(settlementDate, riskFreeRate, dayCounter));
        Handle<YieldTermStructure> flatDividendTS = new Handle(new FlatForward(settlementDate, dividendYield, dayCounter));
        Handle<BlackVolTermStructure> flatVolTS = new Handle(new BlackConstantVol(settlementDate, volatility, dayCounter));

        StochasticProcess stochasticProcess = new BlackScholesMertonProcess(underlyingH,
                                              								flatDividendTS,
                                              								flatTermStructure,
                                              								flatVolTS);

        int timeSteps = 801;

        Handle<Quote> creditSpread = new Handle(new SimpleQuote(spreadRate));
        Quote rate = new SimpleQuote(riskFreeRate);

        Handle<YieldTermStructure> discountCurve = new Handle(new FlatForward(today, new Handle<Quote>(rate), dayCounter));

        PricingEngine engine = new BinomialConvertibleEngine<JarrowRudd>(timeSteps);

        ConvertibleFixedCouponBond europeanBond = new ConvertibleFixedCouponBond(
                                stochasticProcess, exercise, engine,
                                conversionRatio, dividends, callability,
                                creditSpread, issueDate, settlementDays,
                                coupons, bondDayCount, schedule, redemption);

        ConvertibleFixedCouponBond americanBond = new ConvertibleFixedCouponBond(
                                stochasticProcess, amExercise, engine,
                                conversionRatio, dividends, callability,
                                creditSpread, issueDate, settlementDays,
                                coupons, bondDayCount, schedule, redemption);
        String method = "Jarrow-Rudd";
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
	
	public static void main (String [] args){
		new ConvertibleBonds().run();
	}
	
}
