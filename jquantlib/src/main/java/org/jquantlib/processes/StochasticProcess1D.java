/*
 Copyright (C) 2008 Richard Gomes

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquant-devel@lists.sourceforge.net>. The license is also available online at
 <http://www.jquantlib.org/index.php/LICENSE.TXT>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
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


public abstract class StochasticProcess1D extends StochasticProcess {
	
    protected Discretization1D discretization1D; 
	
	/**
	 * @param discretization is an Object that <b>must</b> implement {@link Discretization} <b>and</b> {@link Discretization1D}.
	 */
    protected StochasticProcess1D(final LinearDiscretization discretization) {
    	super(discretization);
    	this.discretization1D = discretization;
    }

    /**
     * Returns the initial value of the state variable
     */ 
    public abstract /*@Price*/ double x0();

    /**
     * 
     * Returns the drift part of the equation 
     * {@latex$ \mu(t, x_t) }
     */
    public abstract /*@Drift*/ double drift(final /*@Time*/ double t, final /*@Price*/ double x);
    
    /**
     * Returns the diffusion part of the equation, i.e.
     * {@latex$ \sigma(t, x_t) }
     */
    public abstract /*@Diffusion*/ double diffusion(final /*@Time*/ double t, final /*@Price*/ double x);
    
    /**
     * Returns the expectation
     * {@latex$ E(x_{t_0 + \Delta t} | x_{t_0} = x_0) }
     * of the process after a time interval {@latex$ \Delta t }
     * according to the given discretization. This method can be
     * overridden in derived classes which want to hard-code a
     * particular discretization.
     */
    public final /*@Expectation*/ double expectation(final /*@Time*/ double t0, final /*@Price*/ double x0, final /*@Time*/ double dt) {
        return apply(x0, discretization1D.driftDiscretization(this, t0, x0, dt)); // XXX
    }
    
    /**
     * Returns the standard deviation
     * {@latex$ S(x_{t_0 + \Delta t} | x_{t_0} = x_0) }
     * of the process after a time interval {@latex$ \Delta t }
     * according to the given discretization. This method can be
     * overridden in derived classes which want to hard-code a
     * particular discretization.
     */
    public final /*@StdDev*/ double stdDeviation(final /*@Time*/ double t0, final double x0, final /*@Time*/ double dt) {
        return discretization1D.diffusionDiscretization(this, t0, x0, dt); // XXX
    }
    
    /**
     * Returns the variance
     * {@latex$ V(x_{t_0 + \Delta t} | x_{t_0} = x_0) }
     * of the process after a time interval {@latex$ \Delta t }
     * according to the given discretization. This method can be
     * overridden in derived classes which want to hard-code a
     * particular discretization.
     */
    public final /*@Variance*/ double variance(final /*@Time*/ double t0, final double x0, final /*@Time*/ double dt) {
        return discretization1D.varianceDiscretization(this, t0, x0, dt); // XXX
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
    public final /*@Price*/ double evolve(final /*@Time*/ double t0, final /*@Price*/ double x0, final /*@Time*/ double dt, final double dw) {
    	return apply(expectation(t0,x0,dt), stdDeviation(t0,x0,dt) * dw);
    }

    /**
     * Applies a change to the asset value. By default, it
     * returns {@latex$ x + \Delta x }.
     */
    public /*@Price*/ double apply(final /*@Price*/ double x0, final /*@Price*/ double dx) {
        return x0 + dx;
    }

    
    // ======================================================================================================
    
    
    static private final String ARRAY_1D_REQUIRED = "1-D array required";
    
    public final int getSize() {
        return 1;
    }

    public final /*@Price*/ double[] initialValues() {
        return new double[] { x0() };
    }

    public final /*@Price*/ double[] drift(final /*@Time*/ double t, /*@Price*/ double[] x) {
    	if (x.length!=1) throw new IllegalArgumentException(ARRAY_1D_REQUIRED);
    	return new double[] { drift(t, x[0]) };
    }

    public final /*@Diffusion*/ double[][] diffusion(final /*@Time*/ double t, /*@Price*/ double[] x) {
    	if (x.length!=1) throw new IllegalArgumentException(ARRAY_1D_REQUIRED);
    	double v = diffusion(t, x[0]);
    	return new double[][] { { v } };
    }

    public final /*@Expectation*/ double[] expectation(final /*@Time*/ double t0, final /*@Price*/ double[] x0, final /*@Time*/ double dt) {
    	if (x0.length!=1) throw new IllegalArgumentException(ARRAY_1D_REQUIRED);
    	return new double[] { expectation(t0, x0[0], dt) };
    }
    
    public final /*@StdDev*/ double[][] stdDeviation(final /*@Time*/ double t0, final /*@Price*/ double[] x0, final /*@Time*/ double dt) {
    	if (x0.length!=1) throw new IllegalArgumentException(ARRAY_1D_REQUIRED);
    	double v = stdDeviation(t0, x0[0], dt);
    	return new double[][] { { v } };
    }

    public final /*@Covariance*/ double[][] covariance(final /*@Time*/ double t0, final /*@Price*/ double[] x0, final /*@Time*/ double dt) {
    	if (x0.length!=1) throw new IllegalArgumentException(ARRAY_1D_REQUIRED);
    	double v = discretization1D.varianceDiscretization(this, t0, x0[0], dt);
    	return new double[][] { { v } };
    }

    public final double[] evolve(final /*@Time*/ double t0, final /*@Price*/ double[] x0, final /*@Time*/ double dt, final double[] dw) {
    	if (x0.length!=1) throw new IllegalArgumentException(ARRAY_1D_REQUIRED);
    	if (dw.length!=1) throw new IllegalArgumentException(ARRAY_1D_REQUIRED);
    	return new double[] { evolve(t0, x0[0], dt, dw[0]) };
    }

    public final /*@Price*/ double[] apply(final /*@Price*/ double[] x0, final double[] dx) {
    	if (x0.length!=1) throw new IllegalArgumentException(ARRAY_1D_REQUIRED);
    	if (dx.length!=1) throw new IllegalArgumentException(ARRAY_1D_REQUIRED);
    	return new double[] { apply(x0[0], dx[0]) };
    }

}
