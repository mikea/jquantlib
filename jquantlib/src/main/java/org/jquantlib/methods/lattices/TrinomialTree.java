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

import java.util.Vector;

import org.jquantlib.processes.StochasticProcess1D;
import org.jquantlib.time.TimeGrid;

public class TrinomialTree extends Tree {

	public static final Branches branches = Branches.TRINOMIAL;

	protected Vector<Branching> branchings_ = new Vector<Branching>();
	protected double x0_;
	protected Vector<Double> dx_ = new Vector<Double>();
	protected TimeGrid timeGrid_;
	
	public TrinomialTree(final StochasticProcess1D process, final TimeGrid timeGrid) {
	    this(process, timeGrid, false);
	}
	
	public TrinomialTree(final StochasticProcess1D process, final TimeGrid timeGrid, boolean isPositive) {
		super(timeGrid.size());
		dx_.add(new Double(1));
		dx_.add(new Double(0.0));
		timeGrid_ = timeGrid;
		x0_ = process.x0();

		int nTimeSteps = timeGrid.size() - 1;
		Integer jMin = 0;
		Integer jMax = 0;

		for (int i = 0; i < nTimeSteps; i++) {
			double t = timeGrid.at(i);
			double dt = timeGrid.dt(i);

			// Variance must be independent of x
			double v2 = process.variance(t, 0.0, dt);
			/* Volatility */double v = Math.sqrt(v2);
			dx_.add(v * Math.sqrt(3.0));

			Branching branching = new Branching();
			for (int j = jMin; j <= jMax; j++) {
				double x = x0_ + j * dx_.get(i);
				double m = process.expectation(t, x, dt);
				int temp = (int) Math.floor((m - x0_) / dx_.get(i + 1) + 0.5);

				if (isPositive) {
					while (x0_ + (temp - 1) * dx_.get(i + 1) <= 0) {
						temp++;
					}
				}

				double e = m - (x0_ + temp * dx_.get(i + 1));
				double e2 = e * e;
				double e3 = e * Math.sqrt(3.0);

				double p1 = (1.0 + e2 / v2 - e3 / v) / 6.0;
				double p2 = (2.0 - e2 / v2) / 3.0;
				double p3 = (1.0 + e2 / v2 + e3 / v) / 6.0;

				branching.add(temp, p1, p2, p3);
			}
			branchings_.add(branching);

			jMin = branching.jMin();
			jMax = branching.jMax();
		}

	}

	public double dx(int i) {
		return dx_.get(i).doubleValue();
	}

	public TimeGrid timeGrid() {
		return timeGrid_;
	}

	@Override
	public int size(int i) {
		return i == 0 ? 1 : branchings_.get(i - 1).size();
	}

	@Override
	public double underlying(int i, int index) {
		if (i == 0)
			return x0_;
		else
			return x0_ + (branchings_.get(i - 1).jMin() + (double) (index))
					* dx(i);
	}

	@Override
	public int descendant(int i, int index, int branch) {
		return branchings_.get(i).descendant(index, branch);
	}

	@Override
	public double probability(int i, int index, int branch) {
		return branchings_.get(i).probability(index, branch);
	}

	private static class Branching {

		private Vector<Integer> k_ = new Vector<Integer>();
		private Vector<Vector<Double>> probs_ = new Vector<Vector<Double>>(3);
		private int kMin_, jMin_, kMax_, jMax_;

		public Branching() {
			kMin_ = Integer.MAX_VALUE;
			jMin_ = Integer.MAX_VALUE;
			kMax_ = Integer.MIN_VALUE;
			jMax_ = Integer.MIN_VALUE;
		}

		public int descendant(int index, int branch) {
			return k_.elementAt(index) - jMin_ - 1 + branch;
		}

		public double probability(int index, int branch) {
			return probs_.elementAt(branch).elementAt(index);
		}

		public int size() {
			return jMax_ - jMin_ + 1;
		}

		public int jMin() {
			return jMin_;
		}

		public int jMax() {
			return jMax_;
		}

		public void add(int k, double p1, double p2, double p3) {
			// store
			k_.add(k);
			Vector<Double> v1 = new Vector<Double>();
			Vector<Double> v2 = new Vector<Double>();
			Vector<Double> v3 = new Vector<Double>();
			v1.add(new Double(p1));
			v2.add(new Double(p2));
			v3.add(new Double(p3));

			probs_.add(v1);
			probs_.add(v2);
			probs_.add(v3);

			// maintain invariants
			kMin_ = Math.min(kMin_, k);
			jMin_ = kMin_ - 1;
			kMax_ = Math.max(kMax_, k);
			jMax_ = kMax_ + 1;

		}

	}

}
