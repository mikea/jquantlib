/*
 Copyright (C) 2009 Ueli Hofstetter

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
 Copyright (C) 2001, 2002, 2003 Nicolas Di Cesare

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

package org.jquantlib.math.optimization;

import org.jquantlib.math.matrixutilities.Array;

public abstract class CostFunction {

    /**
     * Method to overload to compute the cost function values in x
     *
     * @param x
     * @return
     */
    public abstract/* Disposable<Array> */Array values(final Array x);

    /**
     * Method to overload to compute the cost function value in x
     * @param x
     * @return
     */
    public abstract double /* @Real */value(final Array x);

    /**
     * Method to overload to compute grad_f, the first derivative of
     * the cost function with respect to x
     *
     * @param grad
     * @param x
     */
    public void gradient(final Array grad, final Array x) {
        final double /* @Real */eps = finiteDifferenceEpsilon();
        double /* @Real */ fp, fm;
        // TODO: code review :: use of clone()
        final Array xx = x.clone();
        for (int /* @Size */i=0; i<x.size(); i++) {
            // xx[i] += eps;
            xx.set(i, eps + xx.get(i));
            fp = value(xx);
            // xx[i] -= 2.0*eps;
            xx.set(i, xx.get(i) - 2.0 * eps);
            fm = value(xx);
            // grad[i] = 0.5*(fp - fm)/eps;
            grad.set(i, 0.5 * (fp - fm) / eps);
            // xx[i] = x[i];
            xx.set(i, x.get(i));
        }
    }

    /**
     * Method to overload to compute grad_f, the first derivative of
     * the cost function with respect to x and also the cost function
     *
     * @param grad
     * @param x
     * @return
     */
    public double /* @Real */valueAndGradient(final Array grad, final Array x) {
        gradient(grad, x);
        return value(x);
    }

    /**
     *  Default epsilon for finite difference method :
     * @return
     */
    public double /* @Real */finiteDifferenceEpsilon() {
        return 1e-8;
    }
}

