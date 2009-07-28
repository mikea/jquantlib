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

package org.jquantlib.cashflow;

import org.jquantlib.Configuration;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.math.Ops;
import org.jquantlib.math.solvers1D.Brent;
import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.Compounding;
import org.jquantlib.termstructures.InterestRate;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.termstructures.yieldcurves.FlatForward;
import org.jquantlib.time.Frequency;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.jquantlib.util.TypedVisitor;
import org.jquantlib.util.Visitor;
import org.jquantlib.util.stdlibc.Std;

/**
 * Cashflow-analysis functions
 *
 * @author Ueli Hofstetter
 */
// TODO: code review :: license, class comments, comments for access modifiers, comments for @Override
public class CashFlows {

    private final String not_enough_information_available = "not enough information available";
    private final String no_cashflows = "no cashflows";
    private final String unsupported_compounding_type = "unsupported compounding type";
    private final String compounded_rate_required = "compounded rate required";
    private final String unsupported_frequency = "unsupported frequency";
    private final String unknown_duration_type = "unsupported duration type";
    private final String infeasible_cashflow = "the given cash flows cannot result in the given market price due to their sign";

    private static double basisPoint_ = 1.0e-4;


    /**
     * Singleton instance for the whole application.
     * <p>
     * In an application server environment, it could be by class loader depending on scope of the jquantlib library to the module.
     *
     * @see <a href="http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html">The "Double-Checked Locking is Broken"
     *      Declaration </a>
     */
    private static volatile CashFlows instance = null;

    //
    // private constructors
    //

    private CashFlows() {
        // cannot be directly instantiated
    }


    //
    // public static methods
    //

    public static CashFlows getInstance() {
        if (instance == null)
            synchronized (CashFlows.class) {
                if (instance == null)
                    instance = new CashFlows();
            }
        return instance;
    }


    //
    // public methods
    //

    public Date startDate(final Leg cashflows) {
        Date d = DateFactory.getFactory().getMaxDate();
        for (int i = 0; i < cashflows.size(); ++i) {
            final Coupon c = (Coupon) cashflows.get(i);
            if (c != null)
                d = Std.getInstance().min(c.accrualStartDate(), d);
        }
        // TODO: code review :: please verify against original QL/C++ code
        assert d.le(DateFactory.getFactory().getMaxDate()) : not_enough_information_available;
        return d;
    }

    public Date maturityDate(final Leg cashflows) {
        Date d = DateFactory.getFactory().getMinDate();
        for (int i = 0; i < cashflows.size(); i++)
            d = Std.getInstance().max(d, cashflows.get(i).date());
        // TODO: code review :: please verify against original QL/C++ code
        assert d.le(DateFactory.getFactory().getMinDate()) : no_cashflows;
        return d;
    }

    /**
     * NPV of the cash flows.
     * <p>
     * The NPV is the sum of the cash flows, each discounted according to the given term structure.
     *
     * @param cashflows
     * @param discountCurve
     * @param settlementDate
     * @param npvDate
     * @param exDividendDays
     * @return
     */
    public double npv(
            final Leg cashflows,
            final Handle<YieldTermStructure> discountCurve,
            final Date settlementDate,
            final Date npvDate,
            final int exDividendDays) {

        Date date = settlementDate;
        if (date.eq(DateFactory.getFactory().getTodaysDate()))
            date = discountCurve.getLink().referenceDate();

        double totalNPV = 0.0;
        for (int i = 0; i < cashflows.size(); ++i)
            if (!cashflows.get(i).hasOccurred(date.increment(exDividendDays)))
                totalNPV += cashflows.get(i).amount() * discountCurve.getLink().discount(cashflows.get(i).date());

        if (npvDate.eq(DateFactory.getFactory().getTodaysDate()))
            return totalNPV;
        else
            return totalNPV/discountCurve.getLink().discount(npvDate);
    }

    public double npv(final Leg leg, final Handle<YieldTermStructure> discountCurve) {
        return npv(leg, discountCurve, DateFactory.getFactory().getTodaysDate(), DateFactory.getFactory().getTodaysDate(), 0);
    }

