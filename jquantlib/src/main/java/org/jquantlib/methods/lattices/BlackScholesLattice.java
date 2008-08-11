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

import org.jquantlib.time.TimeGrid;

/**
 * @author Srinivas Hasti
 * 
 */
public class BlackScholesLattice<T extends Tree> extends TreeLattice1D {

	private T tree;
	private/* DiscountFactor */double discount;
	private/* Real */double pd;
	private/* Real */double pu;

	public BlackScholesLattice(T tree,
	/* Rate */double riskFreeRate, /* Time */double end, /* Size */int steps) {
		super(new TimeGrid(end, steps), 2);
		this.tree = tree;
		this.discount = Math.exp(-riskFreeRate * (end / steps));
		this.pd = tree.probability(0, 0, 0);
		this.pu = tree.probability(0, 0, 0);

	}

}
