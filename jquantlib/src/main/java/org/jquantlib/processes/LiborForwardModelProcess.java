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

package org.jquantlib.processes;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.cashflow.CashFlow;
import org.jquantlib.cashflow.IborCoupon;
import org.jquantlib.cashflow.Leg;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.indexes.IborIndex;
import org.jquantlib.math.Array;
import org.jquantlib.math.Matrix;
import org.jquantlib.util.Date;
import org.jquantlib.util.stdlibc.Std;

import com.sun.org.apache.regexp.internal.recompile;

//! libor-forward-model process
/*! stochastic process of a libor forward model using the
    rolling forward measure incl. predictor-corrector step

    References:

    Glasserman, Paul, 2004, Monte Carlo Methods in Financial Engineering,
    Springer, Section 3.7

    Antoon Pelsser, 2000, Efficient Methods for Valuing Interest Rate
    Derivatives, Springer, 8

    Hull, John, White, Alan, 1999, Forward Rate Volatilities, Swap Rate
    Volatilities and the Implementation of the Libor Market Model
    (<http://www.rotman.utoronto.ca/~amackay/fin/libormktmodel2.pdf>)

    \test the correctness is tested by Monte-Carlo reproduction of
          caplet & ratchet NPVs and comparison with Black pricing.

    \warning this class does not work correctly with Visual C++ 6.

    \ingroup processes
*/
public class LiborForwardModelProcess extends StochasticProcess {

    private int size_;
    private final IborIndex index_;
    private LfmCovarianceParameterization lfmParam_;
    private Array initialValues_;
    private List</*@Time*/Double> fixingTimes_;
    private List</*@Time*/Date> fixingDates_;
    private List</*@Time*/Double> accrualStartTimes_;
    private List</*@Time*/Double> accrualEndTimes_;
    private List</*@Time*/Double> accrualPeriod_;
    
    //FIXME: replace Array by double[] wherever possible
    private  Array m1, m2;
    
    public LiborForwardModelProcess(
            int size,
            final IborIndex  index){
        super(new EulerDiscretization());
        this.size_ = size;
        this.index_ = index;
        this.initialValues_ = new Array(size_);
        this.fixingDates_ = new ArrayList<Date>(size_);
        this.fixingTimes_ = new ArrayList<Double>(size_);
        this.accrualStartTimes_ = new ArrayList<Double>(size_);
        this.accrualEndTimes_ = new ArrayList<Double>(size_);
        this.accrualPeriod_ = new ArrayList<Double>(size_);
        this.m1 = new Array(size_);
        this.m2 = new Array(size_);

        final DayCounter dayCounter = index_.getDayCounter();
        final List<CashFlow> flows = null;// cashFlows();

        if (size_ != flows.size()) {
            throw new IllegalArgumentException("wrong number of cashflows");
        }

        final Date settlement = index_.getTermStructure().getLink().referenceDate();
        final Date startDate = ((IborCoupon) flows.get(0)).fixingDate();
        for (int i = 0; i < size_; ++i) {
            IborCoupon coupon = (IborCoupon) flows.get(i);

            if (!coupon.date().eq(coupon.accrualEndDate())) {
                throw new IllegalArgumentException("irregular coupon types are not suppported");
            }

            initialValues_.set(i, coupon.rate());
            accrualPeriod_.set(i, coupon.accrualPeriod());

            fixingDates_.set(i, coupon.fixingDate());
            fixingTimes_.set(i, dayCounter.yearFraction(startDate, coupon.fixingDate()));
            accrualStartTimes_.set(i, dayCounter.yearFraction(settlement, coupon.accrualStartDate()));
            accrualEndTimes_.set(i, dayCounter.yearFraction(settlement, coupon.accrualEndDate()));
        }
    }

    public Array drift(/* @Time */double t, final Array x) {
        Array f = new Array(size_, 0.0);
        Matrix covariance = new Matrix(lfmParam_.covariance(t, x));
        final int m = 0;// nextIndexReset(t);
        for (int k = m; k < size_; ++k) {
            m1.set(k, accrualPeriod_.get(k) * x.get(k) / (1 + accrualPeriod_.get(k) * x.get(k)));
            f.set(k, Std.getInstance().inner_product(m1.getData(), m, covariance.getColumn(k), m, k+1-m, 0.0 ) - 0.5 * covariance.get(k, k));
        }
        return f;
    }
    
    public Matrix diffusion(/*@Time*/ double t, final Array x){
        return lfmParam_.diffusion(t, x);
    }
    
    public Matrix covariance(/*@Time*/double t, final Array x, /*@Time*/ double dt){
        return lfmParam_.covariance(dt, x).operatorMultiply(lfmParam_.covariance(dt, x), dt);
    }
    
