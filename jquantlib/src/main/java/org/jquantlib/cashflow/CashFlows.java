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

import java.util.List;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.math.UnaryFunctionDouble;
import org.jquantlib.math.solvers1D.Brent;
import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.Compounding;
import org.jquantlib.termstructures.InterestRate;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.termstructures.yieldcurves.FlatForward;
import org.jquantlib.time.Frequency;
import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;
import org.jquantlib.util.Visitable;
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

    private double basisPoint_ = 1.0e-4;

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
        if (instance == null) {
            synchronized (CashFlows.class) {
                if (instance == null) {
                    instance = new CashFlows();
                }
            }
        }
        return instance;
    }

    
    //
    // public methods
    //
    
    public Date startDate(final List<CashFlow> cashflows) {
        Date d = DateFactory.getFactory().getMaxDate();
        for (int i = 0; i < cashflows.size(); ++i) {
            Coupon c = (Coupon) cashflows.get(i);
            if (c != null) {
                d = Std.getInstance().min(c.accrualStartDate(), d);
            }
        }
        if (d == DateFactory.getFactory().getMaxDate()) {
            throw new IllegalArgumentException(not_enough_information_available);
        }
        return d;
    }

    public Date maturityDate(final List<CashFlow> cashflows) {
        Date d = DateFactory.getFactory().getMinDate();
        for (int i = 0; i < cashflows.size(); i++) {
            d = Std.getInstance().max(d, cashflows.get(i).date());
        }
        if (d == DateFactory.getFactory().getMinDate()) {
            throw new IllegalArgumentException(no_cashflows);
        }
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
    public double npv(final List<CashFlow> cashflows, final Handle<YieldTermStructure> discountCurve, final Date settlementDate,
            final Date npvDate, int exDividendDays) {
        if (true) {
            throw new UnsupportedOperationException("work in progress");
        }
        /*
         * FIXME: how to implement this Date d = settlementDate != Date() ? settlementDate : discountCurve->referenceDate();
         */
        Date d = null;

        double totalNPV = 0.0;
        for (int i = 0; i < cashflows.size(); ++i) {
            if (!cashflows.get(i).hasOccurred(d.increment(exDividendDays))) {
                totalNPV += cashflows.get(i).amount() * discountCurve.getLink().discount(cashflows.get(i).date());
            }
        }
        /*
         * FIXME: how to implement this if (npvDate==Date()){ return totalNPV; } "/ else{ return
         * totalNPV/discountCurve.getLink().discount(npvDate); }
         */
        return totalNPV / discountCurve.getLink().discount(npvDate);

    }

    public double npv(final List<CashFlow> leg, final Handle<YieldTermStructure> discountCurve) {
        return npv(leg, discountCurve, DateFactory.getFactory().getTodaysDate(), DateFactory.getFactory().getTodaysDate(), 0);
    }

    /**
     * NPV of the cash flows.
     * <p>
     * The NPV is the sum of the cash flows, each discounted according to the given constant interest rate. The result is affected
     * by the choice of the interest-rate compounding and the relative frequency and day counter.
     */
    public double npv(final List<CashFlow> cashflows, final InterestRate irr, Date settlementDate) {
        if (true) {
            throw new UnsupportedOperationException("work in progress");
        }
        /*
         * FIXME: how to implement this if (settlementDate == Date()) settlementDate = Settings::instance().evaluationDate();
         */
        YieldTermStructure flatRate = new FlatForward(settlementDate, irr.rate(), irr.dayCounter(), irr.compounding(), irr
                .frequency());
        return npv(cashflows, new Handle<YieldTermStructure>(flatRate), settlementDate, settlementDate, 0);
    }

    public double npv(final List<CashFlow> leg, final InterestRate interestRate) {
        return npv(leg, interestRate, DateFactory.getFactory().getTodaysDate());
    }

    /**
     * Basis-point sensitivity of the cash flows.
     * <p>
     * The result is the change in NPV due to a uniform 1-basis-point change in the rate paid by the cash flows. The change for
     * each coupon is discounted according to the given term structure.
     */
    public double bps(final List<CashFlow> cashflows, final Handle<YieldTermStructure> discountCurve, final Date settlementDate,
            final Date npvDate, int exDividendDays) {
        if (true) {
            throw new UnsupportedOperationException("Work in progress");
        }

        Date d = settlementDate;
        
        
        // FIXME: how to translate this?? Date d = settlementDate; if (d==Date()){ d = discountCurve->referenceDate(); }
        final BPSCalculator calc = new BPSCalculator(discountCurve, npvDate);
        for (int i = 0; i < cashflows.size(); ++i) {
            if (!cashflows.get(i).hasOccurred(d.increment(exDividendDays))) {
                throw new UnsupportedOperationException("Work in progress");
                // cashflows.get(i).accept(calc);
            }
        }
        return basisPoint_ * calc.result();
    }

    public double bps(final List<CashFlow> leg, final Handle<YieldTermStructure> discountCurve) {
        return bps(leg, discountCurve, DateFactory.getFactory().getTodaysDate(), DateFactory.getFactory().getTodaysDate(), 0);
    }

    /**
     * Basis-point sensitivity of the cash flows.
     * <p>
     * The result is the change in NPV due to a uniform 1-basis-point change in the rate paid by the cash flows. The change for
     * each coupon is discounted according to the given constant interest rate. The result is affected by the choice of the
     * interest-rate compounding and the relative frequency and day counter.
     */
    public double bps(final List<CashFlow> cashflows, final InterestRate irr, Date settlementDate) {
        if (true) {
            throw new UnsupportedOperationException("Work in progress");
        }
        /*
         * FIXME: how to translate this?? if (settlementDate == Date()){ settlementDate = Settings::instance().evaluationDate(); }
         */
        YieldTermStructure flatRate = new FlatForward(settlementDate, irr.rate(), irr.dayCounter(), irr.compounding(), irr
                .frequency());
        // TODO: 0 added by hand -> do we have to hide the method using another layer ?
        return bps(cashflows, new Handle<YieldTermStructure>(flatRate), settlementDate, settlementDate, 0);
    }

    public double bps(final List<CashFlow> leg, final InterestRate interestRate) {
        return bps(leg, interestRate, DateFactory.getFactory().getTodaysDate());
    }

    /**
     * At-the-money rate of the cash flows.
     * <p>
     * The result is the fixed rate for which a fixed rate cash flow vector, equivalent to the input vector, has the required NPV
     * according to the given term structure. If the required NPV is not given, the input cash flow vector's NPV is used instead.
     */
    public double atmRate(final List<CashFlow> leg, final Handle<YieldTermStructure> discountCurve, final Date settlementDate,
            final Date npvDate, int exDividendDays, double npv) {
        double bps = bps(leg, discountCurve, settlementDate, npvDate, exDividendDays);
        if (npv == 0) {
            npv = npv(leg, discountCurve, settlementDate, npvDate, exDividendDays);
        }
        return basisPoint_ * npv / bps;
    }

    public double atmRate(final List<CashFlow> leg, final Handle<YieldTermStructure> discountCurve) {
        return atmRate(leg, discountCurve, DateFactory.getFactory().getTodaysDate(), DateFactory.getFactory().getTodaysDate(), 0, 0);
    }

    /**
     * Internal rate of return.
     * <p>
     * The IRR is the interest rate at which the NPV of the cash flows equals the given market price. The function verifies the
     * theoretical existance of an IRR and numerically establishes the IRR to the desired precision.
     */
    public double irr(final List<CashFlow> cashflows, double marketPrice, final DayCounter dayCounter, Compounding compounding,
            Frequency frequency, Date settlementDate, double tolerance, int maxIterations, double guess) {
        if (true) {
            throw new UnsupportedOperationException("Work in progress");
        }
        /*
         * FIXME: how to translate this?? if (settlementDate == Date()) settlementDate = Settings::instance().evaluationDate();
         */
        // depending on the sign of the market price, check that cash
        // flows of the opposite sign have been specified (otherwise
        // IRR is nonsensical.)

        int lastSign = sign(-marketPrice), signChanges = 0;
        for (int i = 0; i < cashflows.size(); ++i) {
            if (!cashflows.get(i).hasOccurred(settlementDate)) {
                int thisSign = sign(cashflows.get(i).amount());
                if (lastSign * thisSign < 0) { // sign change{
                    signChanges++;
                }
                if (thisSign != 0) {
                    lastSign = thisSign;
                }
            }
        }
        if (signChanges <= 0) {
            throw new IllegalArgumentException(infeasible_cashflow);
        }

        /*
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

        Brent solver = new Brent();
        solver.setMaxEvaluations(maxIterations);
        return solver.solve(new IRRFinder(cashflows, marketPrice, dayCounter, compounding, frequency, settlementDate), tolerance,
                guess, guess / 10.0);
    }

    public double irr(final List<CashFlow> leg, double marketPrice, final DayCounter dayCounter, Compounding compounding) {
        return irr(leg, marketPrice, dayCounter, compounding, Frequency.NO_FREQUENCY, DateFactory.getFactory().getTodaysDate(),
                1.0e-10, 10000, 0.05);
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
    public double duration(final List<CashFlow> leg, final InterestRate y, Duration duration, Date date) {
        if (true) {
            throw new UnsupportedOperationException("Work in progress");
        }
        /*
         * FIXME: how to translate this?? if (settlementDate == Date()) settlementDate = Settings::instance().evaluationDate();
         */
        switch (duration) {
        case Simple:
            return simpleDuration(leg, y, date);
        case Modified:
            return modifiedDuration(leg, y, date);
        case Macaulay:
            return macaulayDuration(leg, y, date);
        default:
            throw new IllegalArgumentException(unknown_duration_type);
        }
    }

    public double duration(final List<CashFlow> leg, final InterestRate y) {
        return duration(leg, y, Duration.Modified, DateFactory.getFactory().getTodaysDate());

    }

    /**
     * Cash-flow convexity
     * <p>
     * The convexity of a string of cash flows is defined as 
     * {@latex[ C = \frac{1}{P} \frac{\partial^2 P}{\partial y^2} } 
     * where {@latex$ P } is the present value of the cash flows according to the given IRR {@latex$ y }.
     */
    public double convexity(final List<CashFlow> cashFlows, final InterestRate rate, Date settlementDate) {
        // FIXME.. how to translate this???
        if (true) {
            throw new UnsupportedOperationException("Work in progress");
        }
        /*
         * if (settlementDate == Date()){ settlementDate = Settings::instance().evaluationDate(); }
         */

        DayCounter dayCounter = rate.dayCounter();

        double P = 0.0;
        double d2Pdy2 = 0.0;
        double y = rate.rate();
        int N = rate.frequency().toInteger();

        for (int i = 0; i < cashFlows.size(); ++i) {
            if (!cashFlows.get(i).hasOccurred(settlementDate)) {
                double t = dayCounter.yearFraction(settlementDate, cashFlows.get(i).date());
                double c = cashFlows.get(i).amount();
                double B = rate.discountFactor(t);

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
        }

        if (P == 0.0) {
            // no cashflows
            return 0.0;
        }
        return d2Pdy2 / P;
    }

    public double convexity(final List<CashFlow> leg, final InterestRate y) {
        return convexity(leg, y, DateFactory.getFactory().getTodaysDate());
    }

    private double simpleDuration(final List<CashFlow> cashflows, final InterestRate rate, Date settlementDate) {

        double P = 0.0;
        double tP = 0.0;

        for (int i = 0; i < cashflows.size(); ++i) {
            if (!cashflows.get(i).hasOccurred(settlementDate)) {
                double t = rate.dayCounter().yearFraction(settlementDate, cashflows.get(i).date());
                double c = cashflows.get(i).amount();
                double B = rate.discountFactor(t);

                P += c * B;
                tP += t * c * B;
            }
        }

        if (P == 0.0)
            // no cashflows
            return 0.0;

        return tP / P;
    }

    private double modifiedDuration(final List<CashFlow> cashflows, final InterestRate rate, Date settlementDate) {

        double P = 0.0;
        double dPdy = 0.0;
        double y = rate.rate();
        int N = rate.frequency().toInteger();

        for (int i = 0; i < cashflows.size(); ++i) {
            if (!cashflows.get(i).hasOccurred(settlementDate)) {
                double t = rate.dayCounter().yearFraction(settlementDate, cashflows.get(i).date());
                double c = cashflows.get(i).amount();
                double B = rate.discountFactor(t);

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
        }

        if (P == 0.0) {
            // no cashflows
            return 0.0;
        }
        return -dPdy / P;
    }

    private double macaulayDuration(final List<CashFlow> cashflows, final InterestRate rate, Date settlementDate) {

        double y = rate.rate();
        int N = rate.frequency().toInteger();
        if (!rate.compounding().equals(Compounding.COMPOUNDED)) {
            throw new IllegalArgumentException(compounded_rate_required);
        }
        if (N < 1) {
            throw new IllegalArgumentException(unsupported_frequency);
        }

        return (1 + y / N) * modifiedDuration(cashflows, rate, settlementDate);
    }

    private int sign(double x) {
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

    private class IRRFinder implements UnaryFunctionDouble {

        public IRRFinder(final List<CashFlow> cashflows, double marketPrice, final DayCounter dayCounter, Compounding compounding,
                Frequency frequency, Date settlementDate) {
            this.cashflows_ = cashflows;
            this.marketPrice_ = marketPrice;
            this.dayCounter_ = dayCounter;
            this.compounding_ = compounding;
            this.frequency_ = frequency;
            this.settlementDate_ = settlementDate;
        }

        private final List<CashFlow> cashflows_;
        private double marketPrice_;
        private DayCounter dayCounter_;
        private Compounding compounding_;
        private Frequency frequency_;
        private Date settlementDate_;

        @Override
        public double evaluate(double guess) {
            InterestRate rate = new InterestRate(guess, dayCounter_, compounding_, frequency_);
            double NPV = npv(cashflows_, rate, settlementDate_);
            return marketPrice_ - NPV;
        }
    }

    private class BPSCalculator implements Visitable {

        public BPSCalculator(final Handle<YieldTermStructure> termStructure, final Date npvDate) {
            this.termStructure_ = termStructure;
            this.npvDate_ = npvDate;
            this.result_ = 0.0;
        }

        public void accept(final Visitor v) {
            if (v instanceof Coupon) {
                Coupon c = (Coupon) v;
                result_ += c.accrualPeriod() * c.nominal() * termStructure_.getLink().discount(c.date());
            }
        }

        public double result() {
            /*
             * FIXME: how to implement tis one? if (npvDate_==Date()) return result_; else return
             * result_/termStructure_->discount(npvDate_);
             */
            if (true) {
                throw new UnsupportedOperationException("Work in progress");
            }
            return 0;
        }

        private Handle<YieldTermStructure> termStructure_;
        private Date npvDate_;
        private double result_;
    }

}
