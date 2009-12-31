/*
 * Copyright (C) 2008 Richard Gomes This source code is release under the BSD License. This file is
 * part of JQuantLib, a free-software/open-source library for financial quantitative analysts and
 * developers - http://jquantlib.org/ JQuantLib is free software: you can redistribute it and/or
 * modify it under the terms of the JQuantLib license. You should have received a copy of the
 * license along with this program; if not, please email <jquant-devel@lists.sourceforge.net>. The
 * license is also available online at <http://www.jquantlib.org/index.php/LICENSE.TXT>. This
 * program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license for
 * more details. JQuantLib is based on QuantLib. http://quantlib.org/ When applicable, the original
 * copyright notice follows this notice.
 */

/*
 * Copyright (C) 2005, 2006, 2007 StatPro Italia srl This file is part of QuantLib, a
 * free-software/open-source library for financial quantitative analysts and developers -
 * http://quantlib.org/ QuantLib is free software: you can redistribute it and/or modify it under
 * the terms of the QuantLib license. You should have received a copy of the license along with this
 * program; if not, please email <quantlib-dev@lists.sf.net>. The license is also available online
 * at <http://quantlib.org/license.shtml>. This program is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the license for more details.
 */

package org.jquantlib.termstructures.yieldcurves;


import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.jquantlib.QL;
import org.jquantlib.Settings;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.math.interpolations.Interpolation;
import org.jquantlib.math.interpolations.Interpolator;
import org.jquantlib.math.matrixutilities.Array;
import org.jquantlib.termstructures.BootstrapHelperSorter;
import org.jquantlib.termstructures.Compounding;
import org.jquantlib.termstructures.InterestRate;
import org.jquantlib.termstructures.RateHelper;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.Frequency;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.util.LazyObject;
import org.jquantlib.util.Pair;


import org.jquantlib.termstructures.Bootstrapable;
import org.jquantlib.termstructures.Bootstrapper;

/**
 * Piecewise yield term structure
 * <p>
 * This term structure is bootstrapped on a number of interest rate instruments which are passed as
 * a vector of handles to RateHelper instances. Their maturities mark the boundaries of the
 * interpolated segments.
 * <p>
 * Each segment is determined sequentially starting from the earliest period to the latest and is
 * chosen so that the instrument whose maturity marks the end of such segment is correctly repriced
 * on the curve.
 * 
 * @note The bootstrapping algorithm will raise an exception if any two instruments have the same
 *       maturity date.
 * @category yieldtermstructures
 * @author Richard Gomes
 */
public class PiecewiseYieldCurve extends LazyObject implements YieldTermStructure
{
    private final List <RateHelper> instruments; 
    private Interpolator interpolator;
    private Bootstrapper bootstrapper;
    private YieldTermStructure baseCurve;

    public PiecewiseYieldCurve (final Date referenceDate, final RateHelper [] instruments,
                                final DayCounter dayCounter, 
                                final Interpolator interpolator, final BootstrapTraits traits,
                                Bootstrapper bootstrapper) 
                                
    {

        
        // we can apply the same reasoning for the interpolator. There is no need to 
        // use generics when we have a well defined interface to program to,
        // this class should support the ability for users to specify which 
        // curve trait and interpolator they would like to use. There is no need to do this
        // for them.

        this.instruments = Arrays.asList (instruments);
        this.interpolator = interpolator;
        this.bootstrapper = bootstrapper;

        this.baseCurve = traits.buildCurve (instruments.length, referenceDate,
                dayCounter, this.interpolator);
        
        this.bootstrapper.setup ((YieldTermStructure) baseCurve, (Bootstrapable) baseCurve, instruments, traits);

        checkInstruments ();
    }

    public PiecewiseYieldCurve (final int settlementDays, final Calendar calendar,
                                final RateHelper [] instruments, final DayCounter dayCounter,
                                final Interpolator interpolator,
                                final BootstrapTraits traits, final Bootstrapper bootstrapper, 
                                YieldTermStructure baseCurve) 
    {
        this.instruments = Arrays.asList (instruments);
        this.interpolator = interpolator;
        this.bootstrapper = bootstrapper;

        this.baseCurve = traits.buildCurve (instruments.length, 
            calendar.advance (new Settings().evaluationDate(), new Period (2, TimeUnit.Days)),
             dayCounter, this.interpolator);

        this.bootstrapper.setup (baseCurve, (Bootstrapable) this, instruments, traits);

        checkInstruments ();
    }

    private void checkInstruments ()
    {
        QL.require (instruments.size() > 0, "no instrument given");
                
        // check that there is no instruments with the same maturity
        for (int i = 1; i < instruments.size(); i ++)
        {
            final Date m1 = instruments.get (i - 1).latestDate ();
            final Date m2 = instruments.get(i).latestDate ();
            QL.require (! m1.eq (m2), "two instruments have the same maturity");
        }

        // sort rate helpers
        Collections.sort (instruments, (Comparator) new BootstrapHelperSorter <RateHelper> ());
    }

