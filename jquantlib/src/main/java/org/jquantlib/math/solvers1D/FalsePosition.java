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
import org.jquantlib.math.UnaryFunctionDouble;

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

//TODO FalsePosition: Add test case.
public class FalsePosition extends AbstractSolver1D {
	
	@Override
	protected double solveImpl(UnaryFunctionDouble f, double xAccuracy) {
		
		double fl, fh, xl, xh, dx, del, froot;

        // Identify the limits so that xl corresponds to the low side
        if (fxMin_ < 0.0) {
            xl=xMin_;
            fl = fxMin_;
            xh=xMax_;
            fh = fxMax_;
        } else {
            xl=xMax_;
            fl = fxMax_;
            xh=xMin_;
            fh = fxMin_;
        }
        dx=xh-xl;

        while (evaluationNumber_<= getMaxEvaluations()) {
            // Increment with respect to latest value
            root_=xl+dx*fl/(fl-fh);
            froot=f.evaluate(root_);
            evaluationNumber_++;
            if (froot < 0.0) {       // Replace appropriate limit
                del=xl-root_;
                xl=root_;
                fl=froot;
            } else {
                del=xh-root_;
                xh=root_;
                fh=froot;
            }
            dx=xh-xl;
            
            // Convergence criterion
            if (Math.abs(del) < xAccuracy || froot == 0.0)  {
                return root_;
            }
        }
		throw new ArithmeticException("maximum number of function evaluations ("
                + getMaxEvaluations() + ") exceeded");
	}
}
