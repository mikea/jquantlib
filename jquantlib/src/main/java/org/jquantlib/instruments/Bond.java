/*
 Copyright (C) 2007 Srinivas Hasti, Daniel Kong

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
/*
 Copyright (C) 2004 Jeff Yu
 Copyright (C) 2004 M-Dimension Consulting Inc.
 Copyright (C) 2005, 2006, 2007 StatPro Italia srl
 Copyright (C) 2007, 2008 Ferdinando Ametrano
 Copyright (C) 2007 Chiara Fornarola
 Copyright (C) 2008 Simon Ibbotson

 This file is part of QuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://quantlib.org/

 QuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <quantlib-dev@lists.sf.net>. The license is also available online at
 <http://quantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 */

package org.jquantlib.instruments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.jquantlib.QL;
import org.jquantlib.Settings;
import org.jquantlib.cashflow.CashFlow;
import org.jquantlib.cashflow.CashFlows;
import org.jquantlib.cashflow.Coupon;
import org.jquantlib.cashflow.Leg;
import org.jquantlib.cashflow.SimpleCashFlow;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.lang.reflect.ReflectConstants;
import org.jquantlib.math.Closeness;
import org.jquantlib.math.Constants;
import org.jquantlib.math.Ops.DoubleOp;
import org.jquantlib.math.solvers1D.Brent;
import org.jquantlib.pricingengines.GenericEngine;
import org.jquantlib.pricingengines.PricingEngine;
import org.jquantlib.pricingengines.bond.DiscountingBondEngine;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.termstructures.Compounding;
import org.jquantlib.termstructures.InterestRate;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.termstructures.yieldcurves.ZeroSpreadedTermStructure;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.Frequency;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.util.Observer;

/**
 * Base bond class
 *
 * Derived classes must fill the uninitialized data members. Warning: Most
 * methods assume that the cash flows are stored sorted by date, the
 * redemption(s) being after any cash flow at the same date. In particular, if
 * there's one single redemption, it must be the last cash flow,
 *
 * @category instruments
 *
 *           Tests: @see ...... //FIXME: where are the testcases?!?! -
 *           price/yield calculations are cross-checked for consistency. -
 *           price/yield calculations are checked against known good values.
 *
 * @author Srinivas Hasti
 * @author Daniel Kong
 * @author Ueli Hofstetter
 *
 */
// TODO: code review :: license, class comments, comments for access modifiers, comments for @Override
public class Bond extends Instrument {

    protected/* @Natural */int settlementDays_;
    protected Calendar calendar_;
    protected double faceAmount;
    protected DayCounter paymentDayCounter;
    protected BusinessDayConvention paymentConvention;
    protected Handle<YieldTermStructure> discountCurve;
    protected Frequency frequency;

    //FIXME: name conflict with inner class...
    protected Leg cashflows_;
    protected Date maturityDate_;
    protected Date issueDate_;
    protected Date datedDate;
    protected List<Double> notionals_;
    protected List<Date> notionalSchedule_;
    protected Leg redemptions_;
    protected double settlementValue_;

    /**
     * Constructor for amortizing or non-amortizing bonds. Redemptions and
     * maturity are calculated from the coupon data, if available. Therefore,
     * redemptions must not be included in the passed cash flows.
     *
     * @param settlementDays
     * @param calendar
     * @param issueDate
     * @param coupons
     */
    protected Bond(
            final /* @Natural */int settlementDays,
            final Calendar calendar,
            final Date issueDate,
            final Leg coupons) {

        this.settlementDays_ = (settlementDays);
        this.calendar_ = (calendar);
        this.cashflows_ = (coupons);
        this.issueDate_ = (issueDate);
        this.notionals_ = new ArrayList<Double>();
        this.notionalSchedule_ = new ArrayList<Date>();
        this.redemptions_ = new Leg();

        if (!coupons.isEmpty()) {
            Collections.sort(cashflows_, new EarlierThanCashFlowComparator());
            maturityDate_ = coupons.get(coupons.size() - 1).date();
            addRedemptionsToCashflows();
        }

        final Date evaluationDate = new Settings().evaluationDate();
        evaluationDate.addObserver(this);
    }

    protected Bond(
            final /* @Natural */int settlementDays,
            final Calendar calendar) {
        this(settlementDays, calendar, new Date(), new Leg());
    }

    protected Bond(
            final /* @Natural */int settlementDays,
            final Calendar calendar,
            final Date issueDate) {
        this(settlementDays, calendar, issueDate, new Leg());
    }

    protected Bond(
            final /* @Natural */int settlementDays,
            final Calendar calendar,
            final Leg coupons) {
        this(settlementDays, calendar, new Date(), coupons);
    }

