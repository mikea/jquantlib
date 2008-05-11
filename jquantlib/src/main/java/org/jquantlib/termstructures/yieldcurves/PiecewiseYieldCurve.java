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

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.math.Constants;
import org.jquantlib.math.interpolation.Interpolator;
import org.jquantlib.math.interpolation.factories.Linear;
import org.jquantlib.termstructures.Compounding;
import org.jquantlib.termstructures.RateHelper;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Frequency;
import org.jquantlib.util.Date;
import org.jquantlib.util.LazyObject;
import org.jquantlib.util.Pair;




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

public class PiecewiseYieldCurve<C extends YieldTraits, I extends Interpolator> extends LazyObject implements YieldCurveTraits {

	private YieldCurve			delegateCurve;
	private YieldTraits			delegateTraits;
	private RateHelper[]		instruments_; //FIXME: generics
	private double				accuracy_;


	/**
	 * This constructor executes these steps:
	 * <li>constructs an {@link Interpolator} from the {@link Class} received as parameter</li>
	 * <li>constructs a {@link YieldTraits} from the {@link Class} received as parameter</li>
	 * <li>passes arguments received by <i>this</i> constructior to {@link YieldTraits}' constructor</li>
	 * <li>passes the {@link Interpolator} just constructed to {@link YieldTraits}' constructor</li>
	 * <p>
	 * Doing so, this constructor mimics dinamic inheritance from a certain {@link YieldTraits} class by delegating to the
	 * dinamically created reference.
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
	public PiecewiseYieldCurve(Class<C> classTraits, Class<I> classInterpolator,
			final Date referenceDate, final RateHelper[] instruments, final DayCounter dayCounter,
			final double accuracy) {
		
		try {
			// Constructs the Interpolator
			if (classInterpolator==null) {
				classInterpolator = (Class<I>) Linear.class; // FIXME: code review :: This is arbitrary, I hadn't better to invent here.
			}
			Interpolator interpolator = (Interpolator) classInterpolator.getConstructor().newInstance();
			
			// =====================================================================
			// Constructs a concrete implementation of YieldTraits which 
			// will become ancestor of this class via delegate pattern.
			//
			// In other words: will construct Discount or ForwardRate or ZeroYield
			// passing the arguments received by this constructor, mimicking
			//
			// super(referenceDate, dayCounter, interpolator);
			// =====================================================================
			delegateTraits = classTraits.getConstructor().newInstance(referenceDate, dayCounter, interpolator);
			delegateCurve = (YieldCurve) delegateTraits; // TODO: does not look to be very good !!!
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
	 * Doing so, this constructor mimics dinamic inheritance from a certain {@link YieldTraits} class by delegating to the
	 * dinamically created reference.
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
	public PiecewiseYieldCurve(Class<C> classTraits, Class<I> classInterpolator,
			final int settlementDays, final Calendar calendar, final RateHelper[] instruments,
			final DayCounter dayCounter, final double accuracy) {
		
		try {
			// Constructs the Interpolator
			if (classInterpolator==null) {
				classInterpolator = (Class<I>) Linear.class; // FIXME: code review :: This is arbitrary, I hadn't better to invent here.
			}
			Interpolator interpolator = (Interpolator) classInterpolator.getConstructor().newInstance();
			
			// =====================================================================
			// Constructs a concrete implementation of YieldTraits which 
			// will become ancestor of this class via delegate pattern.
			//
			// In other words: will construct Discount or ForwardRate or ZeroYield
			// passing the arguments received by this constructor, mimicking
			//
			// super(settlementDays, calendar, dayCounter, interpolator);
			// =====================================================================
			delegateTraits = classTraits.getConstructor().newInstance(settlementDays, calendar, dayCounter, interpolator);
			delegateCurve = (YieldCurve) delegateTraits; // TODO: does not look to be very good !!!
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
	 * Doing so, this constructor mimics dinamic inheritance from a certain {@link YieldTraits} class by delegating to the
	 * dinamically created reference.
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
	public PiecewiseYieldCurve(Class<C> classTraits, Class<I> classInterpolator,
			final Date referenceDate, final RateHelper[] instruments, final DayCounter dayCounter) {
		this(classTraits, classInterpolator, referenceDate, instruments, dayCounter, 1.0e-12);
	}

	/**
	 * This constructor executes these steps:
	 * <li>constructs an {@link Interpolator} from the {@link Class} received as parameter</li>
	 * <li>constructs a {@link YieldTraits} from the {@link Class} received as parameter</li>
	 * <li>passes arguments received by <i>this</i> constructior to {@link YieldTraits}' constructor</li>
	 * <li>passes the {@link Interpolator} just constructed to {@link YieldTraits}' constructor</li>
	 * <p>
	 * Doing so, this constructor mimics dinamic inheritance from a certain {@link YieldTraits} class by delegating to the
	 * dinamically created reference.
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
	public PiecewiseYieldCurve(Class<C> classTraits, Class<I> classInterpolator,
			final int settlementDays, 
			final Calendar calendar, 
			final RateHelper[] instruments, 
			final DayCounter dayCounter) {
		this(classTraits, classInterpolator, settlementDays, calendar, instruments, dayCounter, 1.0e-12);
	}


	
	private void checkInstruments() {
	
// QL_REQUIRE(!instruments_.empty(), "no instrument given");
//
//    // sort rate helpers
//    for (Size i=0; i<instruments_.size(); i++)
//        instruments_[i]->setTermStructure(this);
//    std::sort(instruments_.begin(),instruments_.end(),
//              detail::RateHelperSorter());
//    // check that there is no instruments with the same maturity
//    for (Size i=1; i<instruments_.size(); i++) {
//        Date m1 = instruments_[i-1]->latestDate(),
//             m2 = instruments_[i]->latestDate();
//        QL_REQUIRE(m1 != m2,
//                   "two instruments have the same maturity ("<< m1 <<")");
//    }
//    for (Size i=0; i<instruments_.size(); i++)
//        registerWith(instruments_[i]);
    
	}

	public void performCalculations() /* @ReadOnly */ {
	
//	// check that there is no instruments with invalid quote
//    for (Size i=0; i<instruments_.size(); i++)
//        QL_REQUIRE(instruments_[i]->referenceQuote()!=Null<Real>(),
//                   "instrument with null price");
//
//    // setup vectors
//    Size n = instruments_.size();
//    for (Size i=0; i<n; i++) {
//        // don't try this at home!
//        instruments_[i]->setTermStructure(
//                             const_cast<PiecewiseYieldCurve<C,I>*>(this));
//    }
//    this->dates_ = std::vector<Date>(n+1);
//    this->times_ = std::vector<Time>(n+1);
//    this->data_ = std::vector<Real>(n+1);
//    this->dates_[0] = this->referenceDate();
//    this->times_[0] = 0.0;
//    this->data_[0] = C::initialValue();
//    for (Size i=0; i<n; i++) {
//        this->dates_[i+1] = instruments_[i]->latestDate();
//        this->times_[i+1] = this->timeFromReference(this->dates_[i+1]);
//        this->data_[i+1] = this->data_[i];
//    }
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

	
	
	
//	
//	
//	
//	private /*@DiscountFactor*/ double discountImpl(/* @Time */double t) /* @ReadOnly */{
//		calculate();
//		return base_curve.discountImpl(t);
//	}
//
//
//
//	//
//	// implements interface YieldTermStructure
//	//
//
//	public final Date[] getDates() /* @ReadOnly */{
//		calculate();
//		return this.dates;
//	}
//
//	public Date getMaxDate() /* @ReadOnly */{
//		calculate();
//		return this.dates[this.dates.length() - 1];
//	}
//
//	public Time[] getTimes() /* @ReadOnly */ {
//		calculate();
//		return this.times;
//	}
//
//	public Pair<Date, Double> getNodes() /* @ReadOnly */{
//		calculate();
//		return base_curve.getNodes();
//	}
//
//
//
//	//
//	// implements interface Observer
//	//
//
//	public void update() {
//		base_curve.update();
//		super.update();
//	}
//
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	private class ObjectiveFunction<C extends BootstrapTrait, I extends Interpolator> implements UnaryFunctionDouble {
//		
//	    private PiecewiseYieldCurve<C,I> curve;
//	    private RateHelper rateHelper;
//	    private int segment;
//
//	        
//	    public ObjectiveFunction(final PiecewiseYieldCurve<C,I> curve, final RateHelper rateHelper, final int segment) {
//	    	this.curve = curve;
//	    	this.rateHelper = rateHelper;
//	    	this.segment = segment;
//		}
//		
//	    //
//	    // implements UnaryFunctionDouble
//	    //
//		
//	    public double evaluate(double guess) /* @ReadOnly */ {
//	    	C::updateGuess(curve_->data_, guess, segment_);
//	    	curve.interpolation.update();
//	    	return rateHelper_->quoteError();
//	    	
//	    }
//	}
//	
//	
//	
	
	
	
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
	private final class Discount extends InterpolatedForwardCurve<I> implements YieldCurveTraits {
		
		public Discount(int settlementDays, final Calendar cal, final DayCounter dc, final I interpolator) {
			super(settlementDays, cal, dc, interpolator);
		}
		
		public Discount(final Date referenceDate, final DayCounter dc, final I interpolator) {
			super(referenceDate, dc, interpolator);
		}

		
		//
		// implements interface YieldCurveTraits
		//
		
		// FIXME: Override??? from who????
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
	
    	//
    	// implements interface YieldCurveTraits
    	//
    	
    	@Override
    	public YieldCurveTraits getCurve() {
    		// TODO is this method really necessary in interface definition???
    		return this;
    	}

	}
	
	
	/**
	 * Zero-curve traits
	 * 
	 * <p> 
	 * This class provides a concrete implementation of interface {@link YieldCurveTraits} using
	 * {@link InterpolatedZeroCurve} as its {@link YieldTermStructure}
	 * 
	 * @author Richard Gomes
	 */
	private final class ZeroYield extends InterpolatedForwardCurve<I> implements YieldCurveTraits {
		
		public ZeroYield(int settlementDays, final Calendar cal, final DayCounter dc, final I interpolator) {
			super(settlementDays, cal, dc, interpolator);
		}
		
		public ZeroYield(final Date referenceDate, final DayCounter dc, final I interpolator) {
			super(referenceDate, dc, interpolator);
		}
		

		//
		// implements interface YieldCurveTraits
		//
		
		// FIXME: Override??? from who????
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
        
    	//
    	// implements interface YieldCurveTraits
    	//
    	
    	@Override
    	public YieldCurveTraits getCurve() {
    		// TODO is this method really necessary in interface definition???
    		return this;
    	}

	}
	

	/**
	 * Forward-curve traits
	 * 
	 * <p> 
	 * This class provides a concrete implementation of interface {@link YieldCurveTraits} using
	 * {@link InterpolatedForwardCurve} as its {@link YieldTermStructure}
	 * 
	 * @author Richard Gomes
	 */
	private final class ForwardRate extends InterpolatedForwardCurve<I> implements YieldCurveTraits {
		
		public ForwardRate(int settlementDays, final Calendar cal, final DayCounter dc, final I interpolator) {
			super(settlementDays, cal, dc, interpolator);
		}
		
		public ForwardRate(final Date referenceDate, final DayCounter dc, final I interpolator) {
			super(referenceDate, dc, interpolator);
		}
		

		//
		// implements interface YieldTraits
		//
		
		// FIXME: Override??? from who????
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
        
    	//
    	// implements interface YieldCurveTraits
    	//
    	
    	@Override
    	public YieldCurveTraits getCurve() {
    		// TODO is this method really necessary in interface definition???
    		return this;
    	}

	}



	//
	// implements interface YieldCurve
	//
	
	public Date[] dates() {
		return delegateCurve.dates();
	}

	public double[] discounts() {
		return delegateCurve.discounts();
	}

	public Date maxDate() {
		return delegateCurve.maxDate();
	}

	public Pair<Date, Double>[] nodes() {
		return delegateCurve.nodes();
	}

	public double[] times() {
		return delegateCurve.times();
	}
	

	//
	// implements YieldTraits
	//
	
	public double guess(YieldTermStructure c, Date d) {
		return delegateTraits.guess(c, d);
	}

	public double initialGuess() {
		return delegateTraits.initialGuess();
	}

	public double initialValue() {
		return delegateTraits.initialValue();
	}

	public double maxValueAfter(int i, double[] data) {
		return delegateTraits.maxValueAfter(i, data);
	}

	public double minValueAfter(int i, double[] data) {
		return delegateTraits.minValueAfter(i, data);
	}

	public void updateGuess(double[] data, double discount, int i) {
		delegateTraits.updateGuess(data, discount, i);
	}

	
	//
	// implements interface YieldCurveTraits
	//
	
	@Override
	public YieldCurveTraits getCurve() {
		// TODO is this method really necessary in interface definition???
		return this;
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




