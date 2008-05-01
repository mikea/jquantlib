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

import org.jquantlib.util.Observable;
import org.jquantlib.util.Observer;
import org.jquantlib.util.WeakReferenceObservable;

/**
 * Shared handle to an observable
 * <p>
 * All copies of an instance of this class refer to the same observable by means
 * of a relinkable weak reference. When such pointer is relinked to another
 * observable, the change will be propagated to all the copies.
 * 
 * @author Richard Gomes
 */
public class Handle<T extends Observable> implements Observable {

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
    
	public final boolean isEmpty() /* @ReadOnly */ {
		return link.isEmpty();
	}
	
    public final boolean isObserver() /* @ReadOnly */ {
    	return link.isObserver();
    }
    
    public final T getLink() {
    	return link.getLink();
    }
    
    public void setLink(final T observable) {
    	link.setLink(observable);
    }
    

	//
	// implements Observable interface
	//

    private Observable delegatedObservable = new WeakReferenceObservable(this);
	
    public final void addObserver(Observer observer) {
    	delegatedObservable.addObserver(observer);
	}

	public final int countObservers() {
		return delegatedObservable.countObservers();
	}

	public final void deleteObserver(Observer observer) {
		delegatedObservable.deleteObserver(observer);
	}

	public final void notifyObservers() {
		delegatedObservable.notifyObservers();
	}

	public final void notifyObservers(Object arg) {
		delegatedObservable.notifyObservers(arg);
	}

	public final void deleteObservers() {
		delegatedObservable.deleteObservers();
	}

	public final List<Observer> getObservers() {
		return delegatedObservable.getObservers();
	}


	//
    // inner classes
    //
    
    private class Link implements Observable, Observer {
    	static private final String EMPTY_HANDLE = "empty Handle cannot be dereferenced";
    	
		public T observable	= null;

		public Link() {
			this(null);
		}

		public Link(T observable) {
			setLink(observable);
		}

		public final boolean isEmpty() /* @ReadOnly */ {
			return (this.observable==null);
		}
		
		public final boolean isObserver() /* @ReadOnly */{
			return (this.observable != null);
		}

		public final T getLink() /* @ReadOnly */ {
			return this.observable;
		}

		public final void setLink(final T observable) {
			// remove this from observable
			if (this.observable != null) {
				this.observable.deleteObserver(this);
			}
			// register this as observer to a new observable
			if (observable!=null) {
				this.observable = observable;
				this.observable.addObserver(this);
				this.observable.notifyObservers();
			}
		}
		
		
		//
		// Implements Observer interface
		//
		
		public final void update(Observable o, Object arg) {
			delegatedObservable.notifyObservers(arg);
		}

		
		//
		// implements Observable interface
		//
		
	    public final void addObserver(Observer observer) {
	    	if (observable==null) throw new IllegalStateException(EMPTY_HANDLE);
	    	observable.addObserver(observer);
		}

		public final int countObservers() {
	    	if (observable==null) throw new IllegalStateException(EMPTY_HANDLE);
			return observable.countObservers();
		}

		public final void deleteObserver(Observer observer) {
	    	if (observable==null) throw new IllegalStateException(EMPTY_HANDLE);
			observable.deleteObserver(observer);
		}

		public final void notifyObservers() {
	    	if (observable==null) throw new IllegalStateException(EMPTY_HANDLE);
			observable.notifyObservers();
		}

		public final void notifyObservers(Object arg) {
	    	if (observable==null) throw new IllegalStateException(EMPTY_HANDLE);
			observable.notifyObservers(arg);
		}

		public final void deleteObservers() {
	    	if (observable==null) throw new IllegalStateException(EMPTY_HANDLE);
			observable.deleteObservers();
		}

		public final List<Observer> getObservers() {
	    	if (observable==null) throw new IllegalStateException(EMPTY_HANDLE);
			return observable.getObservers();
		}
		
	}
    
}
