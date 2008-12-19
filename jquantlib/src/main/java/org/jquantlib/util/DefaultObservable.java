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

package org.jquantlib.util;

import java.util.Collections; //FIXME: performance
import java.util.List; //FIXME: performance
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// --------------------------------------------------------
// This class is based on the work done by Martin Fischer.
// See references in JavaDoc
//--------------------------------------------------------

/**
 * Default implementation of an {@link Observable}.
 * <p>
 * This implementation notifies the observers in a synchronous fashion. Note that this can cause trouble if you notify the observers
 * while in a transactional context because once the notification is done it cannot be rolled back.
 * 
 * @see <a href="http://www.jroller.com/martin_fischer/entry/a_generic_java_observer_pattern"> Martin Fischer: Observer and
 *      Observable interfaces</a>
 * @see <a href="http://jdj.sys-con.com/read/35878.htm">Improved Observer/Observable</a>
 * 
 * @see Observable
 * @see Observer
 * @see WeakReferenceObservable
 * 
 * @author Richard Gomes
 * @author Srinivas Hasti
 */
public class DefaultObservable implements Observable {
	
	//
	// logger
	//
	private final static Logger logger = LoggerFactory.getLogger(DefaultObservable.class);

    //
    // private final fields
    //

    private final List<Observer> observers = new CopyOnWriteArrayList<Observer>();
    private final Observable observable;

    //
    // public constructors
    //

    public DefaultObservable(Observable observable) {
        if (observable == null)
            throw new NullPointerException("observable is null");
        this.observable = observable;
    }

    //
    // public methods
    //

    public void addObserver(final Observer observer) {
        if (observer == null)
            throw new NullPointerException("observer is null");
        observers.add(observer);
    }

    public int countObservers() {
        return observers.size();
    }

    public List<Observer> getObservers() {
        return Collections.unmodifiableList(this.observers);
    }

    public void deleteObserver(final Observer observer) {
        observers.remove(observer);
    }

    public void deleteObservers() {
        observers.clear();
    }

    public void notifyObservers() {
        notifyObservers(null);
    }

    public void notifyObservers(Object arg) {
        for (Observer observer : observers) {
            wrappedNotify(observer, observable, arg);
        }
    }

    //
    // protected methods
    //

    /**
     * This method is intended to encapsulate the notification semantics, in
     * order to let extended classes to implement their own version. Possible
     * implementations are:
     * <li>remote notification;</li>
     * <li>notification via SwingUtilities.invokeLater</li>
     * <li>others...</li> 
     * 
     * <p>
     * The default notification simply does
     * <pre>
     * observer.update(observable, arg);
     * </pre>
     * 
     * @param observer
     * @param observable
     * @param arg
     */
    protected void wrappedNotify(Observer observer, Observable observable, Object arg) {
        observer.update(observable, arg);
    }

}