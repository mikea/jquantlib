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

import static org.jquantlib.math.Closeness.isClose;

import org.jquantlib.util.stdlibc.Std;

import cern.colt.Sorting;


public abstract class AbstractInterpolation2D implements Interpolation2D {

    //
    // protected fields
    //
    
    /**
	 * @note Derived classes are responsible for initializing <i>vx</i> and <i>vy</i> 
	 */
	protected double[] vx;

	/**
	 * @note Derived classes are responsible for initializing <i>vx</i> and <i>vy</i> 
	 */
	protected double[] vy;
	
	protected double[][] mz;
	

	//
	// protected abstract methods
	//
	
	protected abstract double evaluateImpl(final double x, final double y);
	

	//
	// public methods
	//
	
	/**
	 * This method intentionally throws UnsupportedOperationException in order to
	 * oblige derived classes to reimplement it if needed.
	 * 
	 * @throws UnsupportedOperationException
	 */
	public void calculate() {
	    throw new UnsupportedOperationException();
	}
	
	
	//
    // protected methods
    //
    
    
	/**
     * This method verifies if
     * <li> extrapolation is enabled;</li>
     * <li> requested <i>x</i> is valid</li>
     * 
     * @param x
     * @param extrapolate
     * 
     * @throws IllegalStateException if extrapolation is not enabled.
     * @throws IllegalArgumentException if <i>x</i> is our of range
     */
    // FIXME: code review : verify if parameter 'extrapolate' is really needed
    protected final void checkRange(final double x, final double y, boolean extrapolate) {
        if (! (extrapolate || allowsExtrapolation() || isInRange(x, y)) ) {
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

    
    //
	// implements Interpolation2D
	//
	
	@Override
	public double xMin() {
		return vx[0]; // get first element
	}

    @Override
	public double xMax() {
		return vx[vx.length-1]; // get last element
	}

    @Override
	public double yMin() {
		return  vy[0]; // get first element
	}

    @Override
	public double yMax() {
		return vy[vy.length-1]; // get last element
	}

    @Override
	public double[] xValues() {
    	return vx.clone();
    }
	
    @Override
	public double[] yValues() {
    	return vy.clone();
    }

    @Override
	public double[][] zData() {
        return mz.clone();
    	// FIXME: code review :: return (double[][])Arrays.trimToCapacity(mz, mz.length);
    }

    @Override
    // FIXME: code review here: compare against original C++ code
    public int locateX(double x) /* @ReadOnly */ {
        if (x <= vx[0])
            return 0;
        else if (x > vx[vx.length-1])
            return vx.length-2;
        else
            return Std.upper_bound(vx, 0, vx.length-1, x) - 1;//Sorting.binarySearchFromTo(vx, x, 0, vx.length-1)-1;
    }
    
    @Override
    // FIXME: code review here: compare against original C++ code
    public int locateY(double y) /* @ReadOnly */ {
        if (y <= vy[0])
            return 0;
        else if (y > vy[vy.length-1])
            return vy.length-2;
        else
            return Std.upper_bound(vy, 0, vy.length-1, y) - 1;//Sorting.binarySearchFromTo(vy, y, 0, vy.length-1)-1;
    }
    
    @Override
    public boolean isInRange(final double x, final double y) {
        double x1 = xMin(), x2 = xMax();
        boolean xIsInrange = (x >= x1 && x <= x2) || isClose(x,x1) || isClose(x,x2);
        if (!xIsInrange) return false;

        double y1 = yMin(), y2 = yMax();
        return (y >= y1 && y <= y2) || isClose(y,y1) || isClose(y,y2);
    }
    
    @Deprecated
    @Override
    public void update() {
        reload();
    }

    @Override
	public void reload() {
		if (vx.length < 2 || vy.length < 2)
			throw new IllegalArgumentException("not enough points to interpolate");
		for (int i = 0; i < vx.length-1; i++) {
			if ( vx[i] > vx[i+1] )
				throw new IllegalArgumentException("unsorted values on array X");
			if ( vy[i] > vy[i+1] )
				throw new IllegalArgumentException("unsorted values on array Y");
		}
	}

	
	//
	// implements BinaryFunctionDouble
	//
	
    @Override
	public double evaluate(final double x, final double y) {
        checkRange(x, y, this.allowsExtrapolation());
        return evaluateImpl(x, y);
    }
    
  
	//
	// implements Extrapolator
	//

	/**
	 * Implements multiple inheritance via delegate pattern to an inner class
	 * 
	 * @see Extrapolator
	 */
	private DefaultExtrapolator delegatedExtrapolator = new DefaultExtrapolator();
	
    @Override
	public final boolean allowsExtrapolation() {
		return delegatedExtrapolator.allowsExtrapolation();
	}

    @Override
	public void disableExtrapolation() {
		delegatedExtrapolator.disableExtrapolation();
	}

    @Override
	public void enableExtrapolation() {
		delegatedExtrapolator.enableExtrapolation();
	}

}
