/*
 Copyright (C) 2008 Richard Gomes

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
 Copyright (C) 2005, 2006, 2007 StatPro Italia srl

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

package org.jquantlib.termstructures.yieldcurves;

import org.jquantlib.Error;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.lang.reflect.TypeNode;
import org.jquantlib.lang.reflect.TypeTokenTree;
import org.jquantlib.math.Array;
import org.jquantlib.math.Constants;
import org.jquantlib.math.UnaryFunctionDouble;
import org.jquantlib.math.interpolations.Interpolation;
import org.jquantlib.math.interpolations.Interpolator;
import org.jquantlib.math.interpolations.factories.BackwardFlat;
import org.jquantlib.math.interpolations.factories.Linear;
import org.jquantlib.math.interpolations.factories.LogLinear;
import org.jquantlib.math.solvers1D.Brent;
import org.jquantlib.termstructures.AbstractYieldTermStructure;
import org.jquantlib.termstructures.Compounding;
import org.jquantlib.termstructures.InterestRate;
import org.jquantlib.termstructures.RateHelper;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Frequency;
import org.jquantlib.time.Period;
import org.jquantlib.time.calendars.Target;
import org.jquantlib.util.Date;
import org.jquantlib.util.LazyObject;
import org.jquantlib.util.Observable;
import org.jquantlib.util.Pair;

/**
 * Piecewise yield term structure
 * <p>
 * This term structure is bootstrapped on a number of interest rate instruments which are passed as a vector of handles to
 * RateHelper instances. Their maturities mark the boundaries of the interpolated segments.
 * <p>
 * Each segment is determined sequentially starting from the earliest period to the latest and is chosen so that the instrument
 * whose maturity marks the end of such segment is correctly repriced on the curve.
 * 
 * @note The bootstrapping algorithm will raise an exception if any two instruments have the same maturity date.
 * 
 * 
 * @category yieldtermstructures
 * 
 * @author Richard Gomes
 */
public class PiecewiseYieldCurve<C extends CurveTraits, I extends Interpolator> extends LazyObject implements YieldTraits<I> { 
	
    private final PiecewiseYieldCurve<C,I> container;
    private final YieldTraits<I> baseCurve; // TODO: refactor to YieldCurve ???
    private final CurveTraits    traits;

    // data members
    private RateHelper[] instruments;
    private /*@Price*/ double accuracy;

    // common :: SHOULD GO INTO A DEFAULT DELEGATOR WHICH IMPLEMENTS YieldTraits
    private Date[]                            dates;
    private /* @Time */ Array                 times;
    private /* @Rate */ Array                 data;
    private Interpolation                     interpolation;
    private I                                 interpolator;

    
    public PiecewiseYieldCurve(
            final Date referenceDate, 
            final RateHelper[] instruments, 
            final DayCounter dayCounter,
            final/* @Price */double accuracy, 
            final I interpolator) {

        if (System.getProperty("EXPERIMENTAL") == null)
            throw new UnsupportedOperationException("Work in progress");
        
        final TypeNode root = new TypeTokenTree(this.getClass()).getRoot();
        final Class<?> cparam = root.get(0).getElement();
        final Class<?> iparam = root.get(1).getElement();

        if (cparam==null || iparam==null) throw new IllegalArgumentException("class parameter(s) not specified");
        if (interpolator==null) throw new NullPointerException("interpolation is null"); // TODO: message
        if (!interpolator.getClass().isAssignableFrom(iparam)) 
            throw new ClassCastException("interpolator does not match parameterized type");
        
        if (Discount.class.isAssignableFrom(cparam)) {
            this.baseCurve = new InterpolatedDiscountCurve(referenceDate, dayCounter, interpolator);
            this.traits = new Discount();
        } else if (ForwardRate.class.isAssignableFrom(cparam)) {
            this.baseCurve = new InterpolatedForwardCurve(referenceDate, dayCounter, interpolator);
            this.traits = new ForwardRate();
        } else if (ZeroYield.class.isAssignableFrom(cparam)) {
            this.baseCurve = new InterpolatedZeroCurve(referenceDate, dayCounter, interpolator);
            this.traits = new ZeroYield();
        } else {
            throw new UnsupportedOperationException("only Discount, ForwardRate and ZeroYield are supported");
        }

        this.instruments = instruments;
        this.accuracy = accuracy;
        this.container = this;
        checkInstruments();
    }	

