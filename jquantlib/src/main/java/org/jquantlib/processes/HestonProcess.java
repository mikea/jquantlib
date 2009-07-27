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

import org.jquantlib.math.Array;
import org.jquantlib.math.Constants;
import org.jquantlib.math.Matrix;
import org.jquantlib.math.distributions.CumulativeNormalDistribution;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.RelinkableHandle;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.termstructures.Compounding;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.util.Date;

//TODO: code review: class comments
public class HestonProcess extends StochasticProcess {

    private Handle<YieldTermStructure> riskFreeRate_, dividendYield_;
    private Handle<Quote> s0_;
    private RelinkableHandle<Quote> v0_, kappa_, theta_, sigma_, rho_;

    private enum Discretization {
        PartialTruncation, FullTruncation, Reflection, ExactVariance
    };

    private final Discretization discretization_;

    private double s0v_, v0v_, kappav_, thetav_, sigmav_, rhov_, sqrhov_;

    public HestonProcess(final Handle<YieldTermStructure> riskFreeRate, final Handle<YieldTermStructure> dividendYield,
            final Handle<Quote> s0, double v0, double kappa, double theta, double sigma, double rho) {
        this(riskFreeRate, dividendYield, s0, v0, kappa, theta, sigma, rho, Discretization.FullTruncation);
    }

