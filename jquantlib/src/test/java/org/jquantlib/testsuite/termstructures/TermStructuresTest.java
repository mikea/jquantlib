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

/*
Copyright (C) 2003 RiskMap srl

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

package org.jquantlib.testsuite.termstructures;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.TimeUnit;
import org.junit.Test;


public class TermStructuresTest {

    private final static Logger logger = LoggerFactory.getLogger(TermStructuresTest.class);

	private Calendar calendar;
	private int settlementDays;
	private YieldTermStructure termStructure;
	private YieldTermStructure dummyTermStructure;


	private static class Datum {
	    public int n;
	    public TimeUnit units;
	    public double rate;
	    
		public Datum(int n, TimeUnit units, double rate) {
			this.n = n;
			this.units = units;
			this.rate = rate;
		}
	}
	
	
	public TermStructuresTest() {

		logger.info("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
		
//TODO: remove comments		
//		calendar = org.jquantlib.time.calendars.Target.getCalendar();
//        settlementDays = 2;
//        org.jquantlib.util.Date today = calendar.advance(org.jquantlib.util.DateFactory.getFactory().getTodaysDate());
//        org.jquantlib.Configuration.getSystemConfiguration(null).getGlobalSettings().setEvaluationDate(today);
//        org.jquantlib.util.Date settlement = calendar.advance(today,settlementDays,TimeUnit.DAYS);
//        
//        Datum depositData[] = new Datum[] {
//        		new Datum( 1, TimeUnit.MONTHS, 4.581 ),
//        		new Datum( 2, TimeUnit.MONTHS, 4.573 ),
//        		new Datum( 3, TimeUnit.MONTHS, 4.557 ),
//        		new Datum( 6, TimeUnit.MONTHS, 4.496 ),
//        		new Datum( 9, TimeUnit.MONTHS, 4.490 )
//        };
//        
//        Datum swapData[] =  new Datum[] {
////TODO: remove comments when we have implemented Swaps and related classes, etc        		
////        		  new Datum(  1, TimeUnit.YEARS, 4.54 ),
////                new Datum(  5, TimeUnit.YEARS, 4.99 ),
////                new Datum( 10, TimeUnit.YEARS, 5.47 ),
////                new Datum( 20, TimeUnit.YEARS, 5.89 ),
////                new Datum( 30, TimeUnit.YEARS, 5.96 )
//        };
//        
//        int deposits = depositData.length;
//        int swaps = swapData.length;
//            
//        RateHelper<YieldTermStructure> instruments[] = new RateHelper[deposits+swaps];
//        
//
//        for (int i=0; i<deposits; i++) {
//            instruments[i] = new DepositRateHelper<YieldTermStructure>(
//            						depositData[i].rate/100,
//            						new Period(depositData[i].n, depositData[i].units),
//                                    settlementDays, calendar,
//                                    BusinessDayConvention.MODIFIED_FOLLOWING, true,
//                                    Actual360.getDayCounter());
//        }
//       
//       
//       IborIndex index = new IborIndex(
//       							"dummy", 
//       							new Period(6,TimeUnit.MONTHS),
//       							settlementDays,
//       							calendar,
//								null,								
//								BusinessDayConvention.MODIFIED_FOLLOWING, 
//								false,
//								Actual360.getDayCounter());
       
//TODO: remove comments when we have implemented Swaps and related classes, etc        		
//        for (int i=0; i<swaps; ++i) {
//            instruments[i+deposits] = new SwapRateHelper(
//            						swapData[i].rate/100,
//            						new Period(swapData[i].n, swapData[i].units),
//                                    calendar, Frequency.ANNUAL,
//                                    BusinessDayConvention.UNADJUSTED, Thirty360.getDayCounter(),
//                                    index);
//        }

       
//TODO: remove comments       
//        termStructure = new PiecewiseYieldDiscountCurve<LogLinear>(settlement, instruments, Actual360.getDayCounter());
//                                                    
//        dummyTermStructure = new PiecewiseYieldDiscountCurve<LogLinear>(settlement, instruments, Actual360.getDayCounter());
    }

	
	@Test
	public void testReferenceChange() {
	
	    logger.info("Testing term structure against evaluation date change...");
	    logger.error("***** TEST FAILED *****");
	
//TODO: remove comments	
//	    YieldTermStructure localTermStructure = new FlatForward(settlementDays, new NullCalendar(), 0.03, Actual360.getDayCounter());
//	
//	    Date today = Configuration.getSystemConfiguration(null).getGlobalSettings().getEvaluationDate();
//	    int days[] = { 10, 30, 60, 120, 360, 720 };
//	    /*@DiscountFactor*/ double[] expected = new /*@DiscountFactor*/ double[days.length];
//	    
//	    for (int i=0; i<days.length; i++)
//	        expected[i] = localTermStructure.getDiscount(today.increment(days[i]));
//	
//	    Configuration.getSystemConfiguration(null).getGlobalSettings().setEvaluationDate(today.increment(30));
//	
//	    /*@DiscountFactor*/ double[] calculated = new /*@DiscountFactor*/ double[days.length];
//	
//	    for (int i=0; i<days.length; i++)
//	        calculated[i] = localTermStructure.getDiscount(today.increment(30).increment(days[i]));
//	
//	    for (int i=0; i<days.length; i++) {
//	    	if (!Closeness.isClose(expected[i],calculated[i]))
//	            fail("\n  Discount at " + days[i] + " days:\n"
//	                        + "    before date change: " + expected[i] + "\n"
//	                        + "    after date change:  " + calculated[i]);
//	    }
	}

	@Test
	public void testImplied() {
	
	    logger.info("Testing consistency of implied term structure...");
	    logger.error("***** TEST FAILED *****");
	    
//TODO: remove comments	
//	    double tolerance = 1.0e-10;
//	    Date today = Configuration.getSystemConfiguration(null).getGlobalSettings().getEvaluationDate();
//	    Date newToday = today.increment(3 * Period.ONE_YEAR_FORWARD.getLength());
//	    Date newSettlement = Target.getCalendar().advance(newToday, settlementDays, TimeUnit.DAYS);
//	    Date testDate = newSettlement.increment(5 * Period.ONE_YEAR_FORWARD.getLength());
//	    
//	    YieldTermStructure implied = new ImpliedTermStructure<YieldTermStructure>(
//	    		new Handle<YieldTermStructure>(termStructure), newSettlement);
//	    
//	    /*@DiscountFactor*/ double baseDiscount = termStructure.getDiscount(newSettlement);
//	    /*@DiscountFactor*/ double discount = termStructure.getDiscount(testDate);
//	    /*@DiscountFactor*/ double impliedDiscount = implied.getDiscount(testDate);
//	    	
//        if (Math.abs(discount - baseDiscount*impliedDiscount) > tolerance)
//        	fail("unable to reproduce discount from implied curve\n"
//	            + "    calculated: " + baseDiscount*impliedDiscount + "\n"
//	            + "    expected:   " + discount);
	}
	
