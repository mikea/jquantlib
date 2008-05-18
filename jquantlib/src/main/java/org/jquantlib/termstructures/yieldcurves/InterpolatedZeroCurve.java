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
Copyright (C) 2003, 2004, 2005, 2006 StatPro Italia srl

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

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.math.interpolation.Interpolation;
import org.jquantlib.math.interpolation.Interpolator;
import org.jquantlib.math.interpolation.factories.BackwardFlat;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.calendars.Target;
import org.jquantlib.util.Date;
import org.jquantlib.util.Pair;

/**
 * Term structure based on interpolation of zero yields
 * 
 * @author Richard Gomes
 * @param <T>
 */
public final class InterpolatedZeroCurve<T extends Interpolator> extends ZeroYieldStructure implements YieldCurve {

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
	
    protected InterpolatedZeroCurve(final DayCounter dayCounter, final T interpolator) {
    	super(dayCounter);
		this.interpolator = (interpolator!=null) ? interpolator : new BackwardFlat();
    }

    protected InterpolatedZeroCurve(final Date referenceDate, final DayCounter dayCounter, final T interpolator) {
        super(referenceDate, Target.getCalendar(), dayCounter); // FIXME: code review : default calendar?
		this.interpolator = (interpolator!=null) ? interpolator : new BackwardFlat();
    }

    protected InterpolatedZeroCurve(final int settlementDays, final Calendar calendar, final DayCounter dayCounter, final T interpolator) {
        super(settlementDays,calendar, dayCounter);
		this.interpolator = (interpolator!=null) ? interpolator : new BackwardFlat();
    }

	
	//
	// implements YieldCurve
	//

	@Override
	public final Date[] getDates() /* @ReadOnly */{
		return dates;
	}

	@Override
	public final/* @DiscountFactor */double[] getData() /* @ReadOnly */{
		return data;
	}

	@Override
	public final Date getMaxDate() /* @ReadOnly */{
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
		return times;
	}

	@Override
	public final /*@Rate*/double zeroYieldImpl(/*@Time*/double t) /* @ReadOnly */{
        return interpolation.evaluate(t, true);
	}
	
}
