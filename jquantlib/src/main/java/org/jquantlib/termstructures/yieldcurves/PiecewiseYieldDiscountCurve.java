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

package org.jquantlib.termstructures.yieldcurves;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.math.Constants;
import org.jquantlib.math.UnaryFunctionDouble;
import org.jquantlib.math.interpolation.Interpolator;
import org.jquantlib.math.interpolation.factories.Linear;
import org.jquantlib.math.solvers1D.Brent;
import org.jquantlib.termstructures.RateHelper;
import org.jquantlib.termstructures.RateHelperSorter;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Calendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.LazyObject;
import org.jquantlib.util.Observable;
import org.jquantlib.util.Pair;

import cern.colt.Sorting;

public class PiecewiseYieldDiscountCurve<T extends Interpolator> extends InterpolatedDiscountCurve<T> {

	private static final double ACCURACY = 1.0e-12;
	

	// TODO: analyse if fields should be kept in a separate object
	private CurveData	curveData;
	private class CurveData {
		public RateHelper<YieldTermStructure>[]	instruments;
		public double							accuracy;
	}

	
	//
	// public constructors
	//

	public PiecewiseYieldDiscountCurve(final Date referenceDate, final RateHelper<YieldTermStructure>[] instruments,
			final DayCounter dayCounter, final double accuracy) {
		this(referenceDate, instruments, dayCounter, accuracy, null);
	}

	public PiecewiseYieldDiscountCurve(final Date referenceDate, final RateHelper<YieldTermStructure>[] instruments,
			final DayCounter dayCounter, final T interpolator) {
		this(referenceDate, instruments, dayCounter, ACCURACY, interpolator);
	}

	public PiecewiseYieldDiscountCurve(final Date referenceDate, final RateHelper<YieldTermStructure>[] instruments,
			final DayCounter dayCounter) {
		this(referenceDate, instruments, dayCounter, ACCURACY, null);
	}

	public PiecewiseYieldDiscountCurve(final Date referenceDate, final RateHelper<YieldTermStructure>[] instruments,
			final DayCounter dayCounter, final double accuracy, final T interpolator) {
		super(referenceDate, dayCounter, interpolator);
		this.curveData = new CurveData();
		this.curveData.instruments = instruments;
		this.curveData.accuracy = accuracy;
		this.traits = new Discount();
		this.calculator = new PiecewiseYieldLazyCurve(this, this, traits, curveData);
		this.calculator.checkInstruments();
	}

	// ---
	
	public PiecewiseYieldDiscountCurve(final int settlementDays, final Calendar calendar,
			final RateHelper<YieldTermStructure>[] instruments, final DayCounter dayCounter, final double accuracy) {
		this(settlementDays, calendar, instruments, dayCounter, accuracy, null);
	}

	public PiecewiseYieldDiscountCurve(final int settlementDays, final Calendar calendar,
			final RateHelper<YieldTermStructure>[] instruments, final DayCounter dayCounter, final T interpolator) {
		this(settlementDays, calendar, instruments, dayCounter, ACCURACY, interpolator);
	}

	public PiecewiseYieldDiscountCurve(final int settlementDays, final Calendar calendar,
			final RateHelper<YieldTermStructure>[] instruments, final DayCounter dayCounter) {
		this(settlementDays, calendar, instruments, dayCounter, ACCURACY, null);
	}

	public PiecewiseYieldDiscountCurve(final int settlementDays, final Calendar calendar,
			final RateHelper<YieldTermStructure>[] instruments, final DayCounter dayCounter, final double accuracy,
			final T interpolator) {
		super(settlementDays, calendar, dayCounter, interpolator);
		curveData.instruments = instruments;
		curveData.accuracy = accuracy;
		calculator.checkInstruments();
	}

	
	
	//
	// override methods from class TermStrucuture
	//

	@Override
	protected double discountImpl(double t) {
		calculator.calculate();
		return super.discountImpl(t);
	}

	
	//
	// override methods from interface YieldCurve
	//

	@Override
	public final Date[] getDates() /* @ReadOnly */{
		calculator.calculate();
    	return dates.clone();
	}

	@Override
	public final/* @DiscountFactor */double[] getData() /* @ReadOnly */{
		calculator.calculate();
		return super.getData();
	}

	@Override
	public final Date maxDate() /* @ReadOnly */{
		calculator.calculate();
		return super.maxDate();
	}

	@Override
	public final Pair<Date, Double>[] getNodes() /* @ReadOnly */{
		calculator.calculate();
		return super.getNodes();
	}

	@Override
	public final double[] getTimes() /* @ReadOnly */{
		calculator.calculate();
		return super.getTimes();
	}

	
	//
	// override methods from Observer
	//

	@Override
	public void update(Observable o, Object arg) {
		super.update(o, arg);
		calculator.update(o, arg);
	}

	
	//
	// inner classes
	//

	/**
	 * Composition pattern to an YieldTraits
	 * 
	 * @see YieldTraits
	 */
	private YieldTraits	traits;

	private class Discount implements YieldTraits {

		@Override
		public final/* @DiscountFactor */double initialValue() {
			return 1.0;
		}

		@Override
		public final/* @DiscountFactor */double initialGuess() {
			return 0.9;
		}

		@Override
		public final/* @DiscountFactor */double guess(final YieldTermStructure c, final Date d) {
			return c.discount(d, true);
		}

		@Override
		public final/* @DiscountFactor */double minValueAfter(int i, final double[] data) {
			return Constants.QL_EPSILON;
		}