    public PiecewiseYieldCurve(
            final /*@Natural*/ int settlementDays, 
            final Calendar calendar,
            final RateHelper[] instruments,
            final DayCounter dayCounter,
            final/* @Price */double accuracy,
            final I interpolator) {

        if (System.getProperty("EXPERIMENTAL") == null)
            throw new UnsupportedOperationException("Work in progress");
        
        final TypeNode root = new TypeTokenTree(this.getClass()).getRoot();
        final Class<?> cparam = root.get(0).getElement();
        final Class<?> iparam = root.get(1).getElement();

        if (cparam==null || iparam==null) throw new IllegalArgumentException("class parameter(s) not specified");
        if (interpolator==null) throw new NullPointerException("interpolation is null"); // TODO: message
        if (!interpolator.getClass().isAssignableFrom(iparam)) 
            throw new ClassCastException("interpolator does not match parameterized type");
        
        if (Discount.class.isAssignableFrom(cparam)) {
            this.baseCurve = new InterpolatedDiscountCurve(settlementDays, calendar, dayCounter, interpolator);
            this.traits = new Discount();
        } else if (ForwardRate.class.isAssignableFrom(cparam)) {
            this.baseCurve = new InterpolatedForwardCurve(settlementDays, calendar, dayCounter, interpolator);
            this.traits = new ForwardRate();
        } else if (ZeroYield.class.isAssignableFrom(cparam)) {
            this.baseCurve = new InterpolatedZeroCurve(settlementDays, calendar, dayCounter, interpolator);
            this.traits = new ZeroYield();
        } else {
            throw new UnsupportedOperationException("only Discount, ForwardRate and ZeroYield are supported");
        }

        this.instruments = instruments;
        this.accuracy = accuracy;
        this.container = this;
        checkInstruments();
    }   

    private void checkInstruments() {
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        Error.QL_REQUIRE(!(instruments.length == 0), "no instrument given");

        // sort rate helpers
        for (int i = 0; i < instruments.length; i++)
            instruments[i].setTermStructure(this); // TODO: code review

        // TODO : compilation error
        // Std.sort(instruments_, new RateHelperSorter());
        //                   

        // check that there is no instruments with the same maturity
        for (int i = 1; i < instruments.length; i++) {
            Date m1 = instruments[i - 1].latestDate();
            Date m2 = instruments[i].latestDate();
            Error.QL_REQUIRE(m1 != m2, "two instruments have the same maturity");
        }
        for (int i = 0; i < instruments.length; i++)
            instruments[i].addObserver(this);
    }

    
    //
    // overrides LazyObject
    //
    