    /**
     * NPV of the cash flows.
     * <p>
     * The NPV is the sum of the cash flows, each discounted according to the given constant interest rate. The result is affected
     * by the choice of the interest-rate compounding and the relative frequency and day counter.
     */
    public double npv(
            final Leg cashflows,
            final InterestRate irr,
            final Date settlementDate) {

        Date date = settlementDate;
        if (date.eq(DateFactory.getFactory().getTodaysDate())) {
            date = Configuration.getSystemConfiguration(null).getGlobalSettings().getEvaluationDate();;
        }

        final YieldTermStructure flatRate = new FlatForward(date, irr.rate(), irr.dayCounter(), irr.compounding(), irr.frequency());
        return npv(cashflows, new Handle<YieldTermStructure>(flatRate), date, date, 0);
    }

    public double npv(final Leg leg, final InterestRate interestRate) {
        return npv(leg, interestRate, DateFactory.getFactory().getTodaysDate());
    }

    /**
     * Basis-point sensitivity of the cash flows.
     * <p>
     * The result is the change in NPV due to a uniform 1-basis-point change in the rate paid by the cash flows. The change for
     * each coupon is discounted according to the given term structure.
     */
    public double bps(
            final Leg cashflows,
            final Handle<YieldTermStructure> discountCurve,
            final Date settlementDate,
            final Date npvDate) {
        return bps(cashflows, discountCurve, settlementDate, npvDate, 0);
    }


    /**
     * Basis-point sensitivity of the cash flows.
     * <p>
     * The result is the change in NPV due to a uniform 1-basis-point change in the rate paid by the cash flows. The change for
     * each coupon is discounted according to the given term structure.
     */
    public double bps(
            final Leg cashflows,
            final Handle<YieldTermStructure> discountCurve,
            final Date settlementDate,
            final Date npvDate,
            final int exDividendDays) {

        Date date = settlementDate;
        if (date.eq(DateFactory.getFactory().getTodaysDate()))
            date = discountCurve.getLink().referenceDate();

        final BPSCalculator calc = new BPSCalculator(discountCurve, npvDate);
        for (int i = 0; i < cashflows.size(); ++i)
            if (!cashflows.get(i).hasOccurred(date.increment(exDividendDays)))
                cashflows.get(i).accept(calc);
        return basisPoint_ * calc.result();
    }

    public double bps(final Leg leg, final Handle<YieldTermStructure> discountCurve) {
        return bps(leg, discountCurve, DateFactory.getFactory().getTodaysDate(), DateFactory.getFactory().getTodaysDate(), 0);
    }

    /**
     * Basis-point sensitivity of the cash flows.
     * <p>
     * The result is the change in NPV due to a uniform 1-basis-point change in the rate paid by the cash flows. The change for
     * each coupon is discounted according to the given constant interest rate. The result is affected by the choice of the
     * interest-rate compounding and the relative frequency and day counter.
     */
    public double bps(final Leg cashflows, final InterestRate irr, final Date settlementDate) {
        Date date = settlementDate;
        if (date.eq(DateFactory.getFactory().getTodaysDate())) {
            date = Configuration.getSystemConfiguration(null).getGlobalSettings().getEvaluationDate();;
        }

        final YieldTermStructure flatRate = new FlatForward(date, irr.rate(), irr.dayCounter(), irr.compounding(), irr.frequency());
        return bps(cashflows, new Handle<YieldTermStructure>(flatRate), date, date);
    }

    public double bps(final Leg leg, final InterestRate interestRate) {
        return bps(leg, interestRate, DateFactory.getFactory().getTodaysDate());
    }