		@Override
		public final/* @DiscountFactor */double maxValueAfter(int i, final double[] data) {
			if (isNegativeRates) {
				// discount are not required to be decreasing--all bets are off.
				// We choose as max a value very unlikely to be exceeded.
				return 3.0;
			} else {
				// discounts cannot increaseYieldCurve
				return data[i - 1];
			}
		}

		@Override
		public final void updateGuess(/* @DiscountFactor */double[] data, /* @DiscountFactor */double discount, int i) {
			data[i] = discount;
		}

	}

	/**
	 * Composition pattern to a LazyObject
	 * 
	 * @see LazyObject
	 */
	private PiecewiseYieldLazyCurve	calculator;

	private class PiecewiseYieldLazyCurve extends LazyObject {

		private YieldTermStructure	yts;
		private YieldCurve			curve;
		private YieldTraits			traits;
		private CurveData			curveData;

		public PiecewiseYieldLazyCurve(final YieldTermStructure yts, final YieldCurve curve, final YieldTraits traits, final CurveData curveData) {
			this.yts = yts;
			this.curve = curve;
			this.traits = traits;
			this.curveData = curveData;
		}

		@Override
		protected void calculate() {
			super.calculate();
		}

		@Override
		protected void performCalculations() throws ArithmeticException {
			// check that there is no instruments with invalid quote
			for (int i = 0; i < curveData.instruments.length; i++)
				if (Double.isNaN(curveData.instruments[i].getQuoteValue()))
					throw new IllegalArgumentException("instrument with null price"); // FIXME: message

			// setup vectors
			int n = curveData.instruments.length;
			for (int i = 0; i < n; i++) {
				curveData.instruments[i].setTermStructure(yts);
			}

			dates[0] = referenceDate();
			times[0] = 0.0;
			data[0] = traits.initialValue();
			for (int i = 0; i < n; i++) {
				dates[i + 1] = curveData.instruments[i].getLatestDate();
				times[i + 1] = timeFromReference(dates[i + 1]);
				data[i + 1] = data[i];
			}

			Brent solver = new Brent();
			int maxIterations = 25;
			// bootstrapping loop
			for (int iteration = 0;; iteration++) {
				double[] previousData = curve.getData(); // FIXME: could be: getData() ????
				for (int i = 1; i < n + 1; i++) {
					if (iteration == 0) {
						// extend interpolation a point at a time
						if (interpolator.isGlobal() && i < 2) {
							// not enough points for splines
							interpolation = new Linear().interpolate(i + 1, times, data);
						} else {
							interpolation = interpolator.interpolate(i + 1, times, data);
						}
					}
					interpolation.update();
					RateHelper<? extends YieldTermStructure> instrument = curveData.instruments[i - 1];
					double guess;
					if (iteration > 0) {
						// use perturbed value from previous loop
						guess = 0.99 * data[i];
					} else if (i > 1) {
						// extrapolate
						guess = traits.guess(yts, dates[i]);
					} else {
						guess = traits.initialGuess();
					}
					// bracket
					double min = traits.minValueAfter(i, data);
					double max = traits.maxValueAfter(i, data);
					if (guess <= min || guess >= max) guess = (min + max) / 2.0;
					try {
						data[i] = solver.solve(new ObjectiveFunction(instrument, i), curveData.accuracy, guess, min, max);
					} catch (Exception e) {
						throw new IllegalStateException("could not bootstrap the " + i + "th instrument, maturity " + dates[i], e);
					}
				}
				// check exit conditions
				if (!interpolator.isGlobal()) break; // no need for convergence loop

				double improvement = 0.0;
				for (int i = 1; i < n + 1; i++)
					improvement += Math.abs(data[i] - previousData[i]);
				if (improvement <= n * curveData.accuracy) // convergence reached
					break;

				if (iteration > maxIterations)
					throw new IllegalStateException("convergence not reached after " + maxIterations + " iterations");
			}
		}

		private void checkInstruments() {
			if (curveData.instruments == null || curveData.instruments.length == 0)
				throw new IllegalArgumentException("no instrument given"); // FIXME: message

			// sort rate helpers
			for (int i = 0; i < curveData.instruments.length; i++)
				curveData.instruments[i].setTermStructure(yts);
			Sorting.quickSort(curveData.instruments, new RateHelperSorter<RateHelper<YieldTermStructure>>());
			// check that there is no instruments with the same maturity
			for (int i = 1; i < curveData.instruments.length; i++) {
				Date m1 = curveData.instruments[i - 1].getLatestDate();
				Date m2 = curveData.instruments[i].getLatestDate();
				if (m1.equals(m2)) // FIXME: verify equality 
					throw new IllegalArgumentException("two instruments have the same maturity (" + m1 + ")"); // FIXME: message
			}
			for (int i = 1; i < curveData.instruments.length; i++)
				curveData.instruments[i].addObserver(this);
		}

	}

	/**
	 * Composition pattern to an UnaryFunctionDouble
	 * 
	 * @see UnaryFunctionDouble
	 */
	private class ObjectiveFunction implements UnaryFunctionDouble {

		// XXX :: const PiecewiseYieldCurve<C,I>* curve_;
		private RateHelper<? extends YieldTermStructure>	rateHelper;
		private int											segment;

		public ObjectiveFunction(final RateHelper<? extends YieldTermStructure> rateHelper, final int segment) {
			// XXX
			// parameter ::final PiecewiseYieldCurve<C,I>* curve, 
			// this.curve = curve;
			this.rateHelper = rateHelper;
			this.segment = segment;
		}

		@Override
		public double evaluate(double guess) {
			traits.updateGuess(data, guess, segment);
			interpolation.update();
			return rateHelper.getQuoteError();
		}

	}

}
