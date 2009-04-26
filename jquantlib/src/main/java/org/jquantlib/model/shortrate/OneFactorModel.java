/*
Copyright (C) 
2008 Praneet Tiwari
2009 Ueli Hofstetter

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

import org.jquantlib.math.Array;
import org.jquantlib.math.UnaryFunctionDouble;
import org.jquantlib.math.solvers1D.Brent;
import org.jquantlib.methods.lattices.Lattice;
import org.jquantlib.methods.lattices.TreeLattice1D;
import org.jquantlib.methods.lattices.TrinomialTree;
import org.jquantlib.processes.StochasticProcess1D;
import org.jquantlib.time.TimeGrid;

/**
 * 
 * @author Praneet Tiwari
 */
public abstract class OneFactorModel extends ShortRateModel {

    public OneFactorModel(int /* @Size */nArguments) {
        super(nArguments);
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
    }

    // ! returns the short-rate dynamics
    public abstract ShortRateDynamics dynamics();

    // ! Return by default a trinomial recombining tree
    public  Lattice tree(final TimeGrid grid){
        TrinomialTree trinominal = new TrinomialTree(dynamics().process(), grid);
        return new ShortRateTree(trinominal, dynamics(), grid);
        
    }
    
    public abstract class ShortRateDynamics{
        private StochasticProcess1D process_;

        public ShortRateDynamics(final StochasticProcess1D process) {
            this.process_ = process;
        }

        // ! Compute state variable from short rate
        public abstract double /* @Real */variable(double /* @Time */t, double /* @Rate */r);

        // ! Compute short rate from state variable
        public abstract double /* @Rate */shortRate(double /* @Time */t, double /* @Real */variable);

        // ! Returns the risk-neutral dynamics of the state variable
        public StochasticProcess1D process() {
            return process_;
        }
        
    }
    
    //TODO: generic type?
    //! Recombining trinomial tree discretizing the state variable
    class ShortRateTree extends TreeLattice1D{
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
        public ShortRateTree(final TrinomialTree tree, 
                final ShortRateDynamics dynamics,
                final TermStructureFittingParameter.NumericalImpl theta,
                final TimeGrid timeGrid) {
            super(timeGrid, tree.size(1));
            if (System.getProperty("EXPERIMENTAL") == null) {
                throw new UnsupportedOperationException("Work in progress");
            }
            this.tree_ = tree;
            this.dynamics_ = dynamics;
            theta.reset();

            double /* @Real */value = 1.0;
            double /* @Real */vMin = -100.0;
            double /* @Real */vMax = 100.0;

            for (int /* @Size */i = 0; i < (timeGrid.size() - 1); i++) {
                 double /*@Real*/ discountBond = theta.termStructure().getLink().discount(t.get(i+1));
                 Helper finder = new Helper(i, discountBond, theta, this);
                 Brent s1d = new Brent();
                 s1d.setMaxEvaluations(1000);
                 value = s1d.solve(finder, 1e-7, value, vMin, vMax);
                 // vMin = value - 1.0;
                 // vMax = value + 1.0;
                 theta.change(value);
            }
        }

        public int /* @Size */size(int /* @Size */i) {
            return tree_.size(i);
        }

        @Override
        public double /* @DiscountFactor */discount(int /* @Size */i, int /* @Size */index) {
            double /* @Real */x = tree_.underlying(i, index);
            double /* @Real */r = dynamics_.shortRate(timeGrid().get(i), x);
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
        
        class Helper implements UnaryFunctionDouble{
           
            private int size_;
            private int i_;
            private Array statePrices_;
            private double discountBondPrice_;
            private TermStructureFittingParameter.NumericalImpl theta_;
            private ShortRateTree tree_;
           
           public Helper(int i, double discountBondPrice, TermStructureFittingParameter.NumericalImpl theta,
                   ShortRateTree tree){
               this.size_ = tree.size(i);
               this.i_ = i;
               this.statePrices_ = tree.statePrices(i);
               this.discountBondPrice_ = discountBondPrice;
               this.theta_ = theta;
               this.tree_ = tree;
               //argh.... FIXME: either vice versa or bad design ?
               theta_.set(new Double(tree.timeGrid().get(i)).intValue(), 0.0);
           }
           
           public double evaluate(double theta){
               double value = discountBondPrice_;
               theta_.change(theta);
               for(int j=0; j<size_; j++){
                   value -= statePrices_.get(j)*tree_.discount(i_, j);
               }
               return value;
           }
        }
    }
}
