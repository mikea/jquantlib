/*
 Copyright (C) 2008 Srinivas Hasti

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

/**
 * @author Srinivas Hasti
 * 
 */
//TODO:
public class TsiveriotisFernandesLattice<T extends Tree> extends
		BlackScholesLattice<T> {
	private double pd;
	private double pu;
	private double creditSpread;
	private double dt;
	private/* Rate */double riskFreeRate;

	public TsiveriotisFernandesLattice(T tree,
	/* Rate */double riskFreeRate,
	/* Time */double end, int steps,
	/* Real */double creditSpread,
	/* Volatility */double sigma, /* Spread */double divYield) {
		super(tree, riskFreeRate, end, steps);

		dt = end / steps;

		pd = tree.probability(0, 0, 0);
		pu = tree.probability(0, 0, 1);

		riskFreeRate = riskFreeRate;
		creditSpread = creditSpread;

		if (pu > 1.0)
			throw new IllegalStateException("negative probability");
		if (pu < 0.0)
			throw new IllegalStateException("negative probability");
	}
}
