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
public class Newton extends AbstractSolver1D<Derivative> {
	
	@Override
	protected double solveImpl(Derivative f, double xAccuracy) {
		
		double froot, dfroot, dx;

        froot = f.evaluate(root_);
        dfroot = f.derivative(root_);
        evaluationNumber_++;

        while (evaluationNumber_<= getMaxEvaluations()) {
            dx=froot/dfroot;
            root_ -= dx;
            // jumped out of brackets, switch to NewtonSafe
            if ((xMin_-root_)*(root_-xMax_) < 0.0) {
            	NewtonSafe s = new NewtonSafe();
                s.setMaxEvaluations(getMaxEvaluations()-evaluationNumber_);
                return s.solve(f, xAccuracy, root_+dx, xMin_, xMax_);
            }
            if (Math.abs(dx) < xAccuracy)
                return root_;
            froot = f.evaluate(root_);
            dfroot = f.derivative(root_);
            evaluationNumber_++;
        }
		throw new ArithmeticException("maximum number of function evaluations ("
                + getMaxEvaluations() + ") exceeded");
	}
	
	

}
