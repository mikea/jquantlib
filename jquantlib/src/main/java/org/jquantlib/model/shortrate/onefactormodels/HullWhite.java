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

import static org.jquantlib.pricingengines.BlackFormula.blackFormula;

import org.jquantlib.instruments.Option;
import org.jquantlib.math.Constants;
import org.jquantlib.math.matrixutilities.Array;
import org.jquantlib.methods.lattices.Lattice;
import org.jquantlib.methods.lattices.TrinomialTree;
import org.jquantlib.model.Parameter;
import org.jquantlib.model.shortrate.NullParameter;
import org.jquantlib.model.shortrate.TermStructureFittingParameter;
import org.jquantlib.processes.OrnsteinUhlenbeckProcess;
import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.Compounding;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Frequency;
import org.jquantlib.time.TimeGrid;

/**
 * 
 * @author Praneet Tiwari
 */
// ! Single-factor Hull-White (extended %Vasicek) model class.
/*
 * ! This class implements the standard single-factor Hull-White model defined by \f[ dr_t = (\theta(t) - \alpha r_t)dt + \sigma
 * dW_t \f] where \f$ \alpha \f$ and \f$ \sigma \f$ are constants.
 * 
 * \test calibration results are tested against cached values
 * 
 * \bug When the term structure is relinked, the r0 parameter of the underlying Vasicek model is not updated.
 * 
 * \ingroup shortrate
 */
// TODO: code review :: license, class comments, comments for access modifiers, comments for @Override
public class HullWhite extends Vasicek implements TermStructureConsistentModel {

    // need permanent solution for this one
    TermStructureConsistentModelClass termStructureConsistentModelClass;
    Parameter phi_;

    public HullWhite(final Handle<YieldTermStructure> termStructure){
        this(termStructure, 0.1, 0.01);
    }

    public HullWhite(
            final Handle<YieldTermStructure>/* YieldTermStructure */termStructure,
            final double /* @Real */a /* = 0.1 */,
            final double /* @Real */sigma /* = 0.01 */) {
        super(termStructure.getLink().forwardRate(
                0.0, 0.0, Compounding.CONTINUOUS, Frequency.NO_FREQUENCY).rate(), a, 0.0, sigma, 0.0);

        if (System.getProperty("EXPERIMENTAL") == null)
            throw new UnsupportedOperationException("Work in progress");

        termStructureConsistentModelClass = new TermStructureConsistentModelClass(termStructure);
        b_ = new NullParameter();
        lambda_ = new NullParameter();
        generateArguments();
        termStructure.addObserver(this);
    }

    @Override
    public Lattice tree(final TimeGrid grid) {
        final TermStructureFittingParameter phi = new TermStructureFittingParameter(termStructureConsistentModelClass.termStructure());
        // needed to activate the above constructor
        final ShortRateDynamics numericDynamics = (new Dynamics(phi, a(), sigma()));
        final TrinomialTree trinomial = new TrinomialTree(numericDynamics.process(), grid, true);
        final ShortRateTree numericTree = null;// new ShortRateTree(trinomial, numericDynamics, grid);

        // typedef TermStructureFittingParameter::NumericalImpl NumericalImpl;
        final TermStructureFittingParameter.NumericalImpl impl = (TermStructureFittingParameter.NumericalImpl) (phi.getImplementation());
        impl.reset();
        for (int /* @Size */i = 0; i < (grid.size() - 1); i++) {
            final double /* @Real */discountBond = termStructureConsistentModelClass.termStructure().getLink().discount(grid.at(i + 1));
            final Array statePrices = numericTree.statePrices(i);
            final int /* @Size */size = numericTree.size(i);
            final double /* @Time */dt = numericTree.timeGrid().dt(i);
            final double /* @Real */dx = trinomial.dx(i);
            double /* @Real */x = trinomial.underlying(i, 0);
            double /* @Real */value = 0.0;
            for (int /* @Size */j = 0; j < size; j++) {
                value += statePrices.get(j) * Math.exp(-x * dt);
                x += dx;
            }
            value = Math.log(value / discountBond) / dt;
            // impl->set(grid[i], value);
            impl.set(grid.index(i), value); // ???????????????
        }
        return numericTree;
    }

    @Override
    public double /* @Real */A(final double /* @Time */t, final double /* @Time */T) {
        final double /* @DiscountFactor */discount1 = termStructureConsistentModelClass.termStructure().getLink().discount(t);
        final double /* @DiscountFactor */discount2 = termStructureConsistentModelClass.termStructure().getLink().discount(T);
        final double /* @Rate */forward = termStructureConsistentModelClass.termStructure().getLink().forwardRate(t, t,
                Compounding.CONTINUOUS, Frequency.NO_FREQUENCY).rate();
        final double /* @Real */temp = sigma() * B(t, T);
        final double /* @Real */value = B(t, T) * forward - 0.25 * temp * temp * B(0.0, 2.0 * t);
        return Math.exp(value) * discount2 / discount1;
    }

    @Override
    public void generateArguments() {
        phi_ = new FittingParameter(termStructureConsistentModelClass.termStructure(), a(), sigma());
    }

