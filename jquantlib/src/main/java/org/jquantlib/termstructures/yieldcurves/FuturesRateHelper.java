/*
 Copyright (C) 2008 Richard Gomes

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

package org.jquantlib.termstructures.yieldcurves;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.indexes.IborIndex;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.termstructures.RateHelper;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.IMM;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.util.Date;
import org.jquantlib.util.Observable;

/**
 * @author Srinivas Hasti
 * 
 */
//TODO: Complete
public class FuturesRateHelper extends RateHelper<YieldTermStructure> {

	private double yearFraction;
	private Handle<Quote> convAdj;

	public FuturesRateHelper(Handle<Quote> price, Date immDate, int nMonths,
			Calendar calendar, BusinessDayConvention convention,
			boolean endOfMonth, DayCounter dayCounter, Handle<Quote> convAdj) {
		super(price, null, null, null);

		if (!IMM.getDefaultIMM().isIMMdate(immDate, false))
			throw new IllegalArgumentException(" is not a valid IMM date");

		earliestDate = immDate;
		latestDate = calendar.advance(immDate, new Period(nMonths,
				TimeUnit.MONTHS), convention, endOfMonth);
		yearFraction = dayCounter.yearFraction(earliestDate, latestDate);

		// registerWith(convAdj_);
	}

	public FuturesRateHelper(double price, Date immDate, int nMonths,
			Calendar calendar, BusinessDayConvention convention,
			boolean endOfMonth, DayCounter dayCounter, double conv) {
		super(price);
		convAdj = new Handle<Quote>(new SimpleQuote(conv));
		if (!IMM.getDefaultIMM().isIMMdate(immDate, false))
			throw new IllegalArgumentException(" is not a valid IMM date");

		earliestDate = immDate;
		latestDate = calendar.advance(immDate, new Period(nMonths,
				TimeUnit.MONTHS), convention, endOfMonth);
		yearFraction = dayCounter.yearFraction(earliestDate, latestDate);
	}

	public FuturesRateHelper(double price, Date immDate, IborIndex i,
			double conv) {
		super(price);
		convAdj = new Handle<Quote>(new SimpleQuote(conv));
		if (!IMM.getDefaultIMM().isIMMdate(immDate, false))
			throw new IllegalArgumentException(" is not a valid IMM date");
		earliestDate = immDate;
		Calendar cal = i.fixingCalendar();
		latestDate = cal.advance(immDate, i.tenor(), i.getConvention());
		yearFraction = i.dayCounter().yearFraction(earliestDate,
				latestDate);
	}

	public double impliedQuote() {
		if (termStructure == null)
			throw new IllegalStateException("term structure not set");
		double forwardRate = termStructure.discount(earliestDate)
				/ (termStructure.discount(latestDate) - 1.0) / yearFraction;
		double convA = convAdj.empty() ? 0.0 : convAdj.getLink()
				.evaluate();
		if (convA < 0.0)
			throw new IllegalStateException("Negative (" + convA
					+ ") futures convexity adjustment");
		double futureRate = forwardRate + convA;
		return 100.0 * (1.0 - futureRate);
	}

	public double getConvexityAdjustment() {
		return convAdj.empty() ? 0.0 : convAdj.getLink().evaluate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jquantlib.util.Observer#update(org.jquantlib.util.Observable,
	 *      java.lang.Object)
	 */
	@Override
	//TODO: MOVE TO BASE CLASS
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}

}
