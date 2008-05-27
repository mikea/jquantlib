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
package org.jquantlib.termstructures.yield;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.indexes.IborIndex;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.RelinkableHandle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.util.Date;

/**
 * @author Srinivas Hasti
 * 
 */
public class FraRateHelper extends RelativeDateRateHelper {

	private Date fixingDate;
	private int monthsToStart;
	private IborIndex iborIndex;
	private RelinkableHandle<YieldTermStructure> termStructureHandle;

	public FraRateHelper(Handle<Quote> rate, int monthsToStart,
			int monthsToEnd, int fixingDays, Calendar calendar,
			BusinessDayConvention convention, boolean endOfMonth,
			DayCounter dayCounter) {
		this.quote = rate;
		this.monthsToStart = monthsToStart;
		if (monthsToEnd <= monthsToStart)
			throw new IllegalArgumentException(
					"monthsToEnd must be greater than monthsToStart");
		iborIndex = new IborIndex("no-fix", new Period(monthsToEnd
				- monthsToStart, TimeUnit.MONTHS), fixingDays, calendar, null,
				convention, endOfMonth, dayCounter, termStructureHandle);
		initializeDates();

	}

	public FraRateHelper(double rate, int monthsToStart, int monthsToEnd,
			int fixingDays, Calendar calendar,
			BusinessDayConvention convention, boolean endOfMonth,
			DayCounter dayCounter) {
		super(rate);
		this.monthsToStart = monthsToStart;
		if (monthsToEnd <= monthsToStart)
			throw new IllegalArgumentException(
					"monthsToEnd must be grater than monthsToStart");
		iborIndex = new IborIndex(
				"no-fix", // never take fixing into account
				new Period(monthsToEnd - monthsToStart, TimeUnit.MONTHS),
				fixingDays, calendar, null, convention, endOfMonth, dayCounter,
				termStructureHandle);
		initializeDates();
	}

	public FraRateHelper(Handle<Quote> rate, int monthsToStart, IborIndex i) {
		this.quote = rate;
		this.monthsToStart = monthsToStart;
		iborIndex = new IborIndex(
				"no-fix", // never take fixing into account
				i.getTenor(), i.getFixingDays(), i.getFixingCalendar(), null, i
						.getConvention(), i.isEndOfMonth(), i.getDayCounter(),
				termStructureHandle);
		initializeDates();

	}

	public FraRateHelper(double rate, int monthsToStart, IborIndex i) {
		super(rate);
		this.monthsToStart = monthsToStart;
		iborIndex = new IborIndex(
				"no-fix", // never take fixing into account
				i.getTenor(), i.getFixingDays(), i.getFixingCalendar(), null, i
						.getConvention(), i.isEndOfMonth(), i.getDayCounter(),
				termStructureHandle);
		initializeDates();
	}
	
	public double getImpliedQuote()  {
        if(termStructure == null) throw new IllegalStateException("term structure not set");
        return iborIndex.fixing(fixingDate, true);
    }

    public void setTermStructure(YieldTermStructure t) {
        // no need to register---the index is not lazy
        termStructureHandle.setLink(t);
        super.setTermStructure(t);
    }

    protected void initializeDates() {
        Date settlement = iborIndex.getFixingCalendar().advance(
            evaluationDate, new Period(iborIndex.getFixingDays(),TimeUnit.DAYS), BusinessDayConvention.FOLLOWING, false);
        earliestDate = iborIndex.getFixingCalendar().advance(
                               settlement,
                               new Period(monthsToStart,TimeUnit.MONTHS),
                               iborIndex.getConvention(),
                               iborIndex.isEndOfMonth());
        latestDate = iborIndex.maturityDate(earliestDate);
        fixingDate = iborIndex.fixingDate(earliestDate);
    }
}
