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
package org.jquantlib.model.shortrate.onefactormodels;

import java.util.List;
import org.jquantlib.math.Array;
import org.jquantlib.math.optimization.PositiveConstraint;
import org.jquantlib.math.solvers1D.Brent;
import org.jquantlib.methods.lattices.Lattice;
import org.jquantlib.methods.lattices.TrinomialTree;
import org.jquantlib.model.shortrate.ConstantParameter;
import org.jquantlib.model.shortrate.OneFactorModel;
import org.jquantlib.model.shortrate.Parameter;
import org.jquantlib.model.shortrate.ShortRateDynamics;
import org.jquantlib.model.shortrate.ShortRateTree;
import org.jquantlib.model.shortrate.TermStructureFittingParameter;
import org.jquantlib.model.shortrate.processes.OrnsteinUhlenbeckProcess;
import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.TimeGrid;
import org.jquantlib.util.Observer;

/**
 * 
 * @author Praneet Tiwari
 */
// ! Standard Black-Karasinski model class.
/*
 * ! This class implements the standard Black-Karasinski model defined by \f[ d\ln r_t = (\theta(t) - \alpha \ln r_t)dt + \sigma
 * dW_t, \f] where \f$ alpha \f$ and \f$ sigma \f$ are constants.
 * 
 * \ingroup shortrate
 */
public class BlackKarasinski extends OneFactorModel {
    // need permanent solution for this one
    

    public static double QL_EPSILON = 1e-10;
    TermStructureConsistentModelClass termStructureConsistentModelClass;

    public Double /* @Real */a() {
        return a_.getOperatorEq(0.0);
    }

    public Double /* @Real */sigma() {
        return sigma_.getOperatorEq(0.0);
    }

    Parameter a_;
    Parameter sigma_;

    // all cosmetic methods

    @Override
    public void addObserver(Observer observer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int countObservers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Observer> getObservers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteObserver(Observer observer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteObservers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void notifyObservers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void notifyObservers(Object arg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // inner class
    // Private function used by solver to determine time-dependent parameter
    class Helper {

        private int /* @Size */size_;
        private Double /* @Time */dt_;
        private Double /* @Real */xMin_, dx_;
        private Array statePrices_;
        private Double /* @Real */discountBondPrice_;

        public Helper(int /* @Size */i, Double /* @Real */xMin, Double /* @Real */dx, Double /* @Real */discountBondPrice,
                ShortRateTree tree) {
            size_ = (tree.size(i));
            dt_ = (tree.timeGrid().dt(i));
            xMin_ = (xMin);
            dx_ = (dx);
            statePrices_ = (tree.statePrices(i));
            discountBondPrice_ = (discountBondPrice);
        }

        public Double /* @Real */getOperatorEq /* () */(Double /* @Real */theta) {
            Double /* @Real */value = discountBondPrice_;
            Double /* @Real */x = xMin_;
            for (int /* @Size */j = 0; j < size_; j++) {
                Double /* @Real */discount = Math.exp(-Math.exp(theta + x) * dt_);
                value -= statePrices_.get(j)/* [j] */* discount;
                x += dx_;
            }
            return value;
        }
    } // helper

    public BlackKarasinski(final Handle<YieldTermStructure> termStructure, Double /* @Real */a, Double /* @Real */sigma) {
        super(2);
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
        termStructureConsistentModelClass = new TermStructureConsistentModelClass(termStructure);
        a_ = (arguments_.get(0) /* [0] */);
        sigma_ = (arguments_.get(1) /* [1] */);
        a_ = new ConstantParameter(a, new PositiveConstraint());
        sigma_ = new ConstantParameter(sigma, new PositiveConstraint());

        // registerWith(termStructure);
    }

    @Override
    public ShortRateDynamics dynamics() {
        // QL_FAIL("no defined process for Black-Karasinski");
        throw new RuntimeException("no defined process for Black-Karasinski");
    }

    @Override
    public Lattice tree(final TimeGrid grid) {

        // TermStructureFittingParameter phi(termStructure());
        TermStructureFittingParameter phi = new TermStructureFittingParameter(termStructureConsistentModelClass.termStructure());
        // needed to activate the above constructor
        // boost::shared_ptr<ShortRateDynamics> numericDynamics(
        // new Dynamics(phi, a(), sigma()));

        // boost::shared_ptr<TrinomialTree> trinomial(
        // new TrinomialTree(numericDynamics->process(), grid));
        // boost::shared_ptr<ShortRateTree> numericTree(
        // new ShortRateTree(trinomial, numericDynamics, grid));
        ShortRateDynamics numericDynamics = (new Dynamics(phi, a(), sigma()));
        TrinomialTree trinomial = new TrinomialTree(numericDynamics.process(), grid, true);
        ShortRateTree numericTree = new ShortRateTree(trinomial, numericDynamics, grid);
        // typedef TermStructureFittingParameter::NumericalImpl NumericalImpl;
        // boost::shared_ptr<NumericalImpl> impl =
        // boost::dynamic_pointer_cast<NumericalImpl>(phi.implementation());
        TermStructureFittingParameter.NumericalImpl impl = (TermStructureFittingParameter.NumericalImpl) (phi.getImplementation());
        impl.reset();
        Double /* @Real */value = 1.0;
        Double /* @Real */vMin = -50.0;
        Double /* @Real */vMax = 50.0;
        // for (Size i=0; i<(grid.size() - 1); i++) {
        for (int /* @Size */i = 0; i < (grid.size() - 1); i++) {
            Double /* @Real */discountBond = termStructureConsistentModelClass.termStructure().getLink().discount(grid.at(i + 1));
            Double /* @Real */xMin = trinomial.underlying(i, 0);
            Double /* @Real */dx = trinomial.dx(i);

            Helper finder = new BlackKarasinski.Helper(i, xMin, dx, discountBond, numericTree);
            Brent s1d = new Brent();
            s1d.setMaxEvaluations(1000);
            // value = s1d.solve(finder, 1e-7, value, vMin, vMax);
            // brent currently does not have solve
            // the argument of AbstractSolver1D currently extends UnaryFunctionDouble
            // why is it that way?
            impl.set(grid.index(i) /* [i] */, value);
            // vMin = value - 10.0;
            // vMax = value + 10.0;
        }
        return numericTree;
    }

    // ! Short-rate dynamics in the Black-Karasinski model
    /*
     * ! The short-rate is here \f[ r_t = e^{\varphi(t) + x_t} \f] where \f$ \varphi(t) \f$ is the deterministic time-dependent
     * parameter (which can not be determined analytically) used for term-structure fitting and \f$ x_t \f$ is the state variable
     * following an Ornstein-Uhlenbeck process.
     */
    class Dynamics extends ShortRateDynamics {

        public Dynamics(final Parameter fitting, Double /* @Real */alpha, Double /* @Real */sigma) {
            super(new OrnsteinUhlenbeckProcess(alpha, sigma, /* default */0.0, /* default */0.0));
            fitting_ = (fitting);
        }

        // Real variable(Time t, Rate r) const {
        // return std::log(r) - fitting_(t);
        // }
        public Double /* @Real */variable(Double /* @Time */t, Double /* @Rate */r) {
            return Math.log(r) - fitting_.getOperatorEq(t);
        }

        // Real shortRate(Time t, Real x) const {
        // return std::exp(x + fitting_(t));
        // }
        @Override
        public Double /* @Real */shortRate(Double /* @Time */t, Double /* @Real */x) {
            return Math.exp(x + fitting_.getOperatorEq(t));
        }

        private Parameter fitting_;
    }
}
