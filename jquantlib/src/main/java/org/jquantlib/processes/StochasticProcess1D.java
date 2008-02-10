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

/*
 Copyright (C) 2003 Ferdinando Ametrano
 Copyright (C) 2001, 2002, 2003 Sadruddin Rejeb
 Copyright (C) 2004, 2005 StatPro Italia srl

 This file is part of QuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://quantlib.org/

 QuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <quantlib-dev@lists.sf.net>. The license is also available online at
 <http://quantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
*/

package org.jquantlib.processes;

import org.jquantlib.number.Time;
import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.vector.DenseMatrix;
import org.jscience.mathematics.vector.DenseVector;
import org.jscience.mathematics.vector.Matrix;
import org.jscience.mathematics.vector.Vector;

public abstract class StochasticProcess1D extends StochasticProcess implements Discretization1D {
	
    protected StochasticProcess1D() {
    	super();
    }

    /**
     * Returns the initial value of the state variable
     */ 
    public abstract Real x0();

    /**
     * 
     * Returns the drift part of the equation 
     * {@latex$ \mu(t, x_t) }
     */
    public abstract Real drift(final Time t, final Real x);
    
    /**
     * Returns the diffusion part of the equation, i.e.
     * {@latex$ \sigma(t, x_t) }
     */
    public abstract Real diffusion(final Time t, final Real x);
    
    /**
     * Returns the expectation
     * {@latex$ E(x_{t_0 + \Delta t} | x_{t_0} = x_0) }
     * of the process after a time interval {@latex$ \Delta t }
     * according to the given discretization. This method can be
     * overridden in derived classes which want to hard-code a
     * particular discretization.
     */
    public final Real expectation(final Time t0, final Real x0, final Time dt) {
        return apply(x0, driftDiscretization(/**this, */ t0, x0, dt)); // XXX
    }
    
    /**
     * Returns the standard deviation
     * {@latex$ S(x_{t_0 + \Delta t} | x_{t_0} = x_0) }
     * of the process after a time interval {@latex$ \Delta t }
     * according to the given discretization. This method can be
     * overridden in derived classes which want to hard-code a
     * particular discretization.
     */
    public final Real stdDeviation(final Time t0, final Real x0, final Time dt) {
        return diffusionDiscretization(/**this, */ t0, x0, dt); // XXX
    }
    
    /**
     * Returns the variance
     * {@latex$ V(x_{t_0 + \Delta t} | x_{t_0} = x_0) }
     * of the process after a time interval {@latex$ \Delta t }
     * according to the given discretization. This method can be
     * overridden in derived classes which want to hard-code a
     * particular discretization.
     */
    public final Real variance(final Time t0, final Real x0, final Time dt) {
        return varianceDiscretization(/**this, */ t0, x0, dt); // XXX
    }
    
    /**
     * Returns the asset value after a time interval {@latex$ \Delta t }
     * according to the given discretization. By default, it returns
     * {@latex[
     *     E(x_0,t_0,\Delta t) + S(x_0,t_0,\Delta t) \cdot \Delta w
     * }
     * where {@latex$ E } is the expectation and {@latex$ S } the
     * standard deviation.
     */
    public final Real evolve(final Time t0, final Real x0, final Time dt, final Real dw) {
    	return apply(expectation(t0,x0,dt), stdDeviation(t0,x0,dt).times(dw));
    }

    /**
     * Applies a change to the asset value. By default, it
     * returns {@latex$ x + \Delta x }.
     */
    public Real apply(final Real x0, final Real dx) {
        return new Real( x0.doubleValue() + dx.doubleValue() );
    }

    
    // ======================================================================================================
    
    
    static private final String ARRAY_1D_REQUIRED = "1-D array required";
    
    public final int getSize() {
        return 1;
    }

    public final Vector<Real> initialValues() {
        return new DenseVector(new double[] { x0().doubleValue() } );
    }

    public final Vector<Real> drift(final Time t, final Vector<Real> x) {
    	if (x.size()!=1) throw new IllegalArgumentException(ARRAY_1D_REQUIRED);
    	return new DenseVector( new double[] { drift(t, x.get(0)).doubleValue() } );
    }

    public final Matrix diffusion(final Time t, final Vector<Real> x) {
    	if (x.size()!=1) throw new IllegalArgumentException(ARRAY_1D_REQUIRED);
    	Vector<Real> v = new DenseVector( new double[] { diffusion(t, x.get(0)).doubleValue() } );
        return new DenseMatrix(v);
    }

    public final Vector<Real> expectation(final Time t0, final Vector<Real> x0, final Time dt) {
    	if (x0.size()!=1) throw new IllegalArgumentException(ARRAY_1D_REQUIRED);
    	return new DenseVector( new double[] { expectation(t0, x0.get(0), dt).doubleValue() } );
    }
    
    public final Matrix stdDeviation(final Time t0, final Vector<Real> x0, final Time dt) {
    	if (x0.size()!=1) throw new IllegalArgumentException(ARRAY_1D_REQUIRED);
    	Vector<Real> v = new DenseVector( new double[] { stdDeviation(t0, x0.get(0), dt).doubleValue() } );
        return new DenseMatrix(v);
    }

    public final Matrix covariance(final Time t0, final Vector<Real> x0, final Time dt) {
    	if (x0.size()!=1) throw new IllegalArgumentException(ARRAY_1D_REQUIRED);
    	Vector<Real> v = new DenseVector( new double[] { varianceDiscretization(t0, x0.get(0), dt).doubleValue() } );
        return new DenseMatrix(v);
    }

    public final Vector<Real> evolve(final Time t0, final Vector<Real> x0, final Time dt, final Vector<Real> dw) {
    	if (x0.size()!=1) throw new IllegalArgumentException(ARRAY_1D_REQUIRED);
    	if (dw.size()!=1) throw new IllegalArgumentException(ARRAY_1D_REQUIRED);
    	return new DenseVector( new double[] { evolve(t0, x0.get(0), dt, dw.get(0)).doubleValue() } );
    }

    public final Vector<Real> apply(final Vector<Real> x0, final Vector<Real> dx) {
    	if (x0.size()!=1) throw new IllegalArgumentException(ARRAY_1D_REQUIRED);
    	if (dx.size()!=1) throw new IllegalArgumentException(ARRAY_1D_REQUIRED);
    	return new DenseVector( new double[] { apply(x0.get(0), dx.get(0)).doubleValue() } );
    }

}
