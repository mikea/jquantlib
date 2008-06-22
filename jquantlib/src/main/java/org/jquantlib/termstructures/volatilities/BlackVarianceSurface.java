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
 Copyright (C) 2003, 2004 StatPro Italia srl

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
import org.jquantlib.math.interpolation.BilinearInterpolation;
import org.jquantlib.math.interpolation.Interpolation2D;
import org.jquantlib.math.interpolation.Interpolator;
import org.jquantlib.math.interpolation.Interpolator2D;
import org.jquantlib.termstructures.BlackVarianceTermStructure;
import org.jquantlib.termstructures.TermStructure;
import org.jquantlib.util.Date;
import org.jquantlib.util.TypedVisitor;
import org.jquantlib.util.Visitor;

import cern.colt.Arrays;

/**
 * This class calculates time/strike dependent Black volatilities using as input
 * a matrix of Black volatilities observed in the market.
 * 
 * The calculation is performed interpolating on the variance surface. Bilinear
 * interpolation is used as default; this can be changed by the
 * setInterpolation() method.
 * 
 * @author Richard Gomes
 */
// TODO: check time extrapolation
// CODE REVIEW DONE by Richard Gomes
public class BlackVarianceSurface extends BlackVarianceTermStructure {

	public enum Extrapolation {
		ConstantExtrapolation, InterpolatorDefaultExtrapolation
	};

	private DayCounter dayCounter;
	private Date maxDate;
	private/* @Time */double[] times;
	private/* @Price */double[] strikes;
	private/* @Variance */double[][] variances;
	private Interpolation2D varianceSurface;
	private Extrapolation lowerExtrapolation;
	private Extrapolation upperExtrapolation;
	private Interpolator2D factory;

	public BlackVarianceSurface(final Date referenceDate, final Date[] dates, final/* @Price */double[] strikes,
			final/* @Volatility */double[][] blackVolMatrix, final DayCounter dayCounter) {
		this(referenceDate, dates, strikes, blackVolMatrix, dayCounter, Extrapolation.InterpolatorDefaultExtrapolation,
				Extrapolation.InterpolatorDefaultExtrapolation);
	}

	public BlackVarianceSurface(final Date referenceDate, final Date[] dates, final/* @Price */double[] strikes,
			final/* @Volatility */double[][] blackVolMatrix, final DayCounter dayCounter, 
			final Extrapolation lowerExtrapolation, final Extrapolation upperExtrapolation) {
		super(referenceDate);

		this.dayCounter = dayCounter;
		this.maxDate = dates[dates.length];
		this.strikes = Arrays.trimToCapacity(strikes, strikes.length);
		this.lowerExtrapolation = lowerExtrapolation;
		this.upperExtrapolation = upperExtrapolation;

		if ((dates.length != blackVolMatrix[0].length))
			throw new IllegalArgumentException("mismatch between date vector and vol matrix colums");
		if ((strikes.length != blackVolMatrix.length))
			throw new IllegalArgumentException("mismatch between money-strike vector and vol matrix rows");
		if ((dates[0].le(referenceDate)))
			throw new IllegalArgumentException("cannot have dates[0] <= referenceDate");

		this.times = new /* @Time */double[dates.length + 1];
		this.times[0] = 0.0;
		this.variances = new /* @Variance */double[strikes.length][dates.length + 1];
		for (int i = 0; i < blackVolMatrix.length; i++) {
			variances[i][0] = 0.0;
		}
		for (int j = 1; j <= blackVolMatrix[0].length; j++) {
			times[j] = getTimeFromReference(dates[j - 1]);
			if (!(times[j] > times[j - 1]))
				throw new IllegalArgumentException("dates must be sorted unique!");
			for (int i = 0; i < blackVolMatrix.length; i++) {
				variances[i][j] = times[j] * blackVolMatrix[i][j - 1] * blackVolMatrix[i][j - 1];
				if (!(variances[i][j] >= variances[i][j - 1]))
					throw new IllegalArgumentException("variance must be non-decreasing");
			}
		}
		// default: bilinear interpolation
		factory = BilinearInterpolation.getInterpolator();
	}

	public final DayCounter dayCounter() {
		return dayCounter;
	}

	public final Date getMaxDate() {
		return maxDate;
	}

	public final/* @Price */double getMinStrike() {
		return strikes[0];
	}

	public final/* @Price */double getMaxStrike() {
		return strikes[strikes.length - 1];
	}

	public void setInterpolation(final Interpolator i) {
		varianceSurface = factory.interpolate(times, strikes, variances);
		varianceSurface.enableExtrapolation();
		varianceSurface.reload();
		notifyObservers();
	}

	protected final/* @Variance */double blackVarianceImpl(/* @Time */double t, /* @Price */double strike) /* @ReadOnly */{

		if (t == 0.0)
			return 0.0;

		// enforce constant extrapolation when required
		if (strike < strikes[0] && lowerExtrapolation == Extrapolation.ConstantExtrapolation)
			strike = strikes[0];
		if (strike > strikes[strikes.length - 1] && upperExtrapolation == Extrapolation.ConstantExtrapolation)
			strike = strikes[strikes.length - 1];

		if (t <= times[times.length - 1])
			return varianceSurface.evaluate(t, strike);
		else {
// FIXME: verify against QuantLib
			// t>times_.back() || extrapolate
			/* @Time */double lastTime = times[times.length - 1];
			return varianceSurface.evaluate(lastTime, strike) * t / lastTime;
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