    public Array apply(final Array x0, final Array dx){
        Array tmp = new Array(size_);
        for(int k = 0; k<size_; ++k){
            tmp.set(k, x0.get(k)*Math.exp(dx.get(k)));
        }
        return tmp;
    }
    
    public Array evolve(/*@Time*/ double t0, final Array x0,
            /*@Time*/ double dt, final Array dw)  {
        /* predictor-corrector step to reduce discretization errors.

           Short - but slow - solution would be

           Array rnd_0     = stdDeviation(t0, x0, dt)*dw;
           Array drift_0   = discretization_->drift(*this, t0, x0, dt);

           return apply(x0, ( drift_0 + discretization_
                ->drift(*this,t0,apply(x0, drift_0 + rnd_0),dt) )*0.5 + rnd_0);

           The following implementation does the same but is faster.
        */
        if(true){throw new UnsupportedOperationException("work in progress");}
        final int m   = 0;//nextIndexReset(t0);
        final double sdt = Math.sqrt(dt);

        Array f = new Array(x0);
        Matrix diff       = lfmParam_.diffusion(t0, x0);
        Matrix covariance = lfmParam_.covariance(t0, x0);

        for (int k=m; k<size_; ++k) {
            final double y = accrualPeriod_.get(k)*x0.get(k);
            m1.set(k,y/(1+y));
            final double d = (
                Std.getInstance().inner_product(m1.getData(), m, covariance.getColumn(k), m, k+1-m,0.0)
                -0.5*covariance.get(k, k)) * dt;

            final double r = Std.getInstance().inner_product(diff.getRow(k), dw.getData(), 0.0)*sdt;

            final double x = y*Math.exp(d + r);
            m2.set(k, x/(1+x));
            f.set(k, x0.get(k) * Math.exp(0.5*(d+
                 (Std.getInstance().inner_product(m2.getData(), m, covariance.getColumn(k), m, k+1-m,0.0)
                  -0.5*covariance.get(k,k))*dt)+ r));
        }

        return f;
    }
   public double[] initialValues()  {
        Array tmp = new Array(initialValues_);
        return tmp.getData();
    }

    public void setCovarParam(final LfmCovarianceParameterization  param) {
        lfmParam_ = param;
    }

    public LfmCovarianceParameterization covarParam()  {
        return lfmParam_;
    }

    public IborIndex index() {
        return index_;
    }

    @Override
    public double[][] diffusion(double t, double[] x) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public double[] drift(double t, double[] x) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getSize() {
        // TODO Auto-generated method stub
        return 0;
    }
}
/*
    public List<CashFlow> cashFlows{

        final Date refDate = index_.getTermStructure().getLink().referenceDate();
        
        List<CashFlow> floatingLeg = Leg.FloatingLe, schedule, index, paymentDayCounter, paymentAdj, fixingDays, gearings, spreads, caps, floors, isInArrears)(
                   std::vector<Real>(1, amount),
                   Schedule(refDate,
                            refDate + Period(index_->tenor().length()*size_,
                                             index_->tenor().units()),
                            index_->tenor(), index_->fixingCalendar(),
                            index_->businessDayConvention(),
                            index_->businessDayConvention(), false, false),
                   index_,
                   index_->dayCounter(),
                   index_->businessDayConvention(),
                   index_->fixingDays());
        boost::shared_ptr<IborCouponPricer>
                        fictitiousPricer(new BlackIborCouponPricer(Handle<CapletVolatilityStructure>()));
        setCouponPricer(floatingLeg,fictitiousPricer);
        return floatingLeg;

    }

    Size LiborForwardModelProcess::size() const {
        return size_;
    }

    Size LiborForwardModelProcess::factors() const {
        return lfmParam_->factors();
    }

    const std::vector<Time> & LiborForwardModelProcess::fixingTimes() const {
        return fixingTimes_;
    }

    const std::vector<Date> & LiborForwardModelProcess::fixingDates() const {
        return fixingDates_;
    }

    const std::vector<Time> &
    LiborForwardModelProcess::accrualStartTimes() const {
        return accrualStartTimes_;
    }

    const std::vector<Time> &
    LiborForwardModelProcess::accrualEndTimes() const {
        return accrualEndTimes_;
    }

    Size LiborForwardModelProcess::nextIndexReset(Time t) const {
        return std::upper_bound(fixingTimes_.begin(), fixingTimes_.end(), t)
                 - fixingTimes_.begin();
    }

    std::vector<DiscountFactor> LiborForwardModelProcess::discountBond(
        const std::vector<Rate> & rates) const {

        std::vector<DiscountFactor> discountFactors(size_);
        discountFactors[0] = 1.0/(1.0 + rates[0]*accrualPeriod_[0]);

        for (Size i = 1; i < size_; ++i) {
            discountFactors[i] =
                discountFactors[i-1]/(1.0 + rates[i]*accrualPeriod_[i]);
        }

        return discountFactors;
    }*/
        
