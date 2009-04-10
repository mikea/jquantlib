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
import org.jquantlib.math.optimization.NoConstraint;
import org.jquantlib.math.optimization.PositiveConstraint;
import org.jquantlib.model.shortrate.ConstantParameter;
import org.jquantlib.model.shortrate.OneFactorAffineModel;
import org.jquantlib.model.shortrate.Parameter;
import org.jquantlib.model.shortrate.ShortRateDynamics;
import org.jquantlib.processes.OrnsteinUhlenbeckProcess;

import static org.jquantlib.pricingengines.BlackFormula.*;
import org.jquantlib.util.Observer;

/**
 * 
 * @author Praneet Tiwari
 */
// ! %Vasicek model class
/*
 * ! This class implements the Vasicek model defined by \f[ dr_t = a(b - r_t)dt + \sigma dW_t , \f] where \f$ a \f$, \f$ b \f$ and
 * \f$ \sigma \f$ are constants; a risk premium \f$ \lambda \f$ can also be specified.
 * 
 * \ingroup shortrate
 */
public class Vasicek extends OneFactorAffineModel {

    // check this value, arbitrary for now
    private static double QL_EPSILON = 1e-10;
    Double /* @Real */r0_;
    Parameter a_;
    Parameter b_;
    Parameter sigma_;
    Parameter lambda_;

    Double /* @Real */a() {
        return a_.getOperatorEq(0.0);
    }

    Double /* @Real */b() {
        return b_.getOperatorEq(0.0);
    }

    Double /* @Real */lambda() {
        return lambda_.getOperatorEq(0.0);
    }

    Double /* @Real */sigma() {
        return sigma_.getOperatorEq(0.0);
    }

    public Vasicek(Double /* @Rate */r0, Double /* @Real */a, Double /* @Real */b, Double /* @Real */sigma,
            Double /* @Real */lambda) {
        super(4);
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
        this.r0_ = r0;
        a_ = arguments_.get(0);
        b_ = arguments_.get(1);
        sigma_ = arguments_.get(2);

        lambda_ = arguments_.get(3);

        a_ = new ConstantParameter(a, new PositiveConstraint());
        b_ = new ConstantParameter(b, new NoConstraint());
        sigma_ = new ConstantParameter(sigma, new PositiveConstraint());
        lambda_ = new ConstantParameter(lambda, new NoConstraint());
    }

    public Double /* @Real */discountBondOption(Option.Type type, Double /* @Real */strike, Double /* @Time */maturity,
            Double /* @Time */bondMaturity) {
        Double /* @Real */v;
        Double /* @Real */_a = a();

        /****** disable these checks for a while* v = 0.0; *************/
        // if (std::fabs(maturity) < QL_EPSILON) {
        if (Math.abs(maturity) < QL_EPSILON) {
            v = 0.0;
        } else if (_a < Math.sqrt(QL_EPSILON)) {
            v = sigma() * B(maturity, bondMaturity) * Math.sqrt(maturity);
        } else {
            v = sigma() * B(maturity, bondMaturity) * Math.sqrt(0.5 * (1.0 - Math.exp(-2.0 * _a * maturity)) / _a);
        }

        Double /* @Real */f = discountBond(0.0, bondMaturity, r0_);
        Double /* @Real */k = discountBond(0.0, maturity, r0_) * strike;

        return blackFormula(type, k, f, v);

    }

    @Override
    public ShortRateDynamics dynamics() {
        return new Dynamics(a(), b(), sigma(), r0_);

    }

    // ! Short-rate dynamics in the %Vasicek model
    /*
     * ! The short-rate follows an Ornstein-Uhlenbeck process with mean \f$ b \f$.
     */
    class Dynamics extends ShortRateDynamics {

        public Dynamics(Double /* @Real */a, Double /* @Real */b, Double /* @Real */sigma, Double /* @Real */r0) {
            super(new OrnsteinUhlenbeckProcess(a, sigma, r0 - b, 0.0));

            a_ = (a);
            b_ = (b);
            r0_ = (r0);
        }

        public Double /* @Real */variable(Double /* @Time */t, Double /* @Rate */r) {
            return r - b_;
        }

        @Override
        public Double /* @Real */shortRate(Double /* @Time */t, Double /* @Real */x) {
            return x + b_;
        }

        private Double /* @Real */a_, b_, r0_;
    }

    // the real heart of Vasicek, A & B
    public Double /* @Real */A(Double /* @Time */t, Double /* @Time */T) {
        Double /* @Real */_a = a();
        if (_a < Math.sqrt(QL_EPSILON)) {
            return 0.0;
        } else {
            Double /* @Real */sigma2 = sigma() * sigma();
            Double /* @Real */bt = B(t, T);
            return Math.exp((b() + lambda() * sigma() / _a - 0.5 * sigma2 / (_a * _a)) * (bt - (T - t)) - 0.25 * sigma2 * bt * bt
                    / _a);
        }
    }

    @Override
    public Double /* @Real */B(Double /* @Time */t, Double /* @Time */T) {
        Double /* @Real */_a = a();
        if (_a < Math.sqrt(QL_EPSILON)) {
            return (T - t);
        } else {
            return (1.0 - Math.exp(-_a * (T - t))) / _a;
        }
    }

    // sundry cosmetic methods
    @Override
    protected void generateArguments() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

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
}
