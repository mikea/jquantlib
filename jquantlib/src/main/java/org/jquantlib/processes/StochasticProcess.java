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

import java.util.List; // FIXME :: performance

import org.jquantlib.util.Date;
import org.jquantlib.util.DefaultObservable;
import org.jquantlib.util.Observable;
import org.jquantlib.util.Observer;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.jet.math.Functions;

/**
 * Multi-dimensional stochastic process class.
 * 
 * <p>{@latex[
 * d\mathrm{x}_t = \mu(t,x_t)\mathrm{d}t + \sigma(t,\mathrm{x}_t) \cdot d\mathrm{W}_t.
 * }
 * 
 * @author Richard Gomes
 */ 
public abstract class StochasticProcess implements Observable, Observer {

	private Discretization discretization;
	
	/**
	 * @param discretization is an Object that <b>must</b> implement {@link Discretization}.
	 */
    protected StochasticProcess(final LinearDiscretization discretization) {
    	super();
    	if (discretization==null) throw new NullPointerException(); // FIXME: message
    	this.discretization = discretization;
    }
    
    /**
     * Returns the number of dimensions of the stochastic process
     */
    public abstract int getSize();
    
    /**
     * Returns the number of independent factors of the process
     */
    public int factors() {
    	return getSize();
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
    	return apply(x0, discretization.driftDiscretization(this, t0, x0, dt)); //XXX
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
    	return discretization.diffusionDiscretization(this, t0, x0, dt); // XXX
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
    	return discretization.covarianceDiscretization(this, t0, x0, dt); // XXX
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
    	DenseDoubleMatrix2D m = new DenseDoubleMatrix2D(stdDeviation(t0, x0, dt));
    	DoubleMatrix1D y = m.zMult(new DenseDoubleMatrix1D(dw), null);
    	return apply(expectation(t0, x0, dt), y.toArray());
    }
    
    /**
     * Applies a change to the asset value. By default, it
     * returns {@latex$ \mathrm{x} + \Delta \mathrm{x} }.
     */
    public /*@Price*/ double[] apply(final double[] x0, final double[] dx) {
    	DoubleMatrix1D mx0 = new DenseDoubleMatrix1D(x0);
    	DoubleMatrix1D mdx = new DenseDoubleMatrix1D(dx);
    	return mx0.assign(mdx, Functions.plus).toArray();
    }

    /**
     * Returns the time value corresponding to the given date
     * in the reference system of the stochastic process.
     * 
     * @note As a number of processes might not need this 
     * functionality, a default implementation is given
     * which raises an exception.
     */
    public /*@Time*/ double getTime(final Date date) {
    	throw new UnsupportedOperationException("date/time conversion not supported");
    }
    
    
	//
	// implements Observer interface
	//
	
    public void update(Observable o, Object arg) {
    	notifyObservers();
    }

    
	//
	// implements Observable interface
	//
	
	/**
	 * Implements multiple inheritance via delegate pattern to an inner class
	 * 
	 * @see Observable
	 * @see DefaultObservable
	 */
    private Observable delegatedObservable = new DefaultObservable(this);

	public void addObserver(Observer observer) {
		delegatedObservable.addObserver(observer);
	}

	public int countObservers() {
		return delegatedObservable.countObservers();
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

	public void deleteObservers() {
		delegatedObservable.deleteObservers();
	}

	public List<Observer> getObservers() {
		return delegatedObservable.getObservers();
	}

}
