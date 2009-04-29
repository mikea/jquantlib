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
package org.jquantlib.model.shortrate.twofactormodels;

import java.util.ArrayList;
import java.util.List;
import org.jquantlib.instruments.Option;
import org.jquantlib.math.Array;
import org.jquantlib.math.distributions.CumulativeNormalDistribution;
import org.jquantlib.math.solvers1D.Brent;
import org.jquantlib.model.Parameter;
import org.jquantlib.model.shortrate.AffineModel;
import org.jquantlib.model.shortrate.TwoFactorModel;
//import org.jquantlib.model.shortrate.instruments.Swaption;
import org.jquantlib.model.shortrate.onefactormodels.FittingParameter;
import org.jquantlib.model.shortrate.onefactormodels.TermStructureConsistentModelClass;
import org.jquantlib.processes.OrnsteinUhlenbeckProcess;
import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import static org.jquantlib.pricingengines.BlackFormula.*;
import org.jquantlib.util.Observer;

/**
 *
 * @author Praneet Tiwari
 */

//! Two-additive-factor gaussian model class.
    /*! This class implements a two-additive-factor model defined by
\f[
dr_t = \varphi(t) + x_t + y_t
\f]
where \f$ x_t \f$ and \f$ y_t \f$ are defined by
\f[
dx_t = -a x_t dt + \sigma dW^1_t, x_0 = 0
\f]
\f[
dy_t = -b y_t dt + \sigma dW^2_t, y_0 = 0
\f]
and \f$ dW^1_t dW^2_t = \rho dt \f$.

\bug This class was not tested enough to guarantee
its functionality.

\ingroup shortrate
 */
public class G2 extends TwoFactorModel implements AffineModel {
    //need permanent solution for this one

    public static double QL_EPSILON = 1e-10;
    public static double M_PI = 3.141592653589793238462643383280;
    TermStructureConsistentModelClass termStructureConsistentModelClass;

    private double /*@Real*/ sigmaP(double /*@Time*/ t, double /*@Time*/ s) {
        double /*@Real*/ temp = 1.0 - Math.exp(-(a() + b()) * t);
        double /*@Real*/ temp1 = 1.0 - Math.exp(-a() * (s - t));
        double /*@Real*/ temp2 = 1.0 - Math.exp(-b() * (s - t));
        double /*@Real*/ a3 = a() * a() * a();
        double /*@Real*/ b3 = b() * b() * b();
        double /*@Real*/ sigma2 = sigma() * sigma();
        double /*@Real*/ eta2 = eta() * eta();
        double /*@Real*/ value =
                0.5 * sigma2 * temp1 * temp1 * (1.0 - Math.exp(-2.0 * a() * t)) / a3 +
                0.5 * eta2 * temp2 * temp2 * (1.0 - Math.exp(-2.0 * b() * t)) / b3 +
                2.0 * rho() * sigma() * eta() / (a() * b() * (a() + b())) *
                temp1 * temp2 * temp;
        return Math.sqrt(value);
    }
    private Parameter a_;
    private Parameter sigma_;
    private Parameter b_;
    private Parameter eta_;
    private Parameter rho_;
    private Parameter phi_;

    private double /*@Real*/ V(double /*@Time*/ t) {
        double /*@Real*/ expat = Math.exp(-a() * t);
        double /*@Real*/ expbt = Math.exp(-b() * t);
        double /*@Real*/ cx = sigma() / a();
        double /*@Real*/ cy = eta() / b();
        double /*@Real*/ valuex = cx * cx * (t + (2.0 * expat - 0.5 * expat * expat - 1.5) / a());
        double /*@Real*/ valuey = cy * cy * (t + (2.0 * expbt - 0.5 * expbt * expbt - 1.5) / b());
        double /*@Real*/ value = 2.0 * rho() * cx * cy * (t + (expat - 1.0) / a() + (expbt - 1.0) / b() - (expat * expbt - 1.0) / (a() + b()));
        return valuex + valuey + value;
    }

