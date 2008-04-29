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
 Copyright (C) 2000, 2001, 2002, 2003 RiskMap srl
 Copyright (C) 2003, 2004, 2005, 2006, 2007 StatPro Italia srl

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

package org.jquantlib.quotes;

import java.util.List;

import org.jquantlib.util.DefaultObservable;
import org.jquantlib.util.Observable;
import org.jquantlib.util.Observer;

/**
 * Shared handle to an observable
 * <p>
 * All copies of an instance of this class refer to the same observable by means
 * of a relinkable weak reference. When such pointer is relinked to another
 * observable, the change will be propagated to all the copies.
 * 
 * @author Richard Gomes
 */
public class Handle<T extends Observable> implements Observable, Observer {

	protected Link link;
    
    public Handle() {
    	this.link = new Link();
    }
    
    
    public Handle(final T observable) {
    	this.link = new Link(observable);
    }
    
    public Handle(final Handle<T> another) {
    	this.link = another.link;
    }
    
    protected void linkTo(final T observable) {
    	link.linkTo(observable);
    }
    
    
    public boolean isObserver() /* @ReadOnly */ {
    	return link.isObserver();
    }
    
    
	//
	// Implements Observer interface
	//
	
	public void update(Observable o, Object arg) {
		notifyObservers(arg);
	}

	
	//
	// implements Observable interface
	//
	
    public void addObserver(Observer observer) {
    	link.addObserver(observer);
	}

	public int countObservers() {
		return link.countObservers();
	}

	public void deleteObserver(Observer observer) {
		link.deleteObserver(observer);
	}

	public void notifyObservers() {
		link.notifyObservers();
	}

	public void notifyObservers(Object arg) {
		link.notifyObservers(arg);
	}

	public void deleteObservers() {
		link.deleteObservers();
	}

	public List<Observer> getObservers() {
		return link.getObservers();
	}



	
	//
    // inner classes
    //
    
    private class Link implements Observable, Observer {
		public T	observable	= null;

		public Link() {
			this.observable = null;
		}

		public Link(T observable) {
			this.observable = observable;
		}

		public boolean isObserver() /* @ReadOnly */{
			return (this.observable != null);
		}

		public void linkTo(final T observable) {
			// remove this from observable
			if (this.observable != null) {
				this.observable.deleteObserver(this);
			}
			// register this as observer to a new observable
			this.observable = observable;
			this.observable.addObserver(this);
			this.observable.notifyObservers();
		}

		//
		// Implements Observer interface
		//
		
		public void update(Observable o, Object arg) {
			notifyObservers(arg);
		}
		
		//
		// implements Observable interface
		//
		
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
		
	}
    
}
