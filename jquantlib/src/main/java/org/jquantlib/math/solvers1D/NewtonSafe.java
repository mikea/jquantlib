/*
 Copyright (C) 2008 Richard Gomes

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

package org.jquantlib.math.solvers1D;

import org.jquantlib.math.AbstractSolver1D;
import org.jquantlib.math.distributions.Derivative;

/**
 * 
 * @author Dominik Holenstein
 *
 */

/* The implementation of the algorithm was inspired by
Press, Teukolsky, Vetterling, and Flannery,
"Numerical Recipes in C", 2nd edition,
Cambridge University Press
*/
public class NewtonSafe extends AbstractSolver1D<Derivative> {
	
	@Override
	protected double solveImpl(Derivative f, double xAccuracy) {

        double froot, dfroot, dx, dxold;
        double xh, xl;
        
        // Orient the search so that f(xl) < 0
        if (fxMin_ < 0.0) {
            xl=xMin_;
            xh=xMax_;
        } else {
            xh=xMin_;
            xl=xMax_;
        }

        // the "stepsize before last"
        dxold=xMax_-xMin_;
        // it was dxold=std::fabs(xMax_-xMin_); in Numerical Recipes
        // here (xMax_-xMin_ > 0) is verified in the constructor

        // and the last step
        dx=dxold;

        froot = f.evaluate(root_);
        dfroot = f.derivative(root_);
        evaluationNumber_++;

        while (evaluationNumber_<= getMaxEvaluations()) {
            // Bisect if (out of range || not decreasing fast enough)
            if ((((root_-xh)*dfroot-froot)*
                 ((root_-xl)*dfroot-froot) > 0.0)
                || (Math.abs(2.0*froot) > Math.abs(dxold*dfroot))) {

                dxold = dx;
                dx = (xh-xl)/2.0;
                root_=xl+dx;
            } else {
                dxold=dx;
                dx=froot/dfroot;
                root_ -= dx;
            }
            // Convergence criterion
            if (Math.abs(dx) < xAccuracy)
                return root_;
            
            froot = f.evaluate(root_);
            dfroot = f.derivative(root_);
            evaluationNumber_++;
            
            if (froot < 0.0)
                xl=root_;
            else
                xh=root_;
        }
        throw new ArithmeticException("maximum number of function evaluations ("
                + getMaxEvaluations() + ") exceeded");
    }
}
