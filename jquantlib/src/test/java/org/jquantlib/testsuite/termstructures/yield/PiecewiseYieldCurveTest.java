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

import java.util.List;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.termstructures.RateHelper;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Frequency;
import org.jquantlib.time.Schedule;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.util.Date;

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
		public List<RateHelper<YieldTermStructure>> instruments;
		public List<RateHelper<YieldTermStructure>> fraHelpers;
		public List<RateHelper<YieldTermStructure>> bondHelpers;
		public List<RateHelper<YieldTermStructure>> bmaHelpers;
		public List<Schedule> schedules;
		public YieldTermStructure termStructure;
       
		/*
		// cleanup
		public SavedSettings backup;
		public IndexHistoryCleaner cleaner;

		// setup
		public CommonVars()
		{
			// data
			calendar = TARGET();
			settlementDays = 2;
			today = calendar.adjust(Date.todaysDate());
			Settings.instance().evaluationDate() = today;
			settlement = calendar.advance(today,settlementDays,TimeUnit.Days);
			fixedLegConvention = BusinessDayConvention.Unadjusted;
			fixedLegFrequency = Frequency.Annual;
			fixedLegDayCounter = Thirty360();
			bondSettlementDays = 3;
			bondDayCounter = ActualActual();
			bondConvention = BusinessDayConvention.Following;
			bondRedemption = 100.0;
			bmaFrequency = Frequency.Quarterly;
			bmaConvention = BusinessDayConvention.Following;
			bmaDayCounter = ActualActual();

			deposits = (sizeof(GlobalMembersPiecewiseyieldcurve.depositData)/sizeof(GlobalMembersPiecewiseyieldcurve.depositData[0]));
			fras = (sizeof(GlobalMembersPiecewiseyieldcurve.fraData)/sizeof(GlobalMembersPiecewiseyieldcurve.fraData[0]));
			swaps = (sizeof(GlobalMembersPiecewiseyieldcurve.swapData)/sizeof(GlobalMembersPiecewiseyieldcurve.swapData[0]));
			bonds = (sizeof(GlobalMembersPiecewiseyieldcurve.bondData)/sizeof(GlobalMembersPiecewiseyieldcurve.bondData[0]));
			bmas = (sizeof(GlobalMembersPiecewiseyieldcurve.bmaData)/sizeof(GlobalMembersPiecewiseyieldcurve.bmaData[0]));

			// market elements
			rates = std.<boost.shared_ptr<SimpleQuote> >vector(deposits+swaps);
			fraRates = std.<boost.shared_ptr<SimpleQuote> >vector(fras);
			prices = std.<boost.shared_ptr<SimpleQuote> >vector(bonds);
			fractions = std.<boost.shared_ptr<SimpleQuote> >vector(bmas);
			for (int i =0; i<deposits; i++)
			{
				rates[i] = boost.<SimpleQuote>shared_ptr(new SimpleQuote(GlobalMembersPiecewiseyieldcurve.depositData[i].rate/100));
			}
			for (int i =0; i<swaps; i++)
			{
				rates[i+deposits] = boost.<SimpleQuote>shared_ptr(new SimpleQuote(GlobalMembersPiecewiseyieldcurve.swapData[i].rate/100));
			}
			for (int i =0; i<fras; i++)
			{
				fraRates[i] = boost.<SimpleQuote>shared_ptr(new SimpleQuote(GlobalMembersPiecewiseyieldcurve.fraData[i].rate/100));
			}
			for (int i =0; i<bonds; i++)
			{
				prices[i] = boost.<SimpleQuote>shared_ptr(new SimpleQuote(GlobalMembersPiecewiseyieldcurve.bondData[i].price));
			}
			for (int i =0; i<bmas; i++)
			{
				fractions[i] = boost.<SimpleQuote>shared_ptr(new SimpleQuote(GlobalMembersPiecewiseyieldcurve.bmaData[i].rate/100));
			}

			// rate helpers
			instruments = std.<boost.shared_ptr<RateHelper<YieldTermStructure>> >vector(deposits+swaps);
			fraHelpers = std.<boost.shared_ptr<RateHelper<YieldTermStructure>> >vector(fras);
			bondHelpers = std.<boost.shared_ptr<RateHelper<YieldTermStructure>> >vector(bonds);
			schedules = std.<Schedule>vector(bonds);
			bmaHelpers = std.<boost.shared_ptr<RateHelper<YieldTermStructure>> >vector(bmas);

			boost.shared_ptr<IborIndex> euribor6m = new boost.shared_ptr(new Euribor6M);
			for (int i =0; i<deposits; i++)
			{
				Handle<Quote>[] r = new Handle[i](rates);
				instruments[i] = boost.<RateHelper<YieldTermStructure>>shared_ptr(new DepositRateHelper(r, GlobalMembersPiecewiseyieldcurve.depositData[i].n *GlobalMembersPiecewiseyieldcurve.depositData[i].units, euribor6m.fixingDays(), calendar, euribor6m.businessDayConvention(), euribor6m.endOfMonth(), euribor6m.dayCounter()));
			}
			for (int i =0; i<swaps; i++)
			{
				Handle<Quote>[] r = new Handle[i+deposits](rates);
				instruments[i+deposits] = boost.<RateHelper<YieldTermStructure>>shared_ptr(new SwapRateHelper(r, GlobalMembersPiecewiseyieldcurve.swapData[i].n *GlobalMembersPiecewiseyieldcurve.swapData[i].units, calendar, fixedLegFrequency, fixedLegConvention, fixedLegDayCounter, euribor6m));
			}

			Euribor3M euribor3m;
			for (int i =0; i<fras; i++)
			{
				Handle<Quote>[] r = new Handle[i](fraRates);
				fraHelpers[i] = boost.<RateHelper<YieldTermStructure>>shared_ptr(new FraRateHelper(r, GlobalMembersPiecewiseyieldcurve.fraData[i].n, GlobalMembersPiecewiseyieldcurve.fraData[i].n + 3, euribor3m.fixingDays(), euribor3m.fixingCalendar(), euribor3m.businessDayConvention(), euribor3m.endOfMonth(), euribor3m.dayCounter()));
			}

			for (int i =0; i<bonds; i++)
			{
				Handle<Quote>[] p = new Handle[i](prices);
				Date maturity = calendar.advance(today, GlobalMembersPiecewiseyieldcurve.bondData[i].n, GlobalMembersPiecewiseyieldcurve.bondData[i].units);
				Date issue = calendar.advance(maturity, -GlobalMembersPiecewiseyieldcurve.bondData[i].length, TimeUnit.Years);
				std.vector<Double>[] coupons = new std.vector[i](1, GlobalMembersPiecewiseyieldcurve.bondData.coupon/100.0);
				schedules[i] = Schedule(issue, maturity, Period(GlobalMembersPiecewiseyieldcurve.bondData[i].frequency), calendar, bondConvention, bondConvention, DateGeneration.Backward, false);
				bondHelpers[i] = boost.<RateHelper<YieldTermStructure>>shared_ptr(new FixedRateBondHelper(p, bondSettlementDays, bondRedemption, schedules[i], coupons, bondDayCounter, bondConvention, bondRedemption, issue));
			}
		}*/
	}

}
