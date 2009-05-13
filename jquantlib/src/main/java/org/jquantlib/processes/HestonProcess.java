/*
 Copyright (C) 2009 Ueli Hofstetter

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

package org.jquantlib.processes;

import java.sql.Time;

import org.jquantlib.math.Array;
import org.jquantlib.math.Matrix;
import org.jquantlib.model.shortrate.Disposable;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.RelinkableHandle;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.termstructures.Compounding;
import org.jquantlib.termstructures.YieldTermStructure;


public class HestonProcess extends StochasticProcess {

    private Handle<YieldTermStructure> riskFreeRate_, dividendYield_;
    private Handle<Quote> s0_;
    private RelinkableHandle<Quote> v0_, kappa_, theta_, sigma_, rho_;

    enum Discretization {
        PartialTruncation, FullTruncation, Reflection, ExactVariance
    };

    final Discretization discretization_;

    private double s0v_, v0v_, kappav_, thetav_, sigmav_, rhov_, sqrhov_;

    public HestonProcess(final Handle<YieldTermStructure> riskFreeRate, final Handle<YieldTermStructure> dividendYield,
            final Handle<Quote> s0, double v0, double kappa, double theta, double sigma, double rho) {
        this(riskFreeRate, dividendYield, s0, v0, kappa, theta, sigma, rho, Discretization.FullTruncation);
    }

    public HestonProcess(final Handle<YieldTermStructure> riskFreeRate, final Handle<YieldTermStructure> dividendYield,
            final Handle<Quote> s0, double v0, double kappa, double theta, double sigma, double rho, Discretization d) {
        // super(new EulerDiscretization());
        this.riskFreeRate_ = (riskFreeRate);
        this.dividendYield_ = (dividendYield);
        this.s0_ = (s0);
        this.v0_ = new RelinkableHandle<Quote>(new SimpleQuote(v0));
        this.kappa_ = new RelinkableHandle<Quote>(new SimpleQuote(kappa));
        this.theta_ = new RelinkableHandle<Quote>(new SimpleQuote(theta));
        this.sigma_ = new RelinkableHandle<Quote>(new SimpleQuote(sigma));
        this.rho_ = new RelinkableHandle<Quote>(new SimpleQuote(rho));
        this.discretization_ = (d);

        riskFreeRate_.addObserver(this);

        riskFreeRate_.addObserver(this);
        dividendYield_.addObserver(this);
        s0_.addObserver(this);
        v0_.addObserver(this);
        kappa_.addObserver(this);
        theta_.addObserver(this);
        sigma_.addObserver(this);
        rho_.addObserver(this);

        // update();
    }

    public void update() {
        // helper variables to improve performance
        s0v_ = s0_.getLink().evaluate();
        v0v_ = v0_.getLink().evaluate();
        kappav_ = kappa_.getLink().evaluate();
        thetav_ = theta_.getLink().evaluate();
        sigmav_ = sigma_.getLink().evaluate();
        rhov_ = rho_.getLink().evaluate();
        sqrhov_ = Math.sqrt(1.0 - rhov_ * rhov_);

        // this->StochasticProcess::update();
    }

    public double size() {
        return 2;
    }

    public double[] initialValues() {
        double[] tmp = new double[2];
        tmp[0] = s0v_;
        tmp[1] = v0v_;
        return tmp;
    }

    public/* Disposable<Array> */double[] drift(/* Time */double t, final double[] x) {
        double[] tmp = new double[2];
        final double vol = (x[1] > 0.0) ? Math.sqrt(x[1]) : (discretization_ == Discretization.PartialTruncation) ? -Math
                .sqrt(-x[1]) : 0.0;

        tmp[0] = riskFreeRate_.getLink().forwardRate(t, t, Compounding.CONTINUOUS).rate()
                - dividendYield_.getLink().forwardRate(t, t, Compounding.CONTINUOUS).rate() - 0.5 * vol * vol;

        tmp[1] = kappav_ * (thetav_ - ((discretization_ == Discretization.PartialTruncation) ? x[1] : vol * vol));
        return tmp;
    }

    //
    // Disposable<Matrix> HestonProcess::diffusion(Time, const Array& x) const {
    // /* the correlation matrix is
    // | 1 rho |
    // | rho 1 |
    // whose square root (which is used here) is
    // | 1 0 |
    // | rho sqrt(1-rho^2) |
    // */
    // Matrix tmp(2,2);
    // const Real vol = (x[1] > 0.0) ? std::sqrt(x[1])
    // : (discretization_ == Reflection) ? -sqrt(-x[1])
    // : 0.0;
    // const Real sigma2 = sigmav_ * vol;
    //
    // tmp[0][0] = vol; tmp[0][1] = 0.0;
    // tmp[1][0] = rhov_*sigma2; tmp[1][1] = sqrhov_*sigma2;
    // return tmp;
    // }
    //
    // Disposable<Array> HestonProcess::apply(const Array& x0,
    // const Array& dx) const {
    // Array tmp(2);
    // tmp[0] = x0[0] * std::exp(dx[0]);
    // tmp[1] = x0[1] + dx[1];
    // return tmp;
    // }
    //
    // Disposable<Array> HestonProcess::evolve(Time t0, const Array& x0,
    // Time dt, const Array& dw) const {
    // Array retVal(2);
    // Real ncp, df, p, dy;
    // Real vol, vol2, mu, nu;
    //
    // const Real sdt = std::sqrt(dt);
    //
    // switch (discretization_) {
    // // For the definition of PartialTruncation, FullTruncation
    // // and Reflection see Lord, R., R. Koekkoek and D. van Dijk (2006),
    // // "A Comparison of biased simulation schemes for
    // // stochastic volatility models",
    // // Working Paper, Tinbergen Institute
    // case PartialTruncation:
    // vol = (x0[1] > 0.0) ? std::sqrt(x0[1]) : 0.0;
    // vol2 = sigmav_ * vol;
    // mu = riskFreeRate_->forwardRate(t0, t0, Continuous)
    // - dividendYield_->forwardRate(t0, t0, Continuous)
    // - 0.5 * vol * vol;
    // nu = kappav_*(thetav_ - x0[1]);
    //             
    // retVal[0] = x0[0] * std::exp(mu*dt+vol*dw[0]*sdt);
    // retVal[1] = x0[1] + nu*dt + vol2*sdt*(rhov_*dw[0] + sqrhov_*dw[1]);
    // break;
    // case FullTruncation:
    // vol = (x0[1] > 0.0) ? std::sqrt(x0[1]) : 0.0;
    // vol2 = sigmav_ * vol;
    // mu = riskFreeRate_->forwardRate(t0, t0, Continuous)
    // - dividendYield_->forwardRate(t0, t0, Continuous)
    // - 0.5 * vol * vol;
    // nu = kappav_*(thetav_ - vol*vol);
    //             
    // retVal[0] = x0[0] * std::exp(mu*dt+vol*dw[0]*sdt);
    // retVal[1] = x0[1] + nu*dt + vol2*sdt*(rhov_*dw[0] + sqrhov_*dw[1]);
    // break;
    // case Reflection:
    // vol = std::sqrt(std::fabs(x0[1]));
    // vol2 = sigmav_ * vol;
    // mu = riskFreeRate_->forwardRate(t0, t0, Continuous)
    // - dividendYield_->forwardRate(t0, t0, Continuous)
    // - 0.5 * vol*vol;
    // nu = kappav_*(thetav_ - vol*vol);
    //
    // retVal[0] = x0[0]*std::exp(mu*dt+vol*dw[0]*sdt);
    // retVal[1] = vol*vol
    // +nu*dt + vol2*sdt*(rhov_*dw[0] + sqrhov_*dw[1]);
    // break;
    // case ExactVariance:
    // // use Alan Lewis trick to decorrelate the equity and the variance
    // // process by using y(t)=x(t)-\frac{rho}{sigma}\nu(t)
    // // and Ito's Lemma. Then use exact sampling for the variance
    // // process. For further details please read the wilmott thread
    // // "QuantLib code is very high quatlity"
    // vol = (x0[1] > 0.0) ? std::sqrt(x0[1]) : 0.0;
    // mu = riskFreeRate_->forwardRate(t0, t0, Continuous)
    // - dividendYield_->forwardRate(t0, t0, Continuous)
    // - 0.5 * vol*vol;
    //
    // df = 4*thetav_*kappav_/(sigmav_*sigmav_);
    // ncp = 4*kappav_*std::exp(-kappav_*dt)
    // /(sigmav_*sigmav_*(1-std::exp(-kappav_*dt)))*x0[1];
    //
    // p = CumulativeNormalDistribution()(dw[1]);
    // if (p<0.0)
    // p = 0.0;
    // else if (p >= 1.0)
    // p = 1.0-QL_EPSILON;
    //
    // retVal[1] = sigmav_*sigmav_*(1-std::exp(-kappav_*dt))/(4*kappav_)
    // *InverseNonCentralChiSquareDistribution(df, ncp, 100)(p);
    //
    // dy = (mu - rhov_/sigmav_*kappav_
    // *(thetav_-vol*vol)) * dt + vol*sqrhov_*dw[0]*sdt;
    //
    // retVal[0] = x0[0]*std::exp(dy + rhov_/sigmav_*(retVal[1]-x0[1]));
    // break;
    // default:
    // QL_FAIL("unknown discretization schema");
    // }
    //    
    // return retVal;
    // }
    //
    // const RelinkableHandle<Quote>& HestonProcess::v0() const {
    // return v0_;
    // }
    //
    // const RelinkableHandle<Quote>& HestonProcess::rho() const {
    // return rho_;
    // }
    //
    // const RelinkableHandle<Quote>& HestonProcess::kappa() const {
    // return kappa_;
    // }
    //
    // const RelinkableHandle<Quote>& HestonProcess::theta() const {
    // return theta_;
    // }
    //
    // const RelinkableHandle<Quote>& HestonProcess::sigma() const {
    // return sigma_;
    // }
    //
    // const Handle<Quote>& HestonProcess::s0() const {
    // return s0_;
    // }
    //
    // const Handle<YieldTermStructure>& HestonProcess::dividendYield() const {
    // return dividendYield_;
    // }
    //
    // const Handle<YieldTermStructure>& HestonProcess::riskFreeRate() const {
    // return riskFreeRate_;
    // }
    //
    // Time HestonProcess::time(const Date& d) const {
    // return riskFreeRate_->dayCounter().yearFraction(
    // riskFreeRate_->referenceDate(), d);
    // }
    //    

    @Override
    public double[][] diffusion(double t, double[] x) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getSize() {
        // TODO Auto-generated method stub
        return 0;
    }

}
