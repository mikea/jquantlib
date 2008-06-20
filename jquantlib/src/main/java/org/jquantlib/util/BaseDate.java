/*
 Copyright (C) 2008 Srinivas Hasti
 
 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquant-devel@lists.sourceforge.net>. The license is also available online at
 <http://www.jquantlib.org/index.php/LICENSE.TXT>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */

package org.jquantlib.util;

import java.util.List; //FIXME: performance

/**
 * Base implementation that can be shared by Date implementations.
 * 
 * @author Srinivas Hasti
 * 
 */
public abstract class BaseDate implements Date, FunctionDate {
    
    public final int compareTo(final Date o) {
		if (this.equals(o))
			return 0;
		if (this.le(o))
			return -1;
		return 1;
	}

	/**
     * Implements multiple inheritance via delegate pattern to an inner class
     * 
     */
    private final Observable delegatedObservable = new DefaultObservable(this);

    public final void addObserver(final Observer observer) {
        delegatedObservable.addObserver(observer);
    }

    public final int countObservers() {
        return delegatedObservable.countObservers();
    }

    public final void deleteObserver(final Observer observer) {
        delegatedObservable.deleteObserver(observer);
    }

    public final void notifyObservers() {
        delegatedObservable.notifyObservers();
    }

    public final void notifyObservers(final Object arg) {
        delegatedObservable.notifyObservers(arg);
    }

    public final void deleteObservers() {
        delegatedObservable.deleteObservers();
    }

    public final List<Observer> getObservers() {
        return delegatedObservable.getObservers();
    }

}
