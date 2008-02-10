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
 Copyright (C) 2002, 2003, 2006 Ferdinando Ametrano
 Copyright (C) 2004, 2005, 2006, 2007 StatPro Italia srl

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

import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.vector.Matrix;
import org.jscience.mathematics.vector.Vector;

public abstract class Interpolation2D<T extends Real> implements Extrapolator {


	protected abstract T xMin();
	protected abstract T xMax();
	protected abstract T getValue(final T x, final T y);
	protected abstract T primitive(final T x, final T y);
	protected abstract T derivative(final T x, final T y);
	protected abstract T secondDerivative(final T x, final T y);

	
	protected Vector<T> vx;
	protected Vector<T> vy; // FIXME: code review
	protected Matrix mz; // FIXME: Define our own Matrix class, probably a delegate to http://ressim.berlios.de/doc/no/uib/cipr/matrix/Matrix.html
	

	protected Interpolation2D(final Vector<T> x, final Vector<T> y, final Matrix z) {
		this.vx = x;
		this.vy = y;
		if (this.vx.getDimension() < 2 || this.vy.size() < 2)
			throw new IllegalArgumentException("not enough points to interpolate");
		for (int i = 0; i < this.vx.getDimension()-1; i++) {
			if ( this.vx.get(i).isGT(this.vx.get(i + 1)) )
				throw new IllegalArgumentException("unsorted values on array X");
			if ( this.vy.get(i).isGT(this.vy.get(i + 1)) )
				throw new IllegalArgumentException("unsorted values on array Y");
		}
	}
	
    public final T getValue(final T x, final T y, boolean allowExtrapolation) {
        checkRange(x,y,allowExtrapolation);
        return getValue(x,y);
    }
    
    public final T xMin() {
        return impl_->xMin();
    }
    
    public final  T xMax() {
        return impl_->xMax();
    }
    
    public final  std::vector<Real> xValues() {
        return impl_->xValues();
    }
    
    public final  Size locateX(Real x) {
        return impl_->locateX(x);
    }
    
    public final  Real yMin() {
        return impl_->yMin();
    }
    
    public final  T yMax() {
        return impl_->yMax();
    }
    
    public final  std::vector<Real> yValues() {
        return impl_->yValues();
    }
    
    public final  Size locateY(Real y) {
        return impl_->locateY(y);
    }
    
    public final Matrix zData() {
        return impl_->zData();
    }
    
	protected final boolean isInRange(final MutableDouble x, final MutableDouble y) {

		T x1 = xMin();
		T x2 = xMax();
		if (! (x.isGT(x1) && x.isLE(x2)) ) return false;

		T y1 = yMin();
		T y2 = yMax();
		return (y.isGT(y1) && y.isLE(y2));
    }

    public void update() {
        impl_->calculate();
    }
    
  protected final void checkRange(final MutableDouble x, final MutableDouble y, boolean extrapolate) {
		if (!(extrapolate || allowsExtrapolation() || isInRange(x, y))) {
			StringBuilder sb = new StringBuilder();
			sb.append("interpolation range is [");
			sb.append(xMin()).append(", ").append(xMax());
			sb.append("] x [");
			sb.append(yMin()).append(", ").append(yMax());
			sb.append("]: extrapolation at (");
			sb.append(x).append(",").append(y);
			sb.append(") not allowed");
			throw new IllegalArgumentException(sb.toString());
		}
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
