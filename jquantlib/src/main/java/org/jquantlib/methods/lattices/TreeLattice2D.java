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

import org.jquantlib.math.matrixutilities.Array;
import org.jquantlib.math.matrixutilities.Matrix;

/**
 * @author Srinivas Hasti
 * 
 */
public abstract class TreeLattice2D<T extends TrinomialTree> extends
		TreeLattice {
	private Matrix m;
	private double rho;

	protected T tree1, tree2;

	public TreeLattice2D(T tree1, T tree2, double correlation) {

		super(tree1.timeGrid(), T.branches.getValue() * T.branches.getValue());
		this.tree1 = tree1;
		this.tree2 = tree2;
		this.m = new Matrix(T.branches.getValue(), T.branches.getValue());
		rho = Math.abs(correlation);
		// what happens here?
		if (correlation < 0.0 && T.branches.getValue() == 3) {
			m.set(0, 0, -1.0);
			m.set(0, 1, -4.0);
			m.set(0, 2, 5.0);
			m.set(1, 0, -4.0);
			m.set(1, 1, 8.0);
			m.set(1, 2, -4.0);
			m.set(2, 0, 5.0);
			m.set(2, 1, -4.0);
			m.set(2, 2, -1.0);
		} else {
			m.set(0, 0, 5.0);
			m.set(0, 1, -4.0);
			m.set(0, 2, -1.0);
			m.set(1, 0, -4.0);
			m.set(1, 1, 8.0);
			m.set(1, 2, -4.0);
			m.set(2, 0, -1.0);
			m.set(2, 1, -4.0);
			m.set(2, 2, 5.0);
		}
	}

	// smelly
	public Array grid(double t) {
		throw new RuntimeException("not implemented");
	}

	public int size(int i) {
		return tree1.size(i) * tree2.size(i);
	}

	public int descendant(int i, int index, int branch) {
		int modulo = tree1.size(i);

		int index1 = index % modulo;
		int index2 = index / modulo;
		int branch1 = branch % T.branches.getValue();
		int branch2 = branch / T.branches.getValue();

		modulo = tree1.size(i + 1);
		return tree1.descendant(i, index1, branch1)
				+ tree2.descendant(i, index2, branch2) * modulo;
	}

	public double probability(int i, int index, int branch) {
		int modulo = tree1.size(i);

		int index1 = index % modulo;
		int index2 = index / modulo;
		int branch1 = branch % T.branches.getValue();
		int branch2 = branch / T.branches.getValue();

		double prob1 = tree1.probability(i, index1, branch1);
		double prob2 = tree2.probability(i, index2, branch2);
		// does the 36 below depend on T::branches?
		return prob1 * prob2 + rho * (m.get(branch1, branch2)) / 36.0;
	}

}
