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

package org.jquantlib.math.interpolations.factories;

import org.jquantlib.lang.iterators.ConstIterator;
import org.jquantlib.math.interpolations.Interpolation;
import org.jquantlib.math.interpolations.Interpolator;
import org.jquantlib.math.interpolations.LogLinearInterpolation;


/**
 * This class provides log-linear interpolation factory and traits
 * <p>
 * This is not the implementation of a interpolation class, but only its factory.
 *
 * @see LogLinearInterpolation
 *
 * @author Dominik Holenstein
 * @author Richard Gomes
 */
public class LogLinear implements Interpolator {

    //
    // private final fields
    //

    private final Interpolator delegate;


    //
    // public constructors
    //

    /**
     * Constructs a interpolation factory.
     * <p>
     * This is not the implementation of a interpolation class, but only its factory.
     *
     * @see LogLinearInterpolation
     */
	public LogLinear () {
		delegate = LogLinearInterpolation.getInterpolator();
	}


	//
	// implements Interpolator
	//

    @Override
	public final Interpolation interpolate(final int size, final ConstIterator x, final ConstIterator y) /* @ReadOnly */ {
		return delegate.interpolate(x, y);
	}

    @Override
	public final Interpolation interpolate(final ConstIterator x, final ConstIterator y) /* @ReadOnly */ {
		return delegate.interpolate(x, y);
	}

    @Override
	public final boolean global() /* @ReadOnly */ {
		return delegate.global();
	}

}
