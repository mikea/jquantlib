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

package org.jquantlib.indexes;

import java.util.Currency;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Period;
import org.jquantlib.util.Date;

/**
 * base class for Inter-Bank-Offered-Rate indexes (e.g. %Libor, etc.)
 * 
 * @author Srinivas Hasti
 * 
 */
//TODO: Code review
public class IborIndex extends InterestRateIndex {
    
	private BusinessDayConvention convention_;
	private Handle<YieldTermStructure> termStructure_;
	private boolean endOfMonth_;

	//For now using java currency
	public IborIndex(final String familyName, 
			final Period tenor, 
			/*@Natural*/ int fixingDays,
			final Currency currency,
			final Calendar fixingCalendar, 
			BusinessDayConvention convention, 
			boolean endOfMonth,
			final DayCounter dayCounter, 
			final Handle<YieldTermStructure> handle) {
		super(familyName, tenor, fixingDays, fixingCalendar, currency, dayCounter);
		this.convention_ = convention;
		this.termStructure_ = handle;
		this.endOfMonth_ = endOfMonth;
		handle.getLink().addObserver(this);
	}
	
	   public IborIndex(final String familyName, 
	            final Period tenor, 
	            /*@Natural*/ int fixingDays,
	            final Currency currency,
	            final Calendar fixingCalendar, 
	            BusinessDayConvention convention, 
	            boolean endOfMonth,
	            final DayCounter dayCounter) {
	        this(familyName, 
	                tenor, 
	                fixingDays, 
	                currency, 
	                fixingCalendar, 
	                convention, 
	                endOfMonth, 
	                dayCounter, 
	                new Handle<YieldTermStructure>(YieldTermStructure.class));
	    }
	@Deprecated
	public IborIndex(String familyName, Period tenor, int fixingDays,
			Calendar fixingCalendar, Currency currency,
			BusinessDayConvention convention, boolean endOfMonth,
			DayCounter dayCounter) {
		super(familyName, tenor, fixingDays, fixingCalendar, currency, dayCounter);
		this.convention_ = convention;
		this.endOfMonth_ = endOfMonth;
	}

    /*
	 * (non-Javadoc)
	 * 
	 * @see org.jquantlib.indexes.InterestRateIndex#forecastFixing(org.jquantlib.util.Date)
	 */
	@Override
	protected double forecastFixing(Date fixingDate) {
		if (!termStructure_.isEmpty()){
			throw new IllegalStateException("no forecasting term structure set to " + name());
		}
		Date fixingValueDate = valueDate(fixingDate);
		Date endValueDate = maturityDate(fixingValueDate);
		double fixingDiscount = termStructure_.getLink().discount(fixingValueDate);
		double endDiscount = termStructure_.getLink().discount(endValueDate);
		double fixingPeriod = getDayCounter().yearFraction(fixingValueDate, endValueDate);
		return (fixingDiscount / endDiscount - 1.0) / fixingPeriod;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jquantlib.indexes.InterestRateIndex#getTermStructure()
	 */
	@Override
	public Handle<YieldTermStructure> getTermStructure() {
		return termStructure_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jquantlib.indexes.InterestRateIndex#maturityDate(org.jquantlib.util.Date)
	 */
	@Override
	public Date maturityDate(Date valueDate) {
		return fixingCalendar().advance(valueDate, getTenor(), convention_, endOfMonth_);
	}

	public BusinessDayConvention getConvention() {
		return convention_;
	}

	public boolean isEndOfMonth() {
		return endOfMonth_;
	}
	
	public IborIndex clone(){
	    //FIXME: implement clone here!!!!!!!
//	    return boost::shared_ptr<IborIndex>(
//                new IborIndex(familyName(),
//                              tenor(),
//                              fixingDays(),
//                              currency(),
//                              fixingCalendar(),
//                              businessDayConvention(),
//                              endOfMonth(),
//                              dayCounter(),
//                              h));
	    throw new UnsupportedOperationException("Work in progress...");
	}

}
