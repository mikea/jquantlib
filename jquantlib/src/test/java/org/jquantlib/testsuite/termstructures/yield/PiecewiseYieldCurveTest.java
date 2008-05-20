/*
 Copyright (C) 2007 Srinivas Hasti

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquantlib-dev@lists.sf.net>. The license is also available online at
 <http://jquantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the originating copyright notice follows below.
 */
package org.jquantlib.testsuite.termstructures.yield;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.Configuration;
import org.jquantlib.daycounters.ActualActual;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.daycounters.ActualActual.Convention;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.termstructures.RateHelper;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.termstructures.yield.DepositRateHelper;
import org.jquantlib.termstructures.yield.SwapRateHelper;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Frequency;
import org.jquantlib.time.Schedule;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.time.calendars.Target;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;

/**
 * @author Srinivas Hasti
 * 
 */

// FIXME: refactor package name to
// org.jquantlib.testsuite.termstructures.yieldcurves

public class PiecewiseYieldCurveTest {

	public class Datum {
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

	public class BondDatum {
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

	Datum[] depositData = { new Datum(1, TimeUnit.WEEKS, 4.559),
			new Datum(1, TimeUnit.MONTHS, 4.581),
			new Datum(2, TimeUnit.MONTHS, 4.573),
			new Datum(3, TimeUnit.MONTHS, 4.557),
			new Datum(6, TimeUnit.MONTHS, 4.496),
			new Datum(9, TimeUnit.MONTHS, 4.490) };

	Datum[] fraData = { new Datum(1, TimeUnit.MONTHS, 4.581),
			new Datum(2, TimeUnit.MONTHS, 4.573),
			new Datum(3, TimeUnit.MONTHS, 4.557),
			new Datum(6, TimeUnit.MONTHS, 4.496),
			new Datum(9, TimeUnit.MONTHS, 4.490) };

