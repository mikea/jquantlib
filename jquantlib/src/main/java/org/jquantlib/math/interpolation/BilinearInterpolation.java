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

public class BilinearInterpolation extends AbstractInterpolation2D {

    /**
     * Private default constructor.
     * 
	 * @note Class factory is responsible for initializing <i>vx</i> and <i>vy</i>
	 * 
	 * @author Richard Gomes
     */
	private BilinearInterpolation() {
    	// access denied to public default constructor

	}

	
    //
    // implements Interpolation2D
    //
    
	@Deprecated
	public void update() { reload(); }

	public void reload() {
		// FIXME: code review
		
		// nothing ??????? it's certainly wrong!
	}

	
	//
    // concrete implementation of BinaryFunctionDouble.evaluate
    //

	public double evaluateImpl(double x, double y) /* @ReadOnly */{
		int i = locateX(x);
		int j = locateY(y);

		double z1 = mz[j][i];
		double z2 = mz[j][i + 1];
		double z3 = mz[j + 1][i];
		double z4 = mz[j + 1][i + 1];

		double t = (x - vx[i]) / (vx[i + 1] - vx[i]);
		double u = (y - vy[j]) / (vy[j + 1] - vy[j]);

		return (1.0 - t) * (1.0 - u) * z1 + t * (1.0 - u) * z2 + (1.0 - t) * u * z3 + t * u * z4;
	}

	
	//
    // inner classes
    //
    
    static public Interpolator2D getInterpolator() {
    	return new BilinearInterpolationImpl();
    }
    
    /**
	 * This static class is a factory for BilinearInterpolation instances.
	 * 
	 * @author Richard Gomes
	 */
	static private class BilinearInterpolationImpl implements Interpolator2D {
		private BilinearInterpolation delegate;
		
		public BilinearInterpolationImpl() {
			delegate = new BilinearInterpolation();
		}
		
		public Interpolation2D interpolate(final double[] x, final double[] y, final double[][] z) {
			delegate.vx = x;
			delegate.vy = y;
			delegate.mz = z;
			delegate.reload();
			return delegate;
		}

	}

}
