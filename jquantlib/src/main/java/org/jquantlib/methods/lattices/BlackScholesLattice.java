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

import org.jquantlib.math.Array;
import org.jquantlib.time.TimeGrid;

/**
 * @author Srinivas Hasti
 * 
 */
public class BlackScholesLattice<T extends Tree> extends TreeLattice1D {

	private T tree;
	private double discount;
	private double pd;
	private double pu;

	public BlackScholesLattice(T tree, double riskFreeRate, double end,
			int steps) {
		super(new TimeGrid(end, steps), 2);
		this.tree = tree;
		this.discount = Math.exp(-riskFreeRate * (end / steps));
		this.pd = tree.probability(0, 0, 0);
		this.pu = tree.probability(0, 0, 0);
	}

	public int size(int i) {
		return tree.size(i);
	}

	public double discount(int a, int b) {
		return discount;
	}

	public double underlying(int i, int index) {
		return tree.underlying(i, index);
	}

	public int descendant(int i, int index, int branch) {
		return tree.descendant(i, index, branch);
	}

	public double probability(int i, int index, int branch) {
		return tree.probability(i, index, branch);
	}

	public void stepback(int i, Array values, Array newValues) {
		for (int j = 0; j < size(i); j++)
			newValues.set(j, (pd * values.get(j) + pu * values.get(j + 1))
					* discount);
	}
}
