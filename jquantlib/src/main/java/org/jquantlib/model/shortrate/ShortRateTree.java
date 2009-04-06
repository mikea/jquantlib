/*
Copyright (C) 2008 Praneet Tiwari

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
package org.jquantlib.model.shortrate;

import org.jquantlib.methods.lattices.TreeLattice1D;
import org.jquantlib.methods.lattices.TrinomialTree;
import org.jquantlib.time.TimeGrid;

/**
 * 
 * @author Praneet Tiwari
 */

// ! Recombining trinomial tree discretizing the state variable
public class ShortRateTree extends TreeLattice1D {

    private TrinomialTree tree_;
    private ShortRateDynamics dynamics_;

    // ! Plain tree build-up from short-rate dynamics
    public ShortRateTree(TrinomialTree tree, ShortRateDynamics dynamics, final TimeGrid timeGrid) {
        // why 1 here?
        super(timeGrid, tree.size(1));
        this.tree_ = tree;
        this.dynamics_ = dynamics;
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
    }

    // ! Tree build-up + numerical fitting to term-structure

    public ShortRateTree(final TrinomialTree tree, final ShortRateDynamics dynamics,
            final TermStructureFittingParameter.NumericalImpl theta,
            // int n,
            final TimeGrid timeGrid) {
        // why 1 here?
        super(timeGrid, tree.size(1));
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
        this.tree_ = tree;
        this.dynamics_ = dynamics;
        theta.reset();

        Double /* @Real */value = 1.0;
        Double /* @Real */vMin = -100.0;
        Double /* @Real */vMax = 100.0;

        for (int /* @Size */i = 0; i < (timeGrid.size() - 1); i++) {
            // TODO: Complete the for loop
            // Double /*@Real*/ discountBond = theta.termStructure().discount(t_.[i+1]);

            // Double /*@Real*/ discountBond = theta.termStructure().;
            /*
             * Helper finder(i, discountBond, theta, *this); Brent s1d; s1d.setMaxEvaluations(1000); value = s1d.solve(finder, 1e-7,
             * value, vMin, vMax); // vMin = value - 1.0; // vMax = value + 1.0; theta->change(value);
             */
        }
    }

    public int /* @Size */size(int /* @Size */i) {
        return tree_.size(i);
    }

    @Override
    public double /* @DiscountFactor */discount(int /* @Size */i, int /* @Size */index) {
        Double /* @Real */x = tree_.underlying(i, index);
        Double /* @Real */r = dynamics_.shortRate(timeGrid().at(i), x);
        // TimeGrid has operator overloading, defined as
        // Time operator[](Size i) const { return times_[i]; }
        return Math.exp(-r * timeGrid().dt(i));
    }

    @Override
    public double /* @Real */underlying(int /* @Size */i, int /* @Size */index) {
        return tree_.underlying(i, index);
    }

    @Override
    public int /* @Size */descendant(int /* @Size */i, int /* @Size */index, int /* @Size */branch) {
        return tree_.descendant(i, index, branch);
    }

    @Override
    public double /* @Real */probability(int /* @Size */i, int /* @Size */index, int /* @Size */branch) {
        return tree_.probability(i, index, branch);
    }
}
