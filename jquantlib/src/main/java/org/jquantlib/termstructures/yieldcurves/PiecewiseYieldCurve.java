/*
 Copyright (C) 2007 Richard Gomes

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
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

import java.util.List;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.math.Constants;
import org.jquantlib.math.UnaryFunctionDouble;
import org.jquantlib.math.interpolation.Interpolator;
import org.jquantlib.termstructures.Compounding;
import org.jquantlib.termstructures.InterestRate;
import org.jquantlib.termstructures.RateHelper;
import org.jquantlib.termstructures.TermStructureIntf;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Frequency;
import org.jquantlib.util.Date;
import org.jquantlib.util.LazyObject;
import org.jquantlib.util.Observable;
import org.jquantlib.util.Observer;
import org.jquantlib.util.Pair;
import org.jquantlib.util.Visitor;






/**
 * This term structure is bootstrapped on a number of interest rate instruments which are passed as a vector of handles to
 * RateHelper instances. Their maturities mark the boundaries of the interpolated segments.
 * <p>
 * Each segment is determined sequentially starting from the earliest period to the latest and is chosen so that the instrument
 * whose maturity marks the end of such segment is correctly repriced on the curve.
 * 
 * @note The bootstrapping algorithm will raise an exception if any two instruments have the same maturity date.
 */
// TEST the correctness of the returned values is tested by checking them against the original inputs.
// TEST the observability of the term structure is tested.

//TODO: Finish (Richard)