    /**
     * Old constructor for non amortizing bonds. /* Warning: The last passed
     * cash flow must be the bond redemption. No other cash flow can have a date
     * later than the redemption date.
     *
     * @param settlementDays
     * @param calendar
     * @param faceAmount
     * @param maturityDate
     * @param issueDate
     * @param cashflows
     */
    protected Bond(
            final /* @Natural */int settlementDays,
            final Calendar calendar,
            final /* @Real */double faceAmount,
            final Date maturityDate,
            final Date issueDate,
            final Leg cashflows) {

        this.settlementDays_ = (settlementDays);
        this.calendar_ = (calendar);
        this.cashflows_ = (cashflows);
        this.maturityDate_ = (maturityDate);
        this.issueDate_ = (issueDate);
        this.notionalSchedule_ = new ArrayList<Date>();
        this.notionals_ = new ArrayList<Double>();
        this.redemptions_ = new Leg();

        if (!cashflows.isEmpty()) {
            //notionals_ = new double[2];
            notionalSchedule_.add(0, new Date());

            //notionals_[0] = faceAmount;
            notionals_.add(0, faceAmount);

            notionalSchedule_.add(1,maturityDate);

            //notionals_[1] = 0.0;
            notionals_.add(1, 0.0);

            redemptions_.add(cashflows.last());

            //TODO: code review
            //Attention pleaze: -- Operator on iterator!!!!!!!
            //The last shoudldn't be sorted --> keep a reference
            final CashFlow last = cashflows.last();

            Collections.sort(cashflows, new EarlierThanCashFlowComparator());

            //now add this cashflow to the last position
            this.cashflows_.remove(last);
            this.cashflows_.add(last);

        }

        final Date evaluationDate = new Settings().evaluationDate();
        evaluationDate.addObserver(this);
    }

    protected Bond(
            final /* @Natural */int settlementDays,
            final Calendar calendar,
            final /* @Real */double faceAmount,
            final Date maturityDate) {
        this(settlementDays, calendar, faceAmount, maturityDate, new Date(), new Leg());
    }

    protected Bond(
            final /* @Natural */int settlementDays,
            final Calendar calendar,
            final /* @Real */double faceAmount,
            final Date maturityDate,
            final Date issueDate) {
        this(settlementDays, calendar, faceAmount, maturityDate, issueDate, new Leg());
    }

    protected Bond(
            final /* @Natural */int settlementDays,
            final Calendar calendar,
            final /* @Real */double faceAmount,
            final Date maturityDate,
            final Leg cashflows) {
        this(settlementDays, calendar, faceAmount, maturityDate, new Date(), cashflows);
    }

    @Deprecated
    protected Bond(
            final /* @Natural */int settlementDays,
            final double faceAmount,
            final Calendar calendar,
            final DayCounter paymentDayCounter,
            final BusinessDayConvention paymentConvention) {
        // TODO: code review :: please verify against QL/C++ code
        this(settlementDays, faceAmount, calendar,
                paymentDayCounter, paymentConvention,
                new Handle<YieldTermStructure>(YieldTermStructure.class));
    }

    @Deprecated
    protected Bond(
            final /* @Natural */int settlementDays,
            final double faceAmount,
            final Calendar calendar,
            final DayCounter paymentDayCounter,
            final BusinessDayConvention paymentConvention,
            final Handle<YieldTermStructure> discountCurve) {
        this.settlementDays_ = settlementDays;
        this.faceAmount = faceAmount;
        this.calendar_ = calendar;
        this.paymentDayCounter = paymentDayCounter;
        this.paymentConvention = paymentConvention;
        this.discountCurve = discountCurve;
        this.frequency = Frequency.NoFrequency;

        this.notionals_ = new ArrayList<Double>();
        this.notionalSchedule_ = new ArrayList<Date>();
        this.redemptions_ = new Leg();

        final Date evaluationDate = new Settings().evaluationDate();

        evaluationDate.addObserver(this);
        this.discountCurve.currentLink().addObserver(this);
    }


    //
    // public methods
    //


    public int getSettlementDays() {
        return settlementDays_;
    }

    public Calendar getCalendar() {
        return calendar_;
    }

    public double getFaceAmount() {
        // return faceAmount;
        //return notionals_[0];
        return notionals_.get(0);
    }

    public /*double[]*/List<Double> notionals() {
        return notionals_;
    }

    /** Note: new Date() as default argument. */
    public /* @Real */double notional(){
         return notional(new Date());
    }

