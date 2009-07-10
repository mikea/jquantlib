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

import org.jquantlib.math.Array;
import org.jquantlib.math.Matrix;


public abstract class AbstractInterpolation2D implements Interpolation2D {

    //
    // protected fields
    //
    
    /**
	 * @note Derived classes are responsible for initializing <i>vx</i>, <i>vy</i> and eventually <i>mz</i> 
	 */
	protected Array vx;

	/**
     * @note Derived classes are responsible for initializing <i>vx</i>, <i>vy</i> and eventually <i>mz</i> 
	 */
	protected Array vy;
	
    /**
     * @note Derived classes are responsible for initializing <i>vx</i>, <i>vy</i> and eventually <i>mz</i> 
     */
	protected Matrix mz;
	

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
	    throw new UnsupportedOperationException(); //FIXME: message
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
    // implements Interpolation
    //
    
    /**
     * {@inheritDoc}
     * 
     * @deprecated
     */
    @Override
    public void update() {
        reload();
    }


    //
    // Overrides AbstractInterpolation
    //
    
    @Override
    public void reload() {
        if (vx.length < 2 || vy.length < 2)
            throw new IllegalArgumentException("not enough points to interpolate");
        for (int i = 0; i < vx.length-1; i++) {
            if ( vx.get(i) > vx.get(i+1) )
                throw new IllegalArgumentException("unsorted values on array X");
            if ( vy.get(i) > vy.get(i+1) )
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


    //
	// implements Interpolation2D
	//
	
	@Override
	public double xMin() {
		return vx.first();
	}

    @Override
	public double xMax() {
		return vx.last();
	}

    @Override
	public double yMin() {
		return  vy.first();
	}

    @Override
	public double yMax() {
		return vy.last();
	}

    @Override
	public Array xValues() {
    	return vx.clone();
    }
	
    @Override
	public Array yValues() {
    	return vy.clone();
    }

    @Override
	public Matrix zData() {
        return mz.clone();
    	// FIXME: code review :: return (double[][])Arrays.trimToCapacity(mz, mz.length);
    }

    @Override
    // FIXME: code review here: compare against original C++ code
    public int locateX(double x) /* @ReadOnly */ {
        if (x <= vx.first())
            return 0;
        else if (x > vx.last())
            return vx.length-2;
        else
            return vx.upperBound(x) - 1;
    }
    
    @Override
    // FIXME: code review here: compare against original C++ code
    public int locateY(double y) /* @ReadOnly */ {
        if (y <= vy.first())
            return 0;
        else if (y > vy.last())
            return vy.length-2;
        else
            return vy.upperBound(y) - 1;
    }
    
    @Override
    public boolean isInRange(final double x, final double y) {
        double x1 = xMin(), x2 = xMax();
        boolean xIsInrange = (x >= x1 && x <= x2) || isClose(x,x1) || isClose(x,x2);
        if (!xIsInrange) return false;

        double y1 = yMin(), y2 = yMax();
        return (y >= y1 && y <= y2) || isClose(y,y1) || isClose(y,y2);
    }

    
}