    @Override
    public void performCalculations() /* @ReadOnly */ {
        // check that there is no instruments with invalid quote
        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
        for (int i = 0; i < instruments.length; i++)
            Error.QL_REQUIRE(instruments[i].referenceQuote() != Constants.NULL_Double, "instrument with null price");

        // setup vectors
        int n = instruments.length;
        for (int i = 0; i < n; i++) {
            // don't try this at home!
            instruments[i].setTermStructure(this); // TODO: code review : const_cast<PiecewiseYieldCurve<C,I>*>(this));
        }
        this.dates = new Date[n + 1];
        this.times = new Array(n + 1);
        this.data = new Array(n + 1);

        this.dates[0] = this.referenceDate();
        this.times.set(0, 0.0);

        double prev = traits.initialValue();
        this.data.set(0, prev);

        for (int i = 0; i < n; i++) {
            this.dates[i + 1] = instruments[i].latestDate();
            this.times.set(i + 1, this.timeFromReference(this.dates[i + 1]));
            this.data.set(i + 1, prev);
        }

        final Brent solver = new Brent();
        int maxIterations = 25;
        // bootstrapping loop
        for (int iteration = 0;; iteration++) {
            final Array previousData = this.data.clone();
            for (int i = 1; i < n + 1; i++) {
                if (iteration == 0) {
                    // extend interpolation a point at a time
                    if (this.interpolator.global() && i < 2) {
                        // not enough points for splines
                        this.interpolation = new Linear().interpolate(this.times, this.data);
                    } else {
                        this.interpolation = this.interpolator.interpolate(this.times, this.data);
                    }
                }
                this.interpolation.update();
                RateHelper instrument = instruments[i - 1];
                /* @Price */double guess;
                if (iteration > 0) {
                    // use perturbed value from previous loop
                    guess = 0.99 * this.data.get(i);
                } else if (i > 1) {
                    // extrapolate
                    guess = traits.guess(this, this.dates[i]);
                } else {
                    guess = traits.initialGuess();
                }
                // bracket
                /* @Price */double min = traits.minValueAfter(i, this.data);
                /* @Price */double max = traits.maxValueAfter(i, this.data);
                if (guess <= min || guess >= max)
                    guess = (min + max) / 2.0;
                try {
                    final ObjectiveFunction<C, I> f = new ObjectiveFunction<C, I>(this, instrument, i);
                    this.data.set(i, solver.solve(f, accuracy, guess, min, max));
                } catch (Exception e) {
                    throw new IllegalArgumentException("could not bootstrap"); // TODO: message
                }
            }
            // check exit conditions
            if (this.interpolator.global())
                break; // no need for convergence loop

            double improvement = 0.0;
            for (int i = 1; i < n + 1; i++)
                improvement += Math.abs(this.data.get(i) - previousData.get(i));
            if (improvement <= n * accuracy) // convergence reached
                break;

            if (iteration > maxIterations)
                throw new IllegalArgumentException("convergence not reached"); // TODO: message
        }
    }

    
    //
    // implements YieldTraits
    //
    
    @Override
    public Date[] dates() {
        calculate();
        return baseCurve.dates();
    }

    @Override
    public Date maxDate() {
        calculate();
        return baseCurve.maxDate();
    }

    @Override
    public Array times() {
        calculate();
        return baseCurve.times();
    }

    @Override
    public Pair<Date, Double>[] nodes() {
        calculate();
        return baseCurve.nodes();
    }

    @Override
    public double discountImpl(double t) {
        calculate();
        return baseCurve.discountImpl(t);
    }

    @Override
    public Array discounts() {
        return baseCurve.discounts();
    }

    @Override
    public Array forwards() {
        return baseCurve.forwards();
    }

    @Override
    public Array zeroRates() {
        return baseCurve.zeroRates();
    }

    @Override
    public double forwardImpl(double t) {
        calculate();
        return baseCurve.forwardImpl(t);
    }

    @Override
    public double zeroYieldImpl(double t) {
        calculate();
        return baseCurve.zeroYieldImpl(t);
    }

    
    //
    // implements Extrapolator
    //
    
    @Override
    public boolean allowsExtrapolation() {
        return baseCurve.allowsExtrapolation();
    }

    @Override
    public void disableExtrapolation() {
        baseCurve.disableExtrapolation();
    }

    @Override
    public void enableExtrapolation() {
        baseCurve.enableExtrapolation();
    }


    //
    // implements TermStructure
    //
    
    @Override
    public Calendar calendar() {
        return baseCurve.calendar();
    }

    @Override
    public DayCounter dayCounter() {
        return baseCurve.dayCounter();
    }

    @Override
    public double maxTime() {
        return baseCurve.maxTime();
    }

    @Override
    public Date referenceDate() {
        return baseCurve.referenceDate();
    }

    @Override
    public double timeFromReference(Date date) {
        return baseCurve.timeFromReference(date);
    }

    @Override
    public double discount(Date d, boolean extrapolate) {
        return baseCurve.discount(d, extrapolate);
    }

    @Override
    public double discount(Date d) {
        return baseCurve.discount(d);
    }

    @Override
    public double discount(double t, boolean extrapolate) {
        return baseCurve.discount(t, extrapolate);
    }

