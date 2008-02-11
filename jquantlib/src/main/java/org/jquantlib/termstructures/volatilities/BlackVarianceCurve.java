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

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.math.interpolation.Interpolation;
import org.jquantlib.math.interpolation.Interpolator;
import org.jquantlib.math.interpolation.Linear;
import org.jquantlib.termstructures.BlackVarianceTermStructure;
import org.jquantlib.util.Date;

// Black volatility curve modelled as variance curve

/**
 * This class calculates time-dependent Black volatilities using as input a
 * vector of (ATM) Black volatilities observed in the market.
 * 
 * The calculation is performed interpolating on the variance curve. Linear
 * interpolation is used as default; this can be changed by the
 * setInterpolation() method.
 * 
 * For strike dependence, see BlackVarianceSurface.
 * 
 * @todo check time extrapolation
 * 
 */
public class BlackVarianceCurve extends BlackVarianceTermStructure {

	private DayCounter dayCounter;
	private Date maxDate;
	private Date[] dates;
	private /*@Time*/ double[] times;
	private /*@Variance*/ double[] varianceCurve;
	private Interpolation variances;
	private Interpolator factory;

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
    	if (! (dates.length==blackVolCurve.length) ) throw new IllegalArgumentException("mismatch between date vector and black vol vector");
    	this.dayCounter = dayCounter;
    	this.dates = new FastTable<Date>(dates);
    	maxDate = this.dates.getLast();

    	// cannot have dates[0]==referenceDate, since the
    	// value of the vol at dates[0] would be lost
    	// (variance at referenceDate must be zero)
    	if (dates.get(0).le(referenceDate)) throw new IllegalArgumentException("cannot have dates[0] <= referenceDate");

    	this.varianceCurve = new FastTable<Real>();
    	double lastVariance = 0.0;
    	this.varianceCurve.add(Real.valueOf(lastVariance));
    	
    	this.times = new FastTable</*@Time*/ Double>();
    	/*@Time*/ double lastTime = 0.0;
    	this.times.add(lastTime);
    	
    	for (int i=0; i<blackVolCurve.size(); i++) {
    		/*@Time*/ double currTime = getTimeFromReference(dates.get(i));
    		if (currTime<=lastTime) throw new IllegalArgumentException("dates must be sorted unique");
    		this.times.add(currTime);
    		lastTime = currTime;
    		
    		double volCurve = blackVolCurve.get(i);
    		// var[i] = t[i] * (volCurve[i-1])^2;
    		double currVariance = currTime * Math.pow(volCurve,2);
    		if (currVariance<=lastVariance) throw new IllegalArgumentException("variance must be non-decreasing");
    		this.varianceCurve.add(Real.valueOf(currVariance));
    		lastVariance = currVariance;
    	}

    	factory = new Linear();
    }

	public final DayCounter dayCounter() {
		return dayCounter;
	}

	public final Date getMaxDate() {
		return maxDate;
	}

	public final /*@Price*/ double getMinStrike() {
		return Double.NEGATIVE_INFINITY;
	}

	public final /*@Price*/ double getMaxStrike() {
		return Double.POSITIVE_INFINITY;
	}

	public void setInterpolation() {
		this.setInterpolation(factory);
	}

	public void setInterpolation(final Interpolator factory) {
		varianceCurve = factory.interpolate(times, variances);
		variances.update();
		notifyObservers();
	}

	protected final /*@Variance*/ double blackVarianceImpl(final /*@Time*/ double t, Real maturity) {
		if (t <= times.getLast()) {
			return variances.getValue(t, true);
		} else {
			// extrapolate with flat vol
			return variances.getValue(times.getLast(), true) * t / times.getLast();
		}
	}

	// FIXME: Visitor pattern
	// inline void BlackVarianceCurve::accept(AcyclicVisitor& v) {
	// Visitor<BlackVarianceCurve>* v1 =
	// dynamic_cast<Visitor<BlackVarianceCurve>*>(&v);
	// if (v1 != 0)
	// v1->visit(*this);
	// else
	// BlackVarianceTermStructure::accept(v);
	// }

}
