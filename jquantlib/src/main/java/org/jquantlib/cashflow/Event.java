/*
 Copyright (C) 2007 Richard Gomes

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

package org.jquantlib.cashflow;

import java.util.List; //FIXME: performance

import org.jquantlib.Configuration;
import org.jquantlib.Settings;
import org.jquantlib.util.Date;
import org.jquantlib.util.DefaultObservable;
import org.jquantlib.util.Observable;
import org.jquantlib.util.Observer;
import org.jquantlib.util.TypedVisitable;
import org.jquantlib.util.TypedVisitor;
import org.jquantlib.util.Visitor;

/**
 * This class is the base class for all financial events.
 * 
 * @author Richard Gomes
 */
public abstract class Event implements Observable, TypedVisitable<Event> {

	/**
	 * This private field is automatically initialized by constructor which
	 * picks up it's value from {@link Settings} singleton. This procedure
	 * caches values from the singleton, intending to avoid contention in
	 * heavily multi-threaded environments.
	 */
	// TODO: make this property dynamically configurable
	private boolean todaysPayments = Configuration.getSystemConfiguration(null).getGlobalSettings().isTodaysPayments();;

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
	
	/* (non-Javadoc)
	 * @see org.jquantlib.util.Observable#deleteObservers()
	 */
	@Override
	public void deleteObservers() {
		delegatedObservable.deleteObservers();
	}

	/* (non-Javadoc)
	 * @see org.jquantlib.util.Observable#getObservers()
	 */
	@Override
	public List<Observer> getObservers() {
		return delegatedObservable.getObservers();
	}


	//
	// implements TypedVisitable
	//
	
	@Override
	public void accept(final TypedVisitor<Event> v) {
		Visitor<Event> v1 = (v!=null) ? v.getVisitor(this.getClass()) : null;
		if (v1 != null) {
			v1.visit(this);
		} else {
			throw new IllegalArgumentException("null event visitor"); //TODO: message
		}
	}
	
}