    //
    // overrides LazyObject
    //

    @Override
    public void performCalculations () /* @ReadOnly */
    {
        bootstrapper.calculate();
        /*

        // check that there is no instruments with invalid quote
        for (final RateHelper instrument2 : instruments)
            QL.require (instrument2.referenceQuote () != Constants.NULL_REAL,
                    "instrument with null price"); // TODO: message

        // setup vectors
        final int n = instruments.length;
        for (int i = 0; i < n; i ++)
            // don't try this at home!
            instruments[i].setTermStructure (this); // TODO: code review :
        // const_cast<PiecewiseYieldCurve<C,I>*>(this));
        this.dates = new Date[n + 1];
        this.times = new Array (n + 1);
        this.data = new Array (n + 1);

        this.dates[0] = this.referenceDate ();
        this.times.set (0, 0.0);

        final double prev = traits.initialValue ();
        this.data.set (0, prev);

        for (int i = 0; i < n; i ++)
        {
            this.dates[i + 1] = instruments[i].latestDate ();
            this.times.set (i + 1, this.timeFromReference (this.dates[i + 1]));
            this.data.set (i + 1, prev);
        }

        final Brent solver = new Brent ();
        final int maxIterations = 25;
        // bootstrapping loop
        for (int iteration = 0;; iteration ++)
        {
            final Array previousData = this.data.clone ();
            for (int i = 1; i < n + 1; i ++)
            {
                if (iteration == 0)
                    // extend interpolation a point at a time
                    if (this.interpolator.global () && i < 2)
                        // not enough points for splines
                        this.interpolation = new Linear ().interpolate (times.constIterator (),
                                data.constIterator ());
                    else
                        this.interpolation = this.interpolator.interpolate (times.constIterator (),
                                data.constIterator ());
                this.interpolation.update ();
                final RateHelper instrument = instruments[i - 1];
                double guess;
                if (iteration > 0)
                    // use perturbed value from previous loop
                    guess = 0.99 * this.data.get (i);
                else if (i > 1)
                    // extrapolate
                    guess = traits.guess (this, this.dates[i]);
                else
                    guess = traits.initialGuess ();
                // bracket
                final double min = traits.minValueAfter (i, this.data);
                final double max = traits.maxValueAfter (i, this.data);
                if (guess <= min || guess >= max)
                    guess = (min + max) / 2.0;
                try
                {
                    final ObjectiveFunction <C, I> f = new ObjectiveFunction <C, I> (this,
                            instrument, i);
                    this.data.set (i, solver.solve (f, accuracy, guess, min, max));
                }
                catch (final Exception e)
                {
                    throw new LibraryException ("could not bootstrap"); // QA:[RG]::verified //
                    // TODO: message
                }
            }
            // check exit conditions
            if (this.interpolator.global ())
                break; // no need for convergence loop

            double improvement = 0.0;
            for (int i = 1; i < n + 1; i ++)
                improvement += Math.abs (this.data.get (i) - previousData.get (i));
            if (improvement <= n * accuracy)
                break;

            QL.require (iteration <= maxIterations, "convergence not reached"); // TODO: message
        }
        */
    }
//
//    @Override
//    public Date [] dates ()
//    {
//        calculate ();
//        return baseCurve.dates ();
//    }
//
    @Override
    public Date maxDate ()
    {
        calculate ();
        return baseCurve.maxDate ();
    }
    //FIXME JM remove nodes
    public List <Pair <Date, Double> > getNodes ()
    {
        return ((InterpolatedDiscountCurve) baseCurve).nodes ();
    }
//
//    @Override
//    public Array times ()
//    {
//        calculate ();
//        return baseCurve.times ();
//    }
//
//    @Override
//    public Pair <Date, Double> [] nodes ()
//    {
//        calculate ();
//        return baseCurve.nodes ();
//    }
//
//    @Override
//    public double discountImpl (final double t)
//    {
//        calculate ();
//        return baseCurve.discountImpl (t);
//    }
//
//    @Override
//    public Array discounts ()
//    {
//        return baseCurve.discounts ();
//    }
//
//    @Override
//    public Array forwards ()
//    {
//        return baseCurve.forwards ();
//    }
//
//    @Override
//    public Array zeroRates ()
//    {
//        return baseCurve.zeroRates ();
//    }
//
//    @Override
//    public double forwardImpl (final double t)
//    {
//        calculate ();
//        return baseCurve.forwardImpl (t);
//    }
//
//    @Override
//    public double zeroYieldImpl (final double t)
//    {
//        calculate ();
//        return baseCurve.zeroYieldImpl (t);
//    }

    //
    // implements Extrapolator
    //

    @Override
    public boolean allowsExtrapolation ()
    {
        return baseCurve.allowsExtrapolation ();
    }

    @Override
    public void disableExtrapolation ()
    {
        baseCurve.disableExtrapolation ();
    }

