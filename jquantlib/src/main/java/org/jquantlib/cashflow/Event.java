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

package org.jquantlib.cashflow;

import java.util.Date;

import org.jquantlib.Settings;
import org.jquantlib.util.DefaultObservable;
import org.jquantlib.util.Observable;
import org.jquantlib.util.Observer;
import org.jquantlib.util.Visitable;
import org.jquantlib.util.Visitor;

/**
 * This class is the base class for all financial events.
 * 
 * @author Richard Gomes
 */
//CODE REVIEW DONE by Richard Gomes
public abstract class Event implements Observable, Visitable<Event> {

	/**
	 * This private field is automatically initialized by constructor which
	 * picks up it's value from {@link Settings} singleton. This procedure
	 * caches values from the singleton, intending to avoid contention in
	 * heavily multi-threaded environments.
	 */
	// TODO: make this property dynamically configurable
	private boolean todaysPayments = Settings.getInstance().isTodaysPayments();;

	protected Event() { }

	/**
	 * Keeps the date at which the event occurs
	 */
	protected abstract Date date() /* @ReadOnly */;

	/**
	 * Returns true if an event has already occurred before a date where the
	 * current date may or may not be considered accordingly to defaults taken
	 * from {@link Settings}
	 * 
	 * @param d
	 *            is a Date
	 * @return true if an event has already occurred before a date
	 * 
	 * @see Settings.todaysPayments
	 * @see todaysPayments
	 */
	public boolean hasOccurred(final Date d) /* @ReadOnly */{
		return hasOccurred(d, todaysPayments);
	}

	/**
	 * Returns true if an event has already occurred before a date where it is
	 * explicitly defined whether the current date must considered.
	 * 
	 * @param d
	 *            is a Date
	 * @return true if an event has already occurred before a date
	 */
	public boolean hasOccurred(final Date d, final boolean includeToday) /* @ReadOnly */{
		if (includeToday) {
			return date().compareTo(d) < 0;
		} else {
			return date().compareTo(d) <= 0;
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
	private DefaultObservable delegatedObservable = new DefaultObservable(this);

	public void addObserver(Observer observer) {
		delegatedObservable.addObserver(observer);
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

	//
	// implements Visitable interface
	//

	private static final String NULL_VISITOR = "null event visitor";

	public final void accept(final Visitor<Event> v) {
		if (v != null) {
			v.visit(this);
		} else {
			throw new NullPointerException(NULL_VISITOR);
		}
	}
}