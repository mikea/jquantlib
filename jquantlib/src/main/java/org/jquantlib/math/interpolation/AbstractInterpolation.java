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

import cern.colt.Arrays;
import cern.colt.Sorting;


public abstract class AbstractInterpolation implements Interpolation {

	//
	// abstract methods
	//
	// These methods are used by their counterparts, e.g:
	//   evaluateImpl is called by evaluate
	//   primitiveImpl is called by primitive
	//	 ... and so on.
	//
	protected abstract double evaluateImpl(final double x);
	protected abstract double primitiveImpl(final double x);
	protected abstract double derivativeImpl(final double x);
	protected abstract double secondDerivativeImpl(final double x);

	
	//
	// private fields
	//
	
	/**
	 * This private field is automatically initialized by constructor which
	 * picks up it's value from {@link Settings} singleton. This procedure
	 * caches values from the singleton, intending to avoid contention in
	 * heavily multi-threaded environments.
	 */
	//TODO: We should observe extraSafetyChecks
	private boolean extraSafetyChecks = Configuration.getSystemConfiguration(null).isExtraSafetyChecks();

	
	//
	// protected fields
	//
	
	/**
	 * @note Derived classes are responsible for initializing <i>vx</i> and <i>vy</i> 
	 */
	protected double[] vx;

	/**
	 * @note Derived classes are responsible for initializing <i>vx</i> and <i>vy</i> 
	 */
	protected double[] vy;
	
	
	//
	// public methods
	//
	
	@Override
	public final double getMinX() /* @ReadOnly */ {
		return  vx[0]; // get first element
	}

	@Override
	public final double getMaxX() /* @ReadOnly */ {
		return vx[vx.length-1]; // get last element
	}

	@Override
	public final double[] getValuesX() {
    	return Arrays.trimToCapacity(vx, vx.length);
    }
	
	@Override
	public final double[] getValuesY() {
    	return Arrays.trimToCapacity(vy, vy.length);
    }
	
	//
	// public final double evaluate(final double x) ::: is in a separate section
	//
	
	@Override
	public final double evaluate(final double x, boolean allowExtrapolation) {
        checkRange(x, allowExtrapolation);
		return evaluateImpl(x);
	}

	@Override
	public final double primitive(final double x) {
		return primitive(x, false);
	}
	
	@Override
	public final double primitive(final double x, boolean allowExtrapolation) {
        checkRange(x, allowExtrapolation);
		return primitiveImpl(x);
	}

	@Override
	public final double derivative(final double x) {
		return derivative(x, false);
	}
	
	@Override
	public final double derivative(final double x, boolean allowExtrapolation) {
        checkRange(x, allowExtrapolation);
		return derivativeImpl(x);
	}

	@Override
	public final double secondDerivative(final double x) {
		return secondDerivative(x, false);
	}
	
	@Override
	public final double secondDerivative(final double x, boolean allowExtrapolation) {
        checkRange(x, allowExtrapolation);
		return secondDerivativeImpl(x);
	}

	@Override
	public final boolean isInRange(final double x) {
        double x1 = getMinX(), x2 = getMaxX();
        return (x >= x1 && x <= x2) || isClose(x,x1) || isClose(x,x2);
    }

	
	//
	// protected methods
	//
	
	/**
	 * This method verifies if
	 * <li> extrapolation is enabled;</li>
	 * <li> requested <i>x</i> is valid</li>
	 * 
	 * @param x
	 * @param extrapolate
	 * 
	 * @throws IllegalStateException if extrapolation is not enabled.
	 * @throws IllegalArgumentException if <i>x</i> is our of range
	 */
	protected final void checkRange(final double x, boolean extrapolate) {
		if (! (extrapolate || allowsExtrapolation() || isInRange(x)) ) {
			StringBuilder sb = new StringBuilder();
			sb.append("interpolation range is [");
			sb.append(getMinX()).append(", ").append(getMaxX());
			sb.append("]: extrapolation at ");
			sb.append(x);
			sb.append(" not allowed");
			throw new IllegalArgumentException(sb.toString());
		}
	}

	// FIXME: code review here: compare against original C++ code
	protected int locate(double x) /* @ReadOnly */ {
        if (x <= vx[0])
            return 0;
        else if (x > vx[vx.length-1])
            return vx.length-2;
        else
        	return Sorting.binarySearchFromTo(vx, x, 0, vx.length-1)-1;
    }	


	//
	// implements Interpolation
	//
	
	/**
	 * @note Derived classes are responsible for initializing <i>vx</i> and <i>vy</i> 
	 */
	public void reload() {
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
	
	
	//
	// implements UnaryFunctionDouble
	//
	
	@Override
	public final double evaluate(final double x) {
		return evaluate(x, false);
	}

	
	//
	// implements Extrapolator
	//

	/**
	 * Implements multiple inheritance via delegate pattern to an inner class
	 * 
	 * @see Extrapolator
	 */
	private DefaultExtrapolator delegatedExtrapolator = new DefaultExtrapolator();
	
	@Override
	public final boolean allowsExtrapolation() {
		return delegatedExtrapolator.allowsExtrapolation();
	}

	@Override
	public void disableExtrapolation() {
		delegatedExtrapolator.disableExtrapolation();
	}

	@Override
	public void enableExtrapolation() {
		delegatedExtrapolator.enableExtrapolation();
	}

}
