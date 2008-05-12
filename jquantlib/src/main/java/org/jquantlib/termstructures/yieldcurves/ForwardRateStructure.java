/*
 * Copyright (C) 2007 Richard Gomes This file is part of JQuantLib, a free-software/open-source library for financial quantitative
 * analysts and developers - http://jquantlib.org/ JQuantLib is free software: you can redistribute it and/or modify it under the
 * terms of the QuantLib license. You should have received a copy of the license along with this program; if not, please email
 * <jquant-devel@lists.sourceforge.net>. The license is also available online at <http://www.jquantlib.org/index.php/LICENSE.TXT>.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license for more details. JQuantLib is based on QuantLib.
 * http://quantlib.org/ When applicable, the original copyright notice follows this notice.
 */

/*
 * Copyright (C) 2000, 2001, 2002, 2003 RiskMap srl Copyright (C) 2003, 2004 StatPro Italia srl This file is part of QuantLib, a
 * free-software/open-source library for financial quantitative analysts and developers - http://quantlib.org/ QuantLib is free
 * software: you can redistribute it and/or modify it under the terms of the QuantLib license. You should have received a copy of
 * the license along with this program; if not, please email <quantlib-dev@lists.sf.net>. The license is also available online at
 * <http://quantlib.org/license.shtml>. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license for more details.
 */

package org.jquantlib.termstructures.yieldcurves;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Calendar;
import org.jquantlib.util.Date;

// TODO : Finish (Richard)

/**
 * Forward-rate term structure
 * <p>
 * This abstract class acts as an adapter to TermStructure allowing the programmer to implement only method
 * {@link #forwardImpl(double)} in derived classes. Zero yields and discounts are calculated from forwards. Rates are assumed to be
 * annual continuous compounding.
 * 
 * @note This class is abstract and has protected constructors so that it can only be constructed by extended classes
 */
public abstract class ForwardRateStructure extends YieldTermStructure {

	protected ForwardRateStructure(final DayCounter dc) {
		super(dc);
	}

	protected ForwardRateStructure(final Date refDate, final Calendar cal, final DayCounter dc) {
		super(refDate, cal, dc);
	}

	protected ForwardRateStructure(final int settlDays, final Calendar cal, final DayCounter dc) {
		super(settlDays, cal, dc);
	}

	/**
	 * Instantaneous forward-rate calculation
	 */
	protected abstract /* @Rate */ double forwardImpl(/* @Time */double t);

	/**
	 * Returns the zero yield rate for the given date calculating it from the instantaneous forward rate.
	 * 
	 * @note This is just a default, highly inefficient and possibly wildly inaccurate implementation. Derived classes should
	 *       implement their own zeroYield method.
	 */
	protected/* @Rate */double zeroYieldImpl(/* @Time */double t) /* @ReadOnly */{
		if (t == 0.0) return forwardImpl(0.0);
		// implement smarter integration if plan to use the following code
		/* @Rate */double sum = 0.5 * forwardImpl(0.0);
		int n = 1000;
		/* @Time */double dt = t / n;
		for (/* @Time */double i = dt; i < t; i += dt)
			sum += forwardImpl(i);
		sum += 0.5 * forwardImpl(t);
		return sum * dt / t;
	}

	@Override
	protected/* @DiscountFactor */double discountImpl(/* @Time */double t) /* @ReadOnly */{
		/* @Rate */double r = zeroYieldImpl(t);
		return Math.exp(-r * t);
	}

}
