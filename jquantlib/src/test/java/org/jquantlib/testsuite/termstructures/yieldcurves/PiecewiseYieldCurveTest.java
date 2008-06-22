/*
 Copyright (C) 2007 Srinivas Hasti

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

package org.jquantlib.testsuite.termstructures.yieldcurves;

import org.jquantlib.time.Frequency;
import org.jquantlib.time.TimeUnit;
import org.junit.Test;

/**
 * @author Srinivas Hasti
 * 
 */

// FIXME: refactor package name to
// org.jquantlib.testsuite.termstructures.yieldcurves

public class PiecewiseYieldCurveTest {

	private class Datum {
		public int n;
		public TimeUnit units;
		public double rate;

		public Datum(int n, TimeUnit units, double rate) {
			super();
			this.n = n;
			this.units = units;
			this.rate = rate;
		}
	}

	private class BondDatum {
		public int n;
		public TimeUnit units;
		public int length;
		public Frequency frequency;
		public double coupon;
		public double price;

		public BondDatum(int n, TimeUnit units, int length,
				Frequency frequency, double coupon, double price) {
			super();
			this.n = n;
			this.units = units;
			this.length = length;
			this.frequency = frequency;
			this.coupon = coupon;
			this.price = price;
		}
	}

	private Datum[] depositData = { 
			new Datum(1, TimeUnit.WEEKS, 4.559),
			new Datum(1, TimeUnit.MONTHS, 4.581),
			new Datum(2, TimeUnit.MONTHS, 4.573),
			new Datum(3, TimeUnit.MONTHS, 4.557),
			new Datum(6, TimeUnit.MONTHS, 4.496),
			new Datum(9, TimeUnit.MONTHS, 4.490) };

	private Datum[] fraData = { 
			new Datum(1, TimeUnit.MONTHS, 4.581),
			new Datum(2, TimeUnit.MONTHS, 4.573),
			new Datum(3, TimeUnit.MONTHS, 4.557),
			new Datum(6, TimeUnit.MONTHS, 4.496),
			new Datum(9, TimeUnit.MONTHS, 4.490) };

	private Datum[] swapData = { 
			new Datum(1, TimeUnit.YEARS, 4.54),
			new Datum(2, TimeUnit.YEARS, 4.63),
			new Datum(3, TimeUnit.YEARS, 4.75),
			new Datum(4, TimeUnit.YEARS, 4.86),
			new Datum(5, TimeUnit.YEARS, 4.99),
			new Datum(6, TimeUnit.YEARS, 5.11),
			new Datum(7, TimeUnit.YEARS, 5.23),
			new Datum(8, TimeUnit.YEARS, 5.33),
			new Datum(9, TimeUnit.YEARS, 5.41),
			new Datum(10, TimeUnit.YEARS, 5.47),
			new Datum(12, TimeUnit.YEARS, 5.60),
			new Datum(15, TimeUnit.YEARS, 5.75),
			new Datum(20, TimeUnit.YEARS, 5.89),
			new Datum(25, TimeUnit.YEARS, 5.95),
			new Datum(30, TimeUnit.YEARS, 5.96) };

	private BondDatum[] bondData = {
			new BondDatum(6, TimeUnit.MONTHS, 5, Frequency.SEMI_ANNUAL, 4.75,
					101.320),
			new BondDatum(1, TimeUnit.YEARS, 3, Frequency.SEMI_ANNUAL, 2.75,
					100.590),
			new BondDatum(2, TimeUnit.YEARS, 5, Frequency.SEMI_ANNUAL, 5.00,
					105.650),
			new BondDatum(5, TimeUnit.YEARS, 11, Frequency.SEMI_ANNUAL, 5.50,
					113.610),
			new BondDatum(10, TimeUnit.YEARS, 11, Frequency.SEMI_ANNUAL, 3.75,
					104.070) };

	private Datum[] bmaData = { new Datum(1, TimeUnit.YEARS, 67.56),
			new Datum(2, TimeUnit.YEARS, 68.00),
			new Datum(3, TimeUnit.YEARS, 68.25),
			new Datum(4, TimeUnit.YEARS, 68.50),
			new Datum(5, TimeUnit.YEARS, 68.81),
			new Datum(7, TimeUnit.YEARS, 69.50),
			new Datum(10, TimeUnit.YEARS, 70.44),
			new Datum(15, TimeUnit.YEARS, 71.69),
			new Datum(20, TimeUnit.YEARS, 72.69),
			new Datum(30, TimeUnit.YEARS, 73.81) };
	

	public PiecewiseYieldCurveTest() {
		System.out.println("\n\n::::: "+this.getClass().getSimpleName()+" :::::");
	}
	
