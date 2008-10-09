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
 Copyright (C) 2003 StatPro Italia srl

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

import it.unimi.dsi.fastutil.objects.ObjectArrays;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.math.interpolation.Interpolation;
import org.jquantlib.math.interpolation.Interpolator;
import org.jquantlib.math.interpolation.LinearInterpolation;
import org.jquantlib.termstructures.BlackVarianceTermStructure;
import org.jquantlib.termstructures.TermStructure;
import org.jquantlib.util.Date;
import org.jquantlib.util.TypedVisitor;
import org.jquantlib.util.Visitor;

/**
 * Black volatility curve modelled as variance curve
 * <p>
 * This class calculates time-dependent Black volatilities using as input a
 * vector of (ATM) Black volatilities observed in the market.
 * <p>
 * The calculation is performed interpolating on the variance curve. Linear
 * interpolation is used as default; this can be changed by the
 * setInterpolation() method.
 * <p>
 * For strike dependence, see BlackVarianceSurface.
 * 
 * @author Richard Gomes
 */
// TODO check time extrapolation
public class BlackVarianceCurve extends BlackVarianceTermStructure {

	//
	// private fields
	//
	
	private DayCounter dayCounter;
	private Date maxDate;
	private Date[] dates;
	private /*@Time*/ double[] times;
	private /*@Variance*/ double[] variances;
	private Interpolation varianceCurve;
	private Interpolator factory;

	
	//
	// public constructors
	//
	
	public BlackVarianceCurve(final Date referenceDate, final Date[] dates, /*@Volatility*/ double[] blackVolCurve, final DayCounter dayCounter) {
		this(referenceDate, dates, blackVolCurve, dayCounter, true);
	}

	public BlackVarianceCurve(
    			final Date referenceDate,
    			final Date[] dates,
    			final /*@Volatility*/ double[] blackVolCurve,
    			final DayCounter dayCounter,
                boolean forceMonotoneVariance) {
    	super(referenceDate);
    	this.dayCounter = dayCounter;
    	this.dates = dates.clone();
    	this.maxDate = dates[dates.length-1];
    	
    	if (! (this.dates.length==blackVolCurve.length) ) throw new IllegalArgumentException("mismatch between date vector and black vol vector");

    	// cannot have dates[0]==referenceDate, since the
    	// value of the volatility at dates[0] would be lost
    	// (variance at referenceDate must be zero)
    	if (this.dates[0].le(referenceDate)) throw new IllegalArgumentException("cannot have dates[0] <= referenceDate");

        variances = new /*@Variance*/ double[this.dates.length+1];
        times = new /*@Time*/ double [this.dates.length+1];
        variances[0] = 0.0;
        times[0] = 0.0;
        for (int j=1; j<=blackVolCurve.length; j++) {
            times[j] = timeFromReference(this.dates[j-1]);
            if (! (times[j]>times[j-1]) ) throw new IllegalArgumentException("dates must be sorted unique");
            variances[j] = times[j] * blackVolCurve[j-1]*blackVolCurve[j-1];
            if (! (variances[j]>=variances[j-1] || !forceMonotoneVariance) ) throw new IllegalArgumentException("variance must be non-decreasing");
        }

        // default: linear interpolation
    	factory = LinearInterpolation.getInterpolator();
    }

	//
	// public final methods
	//
	
	public final DayCounter dayCounter() {
		return dayCounter;
	}

	public final Date maxDate() {
		return maxDate;
	}

	public final /*@Price*/ double minStrike() {
		return Double.NEGATIVE_INFINITY;
	}

	public final /*@Price*/ double maxStrike() {
		return Double.POSITIVE_INFINITY;
	}

	public void setInterpolation() {
		this.setInterpolation(factory);
	}

	public final void setInterpolation(final Interpolator factory) {
		varianceCurve = factory.interpolate(times, variances);
		varianceCurve.enableExtrapolation();
		varianceCurve.reload();
		notifyObservers();
	}


	//
	// protected final methods
	//
	
	@Override
	protected final /*@Variance*/ double blackVarianceImpl(final /*@Time*/ double t, /*@Price*/ double maturity) {
		if (t <= times[times.length]) {
			return varianceCurve.evaluate(t);
		} else {
			// extrapolate with flat vol
			/*@Time*/ double lastTime = times[times.length];
			return varianceCurve.evaluate(lastTime) * t / lastTime;
		}
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
