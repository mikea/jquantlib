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

import java.util.List;

import javolution.util.FastTable;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.math.interpolation.Interpolation;
import org.jquantlib.math.interpolation.Interpolator;
import org.jquantlib.math.interpolation.Linear;
import org.jquantlib.number.Time;
import org.jquantlib.number.Volatility;
import org.jquantlib.termstructures.BlackVarianceTermStructure;
import org.jquantlib.util.Date;
import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.vector.Vector;

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

	private DayCounter dayCounter_;
	private Date maxDate_;
	private FastTable<Real> times_;
	private FastTable<Real> variances_; // FIXME: create the "Variance" named type
	private Interpolation<Real> varianceCurve_;
	private Interpolator<Real> factory;

	public BlackVarianceCurve(final Date referenceDate, final List<Date> dates, final List<Volatility> blackVolCurve, final DayCounter dayCounter) {
		this(referenceDate, dates, blackVolCurve, dayCounter, true);
	}

	public BlackVarianceCurve(
    			final Date referenceDate,
    			final List<Date> dates,
    			final List<Volatility> blackVolCurve,
    			final DayCounter dayCounter,
                boolean forceMonotoneVariance) {
    	super(referenceDate);
    	if (! (dates.size()==blackVolCurve.size()) ) throw new IllegalArgumentException("mismatch between date vector and black vol vector");
    	this.dayCounter_ = dayCounter;
    	maxDate_ = dates.getLast();

    	// cannot have dates[0]==referenceDate, since the
    	// value of the vol at dates[0] would be lost
    	// (variance at referenceDate must be zero)
    	if (dates.get(0).le(referenceDate)) throw new IllegalArgumentException("cannot have dates[0] <= referenceDate");

    	this.variances_ = new FastTable<Real>();
    	double lastVariance = 0.0;
    	this.variances_.add(Real.valueOf(lastVariance));
    	
    	this.times_ = new FastTable<Real>();
    	double lastTime = 0.0;
    	this.times_.add(Real.valueOf(lastTime));
    	
    	for (int i=0; i<blackVolCurve.size(); i++) {
    		double currTime = getTimeFromReference(dates.get(i)).doubleValue();
    		if (currTime<=lastTime) throw new IllegalArgumentException("dates must be sorted unique");
    		this.times_.add(Real.valueOf(currTime));
    		lastTime = currTime;
    		
    		double volCurve = blackVolCurve.get(i).doubleValue();
    		// var[i] = t[i] * (volCurve[i-1])^2;
    		double currVariance = currTime * Math.pow(volCurve,2);
    		if (currVariance<=lastVariance) throw new IllegalArgumentException("variance must be non-decreasing");
    		this.variances_.add(Real.valueOf(currVariance));
    		lastVariance = currVariance;
    	}

    	factory = new Linear();
    }

	public final DayCounter dayCounter() {
		return dayCounter_;
	}

	public final Date getMaxDate() {
		return maxDate_;
	}

	public final Real getMinStrike() {
		return new Real(Double.NEGATIVE_INFINITY);
	}

	public final Real getMaxStrike() {
		return new Real(Double.POSITIVE_INFINITY);
	}

	public void setInterpolation() {
		this.setInterpolation(factory);
	}

	public void setInterpolation(final Interpolator<Real> factory) {
		varianceCurve_ = factory.interpolate(times_, variances_);
		varianceCurve_.update();
		notifyObservers();
	}

	protected final Real blackVarianceImpl(final Time t, Real maturity) {
		if (t.doubleValue() <= times_.back().doubleValue()) {
			return varianceCurve_.getValue(t, true);
		} else {
			// extrapolate with flat vol
			return new Real( varianceCurve_.getValue(times_.back(), true).doubleValue() * t.doubleValue() / times_.back().doubleValue() );
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