    public HestonProcess(final Handle<YieldTermStructure> riskFreeRate, final Handle<YieldTermStructure> dividendYield,
            final Handle<Quote> s0, double v0, double kappa, double theta, double sigma, double rho, Discretization d) {
        // TODO: code review :: super(new EulerDiscretization());
        this.riskFreeRate_ = (riskFreeRate);
        this.dividendYield_ = (dividendYield);
        this.s0_ = (s0); // TODO: code review
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

//XXX
//    public double size() {
//        return 2;
//    }

    public final RelinkableHandle<Quote> v0() {
        return v0_;
    }

    public final RelinkableHandle<Quote> rho() {
        return rho_;
    }

    public final RelinkableHandle<Quote> kappa() {
        return kappa_;
    }

    public final RelinkableHandle<Quote> theta() {
        return theta_;
    }

    public final RelinkableHandle<Quote> sigma() {
        return sigma_;
    }

    public final Handle<Quote> s0() {
        return s0_;
    }

    public final Handle<YieldTermStructure> dividendYield() {
        return dividendYield_;
    }

    public Handle<YieldTermStructure> riskFreeRate() {
        return riskFreeRate_;
    }

    public final/* @Time */double time(final Date d) {
        return riskFreeRate_.getLink().dayCounter().yearFraction(riskFreeRate_.getLink().referenceDate(), d);
    }

    
    //
    // Overrides StochasticProcess
    //
    
    @Override
    public Array initialValues() {
        return new Array( new double[] { s0v_, v0v_ } );
    }

    @Override
    public int getSize() {
        return 2;
    }

    @Override
    public Array drift(/* Time */double t, final Array x) {
        final double x1 = x.get(1);
        final double vol = (x1 > 0.0)
                         ? Math.sqrt(x1) 
                         : (discretization_ == Discretization.PartialTruncation) 
                             ? -Math.sqrt(-x1) 
                             : 0.0;

        double[] result = new double[2];
        result[0] = riskFreeRate_.getLink().forwardRate(t, t, Compounding.CONTINUOUS).rate()
                    - dividendYield_.getLink().forwardRate(t, t, Compounding.CONTINUOUS).rate() - 0.5 * vol * vol;

        result[1] = kappav_ * (thetav_ - ((discretization_ == Discretization.PartialTruncation) ? x1 : vol * vol));
        return new Array(result);
    }

    @Override
    public Matrix diffusion(/* @Time */double time, final Array x) {
        /*
         * the correlation matrix is | 1 rho | | rho 1 | whose square root (which is used here) is | 1 0 | | rho sqrt(1-rho^2) |
         */
        final double x1 = x.get(1);
        final double vol = (x1 > 0.0) 
                         ? Math.sqrt(x1) 
                         : (discretization_ == Discretization.Reflection) 
                             ? -Math.sqrt(-x1)
                             : 0.0;
        final double sigma2 = sigmav_ * vol;

        Matrix result = new Matrix(2, 2);
        result.set(0, 0, vol);
        result.set(0, 1, 0.0);
        result.set(1, 0, rhov_ * sigma2);
        result.set(1, 1, sqrhov_ * sigma2);
        return result;
    }

    @Override
    public Array apply(final Array x0, final Array dx) {
        double[] tmp = new double[2];
        tmp[0] = x0.get(0) * Math.exp(dx.get(0));
        tmp[1] = x0.get(1) + dx.get(1);
        return new Array(tmp);
    }

    @Override
    public Array evolve(/* @Time */double t0, final Array x0, /* @Time */double dt, final Array dw) {
        double[] retVal = new double[2];
        double ncp, df, p, dy;
        double vol, vol2, mu, nu;

        final double sdt = Math.sqrt(dt);

        final double x00 = x0.get(0);
        final double x01 = x0.get(1);
        final double dw0 = dw.get(0);
        final double dw1 = dw.get(1);
        
        switch (discretization_) {
        // For the definition of PartialTruncation, FullTruncation
        // and Reflection see Lord, R., R. Koekkoek and D. van Dijk (2006),
        // "A Comparison of biased simulation schemes for
        // stochastic volatility models",
        // Working Paper, Tinbergen Institute
        case PartialTruncation:
            vol = (x01 > 0.0) ? Math.sqrt(x01) : 0.0;
            vol2 = sigmav_ * vol;
            mu = riskFreeRate_.getLink().forwardRate(t0, t0, Compounding.CONTINUOUS).rate()
                    - dividendYield_.getLink().forwardRate(t0, t0, Compounding.CONTINUOUS).rate() - 0.5 * vol * vol;
            nu = kappav_ * (thetav_ - x01);

            retVal[0] = x00 * Math.exp(mu * dt + vol * dw0 * sdt);
            retVal[1] = x01 + nu * dt + vol2 * sdt * (rhov_ * dw0 + sqrhov_ * dw1);
            break;
        case FullTruncation:
            vol = (x01 > 0.0) ? Math.sqrt(x01) : 0.0;
            vol2 = sigmav_ * vol;
            mu = riskFreeRate_.getLink().forwardRate(t0, t0, Compounding.CONTINUOUS).rate()
                    - dividendYield_.getLink().forwardRate(t0, t0, Compounding.CONTINUOUS).rate() - 0.5 * vol * vol;
            nu = kappav_ * (thetav_ - vol * vol);

            retVal[0] = x00 * Math.exp(mu * dt + vol * dw0 * sdt);
            retVal[1] = x01 + nu * dt + vol2 * sdt * (rhov_ * dw0 + sqrhov_ * dw1);
            break;
        case Reflection:
            vol = Math.sqrt(Math.abs(x01));
            vol2 = sigmav_ * vol;
            mu = riskFreeRate_.getLink().forwardRate(t0, t0, Compounding.CONTINUOUS).rate()
                    - dividendYield_.getLink().forwardRate(t0, t0, Compounding.CONTINUOUS).rate() - 0.5 * vol * vol;
            nu = kappav_ * (thetav_ - vol * vol);

            retVal[0] = x00 * Math.exp(mu * dt + vol * dw0 * sdt);
            retVal[1] = vol * vol + nu * dt + vol2 * sdt * (rhov_ * dw0 + sqrhov_ * dw1);
            break;
        case ExactVariance:
            // use Alan Lewis trick to decorrelate the equity and the variance
            // process by using y(t)=x(t)-\frac{rho}{sigma}\nu(t)
            // and Ito's Lemma. Then use exact sampling for the variance
            // process. For further details please read the wilmott thread
            // "QuantLib code is very high quatlity"
            vol = (x01 > 0.0) ? Math.sqrt(x01) : 0.0;
            mu = riskFreeRate_.getLink().forwardRate(t0, t0, Compounding.CONTINUOUS).rate()
                    - dividendYield_.getLink().forwardRate(t0, t0, Compounding.CONTINUOUS).rate() - 0.5 * vol * vol;

            df = 4 * thetav_ * kappav_ / (sigmav_ * sigmav_);
            ncp = 4 * kappav_ * Math.exp(-kappav_ * dt) / (sigmav_ * sigmav_ * (1 - Math.exp(-kappav_ * dt))) * x01;

            p = new CumulativeNormalDistribution().op(dw1);
            if (p < 0.0)
                p = 0.0;
            else if (p >= 1.0)
                p = 1.0 - Constants.QL_EPSILON;

            retVal[1] = sigmav_ * sigmav_ * (1 - Math.exp(-kappav_ * dt)) / (4 * kappav_);
            if (true) {
                throw new UnsupportedOperationException("Work in progress");
            }
            // new InverseNonCentralChiSquareDistribution(df, ncp, 100).(p);

            dy = (mu - rhov_ / sigmav_ * kappav_ * (thetav_ - vol * vol)) * dt + vol * sqrhov_ * dw0 * sdt;

            retVal[0] = x00 * Math.exp(dy + rhov_ / sigmav_ * (retVal[1] - x01));
            break;
        default:
            throw new IllegalArgumentException("unknown discretization schema");
        }

        return new Array( retVal );
    }

}