    public/* @Real */double notional(Date date){
        //FIXME: wrong comparision should be done == Date()
        if (date.isNull()){
            //FIXME: check that settlementDate() returns a new DateInsatence
            date = settlementDate();
        }
        if (date.gt(notionalSchedule_.get(notionalSchedule_.size()-1))) {
            // after maturity
            return 0.0;
        }

        // After the check above, d is between the schedule
        // boundaries.  We search starting from the second notional
        // date, since the first is null.  After the call to
        // lower_bound, *i is the earliest date which is greater or
        // equal than d.  Its index is greater or equal to 1.

        // FIXME:: code review !!!
        int index = Collections.binarySearch(notionalSchedule_, date);
        if (index < 0) {
            index = - (index + 1);
        }

        if (date.le(notionalSchedule_.get(index))) {
            // no doubt about what to return
            //return notionals_[index-1];
            return notionals_.get(index-1);
        } else {
            if (new Settings().isTodaysPayments()) {
                // We consider today's payment as pending; the bond still
                // has the previous notional
                return notionals_.get(index-1);
            } else {
                // today's payment has occurred; the bond already changed
                // notional.
            	return notionals_.get(index);
            }
        }
    }

    /**
     * Returns all the cashflows, including the redemptions.
     *
     * @return The leg of this bond.
     */
    public Leg cashflows() {
        return cashflows_;
    }

    /**
     * Returns just the redemption flows (not interest payments)
     *
     * @return
     */
    public Leg redemptions() {
        return redemptions_;
    }

    /**
     * Returns the redemption, if only one is defined
     *
     * @return The redemption cashflow.
     */
    public CashFlow redemption() {
        QL.require(redemptions_.size() == 1 , "multiple redemption cash flows given"); // QA:[RG]::verified // TODO: message
        return cashflows_.get(cashflows_.size() - 1);
    }

    public Date maturityDate() {
        if (maturityDate_.equals(new Date())) {
            return maturityDate_;
        } else {
            return cashflows().get(cashflows().size() - 1).date();
        }

    }

    public Date issueDate() {
        return issueDate_;
    }

    @Override
    public boolean isExpired() {
        return cashflows_.last().hasOccurred(settlementDate());
    }

    /**
     * Theoretical clean price The default bond settlement is used for
     * calculation. Warning: the theoretical price calculated from a flat term
     * structure might differ slightly from the price calculated from the
     * corresponding yield by means of the other overload of this function. If
     * the price from a constant yield is desired, it is advisable to use such
     * other overload.
     *
     * @return The clean price of this bond.
     */
    public double getCleanPrice() {
        return getDirtyPrice() - accruedAmount(settlementDate());
    }

    /**
     * Theoretical dirty price The default bond settlement is used for
     * calculation. Warning: the theoretical price calculated from a flat term
     * structure might differ slightly from the price calculated from the
     * corresponding yield by means of the other overload of this function. If
     * the price from a constant yield is desired, it is advisable to use such
     * other overload.
     *
     * @return The dirty price of this bond.
     */
    public double getDirtyPrice() {
        return settlementValue()/notional(settlementDate())*100.0;
    }

    public Date settlementDate() {
        return settlementDate(new Date());
    }

    //FIXME: intended to return a copy!!!!!!!!!!!!!!!!!!! review!
    public Date settlementDate(final Date date) {
        final Date d = (date.isNull()) ? new Settings().evaluationDate() : date;

        // usually, the settlement is at T+n...
        final Date settlement = calendar_.advance(d, settlementDays_, TimeUnit.Days);
        // ...but the bond won't be traded until the issue date (if given.)


        if (issueDate_.isNull()){
            return settlement;
        }
        else if (issueDate_.ge(settlement)){
            //settlement is already a copy. In case issueDate will be returned we have to copy it.
            //FIXME: this should be by a helper method/ copy constructor!!
            return issueDate_.clone();
        }
        else{
            return settlement;
        }
    }

    /**
     * Accrued amount at a given date /* The default bond settlement is used if
     * no date is given.
     *
     * @return The accrued amount. double
     */
    public double accruedAmount() {
        return accruedAmount(new Date());
    }