	@Test
	public void fakeTestCase() {
		// This is not a test case.
		// Obtain real test cases from QuantLib-0.8.1 sources
		System.out.println("***** TEST FAILED *****");
	}
	
	
// TODO: remove comments	
//	private class CommonVars {
//		// global variables
//		public Calendar calendar;
//		public int settlementDays;
//		public Date today;
//		public Date settlement;
//		public BusinessDayConvention fixedLegConvention;
//		public Frequency fixedLegFrequency;
//		public DayCounter fixedLegDayCounter;
//		public int bondSettlementDays;
//		public DayCounter bondDayCounter;
//		public BusinessDayConvention bondConvention;
//		public double bondRedemption;
//		public Frequency bmaFrequency;
//		public BusinessDayConvention bmaConvention;
//		public DayCounter bmaDayCounter;
//
//		public int deposits;
//		public int fras;
//		public int swaps;
//		public int bonds;
//		public int bmas;
//		public List<Quote> rates;
//		public List<Quote> fraRates;
//		public List<Quote> prices;
//		public List<Quote> fractions;
//		public List<RateHelper> instruments;
//		public List<RateHelper> fraHelpers;
//		public List<RateHelper> bondHelpers;
//		public List<RateHelper> bmaHelpers;
//		public List<Schedule> schedules;
//		public YieldTermStructure termStructure;
//       
//		
//		//public SavedSettings backup;
//		//public IndexHistoryCleaner cleaner;
//
//		// setup
//		public CommonVars()
//		{
//			// data
//			calendar = Target.getCalendar();
//			settlementDays = 2;
//			today = calendar.advance(DateFactory.getFactory().getTodaysDate());
//			Configuration.getSystemConfiguration(null).getGlobalSettings().setEvaluationDate(today);
//			settlement = calendar.advance(today,settlementDays,TimeUnit.DAYS);
//			fixedLegConvention = BusinessDayConvention.UNADJUSTED;
//			fixedLegFrequency = Frequency.ANNUAL;
//			fixedLegDayCounter = new org.jquantlib.daycounters.Thirty360();
//			bondSettlementDays = 3;
//			bondDayCounter = ActualActual.getActualActual(Convention.BOND);
//			bondConvention = BusinessDayConvention.FOLLOWING;
//			bondRedemption = 100.0;
//			bmaFrequency = Frequency.QUARTERLY;
//			bmaConvention = BusinessDayConvention.FOLLOWING;
//			bmaDayCounter = ActualActual.getActualActual(Convention.BOND);
//
//			deposits = depositData.length;
//            fras = fraData.length;
//            swaps = swapData.length;
//            bonds = bondData.length;
//            bmas = bmaData.length;
//
//		   // market elements
//            rates =
//                new ArrayList<Quote>();
//            fraRates = new ArrayList<Quote>();
//            prices = new ArrayList<Quote>();
//            fractions = new ArrayList<Quote>();
//            for (int i=0; i<deposits; i++) {
//                rates.add(new SimpleQuote(depositData[i].rate/100));
//            }
//            for (int i=0; i<swaps; i++) {
//               rates.add(
//                                       new SimpleQuote(swapData[i].rate/100));
//            }
//            for (int i=0; i<fras; i++) {
//                fraRates.add(
//                                        new SimpleQuote(fraData[i].rate/100));
//            }
//            for (int i=0; i<bonds; i++) {
//                prices.add(
//                                          new SimpleQuote(bondData[i].price));
//            }
//            for (int i=0; i<bmas; i++) {
//                fractions.add(
//                                        new SimpleQuote(bmaData[i].rate/100));
//            }
//
//            // rate helpers
//            instruments =
//                new ArrayList<RateHelper> ();
//            fraHelpers = new ArrayList<RateHelper> ();
//            bondHelpers = new ArrayList<RateHelper>();
//            schedules = new ArrayList<Schedule>();
//            bmaHelpers = new ArrayList<RateHelper>();
//            
////  Srinivas: please review usage of Handle
////            
////            IborIndex euribor6m = new Euribor(new Period(6, TimeUnit.MONTHS), new Handle<YieldTermStructure>());            
////            for (int i=0; i<deposits; i++) {
////                Handle<Quote> r = new Handle(rates.get(i));
////                instruments.add(i,new
////                    DepositRateHelper(r, new Period(depositData[i].n,depositData[i].units),
////                                      euribor6m.getFixingDays(), calendar,
////                                      euribor6m.getConvention(),
////                                      euribor6m.isEndOfMonth(),
////                                      euribor6m.getDayCounter()));
////            }
//            
//            /*for (int i=0; i<swaps; i++) {
//                Handle<Quote> r = new Handle(rates.get(i+deposits));
//                instruments.add((i+deposits), new
//                    SwapRateHelper(r, new Period(swapData[i].n,swapData[i].units),
//                                   calendar,
//                                   fixedLegFrequency, fixedLegConvention,
//                                   fixedLegDayCounter, euribor6m));
//            }*/
//            
////  Srinivas: please review usage of Handle
////          
////            Euribor euribor3m = new Euribor(new Period(3, TimeUnit.MONTHS), new Handle<YieldTermStructure>());            
////            for (int i=0; i<fras; i++) {
////                Handle<Quote> r = new Handle(fraRates.get(i));
////                fraHelpers.add(i, new
////                    FraRateHelper(r, fraData[i].n, fraData[i].n + 3,
////                                  euribor3m.getFixingDays(),
////                                  euribor3m.getFixingCalendar(),
////                                  euribor3m.getConvention(),
////                                  euribor3m.isEndOfMonth(),
////                                  euribor3m.getDayCounter()));
////            }
//            
//            for (int i=0; i<bonds; i++) {
//                Handle<Quote> p = new Handle(prices.get(i));
//                Date maturity =
//                    calendar.advance(today, bondData[i].n, bondData[i].units);
//                Date issue =
//                    calendar.advance(maturity, -bondData[i].length, TimeUnit.YEARS);
//                /*std::vector<Rate> coupons(1, bondData[i].coupon/100.0);
//                schedules.add(i, new Schedule(issue, maturity,
//                                        new Period(bondData[i].frequency),
//                                        calendar,
//                                        bondConvention, bondConvention,
//                                        DateGenerationRule.BACKWARD, false, Date.NULL_DATE, Date.NULL_DATE));
//                bondHelpers[i] = boost::shared_ptr<RateHelper>(new
//                    FixedRateBondHelper(p,
//                                        bondSettlementDays,
//                                        bondRedemption, schedules[i],
//                                        coupons, bondDayCounter,
//                                        bondConvention,
//                                        bondRedemption, issue)); */
//            }
//            
//        }
//	}

}
