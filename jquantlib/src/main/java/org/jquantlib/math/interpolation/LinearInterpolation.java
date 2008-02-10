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

import javolution.util.FastTable;

import org.jquantlib.util.RPN;
import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.vector.Vector;

// FIXME: comments
public class LinearInterpolation extends Interpolation<Real> {

    private FastTable<Real> vp;
    private FastTable<Real> vs;

    public LinearInterpolation(final Vector<Real> x, final Vector<Real> y) {
    	super(x, y);
    	vp = new FastTable<Real>();
    	vs = new FastTable<Real>();
    }
    
	@Override
    public void update() {
		vp.clear();
        vp.add(Real.ZERO);
        RPN rpn = new RPN();
        for (int i=1; i<vx.getDimension(); i++) {
        	// dx = vx[i] - vx[i-1]
        	Real dx = rpn.expr( vy.get(i), vy.get(i-1), '-' );
        	
        	// dy = (vy[i] - vy[i-1]) / dx
        	vs.add(rpn.expr( vy.get(i), vy.get(i-1), '-', dx, '/' ));
        	
            // vp[i] = vp[i-1] + dx*(vy[i-1] +0.5*dx*vs[i-1]);
        	vp.add(rpn.expr( vp.get(i-1), dx, vy.get(i-1), 0.5, dx, vs.get(i-1), '*', '*', '+', '*', '+' ));  
        }
    }

	@Override
	public Real xMin() {
		return  vx.get(0); // get first element
	}

	@Override
	public Real xMax() {
		return vx.get(vx.getDimension()-1); // get last element
	}

	@Override
	protected Real getValue(final Real x) {
        int i = locate(x);
        
        // result = vy[i] + (x - vx[i])*vs[i]
        RPN rpn = new RPN();
        return rpn.expr( vy.get(i), x, vx.get(i), '-', vs.get(i), '*', '+' );
	}

	@Override
	protected Real primitive(final Real x) {
        int i = locate(x);
        
        // dx = x - vx[i]
        RPN rpn = new RPN();
        Real dx = rpn.expr( x, vx.get(i), '-');

        // result = vp[i-1] + dx*(vy[i-1] +0.5*dx*vs[i-1]);
        return rpn.expr( vp.get(i-1), dx, vy.get(i-1), 0.5, dx, vs.get(i-1), '*', '*', '+', '*', '+' );
	}

	@Override
	protected Real derivative(final Real x) {
        int i = locate(x);
        return vs.get(i);
	}

	@Override
	protected Real secondDerivative(final Real x) {
        return Real.ZERO;
	}

}