//	@Test
//	public void testImpliedObs() {
//	
//	    logger.info("Testing observability of implied term structure...");
//	
//	    Date today = Configuration.getSystemConfiguration(null).getGlobalSettings().getEvaluationDate();
//	    Date newToday = today.increment(3 * Period.ONE_YEAR_FORWARD.getLength());
//	    Date newSettlement = Target.getCalendar().advance(newToday, settlementDays, TimeUnit.DAYS);
//	    
//	    RelinkableHandle<YieldTermStructure> h = new RelinkableHandle<YieldTermStructure>(); 
//	    YieldTermStructure implied = new ImpliedTermStructure<YieldTermStructure>(h, newSettlement);
//	    
//	    Flag flag = new Flag();
//	    implied.addObserver(flag);
//	    h.setLink(termStructure);
//	    if (!flag.isUp())
//	    	fail("Observer was not notified of term structure change");
//	}
	
	
//	@Test
//	public void testFSpreaded() {
//	
//	    logger.info("Testing consistency of forward-spreaded term structure...");
//	
//	    double tolerance = 1.0e-10;
//	    Quote me = new SimpleQuote(0.01);
//	    Handle<Quote> mh = new Handle(me);
//	    
//	    YieldTermStructure spreaded = new ForwardSpreadedTermStructure( new Handle<YieldTermStructure>(termStructure), mh);
//	    Date testDate = termStructure.getReferenceDate().increment(5 * Period.ONE_YEAR_FORWARD.getLength());
//	    DayCounter tsdc  = termStructure.getDayCounter();
//	    DayCounter sprdc = spreaded.getDayCounter();
//	
//	    // FIXME :: code review:: could be:: /*@Rate*/ double forward = ... ?????
//	    InterestRate forward = termStructure.getForwardRate(testDate, testDate, tsdc, Compounding.CONTINUOUS, Frequency.NO_FREQUENCY);
//	
//	    // FIXME :: code review:: could be:: /*@Rate*/ double spreadedForward = ... ?????
//	    InterestRate spreadedForward = spreaded.getForwardRate(testDate, testDate, sprdc, Compounding.CONTINUOUS, Frequency.NO_FREQUENCY);
//	    
//        fail(
//            "unable to reproduce forward from spreaded curve\n"
//            + "    calculated: "
//            + (spreadedForward.doubleValue() - me.doubleValue()) + "\n"
//            + "    expected:   " + forward.doubleValue(),
//            Math.abs(forward.doubleValue() - (spreadedForward.doubleValue() - me.doubleValue())) > tolerance
//        );
//	}
//	
//	
//	@Test
//	public void testFSpreadedObs() {
//	
//	    logger.info("Testing observability of forward-spreaded term structure...");
//	
//	    SimpleQuote me = new SimpleQuote(0.01);
//	    Handle<Quote> mh = new Handle<Quote>(me);
//	    RelinkableHandle<YieldTermStructure> h = new RelinkableHandle<YieldTermStructure>(); //(dummyTermStructure_);
//	    YieldTermStructure spreaded = new ForwardSpreadedTermStructure(h, mh);
//	    
//	    Flag flag = new Flag();
//	    spreaded.addObserver(flag);
//	    h.setLink(termStructure);
//	    fail("Observer was not notified of term structure change", !flag.isUp());
//	    
//	    flag.lower();
//	    me.setValue(0.005);
//	    fail("Observer was not notified of spread change", !flag.isUp());
//	}
//	
//	
//	@Test
//	public void testZSpreaded() {
//	
//	    logger.info("Testing consistency of zero-spreaded term structure...");
//	
//	    double tolerance = 1.0e-10;
//	    Quote me = new SimpleQuote(0.01);
//	    Handle<Quote> mh = new Handle(me);
//	    YieldTermStructure spreaded = new ZeroSpreadedTermStructure(new Handle<YieldTermStructure>(termStructure), mh);
//	    Date testDate = termStructure.getReferenceDate().increment(5 * Period.ONE_YEAR_FORWARD.getLength());
//	    DayCounter rfdc  = termStructure.getDayCounter();
//	    
//	    // FIXME :: code review:: could be:: /*@Rate*/ double zero = ... ?????
//	    InterestRate zero = termStructure.getZeroRate(testDate, rfdc, Compounding.CONTINUOUS, Frequency.NO_FREQUENCY);
//	    
//	    // FIXME :: code review:: could be:: /*@Rate*/ double spreadedZero = ... ?????
//	    InterestRate spreadedZero = spreaded.getZeroRate(testDate, rfdc, Compounding.CONTINUOUS, Frequency.NO_FREQUENCY);
//	    
//	    fail(
//	        "unable to reproduce zero yield from spreaded curve\n"
//	        + "    calculated: " + (spreadedZero.doubleValue() - me.doubleValue()) + "\n"
//	        + "    expected:   " + zero.doubleValue(),
//	        Math.abs(zero.doubleValue() - (spreadedZero.doubleValue() - me.doubleValue())) > tolerance);
//	}
//	
//	
//	@Test
//	public void testZSpreadedObs() {
//	
//	    logger.info("Testing observability of zero-spreaded term structure...");
//	
//	    SimpleQuote me = new SimpleQuote(0.01);
//	    Handle<Quote> mh = new Handle<Quote>(me);
//	    
//	    RelinkableHandle<YieldTermStructure> h = new RelinkableHandle<YieldTermStructure>(dummyTermStructure);
//	    YieldTermStructure spreaded = new ZeroSpreadedTermStructure(h, mh);
//	
//	    Flag flag = new Flag();
//	    spreaded.addObserver(flag);
//	    h.setLink(termStructure);
//	    
//	    fail("Observer was not notified of term structure change", !flag.isUp());
//	    
//	    flag.lower();
//	    me.setValue(0.005);
//	    fail("Observer was not notified of spread change", !flag.isUp());
//	}


}
