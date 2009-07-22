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
import org.jquantlib.math.solvers1D.Brent;
import org.jquantlib.termstructures.Compounding;
import org.jquantlib.termstructures.InterestRate;
import org.jquantlib.termstructures.RateHelper;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Frequency;
import org.jquantlib.time.Period;
import org.jquantlib.time.calendars.Target;
import org.jquantlib.util.Date;
import org.jquantlib.util.LazyObject;
import org.jquantlib.util.Pair;

//TODO: Finish (Richard)

public class PiecewiseYieldCurve<C extends CurveTraits, I extends Interpolator> extends LazyObject implements YieldTraits<I> { 
	
//  
//  private methods
//
//      // helper classes for bootstrapping
//      class ObjectiveFunction;
//      friend class ObjectiveFunction;
//      // methods
//      void checkInstruments();
//      void performCalculations() const;
//      DiscountFactor discountImpl(Time) const;

    
    // ===================================================================================
    //
    // THESE METHODS ARE DECLARED FOR THE TIME BEING JUST TO AVOID COMPILATION ERRORS
    //


    @Override
    public boolean allowsExtrapolation() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void disableExtrapolation() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void enableExtrapolation() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Calendar calendar() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DayCounter dayCounter() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public double maxTime() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Date referenceDate() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public double timeFromReference(Date date) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double discount(Date d, boolean extrapolate) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double discount(Date d) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double discount(double t, boolean extrapolate) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double discount(double t) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public InterestRate forwardRate(Date d1, Date d2, DayCounter dayCounter, Compounding comp, Frequency freq, boolean extrapolate) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InterestRate forwardRate(Date d1, Date d2, DayCounter resultDayCounter, Compounding comp, Frequency freq) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InterestRate forwardRate(Date d1, Date d2, DayCounter resultDayCounter, Compounding comp) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InterestRate forwardRate(Date d, Period p, DayCounter dayCounter, Compounding comp, Frequency freq, boolean extrapolate) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InterestRate forwardRate(Date d, Period p, DayCounter resultDayCounter, Compounding comp, Frequency freq) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InterestRate forwardRate(double time1, double time2, Compounding comp, Frequency freq, boolean extrapolate) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InterestRate forwardRate(double t1, double t2, Compounding comp, Frequency freq) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InterestRate forwardRate(double t1, double t2, Compounding comp) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public double parRate(Date[] dates, Frequency freq, boolean extrapolate) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double parRate(double[] times, Frequency frequency, boolean extrapolate) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double parRate(int tenor, Date startDate, Frequency freq, boolean extrapolate) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public InterestRate zeroRate(Date d, DayCounter dayCounter, Compounding comp, Frequency freq, boolean extrapolate) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InterestRate zeroRate(Date d, DayCounter resultDayCounter, Compounding comp, Frequency freq) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InterestRate zeroRate(Date d, DayCounter resultDayCounter, Compounding comp) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InterestRate zeroRate(double time, Compounding comp, Frequency freq, boolean extrapolate) {
        // TODO Auto-generated method stub
        return null;
    }


    //
    // THESE METHODS ARE DECLARED FOR THE TIME BEING JUST TO AVOID COMPILATION ERRORS
    //
    // ===================================================================================






    private final YieldTraits<I> baseCurve; // TODO: refactor to YieldCurve ???
    private final CurveTraits    traits;

    // data members
    private RateHelper[] instruments_;
    private /*@Price*/ double accuracy_;

    // common :: SHOULD GO INTO A DEFAULT DELEGATOR WHICH IMPLEMENTS YieldTraits
    protected Date[]                            dates_;
    protected/* @Time */ Array                  times_;
    protected/* @Rate */ Array                  data_;
    protected Interpolation                     interpolation_;
    protected I interpolator_;

    
    
    
    public PiecewiseYieldCurve(
            final Date referenceDate, 
            final RateHelper[] instruments, 
            final DayCounter dayCounter,
            final/* @Price */double accuracy, 
            final I interpolator) {

        final TypeNode root = new TypeTokenTree(this.getClass()).getRoot();
        final Class<?> cparam = root.get(0).getElement();
        final Class<?> iparam = root.get(1).getElement();

        if (cparam==null || iparam==null) throw new IllegalArgumentException("class parameter(s) not specified");
        if (interpolator==null) throw new NullPointerException("interpolation is null"); // TODO: message
        if (!interpolator.getClass().isAssignableFrom(iparam)) 
            throw new ClassCastException("interpolator does not match parameterized type");
        
        if (Discount.class.isAssignableFrom(cparam)) { // TODO: review
            this.baseCurve = new InterpolatedForwardCurve(referenceDate, dayCounter, interpolator);
            this.traits = new Discount(); // TODO: code review: class parameter
    // } else if (ForwardRate.class.isAssignableFrom(cparam)) {
    // this.curve = new
    // } else if (ZeroYield.class.isAssignableFrom(cparam)) {
    // this.curve = new
        } else {
            throw new UnsupportedOperationException("only List<Double> and List<IntervalPrice> are supported");
        }

        this.instruments_ = instruments;
        this.accuracy_ = accuracy;
        checkInstruments();
    }	

