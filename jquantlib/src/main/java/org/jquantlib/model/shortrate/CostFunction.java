/*
Copyright (C) 2008 Praneet Tiwari

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
package org.jquantlib.model.shortrate;

import org.jquantlib.math.Array;

/**
 * 
 * @author Praneet Tiwari
 */
public abstract class CostFunction {

    // ! minimize the optimization problem P
    // TODO: check if minimize function is +nt:TODO
    public abstract EndCriteria.Type minimize(Problem P, final EndCriteria endCriteria);

    // ! method to overload to compute the cost function values in x
    public abstract/* Disposable<Array> */Array values(final Array x);

    // ! method to overload to compute the cost function value in x
    public abstract Double /* @Real */value(final Array x);

    // ! method to overload to compute grad_f, the first derivative of
    // the cost function with respect to x
    public void gradient(Array grad, final Array x) {
        double /* @Real */eps = finiteDifferenceEpsilon(), fp, fm;
        Array xx = new Array(x);
        for (int /* @Size */i = 0; i < x.size(); i++) {
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

    // ! method to overload to compute grad_f, the first derivative of
    // the cost function with respect to x and also the cost function

    public double /* @Real */valueAndGradient(Array grad, final Array x) {
        gradient(grad, x);
        return value(x);
    }

    // ! Default epsilon for finite difference method :
    public double /* @Real */finiteDifferenceEpsilon() {
        return 1e-8;
    }
}
