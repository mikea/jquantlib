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

import org.jquantlib.processes.StochasticProcess1D;

/**
 * Joshi4 tree
 * 
 * @category lattices
 * 
 * @author Srinivas Hasti
 * @author Tim Swetonic
 */
public class Joshi4 extends BinomialTree {

	protected double up, down, pu, pd;

	public Joshi4(final StochasticProcess1D process, final /* @Time */double end, final int steps, final /* @Price */double strike) {
        super(process, end, ((steps % 2) > 0 ? steps : steps + 1));
        
        if (strike <= 0.0) throw new IllegalStateException("strike must be positive");

        int oddSteps = (steps % 2 > 0 ? steps : steps + 1);
        double variance = process.variance(0.0, x0, end);
        double ermqdt = Math.exp(driftPerStep + 0.5 * variance / oddSteps);
        double d2 = (Math.log(x0 / strike) + driftPerStep * oddSteps) / Math.sqrt(variance);
        pu = computeUpProb((oddSteps - 1.0) / 2.0, d2);
        pd = 1.0 - pu;
        double pdash = computeUpProb((oddSteps - 1.0) / 2.0, d2 + Math.sqrt(variance));
        up = ermqdt * pdash / pu;
        down = (ermqdt - pu * up) / (1.0 - pu);

    }

	public double underlying(int i, int index) {
        long j = (long) i - (long) index;
        double d = (double) j;
		return x0 * Math.pow(down, d) * Math.pow(up, index);

	}

	public double probability(int i, int j, int branch) {
		return (branch == 1 ? pu : pd);
	}

	protected double computeUpProb(/* Real */double k, /* Real */double dj) {
        double alpha = dj / (Math.sqrt(8.0));
        double alpha2 = alpha * alpha;
        double alpha3 = alpha * alpha2;
        double alpha5 = alpha3 * alpha2;
        double alpha7 = alpha5 * alpha2;
        double beta = -0.375 * alpha - alpha3;
        double gamma = (5.0 / 6.0) * alpha5 + (13.0 / 12.0) * alpha3 + (25.0 / 128.0) * alpha;
        double delta = -0.1025 * alpha - 0.9285 * alpha3 - 1.43 * alpha5 - 0.5 * alpha7;
        double p = 0.5;
        double rootk = Math.sqrt(k);
        p += alpha / rootk;
        p += beta / (k * rootk);
        p += gamma / (k * k * rootk);
        // delete next line to get results for j three tree
        p += delta / (k * k * k * rootk);
        return p;
    }

}
