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
 Copyright (C) 2000, 2001, 2002, 2003 RiskMap srl
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

package org.jquantlib.math.interpolation;

import static org.jquantlib.math.Closeness.isClose;

import org.jquantlib.Configuration;
import org.jquantlib.Settings;

import cern.colt.Sorting;



/**
 * 
 * @author Richard Gomes
 */
// FIXME: comments
public abstract class Interpolation implements Extrapolator {

	protected double[] vx;
	protected double[] vy;
	
	
	/**
	 * This private field is automatically initialized by constructor which
	 * picks up it's value from {@link Settings} singleton. This procedure
	 * caches values from the singleton, intending to avoid contention in
	 * heavily multi-threaded environments.
	 */
	private boolean extraSafetyChecks = Configuration.getInstance().isExtraSafetyChecks();

	protected Interpolation(final double[] x, final double[] y) {
		vx = x;
		vy = y;
		if (vx.length < 2)
			throw new IllegalArgumentException("not enough points to interpolate");
		if (extraSafetyChecks) {
			double x1 = vx[0];
			double x2;
			for (int i = 1; i < vx.length; i++) {
				x2 = vx[i];
				if (x1>x2) throw new IllegalArgumentException("unsorted values on array X");
				x1=x2;
			}
		}
	}

	/**
	 * This method does not have anything to do with Observer.update()
	 */
	public abstract void update();
	
	protected abstract double xMin();
	protected abstract double xMax();
	protected abstract double getValue(final double x);
	protected abstract double primitive(final double x);
	protected abstract double derivative(final double x);
	protected abstract double secondDerivative(final double x);

	public final double[] xValues() {
        return vx;
    }
	
	public final double[] yValues() {
        return vy;
    }

	public final double getValue(final double x, boolean allowExtrapolation) {
        checkRange(x, allowExtrapolation);
		return getValue(x);
	}

	protected final double primitive(final double x, boolean allowExtrapolation) {
        checkRange(x, allowExtrapolation);
		return primitive(x);
	}

	protected final double derivative(final double x, boolean allowExtrapolation) {
        checkRange(x, allowExtrapolation);
		return derivative(x);
	}

	protected final double secondDerivative(final double x, boolean allowExtrapolation) {
        checkRange(x, allowExtrapolation);
		return secondDerivative(x);
	}

	protected final boolean isInRange(final double x) {
        double x1 = xMin(), x2 = xMax();
        return (x >= x1 && x <= x2) || isClose(x,x1) || isClose(x,x2);
    }

	protected final void checkRange(final double x, boolean extrapolate) {
		if (! (extrapolate || allowsExtrapolation() || isInRange(x)) ) {
			StringBuilder sb = new StringBuilder();
			sb.append("interpolation range is [");
			sb.append(xMin()).append(", ").append(xMax());
			sb.append("]: extrapolation at ");
			sb.append(x);
			sb.append(" not allowed");
			throw new IllegalArgumentException(sb.toString());
		}
	}

	// FIXME: code review here: compare against original C++ code
	protected int locate(double x) /* @ReadOnly */ {
        if (x < vx[0])
            return 0;
        else if (x > vx[vx.length-1])
            return vx.length-2;
        else
            return Sorting.binarySearchFromTo(vx, x, 0, vx.length-1)-1;
    }	
	

	/**
	 * Implements multiple inheritance via delegate pattern to an inner class
	 * 
	 * @see Extrapolator
	 */
	private DefaultExtrapolator delegatedExtrapolator = new DefaultExtrapolator();
	
	public final boolean allowsExtrapolation() {
		return delegatedExtrapolator.allowsExtrapolation();
	}

	public void disableExtrapolation() {
		delegatedExtrapolator.disableExtrapolation();
	}

	public void enableExtrapolation() {
		delegatedExtrapolator.enableExtrapolation();
	}

}
