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

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import cern.jet.math.Mult;

/**
 * 
 * @author Richard Gomes
 */
public class EulerDiscretization implements LinearDiscretization {

    //
    // Implements interface Discretization
    //

    /**
     * Returns an approximation of the drift defined as
     * <p>
     * {@latex[ \mu(t_0, \mathbf{x}_0) \Delta t }
     */
    public/* @Drift */double[] driftDiscretization(final StochasticProcess sp, /* @Time */final double t0, 
            /* @Price */ final double[] x0, /* @Time */ final double dt) {
        return new DenseDoubleMatrix1D(sp.drift(t0, x0)).assign(Mult.mult(dt)).toArray();
    }

    /**
     * Returns an approximation of the diffusion defined as
     * <p>
     * {@latex[ \sigma(t_0, \mathbf{x}_0) \sqrt{\Delta t} }
     */
    public/* @Diffusion */double[][] diffusionDiscretization(final StochasticProcess sp, /* @Time */final double t0, 
            /* @Price */ final double[] x0, /* @Time */final double dt) {
        return new DenseDoubleMatrix2D(sp.diffusion(t0, x0)).assign(Mult.mult(Math.sqrt(dt))).toArray();
    }

    /**
     * Returns an approximation of the covariance defined as
     * <p>
     * {@latex[ \sigma(t_0, \mathbf{x}_0)^2 \Delta t }
     */
    public/* @Covariance */double[][] covarianceDiscretization(final StochasticProcess sp, /* @Time */final double t0, 
            /* @Price */ final double[] x0, /* @Time */final double dt) {

        final DoubleMatrix2D sigma = new DenseDoubleMatrix2D(sp.diffusion(t0, x0));
        final DoubleMatrix2D sigmaT = new Algebra().transpose(sigma);
        return sigma.zMult(sigmaT, null, dt, 0.0, false, false).toArray();
    }

    //
    // Implements interface Discretization1D
    //

    /**
     * Returns an approximation of the drift defined as
     * <p>
     * {@latex[ \mu(t_0, x_0) \Delta t }
     */
    public/* @Drift */double driftDiscretization(final StochasticProcess1D sp, /* @Time */final double t0, 
            /* @Price */ final double x0, /* @Time */final double dt) {
        return sp.drift(t0, x0) * dt;
    }

    /**
     * Returns an approximation of the diffusion defined as
     * <p>
     * {@latex[ \sigma(t_0, x_0) \sqrt{\Delta t} }
     */
    public/* @Diffusion */double diffusionDiscretization(final StochasticProcess1D sp, /* @Time */final double t0, 
            /* @Price */ final double x0, /* @Time */final double dt) {
        return sp.diffusion(t0, x0) * Math.sqrt(dt);
    }

    /**
     * Returns an approximation of the variance defined as
     * <p>
     * {@latex[ \sigma(t_0, x_0)^2 \Delta t }
     */
    public/* @Variance */double varianceDiscretization(final StochasticProcess1D sp, /* @Time */final double t0, 
            /* @Price */ final double x0, /* @Time */final double dt) {
        /* @Diffusion */final double sigma = sp.diffusion(t0, x0);
        return sigma * sigma * dt;
    }

}
