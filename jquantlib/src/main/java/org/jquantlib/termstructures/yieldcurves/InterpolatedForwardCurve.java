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
 Copyright (C) 2005, 2006 StatPro Italia srl

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

import it.unimi.dsi.fastutil.doubles.DoubleArrays;
import it.unimi.dsi.fastutil.objects.ObjectArrays;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.math.interpolations.Interpolation;
import org.jquantlib.math.interpolations.Interpolator;
import org.jquantlib.math.interpolations.factories.BackwardFlat;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Target;
import org.jquantlib.util.Date;
import org.jquantlib.util.Pair;

/**
 * Term structure based on interpolation of forward rates
 * 
 * @note BackwardFlat interpolation is assumed by default when no interpolation class is passed to constructors.
 * Term structure based on flat interpolation of forward rates.
 */
public final class InterpolatedForwardCurve<T extends Interpolator> extends ForwardRateStructure implements YieldCurve {

	//
	// protected fields
	//

	protected Date[]								dates;
	protected/* @Time */double[]					times;
	protected/* @Rate */double[]					data;
	protected Interpolation							interpolation;
	protected boolean								isNegativeRates;

	//
	// private fields
	//
	
	private Interpolator							interpolator;


	//
	// protected constructors
	//
	
    protected InterpolatedForwardCurve(final DayCounter dayCounter, final T interpolator) {
    	super(dayCounter);
		this.interpolator = (interpolator!=null) ? interpolator : new BackwardFlat();

		if (0==0) throw new UnsupportedOperationException("Work in progress");
		
    }

    protected InterpolatedForwardCurve(final Date referenceDate, final DayCounter dayCounter, final T interpolator) {
        super(referenceDate, Target.getCalendar(), dayCounter); // FIXME: code review:: default calendar
		this.interpolator = (interpolator!=null) ? interpolator : new BackwardFlat();

		if (0==0) throw new UnsupportedOperationException("Work in progress");
		
    }

    protected InterpolatedForwardCurve(final int settlementDays, final Calendar calendar, final DayCounter dayCounter, final T interpolator) {
        super(settlementDays, calendar, dayCounter);
		this.interpolator = (interpolator!=null) ? interpolator : new BackwardFlat();

		if (0==0) throw new UnsupportedOperationException("Work in progress");
		
    }

	
	//
    // public constructors
    //
	
	public InterpolatedForwardCurve(final Date[] dates, final/* @Rate */double[] forwards, final DayCounter dayCounter,
			final T interpolator) {
		// FIXME: code review: calendar
		// FIXME: must check dates
		super(dates[0], Target.getCalendar(), dayCounter);

		if (0==0) throw new UnsupportedOperationException("Work in progress");
		
    	this.dates = dates.clone();
		this.data = forwards.clone();
		this.isNegativeRates = settings.isNegativeRates();
		this.interpolator = (interpolator!=null) ? interpolator : new BackwardFlat();

		if (dates.length <= 1) throw new IllegalArgumentException("too few dates"); // FIXME: message
		if (dates.length != forwards.length) throw new IllegalArgumentException("dates/yields count mismatch"); // FIXME: message


		times = new /* @Time */double[dates.length];
		for (int i = 1; i < dates.length; i++) {
			if (dates[i].le(dates[i - 1]))
				throw new IllegalArgumentException("invalid date (" + dates[i] + ", vs " + dates[i - 1] + ")"); // FIXME: message
			if (!isNegativeRates && (forwards[i] < 0.0)) throw new IllegalArgumentException("negative forward"); // FIXME: message
			times[i] = dayCounter.yearFraction(dates[0], dates[i]);
		}

		this.interpolation = this.interpolator.interpolate(times, forwards);
		this.interpolation.update();
	}

	
	//
	// implements YieldCurve
	//

	@Override
	public final Date[] getDates() /* @ReadOnly */{
    	return dates.clone();
	}

	@Override
	public final/* @DiscountFactor */double[] getData() /* @ReadOnly */{
    	return data.clone();
	}

	@Override
	public final Date maxDate() /* @ReadOnly */{
		return dates[dates.length - 1];
	}

	@Override
	public final Pair<Date, Double>[] getNodes() /* @ReadOnly */{
		Pair<Date, /*@Rate*/Double>[] results = new Pair /* <Date, @Rate Double> */[dates.length];
		for (int i = 0; i < dates.length; ++i)
			results[i] = new Pair<Date, Double>(dates[i], data[i]);
		return results;
	}

	@Override
	public final double[] getTimes() /* @ReadOnly */{
    	return times.clone();
	}

	@Override
	public/*@Rate*/double forwardImpl(/*@Time*/double t) /* @ReadOnly */{
		return interpolation.evaluate(t, true); // FIXME: code review
	}

	@Override
	public/*@Rate*/double zeroYieldImpl(/*@Time*/double t) /* @ReadOnly */{
		if (t == 0.0)
			return forwardImpl(0.0);
		else return interpolation.primitive(t, true) / t; // FIXME: code review
	}

}
