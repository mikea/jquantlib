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


import org.jquantlib.math.interpolation.Interpolation;
import org.jquantlib.math.interpolation.Interpolator;
import org.jquantlib.math.interpolation.LinearInterpolation;

/**
 * This class is provided for backwards compatibility only.
 * 
 * <p>
 * Use LogLinearInterpolation.Factory instead
 * 
 * @see LogLinearInterpolation.Factory
 * 
 * @author Richard Gomes
 */

//FIXME comments
public class LogLinearInterpolation extends AbstractInterpolation {
	
	private double[] logY_;
	private Interpolator linearInterpolation_;
	
	
	private LogLinearInterpolation(){
		//access denied to public default constructor
	}
	
	@Override
	public double getMinX(){
		return vx[0]; // get first element
	}
	
	@Override
	public double getMaxX() {
		return vx[vx.length-1]; // get last element
	}
	
	@Override
	protected double primitiveImpl(final double x){
		throw new ArithmeticException("LogLinear primitive not implemented)");
	}
	
	@Override
	protected double derivativeImpl(final double x){
		throw new ArithmeticException("LogLinear primitive not implemented)");
	}
	
	@Override
	protected double secondDerivativeImpl(final double x){
		throw new ArithmeticException("LogLinear primitive not implemented)");
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
			if (!(vx[i]>0.0)){
				throw new ArithmeticException("negative or null value " + vx[i] + " at " + i + " position.");
			}
			logY_[i] = Math.log(vy[i]);
		}
		
		//FIXME Review the Java code and compare to C++ code (from loglinearinterpolation.hpp)
		/*
		 * C++ code: 
		 * linearInterpolation_ = LinearInterpolation(this->xBegin_,
                                                           this->xEnd_,
                                                           logY_.begin());
                linearInterpolation_.update();
		 */
		linearInterpolation_ = LinearInterpolation.getInterpolator();
		linearInterpolation_.interpolate(vx, logY_);
	}
	
	// 
	// concrete implementation of UnaryFunctionDouble.evalute
	//
	
	// FIXME: The method evaluateImpl(final double x) is not correct.
	
	/*
	 * C++ code (from loglinearinterpolation.hpp):
	 * Real value(Real x) const {
                return std::exp(linearInterpolation_(x,true));
            }
	 */
	protected double evaluateImpl(final double x){
		int i = locate(x);
		return(Math.exp(logY_[i]));
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
	 * @author Richard Gomes
	 */	
	
	static private class LogLinearInterpolationImpl implements Interpolator {
		private LogLinearInterpolation delegate;
		
		public LogLinearInterpolationImpl() {
			delegate = new LogLinearInterpolation();
		}
		
		public Interpolation interpolate(double[] x, double[] y){
			delegate.vx = x;
			delegate.vy = y;
			delegate.reload();
			return delegate;
		}
	}
}