	Datum[] swapData = { new Datum(1, TimeUnit.YEARS, 4.54),
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

	BondDatum[] bondData = {
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

	Datum[] bmaData = { new Datum(1, TimeUnit.YEARS, 67.56),
			new Datum(2, TimeUnit.YEARS, 68.00),
			new Datum(3, TimeUnit.YEARS, 68.25),
			new Datum(4, TimeUnit.YEARS, 68.50),
			new Datum(5, TimeUnit.YEARS, 68.81),
			new Datum(7, TimeUnit.YEARS, 69.50),
			new Datum(10, TimeUnit.YEARS, 70.44),
			new Datum(15, TimeUnit.YEARS, 71.69),
			new Datum(20, TimeUnit.YEARS, 72.69),
			new Datum(30, TimeUnit.YEARS, 73.81) };
	
	
	public class CommonVars
	{
		// global variables
		public Calendar calendar;
		public int settlementDays;
		public Date today;
		public Date settlement;
		public BusinessDayConvention fixedLegConvention;
		public Frequency fixedLegFrequency;
		public DayCounter fixedLegDayCounter;
		public int bondSettlementDays;
		public DayCounter bondDayCounter;
		public BusinessDayConvention bondConvention;
		public double bondRedemption;
		public Frequency bmaFrequency;
		public BusinessDayConvention bmaConvention;
		public DayCounter bmaDayCounter;

		public int deposits;
		public int fras;
		public int swaps;
		public int bonds;
		public int bmas;
		public List<SimpleQuote> rates;
		public List<SimpleQuote> fraRates;
		public List<SimpleQuote> prices;
		public List<SimpleQuote> fractions;
		public List<RateHelper> instruments;
		public List<RateHelper> fraHelpers;
		public List<RateHelper> bondHelpers;
		public List<RateHelper> bmaHelpers;
		public List<Schedule> schedules;
		public YieldTermStructure termStructure;
       
		
		//public SavedSettings backup;
		//public IndexHistoryCleaner cleaner;

		// setup
		public CommonVars()
		{
			// data
			calendar = Target.getCalendar();
			settlementDays = 2;
			today = calendar.advance(DateFactory.getFactory().getTodaysDate());
			Configuration.getSystemConfiguration(null).getGlobalSettings().setEvaluationDate(today);
			settlement = calendar.advance(today,settlementDays,TimeUnit.DAYS);
			fixedLegConvention = BusinessDayConvention.UNADJUSTED;
			fixedLegFrequency = Frequency.ANNUAL;
			fixedLegDayCounter = new org.jquantlib.daycounters.Thirty360();
			bondSettlementDays = 3;
			bondDayCounter = ActualActual.getActualActual(Convention.BOND);
			bondConvention = BusinessDayConvention.FOLLOWING;
			bondRedemption = 100.0;
			bmaFrequency = Frequency.QUARTERLY;
			bmaConvention = BusinessDayConvention.FOLLOWING;
			bmaDayCounter = ActualActual.getActualActual(Convention.BOND);

			deposits = depositData.length;
            fras = fraData.length;
            swaps = swapData.length;
            bonds = bondData.length;
            bmas = bmaData.length;

		   // market elements
            rates =
                new ArrayList<SimpleQuote>();
            fraRates = new ArrayList<SimpleQuote>();
            prices = new ArrayList<SimpleQuote>();
            fractions = new ArrayList<SimpleQuote>();
            for (int i=0; i<deposits; i++) {
                rates.add(new SimpleQuote(depositData[i].rate/100));
            }
            for (int i=0; i<swaps; i++) {
               rates.add(
                                       new SimpleQuote(swapData[i].rate/100));
            }
            for (int i=0; i<fras; i++) {
                fraRates.add(
                                        new SimpleQuote(fraData[i].rate/100));
            }
            for (int i=0; i<bonds; i++) {
                prices.add(
                                          new SimpleQuote(bondData[i].price));
            }
            for (int i=0; i<bmas; i++) {
                fractions.add(
                                        new SimpleQuote(bmaData[i].rate/100));
            }

            // rate helpers
            instruments =
                new ArrayList<RateHelper> ();
            fraHelpers = new ArrayList<RateHelper> ();
            bondHelpers = new ArrayList<RateHelper>();
            schedules = new ArrayList<Schedule>();
            bmaHelpers = new ArrayList<RateHelper>();
          /*
            boost::shared_ptr<IborIndex> euribor6m(new Euribor6M);
            for (Size i=0; i<deposits; i++) {
                Handle<Quote> r(rates[i]);
                instruments[i] = boost::shared_ptr<RateHelper>(new
                    DepositRateHelper(r, depositData[i].n*depositData[i].units,
                                      euribor6m->fixingDays(), calendar,
                                      euribor6m->businessDayConvention(),
                                      euribor6m->endOfMonth(),
                                      euribor6m->dayCounter()));
            }
            for (Size i=0; i<swaps; i++) {
                Handle<Quote> r(rates[i+deposits]);
                instruments[i+deposits] = boost::shared_ptr<RateHelper>(new
                    SwapRateHelper(r, swapData[i].n*swapData[i].units,
                                   calendar,
                                   fixedLegFrequency, fixedLegConvention,
                                   fixedLegDayCounter, euribor6m));
            }

            Euribor3M euribor3m;
            for (Size i=0; i<fras; i++) {
                Handle<Quote> r(fraRates[i]);
                fraHelpers[i] = boost::shared_ptr<RateHelper>(new
                    FraRateHelper(r, fraData[i].n, fraData[i].n + 3,
                                  euribor3m.fixingDays(),
                                  euribor3m.fixingCalendar(),
                                  euribor3m.businessDayConvention(),
                                  euribor3m.endOfMonth(),
                                  euribor3m.dayCounter()));
            }

            for (Size i=0; i<bonds; i++) {
                Handle<Quote> p(prices[i]);
                Date maturity =
                    calendar.advance(today, bondData[i].n, bondData[i].units);
                Date issue =
                    calendar.advance(maturity, -bondData[i].length, Years);
                std::vector<Rate> coupons(1, bondData[i].coupon/100.0);
                schedules[i] = Schedule(issue, maturity,
                                        Period(bondData[i].frequency),
                                        calendar,
                                        bondConvention, bondConvention,
                                        DateGeneration::Backward, false);
                bondHelpers[i] = boost::shared_ptr<RateHelper>(new
                    FixedRateBondHelper(p,
                                        bondSettlementDays,
                                        bondRedemption, schedules[i],
                                        coupons, bondDayCounter,
                                        bondConvention,
                                        bondRedemption, issue));
            } */
        }
	}

}
