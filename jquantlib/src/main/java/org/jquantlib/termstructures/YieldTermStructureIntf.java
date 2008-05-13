package org.jquantlib.termstructures;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.time.Frequency;
import org.jquantlib.util.Date;

public interface YieldTermStructureIntf extends TermStructureIntf {

	/**
	 * The resulting interest rate has the required day-counting rule.
	 */
	public abstract InterestRate getZeroRate(final Date d, final DayCounter resultDayCounter, final Compounding comp);

	/**
	 * The resulting interest rate has the required day-counting rule.
	 */
	public abstract InterestRate getZeroRate(final Date d, final DayCounter resultDayCounter, final Compounding comp,
			final Frequency freq);

	/**
	 * The resulting interest rate has the required day-counting rule.
	 */
	public abstract InterestRate getZeroRate(final Date d, final DayCounter dayCounter, final Compounding comp,
			final Frequency freq, boolean extrapolate);

	/**
	 * Forward rates
	 * 
	 * <p>
	 * These methods returns the implied forward interest rate between two dates
	 * or times. In the former case, times are calculated as fractions of year
	 * from the reference date. The resulting interest rate has the required
	 * day-counting rule.
	 * 
	 * <p>
	 * Dates are not adjusted for holidays
	 */
	public abstract InterestRate getForwardRate(final Date d1, final Date d2, final DayCounter resultDayCounter,
			final Compounding comp);

	public abstract InterestRate getForwardRate(final Date d1, final Date d2, final DayCounter resultDayCounter,
			final Compounding comp, final Frequency freq);

	public abstract InterestRate getForwardRate(final Date d1, final Date d2, final DayCounter dayCounter, final Compounding comp,
			final Frequency freq, boolean extrapolate);

	/**
	 * @see YieldTermStructure#forwardRate(Date, Date, DayCounter,
	 *      org.jquantlib.termstructures.InterestRate.Compounding, Frequency)
	 */
	public abstract InterestRate getForwardRate(final/*@Time*/double t1, final/*@Time*/double t2, final Compounding comp);

	/**
	 * @see YieldTermStructure#forwardRate(Date, Date, DayCounter,
	 *      org.jquantlib.termstructures.InterestRate.Compounding, Frequency)
	 */
	public abstract InterestRate getForwardRate(final/*@Time*/double t1, final/*@Time*/double t2, final Compounding comp,
			final Frequency freq);

	/**
	 * The resulting interest rate has the same day-counting rule used by the
	 * term structure. The same rule should be used for the calculating the
	 * passed times t1 and t2.
	 */
	// FIXME; this method is clearly buggy
	public abstract InterestRate getForwardRate(final/*@Time*/double time1, final/*@Time*/double time2, final Compounding comp,
			final Frequency freq, boolean extrapolate);

	/**
	 * Discount Factors
	 * 
	 * <p>
	 * These methods return the discount factor for a given date or time. In the
	 * former case, the double is calculated as a fraction of year from the
	 * reference date.
	 */
	public abstract/*@DiscountFactor*/double getDiscount(final Date d);

	public abstract/*@DiscountFactor*/double getDiscount(final Date d, boolean extrapolate);

	/**
	 * The same day-counting rule used by the term structure should be used for
	 * calculating the passed double t.
	 */
	public abstract/*@DiscountFactor*/double getDiscount(final/*@Time*/double t);

	public abstract/*@DiscountFactor*/double getDiscount(final/*@Time*/double t, boolean extrapolate);

}