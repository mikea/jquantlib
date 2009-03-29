/*
 Copyright (C) 2009 Ueli Hofstetter

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


/*!
 Copyright (C) 2006 Allen Kuo

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

/*  This example shows how to set up a term structure and price a simple
    forward-rate agreement.
*/


package org.jquantlib.examples;


import org.jquantlib.Configuration;
import org.jquantlib.Settings;
import org.jquantlib.daycounters.ActualActual;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.daycounters.ActualActual.Convention;
import org.jquantlib.indexes.Euribor365_3M;
import org.jquantlib.indexes.IborIndex;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.RelinkableHandle;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.jquantlib.util.Month;

public class FRA {

    /**
     * @param args
     */
    public static void main(String[] args) {
        
        System.out.println("\n\n::::: "+FRA.class.getSimpleName()+" :::::");

        /*********************
         ***  MARKET DATA  ***
         *********************/
        
        //FIXME: What kind of yieldtermstructure, how to initialize?
        RelinkableHandle<YieldTermStructure> euriborTermStructure = null;
        Handle<IborIndex> euribor3m = new Handle<IborIndex>(new Euribor365_3M(euriborTermStructure));
        
        Date todaysDate = DateFactory.getFactory().getDate(23, Month.MAY, 2006);
        Settings settings = Configuration.newConfiguration(null).newSettings();
        settings.setEvaluationDate(todaysDate);
        
        Calendar calendar = euribor3m.getLink().getFixingCalendar();
        int fixingDays = euribor3m.getLink().getFixingDays();
        Date settlementDate = calendar.advance(todaysDate, fixingDays, TimeUnit.MONTHS );
        
        System.out.println("Today: "+ todaysDate.getWeekday() + "," + todaysDate);
        
        System.out.println("Settlement date: " + settlementDate.getWeekday() + "," + settlementDate);
        
        // 3 month term FRA quotes (index refers to monthsToStart)
        double threeMonthFraQuote [] = new double[10];
        threeMonthFraQuote[1]=0.030;
        threeMonthFraQuote[2]=0.031;
        threeMonthFraQuote[3]=0.032;
        threeMonthFraQuote[6]=0.033;
        threeMonthFraQuote[9]=0.034;
        
        /********************
         ***    QUOTES    ***
         ********************/

        // SimpleQuote stores a value which can be manually changed;
        // other Quote subclasses could read the value from a database
        // or some kind of data feed.
        
        Handle<SimpleQuote> fra1x4Rate = new Handle<SimpleQuote>(new SimpleQuote(threeMonthFraQuote[1]));
        Handle<SimpleQuote> fra2x5Rate = new Handle<SimpleQuote>(new SimpleQuote(threeMonthFraQuote[2]));
        Handle<SimpleQuote> fra3x6Rate = new Handle<SimpleQuote>(new SimpleQuote(threeMonthFraQuote[3]));
        Handle<SimpleQuote> fra6x9Rate = new Handle<SimpleQuote>(new SimpleQuote(threeMonthFraQuote[6]));
        Handle<SimpleQuote> fra9x12Rate = new Handle<SimpleQuote>(new SimpleQuote(threeMonthFraQuote[9]));
        
        RelinkableHandle<Quote> h1x4 = null ;       h1x4.setLink(fra1x4Rate.getLink());
        RelinkableHandle<Quote> h2x5 = null;        h2x5.setLink(fra2x5Rate.getLink());
        RelinkableHandle<Quote> h3x6 = null;        h3x6.setLink(fra3x6Rate.getLink());
        RelinkableHandle<Quote> h6x9 = null;        h6x9.setLink(fra6x9Rate.getLink());
        RelinkableHandle<Quote> h9x12 = null;       h9x12.setLink(fra9x12Rate.getLink());

        /*********************
         ***  RATE HELPERS ***
         *********************/

        // RateHelpers are built from the above quotes together with
        // other instrument dependant infos.  Quotes are passed in
        // relinkable handles which could be relinked to some other
        // data source later.
        
        DayCounter fraDayCounter = euribor3m.getLink().getDayCounter();
        BusinessDayConvention convention = euribor3m.getLink().getConvention();
        boolean endOfMonth = euribor3m.getLink().isEndOfMonth();
        
        /*RateHelper fra1x4 = new FraRateHelper(h1x4, 1, 4, fixingDays, calendar, convention,endOfMonth,);/*(h1x4, 1, 4,
                                  fixingDays, calendar, convention,
                                  endOfMonth, fixingDays,
                                  fraDayCounter);*/
        //FIXME....
       /*
        boost::shared_ptr<RateHelper> fra1x4(
                new FraRateHelper(h1x4, 1, 4,
                                  fixingDays, calendar, convention,
                                  endOfMonth, fixingDays,
                                  fraDayCounter));

        boost::shared_ptr<RateHelper> fra2x5(
                new FraRateHelper(h2x5, 2, 5,
                                  fixingDays, calendar, convention,
                                  endOfMonth, fixingDays,
                                  fraDayCounter));

        boost::shared_ptr<RateHelper> fra3x6(
                new FraRateHelper(h3x6, 3, 6,
                                  fixingDays, calendar, convention,
                                  endOfMonth, fixingDays,
                                  fraDayCounter));

        boost::shared_ptr<RateHelper> fra6x9(
                new FraRateHelper(h6x9, 6, 9,
                                  fixingDays, calendar, convention,
                                  endOfMonth, fixingDays,
                                  fraDayCounter));

        boost::shared_ptr<RateHelper> fra9x12(
                new FraRateHelper(h9x12, 9, 12,
                                  fixingDays, calendar, convention,
                                  endOfMonth, fixingDays,
                                  fraDayCounter));
        */
        
        /*********************
         **  CURVE BUILDING **
         *********************/

        // Any DayCounter would be fine.
        // ActualActual::ISDA ensures that 30 years is 30.0
        DayCounter termStructureDayCounter = ActualActual.getDayCounter(Convention.ISDA);
        
        double tolerance = 1.0e-15;
        
        //A FRA curve
        

    }

}