    /**
     * At-the-money rate of the cash flows.
     * <p>
     * The result is the fixed rate for which a fixed rate cash flow vector, equivalent to the input vector, has the required NPV
     * according to the given term structure. If the required NPV is not given, the input cash flow vector's NPV is used instead.
     */
    public double atmRate(
            final Leg leg,
            final Handle<YieldTermStructure> discountCurve,
            final Date settlementDate,
            final Date npvDate,
            final int exDividendDays,
            double npv) {
        final double bps = bps(leg, discountCurve, settlementDate, npvDate, exDividendDays);
        if (npv == 0)
            npv = npv(leg, discountCurve, settlementDate, npvDate, exDividendDays);
        return basisPoint_ * npv / bps;
    }

    public double atmRate(final Leg leg, final Handle<YieldTermStructure> discountCurve) {
        return atmRate(leg, discountCurve, DateFactory.getFactory().getTodaysDate(), DateFactory.getFactory().getTodaysDate(), 0, 0);
    }

    /**
     * Internal rate of return.
     * <p>
     * The IRR is the interest rate at which the NPV of the cash flows equals the given market price. The function verifies the
     * theoretical existance of an IRR and numerically establishes the IRR to the desired precision.
     */
    public double irr(
            final Leg cashflows,
            final double marketPrice,
            final DayCounter dayCounter,
            final Compounding compounding,
            final Frequency frequency,
            final Date settlementDate,
            final double tolerance,
            final int maxIterations,
            final double guess) {

        Date date = settlementDate;
        if (date.eq(DateFactory.getFactory().getTodaysDate())) {
            date = Configuration.getSystemConfiguration(null).getGlobalSettings().getEvaluationDate();;
        }

        // depending on the sign of the market price, check that cash
        // flows of the opposite sign have been specified (otherwise
        // IRR is nonsensical.)

        int lastSign = sign(-marketPrice), signChanges = 0;
        for (int i = 0; i < cashflows.size(); ++i)
            if (!cashflows.get(i).hasOccurred(date)) {
                final int thisSign = sign(cashflows.get(i).amount());
                if (lastSign * thisSign < 0)
                    signChanges++;
                if (thisSign != 0)
                    lastSign = thisSign;
            }

        assert signChanges > 0 : infeasible_cashflow;

        /*
         * THIS COMMENT COMES UNMODIFIED FROM QL/C++ SOURCES
         *
         * The following is commented out due to the lack of a QL_WARN macro
         *
         * if (signChanges > 1) { // Danger of non-unique solution
         *     // Check the aggregate cash flows (Norstrom)
         *     Real aggregateCashFlow = marketPrice; signChanges = 0;
         *     for (Size i = 0; i < cashflows.size(); ++i) {
         *         Real nextAggregateCashFlow = aggregateCashFlow + cashflows[i]->amount();
         *         if (aggregateCashFlow * nextAggregateCashFlow < 0.0) signChanges++;
         *         aggregateCashFlow = nextAggregateCashFlow;
         *     }
         *     if (signChanges > 1) QL_WARN( "danger of non-unique solution");
         * }
         */

        final Brent solver = new Brent();
        solver.setMaxEvaluations(maxIterations);
        return solver.solve(
                new IRRFinder(cashflows, marketPrice, dayCounter, compounding, frequency, date),
                tolerance,
                guess,
                guess/10.0);
    }

    public double irr(final Leg leg, final double marketPrice, final DayCounter dayCounter, final Compounding compounding) {
        return irr(
                leg,
                marketPrice,
                dayCounter,
                compounding,
                Frequency.NO_FREQUENCY,
                DateFactory.getFactory().getTodaysDate(),
                1.0e-10,
                10000,
                0.05);
    }

