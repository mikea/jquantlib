/*
 Copyright (C) 2000, 2001, 2002, 2003 RiskMap srl

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

/*
 Copyright (C) 2000, 2001, 2002, 2003 RiskMap srl

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

import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Calendar;
import org.jquantlib.util.Date;

/**
 * Implied term structure at a given date in the future.
 * 
 * @note The given date will be the implied reference date.
 * @note This term structure will remain linked to the original structure, i.e., any changes in the latter will be reflected in this
 *       structure as well.
 */
// TEST the correctness of the returned values is tested by checking them against numerical calculations.
// TEST observability against changes in the underlying term structure is checked.
public class ImpliedTermStructure<T extends YieldTermStructure> extends YieldTermStructure {

	private Handle<T>	originalCurve;

	public ImpliedTermStructure(final Handle<T> h, final Date referenceDate) {
		super(referenceDate);
		this.originalCurve = h;
		this.originalCurve.addObserver(this);
	}

	@Override
	public Calendar getCalendar() /* @ReadOnly */ {
		return null; // FIXME: originalCurve.getLink().getCalendar();
	}

	@Override
	public Date getMaxDate() /* @ReadOnly */ {
		return null; // FIXME: originalCurve.getLink().getMaxDate();
	}

	@Override
	protected /*@DiscountFactor*/ double discountImpl(/*@Time*/double t) /* @ReadOnly */{
		YieldTermStructure yts = originalCurve.getLink();
		/* t is relative to the current reference date
		   and needs to be converted to the time relative
		   to the reference date of the original curve */
		Date ref = getReferenceDate();
		/*@Time*/double originalTime = 0.0; // FUXME: t + getDayCounter().getYearFraction(yts.getReferenceDate(), ref);
		/* discount at evaluation date cannot be cached
		   since the original curve could change between
		   invocations of this method */
		return yts.getDiscount(originalTime, true) / yts.getDiscount(ref, true);
	}

}
