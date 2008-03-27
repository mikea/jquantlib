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
 Copyright (C) 2003 RiskMap srl

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

package org.jquantlib.util;


/**
 * Framework for calculation on demand and result caching.
 * 
 * @see <a href="http://c2.com/cgi/wiki?LazyObject">Lazy Object Design Pattern</a>
 */
public abstract class LazyObject implements Observable, Observer {

	protected boolean calculated;
	protected boolean frozen;

	/**
	 * This method must implement any calculations which must be (re)done in
	 * order to calculate the desired results.
	 */
	protected abstract void performCalculations() throws ArithmeticException;

	public LazyObject() {
		this.calculated = false;
		this.frozen = false;
	}

	public void update(Observable o, Object arg) {
		// observers don't expect notifications from frozen objects
		// LazyObject forwards notifications only once until it has been
		// recalculated
		if (!frozen && calculated)
			notifyObservers(arg);
		calculated = false;
	}

	/**
	 * This method force the recalculation of any results which would otherwise
	 * be cached.
	 * 
	 * @note Explicit invocation of this method is <b>not</b> necessary
	 * if the object registered itself as observer with the structures on which
	 * such results depend. It is strongly advised to follow this policy when
	 * possible.
	 */
	public final void recalculate() {
		boolean wasFrozen = frozen;
		calculated = frozen = false;
		try {
			calculate();
		} finally {
			frozen = wasFrozen;
			notifyObservers();
		}
	}

	/**
	 * This method constrains the object to return the presently cached results
	 * on successive invocations, even if arguments upon which they depend
	 * should change.
	 */
	public void freeze() {
		frozen = true;
	}

	/**
	 * This method reverts the effect of the <i><b>freeze</b></i> method,
	 * thus re-enabling recalculations.
	 */
	public void unfreeze() {
		frozen = false;
		// send notification, just in case we lost any
		notifyObservers();
	}

	/**
	 * This method performs all needed calculations by calling the <i><b>performCalculations</b></i>
	 * method.
	 * 
	 * @note Objects cache the results of the previous calculation. Such
	 * results will be returned upon later invocations of <i><b>calculate</b></i>.
	 * When the results depend on arguments which could change between
	 * invocations, the lazy object must register itself as observer of such
	 * objects for the calculations to be performed again when they change.
	 */
	protected void calculate() {
		if (!calculated && !frozen) {
			// prevent infinite recursion in case of bootstrapping
			calculated = true; 
			try {
				performCalculations();
			} finally {
				calculated = false;
			}
		}
	}

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

	public void deleteObserver(Observer observer) {
		delegatedObservable.deleteObserver(observer);
	}

	public void notifyObservers() {
		delegatedObservable.notifyObservers();
	}

	public void notifyObservers(Object arg) {
		delegatedObservable.notifyObservers(arg);
	}


}