public class PiecewiseYieldCurve<C extends YieldCurveTraits, I extends Interpolator> 
			extends LazyObject 
			implements YieldCurveTraits {

	private YieldCurveTraits	delegate;
	private RateHelper[]		instruments_; //FIXME: generics
	private double				accuracy_;


	/**
	 * This constructor executes these steps:
	 * <li>constructs an {@link Interpolator} from the {@link Class} received as parameter</li>
	 * <li>constructs a {@link YieldTraits} from the {@link Class} received as parameter</li>
	 * <li>passes arguments received by <i>this</i> constructior to {@link YieldTraits}' constructor</li>
	 * <li>passes the {@link Interpolator} just constructed to {@link YieldTraits}' constructor</li>
	 * <p>
	 * Doing so, this constructor mimics dynamic inheritance from a certain {@link YieldTraits} class by delegating to the
	 * dynamically created reference.
	 * 
	 * @param classTraits
	 *            is the {@link Class} relative to a {@link YieldTraits} interface
	 * @param classInterpolator
	 *            is the {@link Class} relative to a {@link Interpolator} to be used by the concrete implementation of a
	 *            {@link YieldTraits} interface
	 * @param referenceDate
	 * @param instruments
	 * @param dayCounter
	 * @param accuracy
	 */
	public PiecewiseYieldCurve(Class<C> classCurveTraits, Class<I> classInterpolator,
			final Date referenceDate, final RateHelper[] instruments, final DayCounter dayCounter,
			final double accuracy) {
		
		try {
			// =====================================================================
			// Constructs a concrete implementation of YieldTraits which 
			// will become ancestor of this class via delegate pattern.
			//
			// In other words: will construct Discount or ForwardRate or ZeroYield
			// passing the arguments received by this constructor, mimicking
			//
			// super(referenceDate, dayCounter, interpolator);
			// =====================================================================
			delegate = classCurveTraits.getConstructor().newInstance(referenceDate, dayCounter, classInterpolator);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
		
		this.instruments_ = instruments;
		this.accuracy_ = accuracy;
		checkInstruments();
	}

	/**
	 * This constructor executes these steps:
	 * <li>constructs an {@link Interpolator} from the {@link Class} received as parameter</li>
	 * <li>constructs a {@link YieldTraits} from the {@link Class} received as parameter</li>
	 * <li>passes arguments received by <i>this</i> constructior to {@link YieldTraits}' constructor</li>
	 * <li>passes the {@link Interpolator} just constructed to {@link YieldTraits}' constructor</li>
	 * <p>
	 * Doing so, this constructor mimics dynamic inheritance from a certain {@link YieldTraits} class by delegating to the
	 * dynamically created reference.
	 * 
	 * @param classTraits
	 *            is the {@link Class} relative to a {@link YieldTraits} interface
	 * @param classInterpolator
	 *            is the {@link Class} relative to a {@link Interpolator} to be used by the concrete implementation of a
	 *            {@link YieldTraits} interface
	 * @param settlementDays
	 * @param calendar
	 * @param instruments
	 * @param dayCounter
	 * @param accuracy
	 */
	public PiecewiseYieldCurve(Class<C> classCurveTraits, Class<I> classInterpolator,
			final int settlementDays, final Calendar calendar, final RateHelper[] instruments,
			final DayCounter dayCounter, final double accuracy) {
		
		try {
			// =====================================================================
			// Constructs a concrete implementation of YieldTraits which 
			// will become ancestor of this class via delegate pattern.
			//
			// In other words: will construct Discount or ForwardRate or ZeroYield
			// passing the arguments received by this constructor, mimicking
			//
			// super(settlementDays, calendar, dayCounter, interpolator);
			// =====================================================================
			delegate = classCurveTraits.getConstructor().newInstance(settlementDays, calendar, dayCounter, classInterpolator);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
		
		this.instruments_ = instruments;
		this.accuracy_ = accuracy;
		checkInstruments();
	}

	/**
	 * This constructor executes these steps:
	 * <li>constructs an {@link Interpolator} from the {@link Class} received as parameter</li>
	 * <li>constructs a {@link YieldTraits} from the {@link Class} received as parameter</li>
	 * <li>passes arguments received by <i>this</i> constructior to {@link YieldTraits}' constructor</li>
	 * <li>passes the {@link Interpolator} just constructed to {@link YieldTraits}' constructor</li>
	 * <p>
	 * Doing so, this constructor mimics dynamic inheritance from a certain {@link YieldTraits} class by delegating to the
	 * dynamically created reference.
	 * 
	 * @param classTraits
	 *            is the {@link Class} relative to a {@link YieldTraits} interface
	 * @param classInterpolator
	 *            is the {@link Class} relative to a {@link Interpolator} to be used by the concrete implementation of a
	 *            {@link YieldTraits} interface
	 * @param referenceDate
	 * @param instruments
	 * @param dayCounter
	 */
	public PiecewiseYieldCurve(Class<C> classCurveTraits, Class<I> classInterpolator,
			final Date referenceDate, final RateHelper[] instruments, final DayCounter dayCounter) {
		this(classCurveTraits, classInterpolator, referenceDate, instruments, dayCounter, 1.0e-12);
	}

	/**
	 * This constructor executes these steps:
	 * <li>constructs an {@link Interpolator} from the {@link Class} received as parameter</li>
	 * <li>constructs a {@link YieldTraits} from the {@link Class} received as parameter</li>
	 * <li>passes arguments received by <i>this</i> constructior to {@link YieldTraits}' constructor</li>
	 * <li>passes the {@link Interpolator} just constructed to {@link YieldTraits}' constructor</li>
	 * <p>
	 * Doing so, this constructor mimics dynamic inheritance from a certain {@link YieldTraits} class by delegating to the
	 * dynamically created reference.
	 * 
	 * @param classTraits
	 *            is the {@link Class} relative to a {@link YieldTraits} interface
	 * @param classInterpolator
	 *            is the {@link Class} relative to a {@link Interpolator} to be used by the concrete implementation of a
	 *            {@link YieldTraits} interface
	 * @param settlementDays
	 * @param calendar
	 * @param instruments
	 * @param dayCounter
	 */
	public PiecewiseYieldCurve(Class<C> classCurveTraits, Class<I> classInterpolator,
			final int settlementDays, 
			final Calendar calendar, 
			final RateHelper[] instruments, 
			final DayCounter dayCounter) {
		this(classCurveTraits, classInterpolator, settlementDays, calendar, instruments, dayCounter, 1.0e-12);
	}


	
	private void checkInstruments() {
	
//		if (instruments_.length==0) throw new IllegalArgumentException("no instrument given"); // FIXME: message
//
//		// sort rate helpers
//	    for (int i=0; i<instruments_.length; i++)
//	        instruments_[i].setTermStructure(this);
//	    Sorting.mergeSort(instruments_, 0, instruments_.length-1, new RateHelperSorter());
//	    // check that there is no instruments with the same maturity
//	    for (int i=1; i<instruments_.length; i++) {
//	        Date m1 = instruments_[i-1].getLatestDate();
//	        Date m2 = instruments_[i].getLatestDate();
//	        if (m1.equals(m2)) throw new IllegalArgumentException("two instruments have the same maturity " + m1); // FIXME: message
//	    }
//	    for (int i=0; i<instruments_.length; i++)
//	        instruments_[i].addObserver(this);
	}

	public void performCalculations() /* @ReadOnly */ {
	
//		// check that there is no instruments with invalid quote
//	    for (int i=0; i<instruments_.length; i++)
//	        if (Double.isNaN(instruments_[i].getQuoteValue())) 
//	        	throw new IllegalArgumentException("instrument with null price"); // FIXME: message
//	                   
//	
//	    // setup vectors
//	    int n = instruments_.length;
//	    for (int i=0; i<n; i++) {
//	        instruments_[i].setTermStructure(this);
//	    }
//	    dates_ = new Date[n+1];
//	    times_ = new /*@Time*/ double[n+1];
//	    data_ = new double[n+1];
//	    dates_[0] = this.getReferenceDate();
//	    times_[0] = 0.0;
//	    data_[0] = initialValue();
//	    for (int i=0; i<n; i++) {
//	        dates_[i+1] = instruments_[i].getLatestDate();
//	        times_[i+1] = this.getTimeFromReference(dates_[i+1]);
//	        data_[i+1]  = this.getData_(i);
//	    }
	    
//    Brent solver;
//    Size maxIterations = 25;
//    // bootstrapping loop
//    for (Size iteration = 0; ; iteration++) {
//        std::vector<Real> previousData = this->data_;
//        for (Size i=1; i<n+1; i++) {
//            if (iteration == 0) {
//                // extend interpolation a point at a time
//                if (I::global && i < 2) {
//                    // not enough points for splines
//                    this->interpolation_ = Linear().interpolate(
//                                                this->times_.begin(),
//                                                this->times_.begin()+i+1,
//                                                this->data_.begin());
//                } else {
//                    this->interpolation_ = this->interpolator_.interpolate(
//                                                this->times_.begin(),
//                                                this->times_.begin()+i+1,
//                                                this->data_.begin());
//                }
//            }
//            this->interpolation_.update();
//            boost::shared_ptr<RateHelper> instrument = instruments_[i-1];
//            Real guess;
//            if (iteration > 0) {
//                // use perturbed value from previous loop
//                guess = 0.99*this->data_[i];
//            } else if (i > 1) {
//                // extrapolate
//                guess = C::guess(this,this->dates_[i]);
//            } else {
//                guess = C::initialGuess();
//            }
//            // bracket
//            Real min = C::minValueAfter(i, this->data_);
//            Real max = C::maxValueAfter(i, this->data_);
//            if (guess <= min || guess >= max)
//                guess = (min+max)/2.0;
//            try {
//                this->data_[i] =
//                    solver.solve(ObjectiveFunction(this, instrument, i),
//                                 accuracy_, guess, min, max);
//            } catch (std::exception& e) {
//                QL_FAIL("could not bootstrap the " << io::ordinal(i) <<
//                        " instrument, maturity " << this->dates_[i] <<
//                        "\n error message: " << e.what());
//            }
//        }
//        // check exit conditions
//        if (!I::global)
//            break;   // no need for convergence loop
//
//        Real improvement = 0.0;
//        for (Size i=1; i<n+1; i++)
//            improvement += std::abs(this->data_[i]-previousData[i]);
//        if (improvement <= n*accuracy_)  // convergence reached
//            break;
//
//        if (iteration > maxIterations)
//            QL_FAIL("convergence not reached after "
//                    << maxIterations << " iterations");
//    }
    
	}

	
	
	
	
	
	
	private /*@DiscountFactor*/ double discountImpl(/* @Time */double t) /* @ReadOnly */{
		calculate();
		return 0.0; // TODO: delegate.discountImpl(t);
	}



	//
	// implements interface Observer
	//

	public void update() {
		// TODO
		//delegate.update();
		//super.update();
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	private class ObjectiveFunction<C extends YieldCurveTraits, I extends Interpolator> implements UnaryFunctionDouble {
		
	    private PiecewiseYieldCurve<C,I> curve;
	    private RateHelper rateHelper;
	    private int segment;

	        
	    public ObjectiveFunction(final PiecewiseYieldCurve<C,I> curve, final RateHelper rateHelper, final int segment) {
	    	this.curve = curve;
	    	this.rateHelper = rateHelper;
	    	this.segment = segment;
		}
		
	    //
	    // implements UnaryFunctionDouble
	    //
		
	    public double evaluate(double guess) /* @ReadOnly */ {
	    	curve.updateGuess(curve.getData(), guess, segment);
	    	// TODO
	    	//curve.interpolation.update();
	    	//return rateHelper.quoteError();
	    	return 0.0; //XXX
	    }
	}
	
	
	
	
	
	
	//PiecewiseYieldCurve<C,I>::ObjectiveFunction::ObjectiveFunction(
	//const PiecewiseYieldCurve<C,I>* curve,
	//const boost::shared_ptr<RateHelper>& rateHelper,
	//Size segment)
	//: curve_(curve), rateHelper_(rateHelper), segment_(segment) {}
	//
	//template <class C, class I>
	//Real PiecewiseYieldCurve<C,I>::ObjectiveFunction::operator()(Real guess)
//	                                       const {
	//C::updateGuess(curve_->data_, guess, segment_);
	//curve_->interpolation_.update();
	//return rateHelper_->quoteError();
	//}
	//

	



	
	

	//
	// inner classes
	//
	
	/**
	 * Discount curve traits
	 * 
	 * <p> 
	 * This class provides a concrete implementation of interface {@link YieldCurveTraits} using
	 * {@link InterpolatedDiscountCurve} as its {@link YieldTermStructure}
	 * 
	 * @author Richard Gomes
	 */
	// FIXME : should not be public
	public final class Discount extends InterpolatedForwardCurve<I> implements YieldCurveTraits {
		
		public Discount(int settlementDays, final Calendar cal, final DayCounter dc, final Class<I> classInterpolator) {
			super(settlementDays, cal, dc, classInterpolator);
		}
		
		public Discount(final Date referenceDate, final DayCounter dc, final Class<I> classInterpolator) {
			super(referenceDate, dc, classInterpolator);
		}

		
		//
		// implements interface YieldCurveTraits
		//
		
        @Override
        public final /* @DiscountFactor */ double initialValue() { return 1.0; }
        
        @Override
        public final /* @DiscountFactor */ double initialGuess() { return 0.9; }
        
        @Override
        public final /* @DiscountFactor */ double guess(final YieldTermStructure c, final Date d) {
            return c.getDiscount(d, true);
        }

        @Override
        public final /* @DiscountFactor */ double minValueAfter(int i, final double[] data) {
            return Constants.QL_EPSILON;
        }
        
        @Override
        public final /* @DiscountFactor */ double maxValueAfter(int i, final double[] data) {
            if (super.isNegativeRates) {
                // discount are not required to be decreasing--all bets are off.
                // We choose as max a value very unlikely to be exceeded.
                return 3.0;
            } else {
                // discounts cannot increaseYieldCurve
                return data[i-1];
            }
        }
        
        @Override
        public final void updateGuess(/* @DiscountFactor */ double[] data, /* @DiscountFactor */ double discount, int i) {
            data[i] = discount;
        }
	
	}
	
	
	// ======================================================================

	
	/**
	 * Zero-curve traits
	 * 
	 * <p> 
	 * This class provides a concrete implementation of interface {@link YieldCurveTraits} using
	 * {@link InterpolatedZeroCurve} as its {@link YieldTermStructure}
	 * 
	 * @author Richard Gomes
	 */
	// FIXME: should not be public
	public final class ZeroYield extends InterpolatedForwardCurve<I> implements YieldCurveTraits {
		
		public ZeroYield(int settlementDays, final Calendar cal, final DayCounter dc, final Class<I> classInterpolator) {
			super(settlementDays, cal, dc, classInterpolator);
		}
		
		public ZeroYield(final Date referenceDate, final DayCounter dc, final Class<I> classInterpolator) {
			super(referenceDate, dc, classInterpolator);
		}
		

		//
		// implements interface YieldCurveTraits
		//
		
        @Override
        public final /* @DiscountFactor */ double initialValue() { return 0.02; }
        
        @Override
        public final /* @DiscountFactor */ double initialGuess() { return 0.02; }
        
        @Override
        public final /* @DiscountFactor */ double guess(final YieldTermStructure c, final Date d) {
            return c.getZeroRate(d, c.getDayCounter(), Compounding.CONTINUOUS, Frequency.ANNUAL, true).doubleValue();
        }

        @Override
        public final /* @DiscountFactor */ double minValueAfter(int i, final double[] data) {
            if (super.isNegativeRates) {
                // no constraints.
                // We choose as min a value very unlikely to be exceeded.
                return -3.0;
            } else {
                return Constants.QL_EPSILON;
            }
        }
        
        @Override
        public final /* @DiscountFactor */ double maxValueAfter(int i, final double[] data) {
            // no constraints.
            // We choose as max a value very unlikely to be exceeded.
            return 3.0;
        }
        
        @Override
        public final void updateGuess(/* @DiscountFactor */ double[] data, /* @Rate */ double rate, int i) {
            data[i] = rate;
            if (i == 1) data[0] = rate; // first point is updated as<C,I> well
        }
        
	}
	

	// ======================================================================
	
	
	/**
	 * Forward-curve traits
	 * 
	 * <p> 
	 * This class provides a concrete implementation of interface {@link YieldCurveTraits} using
	 * {@link InterpolatedForwardCurve} as its {@link YieldTermStructure}
	 * 
	 * @author Richard Gomes
	 */
	// FIXME: should not be public
	public final class ForwardRate extends InterpolatedForwardCurve<I> implements YieldCurveTraits {
		
		public ForwardRate(int settlementDays, final Calendar cal, final DayCounter dc, final Class<I> classInterpolator) {
			super(settlementDays, cal, dc, classInterpolator);
		}
		
		public ForwardRate(final Date referenceDate, final DayCounter dc, final Class<I> classInterpolator) {
			super(referenceDate, dc, classInterpolator);
		}
		

		//
		// implements interface YieldTraits
		//
		
        @Override
		public final /* @DiscountFactor */ double initialValue() { return 0.02; }
        
        @Override
        public final /* @DiscountFactor */ double initialGuess() { return 0.02; }
        
        @Override
        public final /* @DiscountFactor */ double guess(final YieldTermStructure c, final Date d) {
            return c.getForwardRate(d, d, c.getDayCounter(), Compounding.CONTINUOUS, Frequency.ANNUAL, true).doubleValue();
        }

        @Override
        public final /* @DiscountFactor */ double minValueAfter(int i, final double[] data) {
            if (super.isNegativeRates) {
                // no constraints.
                // We choose as min a value very unlikely to be exceeded.
                return -3.0;
            } else {
                return Constants.QL_EPSILON;
            }
        }
        
        @Override
        public final /* @DiscountFactor */ double maxValueAfter(int i, final double[] data) {
            // no constraints.
            // We choose as max a value very unlikely to be exceeded.
            return 3.0;
        }
        
        @Override
        public final void updateGuess(/* @DiscountFactor */ double[] data, /* @Price */ double forward, int i) {
            data[i] = forward;
            if (i == 1)
                data[0] = forward; // first point is updated as well
        }
        
	}


	// ======================================================================
	

	//
	// implements interface YieldCurve
	//
	
	public Date[] getDates() {
		return delegate.getDates();
	}

	public double[] getData() {
		return delegate.getData();
	}

	public Date getMaxDate() {
		return delegate.getMaxDate();
	}

	public Pair<Date, Double>[] getNodes() {
		return delegate.getNodes();
	}

	public double[] getTimes() {
		return delegate.getTimes();
	}
	

	//
	// implements YieldTraits
	//
	
	public double guess(YieldTermStructure c, Date d) {
		return delegate.guess(c, d);
	}

	public double initialGuess() {
		return delegate.initialGuess();
	}

	public double initialValue() {
		return delegate.initialValue();
	}

	public double maxValueAfter(int i, double[] data) {
		return delegate.maxValueAfter(i, data);
	}

	public double minValueAfter(int i, double[] data) {
		return delegate.minValueAfter(i, data);
	}

	public void updateGuess(double[] data, double discount, int i) {
		delegate.updateGuess(data, discount, i);
	}

	
	//
	// implements YieldTermStructureIntf
	//
	
	public double getDiscount(Date d, boolean extrapolate) {
		return delegate.getDiscount(d, extrapolate);
	}

	public double getDiscount(Date d) {
		return delegate.getDiscount(d);
	}

	public double getDiscount(double t, boolean extrapolate) {
		return delegate.getDiscount(t, extrapolate);
	}

	public double getDiscount(double t) {
		return delegate.getDiscount(t);
	}

	public InterestRate getForwardRate(Date d1, Date d2, DayCounter dayCounter, Compounding comp, Frequency freq,
			boolean extrapolate) {
		return delegate.getForwardRate(d1, d2, dayCounter, comp, freq, extrapolate);
	}

	public InterestRate getForwardRate(Date d1, Date d2, DayCounter resultDayCounter, Compounding comp, Frequency freq) {
		return delegate.getForwardRate(d1, d2, resultDayCounter, comp, freq);
	}

	public InterestRate getForwardRate(Date d1, Date d2, DayCounter resultDayCounter, Compounding comp) {
		return delegate.getForwardRate(d1, d2, resultDayCounter, comp);
	}

	public InterestRate getForwardRate(double time1, double time2, Compounding comp, Frequency freq, boolean extrapolate) {
		return delegate.getForwardRate(time1, time2, comp, freq, extrapolate);
	}

	public InterestRate getForwardRate(double t1, double t2, Compounding comp, Frequency freq) {
		return delegate.getForwardRate(t1, t2, comp, freq);
	}

	public InterestRate getForwardRate(double t1, double t2, Compounding comp) {
		return delegate.getForwardRate(t1, t2, comp);
	}

	public InterestRate getZeroRate(Date d, DayCounter dayCounter, Compounding comp, Frequency freq, boolean extrapolate) {
		return delegate.getZeroRate(d, dayCounter, comp, freq, extrapolate);
	}

	public InterestRate getZeroRate(Date d, DayCounter resultDayCounter, Compounding comp, Frequency freq) {
		return delegate.getZeroRate(d, resultDayCounter, comp, freq);
	}

	public InterestRate getZeroRate(Date d, DayCounter resultDayCounter, Compounding comp) {
		return delegate.getZeroRate(d, resultDayCounter, comp);
	}

	
	//
	// implements TermStructureIntf
	//
	
	public Calendar getCalendar() {
		return delegate.getCalendar();
	}

	public DayCounter getDayCounter() {
		return delegate.getDayCounter();
	}

	public double getMaxTime() {
		return delegate.getMaxTime();
	}

	
	public Date getReferenceDate() {
		return delegate.getReferenceDate();
	}

	//
	// implements Observable
	//
	
	public void addObserver(Observer observer) {
		delegate.addObserver(observer);
	}

	public int countObservers() {
		return delegate.countObservers();
	}

	public void deleteObserver(Observer observer) {
		delegate.deleteObserver(observer);
	}

	public void deleteObservers() {
		delegate.deleteObservers();
	}

	public List<Observer> getObservers() {
		return delegate.getObservers();
	}

	public void notifyObservers() {
		delegate.notifyObservers();
	}

	public void notifyObservers(Object arg) {
		delegate.notifyObservers(arg);
	}


	//
	// implements Extrapolator
	//
	
	public boolean allowsExtrapolation() {
		return delegate.allowsExtrapolation();
	}

	public void disableExtrapolation() {
		delegate.disableExtrapolation();
	}

	public void enableExtrapolation() {
		delegate.enableExtrapolation();
	}

	
	//
	// implements Visitor
	//
	
	public void accept(Visitor<TermStructureIntf> v) {
		delegate.accept(v);
	}

	
	//
	// implements Observer
	//
	
	public void update(Observable o, Object arg) {
		delegate.update(o, arg);
	}

}










//
// ============================================================================
//


//#include <ql/quote.hpp>
//#include <ql/termstructures/yieldcurves/discountcurve.hpp>
//#include <ql/termstructures/yieldcurves/bootstraptraits.hpp>
//#include <ql/math/interpolations/linearinterpolation.hpp>
//#include <ql/patterns/lazyobject.hpp>
//#include <ql/math/solvers1d/brent.hpp>
//
//namespace QuantLib {
//
//    //! Base helper class for yield-curve bootstrapping
//    /*! This class provides an abstraction for the instruments used to
//        bootstrap a term structure.
//        It is advised that a rate helper for an instrument contains an
//        instance of the actual instrument class to ensure consistancy
//        between the algorithms used during bootstrapping and later
//        instrument pricing. This is not yet fully enforced in the
//        available rate helpers, though - only SwapRateHelper and
//        FixedCouponBondHelper contain their corresponding instrument
//        for the time being.
//    */
//    class RateHelper : public Observer, public Observable {
//      public:
//        RateHelper(const Handle<Quote>& quote);
//        RateHelper(Real quote);
//        virtual ~RateHelper() {}
//        //! \name RateHelper interface
//        //@{
//        Real quoteError() const;
//        Real referenceQuote() const { return quote_->value(); }
//        virtual Real impliedQuote() const = 0;
//        virtual DiscountFactor discountGuess() const {
//            return Null<Real>();
//        }
//        //! sets the term structure to be used for pricing
//        /*! \warning Being a pointer and not a shared_ptr, the term
//                     structure is not guaranteed to remain allocated
//                     for the whole life of the rate helper. It is
//                     responsibility of the programmer to ensure that
//                     the pointer remains valid. It is advised that
//                     rate helpers be used only in term structure
//                     constructors, setting the term structure to
//                     <b>this</b>, i.e., the one being constructed.
//        */
//        virtual void setTermStructure(YieldTermStructure*);
//        //! earliest relevant date
//        /*! The earliest date at which discounts are needed by the
//            helper in order to provide a quote.
//        */
//        virtual Date earliestDate() const { return earliestDate_;}
//        //! latest relevant date
//        /*! The latest date at which discounts are needed by the
//            helper in order to provide a quote. It does not
//            necessarily equal the maturity of the underlying
//            instrument.
//        */
//        virtual Date latestDate() const { return latestDate_;}
//        //@}, Yield
//        //! \name Observer interface
//        //@{
//        virtual void update() { notifyObservers(); }
//        //@}
//      protected:
//        Handle<Quote> quote_;
//        YieldTermStructure* termStructure_;
//        Date earliestDate_, latestDate_;
//    };
//
//    // helper class
//    namespace detail {
//
//        class RateHelperSorter {
//          public:
//            bool operator()(const boost::shared_ptr<RateHelper>& h1,
//                            const boost::shared_ptr<RateHelper>& h2) const {
//                return (h1->latestDate() < h2->latestDate());
//            }
//        };
//
//    }
//
//    //! Piecewise yield term structure
//    /*! This term structure is bootstrapped on a number of interest
//        rate instruments which are passed as a vector of handles to
//        RateHelper instances. Their maturities mark the boundaries of
//        the interpolated segments.
//
//        Each segment is determined sequentially starting from the
//        earliest period to the latest and is chosen so that the
//        instrument whose maturity marks the end of such segment is
//        correctly repriced on the curve.
//
//        \warning The bootstrapping algorithm will raise an exception if
//                 any two instruments have the same maturity date.
//
//        \ingroup yieldtermstructures
//
//        \test
//        - the correctness of the returned values is tested by
//          checking them against the original inputs.
//        - the observability of the term structure is tested.
//    */
//    template <class Traits, class Interpolator>
//    class PiecewiseYieldCurve
//        : public Traits::template curve<Interpolator>::type,
//          public LazyObject {
//      private:
//        typedef typename Traits::template curve<Interpolator>::type base_curve;
//      public:
//        //! \name Constructors
//        //@{
//        PiecewiseYieldCurve(
//               const Date& referenceDate,
//               const std::vector<boost::shared_ptr<RateHelper> >& instruments,
//               const DayCounter& dayCounter,
//               Real accuracy = 1.0e-12,
//               const Interpolator& i = Interpolator());
//        PiecewiseYieldCurve(
//               Natural settlementDays,
//               const Calendar& calendar,
//               const std::vector<boost::shared_ptr<RateHelper> >& instruments,
//               const DayCounter& dayCounter,
//               Real accuracy = 1.0e-12,
//               const Interpolator& i = Interpolator());
//        //@}
//        //! \name YieldTermStructure interface
//        //@{
//        const std::vector<Date>& dates() const;
//        Date maxDate() const;
//        const std::vector<Time>& times() const;
//        //@}
//        //! \name Inspectors
//        //@{
//        std::vector<std::pair<Date,Real> > nodes() const;
//        //@}
//        //! \name Observer interface
//        //@{
//        void update();
//        //@}
//      private:
//        // helper classes for bootstrapping
//        class ObjectiveFunction;
//        friend class ObjectiveFunction;
//        // methods
//        void checkInstruments();
//        void performCalculations() const;
//        DiscountFactor discountImpl(Time) const;
//        // data members
//        std::vector<boost::shared_ptr<RateHelper> > instruments_;
//        Real accuracy_;
//    };
//
//
//    // objective function for solver
//
//
//    // inline definitions
//
//    template <class C, class I>
//    inline const std::vector<Date>& PiecewiseYieldCurve<C,I>::dates() const {
//        calculate();
//        return this->dates_;
//    }
//
//    template <class C, class I>
//    inline Date PiecewiseYieldCurve<C,I>::maxDate() const {
//        calculate();
//        return this->dates_.back();
//    }
//
//    template <class C, class I>
//    inline const std::vector<Time>& PiecewiseYieldCurve<C,I>::times() const {
//        calculate();
//        return this->times_;
//    }
//
//    template <class C, class I>
//    inline std::vector<std::pair<Date,Real> >
//    PiecewiseYieldCurve<C,I>::nodes() const {
//        calculate();
//        return base_curve::nodes();
//    }
//
//    template <class C, class I>
//    inline void PiecewiseYieldCurve<C,I>::update() {
//        base_curve::update();
//        LazyObject::update();
//    }
//
//    template <class C, class I>
//    inline DiscountFactor PiecewiseYieldCurve<C,I>::discountImpl(Time t)
//                                                                       const {
//        calculate();
//        return base_curve::discountImpl(t);
//    }
//
//
//    // template definitions
//
//    template <class C, class I>
//    PiecewiseYieldCurve<C,I>::PiecewiseYieldCurve(
//               const Date& referenceDate,
//               const std::vector<boost::shared_ptr<RateHelper> >& instruments,
//               const DayCounter& dayCounter, Real accuracy,
//               const I& interpolator)
//    : base_curve(referenceDate, dayCounter, interpolator),
//      instruments_(instruments), accuracy_(accuracy) {
//        checkInstruments();
//    }
//
//    template <class C, class I>
//    PiecewiseYieldCurve<C,I>::PiecewiseYieldCurve(
//               Natural settlementDays,
//               const Calendar& calendar,
//               const std::vector<boost::shared_ptr<RateHelper> >& instruments,
//               const DayCounter& dayCounter, Real accuracy,
//               const I& interpolator)
//    : base_curve(settlementDays, calendar, dayCounter, interpolator),
//      instruments_(instruments), accuracy_(accuracy) {
//        checkInstruments();
//    }
//
//    template <class C, class I>
//    void PiecewiseYieldCurve<C,I>::checkInstruments() {
//
//        QL_REQUIRE(!instruments_.empty(), "no instrument given");
//
//        // sort rate helpers
//        for (Size i=0; i<instruments_.size(); i++)
//            instruments_[i]->setTermStructure(this);
//        std::sort(instruments_.begin(),instruments_.end(),
//                  detail::RateHelperSorter());
//        // check that there is no instruments with the same maturity
//        for (Size i=1; i<instruments_.size(); i++) {
//            Date m1 = instruments_[i-1]->latestDate(),
//                 m2 = instruments_[i]->latestDate();
//            QL_REQUIRE(m1 != m2,
//                       "two instruments have the same maturity ("<< m1 <<")");
//        }
//        for (Size i=0; i<instruments_.size(); i++)
//            registerWith(instruments_[i]);
//    }
//
//    template <class C, class I>
//    void PiecewiseYieldCurve<C,I>::performCalculations() const
//    {
//        // check that there is no instruments with invalid quote
//        for (Size i=0; i<instruments_.size(); i++)
//            QL_REQUIRE(instruments_[i]->referenceQuote()!=Null<Real>(),
//                       "instrument with null price");
//
//        // setup vectors
//        Size n = instruments_.size();
//        for (Size i=0; i<n; i++) {
//            // don't try this at home!
//            instruments_[i]->setTermStructure(
//                                 const_cast<PiecewiseYieldCurve<C,I>*>(this));
//        }
//        this->dates_ = std::vector<Date>(n+1);
//        this->times_ = std::vector<Time>(n+1);
//        this->data_ = std::vector<Real>(n+1);
//        this->dates_[0] = this->referenceDate();
//        this->times_[0] = 0.0;
//        this->data_[0] = C::initialValue();
//        for (Size i=0; i<n; i++) {
//            this->dates_[i+1] = instruments_[i]->latestDate();
//            this->times_[i+1] = this->timeFromReference(this->dates_[i+1]);
//            this->data_[i+1] = this->data_[i];
//        }
//        Brent solver;
//        Size maxIterations = 25;
//        // bootstrapping loop
//        for (Size iteration = 0; ; iteration++) {
//            std::vector<Real> previousData = this->data_;
//            for (Size i=1; i<n+1; i++) {
//                if (iteration == 0) {
//                    // extend interpolation a point at a time
//                    if (I::global && i < 2) {
//                        // not enough points for splines
//                        this->interpolation_ = Linear().interpolate(
//                                                    this->times_.begin(),
//                                                    this->times_.begin()+i+1,
//                                                    this->data_.begin());
//                    } else {
//                        this->interpolation_ = this->interpolator_.interpolate(
//                                                    this->times_.begin(),
//                                                    this->times_.begin()+i+1,
//                                                    this->data_.begin());
//                    }
//                }
//                this->interpolation_.update();
//                boost::shared_ptr<RateHelper> instrument = instruments_[i-1];
//                Real guess;
//                if (iteration > 0) {
//                    // use perturbed value from previous loop
//                    guess = 0.99*this->data_[i];
//                } else if (i > 1) {
//                    // extrapolate
//                    guess = C::guess(this,this->dates_[i]);
//                } else {
//                    guess = C::initialGuess();
//                }
//                // bracket
//                Real min = C::minValueAfter(i, this->data_);
//                Real max = C::maxValueAfter(i, this->data_);
//                if (guess <= min || guess >= max)
//                    guess = (min+max)/2.0;
//                try {
//                    this->data_[i] =
//                        solver.solve(ObjectiveFunction(this, instrument, i),
//                                     accuracy_, guess, min, max);
//                } catch (std::exception& e) {
//                    QL_FAIL("could not bootstrap the " << io::ordinal(i) <<
//                            " instrument, maturity " << this->dates_[i] <<
//                            "\n error message: " << e.what());
//                }
//            }
//            // check exit conditions
//            if (!I::global)
//                break;   // no need for convergence loop
//
//            Real improvement = 0.0;
//            for (Size i=1; i<n+1; i++)
//                improvement += std::abs(this->data_[i]-previousData[i]);
//            if (improvement <= n*accuracy_)  // convergence reached
//                break;
//
//            if (iteration > maxIterations)
//                QL_FAIL("convergence not reached after "
//                        << maxIterations << " iterations");
//        }
//    }
//
//    #ifndef __DOXYGEN__
//
//    template <class C, class I>
//    PiecewiseYieldCurve<C,I>::ObjectiveFunction::ObjectiveFunction(
//                              const PiecewiseYieldCurve<C,I>* curve,
//                              const boost::shared_ptr<RateHelper>& rateHelper,
//                              Size segment)
//    : curve_(curve), rateHelper_(rateHelper), segment_(segment) {}
//
//    template <class C, class I>
//    Real PiecewiseYieldCurve<C,I>::ObjectiveFunction::operator()(Real guess)
//                                                                       const {
//        C::updateGuess(curve_->data_, guess, segment_);
//        curve_->interpolation_.update();
//        return rateHelper_->quoteError();
//    }
//
//    #endif
//
//}
//
//
//#endif