    private double /*@Real*/ a() {
        return a_.getOperatorEq(0.0);
    }

    private double /*@Real*/ sigma() {
        return sigma_.getOperatorEq(0.0);
    }

    private double /*@Real*/ b() {
        return b_.getOperatorEq(0.0);
    }

    private double /*@Real*/ eta() {
        return eta_.getOperatorEq(0.0);
    }

    private double /*@Real*/ rho() {
        return rho_.getOperatorEq(0.0);
    }

    public G2(final Handle<YieldTermStructure> termStructure,
            double /*@Real*/ a /*= 0.1*/,
            double /*@Real*/ sigma /*= 0.01*/,
            double /*@Real*/ b/* = 0.1*/,
            double /*@Real*/ eta/* = 0.01*/,
            double /*@Real*/ rho/*= -0.75*/) {
        super(5);
        termStructureConsistentModelClass = new TermStructureConsistentModelClass(termStructure);
        a_ = (arguments_.get(0) /*[0]*/);
        sigma_ = (arguments_.get(1) /*[1]*/);
        b_ = (arguments_.get(2) /*[]2]*/);

        eta_ = (arguments_.get(3) /*[]3]*/);
        rho_ = (arguments_.get(4) /*[]4]*/);
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

    @Override
    public double discount(double t) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double discountBond(double now, double maturity, Array factors) {
        if (factors.size() < 1) {
            throw new IllegalArgumentException("g2 model needs two factors to compute discount bond");
        }
        return discountBond(now, maturity, factors.get(0), factors.get(1));
    }

    public double discountBond(double t, double T, double x, double y) {
        return A(t, T) * Math.exp(-B(a(), (T - t)) * x - B(b(), (T - t)) * y);
    }

    class Dynamics extends TwoFactorModel.ShortRateDynamics {

        public Dynamics(final Parameter fitting,
                double /*@Real*/ a,
                double /*@Real*/ sigma,
                double /*@Real*/ b,
                double /*@Real*/ eta,
                double /*@Real*/ rho) {
            super(
                    new OrnsteinUhlenbeckProcess(a, sigma, 0.0, 0.0),
                    new OrnsteinUhlenbeckProcess(b, eta, 0.0, 0.0),
                    rho);
            fitting_ = (fitting);
        }

        public double /*@Rate*/ shortRate(double /*@Time*/ t,
                double /*@Real*/ x,
                double /*@Real*/ y) {
            return fitting_.getOperatorEq(t) + x + y;
        }
        private Parameter fitting_;
    } //dynamics

    //! Analytical term-structure fitting parameter \f$ \varphi(t) \f$.
    /*! \f$ \varphi(t) \f$ is analytically defined by
    \f[
    \varphi(t) = f(t) +
    \frac{1}{2}(\frac{\sigma(1-e^{-at})}{a})^2 +
    \frac{1}{2}(\frac{\eta(1-e^{-bt})}{b})^2 +
    \rho\frac{\sigma(1-e^{-at})}{a}\frac{\eta(1-e^{-bt})}{b},
    \f]
    where \f$ f(t) \f$ is the instantaneous forward rate at \f$ t \f$.
     */
    //   class G2::FittingParameter : public TermStructureFittingParameter {
    // CAN WE USE THE FITTING PARAMETER OF THE PACKAGE???
    @Override
    public TwoFactorModel.ShortRateDynamics dynamics() {
        return (new Dynamics(phi_, a(), sigma(), b(), eta(), rho()));
    }

    public void generateArguments() {

        phi_ = new TwoFactorFittingParameter(termStructureConsistentModelClass.termStructure(),
                a(), sigma(), b(), eta(), rho());
    }

    @Override
    public double /*@Real*/ discountBondOption(Option.Type type, double /*@Real*/ strike, double /*@Time*/ maturity,
            double /*@Time*/ bondMaturity) {

        double /*@Real*/ v = sigmaP(maturity, bondMaturity);
        double /*@Real*/ f = termStructureConsistentModelClass.termStructure().getLink().discount(bondMaturity);
        double /*@Real*/ k = termStructureConsistentModelClass.termStructure().getLink().discount(maturity) * strike;

        return blackFormula(type, k, f, v);
    }

    double /*@Real*/ A(double /*@Time*/ t, double /*@Time*/ T) {
        return termStructureConsistentModelClass.termStructure().getLink().discount(T) / termStructureConsistentModelClass.termStructure().getLink().discount(t) *
                Math.exp(0.5 * (V(T - t) - V(T) + V(t)));
    }

    double /*@Real*/ B(double /*@Real*/ x, double /*@Time*/ t) {
        return (1.0 - Math.exp(-x * t)) / x;
    }

    //inner class
    class SwaptionPricingFunction {

        public SwaptionPricingFunction(double /*@Real*/ a, double /*@Real*/ sigma,
                double /*@Real*/ b, double /*@Real*/ eta, double /*@Real*/ rho,
                double /*@Real*/ w, double /*@Real*/ start,
                final /*std::vector<Time>& */ ArrayList<Double /*@Time*/> payTimes,
                double /*@Rate*/ fixedRate, final G2 model) {
            a_ = (a);
            sigma_ = (sigma);
            b_ = (b);
            eta_ = (eta);
            rho_ = (rho);
            w_ = (w);

            T_ = (start);
            t_ = (payTimes);
            rate_ = (fixedRate);
            size_ = (t_.size());

            A_ = new Array(size_);
            Ba_ = new Array(size_);
            Bb_ = new Array(size_);


            sigmax_ = sigma_ * Math.sqrt(0.5 * (1.0 - Math.exp(-2.0 * a_ * T_)) / a_);
            sigmay_ = eta_ * Math.sqrt(0.5 * (1.0 - Math.exp(-2.0 * b_ * T_)) / b_);
            rhoxy_ = rho_ * eta_ * sigma_ * (1.0 - Math.exp(-(a_ + b_) * T_)) /
                    ((a_ + b_) * sigmax_ * sigmay_);

            double /*@Real*/ temp = sigma_ * sigma_ / (a_ * a_);
            mux_ = -((temp + rho_ * sigma_ * eta_ / (a_ * b_)) * (1.0 - Math.exp(-a * T_)) -
                    0.5 * temp * (1.0 - Math.exp(-2.0 * a_ * T_)) -
                    rho_ * sigma_ * eta_ / (b_ * (a_ + b_)) *
                    (1.0 - Math.exp(-(b_ + a_) * T_)));

            temp = eta_ * eta_ / (b_ * b_);
            muy_ = -((temp + rho_ * sigma_ * eta_ / (a_ * b_)) * (1.0 - Math.exp(-b * T_)) -
                    0.5 * temp * (1.0 - Math.exp(-2.0 * b_ * T_)) -
                    rho_ * sigma_ * eta_ / (a_ * (a_ + b_)) *
                    (1.0 - Math.exp(-(b_ + a_) * T_)));

            for (int /*@Size*/ i = 0; i < size_; i++) {
                /*
                A_[i] = model.A(T_, t_[i]);
                Ba_[i] = model.B(a_, t_[i]-T_);
                Bb_[i] = model.B(b_, t_[i]-T_);
                 */
                A_.set(i, model.A(T_, t_.get(i)));
                Ba_.set(i, model.B(a_, t_.get(i) - T_));
                Bb_.set(i, model.B(b_, t_.get(i) - T_));
            }
        }

        double /*@Real*/ mux() {
            return mux_;
        }

        double /*@Real*/ sigmax() {
            return sigmax_;
        }

        double /*@Real*/ getOperatorEq(double /*@Real*/ x) {
            CumulativeNormalDistribution phi = new CumulativeNormalDistribution();
            double /*@Real*/ temp = (x - mux_) / sigmax_;
            double /*@Real*/ txy = Math.sqrt(1.0 - rhoxy_ * rhoxy_);

            Array lambda = new Array(size_);
            int /*@Size*/ i;
            for (i = 0; i < size_; i++) {
                double /*@Real*/ tau = (i == 0 ? t_.get(0) - T_ : t_.get(i) - t_.get(i - 1));
                double /*@Real*/ c = (i == size_ - 1 ? (1.0 + rate_ * tau) : rate_ * tau);
                lambda.set(i, c * A_.get(i) * Math.exp(-Ba_.at(i) * x));
            }

            SolvingFunction function = new SolvingFunction(lambda, Bb_);
            Brent s1d = new Brent();
            s1d.setMaxEvaluations(1000);

            //brent does not have solve
            //      double /*@Real*/ yb = s1d.solve(function, 1e-6, 0.00, -100.0, 100.0);
            double /*@Real*/ yb = 0.0;
            double /*@Real*/ h1 = (yb - muy_) / (sigmay_ * txy) -
                    rhoxy_ * (x - mux_) / (sigmax_ * txy);
            //not sure if evaluate method is equivalent of op overloading
            double /*@Real*/ value = /*phi(-w_*h1)*/ phi.evaluate(-w_ * h1);


            for (i = 0; i < size_; i++) {
                double /*@Real*/ h2 = h1 +
                        Bb_.get(i) * sigmay_ * Math.sqrt(1.0 - rhoxy_ * rhoxy_);
                double /*@Real*/ kappa = -Bb_.get(i) *
                        (muy_ - 0.5 * txy * txy * sigmay_ * sigmay_ * Bb_.get(i) +
                        rhoxy_ * sigmay_ * (x - mux_) / sigmax_);
                // operator overloading problem again
                value -= lambda.get(i) * Math.exp(kappa) * /*phi(-w_*h2)*/ phi.evaluate(-w_ * h2);
            }

            return Math.exp(-0.5 * temp * temp) * value /
                    (sigmax_ * Math.sqrt(2.0 * M_PI));
        }

        private class SolvingFunction {

            public SolvingFunction(final Array lambda, final Array Bb) {
                lambda_ = (lambda);
                Bb_ = (Bb);
            }

            public double /*@Real*/ getOperatorEq(double /*@Real*/ y) {
                double /*@Real*/ value = 1.0;
                for (int /*@Size*/ i = 0; i < lambda_.size(); i++) {
                    value -= lambda_.get(i) * Math.exp(-Bb_.get(i) * y);
                }
                return value;
            }
            private Array lambda_;
            private Array Bb_;
        }
        double /*@Real*/ a_, sigma_, b_, eta_, rho_, w_;
        double /*@Time*/ T_;
        /*  std::vector<Time> */ ArrayList<Double /*@Time*/> t_;
        double /*@Rate*/ rate_;
        int /*@Size*/ size_;
        Array A_, Ba_, Bb_;
        double /*@Real*/ mux_, muy_, sigmax_, sigmay_, rhoxy_;
    }
    /*
    public     double /*@Real   swaption(final Swaption ::arguments& arguments,
    Rate fixedRate, Real range, Size intervals) const {

    Date settlement = termStructure()->referenceDate();
    DayCounter dayCounter = termStructure()->dayCounter();
    Time start = dayCounter.yearFraction(settlement,
    arguments.floatingResetDates[0]);
    Real w = (arguments.type==VanillaSwap::Payer ? 1 : -1 );

    std::vector<Time> fixedPayTimes(arguments.fixedPayDates.size());
    for (Size i=0; i<fixedPayTimes.size(); ++i)
    fixedPayTimes[i] =
    dayCounter.yearFraction(settlement,
    arguments.fixedPayDates[i]);

    SwaptionPricingFunction function(a(), sigma(), b(), eta(), rho(),
    w, start,
    fixedPayTimes,
    fixedRate, (*this));

    Real upper = function.mux() + range*function.sigmax();
    Real lower = function.mux() - range*function.sigmax();
    SegmentIntegral integrator(intervals);
    return arguments.nominal*w*termStructure()->discount(start)*
    integrator(function, lower, upper);
    }
     */
}
