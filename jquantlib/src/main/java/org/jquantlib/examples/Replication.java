/*!
 Copyright (C) 2006 StatPro Italia srl

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

/*  This example showcases the CompositeInstrument class. Such class
    is used to build a static replication of a down-and-out barrier
    option, as outlined in Section 10.2 of Mark Joshi's "The Concepts
    and Practice of Mathematical Finance" to which we refer the
    reader.
*/

package org.jquantlib.examples;

import org.jquantlib.Configuration;
import org.jquantlib.Settings;
import org.jquantlib.daycounters.Actual365Fixed;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.exercise.EuropeanExercise;
import org.jquantlib.exercise.Exercise;
import org.jquantlib.instruments.BarrierType;
import org.jquantlib.instruments.Option;
import org.jquantlib.instruments.Payoff;
import org.jquantlib.instruments.PlainVanillaPayoff;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;
import org.jquantlib.processes.StochasticProcess;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.termstructures.volatilities.BlackConstantVol;
import org.jquantlib.termstructures.yieldcurves.FlatForward;
import org.jquantlib.time.Period;
import org.jquantlib.time.calendars.NullCalendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.jquantlib.util.Month;
import org.jquantlib.util.StopClock;

public class Replication {
    
    public static String fmt = "%-45s %-15s %-15s\n";
    
    public static void main(String[] args) {
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
        System.out.println("\n\n::::: "+Replication.class.getSimpleName()+" :::::");
    
        try{
            StopClock clock = new StopClock();
            clock.startClock();
            
            Date today = DateFactory.getFactory().getDate(29, Month.MAY, 2006);
            
            //FIXME: throws nullpointer
            Settings settings = Configuration.newConfiguration(null).newSettings();
            settings.setEvaluationDate(today);
            
            //the option to replicate
            BarrierType barrierType = BarrierType.DownOut;
            double barrier = 70.0;
            double rebate = 0.0;
            Option.Type type = Option.Type.PUT;
            double underlyingValue = 100;
            Handle<SimpleQuote> underlying = new Handle<SimpleQuote>(new SimpleQuote(underlyingValue));
            
            double strike = 100.0;
            Handle<SimpleQuote> riskFreeRate = new Handle<SimpleQuote>(new SimpleQuote(0.04));
            Handle<SimpleQuote> volatility = new Handle<SimpleQuote>(new SimpleQuote(0.20));
            Date maturity = today.getDateAfter(Period.ONE_YEAR_FORWARD);
            
            System.out.println("\n");
            
            //write column headings - we don't need these values ...
            int widths[] = {45, 15, 15};
            int totalWidth = widths[0] + widths[1]+ widths[2];
            
            System.out.println("Initial Market conditions");
            System.out.printf(fmt, "Option", "NPV", "Error");
            /*
            Size widths[] = { 45, 15, 15 };
            Size totalWidth = widths[0]+widths[1]+widths[2];
            std::string rule(totalWidth, '-'), dblrule(totalWidth, '=');

            std::cout << dblrule << std::endl;
            std::cout << "Initial market conditions" << std::endl;
            std::cout << dblrule << std::endl;
            std::cout << std::setw(widths[0]) << std::left << "Option"
                  << std::setw(widths[1]) << std::left << "NPV"
                  << std::setw(widths[2]) << std::left << "Error"
                  << std::endl;
            std::cout << rule << std::endl;
             */
            
            //bootstrap the yield/vol curves
            DayCounter dayCounter = Actual365Fixed.getDayCounter();
            Handle<Quote> h1 = new Handle<Quote>(riskFreeRate.getLink());
            Handle<Quote> h2 = new Handle<Quote>(volatility.getLink());
            Handle<YieldTermStructure> flatRate = new Handle<YieldTermStructure>(new FlatForward(0, new NullCalendar(), h1, dayCounter));
            Handle<BlackConstantVol> flatVol = new Handle<BlackConstantVol>(new BlackConstantVol(0, new NullCalendar(), h2, dayCounter));
            
            //instantiate the option
            Exercise exercise = new EuropeanExercise(maturity);
            Payoff payoff = new PlainVanillaPayoff(type, strike);
            
            //FIXME:Review BlackScholes, GeneralizedBlackScholesStochasticProcess
            //Handle<StochasticProcess> stochasticProcess = new Handle<StochasticProcess>(new GeneralizedBlackScholesProcess(underlying, flatRate, flatVol));
            
            
        }
        
        catch(Exception ex){
            ex.printStackTrace();
        }
        finally{
            
        }

}
}
