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

package org.jquantlib.math.interpolation;

import java.util.Arrays;



/**
 * This class provides linear interpolation between discrete points
 * 
 * @author Dominik Holenstein
 * @author Richard Gomes
 */
public class LinearInterpolation extends AbstractInterpolation {

    private double[] vp;
    private double[] vs;

    /**
     * Private default constructor.
     * 
	 * @note Class factory is responsible for initializing <i>vx</i> and <i>vy</i>
	 * 
	 * @author Richard Gomes
     */
    private LinearInterpolation() {
    	// access denied to default constructor
    }
    
	@Override
	protected double primitiveImpl(final double x) /* @ReadOnly */ {
        int i = locate(x);
        double dx = x - vx[i];
        return vp[i-1] + dx*(vy[i-1] + 0.5*dx*vs[i-1]);
	}

	@Override
	protected double derivativeImpl(final double x) /* @ReadOnly */ {
        int i = locate(x);
        return vs[i];
	}

	@Override
	protected double secondDerivativeImpl(final double x) /* @ReadOnly */ {
        return 0.0;
	}

	
    //
    // implements Interpolation
    //
    
	@Deprecated
	public void update() { reload(); }

	
	/**
	 * @note Class factory is responsible for initializing <i>vx</i> and <i>vy</i>  
	 */
	@Override
	public void reload() {
    	super.reload();

    	vp = new double[vx.length];
    	vs = new double[vx.length];
        vp[0] = 0.0;
        for (int i=1; i < vx.length; i++) {
        	double dx = vx[i] - vx[i-1];
        	vs[i-1] = (vy[i] - vy[i-1]) / dx;
            vp[i] = vp[i-1] + dx*(vy[i-1] +0.5*dx*vs[i-1]);
        }
	}
	


    //
    // concrete implementation of UnaryFunctionDouble.evaluate
    //
    
    protected double evaluateImpl(final double x) /* @ReadOnly */ {
        int i = locate(x);
        return vy[i] + (x - vx[i])*vs[i];
	}

    
    //
    // static methods
    //
    
    static public Interpolator getInterpolator() /* @ReadOnly */ {
    	LinearInterpolation linearInterpolation = new LinearInterpolation();
		return linearInterpolation. new LinearInterpolationImpl(linearInterpolation);
    }
    
    
    //
    // inner classes
    //
    
    /**
	 * This class is a factory for LinearInterpolation instances.
	 * 
	 * @author Richard Gomes
	 */
	private class LinearInterpolationImpl implements Interpolator {
		private LinearInterpolation delegate;
		
		public LinearInterpolationImpl(final LinearInterpolation delegate) {
			this.delegate = delegate;
		}
		
		public final Interpolation interpolate(final double[] x, final double[] y) /* @ReadOnly */ {
			return interpolate(x.length, x, y);
		}

		public final Interpolation interpolate(final int size, final double[] x, final double[] y) /* @ReadOnly */ {
			delegate.vx = Arrays.copyOf(x, size);
			delegate.vy = Arrays.copyOf(y, size);
			delegate.reload();
			return delegate;
		}
		
		public final boolean isGlobal() {
			return false; // only CubicSpline and Sabr are global, whatever it means!
		}
		
	}

}
