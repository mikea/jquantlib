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

package org.jquantlib.quotes;

import org.jquantlib.util.Observable;

/**
 * This class exists only as a tagging point for translations of other classes
 * and, in fact, <b>THIS CLASS MUST NEVER BE USED</b> in your code.
 * 
 * @note All methods of this class throw UnsupportedOperationException on purpose.
 * 
 * @author Richard Gomes
 */
@Deprecated
public class Handle<T extends Observable> {

	/**
	 * All methods from class Handle throw UnsupportedOperationException
	 * on purpose. Never use this class.
	 */
	@Override
	@Deprecated
	public boolean equals(Object other) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * All methods from class Handle throw UnsupportedOperationException
	 * on purpose. Never use this class.
	 */
	@Deprecated
	public boolean eq(T other) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * All methods from class Handle throw UnsupportedOperationException
	 * on purpose. Never use this class.
	 */
	@Deprecated
	public boolean ne(T other) {
		throw new UnsupportedOperationException();
	}

	/**
	 * All methods from class Handle throw UnsupportedOperationException
	 * on purpose. Never use this class.
	 */
	@Deprecated
	public boolean le(T other) {
		throw new UnsupportedOperationException();
	}

	/**
	 * All methods from class Handle throw UnsupportedOperationException
	 * on purpose. Never use this class.
	 */
	@Deprecated
	public boolean lt(T other) {
		throw new UnsupportedOperationException();
	}

	/**
	 * All methods from class Handle throw UnsupportedOperationException
	 * on purpose. Never use this class.
	 */
	@Deprecated
	public boolean ge(T other) {
		throw new UnsupportedOperationException();
	}

	/**
	 * All methods from class Handle throw UnsupportedOperationException
	 * on purpose. Never use this class.
	 */
	@Deprecated
	public boolean gt(T other) {
		throw new UnsupportedOperationException();
	}

	/**
	 * All methods from class Handle throw UnsupportedOperationException
	 * on purpose. Never use this class.
	 */
	@Deprecated
	public boolean empty() {
		throw new UnsupportedOperationException();
	}

	/**
	 * All methods from class Handle throw UnsupportedOperationException
	 * on purpose. Never use this class.
	 *
	 *<p>Substitute this code ...
<code>
Handle h = observable1;
h.addObserver(observer1);
h.addObserver(observer2);
h.addObserver(observer3);
h.linkTo(observable2);
</code>
by...
<code>
observable1.addObserver(observer);
foreach (Observer observer : observable1.getObservers) {
  observable2.addObserver(observer);
}
observable2.notifyObservers();
</code>
	 */
	@Deprecated
	public void linkTo(T other) {
		throw new UnsupportedOperationException();
	}

	/**
	 * All methods from class Handle throw UnsupportedOperationException
	 * on purpose. Never use this class.
	 */
	@Deprecated
	public boolean evaluate() {
		throw new UnsupportedOperationException();
	}

	/**
	 * All methods from class Handle throw UnsupportedOperationException
	 * on purpose. Never use this class.
	 */
	@Deprecated
	public T dereference() {
		throw new UnsupportedOperationException();
	}

	/**
	 * All methods from class Handle throw UnsupportedOperationException
	 * on purpose. Never use this class.
	 */
	@Deprecated
	public T currentLink() {
		throw new UnsupportedOperationException();
	}

}
