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
import org.jquantlib.instruments.Option;
import org.jquantlib.math.Array;
import org.jquantlib.math.optimization.Constraint;
import org.jquantlib.math.optimization.PositiveConstraint;
import org.jquantlib.methods.lattices.Lattice;
import org.jquantlib.methods.lattices.TrinomialTree;
import org.jquantlib.model.shortrate.ConstantParameter;
import org.jquantlib.model.shortrate.OneFactorAffineModel;
import org.jquantlib.model.shortrate.Parameter;
import org.jquantlib.model.shortrate.ShortRateDynamics;
import org.jquantlib.model.shortrate.ShortRateTree;
import org.jquantlib.processes.StochasticProcess1D;
import org.jquantlib.time.TimeGrid;
import org.jquantlib.util.Observer;

/**
 * 
 * @author Praneet Tiwari
 */
// ! Cox-Ingersoll-Ross model class.
/*
 * ! This class implements the Cox-Ingersoll-Ross model defined by \f[ dr_t = k(\theta - r_t)dt + \sqrt{r_t}\sigma dW_t . \f]
 * 
 * \bug this class was not tested enough to guarantee its functionality.
 * 
 * \ingroup shortrate
 */
public class CoxIngersollRoss extends OneFactorAffineModel {
    // private Double /*@Real*/ y0_, theta_, k_, sigma_;
    // check this value, arbitrary for now

    private static double QL_EPSILON = 1e-10;
    private Parameter theta_;
    private Parameter k_;
    private Parameter sigma_;
    private Parameter r0_;

    @Override
    public Lattice tree(TimeGrid grid) {
        TrinomialTree trinomial = new TrinomialTree(dynamics().process(), grid, true);
        return null;//new ShortRateTree(trinomial, dynamics(), grid);
    }

    protected Double /* @Real */A(Double /* @Time */t, Double /* @Time */T) {
        Double /* @Real */sigma2 = sigma() * sigma();
        Double /* @Real */h = Math.sqrt(k() * k() + 2.0 * sigma2);
        Double /* @Real */numerator = 2.0 * h * Math.exp(0.5 * (k() + h) * (T - t));
        Double /* @Real */denominator = 2.0 * h + (k() + h) * (Math.exp((T - t) * h) - 1.0);
        Double /* @Real */value = Math.log(numerator / denominator) * 2.0 * k() * theta() / sigma2;
        return Math.exp(value);
    }

    protected Double /* @Real */B(Double /* @Time */t, Double /* @Time */T) {
        Double /* @Real */h = Math.sqrt(k() * k() + 2.0 * sigma() * sigma());
        Double /* @Real */temp = Math.exp((T - t) * h) - 1.0;
        Double /* @Real */numerator = 2.0 * temp;
        Double /* @Real */denominator = 2.0 * h + (k() + h) * temp;
        Double /* @Real */value = numerator / denominator;
        return value;
    }

    protected Double /* @Real */theta() {
        return theta_.getOperatorEq(0.0);
    }

    protected Double /* @Real */k() {
        return k_.getOperatorEq(0.0);
    }

    protected Double /* @Real */sigma() {
        return sigma_.getOperatorEq(0.0);
    }

    protected Double /* @Real */x0() {
        return r0_.getOperatorEq(0.0);
    }

    // cosmetic methods, not supported yet.
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

    class VolatilityConstraint extends Constraint {

        Double /* @Real */k_, theta_;

        public VolatilityConstraint(Double /* @Real */k, Double /* @Real */theta) {
            // : Constraint(boost::shared_ptr<Constraint::Impl>(
            // new VolatilityConstraint::Impl(k, theta))) {
            this.k_ = k;
            this.theta_ = theta;
        }

        public boolean test(final Array params) {
            Double /* @Real */sigma = params.get(0);
            if (sigma <= 0.0) {
                return false;
            }
            if (sigma * sigma >= 2.0 * k_ * theta_) {
                return false;
            }
            return true;
        }
    }

    class HelperProcess extends StochasticProcess1D {

        public HelperProcess(Double /* @Real */theta, Double /* @Real */k, Double /* @Real */sigma, Double /* @Real */y0) {
            this.y0_ = y0;
            this.theta_ = theta;
            this.k_ = k;
            this.sigma_ = sigma;

        }

        @Override
        public double /* @Real */x0() {
            return y0_;
        }

