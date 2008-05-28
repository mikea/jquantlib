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

package org.jquantlib.math.integrals;

import org.jquantlib.math.UnaryFunctionDouble;

// TODO: Add test case.
// TODO: Check the getNumberOfEvaluations() method.

public class SegmentIntegral implements Integrator{
	
	private int intervals_;
	
	public SegmentIntegral(int intervals) {
		if (intervals < 1) {
			throw new ArithmeticException("at least 1 interval needed, 0 given");
		}
		intervals_ = intervals;
	}
	
	public double integrate(UnaryFunctionDouble f, double a, double b) {
		double dx = (b-a)/getNumberOfEvaluations(); // getNumberOfEvaluations() returns intervals_
        double sum = 0.5*(f.evaluate(a)+f.evaluate(b));
        double end = b - 0.5*dx;
        for (double x = a+dx; x < end; x += dx)
            sum += f.evaluate(x);
        return sum*dx;
	}
	
	public int getNumberOfEvaluations() {
		return intervals_;
	}
}
