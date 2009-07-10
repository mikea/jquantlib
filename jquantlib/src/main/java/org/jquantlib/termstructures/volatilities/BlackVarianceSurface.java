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
import org.jquantlib.math.Array;
import org.jquantlib.math.Matrix;
import org.jquantlib.math.interpolations.BilinearInterpolation;
import org.jquantlib.math.interpolations.Interpolation2D;
import org.jquantlib.math.interpolations.Interpolator;
import org.jquantlib.math.interpolations.Interpolator2D;
import org.jquantlib.termstructures.BlackVarianceTermStructure;
import org.jquantlib.termstructures.TermStructure;
import org.jquantlib.util.Date;
import org.jquantlib.util.TypedVisitor;
import org.jquantlib.util.Visitor;

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
public class BlackVarianceSurface extends BlackVarianceTermStructure {

	public enum Extrapolation {
		ConstantExtrapolation, InterpolatorDefaultExtrapolation
	};


	//
	// private fields
	//
	
	private DayCounter dayCounter;
	private Date maxDate;
	private /* @Time */ Array times;
	private /* @Price */ Array strikes;
	private /* @Variance */ Matrix variances;
	private Interpolation2D varianceSurface;
	private Extrapolation lowerExtrapolation;
	private Extrapolation upperExtrapolation;
	private Interpolator2D factory;

	
	//
	// public constructors
	//
	
	public BlackVarianceSurface(final Date referenceDate, final Date[] dates, 
	        final/* @Price */ Array strikes, final/* @Volatility */ Matrix blackVolMatrix, final DayCounter dayCounter) {
	    
		this(referenceDate, dates, strikes, blackVolMatrix, dayCounter, 
		        Extrapolation.InterpolatorDefaultExtrapolation,
				Extrapolation.InterpolatorDefaultExtrapolation);
	}

	public BlackVarianceSurface(final Date referenceDate, final Date[] dates, 
	        final/* @Price */ Array strikes, final/* @Volatility */ Matrix blackVolMatrix, final DayCounter dayCounter, 
			final Extrapolation lowerExtrapolation, final Extrapolation upperExtrapolation) {
	    
		super(referenceDate);
		this.dayCounter = dayCounter;
		this.maxDate = dates[dates.length-1]; // TODO: code review: index seems to be wrong
		// TODO: code review :: use of clone()
		this.strikes = strikes.clone();
		this.lowerExtrapolation = lowerExtrapolation;
		this.upperExtrapolation = upperExtrapolation;

		// TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
		if ((dates.length != blackVolMatrix.cols))
			throw new IllegalArgumentException("mismatch between date vector and vol matrix colums");
		if ((strikes.length != blackVolMatrix.rows))
			throw new IllegalArgumentException("mismatch between money-strike vector and vol matrix rows");
		if ((dates[0].le(referenceDate)))
			throw new IllegalArgumentException("cannot have dates[0] <= referenceDate");

		this.times = new Array(dates.length+1); // TODO: verify if length is correct
		this.times.set(0, 0.0);
		
		this.variances = new Matrix(strikes.length, dates.length+1); // TODO: verify if length is correct
		this.variances.fill(0.0);
		
		this.strikes = new Array(strikes.length+1); // TODO: verify if length is correct
		this.strikes.set(0, 0.0);
		for(int i = 1; i < strikes.length+1; i++){
			this.strikes.set(i, strikes.get(i-1));
		}
		
		for (int j = 1; j <= blackVolMatrix.cols; j++) {
			times.set(j, timeFromReference(dates[j-1]));
			if (!(times.get(j) > times.get(j-1)))
				throw new IllegalArgumentException("dates must be sorted unique!");
			for (int i = 0; i < blackVolMatrix.rows; i++) {
			    double elem = blackVolMatrix.get(i, j-1);
			    double ijvar = times.get(j) * elem * elem;
				variances.set(i, j, ijvar); 
				if (!(ijvar >= variances.get(i, j-1)))
					throw new IllegalArgumentException("variance must be non-decreasing");
			}
		}
		// default: bilinear interpolation
		factory = BilinearInterpolation.getInterpolator();
	}


	//
	// public methods
	//
	
	public void setInterpolation(final Interpolator i) {
        varianceSurface = factory.interpolate(times, strikes, variances);
        varianceSurface.enableExtrapolation();
        varianceSurface.reload();
        notifyObservers();
    }

	
	//
	// Overrides TermStructure
	//
	
	@Override
	public final DayCounter dayCounter() {
		return dayCounter;
	}

    @Override
	public final Date maxDate() {
		return maxDate;
	}


    //
    // Overrides BlackVolTermStructure
    //
    
    @Override
	public final /* @Price */ double minStrike() {
		return strikes.first();
	}

    @Override
	public final /* @Price */ double maxStrike() {
		return strikes.last();
	}

    @Override
	protected final/* @Variance */double blackVarianceImpl(/* @Time */double t, /* @Price */double strike) /* @ReadOnly */{

		if (t == 0.0)
			return 0.0;

		// enforce constant extrapolation when required
		if (strike < strikes.first() && lowerExtrapolation == Extrapolation.ConstantExtrapolation) strike = strikes.first();
		if (strike > strikes.last()  && upperExtrapolation == Extrapolation.ConstantExtrapolation) strike = strikes.last();

		if (t <= times.last())
			return varianceSurface.evaluate(t, strike);
		else {
// FIXME: verify against QuantLib
			// t>times_.back() || extrapolate
			/* @Time */double lastTime = times.last();
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
