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


/**
 * This class provides log-linear interpolation between discrete points
 * 
 * @author Dominik Holenstein
 * @author Richard Gomes
 */
// TODO implement primitive, derivative, and secondDerivative functions.
public class LogLinearInterpolation extends AbstractInterpolation {
	
	private double[] logY;
	private Interpolation linearInterpolation;
	
	
	private LogLinearInterpolation() {
		//access denied to default constructor
	}
	
	@Override
	public double getMinX() /* @ReadOnly */ {
		return vx[0]; // get first element
	}
	
	@Override
	public double getMaxX() /* @ReadOnly */ {
		return vx[vx.length-1]; // get last element
	}
	
	@Override
	protected double primitiveImpl(final double x) /* @ReadOnly */ {
		throw new UnsupportedOperationException();
	}
	
	@Override
	protected double derivativeImpl(final double x) /* @ReadOnly */ {
		throw new UnsupportedOperationException();
	}
	
	@Override
	protected double secondDerivativeImpl(final double x) /* @ReadOnly */ {
		throw new UnsupportedOperationException();
	}
	
	//
	// Implements interpolation
	//
	
	@Deprecated
	public void update() { reload(); }
	
	/**
	 * @note Class factory is responsible for initializing <i>vx</i> and <i>vy</i>
	 */
	
	@Override
	public void reload() {
		super.reload();
		
		for (int i=0; i<vx.length; i++){
			if (vx[i] <= 0.0) {
				throw new ArithmeticException("negative or null value " + vx[i] + " at " + i + " position.");
			}
			logY[i] = Math.log(vy[i]);
		}
		linearInterpolation = LinearInterpolation.getInterpolator().interpolate(vx, logY);
	}
	

	// 
	// concrete implementation of UnaryFunctionDouble.evaluate
	//
	
	protected double evaluateImpl(final double x) /* @ReadOnly */ {
		return Math.exp(linearInterpolation.evaluate(x));
	}
	
	//
	// inner classes
	//
	
	static public Interpolator getInterpolator() {
		return new LogLinearInterpolationImpl();
	}
	
	/**
	 * This static class is a factory for LogLinearInterpolation instances.
	 * 
	 * @author Dominik Holenstein
	 * @author Richard Gomes
	 */	
	
	static private class LogLinearInterpolationImpl implements Interpolator {
		private LogLinearInterpolation delegate;
		
		public LogLinearInterpolationImpl() {
			delegate = new LogLinearInterpolation();
		}
		
		public Interpolation interpolate(double[] x, double[] y) /* @ReadOnly */ {
			delegate.vx = x;
			delegate.vy = y;
			delegate.reload();
			return delegate;
		}
	}
}
