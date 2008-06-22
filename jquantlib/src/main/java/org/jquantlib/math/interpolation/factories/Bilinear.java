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

package org.jquantlib.math.interpolation.factories;

import org.jquantlib.math.interpolation.BilinearInterpolation;
import org.jquantlib.math.interpolation.Interpolation2D;
import org.jquantlib.math.interpolation.Interpolator2D;

/**
 * This class is provided for backwards compatibility only.
 * 
 * <p>
 * Use BilinearInterpolation.Factory instead
 * 
 * @see BilinearInterpolation.Factory
 * 
 * @author Richard Gomes
 */
public class Bilinear implements Interpolator2D {

	private Interpolator2D delegate;
	
	public Bilinear() {
		delegate = BilinearInterpolation.getInterpolator();
	}
	
	public Interpolation2D interpolate(final double[] x, final double[] y, final double[][] z) {
		return delegate.interpolate(x, y, z);
	}

}
