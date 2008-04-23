/*
 Copyright (C) 2007 Richard Gomes

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquantlib-dev@lists.sf.net>. The license is also available online at
 <http://jquantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the originating copyright notice follows below.
 */

/*
 Copyright (C) 2004, 2005, 2006 StatPro Italia srl

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

package org.jquantlib.termstructures;

import java.util.List;

import org.jquantlib.Settings;
import org.jquantlib.daycounters.Actual365Fixed;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.math.interpolation.DefaultExtrapolator;
import org.jquantlib.math.interpolation.Extrapolator;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.util.Date;
import org.jquantlib.util.DefaultObservable;
import org.jquantlib.util.Observable;
import org.jquantlib.util.Observer;
import org.jquantlib.util.Visitable;
import org.jquantlib.util.Visitor;



/**
 * Basic term-structure functionality.
 * 
 * <p><b>More Details about constructors:</b>
 * <p>There are three ways in which a term structure can keep
 * track of its reference date:
 * <li>such date is fixed;</li>
 * <li>such date is determined by advancing the current date of a given number of business days;</li>
 * <li>such date is based on the reference date of some other structure.</li>
 * 
 * <p>Case 1: The constructor taking a date is to be used.
 * The default implementation of {@link TermStructure#getReferenceDate()} will
 * then return such date.
 * 
 * <p>Case 2: The constructor taking a number of days and a calendar is to be used 
 * so that {@link TermStructure#getReferenceDate()} will return a date calculated based on the
 * current evaluation date and the term structure and observers will be notified when the
 * evaluation date changes.
 * 
 * <p>Case 3: The {@link TermStructure#getReferenceDate()} method must
 * be overridden in derived classes so that it fetches and
 * return the appropriate date.
 *  
 * @author Richard Gomes
 */
// FIXME: document this class
public abstract class TermStructure implements Observable, Observer, Extrapolator, Visitable<TermStructure> {

	private Date referenceDate;
	private int settlementDays;
	private Calendar calendar;
	private DayCounter dayCounter;
	private boolean moving;
	private boolean updated;
	private int nCase;

	/**
	 * This private field is automatically initialized by constructor which
	 * picks up it's value from {@link Settings} singleton. This procedure
	 * caches values from the singleton, intending to avoid contention in
	 * heavily multi-threaded environments.
	 */
	private Date today = null;

	
	/**
	 * <p>This constructor requires an override of method {@link TermStructure#getReferenceDate()} in 
	 * derived classes so that it fetches and return the appropriate reference date.
	 * This is the <i>Case 3</i> described on the top of this class.
	 * 
     * @see TermStructure documentation for more details about constructors.
	 */
	public TermStructure() {
		this(new Actual365Fixed());
	}

	/**
	 * <p>This constructor requires an override of method {@link TermStructure#getReferenceDate()} in 
	 * derived classes so that it fetches and return the appropriate reference date.
	 * This is the <i>Case 3</i> described on the top of this class.
	 * 
     * @see TermStructure documentation for more details about constructors.
	 */
	public TermStructure(final DayCounter dc) {
		if (dc==null) throw new NullPointerException(); // TODO: message
		this.settlementDays = 0;
		this.dayCounter = dc;
		this.moving = false;
		this.updated = true;
		this.nCase = 3;
		today = Settings.getInstance().getEvaluationDate();
		today.addObserver(this);
	}

	/**
	 * Initialize with a fixed reference date
	 * 
	 * <p>This constructor takes a date to be used. 
	 * The default implementation of {@link TermStructure#getReferenceDate()} will
	 * then return such date.
	 * This is the <i>Case 1</i> described on the top of this class.
	 * 
     * @see TermStructure documentation for more details about constructors.
	 */ 
	public TermStructure(final Date referenceDate, final Calendar calendar) {
		this(referenceDate, calendar, new Actual365Fixed());
	}

	/**
	 * Initialize with a fixed reference date
	 * 
	 * <p>This constructor takes a date to be used. 
	 * The default implementation of {@link TermStructure#getReferenceDate()} will
	 * then return such date.
	 * This is the <i>Case 1</i> described on the top of this class.
	 * 
     * @see TermStructure documentation for more details about constructors.
	 */ 
	public TermStructure(final Date referenceDate, final Calendar calendar, final DayCounter dc) {
		if (referenceDate==null) throw new NullPointerException(); // TODO: message
		if (calendar==null) throw new NullPointerException(); // TODO: message
		if (dc==null) throw new NullPointerException(); // TODO: message
		this.referenceDate = referenceDate;
		this.settlementDays = 0;
		this.calendar = calendar;
		this.dayCounter = dc;
		this.moving = false;
		this.updated = true;
		this.nCase = 1;
		today = Settings.getInstance().getEvaluationDate();
		today.addObserver(this);
	}
	
