/*
 Copyright (C) 
 2009 Ueli Hofstetter

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
package org.jquantlib.math.integrals;

import org.jquantlib.math.UnaryFunctionDouble;

//! Integral of a 1-dimensional function using the Gauss-Kronrod methods
/*! This class provide a non-adaptive integration procedure which
    uses fixed Gauss-Kronrod abscissae to sample the integrand at
    a maximum of 87 points.  It is provided for fast integration
    of smooth functions.

    This function applies the Gauss-Kronrod 10-point, 21-point, 43-point
    and 87-point integration rules in succession until an estimate of the
    integral of f over (a, b) is achieved within the desired absolute and
    relative error limits, epsabs and epsrel. The function returns the
    final approximation, result, an estimate of the absolute error,
    abserr and the number of function evaluations used, neval. The
    Gauss-Kronrod rules are designed in such a way that each rule uses
    all the results of its predecessors, in order to minimize the total
    number of function evaluations.
*/
public class GaussKronrodAdaptive extends KronrodIntegral {
    
 // weights for 7-point Gauss-Legendre integration
    // (only 4 values out of 7 are given as they are symmetric)
    static final double g7w[] = { 0.417959183673469,
                                0.381830050505119,
                                0.279705391489277,
                                0.129484966168870 };
    // weights for 15-point Gauss-Kronrod integration
    static final double k15w[] = { 0.209482141084728,
                                 0.204432940075298,
                                 0.190350578064785,
                                 0.169004726639267,
                                 0.140653259715525,
                                 0.104790010322250,
                                 0.063092092629979,
                                 0.022935322010529 };
    // abscissae (evaluation points)
    // for 15-point Gauss-Kronrod integration
    static final double k15t[] = { 0.000000000000000,
                                 0.207784955007898,
                                 0.405845151377397,
                                 0.586087235467691,
                                 0.741531185599394,
                                 0.864864423359769,
                                 0.949107912342758,
                                 0.991455371120813 };

    public GaussKronrodAdaptive(double absoluteAccuracy, int maxEvaluations) {
        super(absoluteAccuracy, maxEvaluations);
        if (maxEvaluations < 15) {
            throw new IllegalArgumentException("required maxEvaluations (" + maxEvaluations + ") not allowed. It must be >= 15");
        }
    }

    @Override
    protected double integrate(UnaryFunctionDouble f, double a, double b) {
        return integrateRecursively(f, a, b, getAbsoluteAccuracy());
    }

    private double integrateRecursively(UnaryFunctionDouble f, double a, double b, double tolerance) {

        double halflength = (b - a) / 2;
        double center = (a + b) / 2;

        double g7; // will be result of G7 integral
        double k15; // will be result of K15 integral

        double t, fsum; // t (abscissa) and f(t)
        double fc = f.evaluate(center);
        g7 = fc * g7w[0];
        k15 = fc * k15w[0];

        // calculate g7 and half of k15
        int j, j2;
        for (j = 1, j2 = 2; j < 4; j++, j2 += 2) {
            t = halflength * k15t[j2];
            fsum = f.evaluate(center - t) + f.evaluate(center + t);
            g7 += fsum * g7w[j];
            k15 += fsum * k15w[j2];
        }

        // calculate other half of k15
        for (j2 = 1; j2 < 8; j2 += 2) {
            t = halflength * k15t[j2];
            fsum = f.evaluate(center - t) + f.evaluate(center + t);
            k15 += fsum * k15w[j2];
        }

        // multiply by (a - b) / 2
        g7 = halflength * g7;
        k15 = halflength * k15;

        // 15 more function evaluations have been used
        increaseNumberOfEvaluations(15);

        // error is <= k15 - g7
        // if error is larger than tolerance then split the interval
        // in two and integrate recursively
        if (Math.abs(k15 - g7) < tolerance) {
            return k15;
        } else {
            if (getNumberOfEvaluations() + 30 > getMaxEvaluations()) {
                throw new IllegalArgumentException("maximum number of function evaluations exceeded");
            }
            return integrateRecursively(f, a, center, tolerance / 2) + integrateRecursively(f, center, b, tolerance / 2);
        }
    }
}




 