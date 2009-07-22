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
 * Copyright (C) 2002, 2003 Decillion Pty(Ltd)
 * Copyright (C) 2005, 2006 StatPro Italia srl
 * 
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
import org.jquantlib.math.Array;
import org.jquantlib.math.interpolations.Interpolation;
import org.jquantlib.math.interpolations.Interpolator;
import org.jquantlib.math.interpolations.factories.LogLinear;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Target;
import org.jquantlib.util.Date;
import org.jquantlib.util.Pair;


/**
 * Term structure based on interpolation of discount factors.
 * 
 * @note LogLinear interpolation is assumed by default when no interpolation class is passed to constructors.
 * Log-linear interpolation guarantees piecewise-constant forward rates.
 */
public class InterpolatedDiscountCurve /* <T extends Interpolator> extends YieldTermStructure implements CurveTraits */ {

//	//
//	// protected fields
//	//
//
//	protected Date[]								dates;
//	protected /* @Time */ Array					    times;
//	protected /* @Rate */ Array					    data;
//	protected boolean								isNegativeRates;
//
//
//	// TODO: These fields should not be protected !!!
//	// Extended classes access and modify directly these fields 
//	// in a way which breaks code encapsulation and good practices
//	protected Interpolator							interpolator;
//	protected Interpolation							interpolation;
//
//	
//	
//	//
//	// protected constructors
//	//
//
//	protected InterpolatedDiscountCurve(final DayCounter dayCounter, final T interpolator) {
//		super(dayCounter);
//		this.isNegativeRates = settings.isNegativeRates();
//		this.interpolator = (interpolator!=null) ? interpolator : new LogLinear();
//
//		if (0==0) throw new UnsupportedOperationException("Work in progress");
//		
//	}
//
//	protected InterpolatedDiscountCurve(final Date referenceDate, final DayCounter dayCounter, final T interpolator) {
//		super(referenceDate, Target.getCalendar(), dayCounter); // FIXME: code review :: default calendar
//		this.isNegativeRates = settings.isNegativeRates();
//		this.interpolator = (interpolator!=null) ? interpolator : new LogLinear();
//
//		if (0==0) throw new UnsupportedOperationException("Work in progress");
//		
//	}
//
//	protected InterpolatedDiscountCurve(final int settlementDays, final Calendar calendar, final DayCounter dayCounter,
//			final T interpolator) {
//		super(settlementDays, calendar, dayCounter);
//		this.isNegativeRates = settings.isNegativeRates();
//		this.interpolator = (interpolator!=null) ? interpolator : new LogLinear();
//
//		if (0==0) throw new UnsupportedOperationException("Work in progress");
//		
//	}
//
//	//
//	// public constructors
//	//
//	
//	public InterpolatedDiscountCurve(final Date[] dates, final /* @DiscountFactor */ Array discounts,
//			final DayCounter dayCounter, final Calendar cal, final T interpolator) {
//		super(dates[0], cal, dayCounter);
//
//		if (0==0) throw new UnsupportedOperationException("Work in progress");
//		
//    	this.dates = dates.clone();
//		this.data = discounts.clone();
//		this.isNegativeRates = settings.isNegativeRates();
//		this.interpolator = (interpolator!=null) ? interpolator : new LogLinear();
//
//		if (this.dates == null || this.dates.length == 0) throw new IllegalArgumentException("no input dates given"); // FIXME: message
//		if (this.data == null || this.data.length != this.dates.length)
//			throw new IllegalArgumentException("dates/discount factors count mismatch"); // FIXME: message
//		if (this.data.first() != 1.0)
//			throw new IllegalArgumentException("the first discount must be == 1.0 to flag the corrsponding date as settlement date"); // FIXME: message
//
//		this.times = new Array(this.dates.length);
//		times.set(0, 0.0);
//		for (int i = 1; i < dates.length; i++) {
//			if (this.dates[i].le(this.dates[i - 1]))
//				throw new IllegalArgumentException("invalid date (" + dates[i] + ", vs " + dates[i - 1] + ")"); // FIXME: message
//			if (this.data.get(i) <= 0.0) throw new IllegalArgumentException("negative discount"); // FIXME: message
//
//			double value = dayCounter.yearFraction(dates[0], dates[i]);
//			times.set(i, value);
//		}
//
//		this.interpolation = this.interpolator.interpolate(times, discounts);
//		this.interpolation.update();
//	}
//
//	
//	//
//	// implement abstract methods
//	//
//
//	protected /* @DiscountFactor */double discountImpl(final/* @Time */double t) /* @ReadOnly */{
//		return interpolation.evaluate(t, true);
//	}
//
//	
//	//
//	// implements YieldCurve
//	//
//
//	@Override
//	public Date[] getDates() /* @ReadOnly */{
//    	return dates.clone();
//	}
//
//	@Override
//	public /* @DiscountFactor */ Array getData() /* @ReadOnly */{
//    	return data.clone();
//	}
//
//	@Override
//	public Date maxDate() /* @ReadOnly */{
//		return dates[dates.length - 1];
//	}
//
//	@Override
//	public Pair<Date, Double>[] getNodes() /* @ReadOnly */{
//		Pair<Date, /* @Rate */Double>[] results = new Pair /* <Date, @Rate Double> */[dates.length];
//		for (int i = 0; i < dates.length; ++i)
//			results[i] = new Pair<Date, Double>(dates[i], data.get(i));
//		return results;
//	}
//
//	@Override
//	public Array getTimes() /* @ReadOnly */{
//    	return times.clone();
//	}

}
