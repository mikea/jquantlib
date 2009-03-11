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

import org.jquantlib.math.interpolations.factories.LogLinear;


/**
 * This class provides log-linear interpolation between discrete points
 * <p>
 * Interpolations are not instantiated directly by applications, but via a factory class.
 * 
 * @see LogLinear
 * 
 * @author Dominik Holenstein
 * @author Richard Gomes
 */
public class LogLinearInterpolation extends AbstractInterpolation {
	
    //
    // private fields
    //
    
    private double[] logY;
	private Interpolation linearInterpolation;
	
	
	//
	// private constructors
	//
	
    /**
     * Constructor for a backward-flat interpolation between discrete points
     * <p>
     * Interpolations are not instantiated directly by applications, but via a factory class.
     * 
     * @see LogLinear
     */
	private LogLinearInterpolation() {
		// access denied to default constructor
	}

	
    //
    // static public methods
    //
    
    /**
     * This is a factory method intended to create this interpolation.
     * 
     * @see LogLinear
     */
    static public Interpolator getInterpolator() {
        LogLinearInterpolation logLinearInterpolation = new LogLinearInterpolation();
        return new LogLinearInterpolationImpl(logLinearInterpolation);
    }
    

    //
    // overrides AbstractInterpolation
    //
    
	/**
	 * This method throws UnsupportedOperationException because the original
	 * QuantLib/C++ code does not implement this functionality
	 * 
	 * @throws UnsupportedOperationException
	 */
	@Override
	protected double primitiveImpl(final double x) /* @ReadOnly */ {
		throw new UnsupportedOperationException(); //TODO: message
	}
	
    /**
     * This method throws UnsupportedOperationException because the original
     * QuantLib/C++ code does not implement this functionality
     * 
     * @throws UnsupportedOperationException
     */
	@Override
	protected double derivativeImpl(final double x) /* @ReadOnly */ {
		throw new UnsupportedOperationException(); //TODO: message
	}
	
    /**
     * This method throws UnsupportedOperationException because the original
     * QuantLib/C++ code does not implement this functionality
     * 
     * @throws UnsupportedOperationException
     */
	@Override
	protected double secondDerivativeImpl(final double x) /* @ReadOnly */ {
		throw new UnsupportedOperationException(); //TODO: message
	}
	

	//
	// implements interpolation
	//
	
    /**
     * {@inheritDoc}
     */
    @Deprecated
    @Override
	public void update() {
        reload();
    }
	
    /**
     * {@inheritDoc}
     * 
     * @note Class factory is responsible for initializing <i>vx</i> and <i>vy</i>  
     */
	@Override
	public void reload() {
		super.reload();
		
		logY = new double[vx.length];
		for (int i=0; i<vx.length; i++){
			if (vx[i] <= 0.0) {
				throw new ArithmeticException("negative or null value " + vx[i] + " at " + i + " position.");
			}
			logY[i] = Math.log(vy[i]);
		}
		linearInterpolation = LinearInterpolation.getInterpolator().interpolate(vx, logY);
	}
	

	// 
	// implements UnaryFunctionDouble
	//
	
	protected double evaluateImpl(final double x) /* @ReadOnly */ {
		return Math.exp(linearInterpolation.evaluate(x));
	}
	
    
	//
	// private inner classes
	//
	
	/**
	 * This static class is a default implementation for {@link LogLinearInterpolation} instances.
	 * 
	 * @author Dominik Holenstein
	 * @author Richard Gomes
	 */	
	
	private static class LogLinearInterpolationImpl implements Interpolator {
		private LogLinearInterpolation delegate;
		
		public LogLinearInterpolationImpl(final LogLinearInterpolation delegate) {
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
