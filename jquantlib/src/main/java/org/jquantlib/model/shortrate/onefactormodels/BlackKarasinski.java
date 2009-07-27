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
package org.jquantlib.model.shortrate.onefactormodels;

import org.jquantlib.math.Array;
import org.jquantlib.math.Ops;
import org.jquantlib.math.optimization.PositiveConstraint;
import org.jquantlib.math.solvers1D.Brent;
import org.jquantlib.methods.lattices.Lattice;
import org.jquantlib.methods.lattices.TrinomialTree;
import org.jquantlib.model.ConstantParameter;
import org.jquantlib.model.Parameter;
import org.jquantlib.model.shortrate.OneFactorModel;
import org.jquantlib.model.shortrate.TermStructureFittingParameter;
import org.jquantlib.processes.OrnsteinUhlenbeckProcess;
import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.TimeGrid;

/**
 * Standard Black-Karasinski model class.
 * <p>
 * This class implements the standard Black-Karasinski model defined by 
 * {@latex[ d\ln r_t = (\theta(t) - \alpha \ln r_t)dt + \sigma dW_t }
 * where {@latex$ \alpha } and {@latex$ \sigma } are constants.
 * 
 * @category shortrate
 * 
 * @author Praneet Tiwari
 */
public class BlackKarasinski extends OneFactorModel implements TermStructureConsistentModel {
    // need permanent solution for this one
    
    //TODO:renaming....
    private TermStructureConsistentModelClass termstructureConsistentModel;
    
    private static final String no_defined_process_for_bk = "no defined process for Black-Karasinski";
    
    public BlackKarasinski(final Handle<YieldTermStructure> termStructure){
        this(termStructure, 0.1,0.1);
    }
    
    public BlackKarasinski(final Handle<YieldTermStructure> termStructure, double a, double sigma){
       super(2);
       
       if (System.getProperty("EXPERIMENTAL") == null)
        throw new UnsupportedOperationException("Work in progress");
       
       termstructureConsistentModel = new TermStructureConsistentModelClass(termStructure);
       this.a_ = arguments_.get(0);
       this.sigma_ = arguments_.get(1);
       //FIXME: bug?
       this.a_ = new ConstantParameter(a, new PositiveConstraint());
       this.sigma_ = new ConstantParameter(sigma, new PositiveConstraint());
       termStructure.addObserver(this);
    }
    
    public double /* @Real */a() {
        return a_.getOperatorEq(0.0);
    }

    public double /* @Real */sigma() {
        return sigma_.getOperatorEq(0.0);
    }

    Parameter a_;
    Parameter sigma_;

    private class Helper implements Ops.DoubleOp {
        private int /* @Size */size_;
        private double /* @Time */dt_;
        private double /* @Real */xMin_, dx_;
        private Array statePrices_;
        private double /* @Real */discountBondPrice_;

        public Helper(int /* @Size */i, double /* @Real */xMin, double /* @Real */dx, double /* @Real */discountBondPrice,
                OneFactorModel.ShortRateTree tree) {
            size_ = (tree.size(i));
            dt_ = (tree.timeGrid().dt(i));
            xMin_ = (xMin);
            dx_ = (dx);
            statePrices_ = (tree.statePrices(i));
            discountBondPrice_ = (discountBondPrice);
        }

        public double /* @Real */op /* () */(final double /* @Real */theta) {
            double /* @Real */value = discountBondPrice_;
            double /* @Real */x = xMin_;
            for (int /* @Size */j = 0; j < size_; j++) {
                double /* @Real */discount = Math.exp(-Math.exp(theta + x) * dt_);
                value -= statePrices_.get(j)/* [j] */* discount;
                x += dx_;
            }
            return value;
        }
    } 

   

    @Override
    public ShortRateDynamics dynamics() {
        throw new IllegalArgumentException(no_defined_process_for_bk);
    }

    @Override
    public Lattice tree(final TimeGrid grid) {
        TermStructureFittingParameter phi = new TermStructureFittingParameter(termstructureConsistentModel.termStructure());
        ShortRateDynamics numericDynamics = (new Dynamics(phi, a(), sigma()));
        TrinomialTree trinomial = new TrinomialTree(numericDynamics.process(), grid, true);
        ShortRateTree numericTree = null;//new ShortRateTree(trinomial, numericDynamics, grid);
       
        TermStructureFittingParameter.NumericalImpl impl = (TermStructureFittingParameter.NumericalImpl) (phi.getImplementation());
        impl.reset();
        double /* @Real */value = 1.0;
        double /* @Real */vMin = -50.0;
        double /* @Real */vMax = 50.0;
        for (int /* @Size */i = 0; i < (grid.size() - 1); i++) {
            double /* @Real */discountBond = termstructureConsistentModel.termStructure().getLink().discount(grid.at(i + 1));
            double /* @Real */xMin = trinomial.underlying(i, 0);
            double /* @Real */dx = trinomial.dx(i);

            Helper finder = new BlackKarasinski.Helper(i, xMin, dx, discountBond, numericTree);
            Brent s1d = new Brent();
            s1d.setMaxEvaluations(1000);
            value = s1d.solve(finder, 1e-7, value, vMin, vMax);
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

        public double /* @Real */variable(double /* @Time */t, double /* @Rate */r) {
            return Math.log(r) - fitting_.getOperatorEq(t);
        }

        @Override
        public double /* @Real */shortRate(double /* @Time */t, double /* @Real */x) {
            return Math.exp(x + fitting_.getOperatorEq(t));
        }

        private Parameter fitting_;
    }

    @Override
    public Handle<YieldTermStructure> termStructure() {
        return termstructureConsistentModel.termStructure();
    }
}