    @Override
    public double discount(double t) {
        return baseCurve.discount(t);
    }


    //
    // implements YieldTermStructure
    //
    
    @Override
    public InterestRate forwardRate(Date d1, Date d2, DayCounter dayCounter, Compounding comp, Frequency freq, boolean extrapolate) {
        return baseCurve.forwardRate(d1, d2, dayCounter, comp, freq, extrapolate);
    }

    @Override
    public InterestRate forwardRate(Date d1, Date d2, DayCounter resultDayCounter, Compounding comp, Frequency freq) {
        return baseCurve.forwardRate(d1, d2, resultDayCounter, comp, freq);
    }

    @Override
    public InterestRate forwardRate(Date d1, Date d2, DayCounter resultDayCounter, Compounding comp) {
        return baseCurve.forwardRate(d1, d2, resultDayCounter, comp);
    }

    @Override
    public InterestRate forwardRate(Date d, Period p, DayCounter dayCounter, Compounding comp, Frequency freq, boolean extrapolate) {
        return baseCurve.forwardRate(d, p, dayCounter, comp, freq, extrapolate);
    }

    @Override
    public InterestRate forwardRate(Date d, Period p, DayCounter resultDayCounter, Compounding comp, Frequency freq) {
        return baseCurve.forwardRate(d, p, resultDayCounter, comp, freq);
    }

    @Override
    public InterestRate forwardRate(double time1, double time2, Compounding comp, Frequency freq, boolean extrapolate) {
        return baseCurve.forwardRate(time1, time2, comp, freq, extrapolate);
    }

    @Override
    public InterestRate forwardRate(double t1, double t2, Compounding comp, Frequency freq) {
        return baseCurve.forwardRate(t1, t2, comp, freq);
    }

    @Override
    public InterestRate forwardRate(double t1, double t2, Compounding comp) {
        return baseCurve.forwardRate(t1, t2, comp);
    }

    @Override
    public double parRate(Date[] dates, Frequency freq, boolean extrapolate) {
        return baseCurve.parRate(dates, freq, extrapolate);
    }
    

    @Override
    public double parRate(double[] times, Frequency frequency, boolean extrapolate) {
        return baseCurve.parRate(times, frequency, extrapolate);
    }

    @Override
    public double parRate(int tenor, Date startDate, Frequency freq, boolean extrapolate) {
        return baseCurve.parRate(tenor, startDate, freq, extrapolate);
    }

    @Override
    public InterestRate zeroRate(Date d, DayCounter dayCounter, Compounding comp, Frequency freq, boolean extrapolate) {
        return baseCurve.zeroRate(d, dayCounter, comp, freq, extrapolate);
    }

    @Override
    public InterestRate zeroRate(Date d, DayCounter resultDayCounter, Compounding comp, Frequency freq) {
        return baseCurve.zeroRate(d, resultDayCounter, comp, freq);
    }

    @Override
    public InterestRate zeroRate(Date d, DayCounter resultDayCounter, Compounding comp) {
        return baseCurve.zeroRate(d, resultDayCounter, comp);
    }

    @Override
    public InterestRate zeroRate(double time, Compounding comp, Frequency freq, boolean extrapolate) {
        return baseCurve.zeroRate(time, comp, freq, extrapolate);
    }

    
    //
    // implements Observer interface
    //

    public void update(final Observable o, final Object arg) {
        baseCurve.update(o, arg);
        super.update(o, arg);
    }
    
    
    //
    // inner classes
    //

    /**
     * Term structure based on interpolation of discount factors.
     * 
     * @note LogLinear interpolation is assumed by default when no interpolation class is passed to constructors.
     * Log-linear interpolation guarantees piecewise-constant forward rates.
     * 
     * @category yieldtermstructures
     * 
     * @author Richard Gomes
     */
    private class InterpolatedDiscountCurve extends AbstractYieldTermStructure implements YieldTraits<I> {

        private boolean isNegativeRates;