    public PiecewiseYieldCurve(
            final /*@Natural*/ int settlementDays, 
            final Calendar calendar,
            final RateHelper[] instruments,
            final DayCounter dayCounter,
            final/* @Price */double accuracy,
            final I interpolator) {

        final TypeNode root = new TypeTokenTree(this.getClass()).getRoot();
        final Class<?> cparam = root.get(0).getElement();
        final Class<?> iparam = root.get(1).getElement();

        if (cparam==null || iparam==null) throw new IllegalArgumentException("class parameter(s) not specified");
        if (interpolator==null) throw new NullPointerException("interpolation is null"); // TODO: message
        if (!interpolator.getClass().isAssignableFrom(iparam)) 
            throw new ClassCastException("interpolator does not match parameterized type");
        
        if (Discount.class.isAssignableFrom(cparam)) { // TODO: review
            this.baseCurve = new InterpolatedForwardCurve(settlementDays, calendar, dayCounter, interpolator);
            this.traits = new Discount(); // TODO: code review: class parameter
    // } else if (ForwardRate.class.isAssignableFrom(cparam)) {
    // this.curve = new
    // } else if (ZeroYield.class.isAssignableFrom(cparam)) {
    // this.curve = new
        } else {
            throw new UnsupportedOperationException("only List<Double> and List<IntervalPrice> are supported");
        }

        this.instruments_ = instruments;
        this.accuracy_ = accuracy;
        checkInstruments();
    }   

    
    
    
//               template <class C, class I>
//               inline void PiecewiseYieldCurve<C,I>::update() {
//                   base_curve::update();
//                   LazyObject::update();
//               }
//

    
    
    
    
    
               private void checkInstruments() {
                   // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
                   Error.QL_REQUIRE(!(instruments_.length==0), "no instrument given");

                   // sort rate helpers
                   for (int i=0; i<instruments_.length; i++)
                       instruments_[i].setTermStructure(this); // TODO: code review

// TODO : compilation error                   
//                   Std.sort(instruments_, new RateHelperSorter());
//                   
                   
                   
                   // check that there is no instruments with the same maturity
                   for (int i=1; i<instruments_.length; i++) {
                       Date m1 = instruments_[i-1].latestDate();
                       Date m2 = instruments_[i].latestDate();
                       Error.QL_REQUIRE(m1 != m2, "two instruments have the same maturity");
                   }
                   for (int i=0; i<instruments_.length; i++)
                       instruments_[i].addObserver(this);
               }

               
               @Override
               public void performCalculations() /* @ReadOnly */ {
                   // check that there is no instruments with invalid quote
                   // TODO: Design by Contract? http://bugs.jquantlib.org/view.php?id=291
                   for (int i=0; i<instruments_.length; i++)
                       Error.QL_REQUIRE(instruments_[i].referenceQuote() != Constants.NULL_Double, "instrument with null price");

                   // setup vectors
                   int n = instruments_.length;
                   for (int i=0; i<n; i++) {
                       // don't try this at home!
                       instruments_[i].setTermStructure(this); // TODO: code review : const_cast<PiecewiseYieldCurve<C,I>*>(this));
                   }
                   this.dates_ = new Date[n+1];
                   this.times_ = new Array(n+1);
                   this.data_  = new Array(n+1);
                   
                   this.dates_[0] = this.referenceDate();
                   this.times_.set(0, 0.0);
                   
                   double prev = traits.initialValue();
                   this.data_.set(0, prev);
                   
                   for (int i=0; i<n; i++) {
                       this.dates_[i+1] = instruments_[i].latestDate();
                       this.times_.set(i+1, this.timeFromReference(this.dates_[i+1]));
                       this.data_.set(i+1, prev);
                   }
                   
                   final Brent solver = new Brent();
                   int maxIterations = 25;
                   // bootstrapping loop
                   for (int iteration = 0; ; iteration++) {
                       final Array previousData = this.data_.clone();
                       for (int i=1; i<n+1; i++) {
                           if (iteration == 0) {
                               // extend interpolation a point at a time
                               if (this.interpolator_.global() && i < 2) {
                                   // not enough points for splines
                                   this.interpolation_ = new Linear().interpolate(this.times_, this.data_);
                               } else {
                                   this.interpolation_ = this.interpolator_.interpolate(this.times_, this.data_);
                               }
                           }
                           this.interpolation_.update();
                           RateHelper instrument = instruments_[i-1];
                           /*@Price*/ double guess;
                           if (iteration > 0) {
                               // use perturbed value from previous loop
                               guess = 0.99*this.data_.get(i);
                           } else if (i > 1) {
                               // extrapolate
                               guess = traits.guess(this, this.dates_[i]);
                           } else {
                               guess = traits.initialGuess();
                           }
                           // bracket
                           /*@Price*/ double min = traits.minValueAfter(i, this.data_);
                           /*@Price*/ double max = traits.maxValueAfter(i, this.data_);
                           if (guess <= min || guess >= max)
                               guess = (min+max)/2.0;
                           try {
                               final ObjectiveFunction<C,I> f = new ObjectiveFunction<C,I>(this, instrument, i);
                               this.data_.set(i, solver.solve(f, accuracy_, guess, min, max));
                           } catch (Exception e) {
                               throw new IllegalArgumentException("could not bootstrap"); // TODO: message
                           }
                       }
                       // check exit conditions
                       if (this.interpolator_.global())
                           break;   // no need for convergence loop

                       double improvement = 0.0;
                       for (int i=1; i<n+1; i++)
                           improvement += Math.abs(this.data_.get(i) - previousData.get(i));
                       if (improvement <= n*accuracy_)  // convergence reached
                           break;

                       if (iteration > maxIterations)
                           throw new IllegalArgumentException("convergence not reached"); // TODO: message
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
            traits.updateGuess(this.curve.data_, guess, this.segment);
            curve.interpolation_.update();
            return rateHelper.quoteError();
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
    // inner classes
    //
	
	private final class InterpolatedForwardCurve extends ForwardRateStructure implements YieldTraits<I> {
	    
	    //
	    // protected fields
	    //

	    protected boolean      isNegativeRates;

	    //
	    // public constructors
	    //
	    
	    public InterpolatedForwardCurve(final DayCounter dayCounter, final I interpolator) {
	        super(dayCounter);
	        interpolator_ = (interpolator!=null) ? interpolator : (I) new BackwardFlat();
	    }

	    public InterpolatedForwardCurve(final Date referenceDate, final DayCounter dayCounter, final I interpolator) {
	        super(referenceDate, Target.getCalendar(), dayCounter); // FIXME: code review:: default calendar
	        interpolator_ = (interpolator!=null) ? interpolator : (I) new BackwardFlat();
	    }

	    public InterpolatedForwardCurve(final int settlementDays, final Calendar calendar, final DayCounter dayCounter, final I interpolator) {
	        super(settlementDays, calendar, dayCounter);
	        interpolator_ = (interpolator!=null) ? interpolator : (I) new BackwardFlat();
	    }

	    
	    //
	    // public constructors
	    //
	    
	    //TODO: who's calling this constructor???
	    public InterpolatedForwardCurve(final Date[] dates, final/* @Rate */Array forwards, final DayCounter dayCounter, final I interpolator) {
	        // FIXME: code review: calendar
	        // FIXME: must check dates
	        super(dates[0], Target.getCalendar(), dayCounter);

	        dates_ = dates.clone();
	        data_ = forwards.clone();
	        this.isNegativeRates = settings.isNegativeRates();
	        interpolator_ = (interpolator!=null) ? interpolator : (I) new BackwardFlat();

	        if (dates.length <= 1) throw new IllegalArgumentException("too few dates"); // FIXME: message
	        if (dates.length != forwards.length) throw new IllegalArgumentException("dates/yields count mismatch"); // FIXME: message

	        times_ = new Array(dates.length);
	        for (int i = 1; i < dates_.length; i++) {
	            if (dates[i].le(dates[i - 1]))
	                throw new IllegalArgumentException("invalid date"); // FIXME: message
	            if (!isNegativeRates && (forwards.get(i) < 0.0)) throw new IllegalArgumentException("negative forward"); // FIXME: message
	            double value = dayCounter.yearFraction(dates[0], dates[i]);
	            times_.set(i, value);
	        }

	        interpolation_ = interpolator_.interpolate(times_, forwards);
	        interpolation_.update();
	    }

	    //
	    // implements TraitsCurve
	    //

        @Override
        public final Date maxDate() /* @ReadOnly */{
            return dates_[dates_.length - 1];
        }

        @Override
        public final Array times() /* @ReadOnly */{
            return times_.clone();
        }

        @Override
        public final Date[] dates() /* @ReadOnly */{
            return dates_.clone();
        }

        @Override
        public final Pair<Date, Double>[] nodes() /* @ReadOnly */{
            Pair<Date, /*@Rate*/Double>[] results = new Pair /* <Date, @Rate Double> */[dates_.length];
            for (int i = 0; i < dates_.length; ++i)
                results[i] = new Pair<Date, Double>(dates_[i], data_.get(i));
            return results;
        }

	    // exclusive to discount curve
	    public /* @DiscountFactor */Array discounts() /* @ReadOnly */ {
            throw new UnsupportedOperationException(); 
        }

	    // exclusive to forward curve
	    public /* @Rate */Array forwards() /* @ReadOnly */ {
	        return data_.clone();
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
            return interpolation_.evaluate(t, true); // FIXME: code review
        }

        @Override
        public /*@Rate*/ double zeroYieldImpl(/*@Time*/double t) /* @ReadOnly */{
            if (t == 0.0) 
                return forwardImpl(0.0);
            else 
                return interpolation_.primitive(t, true) / t; // FIXME: code review
        }

	}
	
}