    @Override
    public void enableExtrapolation ()
    {
        baseCurve.enableExtrapolation ();
    }

    //
    // implements TermStructure
    //

    @Override
    public Calendar calendar ()
    {
        return baseCurve.calendar ();
    }

    @Override
    public DayCounter dayCounter ()
    {
        return baseCurve.dayCounter ();
    }

    @Override
    public double maxTime ()
    {
        return baseCurve.maxTime ();
    }

    @Override
    public Date referenceDate ()
    {
        return baseCurve.referenceDate ();
    }

    @Override
    public/* @Natural */int settlementDays ()
    {
        return baseCurve.settlementDays ();
    }

    @Override
    public double timeFromReference (final Date date)
    {
        return baseCurve.timeFromReference (date);
    }

    //
    // implements YieldTermStructure
    //

    @Override
    public double discount (final Date d, final boolean extrapolate)
    {
        return baseCurve.discount (d, extrapolate);
    }

    @Override
    public double discount (final Date d)
    {
        return baseCurve.discount (d);
    }

    @Override
    public double discount (final double t, final boolean extrapolate)
    {
        return baseCurve.discount (t, extrapolate);
    }

    @Override
    public double discount (final double t)
    {
        return baseCurve.discount (t);
    }

    @Override
    public InterestRate forwardRate (final Date d1, final Date d2, final DayCounter dayCounter,
            final Compounding comp, final Frequency freq, final boolean extrapolate)
    {
        return baseCurve.forwardRate (d1, d2, dayCounter, comp, freq, extrapolate);
    }

    @Override
    public InterestRate forwardRate (final Date d1, final Date d2,
            final DayCounter resultDayCounter, final Compounding comp, final Frequency freq)
    {
        return baseCurve.forwardRate (d1, d2, resultDayCounter, comp, freq);
    }

    @Override
    public InterestRate forwardRate (final Date d1, final Date d2,
            final DayCounter resultDayCounter, final Compounding comp)
    {
        return baseCurve.forwardRate (d1, d2, resultDayCounter, comp);
    }

    @Override
    public InterestRate forwardRate (final Date d, final Period p, final DayCounter dayCounter,
            final Compounding comp, final Frequency freq, final boolean extrapolate)
    {
        return baseCurve.forwardRate (d, p, dayCounter, comp, freq, extrapolate);
    }

    @Override
    public InterestRate forwardRate (final Date d, final Period p,
            final DayCounter resultDayCounter, final Compounding comp, final Frequency freq)
    {
        return baseCurve.forwardRate (d, p, resultDayCounter, comp, freq);
    }

    @Override
    public InterestRate forwardRate (final double time1, final double time2,
            final Compounding comp, final Frequency freq, final boolean extrapolate)
    {
        return baseCurve.forwardRate (time1, time2, comp, freq, extrapolate);
    }

    @Override
    public InterestRate forwardRate (final double t1, final double t2, final Compounding comp,
            final Frequency freq)
    {
        return baseCurve.forwardRate (t1, t2, comp, freq);
    }

    @Override
    public InterestRate forwardRate (final double t1, final double t2, final Compounding comp)
    {
        return baseCurve.forwardRate (t1, t2, comp);
    }

    @Override
    public double parRate (final Date [] dates, final Frequency freq, final boolean extrapolate)
    {
        return baseCurve.parRate (dates, freq, extrapolate);
    }

    @Override
    public double parRate (final double [] times, final Frequency frequency,
            final boolean extrapolate)
    {
        return baseCurve.parRate (times, frequency, extrapolate);
    }

    @Override
    public double parRate (final int tenor, final Date startDate, final Frequency freq,
            final boolean extrapolate)
    {
        return baseCurve.parRate (tenor, startDate, freq, extrapolate);
    }

    @Override
    public InterestRate zeroRate (final Date d, final DayCounter dayCounter,
            final Compounding comp, final Frequency freq, final boolean extrapolate)
    {
        return baseCurve.zeroRate (d, dayCounter, comp, freq, extrapolate);
    }

    @Override
    public InterestRate zeroRate (final Date d, final DayCounter resultDayCounter,
            final Compounding comp, final Frequency freq)
    {
        return baseCurve.zeroRate (d, resultDayCounter, comp, freq);
    }

    @Override
    public InterestRate zeroRate (final Date d, final DayCounter resultDayCounter,
            final Compounding comp)
    {
        return baseCurve.zeroRate (d, resultDayCounter, comp);
    }

    @Override
    public InterestRate zeroRate (final double time, final Compounding comp, final Frequency freq,
            final boolean extrapolate)
    {
        return baseCurve.zeroRate (time, comp, freq, extrapolate);
    }

    //
    // implements Observer
    //

    @Override
    // XXX::OBS public void update(final Observable o, final Object arg) {
    public void update ()
    {
        // XXX::OBS baseCurve.update(o, arg);
        baseCurve.update ();
        // XXX::OBS super.update(o, arg);
        super.update ();
    }

}