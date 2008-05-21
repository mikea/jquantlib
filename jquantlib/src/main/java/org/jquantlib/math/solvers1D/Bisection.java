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

/**
 * @author Dominik Holenstein
 */

//TODO Bisection: Add test case.
import org.jquantlib.math.AbstractSolver1D;
import org.jquantlib.math.UnaryFunctionDouble;

	/*
	 * 	Bisection 1-D solver
    	test the correctness of the returned values is tested by
  	 checking them against known good results.
	 */

	/* The implementation of the algorithm was inspired by
	Press, Teukolsky, Vetterling, and Flannery,
	"Numerical Recipes in C", 2nd edition, Cambridge
	University Press
	 */

public class Bisection extends AbstractSolver1D<UnaryFunctionDouble>  {
	
	@Override
	protected double solveImpl(UnaryFunctionDouble f, double xAccuracy) {
		double dx, xMid, fMid;

        // Orient the search so that f>0 lies at root_+dx
        if (fxMin_ < 0.0) {
            dx = xMax_-xMin_;
            root_ = xMin_;
        } else {
            dx = xMin_-xMax_;
            root_ = xMax_;
        }

        while (evaluationNumber_<= getMaxEvaluations()) {
            dx /= 2.0;
            xMid=root_+dx;
            fMid=f.evaluate(xMid);
            evaluationNumber_++;
            if (fMid <= 0.0)
                root_=xMid;
            if (Math.abs(dx) < xAccuracy || fMid == 0.0) {
                return root_;
            }
        }
        throw new ArithmeticException("maximum number of function evaluations ("
                + getMaxEvaluations() + ") exceeded");
	}
}
