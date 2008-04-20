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


// FIXME: comments
public class LinearInterpolation extends AbstractInterpolation {

    private double[] vp;
    private double[] vs;

    /**
     * Private constructor.
     * 
     * @param x
     * @param y
     */
    private LinearInterpolation(final double[] x, final double[] y) {
    	super(x, y);
    	vp = new double[x.length];
    	vs = new double[x.length];
    }
    
	@Override
	public double xMin() {
		return  vx[0]; // get first element
	}

	@Override
	public double xMax() {
		return vx[vx.length-1]; // get last element
	}

	@Override
	protected double primitiveImpl(final double x) {
        int i = locate(x);
        double dx = x - vx[i];
        return vp[i-1] + dx*(vy[i-1] + 0.5*dx*vs[i-1]);
	}

	@Override
	protected double derivativeImpl(final double x) {
        int i = locate(x);
        return vs[i];
	}

	@Override
	protected double secondDerivativeImpl(final double x) {
        return 0.0;
	}

	
    //
    // implements Interpolation
    //
    
	@Deprecated
	public void update() { reload(); }

	public void reload() {
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
    
    protected double evaluateImpl(final double x) {
        int i = locate(x);
        return vy[i] + (x - vx[i])*vs[i];
	}

    
    //
    // inner classes
    //
    
    static public Interpolator getFactory() {
    	return new Factory();
    }
    
    /**
	 * This static class is a factory for LinearInterpolation instances.
	 * 
	 * @author Richard Gomes
	 */
	static private class Factory implements Interpolator {

		public Interpolation interpolate(double[] x, double[] y) {
			return new LinearInterpolation(x, y);
		}

	}

}
