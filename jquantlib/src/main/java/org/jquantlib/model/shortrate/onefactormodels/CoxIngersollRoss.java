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

import org.jquantlib.instruments.Option;
import org.jquantlib.math.Array;
import org.jquantlib.math.Constants;
import org.jquantlib.math.distributions.NonCentralChiSquaredDistribution;
import org.jquantlib.math.optimization.Constraint;
import org.jquantlib.math.optimization.PositiveConstraint;
import org.jquantlib.methods.lattices.Lattice;
import org.jquantlib.methods.lattices.TrinomialTree;
import org.jquantlib.model.ConstantParameter;
import org.jquantlib.model.Parameter;
import org.jquantlib.model.shortrate.OneFactorAffineModel;
import org.jquantlib.processes.StochasticProcess1D;
import org.jquantlib.time.TimeGrid;

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
    // private double /*@Real*/ y0_, theta_, k_, sigma_;
    // check this value, arbitrary for now
    
    private static final String strike_must_be_positive = "strike must be positive";
    private static final String unsupported_option_type = "unsupported option type";
    
    
    private Parameter theta_;
    private Parameter k_;
    private Parameter sigma_;
    private Parameter r0_;
    
    public CoxIngersollRoss() {
        this(0.05, 0.1, 0.1, 0.1);
    }
    
    public CoxIngersollRoss(double /* @Rate */r0, double /* @Real */theta, double /* @Real */k, double /* @Real */sigma) {
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

    @Override
    public Lattice tree(TimeGrid grid) {
        TrinomialTree trinomial = new TrinomialTree(dynamics().process(), grid, true);
        return new ShortRateTree(trinomial, dynamics(), grid);
    }

    protected double /* @Real */A(double /* @Time */t, double /* @Time */T) {
        double /* @Real */sigma2 = sigma() * sigma();
        double /* @Real */h = Math.sqrt(k() * k() + 2.0 * sigma2);
        double /* @Real */numerator = 2.0 * h * Math.exp(0.5 * (k() + h) * (T - t));
        double /* @Real */denominator = 2.0 * h + (k() + h) * (Math.exp((T - t) * h) - 1.0);
        double /* @Real */value = Math.log(numerator / denominator) * 2.0 * k() * theta() / sigma2;
        return Math.exp(value);
    }

    protected double /* @Real */B(double /* @Time */t, double /* @Time */T) {
        double /* @Real */h = Math.sqrt(k() * k() + 2.0 * sigma() * sigma());
        double /* @Real */temp = Math.exp((T - t) * h) - 1.0;
        double /* @Real */numerator = 2.0 * temp;
        double /* @Real */denominator = 2.0 * h + (k() + h) * temp;
        double /* @Real */value = numerator / denominator;
        return value;
    }

    protected double /* @Real */theta() {
        return theta_.getOperatorEq(0.0);
    }

    protected double /* @Real */k() {
        return k_.getOperatorEq(0.0);
    }

    protected double /* @Real */sigma() {
        return sigma_.getOperatorEq(0.0);
    }

    protected double /* @Real */x0() {
        return r0_.getOperatorEq(0.0);
    }

    class VolatilityConstraint extends Constraint {
        //TODO: check whether IMPL is necessary
        double /* @Real */k_, theta_;
        public VolatilityConstraint(double k, double theta){
            this.k_ = k;
            this.theta_ = theta;
        }
       
        public boolean test(final Array params) {
            double /* @Real */sigma = params.get(0);
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

        public HelperProcess(double /* @Real */theta, double /* @Real */k, double /* @Real */sigma, double /* @Real */y0) {
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

        private double /* @Real */y0_, theta_, k_, sigma_;
    };

    // ! %Dynamics of the short-rate under the Cox-Ingersoll-Ross model
    /*
     * ! The state variable \f$ y_t \f$ will here be the square-root of the short-rate. It satisfies the following stochastic
     * equation \f[ dy_t=\left[ (\frac{k\theta }{2}+\frac{\sigma ^2}{8})\frac{1}{y_t}- \frac{k}{2}y_t \right] d_t+ \frac{\sigma
     * }{2}dW_{t} \f].
     */

    class Dynamics extends ShortRateDynamics {

        public Dynamics(double /* @Real */theta, double /* @Real */k, double /* @Real */sigma, double /* @Real */x0) {
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



    public ShortRateDynamics dynamics() {
        return new Dynamics(theta(), k(), sigma(), x0());
    }

    public double /* @Real */discountBondOption(Option.Type type, double /* @Real */strike, double /* @Time */t,
            double /* @Time */s) {

        if (strike <= 0.0) {
            throw new IllegalArgumentException(strike_must_be_positive);
        }
        double /* @DiscountFactor */discountT = discountBond(0.0, t, x0());
        double /* @DiscountFactor */discountS = discountBond(0.0, s, x0());
        
        if (t < Constants.QL_EPSILON) {
            switch(type) {
              case CALL:
                return Math.max(discountS - strike, 0.0);
              case PUT:
                return Math.max(strike - discountS, 0.0);
              default: throw new IllegalArgumentException(unsupported_option_type);
            }
        }
        
        double /* @Real */sigma2 = sigma() * sigma();
        double /* @Real */h = Math.sqrt(k() * k() + 2.0 * sigma2);
        double /* @Real */b = B(t, s);

        double /* @Real */rho = 2.0 * h / (sigma2 * (Math.exp(h * t) - 1.0));
        double /* @Real */psi = (k() + h) / sigma2;

        double /* @Real */df = 4.0 * k() * theta() / sigma2;
        double /* @Real */ncps = 2.0 * rho * rho * x0() * Math.exp(h * t) / (rho + psi + b);
        double /* @Real */ncpt = 2.0 * rho * rho * x0() * Math.exp(h * t) / (rho + psi);
        
        NonCentralChiSquaredDistribution chis = new NonCentralChiSquaredDistribution(df, ncps);
        NonCentralChiSquaredDistribution chit = new NonCentralChiSquaredDistribution(df, ncpt);
        
        double /* @Real */z = Math.log(A(t, s) / strike) / b;
        double /*@Real*/ call = discountS*chis.evaluate(2.0*z*(rho+psi+b)) -
        strike*discountT*chit.evaluate(2.0*z*(rho+psi));

        if (type == Option.Type.CALL) // return call;
        {
            return 0.0;
        } else // return call - discountS + strike*discountT;
        {
            return 1.0;
        }
    }


}