    /**
     * Cash-flow duration.
     * <p>
     * The simple duration of a string of cash flows is defined as
     * {@latex[ D_{\mathrm{simple}} = \frac{\sum t_i c_i B(t_i)}{\sum c_i B(t_i)} }
     * where {@latex$ c_i } is the amount of the {@latex$ i }-th cash flow, {@latex$ t_i } is its payment time,
     * and {@latex$ B(t_i) } is the corresponding discount according to the passed yield.
     * <p>
     * The modified duration is defined as
     * {@latex[ D_{\mathrm{modified}} = -\frac{1}{P} \frac{\partial P}{\partial y} }
     * where {@latex$ P }is the present value of the cash flows according to the given IRR {@latex$ y }.
     * <p>
     * The Macaulay duration is defined for a compounded IRR as
     * {@latex[ D_{\mathrm{Macaulay}} = \left( 1 + \frac{y}{N} \right) D_{\mathrm{modified}} }
     * where {@latex$ y } is the IRR and {@latex$ N } is the number of cash flows per year.
     */
    public double duration(final Leg leg, final InterestRate y, final Duration duration, final Date settlementDate) {

        Date date = settlementDate;
        if (date.eq(DateFactory.getFactory().getTodaysDate())) {
            date = Configuration.getSystemConfiguration(null).getGlobalSettings().getEvaluationDate();;
        }

        switch (duration) {
        case Simple:
            return simpleDuration(leg, y, date);
        case Modified:
            return modifiedDuration(leg, y, date);
        case Macaulay:
            return macaulayDuration(leg, y, date);
        default:
            throw new AssertionError(unknown_duration_type);
        }
    }

    public double duration(final Leg leg, final InterestRate y) {
        return duration(leg, y, Duration.Modified, DateFactory.getFactory().getTodaysDate());
    }

    /**
     * Cash-flow convexity
     * <p>
     * The convexity of a string of cash flows is defined as
     * {@latex[ C = \frac{1}{P} \frac{\partial^2 P}{\partial y^2} }
     * where {@latex$ P } is the present value of the cash flows according to the given IRR {@latex$ y }.
     */
    public double convexity(final Leg cashFlows, final InterestRate rate, final Date settlementDate) {

        Date date = settlementDate;
        if (date.eq(DateFactory.getFactory().getTodaysDate())) {
            date = Configuration.getSystemConfiguration(null).getGlobalSettings().getEvaluationDate();;
        }

        final DayCounter dayCounter = rate.dayCounter();

        double P = 0.0;
        double d2Pdy2 = 0.0;
        final double y = rate.rate();
        final int N = rate.frequency().toInteger();

        for (int i = 0; i < cashFlows.size(); ++i)
            if (!cashFlows.get(i).hasOccurred(date)) {
                final double t = dayCounter.yearFraction(date, cashFlows.get(i).date());
                final double c = cashFlows.get(i).amount();
                final double B = rate.discountFactor(t);

                P += c * B;
                switch (rate.compounding()) {
                case SIMPLE:
                    d2Pdy2 += c * 2.0 * B * B * B * t * t;
                    break;
                case COMPOUNDED:
                    d2Pdy2 += c * B * t * (N * t + 1) / (N * (1 + y / N) * (1 + y / N));
                    break;
                case CONTINUOUS:
                    d2Pdy2 += c * B * t * t;
                    break;
                case SIMPLE_THEN_COMPOUNDED:
                default:
                    throw new IllegalArgumentException(unsupported_compounding_type);
                }
            }

        if (P == 0.0)
            // no cashflows
            return 0.0;
        return d2Pdy2 / P;
    }

    public double convexity(final Leg leg, final InterestRate y) {
        return convexity(leg, y, DateFactory.getFactory().getTodaysDate());
    }

    private double simpleDuration(final Leg cashflows, final InterestRate rate, final Date settlementDate) {

        double P = 0.0;
        double tP = 0.0;

        for (int i = 0; i < cashflows.size(); ++i)
            if (!cashflows.get(i).hasOccurred(settlementDate)) {
                final double t = rate.dayCounter().yearFraction(settlementDate, cashflows.get(i).date());
                final double c = cashflows.get(i).amount();
                final double B = rate.discountFactor(t);

                P += c * B;
                tP += t * c * B;
            }

        if (P == 0.0)
            // no cashflows
            return 0.0;

        return tP / P;
    }

