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
import org.jquantlib.termstructures.TermStructure;
import org.jquantlib.time.Calendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.jquantlib.util.TypedVisitor;
import org.jquantlib.util.Visitor;

/**
 * Constant Black volatility, no time-strike dependence
 * 
 * <p>This class implements the BlackVolatilityTermStructure
 * interface for a constant Black volatility (no time/strike
 * dependence).
 */
public class BlackConstantVol extends BlackVolatilityTermStructure {

    private Handle<? extends Quote> volatility;
    private DayCounter dayCounter;
    
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

	public BlackConstantVol(final Date referenceDate,
		final Calendar calendar, final /*@Volatility*/ double volatility, 
		final DayCounter dayCounter){

    	super(referenceDate,calendar);
		
    	this.volatility = new Handle<Quote>(new SimpleQuote(volatility));
    	this.dayCounter = dayCounter;
		
	}
	
    
	//
	// Overrides TermStructure
	//
	
	@Override
    public final DayCounter dayCounter() { 
    	return dayCounter; 
    }
    
    @Override
    public final Date maxDate() {
        return DateFactory.getFactory().getMaxDate();
    }
    
    
    //
    // Override BlackVolTermStructure
    //
    
    @Override
    public final /*@Price*/ double minStrike() {
    	return Double.NEGATIVE_INFINITY;
	}
    
	@Override
    public final /*@Price*/ double maxStrike() {
    	return Double.POSITIVE_INFINITY;
	}
	
    @Override
    protected final /*@Volatility*/ double blackVolImpl(final /*@Time*/ double maturity, final /*@Price*/ double strike) {
        return volatility.getLink().evaluate();
    }
    

	//
	// implements TypedVisitable
	//
	
	@Override
	public void accept(final TypedVisitor<TermStructure> v) {
		Visitor<TermStructure> v1 = (v!=null) ? v.getVisitor(this.getClass()) : null;
		if (v1 != null) {
			v1.visit(this);
		} else {
			super.accept(v);
		}
	}

}
