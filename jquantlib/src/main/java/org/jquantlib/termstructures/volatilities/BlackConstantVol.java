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
 Copyright (C) 2003, 2004, 2005, 2006, 2007 StatPro Italia srl

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
import org.jquantlib.termstructures.BlackVolatilityTermStructure;
import org.jquantlib.time.Calendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.jquantlib.util.Visitable;
import org.jquantlib.util.Visitor;

/**
 * Constant Black volatility, no time-strike dependence
 * 
 * <p>This class implements the BlackVolatilityTermStructure
 * interface for a constant Black volatility (no time/strike
 * dependence).
 */
public class BlackConstantVol extends BlackVolatilityTermStructure implements Visitable<Object> {

    private Handle<? extends Quote> volatility;
    private DayCounter dayCounter;
    
    @Override
    protected final /*@Volatility*/ double blackVolImpl(final /*@Time*/ double maturity, final /*@Price*/ double strike) {
        return volatility.getLink().doubleValue();
    }
    
    public BlackConstantVol(final Date referenceDate, final /*@Volatility*/ double volatility, final DayCounter dayCounter) {
    	super(referenceDate);
    	this.volatility = new Handle<Quote>(new SimpleQuote(volatility));
    	this.dayCounter = dayCounter;
    }
    
    public BlackConstantVol(final Date referenceDate, final Handle<? extends Quote> volatility, final DayCounter dayCounter) {
    	super(referenceDate);
    	this.volatility = volatility;
    	this.dayCounter = dayCounter;
    	volatility.addObserver(this);
    }
    
    public BlackConstantVol(int settlementDays, final Calendar calendar, final /*@Volatility*/ double volatility, final DayCounter dayCounter) {
    	super(settlementDays, calendar);
    	this.volatility = new Handle<Quote>(new SimpleQuote(volatility));
    	this.dayCounter = dayCounter;
    }
    
    public BlackConstantVol(int settlementDays, final Calendar calendar, final Handle<? extends Quote> volatility, final DayCounter dayCounter) {
    	super(settlementDays, calendar);
    	this.volatility = volatility; 
    	this.dayCounter = dayCounter;
    	volatility.addObserver(this);
    }

    @Override
    public final DayCounter getDayCounter() { 
    	return dayCounter; 
    }
    
    @Override
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

	//
	// implements Visitable
	//
	
	@Override
	public void accept(final Visitor<Object> v) {
		if (v != null) {
			v.visit(this);
		} else {
			super.accept(v);
		}
	}

}
