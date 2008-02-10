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

package org.jquantlib.termstructures;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.number.Time;
import org.jquantlib.number.Volatility;
import org.jquantlib.time.Calendar;
import org.jquantlib.util.Date;
import org.jscience.mathematics.number.Real;

/**
 * Black-volatility term structure
 * 
 * <p>
 * This abstract class defines the interface of concrete Black-volatility term
 * structures which will be derived from this one.
 * 
 * <p>
 * Volatilities are assumed to be expressed on an annual basis.
 * 
 * @author Richard Gomes
 */
// FIXME: pending review
public abstract class BlackVolTermStructure extends TermStructure {

	static private final double dT = 1.0/365.0;

	/**
	 * The minimum strike for which the term structure can return vols
	 */
	public abstract Real getMinStrike();

	/**
	 * The maximum strike for which the term structure can return vols
	 */
	public abstract Real getMaxStrike();

	protected abstract Volatility blackVolImpl(final Time maturity, final Real strike);

	protected abstract Real blackVarianceImpl(final Time maturity, final Real strike);

	
	
	// Constructors

	public BlackVolTermStructure(final DayCounter dc) {
		super(dc);
	}

	public BlackVolTermStructure(final Date refDate, final Calendar cal, final DayCounter dc) {
		super(refDate, cal, dc);
	}

	public BlackVolTermStructure(int settlDays, final Calendar cal, final DayCounter dc) {
		super(settlDays, cal, dc);
	}

	/**
	 * Present (a.k.a spot) volatility
	 */
	public final Volatility blackVol(final Date maturity, final Real strike) {
		return blackVol(maturity, strike, false);
	}

	/**
	 * Present (a.k.a spot) volatility
	 */
	public final Volatility blackVol(final Date maturity, final Real strike, boolean extrapolate) {
		Time t = super.getTimeFromReference(maturity);
		checkRange(t, strike, extrapolate);
		return blackVolImpl(t, strike);
	}

	/**
	 * Present (a.k.a spot) volatility
	 */
	public final Volatility blackVol(final Time maturity, final Real strike) {
		return blackVol(maturity, strike, false);
	}

	/**
	 * Present (a.k.a spot) volatility
	 */
	public final Volatility blackVol(final Time maturity, final Real strike, boolean extrapolate) {
		checkRange(maturity, strike, extrapolate);
		return blackVolImpl(maturity, strike);
	}

	/**
	 * Present (a.k.a spot) variance
	 */
	public final Real blackVariance(final Date maturity, final Real strike) {
		return blackVariance(maturity, strike, false);
	}

	/**
	 * Present (a.k.a spot) variance
	 */
	public final Real blackVariance(final Date maturity, final Real strike, boolean extrapolate) {
		Time t = super.getTimeFromReference(maturity);
		checkRange(t, strike, extrapolate);
		return blackVarianceImpl(t, strike);
	}

	/**
	 * Present (a.k.a spot) variance
	 */
	public final Real blackVariance(final Time maturity, final Real strike) {
		return blackVariance(maturity, strike, false);
	}

	/**
	 * Present (a.k.a spot) variance
	 */
	public final Real blackVariance(final Time maturity, final Real strike, boolean extrapolate) {
		checkRange(maturity, strike, extrapolate);
		return blackVarianceImpl(maturity, strike);
	}

// XXX	
//	protected void accept(final ElementVisitor<BlackVolTermStructure> v) {
//		if (v != null) {
//			v.visit(this);
//		} else {
//			throw new IllegalArgumentException("not a Black-volatility term structure visitor");
//		}
//	}

	private final void checkRange(final Time time, final Real strike, boolean extrapolate) {
		super.checkRange(time, extrapolate);
		double k = strike.doubleValue();
		if (!(extrapolate || allowsExtrapolation() || (k >= getMinStrike().doubleValue() && k <= getMaxStrike().doubleValue())))
			throw new ArithmeticException("strike (" + k + ") is outside the curve domain [" + getMinStrike() + "," + getMaxStrike() + "]");
	}

	/**
	 * Future (a.k.a. forward) volatility
	 * 
	 * @param date1
	 * @param date2
	 * @param strike
	 * @param extrapolate
	 * @return
	 */
	public final Volatility blackForwardVol(final Date date1, final Date date2, final Real strike, boolean extrapolate) {
		if (date1.gt(date2))
			throw new IllegalArgumentException(date1 + " later than " + date2);
		Time time1 = getTimeFromReference(date1);
		Time time2 = getTimeFromReference(date2);
		return blackForwardVol(time1, time2, strike, extrapolate);
	}

	/**
	 * Future (a.k.a. forward) volatility
	 * 
	 * @param time1
	 * @param time2
	 * @param strike
	 * @param extrapolate
	 * @return
	 */
	public final Volatility blackForwardVol(final Time time1, final Time time2, final Real strike, boolean extrapolate) {
		double t1 = time1.doubleValue();
		double t2 = time2.doubleValue();
		if (t1>t2) throw new IllegalArgumentException(time1 + " later than " + time2);
		checkRange(time2, strike, extrapolate);
		
		if (t1==t2) {
			if (t1==0.0) {
				double epsilon = 1.0e-5;
				double var = blackVarianceImpl(new Time(epsilon), strike).doubleValue();
				return new Volatility(Math.sqrt(var/epsilon));
			} else {
				double epsilon = Math.min(1.0e-5, t1);
				double var1 = blackVarianceImpl(new Time(t1-epsilon), strike).doubleValue();
				double var2 = blackVarianceImpl(new Time(t1+epsilon), strike).doubleValue();
				if (var2<var1) throw new ArithmeticException("variances must be non-decreasing");
				return new Volatility( Math.sqrt((var2-var1) / (2*epsilon)) );
			}
		} else {
			double var1 = blackVarianceImpl(time1, strike).doubleValue();
			double var2 = blackVarianceImpl(time2, strike).doubleValue();
			if (var2<var1) throw new ArithmeticException("variances must be non-decreasing");
			return new Volatility( Math.sqrt((var2-var1)/(t2-t1)) );
		}
	}

	/**
	 * Future (a.k.a. forward) variance
	 * 
	 * @param date1
	 * @param date2
	 * @param strike
	 * @param extrapolate
	 * @return
	 */
	public final Real blackForwardVariance(final Date date1, final Date date2, final Real strike, boolean extrapolate) {
		if (date1.gt(date2))
			throw new IllegalArgumentException(date1 + " later than " + date2);
		Time time1 = getTimeFromReference(date1);
		Time time2 = getTimeFromReference(date2);
		return blackForwardVariance(time1, time2, strike, extrapolate);
	}

	/**
	 * Future (a.k.a. forward) variance
	 * 
	 * @param time1
	 * @param time2
	 * @param strike
	 * @param extrapolate
	 * @return
	 */
	public final Real blackForwardVariance(final Time time1, final Time time2, final Real strike, boolean extrapolate) {
		double t1 = time1.doubleValue();
		double t2 = time2.doubleValue();
		if (t1>t2) throw new IllegalArgumentException(time1 + " later than " + time2);
		checkRange(time2, strike, extrapolate);

		double v1 = blackVarianceImpl(time1, strike).doubleValue();
		double v2 = blackVarianceImpl(time2, strike).doubleValue();
		if (v2<v1) throw new ArithmeticException("variances must be non-decreasing");
		return new Real( v2-v1 );
	}

}
