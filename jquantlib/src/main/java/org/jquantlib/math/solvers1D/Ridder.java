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
import org.jquantlib.math.Constants;

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

// TODO Ridder.java: Add Test case.
public class Ridder extends AbstractSolver1D<UnaryFunctionDouble> {
	
	@Override
	protected double solveImpl(UnaryFunctionDouble f, double xAcc){

        double fxMid, froot, s, xMid, nextRoot;

        // test on Black-Scholes implied volatility show that
        // Ridder solver algorithm actually provides an
        // accuracy 100 times below promised
        double xAccuracy = xAcc/100.0;

        // Any highly unlikely value, to simplify logic below
        root_ = Constants.QL_MIN_POSITIVE_REAL;

        while (evaluationNumber_<= getMaxEvaluations()) {
            xMid=0.5*(xMin_+xMax_);
            // First of two function evaluations per iteraton
            fxMid=f.evaluate(xMid);
            evaluationNumber_++;
            s = Math.sqrt(fxMid*fxMid-fxMin_*fxMax_);
            if (s == 0.0)
                return root_;
            // Updating formula
            nextRoot = xMid + (xMid - xMin_) *
                ((fxMin_ >= fxMax_ ? 1.0 : -1.0) * fxMid / s);
            if (Math.abs(nextRoot-root_) <= xAccuracy)
                return root_;

            root_=nextRoot;
            // Second of two function evaluations per iteration
            froot=f.evaluate(root_);
            evaluationNumber_++;
            if (froot == 0.0)
                return root_;

            // Bookkeeping to keep the root bracketed on next iteration
            if (sign(fxMid,froot) != fxMid) {
                xMin_=xMid;
                fxMin_=fxMid;
                xMax_=root_;
                fxMax_=froot;
            } else if (sign(fxMin_,froot) != fxMin_) {
                xMax_=root_;
                fxMax_=froot;
            } else if (sign(fxMax_,froot) != fxMax_) {
                xMin_=root_;
                fxMin_=froot;
            } else {
                throw new ArithmeticException("never get here.");
            }

            if (Math.abs(xMax_-xMin_) <= xAccuracy) return root_;
        }
        throw new ArithmeticException("maximum number of function evaluations ("
                + getMaxEvaluations() + ") exceeded");
    }
      
    private double sign(double a, double b) {
        return b >= 0.0 ? Math.abs(a) : -Math.abs(a);
    }
}
