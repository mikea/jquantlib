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

import java.util.Vector;

import org.jquantlib.util.Date;
import org.jquantlib.util.DefaultObservable;
import org.jquantlib.util.Observable;
import org.jquantlib.util.Observer;

/**
 * Multi-dimensional stochastic process class.
 * 
 * <p>{@latex[
 *       d\mathrm{x}_t =
 *         \mu(t, x_t)\mathrm{d}t + \sigma(t, \mathrm{x}_t) \cdot d\mathrm{W}_t.
 *    }
 */ 
public abstract class StochasticProcess implements Observable, Discretization, Observer {

    protected StochasticProcess() {
    	super();
    }

    /**
     * Returns the number of dimensions of the stochastic process
     */
    public abstract double size();
    
    /**
     * Returns the number of independent factors of the process
     */
    public double factors() {
    	return size();
    }
        
    /**
     * Returns the initial values of the state variables
     */
    public abstract double[] initialValues(); // FIXME: add typecast
        
    /**
     * Returns the drift part of the equation, i.e.,
     * {@latex$ \mu(t, \mathrm{x}_t) }
     */
    public abstract /*@Drift*/ double[] drift(final /*@Time*/ double t, final double[] x);
        
    /**
     * Returns the diffusion part of the equation, i.e.
     * {@latex$ \sigma(t, \mathrm{x}_t) }
     */
    public abstract /*@Diffusion*/ double[][] diffusion(final /*@Time*/ double t, final double[] x);
        
    /**
     * Returns the expectation
     * {@latex$ S(\mathrm{x}_{t_0 + \Delta t}
     *     | \mathrm{x}_{t_0} = \mathrm{x}_0) }
     * of the process after a time interval {@latex$ \Delta t }
     * according to the given discretization. This method can be
     * overridden in derived classes which want to hard-code a
     * particular discretization.
     */
    public /*@Expectation*/ double[] expectation(final /*@Time*/ double t0, final double[] x0, final /*@Time*/ double dt) {
    	return apply(x0, driftDiscretization(/*this, */t0, x0, dt)); //XXX
    }
    
    /**
     * Returns the standard deviation
     * {@latex$ S(\mathrm{x}_{t_0 + \Delta t}
     *     | \mathrm{x}_{t_0} = \mathrm{x}_0) }
     * of the process after a time interval {@latex$ \Delta t }
     * according to the given discretization. This method can be
     * overridden in derived classes which want to hard-code a
     * particular discretization.
     */
    public /*@StdDev*/ double[][] stdDeviation(final /*@Time*/ double t0, final double[] x0, final /*@Time*/ double dt) {
    	return diffusionDiscretization(/*this, */t0, x0, dt); // XXX
    }
    
    /**
     * Returns the covariance
     * {@latex$ V(\mathrm{x}_{t_0 + \Delta t}
     *     | \mathrm{x}_{t_0} = \mathrm{x}_0) }
     * of the process after a time interval {@latex$ \Delta t }
     * according to the given discretization. This method can be
     * overridden in derived classes which want to hard-code a
     * particular discretization.
     */
    public /*@Covariance*/ double[][] covariance(final /*@Time*/ double t0, final double[] x0, final /*@Time*/ double dt) {
    	return covarianceDiscretization(/*this, */t0, x0, dt); // XXX
    }
    
    /**
     * Returns the asset value after a time interval {@latex$ \Delta t }
     * according to the given discretization. By default, it returns
     * {@latex[
     *   E(\mathrm{x}_0,t_0,\Delta t) +
     *   S(\mathrm{x}_0,t_0,\Delta t) \cdot \Delta \mathrm{w}
     * }
     * where {@latex$ E } is the expectation and {@latex$ S } the
     * standard deviation.
     */
    public /*@Price*/ double[] evolve(final /*@Time*/ double t0, final double[] x0, final /*@Time*/ double dt, final double[] dw) {
    	// y = M * dw
    	Vector<Real> y = stdDeviation(t0, x0, dt).times(dw);
    	return apply(expectation(t0, x0, dt), y);
    }
    
    /**
     * Applies a change to the asset value. By default, it
     * returns {@latex$ \mathrm{x} + \Delta \mathrm{x} }.
     */
    public /*@Price*/ double[] apply(final double[] x0, final double[] dx) {
    	return x0.plus(dx);
    }

    /**
     * Returns the time value corresponding to the given date
     * in the reference system of the stochastic process.
     * 
     * <p><b>Note:</b> As a number of processes might not need this 
     * functionality, a default implementation is given
     * which raises an exception.
     */
    public final /*@Time*/ double getTime(final Date date) {
    	throw new UnsupportedOperationException("date/time conversion not supported");
    }
    

    void update() {
    	notifyObservers();
    }

	/**
	 * Implements multiple inheritance via delegate pattern to an inner class
	 * 
	 * @see Observable
	 * @see DefaultObservable
	 */
    private Observable delegatedObservable = new DefaultObservable();

	public void addObserver(Observer observer) {
		delegatedObservable.addObserver(observer);
	}

	public void deleteObserver(Observer observer) {
		delegatedObservable.deleteObserver(observer);
	}

	public void notifyObservers() {
		delegatedObservable.notifyObservers();
	}

	public void notifyObservers(Object arg) {
		delegatedObservable.notifyObservers(arg);
	}

}