        public InterpolatedDiscountCurve(final DayCounter dayCounter, final I interpolator) {
            super(dayCounter);
            this.isNegativeRates = settings.isNegativeRates();
            container.interpolator = (interpolator!=null) ? interpolator : (I) new LogLinear();
        }

        public InterpolatedDiscountCurve(final Date referenceDate, final DayCounter dayCounter, final I interpolator) {
            super(referenceDate, Target.getCalendar(), dayCounter); // FIXME: code review :: default calendar
            this.isNegativeRates = settings.isNegativeRates();
            container.interpolator = (interpolator!=null) ? interpolator : (I) new LogLinear();
        }

        public InterpolatedDiscountCurve(final int settlementDays, final Calendar calendar, final DayCounter dayCounter, final I interpolator) {
            super(settlementDays, calendar, dayCounter);
            this.isNegativeRates = settings.isNegativeRates();
            container.interpolator = (interpolator!=null) ? interpolator : (I) new LogLinear();
        }

        //TODO: who's calling this constructor???
        public InterpolatedDiscountCurve(
                final Date[] dates, 
                final /* @DiscountFactor */ Array discounts,
                final DayCounter dayCounter, 
                final Calendar cal, 
                final I interpolator) {
            super(dates[0], cal, dayCounter);

            // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
            // in particular, we are verifying dates.length after calling the super(...)
            if (dates.length <= 1) throw new IllegalArgumentException("too few dates"); // FIXME: message
            if (dates.length != discounts.length) throw new IllegalArgumentException("dates/discount factors count mismatch"); // FIXME: message
            if (discounts.first() != 1.0) throw new IllegalArgumentException("the first discount must be == 1.0 to flag the corrsponding date as settlement date"); // FIXME: message

            isNegativeRates = settings.isNegativeRates();

            container.times = new Array(dates.length);
            for (int i = 1; i < dates.length; i++) {
                if (dates[i].le(dates[i-1]))
                    throw new IllegalArgumentException("invalid date"); // FIXME: message
                if (!isNegativeRates && (discounts.get(i) < 0.0)) throw new IllegalArgumentException("negative discount"); // FIXME: message
                double value = dayCounter.yearFraction(dates[0], dates[i]);
                times.set(i, value);
            }

            container.dates = dates.clone();
            container.data = discounts.clone();
            container.interpolator = (interpolator!=null) ? interpolator : (I) new LogLinear();
            container.interpolation = container.interpolator.interpolate(container.times, container.data);
            container.interpolation.update();
        }

        
        //
        // implements TraitsCurve
        //

        @Override
        public final Date maxDate() /* @ReadOnly */{
            return dates[dates.length - 1];
        }

        @Override
        public final Array times() /* @ReadOnly */{
            return times.clone();
        }

        @Override
        public final Date[] dates() /* @ReadOnly */{
            return dates.clone();
        }

        @Override
        public final Pair<Date, Double>[] nodes() /* @ReadOnly */{
            Pair<Date, /*@Rate*/Double>[] results = new Pair /* <Date, @Rate Double> */[dates.length];
            for (int i = 0; i < dates.length; i++)
                results[i] = new Pair<Date, Double>(dates[i], data.get(i));
            return results;
        }

        // exclusive to discount curve
        public /* @DiscountFactor */Array discounts() /* @ReadOnly */ {
            throw new UnsupportedOperationException(); 
        }

        // exclusive to forward curve
        public /* @Rate */Array forwards() /* @ReadOnly */ {
            return data.clone();
        }

        // exclusive to zero rate
        public /* @Rate */Array zeroRates() /* @ReadOnly */{
            throw new UnsupportedOperationException(); 
        }


        //
        // The following methods should be protected in order to mimick as it is done in C++
        //
        
        @Override
        public /* @DiscountFactor */ double discountImpl(final/* @Time */double t) /* @ReadOnly */ {
            return interpolation.evaluate(t, true);
        }

        @Override
        public /*@Rate*/ double forwardImpl(/*@Time*/double t) /* @ReadOnly */{
            throw new UnsupportedOperationException(); 
        }

        @Override
        public /*@Rate*/ double zeroYieldImpl(/*@Time*/double t) /* @ReadOnly */{
            throw new UnsupportedOperationException(); 
        }

    }
    
