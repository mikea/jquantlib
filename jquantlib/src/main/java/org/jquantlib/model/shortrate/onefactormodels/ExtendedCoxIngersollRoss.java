/*
Copyright (C)
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

import org.jquantlib.instruments.Option;
import org.jquantlib.math.Constants;
import org.jquantlib.math.distributions.NonCentralChiSquaredDistribution;
import org.jquantlib.math.matrixutilities.Array;
import org.jquantlib.methods.lattices.Lattice;
import org.jquantlib.methods.lattices.TrinomialTree;
import org.jquantlib.model.Parameter;
import org.jquantlib.model.shortrate.OneFactorModel;
import org.jquantlib.model.shortrate.TermStructureFittingParameter;
import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.Compounding;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Frequency;
import org.jquantlib.time.TimeGrid;

//! Extended Cox-Ingersoll-Ross model class.
/*! This class implements the extended Cox-Ingersoll-Ross model
    defined by
    \f[
        dr_t = (\theta(t) - \alpha r_t)dt + \sqrt{r_t}\sigma dW_t .
    \f]

    \bug this class was not tested enough to guarantee
         its functionality.

    \ingroup shortrate
 */

public class ExtendedCoxIngersollRoss extends CoxIngersollRoss {

    private static final String strike_must_be_positive = "strike must be positive";
    private static final String unsupported_option_type = "unsupported option type";


    private final TermStructureConsistentModelClass termstructureConsistentModel;
    private Parameter phi_;

    public ExtendedCoxIngersollRoss(final Handle<YieldTermStructure> termStructure,
            final double theta, final double k, final double sigma, final double x0){
        super(x0, theta, k, sigma);
        termstructureConsistentModel = new TermStructureConsistentModelClass(termStructure);
        generateArguments();
    }

    @Override
    public OneFactorModel.ShortRateDynamics dynamics(){
        return new Dynamics(phi_, theta(), k(), sigma(), x0());
    }

    @Override
    public void generateArguments(){
        phi_ = new FittingParameter(termstructureConsistentModel.termStructure(), theta(), k(), sigma(), x0());
    }

    @Override
    public double A(final double t, final double s)  {
        final double pt = termstructureConsistentModel.termStructure().getLink().discount(t);
        final double ps = termstructureConsistentModel.termStructure().getLink().discount(s);
        final double value = super.A(t,s)*Math.exp(B(t,s)*phi_.getOperatorEq(t))*
        (ps*super.A(0.0,t)*Math.exp(-B(0.0,t)*x0()))/
        (pt*super.A(0.0,s)*Math.exp(-B(0.0,s)*x0()));
        return value;
    }

    @Override
    public double discountBondOption(
            final Option.Type type,
            final double strike,
            final double t,
            final double s){
        assert strike > 0.0 : strike_must_be_positive;
        final double discountT = termstructureConsistentModel.termStructure().getLink().discount(t);
        final double discountS = termstructureConsistentModel.termStructure().getLink().discount(s);
        if(t<Constants.QL_EPSILON)
            switch (type) {
            case CALL:
                return Math.max(discountS - strike, 0);
            case PUT:
                return Math.max(strike - discountS, 0);
            default:
                throw new AssertionError(unsupported_option_type);
            }
        final double sigma2 = sigma() * sigma();
        final double h = Math.sqrt(k() * k() + 2 * sigma2);
        final double r0 = termstructureConsistentModel.termStructure().getLink().forwardRate(0.0, 0.0, Compounding.CONTINUOUS,
                Frequency.NO_FREQUENCY).rate();
        final double b = B(t, s);

        final double rho = 2.0 * h / (sigma2 * (Math.exp(h * t) - 1.0));
        final double psi = (k() + h) / sigma2;

        final double df = 4.0 * k() * theta() / sigma2;
        final double ncps = 2.0 * rho * rho * (r0 - phi_.getOperatorEq(0.0)) * Math.exp(h * t) / (rho + psi + b);
        final double ncpt = 2.0 * rho * rho * (r0 - phi_.getOperatorEq(0.0)) * Math.exp(h * t) / (rho + psi);

        final NonCentralChiSquaredDistribution chis = new NonCentralChiSquaredDistribution(df, ncps);
        final NonCentralChiSquaredDistribution chit = new NonCentralChiSquaredDistribution(df, ncpt);

        final double z = Math.log(super.A(t, s) / strike) / b;
        final double call = discountS * chis.op(2.0 * z * (rho + psi + b)) - strike * discountT
        * chit.op(2.0 * z * (rho + psi));
        if (type.equals(Option.Type.CALL))
            return call;
        else
            return call - discountS + strike * discountT;

    }