    @Override
    public double /* @Real */discountBondOption(final Option.Type type, final double /* @Real */strike, final double /* @Time */maturity,
            final double /* @Time */bondMaturity) {

        final double /* @Real */_a = a();
        double /* @Real */v;
        if (_a < Math.sqrt(Constants.QL_EPSILON))
            v = sigma() * B(maturity, bondMaturity) * Math.sqrt(maturity);
        else
            v = sigma() * B(maturity, bondMaturity) * Math.sqrt(0.5 * (1.0 - Math.exp(-2.0 * _a * maturity)) / _a);
        final double /* @Real */f = termStructureConsistentModelClass.termStructure().getLink().discount(bondMaturity);
        final double /* @Real */k = termStructureConsistentModelClass.termStructure().getLink().discount(maturity) * strike;

        return blackFormula(type, k, f, v);
    }

    /*! Futures convexity bias (i.e., the difference between
    futures implied rate and forward rate) calculated as in
    G. Kirikos, D. Novak, "Convexity Conundrums", Risk
    Magazine, March 1997.

    \note t and T should be expressed in yearfraction using
          deposit day counter, F_quoted is futures' market price.
     */
    public static double /* @Rate */convexityBias(final double /* @Real */futuresPrice, final double /* @Time */t, final double /* @Time */T,
            final double /* @Real */sigma, final double /* @Real */a) {

        assert futuresPrice >= 0.0 : "negative futures price not allowed";
        assert t >= 0.0 : "negative t not allowed";
        assert T >= t : "T must not be less than t";
        assert a >= 0.0 : "negative a not allowed";

        final double /* @Time */deltaT = (T - t);
        final double /* @Real */tempDeltaT = (1. - Math.exp(-a * deltaT)) / a;
        final double /* @Real */halfSigmaSquare = sigma * sigma / 2.0;

        // lambda adjusts for the fact that the underlying is an interest rate
        final double /* @Real */lambda = halfSigmaSquare * (1. - Math.exp(-2.0 * a * t)) / a * tempDeltaT * tempDeltaT;

        final double /* @Real */tempT = (1.0 - Math.exp(-a * t)) / a;

        // phi is the MtM adjustment
        final double /* @Real */phi = halfSigmaSquare * tempDeltaT * tempT * tempT;

        // the adjustment
        final double /* @Real */z = lambda + phi;

        final double /* @Rate */futureRate = (100.0 - futuresPrice) / 100.0;
        return (1.0 - Math.exp(-z)) * (futureRate + 1.0 / (T - t));
    }

    //! Analytical term-structure fitting parameter \f$ \varphi(t) \f$.
    /*! \f$ \varphi(t) \f$ is analytically defined by
        \f[
            \varphi(t) = f(t) + \frac{1}{2}[\frac{\sigma(1-e^{-at})}{a}]^2,
        \f]
        where \f$ f(t) \f$ is the instantaneous forward rate at \f$ t \f$.
     */
    public class FittingParameter extends TermStructureFittingParameter{
        private class Impl extends Parameter.Impl{
            public Impl(final Handle<YieldTermStructure> termStructure, final double a, final double sigma) {
                this.termStructure_ = termStructure;
                this.a_ = a;
                this.sigma_ = sigma;
            }

            private final Handle<YieldTermStructure> termStructure_;
            private final double a_, sigma_;

            @Override
            public double value(final Array params, final double t) {
                final double forwardRate =
                    termStructure_.getLink().forwardRate(t, t, Compounding.CONTINUOUS, Frequency.NO_FREQUENCY).rate();
                final double temp = sigma_*(1.0 - Math.exp(-a_*t))/a_;
                return (forwardRate + 0.5*temp*temp);
            }
        }
        public FittingParameter(final Handle<YieldTermStructure> termStructure, final double a, final double sigma) {
            super(termStructure);
            throw new UnsupportedOperationException("work in progress");
            //FIXME: change class hierarchy -> use static inner classes ?
            //super(new Impl(termStructure, a, sigma));

        }

    }

    // ! Short-rate dynamics in the Hull-White model
    /*
     * ! The short-rate is here \f[ r_t = \varphi(t) + x_t \f] where \f$ \varphi(t) \f$ is the deterministic time-dependent
     * parameter used for term-structure fitting and \f$ x_t \f$ is the state variable following an Ornstein-Uhlenbeck process.
     */
    public class Dynamics extends ShortRateDynamics {

        private final Parameter fitting_;

        public Dynamics(final Parameter fitting, final double /* @Real */a, final double /* @Real */sigma) {
            super(new OrnsteinUhlenbeckProcess(a, sigma, /* default */0.0, /* default */0.0));
            fitting_ = (fitting);
        }

        @Override
        public double /* @Real */variable(final double /* @Time */t, final double /* @Rate */r) {
            return r - fitting_.getOperatorEq(t);
        }

        @Override
        public double /* @Real */shortRate(final double /* @Time */t, final double /* @Real */x) {
            return x + fitting_.getOperatorEq(t);
        }
    }

    @Override
    public ShortRateDynamics dynamics() {
        return (new Dynamics(phi_, a(), sigma()));
    }

    @Override
    public Handle<YieldTermStructure> termStructure() {
        // TODO Auto-generated method stub
        return null;
    }
}
