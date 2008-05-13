package org.jquantlib.termstructures;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.math.interpolation.Extrapolator;
import org.jquantlib.time.Calendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.Observable;
import org.jquantlib.util.Observer;
import org.jquantlib.util.Visitable;

public interface TermStructureIntf extends Observable, Observer, Extrapolator, Visitable<TermStructureIntf> {

	/**
	 * @return the latest date for which the curve can return values
	 */
	public abstract Date getMaxDate();

	/**
	 * @return the calendar used for reference date calculation
	 */
	public abstract Calendar getCalendar();

	/**
	 * @return the day counter used for date/double conversion 
	 */
	public abstract DayCounter getDayCounter();

	/**
	 * @return the latest double for which the curve can return values
	 */
	public abstract/*@Time*/double getMaxTime();

	/**
	 * Returns the Date at which discount = 1.0 and/or variance = 0.0
	 * 
	 * @note Term structures initialized by means of this
	 * constructor must manage their own reference date 
	 * by overriding the referenceDate() method.
	 *  
	 * @returns the Date at which discount = 1.0 and/or variance = 0.0
	 */
	public abstract Date getReferenceDate();

}