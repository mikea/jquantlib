/*
 Copyright (C) 2008 Srinivas Hasti
 Copyright (C) 2008 Tim Swetonic

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
package org.jquantlib.methods.lattices;

import org.jquantlib.lang.annotation.NonNegative;
import org.jquantlib.lang.annotation.Price;
import org.jquantlib.lang.annotation.Time;
import org.jquantlib.processes.StochasticProcess1D;

/**
 * Leisen & Reimer tree: multiplicative approach
 * 
 * @category lattices
 * 
 * @author Srinivas Hasti
 * @author Tim Swetonic
 */
public class LeisenReimer extends BinomialTree {

	protected double up, down, pu, pd;

	public LeisenReimer(final StochasticProcess1D process, @Time final double end, @NonNegative final int steps, @Price final double strike) {
	    super(process, end, steps);
	    
        if (strike <= 0.0) throw new IllegalArgumentException("strike must be positive");

        int oddSteps = (steps % 2 > 0 ? steps : steps + 1);
        double variance = process.variance(0.0, x0, end);
        double ermqdt = Math.exp(driftPerStep + 0.5 * variance / oddSteps);
        double d2 = (Math.log(x0 / strike) + driftPerStep * oddSteps) / Math.sqrt(variance);
        pu = PeizerPrattMethod2Inversion(d2, oddSteps);
        pd = 1.0 - pu;
        double pdash = PeizerPrattMethod2Inversion(d2 + Math.sqrt(variance), oddSteps);
        up = ermqdt * pdash / pu;
        down = (ermqdt - pu * up) / (1.0 - pu);
    }

	public double underlying(int i, int index) {
        long j = (long) i - (long) index;
        double d = (double) j;
        return x0 * Math.pow(down, d) * Math.pow(up, (index));
    }

	public double probability(int n, int m, int branch) {
		return (branch == 1 ? pu : pd);
	}

	/**
	 * Given an odd integer n and a real number z it returns p such that:
	 * <pre>
	 * 1 - CumulativeBinomialDistribution((n-1)/2, n, p) = CumulativeNormalDistribution(z)
	 * </pre>
	 * where n must be odd
	 */
	private double PeizerPrattMethod2Inversion(double z, int n) {

		if (! (n % 2 != 0) ) throw new IllegalArgumentException("n must be an odd number");

		double result = (z / (n + 1.0 / 3.0 + 0.1 / (n + 1.0)));
		result *= result;
		result = Math.exp(-result * (n + 1.0 / 6.0));
		result = 0.5 + (z > 0 ? 1 : -1) * Math.sqrt((0.25 * (1.0 - result)));
		return result;
	}

}
