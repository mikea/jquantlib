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

package org.jquantlib.math.interpolation.factories;

import org.jquantlib.math.interpolation.BackwardFlatInterpolation;
import org.jquantlib.math.interpolation.Interpolation;
import org.jquantlib.math.interpolation.Interpolator;


/**
 * This class provides linear interpolation factory and traits
 * 
 * @author Richard Gomes
 */
public class BackwardFlat implements Interpolator {

	private Interpolator delegate;
	
	public BackwardFlat() {
		delegate = BackwardFlatInterpolation.getInterpolator();
	}
	
	//
	// implements Interpolator
	//
	
	public final Interpolation interpolate(final int size, final double[] x, final double[] y) /* @ReadOnly */ {
		return delegate.interpolate(x, y);
	}

	public final Interpolation interpolate(final double[] x, final double[] y) /* @ReadOnly */ {
		return delegate.interpolate(x, y);
	}

	public final boolean isGlobal() /* @ReadOnly */ {
		return delegate.isGlobal();
	}

}