    public double accruedAmount(Date settlement) {
        if (settlement==new Date()){
            settlement = settlementDate();
        }
        final CashFlow cf = CashFlows.getInstance().nextCashFlow(cashflows_, settlement);

        if (cf==null) {return 0.0;}

        final Date paymentDate = cf.date();
        boolean firstCouponFound = false;
        /*@Real*/double nominal = Constants.NULL_REAL;
        /*@Time*/double accrualPeriod = Constants.NULL_REAL;
        DayCounter dc = null;

        /*@Rate*/double result = 0.0;

        // QL starts at the next cashflow and only continues to the one after to
        // check that it isn't the same date. Also stop at the penultimate flow. The last flow
        // is not a Coupon
        final int startIndex = cashflows_.indexOf(cf);
        for (final Iterator<CashFlow> iterator = cashflows_.listIterator(startIndex); iterator.hasNext();) {
			final CashFlow flow = iterator.next();
            if(!flow.date().eq(paymentDate) || ! iterator.hasNext()){break;}
            final Coupon cp = (Coupon)flow;
            if (cp != null) {
                if (firstCouponFound) {
                    assert(nominal       == cp.nominal() &&
                            accrualPeriod == cp.accrualPeriod() &&
                            //FIXME: implement equals for dayCounters!
                            dc.getClass()            == cp.dayCounter().getClass()):
                                "cannot aggregate accrued amount of two " +
                                "different coupons on "+ paymentDate.toString();
                } else {
                    firstCouponFound = true;
                    nominal = cp.nominal();
                    accrualPeriod = cp.accrualPeriod();
                    dc = cp.dayCounter();
                }
                result += cp.accruedAmount(settlement);
            }

        }
        return result/notional(settlement)*100.0;
        //old implementation....
        //		if (settlement.isNull()) {
        //			settlement = settlementDate();
        //		}
        //		for (int i = 0; i < cashFlows_.size(); ++i) {
        //			// the first coupon paying after d is the one we're after
        //			if (!cashFlows_.get(i).hasOccurred(settlement)) {
        //				Coupon coupon = (Coupon) cashFlows_.get(i);
        //				if (coupon != null)
        //					// !!!
        //					return coupon.accruedAmount(settlement) / faceAmount
        //							* 100.0;
        //				else
        //					return 0.0;
        //			}
        //		}
        //		return 0.0;
    }

    /**
     * Theoretical settlement value The default bond settlement date is used for
     * calculation.
     *
     * @return The theoretical settlement value.
     */
    public/* @Real */double settlementValue() {
        calculate();
        //FIXME: how to implement QL_Require
        //	        QL_REQUIRE(settlementValue_ != Null<Real>(),
        //	                   "settlement value not provided");
        return settlementValue_;
    }

    /**
     * Theoretical bond yield /* The default bond settlement and theoretical
     * price are used for calculation.
     *
     * @param dc
     * @param comp
     * @param freq
     */
    public/* @Rate */double yield(final DayCounter dc, final Compounding comp,
            final Frequency freq,
            /* Real */final double accuracy,
            /* Size */final int maxEvaluations) {
        final Brent solver = new Brent();
        solver.setMaxEvaluations(maxEvaluations);
        final YieldFinder objective = new YieldFinder(notional(settlementDate()), this.cashflows_,
                getDirtyPrice(),
                dc, comp, freq,
                settlementDate());
        return solver.solve(objective, accuracy, 0.02, 0.0, 1.0);

    }

    public/* @Rate */double yield(final DayCounter dc, final Compounding comp,
            final Frequency freq) {
        return yield(dc, comp, freq, 1.0e-8, 100);

    }

    /**
     * Clean price given a yield and settlement date The default bond settlement
     * is used if no date is given.
     *
     * @param yield
     * @param dc
     * @param comp
     * @param freq
     * @param settlementDate
     * @return
     */
    public/* @Real */double cleanPrice(/* @Rate */final double yield,
            final DayCounter dc, final Compounding comp, final Frequency freq,
            Date settlementDate) {
        //FIXME: wrong - should be compared with new Date()
        if (settlementDate.isNull()) {
            settlementDate = settlementDate();
        }
        return dirtyPrice(yield, dc, comp, freq, settlementDate)
        - accruedAmount(settlementDate);
    }

    public/* @Real */double cleanPrice(/* @Rate */final double yield,
            final DayCounter dc, final Compounding comp, final Frequency freq) {

        return cleanPrice(yield, dc, comp, freq, new Date());
    }

    /**
     * Dirty price given a yield and settlement date The default bond settlement
     * is used if no date is given.
     *
     * @param yield
     * @param dc
     * @param comp
     * @param freq
     * @param settlementDate
     * @return
     */
    public/* @Real */double dirtyPrice(/* @Rate */final double yield,
            final DayCounter dc, final Compounding comp, final Frequency freq,
            Date settlementDate) {
        //FIXME: should be compared with Date()
        if (settlementDate.isNull()){
            settlementDate = settlementDate();
        }
        return dirtyPriceFromYield(notional(settlementDate), this.cashflows_, yield,
                dc, comp, freq,
                settlementDate);
    }

    /**
     * see {@link #dirtyPrice()} Default date is today's date.
     *
     * @param yield
     * @param dc
     * @param comp
     * @param freq
     * @param settlementDate
     * @return
     */
    public/* @Real */double dirtyPrice(/* @Rate */final double yield,
            final DayCounter dc, final Compounding comp, final Frequency freq) {
        return dirtyPrice(yield, dc, comp, freq, new Date());
    }

