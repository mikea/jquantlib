/*
 Copyright (C) 2008 Anand Mani

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


/**
 * This class performs a flat extrapolation backed by an existing {@link Interpolation2D}.
 * <p>
 * Unlike other classes extended from Interpolation and Interpolation2D interfaces, this class is not self enclosed but
 * it depends on another interpolation class in order to perform the extrapolation.
 *
 * @author Richard Gomes
 */
public class FlatExtrapolator2D extends AbstractInterpolation2D {

    //
    // private final fields
    //
    
    private final Interpolation2D decorated;
    
    
    //
	// private constructors
	//

	private FlatExtrapolator2D(final Interpolation2D decorated) {
	    this.decorated = decorated;
	    if (this.decorated==null) throw new NullPointerException();
	}

	
	//
	// static public methods
	//
	
	// FIXME: should be Interpolation2D, but the xMin(), xMax() etc are not in the interface.
    public static Interpolation2D getInterpolation2D(final Interpolation2D decorated) {
        FlatExtrapolator2D flatExtrapolation2D = new FlatExtrapolator2D(decorated);
        return flatExtrapolation2D;
    }


    //
    // overrides Interpolator2D
    //
    
    @Override
    public double xMin() {
        return decorated.xMin();
    }

    @Override
    public double xMax() {
        return decorated.xMax();
    }

    @Override
    public double yMin() {
        return decorated.yMin();
    }

    @Override
    public double yMax() {
        return decorated.yMax();
    }

    @Override
    public double[] xValues() {
        return decorated.xValues();
    }

    @Override
    public double[] yValues() {
        return decorated.yValues();
    }

    @Override
    public double[][] zData() {
        return decorated.zData();
    }
    
    @Override
    public int locateX(double x) {
        return decorated.locateX(x);
    }

    @Override
    public int locateY(double y) {
        return decorated.locateY(y);
    }

    @Override
    public boolean isInRange(double x, double y) {
        return decorated.isInRange(x, y);
    }

    @Deprecated
    @Override
    public void update() {
        decorated.update();
    }

    @Override
    public void reload() {
        decorated.reload();
    }

    
    //
    // overrides BinaryFunctionDouble
    //
    
    @Override
    public double evaluate(double x, double y) {
        x = bindX(x);
        y = bindY(y);
        return decorated.evaluate(x, y);
    }

    
    //
    // overrides AbstractInterpolation2D
    //
    
    /**
     * This method always throws UnsupportedOperationException
     * 
     * @throws UnsupportedOperationException
     */
    @Override
    protected double evaluateImpl(double x, double y) {
        throw new UnsupportedOperationException(); //TODO: message
    }


    //
	// private methods
	//
	
	private double bindX(double x) /* @ReadOnly */{
		if (x < xMin())
			return xMin();
		if (x > xMax())
			return xMax();
		return x;
	}

	private double bindY(double y) /* @ReadOnly */{
		if (y < yMin())
			return yMin();
		if (y > yMax())
			return yMax();
		return y;
	}

}
