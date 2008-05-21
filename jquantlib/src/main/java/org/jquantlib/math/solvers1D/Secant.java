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
 * @author Dominik Holenstein
 */

/* The implementation of the algorithm was inspired by
Press, Teukolsky, Vetterling, and Flannery,
"Numerical Recipes in C", 2nd edition,
Cambridge University Press
*/

// TODO Secant.java: Add test case.
public class Secant extends AbstractSolver1D<UnaryFunctionDouble> {
	
	@Override
	protected double solveImpl(UnaryFunctionDouble f, double xAccuracy)  {

		double fl, froot, dx, xl;

        // Pick the bound with the smaller function value
        // as the most recent guess
        if (Math.abs(fxMin_) < Math.abs(fxMax_)) {
            root_=xMin_;
            froot=fxMin_;
            xl=xMax_;
            fl=fxMax_;
        } else {
            root_=xMax_;
            froot=fxMax_;
            xl=xMin_;
            fl=fxMin_;
        }
        while (evaluationNumber_<= getMaxEvaluations()) {
            dx=(xl-root_)*froot/(froot-fl);
            xl=root_;
            fl=froot;
            root_ += dx;
            froot=f.evaluate(root_);
            evaluationNumber_++;
            if (Math.abs(dx) < xAccuracy || froot == 0.0)
                return root_;
        }
        throw new ArithmeticException("maximum number of function evaluations ("
                + getMaxEvaluations() + ") exceeded");
    }
}
