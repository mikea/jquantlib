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
 Copyright (C) 2002, 2003 Ferdinando Ametrano

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
import org.jquantlib.number.Time;
import org.jquantlib.number.Volatility;
import org.jquantlib.termstructures.LocalVolTermStructure;
import org.jquantlib.util.Date;
import org.jscience.mathematics.number.Real;

// Local volatility curve derived from a Black curve

public class LocalVolCurve extends LocalVolTermStructure {

	private BlackVarianceCurve blackVarianceCurve_;

	public LocalVolCurve(final BlackVarianceCurve curve) {
		super(curve.getDayCounter());
		blackVarianceCurve_ = curve;
		blackVarianceCurve_.addObserver(this);
	}

	public final Date referenceDate() {
		return blackVarianceCurve_.getReferenceDate();
	}

	public final DayCounter dayCounter() {
		return blackVarianceCurve_.getDayCounter();
	}

	public final Date getMaxDate() {
		return blackVarianceCurve_.getMaxDate();
	}

	@Override
	public final Real getMinStrike() {
		return new Real(Double.NEGATIVE_INFINITY);
	}

	@Override
	public final Real getMaxStrike() {
		return new Real(Double.POSITIVE_INFINITY);
	}

	// FIXME: Visitor
	// inline void LocalVolCurve::accept(AcyclicVisitor& v) {
	// Visitor<LocalVolCurve>* v1 =
	// dynamic_cast<Visitor<LocalVolCurve>*>(&v);
	// if (v1 != 0)
	// v1->visit(*this);
	// else
	// LocalVolTermStructure::accept(v);
	// }

	/**
	 * The relation
	 {@latex[ \int_0^T \sigma_L^2(t)dt = \sigma_B^2 T }
	 * holds, where
	 {@latex$ \sigma_L(t) }
	 * is the local volatility at time {@latex$ t } and {@latex$ \sigma_B(T) }
	 * is the Black volatility for maturity {@latex$ T }.
	 * 
	 * <p>From the above, the formula
	 {@latex[ \sigma_L(t) = \sqrt{\frac{\mathrm{d}}{\mathrm{d}t}\sigma_B^2(t)t} }
	 * can be deduced which is here implemented.
	 */
	protected final Volatility localVolImpl(final Time maturity, final Real strike) {
		double m = maturity.doubleValue();
		double dt = 1.0 / 365.0;
		double var1 = blackVarianceCurve_.blackVariance(maturity, strike, true).doubleValue();
		double var2 = blackVarianceCurve_.blackVariance(new Time(m + dt), strike, true).doubleValue();
		double derivative = (var2 - var1) / dt;
		return new Volatility(Math.sqrt(derivative));
	}

}
