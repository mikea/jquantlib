/*
 Copyright (C) 2007 Richard Gomes

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
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

import org.jquantlib.Configuration;
import org.jquantlib.Settings;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.math.interpolation.Interpolation;
import org.jquantlib.math.interpolation.Interpolator;
import org.jquantlib.math.interpolation.factories.Linear;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Target;
import org.jquantlib.util.Date;
import org.jquantlib.util.Pair;

// TODO: Finish (Richard)

/**
 * Term structure based on interpolation of forward rates
 */
public class InterpolatedForwardCurve<I extends Interpolator> extends ForwardRateStructure implements YieldCurve {

	//
	// private fields
	//
	// In the original C++ implementation these fields are protected and, doing so, accessible by
	// PiecewiseCurve, which *optionally* extends this class, depending on template metaprogramming.
	//
	private Date[]				dates;
	private /* @Time */double[]	times;
	private /* @Rate */double[]	data; // forwards
	
	//
	// protected fields
	//
	
	protected Interpolation			interpolation;
	protected boolean isNegativeRates;
	
	
	public InterpolatedForwardCurve(
			final Date[] dates, 
			final/* @Rate */double[] forwards, 
			final DayCounter dayCounter,
			final Class<I> classInterpolator) {
		// FIXME: code review: calendar
		// FIXME: must check dates
		super(dates[0], Target.getCalendar(), dayCounter);
		this.dates = dates;
		this.data = forwards;

		if (dates.length <= 1) throw new IllegalArgumentException("too few dates"); // FIXME: message
		if (dates.length != data.length) throw new IllegalArgumentException("dates/yields count mismatch"); // FIXME: message

		// initialize isNegativeRates
		obtainSettings();
		
		times = new /* @Time */double[dates.length];
		for (int i = 1; i < dates.length; i++) {
			if (dates[i].le(dates[i - 1]))
				throw new IllegalArgumentException("invalid date (" + dates[i] + ", vs " + dates[i - 1] + ")"); // FIXME: message
			if (!isNegativeRates && (data[i] < 0.0))
				throw new IllegalArgumentException("negative forward"); // FIXME: message
			times[i] = dayCounter.getYearFraction(dates[0], dates[i]);
		}

		this.interpolation = obtainInterpolator(classInterpolator).interpolate(times, data);
		this.interpolation.update();
	}

	protected InterpolatedForwardCurve(final DayCounter dayCounter, final Class<I> classInterpolator) {
		super(dayCounter);
		obtainSettings();
		
		// FIXME: code review ::: this is probably wrong
		this.times = new /*@Time*/ double[0];
		this.dates = new Date[0];
		this.data  = new double[0];
		this.interpolation = obtainInterpolator(classInterpolator).interpolate(times, data);
	}

	protected InterpolatedForwardCurve(final Date referenceDate, final DayCounter dayCounter, final Class<I> classInterpolator) {
		super(referenceDate, Target.getCalendar(), dayCounter); // FIXME: code review: calendar
		obtainSettings();
		
		// FIXME: code review ::: this is probably wrong
		this.times = new /*@Time*/ double[0];
		this.dates = new Date[0];
		this.data  = new double[0];
		this.interpolation = obtainInterpolator(classInterpolator).interpolate(times, data);
	}

	protected InterpolatedForwardCurve(final int settlementDays, final Calendar calendar, final DayCounter dayCounter,
			final Class<I> classInterpolator) {
		super(settlementDays, calendar, dayCounter);
		obtainSettings();
		
		// FIXME: code review ::: this is probably wrong
		this.times = new /*@Time*/ double[0];
		this.dates = new Date[0];
		this.data  = new double[0];
		this.interpolation = obtainInterpolator(classInterpolator).interpolate(times, data);
	}

	/**
	 * Obtains Global Settings and copy settings locally to <i>this</i> instance
	 */
	private void obtainSettings() {
		// obtain reference to Settings
		Settings settings = Configuration.getSystemConfiguration(null).getGlobalSettings();
		this.isNegativeRates = settings.isNegativeRates();
	}
	
	
	private Interpolator obtainInterpolator(final Class<I> classInterpolator) {
		// Constructs the Interpolator
		try {
			Class<I> klass = classInterpolator;
			if (classInterpolator==null) {
				klass = (Class<I>) Linear.class; // FIXME: code review :: This is arbitrary, I hadn't better to invent here.
			}
			Interpolator interpolator = (Interpolator) (klass.getDeclaredConstructor().newInstance());
			return interpolator;
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	
	//
	// extends ForwardRateStructure
	//
	
    protected /*@Rate*/ double forwardImpl(/*@Time*/ double t) /* @ReadOnly */ {
        return interpolation.evaluate(t, true); // FIXME: code review
    }

    protected /*@Rate*/ double zeroYieldImpl(/*@Time*/ double t) /* @ReadOnly */ {
        if (t == 0.0)
            return forwardImpl(0.0);
        else
            return interpolation.primitive(t, true)/t; // FIXME: code review
    }

	
	//
	// implements PiecewiseYieldCurve.YieldCurve
	//
	
	@Override
	public final Date[] getDates() /* @ReadOnly */ {
		return dates;
	}

	@Override
	public final double[] getData() /* @ReadOnly */ {
		return data;
	}

	@Override
	public final Date getMaxDate() /* @ReadOnly */ {
		return dates[dates.length-1];
	}
	
	@Override
	public final Pair<Date, Double>[] getNodes() /* @ReadOnly */ {
      Pair<Date, /*@Rate*/ Double>[] results = new Pair /* <Date, @Rate Double> */ [dates.length];
      for (int i=0; i<dates.length; ++i)
          results[i] = new Pair<Date, Double>(dates[i], data[i]);
      return results;
	}

	@Override
	public final double[] getTimes() /* @ReadOnly */ {
		return times;
	}

	// In particular, these methods should not exist in the interface.
	// In the original C++ implementation the related fields are protected and, doing so, accessible by
	// PiecewiseCurve, which *optionally* extends ancestor classes using template metaprogramming.
	
	@Override 
	public final void setMaxDate(final Date maxDate) {
		dates[dates.length-1] = maxDate;
	}

	@Override
	public void setDates(Date[] dates) {
		this.dates = dates;
	}

	@Override
	public void setTimes(double[] times) {
		this.times = times;
	}

	@Override
	public void setData(double[] data) {
		this.data = data;
	}

	@Override
	public final void setNodes(Pair<Date, Double>[] pairs) /* @ReadOnly */ {
		this.dates = new Date[pairs.length];
		this.data = new double[pairs.length];
	    for (int i=0; i<dates.length; ++i) {
	        dates[i] = pairs[i].getFirst();
	        data[i]  = pairs[i].getSecond();
	    }
	}

}
