/*
 Copyright (C) 2007 Richard Gomes

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

/*
 Copyright (C) 2002, 2003, 2004 Ferdinando Ametrano

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

package org.jquantlib.termstructures.volatilities;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.termstructures.LocalVolTermStructure;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.NullCalendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;

// Local constant volatility, no time dependence, no asset dependence

/**
 * This class implements the LocalVolatilityTermStructure interface for a
 * constant local volatility (no time/asset dependence). Local volatility and
 * Black volatility are the same when volatility is at most time dependent, so
 * this class is basically a proxy for BlackVolatilityTermStructure.
 */
public class LocalConstantVol extends LocalVolTermStructure {

	private Handle<Quote> volatility_;
	private DayCounter dayCounter_;

	public LocalConstantVol(
			final Date referenceDate, 
			final /*@Volatility*/ double volatility, 
			final DayCounter dayCounter) {
		super(referenceDate);
		this.volatility_ = new Handle<Quote>(new SimpleQuote(volatility));
		this.dayCounter_ = dayCounter;
	}

	public LocalConstantVol(
			final Date referenceDate, 
			final Handle<Quote> volatility, 
			final DayCounter dayCounter) {
		super(referenceDate);
		this.volatility_ = volatility;
		this.dayCounter_ = dayCounter;
		this.volatility_.addObserver(this);
	}

	public LocalConstantVol(
			int settlementDays, 
			final Calendar cal, 
			final /*@Volatility*/ double volatility, 
			final DayCounter dayCounter) {
		super(settlementDays, new NullCalendar());
		this.volatility_ = new Handle<Quote>(new SimpleQuote(volatility));
		this.dayCounter_ = dayCounter;
	}

	public LocalConstantVol(
			int settlementDays, 
			final Calendar cal, 
			final Handle<Quote> volatility, 
			final DayCounter dayCounter) {
		super(settlementDays, new NullCalendar());
		this.volatility_ = volatility;
		this.dayCounter_ = dayCounter;
		this.volatility_.addObserver(this);
	}

	public final DayCounter dayCounter() {
		return dayCounter_;
	}

	public final Date getMaxDate() {
		return DateFactory.getFactory().getMaxDate();
	}

	@Override
	public final /*@Price*/ double getMinStrike() {
		return Double.NEGATIVE_INFINITY;
	}

	@Override
	public final /*@Price*/ double getMaxStrike() {
		return Double.POSITIVE_INFINITY;
	}

	@Override
	protected final /*@Volatility*/ double localVolImpl(final /*@Time*/ double maturity, final /*@Price*/ double strike) {
		return this.volatility_.getLink().doubleValue();
	}

}
