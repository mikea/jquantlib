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
import org.jquantlib.util.TypedVisitor;
import org.jquantlib.util.Visitor;

/**
 * This abstract class acts as an adapter to BlackVolTermStructure allowing the
 * programmer to implement only the method
 * {@link BlackVolTermStructure#blackVolImpl(double, double)} in derived
 * classes.
 * 
 * <p>
 * Volatility is assumed to be expressed on an annual basis.
 */
// FIXME: ElementVisitor
abstract public class BlackVolatilityTermStructure extends BlackVolTermStructure {

	/**
	 * Term structures initialized by means of this constructor must manage
	 * their own reference date by overriding the referenceDate() method.
	 */
	public BlackVolatilityTermStructure() {
		this(Actual365Fixed.getDayCounter());
	}

	/**
	 * Term structures initialized by means of this constructor must manage
	 * their own reference date by overriding the referenceDate() method.
	 */
	public BlackVolatilityTermStructure(final DayCounter dc) {
		super(dc);
	}

	/**
	 * Initialize with a fixed reference date
	 */
	public BlackVolatilityTermStructure(final Date referenceDate) {
		this(referenceDate, new NullCalendar());
	}

	/**
	 * Initialize with a fixed reference date
	 */
	public BlackVolatilityTermStructure(final Date referenceDate, final Calendar cal) {
		this(referenceDate, cal, Actual365Fixed.getDayCounter());
	}

	/**
	 * Initialize with a fixed reference date
	 */
	public BlackVolatilityTermStructure(final Date refDate, final Calendar cal, final DayCounter dc) {
		super(refDate, cal, dc);
	}

	/**
	 * Calculate the reference date based on the global evaluation date
	 */
	public BlackVolatilityTermStructure(int settlementDays, final Calendar cal) {
		this(settlementDays, cal, Actual365Fixed.getDayCounter());
	}

	/**
	 * Calculate the reference date based on the global evaluation date
	 */
	public BlackVolatilityTermStructure(int settlementDays, final Calendar cal, final DayCounter dc) {
		super(settlementDays, cal, dc);
	}

	/**
	 * Returns the variance for the given strike and date calculating it from
	 * the volatility.
	 */
	@Override
	protected final /*@Variance*/ double blackVarianceImpl(final /*@Time*/ double maturity, final /*@Price*/ double strike) {
		/*@Volatility*/ double vol = blackVolImpl(maturity, strike);
		/*@Variance*/ double variance = vol*vol*maturity;
		return variance;
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
