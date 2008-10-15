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

package org.jquantlib.math.interpolations;

import it.unimi.dsi.fastutil.doubles.DoubleArrays;


/**
 * Backward-flat interpolation between discrete points
 * 
 * @author Richard Gomes
 */
public class BackwardFlatInterpolation extends AbstractInterpolation {

    //
    // private fields
    //
    
    private double[] primitive;


    //
    // private constructors
    //
    
    private BackwardFlatInterpolation() {
    	// access denied to default constructor
    }

    
    //
    // static public methods
    //
    
    static public Interpolator getInterpolator() /* @ReadOnly */ {
        BackwardFlatInterpolation backwardFlatInterpolation = new BackwardFlatInterpolation();
        return backwardFlatInterpolation. new BackwardFlarInterpolationImpl(backwardFlatInterpolation);
    }
    
    
    //
    // overrides AbstractInterpolation
    //
    
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
     * {@inheritDoc}
     */
    @Deprecated
    @Override
	public void update() { reload(); }

    /**
     * {@inheritDoc}
     * 
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
    // implements UnaryFunctionDouble
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
    // private inner classes
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
			delegate.vx = DoubleArrays.copy(x, 0, size);
			delegate.vy = DoubleArrays.copy(y, 0, size);
			delegate.reload();
			return delegate;
		}

		public final boolean isGlobal() {
			return false; // only CubicSpline and Sabr are global, whatever it means!
		}
	}

}
