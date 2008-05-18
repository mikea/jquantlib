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
 Copyright (C) 2003 Ferdinando Ametrano

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
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.termstructures.BlackVolTermStructure;
import org.jquantlib.termstructures.LocalVolTermStructure;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.util.Date;

/**
 * For details about this implementation refer to "Stochastic Volatility and
 * Local Volatility," in "Case Studies and Financial Modelling Course Notes," by
 * Jim Gatheral, Fall Term, 2003
 * 
 * @see <a
 *      href="http://www.math.nyu.edu/fellows_fin_math/gatheral/Lecture1_Fall02.pdf">This
 *      article</a>
 */
// TODO: this class is untested, probably unreliable.
public class LocalVolSurface extends LocalVolTermStructure {

	private Handle<BlackVolTermStructure> blackTS_;
	private Handle<YieldTermStructure> riskFreeTS_;
	private Handle<YieldTermStructure> dividendTS_;
	private Handle<? extends Quote> underlying_;

	public LocalVolSurface(
			final Handle<BlackVolTermStructure> blackTS, 
			final Handle<YieldTermStructure> riskFreeTS, 
			final Handle<YieldTermStructure> dividendTS, 
			final Handle<? extends Quote> underlying) {
		super(blackTS.getLink().getDayCounter());
		this.blackTS_ = blackTS;
		this.riskFreeTS_ = riskFreeTS;
		this.dividendTS_ = dividendTS;
		this.underlying_ = underlying;
		this.blackTS_.addObserver(this);
		this.riskFreeTS_.addObserver(this);
		this.dividendTS_.addObserver(this);
		this.underlying_.addObserver(this);
	}

	public LocalVolSurface(
			final Handle<BlackVolTermStructure> blackTS, 
			final Handle<YieldTermStructure> riskFreeTS, 
			final Handle<YieldTermStructure> dividendTS, 
			final /*@Price*/ double underlying) {
		super(blackTS.getLink().getDayCounter());
		this.blackTS_ = blackTS;
		this.riskFreeTS_ = riskFreeTS;
		this.dividendTS_ = dividendTS;
		this.underlying_ = new Handle<Quote>(new SimpleQuote(underlying));
		this.blackTS_.addObserver(this);
		this.riskFreeTS_.addObserver(this);
		this.dividendTS_.addObserver(this);
	}

	public final Date getReferenceDate() {
		return this.blackTS_.getLink().getReferenceDate();
	}

	public final DayCounter getDayCounter() {
		return this.blackTS_.getLink().getDayCounter();
	}

	public final Date getMaxDate() {
		return blackTS_.getLink().getMaxDate();
	}

	public final /*@Price*/ double getMinStrike() {
		return blackTS_.getLink().getMinStrike();
	}

	public final /*@Price*/ double getMaxStrike() {
		return blackTS_.getLink().getMaxStrike();
	}

	// void LocalVolSurface::accept(AcyclicVisitor& v) {
	// Visitor<LocalVolSurface>* v1 =
	// dynamic_cast<Visitor<LocalVolSurface>*>(&v);
	// if (v1 != 0)
	// v1->visit(*this);
	// else
	// LocalVolTermStructure::accept(v);
	// }

	protected final /*@Volatility*/ double localVolImpl(final /*@Time*/ double time, final /*@Price*/ double underlyingLevel) {

		// obtain local copies of objects
		Quote u = underlying_.getLink();
		YieldTermStructure dTS = dividendTS_.getLink();
		YieldTermStructure rTS = riskFreeTS_.getLink();
		BlackVolTermStructure bTS = blackTS_.getLink();
		
		double forwardValue = u.doubleValue() * ( dTS.getDiscount(time, true) / rTS.getDiscount(time, true) );

		// strike derivatives
		/*@Price*/ double strike;
		/*@Price*/ double strikem;
		/*@Price*/ double strikep;
		double y, dy;
		double w, wp, wm, dwdy, d2wdy2;
		strike = underlyingLevel;
		y = Math.log(strike / forwardValue);
		dy = ((y != 0.0) ? y * 0.000001 : 0.000001);
		strikep = strike * Math.exp(dy);
		strikem = strike / Math.exp(dy);
		w = bTS.blackVariance(time,  strike, true);
		wp = bTS.blackVariance(time, strikep, true);
		wm = bTS.blackVariance(time, strikem, true);
		dwdy = (wp - wm) / (2.0 * dy);
		d2wdy2 = (wp - 2.0 * w + wm) / (dy * dy);

		// time derivative
		/*@Time*/ double t = time;
		/*@Time*/ double dt;
		double wpt, wmt, dwdt;
		if (t == 0.0) {
			dt = 0.0001;
			wpt = bTS.blackVariance(/*@Time*/ (t + dt), strike, true);
			if (wpt < w)
				throw new ArithmeticException("decreasing variance at strike " + strike + " between time " + t + " and time " + t + dt);

			dwdt = (wpt - w) / dt;
		} else {
			dt = Math.min(0.0001, t / 2.0);
			wpt = bTS.blackVariance(/*@Time*/ (t + dt), strike, true);
			wmt = bTS.blackVariance(/*@Time*/ (t - dt), strike, true);
			if (wpt < w)
				throw new ArithmeticException("decreasing variance at strike " + strike + " between time " + t + " and time " + t + dt);
			if (w < wmt)
				throw new ArithmeticException("decreasing variance at strike " + strike + " between time " + (t - dt) + " and time " + t);
			dwdt = (wpt - wmt) / (2.0 * dt);
		}

		if (dwdy == 0.0 && d2wdy2 == 0.0) { // avoid /w where w might be 0.0
			return Math.sqrt(dwdt);
		} else {
			double den1 = 1.0 - y / w * dwdy;
			double den2 = 0.25 * (-0.25 - 1.0 / w + y * y / w / w) * dwdy * dwdy;
			double den3 = 0.5 * d2wdy2;
			double den = den1 + den2 + den3;
			double result = dwdt / den;
			if (result < 0.0)
				throw new ArithmeticException("negative local vol^2 at strike " + strike + " and time " + t + "; the black vol surface is not smooth enough");
			return Math.sqrt(result);

			// commented out at original source QuantLib
			// return std::sqrt(dwdt / (1.0 - y/w*dwdy +
			// 0.25*(-0.25 - 1.0/w + y*y/w/w)*dwdy*dwdy + 0.5*d2wdy2));
		}
	}

}