    /**
     * Settlement value as a function of the clean price The default bond
     * settlement date is used for calculation.
     *
     * @param cleanPrice
     * @return
     */
    public/* @Real */double settlementValue(/* @Real */final double cleanPrice) {
        /*@Real*/ final double dirtyPrice = cleanPrice + accruedAmount(settlementDate());
        return dirtyPrice/100.0 * notional(settlementDate());
    }

    /**
     * Yield given a (clean) price and settlement date The default bond
     * settlement is used if no date is given.
     *
     * @param cleanPrice
     * @param dc
     * @param comp
     * @param freq
     * @param settlementDate
     * @param accuracy
     * @param maxEvaluations
     * @return
     */
    public/* @Real */double yield(/* @Real */final double cleanPrice,
            final DayCounter dc, final Compounding comp, final Frequency freq,
            Date settlementDate,
            /* @Real */final double accuracy,
            /* @Size */final int maxEvaluations) {
        if (settlementDate.isNull()) {
            settlementDate = settlementDate();
        }

        final Brent solver = new Brent();
        final double dirtyPrice = cleanPrice + accruedAmount(settlementDate);
        solver.setMaxEvaluations(maxEvaluations);
        final YieldFinder objective = new YieldFinder(notional(settlementDate), this.cashflows_,
                dirtyPrice,
                dc, comp, freq,
                settlementDate);
        return solver.solve(objective, accuracy, 0.02, 0.0, 1.0);
    }

    /**
     * @see #yield(double, DayCounter, Compounding, Frequency, Date, double,
     *      int) using settlementDate = today, accuracy = 1.0e-8 and
     *      maxEvaluation = 100.
     * @param cleanPrice
     * @param dc
     * @param comp
     * @param freq
     * @return
     */
    public/* @Real */double yield(/* @Real */final double cleanPrice,
            final DayCounter dc, final Compounding comp, final Frequency freq) {
        return yield(cleanPrice, dc, comp, freq, new Date(), 1.0e-8, 100);
    }

    /**
     * @see #yield(double, DayCounter, Compounding, Frequency, Date, double,
     *      int) using accuracy = 1.0e-8 and maxEvaluation = 100.
     * @param cleanPrice
     * @param dc
     * @param comp
     * @param freq
     * @param settlementDate
     * @return
     */
    public/* @Real */double yield(/* @Real */final double cleanPrice,
            final DayCounter dc, final Compounding comp, final Frequency freq,
            final Date settlementDate) {
        return yield(cleanPrice, dc, comp, freq, settlementDate, 1.0e-8, 100);
    }

    /**
     * @see #yield(double, DayCounter, Compounding, Frequency, Date, double,
     *      int) using maxEvaluation = 100.
     * @param cleanPrice
     * @param dc
     * @param comp
     * @param freq
     * @param settlementDate
     * @param accuracy
     * @return
     */
    public/* @Real */double yield(/* @Real */final double cleanPrice,
            final DayCounter dc, final Compounding comp, final Frequency freq,
            final Date settlementDate,
            /* @Rea */final double accuracy) {
        return yield(cleanPrice, dc, comp, freq, settlementDate, accuracy, 100);
    }

    /**
     * Clean price given Z-spread /* Z-spread compounding, frequency, daycount
     * are taken into account The default bond settlement is used if no date is
     * given. For details on Z-spread refer to: "Credit Spreads Explained",
     * Lehman Brothers European Fixed Income Research - March 2004, D. O'Kane
     *
     * @param zSpread
     * @param dc
     * @param comp
     * @param freq
     * @param settlementDate
     * @return
     */
    public/* @Real */double cleanPriceFromZSpread( /* @Spread */final double zSpread,
            final DayCounter dc, final Compounding comp, final Frequency freq,
            final Date settlementDate) {
        /*@Real*/final double p = dirtyPriceFromZSpread(zSpread, dc, comp, freq, settlementDate);
        return p - accruedAmount(settlementDate);
    }

    /**
     * @see Bond#cleanPriceFromZSpread(double, DayCounter, Compounding,
     *      Frequency, Date) using today's date as settlementday.
     * @param zSpread
     * @param dc
     * @param comp
     * @param freq
     * @return
     */
    public/* @Real */double cleanPriceFromZSpread( /* @Spread */final double zSpread,
            final DayCounter dc, final Compounding comp, final Frequency freq) {
        return cleanPriceFromZSpread(zSpread, dc, comp, freq, new Date());
    }