    /**
     * Term structure based on interpolation of forward rates
     * 
     * @category yieldtermstructures
     * 
     * @author Richard Gomes
     */
    private final class InterpolatedForwardCurve extends ForwardRateStructure implements YieldTraits<I> {
	    
	    private boolean      isNegativeRates;

	    public InterpolatedForwardCurve(final DayCounter dayCounter, final I interpolator) {
	        super(dayCounter);
	        container.interpolator = (interpolator!=null) ? interpolator : (I) new BackwardFlat();
	    }

	    public InterpolatedForwardCurve(final Date referenceDate, final DayCounter dayCounter, final I interpolator) {
	        super(referenceDate, Target.getCalendar(), dayCounter); // FIXME: code review:: default calendar
	        container.interpolator = (interpolator!=null) ? interpolator : (I) new BackwardFlat();
	    }

	    public InterpolatedForwardCurve(final int settlementDays, final Calendar calendar, final DayCounter dayCounter, final I interpolator) {
	        super(settlementDays, calendar, dayCounter);
	        container.interpolator = (interpolator!=null) ? interpolator : (I) new BackwardFlat();
	    }

	    //TODO: who's calling this constructor???
	    public InterpolatedForwardCurve(final Date[] dates, final /* @Rate */ Array forwards, final DayCounter dayCounter, final I interpolator) {
	        // FIXME: code review: calendar
	        // FIXME: must check dates
	        super(dates[0], Target.getCalendar(), dayCounter);

	        // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
	        // in particular, we are verifying dates.length after calling the super(...)
	        if (dates.length <= 1) throw new IllegalArgumentException("too few dates"); // FIXME: message
            if (dates.length != forwards.length) throw new IllegalArgumentException("dates/yields count mismatch"); // FIXME: message

	        isNegativeRates = settings.isNegativeRates();

	        container.times = new Array(dates.length);
	        for (int i = 1; i < dates.length; i++) {
	            if (dates[i].le(dates[i-1]))
	                throw new IllegalArgumentException("invalid date"); // FIXME: message
	            if (!isNegativeRates && (forwards.get(i) < 0.0)) throw new IllegalArgumentException("negative forward"); // FIXME: message
	            double value = dayCounter.yearFraction(dates[0], dates[i]);
	            times.set(i, value);
	        }

            container.dates = dates.clone();
            container.data = forwards.clone();
            container.interpolator = (interpolator!=null) ? interpolator : (I) new BackwardFlat();
	        container.interpolation = container.interpolator.interpolate(container.times, container.data);
	        container.interpolation.update();
	    }

	    //
	    // implements TraitsCurve
	    //

        @Override
        public final Date maxDate() /* @ReadOnly */{
            return dates[dates.length - 1];
        }

        @Override
        public final Array times() /* @ReadOnly */{
            return times.clone();
        }

        @Override
        public final Date[] dates() /* @ReadOnly */{
            return dates.clone();
        }

        @Override
        public final Pair<Date, Double>[] nodes() /* @ReadOnly */{
            Pair<Date, /*@Rate*/Double>[] results = new Pair /* <Date, @Rate Double> */[dates.length];
            for (int i = 0; i < dates.length; i++)
                results[i] = new Pair<Date, Double>(dates[i], data.get(i));
            return results;
        }

	    // exclusive to discount curve
	    public /* @DiscountFactor */Array discounts() /* @ReadOnly */ {
            throw new UnsupportedOperationException(); 
        }

	    // exclusive to forward curve
	    public /* @Rate */Array forwards() /* @ReadOnly */ {
	        return data.clone();
	    }

	    // exclusive to zero rate
	    public /* @Rate */Array zeroRates() /* @ReadOnly */{
            throw new UnsupportedOperationException(); 
        }


	    //
	    // The following methods should be protected in order to mimick as it is done in C++
	    //
	    
        @Override
        public /* @DiscountFactor */ double discountImpl(final/* @Time */double t) /* @ReadOnly */ {
            throw new UnsupportedOperationException(); 
        }