        @Override
        public double /* @Real */drift(double /* @Time */t, double /* @Real */y) {
            return (0.5 * theta_ * k_ - 0.125 * sigma_ * sigma_) / y - 0.5 * k_ * y;
        }

        @Override
        public double /* @Real */diffusion(double /* @Time */t, double /* @Real */y) {
            return 0.5 * sigma_;
        }

        private Double /* @Real */y0_, theta_, k_, sigma_;
    };

    // ! %Dynamics of the short-rate under the Cox-Ingersoll-Ross model
    /*
     * ! The state variable \f$ y_t \f$ will here be the square-root of the short-rate. It satisfies the following stochastic
     * equation \f[ dy_t=\left[ (\frac{k\theta }{2}+\frac{\sigma ^2}{8})\frac{1}{y_t}- \frac{k}{2}y_t \right] d_t+ \frac{\sigma
     * }{2}dW_{t} \f].
     */

    class Dynamics extends ShortRateDynamics {

        public Dynamics(Double /* @Real */theta, Double /* @Real */k, Double /* @Real */sigma, Double /* @Real */x0) {
            super(new HelperProcess(theta, k, sigma, Math.sqrt(x0)));
        }

        @Override
        public double /* @Real */variable(double /* @Time */t, double /* @Real */r) {
            return Math.sqrt(r);
        }

        @Override
        public double /* @Real */shortRate(double /* @Time */t, double /* @Real */y) {
            return y * y;
        }
    }

    public CoxIngersollRoss(Double /* @Rate */r0, Double /* @Real */theta, Double /* @Real */k, Double /* @Real */sigma) {
        super(4);
        theta_ = (arguments_.get(0));
        k_ = arguments_.get(1);
        sigma_ = arguments_.get(2);
        r0_ = arguments_.get(3);
        theta_ = new ConstantParameter(theta, new PositiveConstraint());
        k_ = new ConstantParameter(k, new PositiveConstraint());
        sigma_ = new ConstantParameter(sigma, new VolatilityConstraint(k, theta));
        r0_ = new ConstantParameter(r0, new PositiveConstraint());
    }

    public ShortRateDynamics dynamics() {
        return new Dynamics(theta(), k(), sigma(), x0());
    }

    public Double /* @Real */discountBondOption(Option.Type type, Double /* @Real */strike, Double /* @Time */t,
            Double /* @Time */s) {

        // QL_REQUIRE(strike>0.0, "strike must be positive");
        if (strike < 0.0) {
            throw new IllegalArgumentException("strike must be positive");
        }
        Double /* @DiscountFactor */discountT = discountBond(0.0, t, x0());
        Double /* @DiscountFactor */discountS = discountBond(0.0, s, x0());
        /****
         * TODO if (t < QL_EPSILON) { switch(type) { case 1:// Option.Type.CALL: return Math.max<Double /*@Real/>(discountS -
         * strike, 0.0); case Option::Put: return Math.max<Double /*@Real/>(strike - discountS, 0.0); default:
         * QL_FAIL("unsupported option type"); } }
         * ****/
        Double /* @Real */sigma2 = sigma() * sigma();
        Double /* @Real */h = Math.sqrt(k() * k() + 2.0 * sigma2);
        Double /* @Real */b = B(t, s);

        Double /* @Real */rho = 2.0 * h / (sigma2 * (Math.exp(h * t) - 1.0));
        Double /* @Real */psi = (k() + h) / sigma2;

        Double /* @Real */df = 4.0 * k() * theta() / sigma2;
        Double /* @Real */ncps = 2.0 * rho * rho * x0() * Math.exp(h * t) / (rho + psi + b);
        Double /* @Real */ncpt = 2.0 * rho * rho * x0() * Math.exp(h * t) / (rho + psi);
        /***
         * TODO: Implement NonCentralChiSquareDistribution NonCentralChiSquareDistribution chis(df, ncps);
         * NonCentralChiSquareDistribution chit(df, ncpt);
         **/
        Double /* @Real */z = Math.log(A(t, s) / strike) / b;
        // Double /*@Real*/ call = discountS*chis(2.0*z*(rho+psi+b)) -
        // strike*discountT*chit(2.0*z*(rho+psi));

        if (type == Option.Type.CALL) // return call;
        {
            return 0.0;
        } else // return call - discountS + strike*discountT;
        {
            return 1.0;
        }
    }
}