    /**
     * Dirty price given Z-spread Z-spread compounding, frequency, daycount are
     * taken into account The default bond settlement is used if no date is
     * given. For details on Z-spread refer to: "Credit Spreads Explained",
     * Lehman Brothers European Fixed Income Research - March 2004, D. O'Kane
     *
     * @param zSpread
     * @param dc
     * @param comp
     * @param freq
     * @param settlementDate
     * @return
     */
    public/* @Real */double dirtyPriceFromZSpread(
            /* @Spread */final double zSpread,
            final DayCounter dc,
            final Compounding comp,
            final Frequency freq,
            Date settlement) {
        if (settlement == new Date()){
            settlement = settlementDate();
        }

        if (engine==null) {
            throw new LibraryException("null pricing engine"); //// TODO: message

        }
        //FIXME: DiscontingBondEngine
        QL.require(DiscountingBondEngine.class.isAssignableFrom(engine.getClass()), ReflectConstants.WRONG_ARGUMENT_TYPE); // QA:[RG]::verified
        final DiscountingBondEngine discountingBondEngine = (DiscountingBondEngine)engine;

        return dirtyPriceFromZSpreadFunction(notional(settlement), cashflows_,
                zSpread, dc, comp, freq,
                settlement,
                discountingBondEngine.discountCurve());
    }

    /**
     * @see Bond#dirtyPriceFromZSpread(double, DayCounter, Compounding,
     *      Frequency, Date) using today's date as settlement date.
     * @param zSpread
     * @param dc
     * @param comp
     * @param freq
     * @return
     */
    public/* @Real */double dirtyPriceFromZSpread(/* @Spread */final double zSpread,
            final DayCounter dc, final Compounding comp, final Frequency freq) {
        return dirtyPriceFromZSpread(zSpread, dc, comp, freq, new Date());
    }

    /**
     * Expected next coupon: depending on (the bond and) the given date the
     * coupon can be historic, deterministic or expected in a stochastic sense.
     * When the bond settlement date is used the coupon is the already-fixed
     * not-yet-paid one. The current bond settlement is used if no date is
     * given.
     *
     * @return
     */
    public  /* @Rate */double nextCoupon(){
        return nextCoupon(new Date());
    }

    public  /* @Rate */double nextCoupon(Date settlement){
        if (settlement.isNull()){
            settlement = settlementDate();
        }
        return CashFlows.getInstance().nextCouponRate(cashflows_, settlement);
    }

    /**
     * Previous coupon already paid at a given date /* Expected previous coupon:
     * depending on (the bond and) the given date the coupon can be historic,
     * deterministic or expected in a stochastic sense. When the bond settlement
     * date is used the coupon is the last paid one. The current bond settlement
     * is used if no date is given.
     *
     * @return
     */
    public /* @Rate */double previousCoupon(){
        return previousCoupon(new Date());
    }

    public /* @Rate */double previousCoupon(Date settlement){
        if (settlement.isNull()){
            settlement = settlementDate();
        }
        return CashFlows.getInstance().previousCouponRate(cashflows_, settlement);
    }

    @Override
    protected void setupExpired() {
        super.setupExpired();
        this.settlementValue_ = 0.0;
    }

    @Override
    protected void setupArguments(final PricingEngine.Arguments arguments) {
        QL.require(Bond.Arguments.class.isAssignableFrom(arguments.getClass()), ReflectConstants.WRONG_ARGUMENT_TYPE); // QA:[RG]::verified
        final Bond.ArgumentsImpl bondArguments = (Bond.ArgumentsImpl)arguments;

        // TODO: code review :: please verify against QL/C++ code
        //FIXME: check ... this actually requires cloning
        //        arguments->settlementDate = settlementDate();
        //        arguments->cashflows = cashflows_;
        //        arguments->calendar = calendar_;
        bondArguments.settlementDate = settlementDate();
        bondArguments.cashflows = (Leg)cashflows_.clone();
        //FIXME: would be a copy.. since calendars are kind of immutable anyway just return the a reference
        //bondArguments.calendar = calendar_.clone();
        bondArguments.calendar = calendar_;
    }

    @Override
    protected void fetchResults(final PricingEngine.Results results) {
        QL.require(Bond.Results.class.isAssignableFrom(results.getClass()), ReflectConstants.WRONG_ARGUMENT_TYPE); // QA:[RG]::verified
        super.fetchResults(results);
        final Bond.ResultsImpl tempResults = (Bond.ResultsImpl) results;
        //already checked above
        //QL.require(results != 0, "wrong result type";
        settlementValue_ = tempResults.settlementValue;
    }

