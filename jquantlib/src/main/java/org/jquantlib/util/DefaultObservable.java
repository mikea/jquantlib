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

package org.jquantlib.util;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Implementation of a Observable that can be used as delegate for your own
 * implementations. This implementation notifies the observers in a synchronous
 * fashion. Note that this can cause trouble if you notify the observers while
 * in a transactional context because the notification is then done also in the
 * transaction.
 * 
 * <p>
 * This class is based on the work done by Martin Fischer, with only minor
 * changes. See references below.
 * 
 * @see <a
 *      href="http://www.jroller.com/martin_fischer/entry/a_generic_java_observer_pattern">
 *      Martin Fischer: Observer and Observable interfaces</a>
 * @see <a href="http://jdj.sys-con.com/read/35878.htm">Improved
 *      Observer/Observable</a>
 * @see Observable
 * 
 * @author Martin Fischer (original author)
 * @author Richard Gomes
 * @author Srinivas Hasti
 */
public class DefaultObservable implements Observable {

    private final List<Observer> observers = new CopyOnWriteArrayList<Observer>();
    private final Observable observable;
    
    public DefaultObservable(Observable observable) {
        if(observable == null) throw new NullPointerException("observable is null");
        this.observable = observable;       
    }
    
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
        for (Observer observ : observers) {
            wrappedNotify(observ, observable, arg);
        }
    }

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