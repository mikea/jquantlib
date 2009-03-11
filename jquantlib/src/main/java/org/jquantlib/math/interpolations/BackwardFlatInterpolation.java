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

import java.util.Arrays;

import org.jquantlib.math.interpolations.factories.BackwardFlat;


/**
 * Backward-flat interpolation between discrete points
 * <p>
 * Interpolations are not instantiated directly by applications, but via a factory class.
 *
 * @see BackwardFlat
 * 
 * @author Richard Gomes
 */
public class BackwardFlatInterpolation extends AbstractInterpolation {

    //
    // private fields
    //
    
    private double[] vp;


    //
    // private constructors
    //
    
    /**
     * Constructor for a backward-flat interpolation between discrete points
     * <p>
     * Interpolations are not instantiated directly by applications, but via a factory class.
     * 
     * @see BackwardFlat
     */
    private BackwardFlatInterpolation() {
    	// access denied to default constructor
    }

    
    //
    // static public methods
    //
    
    /**
     * This is a factory method intended to create this interpolation.
     * 
     * @see BackwardFlat
     */
    static public Interpolator getInterpolator() /* @ReadOnly */ {
        BackwardFlatInterpolation backwardFlatInterpolation = new BackwardFlatInterpolation();
        return new BackwardFlarInterpolationImpl(backwardFlatInterpolation);
    }
    
    
    //
    // overrides AbstractInterpolation
    //
    
	@Override
	protected double primitiveImpl(final double x) /* @ReadOnly */ {
        int i = locate(x);
        double dx = x - vx[i];
        return vp[i] + dx*vy[i+1];
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
		
    	vp = new double[vx.length];
        vp[0] = 0.0;
        for (int i=1; i<vx.length; i++) {
            double dx = vx[i] - vx[i-1];
            vp[i] = vp[i-1] + dx*vy[i];
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
	 * This class is a default implementation for {@link BackwardFlatInterpolation} instances.
	 * 
	 * @author Richard Gomes
	 */
    
	private static class BackwardFlarInterpolationImpl implements Interpolator {
		private BackwardFlatInterpolation delegate;
		
		public BackwardFlarInterpolationImpl(final BackwardFlatInterpolation delegate) {
			this.delegate = delegate;
		}
		
		public final Interpolation interpolate(final double[] x, final double[] y) /* @ReadOnly */ {
			return interpolate(x.length, x, y);
		}

		public final Interpolation interpolate(final int size, final double[] x, final double[] y) /* @ReadOnly */ {
			delegate.vx = Arrays.copyOfRange(x, 0, size);
			delegate.vy = Arrays.copyOfRange(y, 0, size);
			delegate.reload();
			return delegate;
		}

		public final boolean isGlobal() {
			return false; // only CubicSpline and Sabr are global, whatever it means!
		}
	}

}