    @Override
    public Lattice tree(final TimeGrid grid){
        final TermStructureFittingParameter phi = new TermStructureFittingParameter(termstructureConsistentModel.termStructure());
        final Dynamics numericDynamics =  new Dynamics(phi, theta(), k(), sigma(), x0());
        final TrinomialTree trinominal = new TrinomialTree(numericDynamics.process(), grid, true);
        final TermStructureFittingParameter.NumericalImpl impl = (TermStructureFittingParameter.NumericalImpl)phi.getImplementation();
        return new OneFactorModel.ShortRateTree(trinominal, numericDynamics, impl, grid);
    }

    //! Short-rate dynamics in the extended Cox-Ingersoll-Ross model
    /*! The short-rate is here
        \f[
            r_t = \varphi(t) + y_t^2
        \f]
        where \f$ \varphi(t) \f$ is the deterministic time-dependent
        parameter used for term-structure fitting and \f$ y_t \f$ is the
        state variable, the square-root of a standard CIR process.
     */

    private class Dynamics extends CoxIngersollRoss.Dynamics{

        private final Parameter phi_;

        public Dynamics(final Parameter phi,
                final double theta,
                final double k,
                final double sigma,
                final double x0){
            super(theta, k, sigma, x0);
            this.phi_ = phi;
        }
        @Override
        public double variable(final double t, final double r){
            return Math.sqrt(r - phi_.getOperatorEq(t));
        }
        @Override
        public double shortRate(final double t, final double y){
            return y*y + phi_.getOperatorEq(t);
        }
    }

    //! Analytical term-structure fitting parameter \f$ \varphi(t) \f$.
    /*! \f$ \varphi(t) \f$ is analytically defined by
        \f[
            \varphi(t) = f(t) -
                         \frac{2k\theta(e^{th}-1)}{2h+(k+h)(e^{th}-1)} -
                         \frac{4 x_0 h^2 e^{th}}{(2h+(k+h)(e^{th}-1))^1},
        \f]
        where \f$ f(t) \f$ is the instantaneous forward rate at \f$ t \f$
        and \f$ h = \sqrt{k^2 + 2\sigma^2} \f$.
     */

    private class FittingParameter extends TermStructureFittingParameter{
        // FIXME: Review object model
        public FittingParameter(final Handle<YieldTermStructure> termStructure,
                final double theta, final double k, final double sigma, final double x0){
            super(termStructure);
            throw new UnsupportedOperationException("Work in progress");
            //super(this.new Impl(termStructure, theta, k, sigma, x0));
        }

        public FittingParameter(final Handle<YieldTermStructure> term) {
            super(term);
        }

        public class Impl extends Parameter.Impl{
            private final Handle<YieldTermStructure> termStructure_;
            private final double theta_, k_, sigma_, x0_;
            public Impl(final Handle<YieldTermStructure> termStructure,
                    final double theta,
                    final double k,
                    final double sigma,
                    final double x0){
                this.termStructure_ = termStructure;
                this.theta_ = theta;
                this.k_ = k;
                this.sigma_ = sigma;
                this.x0_ = x0;
            }

            @Override
            public double value(final Array params, final double t) {
                final double forwardRate = termStructure_.getLink().forwardRate(t, t, Compounding.CONTINUOUS,
                        Frequency.NO_FREQUENCY).rate();
                final double h = Math.sqrt(k_*k_ + 2.0 * sigma_ * sigma_);
                final double expth = Math.exp(t*h);
                final double temp = 2.0*h + (k_+h)*(expth - 1.0);
                final double phi = forwardRate -
                2.0*k_*theta_*(expth - 1.0)/temp -
                x0_*4.0*h*h*expth/(temp*temp);
                return phi;
            }

        }
    }

}
