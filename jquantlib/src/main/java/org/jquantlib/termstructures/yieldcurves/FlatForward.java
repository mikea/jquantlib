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
 Copyright (C) 2000, 2001, 2002, 2003 RiskMap srl
 Copyright (C) 2003, 2004, 2005, 2007 StatPro Italia srl

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

package org.jquantlib.termstructures.yieldcurves;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.termstructures.Compounding;
import org.jquantlib.termstructures.InterestRate;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Frequency;
import org.jquantlib.time.calendars.NullCalendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;

public class FlatForward extends YieldTermStructure {

	private Handle<? extends Quote> forward;
	private Compounding compounding;
	private Frequency frequency;
	private InterestRate rate;


	// --------------------------------------------
	
    public FlatForward(
    		final Date referenceDate,
            final Handle<? extends Quote> forward,
            final DayCounter dayCounter,
            final Compounding compounding,
            final Frequency frequency) {
		super(referenceDate, new NullCalendar(), dayCounter);
		this.forward = forward;
		this.compounding = compounding;
		this.frequency = frequency;
		this.forward.addObserver(this);
		updateRate();
	}

    public FlatForward(
			final Date referenceDate,
            final Handle<? extends Quote> forward,
            final DayCounter dayCounter,
            final Compounding compounding) {
		this(referenceDate, forward, dayCounter, compounding, Frequency.ANNUAL);
	}

	public FlatForward(
				final Date referenceDate,
	            final Handle<? extends Quote> forward,
	            final DayCounter dayCounter) {
		this(referenceDate, forward, dayCounter, Compounding.CONTINUOUS);
	}
	
	// --------------------------------------------
	
    public FlatForward(
			final Date referenceDate,
			final /*@Rate*/ double forward,
			final DayCounter dayCounter,
			final Compounding compounding,
			final Frequency frequency) {
		super(referenceDate, new NullCalendar(), dayCounter);
		this.forward = new Handle<SimpleQuote>(new SimpleQuote(forward));
		this.compounding = compounding;
		this.frequency = frequency;
		updateRate();
	}
    
    public FlatForward(
			final Date referenceDate,
            final /*@Rate*/ double forward,
            final DayCounter dayCounter,
            final Compounding compounding) {
		this(referenceDate, forward, dayCounter, compounding, Frequency.ANNUAL);
	}

    public FlatForward(
			final Date referenceDate,
            final /*@Rate*/ double forward,
            final DayCounter dayCounter) {
		this(referenceDate, forward, dayCounter, Compounding.CONTINUOUS);
	}

	// --------------------------------------------
	
    public FlatForward(
    		int settlementDays,
            final Calendar calendar,
            final Handle<? extends Quote> forward,
            final DayCounter dayCounter,
            final Compounding compounding,
            final Frequency frequency) {
		super(settlementDays, calendar, dayCounter);
		this.forward = forward;
		this.compounding = compounding;
		this.frequency = frequency;
		this.forward.addObserver(this);
		updateRate();
	}

    public FlatForward(
    			int settlementDays,
                final Calendar calendar,
                final Handle<? extends Quote> forward,
                final DayCounter dayCounter) {
    	this(settlementDays, calendar, forward, dayCounter, Compounding.CONTINUOUS);
    }

    public FlatForward(
    			int settlementDays,
                final Calendar calendar,
                final Handle<? extends Quote> forward,
                final DayCounter dayCounter,
                final Compounding compounding) {
    	this(settlementDays, calendar, forward, dayCounter, compounding, Frequency.ANNUAL);
    }
    
	// --------------------------------------------
	
    public FlatForward(
    		int settlementDays,
            final Calendar calendar,
            final /*@Rate*/ double forward,
            final DayCounter dayCounter,
            final Compounding compounding,
            final Frequency frequency) {
		super(settlementDays, calendar, dayCounter);
		this.forward = new Handle<Quote>(new SimpleQuote(forward));
		this.compounding = compounding;
		this.frequency = frequency;
		updateRate();
	}

    public FlatForward(
    			int settlementDays,
                final Calendar calendar,
                final /*@Rate*/ double forward,
                final DayCounter dayCounter) {
    	this(settlementDays, calendar, forward, dayCounter, Compounding.CONTINUOUS);
    }
    
    public FlatForward(
    			int settlementDays,
                final Calendar calendar,
                final /*@Rate*/ double forward,
                final DayCounter dayCounter,
                final Compounding compounding) {
    	this(settlementDays, calendar, forward, dayCounter, compounding, Frequency.ANNUAL);
    }

	// --------------------------------------------
	
    private void updateRate() {
        rate = new InterestRate(forward.getLink().doubleValue(), this.getDayCounter(), this.compounding, this.frequency);
    }

    @Override
	protected final /*@DiscountFactor*/ double discountImpl(final /*@Time*/ double t) {
        return rate.getDiscountFactor(t);
    }
    
    public final Compounding compounding() {
    	return compounding;
    }

    public final Frequency compoundingFrequency() {
    	return frequency;
    }

    public final Date getMaxDate() {
        return DateFactory.getFactory().getMaxDate();
    }




// FIXME: code review


    //
    // Implements Observer interface
    //
    
//    private Observer delegate = new DefaultObserverImpl();
    
    /**
     * This method implements {@link Observer#update(Observable, Object)}
     * 
     * @see Observer#update(Observable, Object)
     */
//    public void update(Observable o, Object arg) {
//        updateRate();
//        delegate.update(o, arg);
//    }

    
    //
    // delegated private inner class
    //
    
//    private class DefaultObserverImpl implements Observer {
//    	
//        public void update(Observable o, Object arg) {
//        System.out.println("blah!");
//        }
//        
//    }

}
