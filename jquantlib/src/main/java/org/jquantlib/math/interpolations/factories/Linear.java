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

import org.jquantlib.math.interpolations.Interpolation;
import org.jquantlib.math.interpolations.Interpolator;
import org.jquantlib.math.interpolations.LinearInterpolation;


/**
 * This class provides linear interpolation factory and traits
 * 
 * @author Dominik Holenstein
 * @author Richard Gomes
 */
public class Linear implements Interpolator {

    //
    // private fields
    //
    
	private Interpolator delegate;
	
	
    //
    // public constructors
    //
    
	public Linear() {
		delegate = LinearInterpolation.getInterpolator();
	}
	
	
	//
	// implements Interpolator
	//
	
	@Override
	public final Interpolation interpolate(final int size, final double[] x, final double[] y) /* @ReadOnly */ {
		return delegate.interpolate(x, y);
	}

    @Override
	public final Interpolation interpolate(final double[] x, final double[] y) /* @ReadOnly */ {
		return delegate.interpolate(x, y);
	}

    @Override
	public final boolean isGlobal() /* @ReadOnly */ {
		return delegate.isGlobal();
	}
	
}