    /**
     * This method can be called by derived classes in order to build redemption
     * payments from the existing cash flows. It must be called after setting up
     * the cashflows_ vector and will fill the notionalSchedule_, notionals_,
     * and redemptions_ data members. If given, the elements of the redemptions
     * vector will multiply the amount of the redemption cash flow. The elements
     * will be taken in base 100, i.e., a redemption equal to 100 does not
     * modify the amount. The cashflows_ vector must contain at least one coupon
     * and must be sorted by date.
     *
     * @param redemptions
     */
    protected void addRedemptionsToCashflows(final double[] redemptions) {
        // First, we gather the notional information from the cashflows
        calculateNotionalsFromCashflows();
        // Then, we create the redemptions based on the notional
        // information and we add them to the cashflows vector after
        // the coupons.
        redemptions_.clear();
        for (int i=1; i<notionalSchedule_.size(); ++i) {
            /*@Real*/final double R = (i < redemptions.length) ? redemptions[i] :
                !(redemptions.length == 0)   ? redemptions[redemptions.length-1] : 100.0;
                /*@Real*/final double amount = (R/100.0)*(notionals_.get(i-1)-notionals_.get(i));
                final CashFlow  redemption = new SimpleCashFlow(amount, notionalSchedule_.get(i));
                cashflows_.add(redemption);
                redemptions_.add(redemption);
        }
        // stable_sort now moves the redemptions to the right places
        // while ensuring that they follow coupons with the same date.
        //FIXME: Note: this should be a stable sort. according to the the java documentation Collections.sort algorithms have to be stable!
        //It has to be checked whether this is similar to std::stable_sort
        Collections.sort(cashflows_, new EarlierThanCashFlowComparator());

    }

    protected void addRedemptionsToCashflows() {
        addRedemptionsToCashflows(new double[]{});
    }

    protected void calculateNotionalsFromCashflows() {
        notionalSchedule_.clear();
        notionals_.clear();
        Date lastPaymentDate = new Date();
        notionalSchedule_.add(new Date());
        for (int i=0; i<cashflows_.size(); ++i) {
            final Coupon coupon = (Coupon)cashflows_.get(i);
            if (coupon==null){
                continue;
            }
            /*@Real*/final double notional = coupon.nominal();
            // we add the notional only if it is the first one...
            if (notionals_.isEmpty()) {
                notionals_.add(coupon.nominal());
                lastPaymentDate = coupon.date();
            } else if (!Closeness.isClose(notional, notionals_.get(notionals_.size() -1 ))) {
                // ...or if it has changed.
                assert(notional < notionals_.get(notionals_.size()-1)):("increasing coupon notionals");
                notionals_.add(coupon.nominal());
                // in this case, we also add the last valid date for
                // the previous one...
                notionalSchedule_.add(lastPaymentDate);
                // ...and store the candidate for this one.
                lastPaymentDate = coupon.date();
            } else {
                // otherwise, we just extend the valid range of dates
                // for the current notional.
                lastPaymentDate = coupon.date();
            }
        }
        assert!notionals_.isEmpty(): "no coupons provided";
        notionals_.add(0.0);
        notionalSchedule_.add(lastPaymentDate);
    }

    /**
     * This method can be called by derived classes in order to build a bond
     * with a single redemption payment. It will fill the notionalSchedule_,
     * notionals_, and redemptions_ data members.
     *
     * @param notional
     * @param redemption
     * @param date
     */
    void setSingleRedemption(/* @Real */final double notional,
            /* @Real */final double redemption, final Date date) {
        notionals_.clear();
        notionalSchedule_.clear();
        redemptions_.clear();

        notionalSchedule_.add(new Date());
        notionals_.add( notional);

        notionalSchedule_.add(date);
        notionals_.add(0.0);

        final CashFlow redemptionCashflow =  new SimpleCashFlow(notional*redemption/100.0, date);
        cashflows_.add(redemptionCashflow);
        redemptions_.add(redemptionCashflow);
    }

    public static/* @Real */double dirtyPriceFromYield(
            /* @Real */final double faceAmount, final Leg cashflows,
            /* @Rate */final double yield, final DayCounter dayCounter,
            final Compounding compounding, Frequency frequency, final Date settlement) {
        if (frequency == Frequency.NoFrequency || frequency == Frequency.Once) {
            frequency = Frequency.Annual;
        }

        final InterestRate y = new InterestRate(yield, dayCounter, compounding,
                frequency);

        /* @Real */double price = 0.0;
        /* @DiscountFactor */double discount = 1.0;
        Date lastDate = new Date();

        for (int i = 0; i < cashflows.size(); ++i) {
            if (cashflows.get(i).hasOccurred(settlement)) {
                continue;
            }

            final Date couponDate = cashflows.get(i).date();
            /* @Real */final double amount = cashflows.get(i).amount();

            if (lastDate.isNull()) {
                // first not-expired coupon
                if (i > 0) {
                    lastDate = cashflows.get(i - 1).date();
                } else {
                    // Coupon coupon = (Coupon )cashflows.get(i);
                    final CashFlow coupon = cashflows.get(i);
                    if (coupon instanceof Coupon && coupon != null) {
                        // if (coupon)
                        lastDate = ((Coupon) coupon).accrualStartDate();
                    } else {
                        lastDate = couponDate.sub(new Period(1, TimeUnit.Years));
                    }
                }
                discount *= y.discountFactor(settlement, couponDate, lastDate,
                        couponDate);
            } else {
                discount *= y.discountFactor(lastDate, couponDate);
            }
            lastDate = couponDate;

            price += amount * discount;
        }

        return price / faceAmount * 100.0;
    }

