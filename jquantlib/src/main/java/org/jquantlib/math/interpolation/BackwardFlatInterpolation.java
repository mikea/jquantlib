/*
 Copyright (C) 2008 Richard Gomes

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

package org.jquantlib.math.interpolation;

import cern.colt.Arrays;


/**
 * Backward-flat interpolation between discrete points
 * 
 * @author Richard Gomes
 */
public class BackwardFlatInterpolation extends AbstractInterpolation {

    private double[] primitive;

    /**
     * Private default constructor.
     * 
	 * @note Class factory is responsible for initializing <i>vx</i> and <i>vy</i>
	 * 
	 * @author Richard Gomes
     */
    private BackwardFlatInterpolation() {
    	// access denied to default constructor
    }
    
	@Override
	protected double primitiveImpl(final double x) /* @ReadOnly */ {
        int i = locate(x);
        double dx = x - vx[i];
        return primitive[i] + dx*vy[i+1];
	}

	@Override
	protected double derivativeImpl(final double x) /* @ReadOnly */ {
        return 0.0;
	}

	@Override
	protected double secondDerivativeImpl(final double x) /* @ReadOnly */ {
        return 0.0;
	}

	
    //
    // implements Interpolation
    //
    
	/**
	 * This method must be avoided due to confusion with <code>Observer.update(org.jquantlib.util.Observable, Object)</code>
	 * <p>
	 * Use <code>reload()</code> instead.
	 */
	@Deprecated
	public void update() { reload(); }

	
	/**
	 * @note Class factory is responsible for initializing <i>vx</i> and <i>vy</i>  
	 */
	@Override
	public void reload() {
    	super.reload();
		
    	primitive = new double[vx.length];
        primitive[0] = 0.0;
        for (int i=1; i<vx.length; i++) {
            double dx = vx[i] - vx[i-1];
            primitive[i] = primitive[i-1] + dx*vy[i];
        }
	}
	


    //
    // concrete implementation of UnaryFunctionDouble.evaluate
    //
    
    protected double evaluateImpl(final double x) /* @ReadOnly */ {
    	if (x <= vx[0])
            return vy[0];
    	int i = locate(x);
        if (x == vx[i])
            return vy[i];
        else
            return vy[i+1];
	}

    //
    // static methods
    //
    
    static public Interpolator getInterpolator() /* @ReadOnly */ {
    	BackwardFlatInterpolation backwardFlatInterpolation = new BackwardFlatInterpolation();
		return backwardFlatInterpolation. new BackwardFlarInterpolationImpl(backwardFlatInterpolation);
    }
    
    
    //
    // inner classes
    //
    
    /**
	 * This class is a factory for LinearInterpolation instances.
	 * 
	 * @author Richard Gomes
	 */
    
	private class BackwardFlarInterpolationImpl implements Interpolator {
		private BackwardFlatInterpolation delegate;
		
		public BackwardFlarInterpolationImpl(final BackwardFlatInterpolation delegate) {
			this.delegate = delegate;
		}
		
		public final Interpolation interpolate(final double[] x, final double[] y) /* @ReadOnly */ {
			return interpolate(x.length, x, y);
		}

		public final Interpolation interpolate(final int size, final double[] x, final double[] y) /* @ReadOnly */ {
			delegate.vx = Arrays.trimToCapacity(x, size);
			delegate.vy = Arrays.trimToCapacity(y, size);
			delegate.reload();
			return delegate;
		}

		public final boolean isGlobal() {
			return false; // only CubicSpline and Sabr are global, whatever it means!
		}
	}

}