        @Override
        public /*@Rate*/ double forwardImpl(/*@Time*/double t) /* @ReadOnly */{
            return interpolation.evaluate(t, true); // FIXME: code review
        }

        @Override
        public /*@Rate*/ double zeroYieldImpl(/*@Time*/double t) /* @ReadOnly */{
            if (t == 0.0) 
                return forwardImpl(0.0);
            else 
                return interpolation.primitive(t, true) / t; // FIXME: code review
        }

	}
	



    /**
     * Term structure based on interpolation of zero yields
     * 
     * @category yieldtermstructures
     * 
     * @author Richard Gomes
     */
    private final class InterpolatedZeroCurve extends ZeroYieldStructure implements YieldTraits<I> {

        private boolean isNegativeRates;

        public InterpolatedZeroCurve(final DayCounter dayCounter, final I interpolator) {
            super(dayCounter);
            container.interpolator = (interpolator!=null) ? interpolator : (I) new BackwardFlat();
        }

        public InterpolatedZeroCurve(final Date referenceDate, final DayCounter dayCounter, final I interpolator) {
            super(referenceDate, Target.getCalendar(), dayCounter); // FIXME: code review : default calendar?
            container.interpolator = (interpolator!=null) ? interpolator : (I) new BackwardFlat();
        }

        public InterpolatedZeroCurve(final int settlementDays, final Calendar calendar, final DayCounter dayCounter, final I interpolator) {
            super(settlementDays,calendar, dayCounter);
            container.interpolator = (interpolator!=null) ? interpolator : (I) new BackwardFlat();
        }

        
        //
        // implements TraitsCurve
        //

        @Override
        public final Date maxDate() /* @ReadOnly */{
            return dates[dates.length - 1];
        }

        @Override
        public final Array times() /* @ReadOnly */{
            return times.clone();
        }

        @Override
        public final Date[] dates() /* @ReadOnly */{
            return dates.clone();
        }

        @Override
        public final Pair<Date, Double>[] nodes() /* @ReadOnly */{
            Pair<Date, /*@Rate*/Double>[] results = new Pair /* <Date, @Rate Double> */[dates.length];
            for (int i = 0; i < dates.length; i++)
                results[i] = new Pair<Date, Double>(dates[i], data.get(i));
            return results;
        }

        // exclusive to discount curve
        public /* @DiscountFactor */Array discounts() /* @ReadOnly */ {
            throw new UnsupportedOperationException(); 
        }

        // exclusive to forward curve
        public /* @Rate */Array forwards() /* @ReadOnly */ {
            return data.clone();
        }

        // exclusive to zero rate
        public /* @Rate */Array zeroRates() /* @ReadOnly */{
            throw new UnsupportedOperationException(); 
        }


        //
        // The following methods should be protected in order to mimick as it is done in C++
        //
        
        @Override
        public /* @DiscountFactor */ double discountImpl(final/* @Time */double t) /* @ReadOnly */ {
            throw new UnsupportedOperationException(); 
        }

        @Override
        public /*@Rate*/ double forwardImpl(/*@Time*/double t) /* @ReadOnly */{
            throw new UnsupportedOperationException(); 
        }

        @Override
        public /*@Rate*/ double zeroYieldImpl(/*@Time*/double t) /* @ReadOnly */{
            return interpolation.evaluate(t, true);
        }

    }

    
    private class ObjectiveFunction<C extends CurveTraits, I extends Interpolator> implements UnaryFunctionDouble {

        private final PiecewiseYieldCurve<C, I> curve;
        private final RateHelper rateHelper;
        private final int segment;

        public ObjectiveFunction(final PiecewiseYieldCurve<C, I> curve, final RateHelper rateHelper, final int segment) {
            this.curve = curve;
            this.rateHelper = rateHelper;
            this.segment = segment;
        }

        @Override
        public double evaluate(double guess) /* @ReadOnly */{
            traits.updateGuess(this.curve.data, guess, this.segment);
            curve.interpolation.update();
            return rateHelper.quoteError();
        }

    }
               
}