    public static class YieldFinder implements DoubleOp {
        private final/* @Real */double faceAmount_;
        private final Leg cashflows_;
        private final/* @Real */double dirtyPrice_;
        private final Compounding compounding_;
        private final DayCounter dayCounter_;
        private final Frequency frequency_;
        private final Date settlement_;

        public YieldFinder(
                /* @Real */final double faceAmount, final Leg cashflows,
                /* @Real */final double dirtyPrice, final DayCounter dayCounter,
                final Compounding compounding, final Frequency frequency,
                final Date settlement) {
            this.faceAmount_ = faceAmount;
            this.cashflows_ = cashflows;
            this.dirtyPrice_ = dirtyPrice;
            this.compounding_ = compounding;
            this.dayCounter_ = dayCounter;
            this.frequency_ = frequency;
            this.settlement_ = settlement;
        }

        @Deprecated
        /* @Real */double operator(/* @Real */final double yield) {
            return dirtyPrice_
            - dirtyPriceFromYield(faceAmount_, cashflows_, yield,
                    dayCounter_, compounding_, frequency_, settlement_);
        }

        @Override
        public double op(final double yield) {
            return dirtyPrice_
            - dirtyPriceFromYield(faceAmount_, cashflows_, yield,
                    dayCounter_, compounding_, frequency_, settlement_);
        }

    }

    static/* @Real */double dirtyPriceFromZSpreadFunction(
            /* @Real */final double faceAmount, final Leg cashflows,
            /* @Spread */final double zSpread, final DayCounter dc, final Compounding comp,
            final Frequency freq, final Date settlement,
            final Handle<YieldTermStructure> discountCurve) {

        assert(freq!=Frequency.NoFrequency && freq != Frequency.Once):"invalid frequency:" + freq.toString();

        final Handle<Quote> zSpreadQuoteHandle = new Handle<Quote>(new SimpleQuote(
                zSpread));

        final ZeroSpreadedTermStructure spreadedCurve = new ZeroSpreadedTermStructure(
                discountCurve, zSpreadQuoteHandle, comp, freq, dc);
        /* @Real */double price = 0.0;
        for (int i = 0; i < cashflows.size(); ++i) {
            if (cashflows.get(i).hasOccurred(settlement)) {
                continue;
            }
            final Date couponDate = cashflows.get(i).date();
            /* @Real */final double amount = cashflows.get(i).amount();
            price += amount * spreadedCurve.discount(couponDate);
        }
        price /= spreadedCurve.discount(settlement);
        return price / faceAmount * 100.0;
    }



    //
    // inner interfaces
    //

    /**
     * basic bond arguments
     *
     * @author Richard Gomes
     */
    public interface Arguments extends Instrument.Arguments { }


    /**
     * basic bond results
     *
     * @author Richard Gomes
     */
    public interface Results extends Instrument.Results { }


    /**
     * basic bond price engine
     *
     * @author Richard Gomes
     */
    public interface Engine extends PricingEngine, Observer { }


    //
    // inner classes
    //

    static public class ArgumentsImpl implements Bond.Arguments {

        public Date settlementDate;
        public Leg cashflows;
        public Calendar calendar;

        @Override
        public void validate() {
            QL.require(!settlementDate.isNull(), "no settlement date provided"); // QA:[RG]::verified // TODO: message
            assert(!cashflows.isEmpty()): ("no cash flow provided");
            for (int i=0; i<cashflows.size(); ++i) {
                assert(cashflows.get(i)!=null): ("null cash flow provided");
            }

        }

    }

    static public class ResultsImpl extends Instrument.ResultsImpl implements Bond.Results {

        public /*@Real*/double settlementValue;

        @Override
        public void reset() {
            settlementValue = Constants.NULL_REAL;
        }

    }


    /**
     * The pricing engine for bonds
     *
     * @author Richard Gomes
     */
    static public abstract class EngineImpl extends GenericEngine<Bond.Arguments, Bond.Results> implements Bond.Engine {

        protected EngineImpl() {
            super(new ArgumentsImpl(), new ResultsImpl());
        }

    }

}
