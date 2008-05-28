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
 Copyright (C) 2002, 2003 Ferdinando Ametrano
 Copyright (C) 2003, 2004, 2005, 2006 StatPro Italia srl

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

package org.jquantlib.termstructures;

import org.jquantlib.daycounters.Actual365Fixed;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.NullCalendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.Visitable;
import org.jquantlib.util.Visitor;

// Black variance term structure

/** This abstract class acts as an adapter to VolTermStructure allowing
 *  the programmer to implement only the
 *  <tt>blackVarianceImpl(Time, Real, bool)</tt> method in derived
 *  classes.
 *
 *  <p>
 *  Volatility is assumed to be expressed on an annual basis.
 */
// FIXME: code review
public abstract class BlackVarianceTermStructure extends BlackVolTermStructure implements Visitable<Object> {

        /*! \name Constructors
            See the TermStructure documentation for issues regarding
            constructors.
        */

        /*! \warning term structures initialized by means of this
                     constructor must manage their own reference date
                     by overriding the referenceDate() method.
        */
	public BlackVarianceTermStructure() {
		this(new Actual365Fixed());
	}
	
    public BlackVarianceTermStructure(final DayCounter dc) {
    	super(dc);
    }

    //! initialize with a fixed reference date
    public BlackVarianceTermStructure(final Date referenceDate) {
    	this(referenceDate, new NullCalendar());
    }

    public BlackVarianceTermStructure(final Date referenceDate, final Calendar cal) {
    	this(referenceDate, cal, new Actual365Fixed());
    }

    public BlackVarianceTermStructure(final Date referenceDate, final Calendar cal, final DayCounter dc) {
    	super(referenceDate, cal, dc);
    }


    
    
    
    
    
    //! calculate the reference date based on the global evaluation date
    public BlackVarianceTermStructure(int settlementDays, final Calendar cal) {
    	super(settlementDays, cal, new Actual365Fixed());
    }

    public BlackVarianceTermStructure(int settlementDays, final Calendar cal, final DayCounter dc) {
    	super(settlementDays, cal, dc);
    }

    
    /*! Returns the volatility for the given strike and date calculating it
        from the variance.
    */
    protected final /*@Volatility*/ double blackVolImpl(final /*@Time*/ double maturity, final /*@Price*/ double strike) {
    	/*@Time*/ double nonZeroMaturity;
    	/*@Time*/ double m = maturity;
		if (m==0.0) {
			nonZeroMaturity = 0.00001;
		} else {
			nonZeroMaturity = m;
		}
		/*@Variance*/ double var = blackVarianceImpl(/*Time*/ nonZeroMaturity, strike);
		return Math.sqrt(var/nonZeroMaturity);
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
