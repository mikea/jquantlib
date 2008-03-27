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

package org.jquantlib.pricingengines;

import org.jquantlib.pricingengines.arguments.Arguments;
import org.jquantlib.pricingengines.results.Results;
import org.jquantlib.util.DefaultObservable;
import org.jquantlib.util.Observable;
import org.jquantlib.util.Observer;

public abstract class GenericEngine<A extends Arguments, R extends Results> implements PricingEngine {

	protected A arguments;
	protected R results;

	public GenericEngine(final A arguments, final R results) {
		this.arguments = arguments;
		this.results = results;
	}

	public final A getArguments() {
		return arguments;
	}

	public final R getResults() {
		return results;
	}

	public void reset() {
		results.reset();
	}


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

}