    private double modifiedDuration(final Leg cashflows, final InterestRate rate, final Date settlementDate) {

        double P = 0.0;
        double dPdy = 0.0;
        final double y = rate.rate();
        final int N = rate.frequency().toInteger();

        for (int i = 0; i < cashflows.size(); ++i)
            if (!cashflows.get(i).hasOccurred(settlementDate)) {
                final double t = rate.dayCounter().yearFraction(settlementDate, cashflows.get(i).date());
                final double c = cashflows.get(i).amount();
                final double B = rate.discountFactor(t);

                P += c * B;
                switch (rate.compounding()) {
                case SIMPLE:
                    dPdy -= c * B * B * t;
                    break;
                case COMPOUNDED:
                    dPdy -= c * B * t / (1 + y / N);
                    break;
                case CONTINUOUS:
                    dPdy -= c * B * t;
                    break;
                case SIMPLE_THEN_COMPOUNDED:
                default:
                    throw new IllegalArgumentException(unsupported_compounding_type);
                }
            }

        if (P == 0.0)
            // no cashflows
            return 0.0;
        return -dPdy / P;
    }

    private double macaulayDuration(final Leg cashflows, final InterestRate rate, final Date settlementDate) {

        final double y = rate.rate();
        final int N = rate.frequency().toInteger();
        if (!rate.compounding().equals(Compounding.COMPOUNDED))
            throw new IllegalArgumentException(compounded_rate_required);
        if (N < 1)
            throw new IllegalArgumentException(unsupported_frequency);

        return (1 + y / N) * modifiedDuration(cashflows, rate, settlementDate);
    }

    private int sign(final double x) {
        if (x == 0)
            return 0;
        else if (x > 0)
            return 1;
        else
            return -1;
    }

    //
    // public Enums
    //

    /**
     * Duration type
     */
    public enum Duration {
        Simple, Macaulay, Modified
    }


    //
    // private inner classes
    //

    private class IRRFinder implements Ops.DoubleOp {

        private final Leg cashflows_;
        private final double marketPrice_;
        private final DayCounter dayCounter_;
        private final Compounding compounding_;
        private final Frequency frequency_;
        private final Date settlementDate_;

        public IRRFinder(
                final Leg cashflows,
                final double marketPrice,
                final DayCounter dayCounter,
                final Compounding compounding,
                final Frequency frequency,
                final Date settlementDate) {
            this.cashflows_ = cashflows;
            this.marketPrice_ = marketPrice;
            this.dayCounter_ = dayCounter;
            this.compounding_ = compounding;
            this.frequency_ = frequency;
            this.settlementDate_ = settlementDate;
        }

        @Override
        public double op(final double guess) {
            final InterestRate rate = new InterestRate(guess, dayCounter_, compounding_, frequency_);
            final double NPV = npv(cashflows_, rate, settlementDate_);
            return marketPrice_ - NPV;
        }
    }

    private class BPSCalculator implements TypedVisitor<Object> {

        private static final String UNKNOWN_VISITABLE = "unknow visitable object";

        private final Handle<YieldTermStructure> termStructure;
        private final Date npvDate;

        private double result;

        public BPSCalculator(final Handle<YieldTermStructure> termStructure, final Date npvDate) {
            this.termStructure = termStructure;
            this.npvDate = npvDate;
            this.result = 0.0;
        }

        public double result() {
            if (npvDate.eq(DateFactory.getFactory().getTodaysDate()))
                return result;
            else
                return result/termStructure.getLink().discount(npvDate);
        }


        //
        // implements TypedVisitor
        //

        @Override
        public Visitor<Object> getVisitor(final Class<? extends Object> klass) {
            if (klass==CashFlow.class)
                return new CashFlowVisitor();
            if (klass==Coupon.class)
                return new CouponVisitor();

            throw new AssertionError(UNKNOWN_VISITABLE);
        }


        //
        // private inner classes
        //

        private class CashFlowVisitor implements Visitor<Object> {
            @Override
            public void visit(final Object o) {
                // nothing
            }
        }

        private class CouponVisitor implements Visitor<Object> {
            @Override
            public void visit(final Object o) {
                final Coupon c = (Coupon) o;
                result += c.accrualPeriod() * c.nominal() * termStructure.getLink().discount(c.date());
            }
        }

    }

}