	/**
	 * Calculate the reference date based on the global evaluation date
	 * 
	 * <p>This constructor takes a number of days and a calendar to be used 
	 * so that {@link TermStructure#getReferenceDate()} will return a date calculated based on the
	 * current evaluation date and the term structure. This class will be notified when the
	 * evaluation date changes.
	 * This is the <i>Case 2</i> described on the top of this class.
	 * 
     * @see TermStructure documentation for more details about constructors.
	 */ 
	public TermStructure(final int settlementDays, final Calendar calendar) {
		this(settlementDays, calendar, new Actual365Fixed());
	}

	
	/**
	 * Calculate the reference date based on the global evaluation date
	 * 
	 * <p>This constructor takes a number of days and a calendar to be used 
	 * so that {@link TermStructure#getReferenceDate()} will return a date calculated based on the
	 * current evaluation date and the term structure. This class will be notified when the
	 * evaluation date changes.
	 * This is the <i>Case 2</i> described on the top of this class.
	 * 
     * @see TermStructure documentation for more details about constructors.
	 */ 
	public TermStructure(final int settlementDays, final Calendar calendar, final DayCounter dc) {
		this.settlementDays = settlementDays;
		this.calendar = calendar;
		this.dayCounter = dc;
		this.moving = true;
		this.updated = false;
		this.nCase = 2;
		today = Settings.getInstance().getEvaluationDate();
		today.addObserver(this);
	}
	
	/**
	 * @return the latest date for which the curve can return values
	 */
	public abstract Date getMaxDate();

	/**
	 * @return the calendar used for reference date calculation
	 */
	public final Calendar getCalendar() {
		return calendar;
	}

	/**
	 * This method performs a date to double conversion which represents
	 * the fraction of the year between the reference date and 
	 * the date passed as parameter.
	 *  
	 * @param date
	 * @return the fraction of the year as a double
	 */
	protected final /*@Time*/ double getTimeFromReference(final Date date) {
		return dayCounter.getYearFraction(getReferenceDate(), date);
	}

	/**
	 * This method performs date-range check
	 */ 
	protected final void checkRange(final Date date, boolean extrapolate) {
		checkRange(getTimeFromReference(date), extrapolate);
	}

	/**
	 * This method performs date-range check
	 */ 
	protected final void checkRange(final /*@Time*/ double time, boolean extrapolate) {
		/*@Time*/ double t = time;
		if (t<0.0) throw new IllegalArgumentException("negative double given");
		if (! (extrapolate || allowsExtrapolation() || (t<=getMaxTime())) ) 
			throw new IllegalArgumentException("double ("+time+") is past max curve double ("+getMaxTime()+")");
	}
	
	/**
	 * @return the day counter used for date/double conversion 
	 */
	public DayCounter getDayCounter() {
		return dayCounter;
	}

	/**
	 * @return the latest double for which the curve can return values
	 */
	public final /*@Time*/ double getMaxTime(){
		return getTimeFromReference(getMaxDate());
	}

	/**
	 * Returns the Date at which discount = 1.0 and/or variance = 0.0
	 * 
	 * @note Term structures initialized by means of this
	 * constructor must manage their own reference date 
	 * by overriding the referenceDate() method.
	 *  
	 * @returns the Date at which discount = 1.0 and/or variance = 0.0
	 */
	public Date getReferenceDate() {
		switch (nCase) {
		case 1:
			return referenceDate;
		case 2:
			if (!updated) {
				referenceDate = calendar.advance(today, settlementDays, TimeUnit.Days);
				updated = true;
			}
			return referenceDate;
		case 3:
			throw new UnsupportedOperationException("getReferenceDate must be overridden on derived classes");
		default:
			throw new IllegalArgumentException();
		}
	}

	
	/**
	 * Implements multiple inheritance via delegate pattern to a inner class
	 * 
	 * @see Extrapolator
	 */
	private DefaultExtrapolator delegatedExtrapolator = new DefaultExtrapolator();
	
	public final boolean allowsExtrapolation() {
		return delegatedExtrapolator.allowsExtrapolation();
	}

	public void disableExtrapolation() {
		delegatedExtrapolator.disableExtrapolation();
	}

	public void enableExtrapolation() {
		delegatedExtrapolator.enableExtrapolation();
	}

	

	
	//
	// Implements Observer interface
	//
	
	public void update(Observable o, Object arg) {
		if (moving) {
			updated = false;
			today = Settings.getInstance().getEvaluationDate();
			notifyObservers();
		}
	}

	
	//
	// implements Observable interface
	//
	
	/**
	 * Implements multiple inheritance via delegate pattern to an inner class
	 * 
	 * @see Observable
	 * @see DefaultObservable
	 */
    private Observable delegatedObservable = new DefaultObservable(this);

	public void addObserver(Observer observer) {
		delegatedObservable.addObserver(observer);
	}

	public int countObservers() {
		return delegatedObservable.countObservers();
	}

	public void deleteObserver(Observer observer) {
		delegatedObservable.deleteObserver(observer);
	}

	public void notifyObservers() {
		delegatedObservable.notifyObservers();
	}

	public void notifyObservers(Object arg) {
		delegatedObservable.notifyObservers(arg);
	}

	public void deleteObservers() {
		delegatedObservable.deleteObservers();
	}

	public List<Observer> getObservers() {
		return delegatedObservable.getObservers();
	}

	
	
	//
	// implements Visitable interface
	//
	
	private static final String NULL_VISITOR = "null term structure visitor";

	public final void accept(final Visitor<TermStructure> v) {
		if (v != null) {
			v.visit(this);
		} else {
			throw new NullPointerException(NULL_VISITOR);
		}
	}

}
