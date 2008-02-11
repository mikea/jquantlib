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

package org.jquantlib.processes;


/**
 *  Discretization of a stochastic process over a given time interval
 *  
 * @author Richard Gomes
 */
public interface Discretization1D {

    /**
     * Returns the drift part of the equation, i.e. {@latex$ \mu(t, x_t) }
     */ 
	public abstract /*@Drift*/ double driftDiscretization(/*final StochasticProcess1D sp, */final /*@Time*/ double t0, final double x0, final /*@Time*/ double dt); // XXX
	
    /**
     * Returns the diffusion part of the equation, i.e.
     {@latex$ \sigma(t, x_t) }
     */
	public abstract /*@Diffusion*/ double diffusionDiscretization(/*final StochasticProcess1D sp, */final /*@Time*/ double t0, final double x0, final /*@Time*/ double dt); // XXX
	
    /**
     * Returns the variance
     {@latex$ V(x_{t_0 + \Delta t} | x_{t_0} = x_0) }
     * of the process after a time interval
     {@latex$ \Delta t }
     * according to the given discretization. This method can be
     * overridden in derived classes which want to hard-code a
     * particular discretization.
     */
	public abstract /*@Variance*/ double varianceDiscretization(/*final StochasticProcess1D sp, */final /*@Time*/ double t0, final double x0, final /*@Time*/ double dt); // XXX

}
