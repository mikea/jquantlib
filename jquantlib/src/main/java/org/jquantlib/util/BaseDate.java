/*
 Copyright (C) 2007 Srinivas Hasti

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

import java.util.List;

/**
 * Base implementation that can be shared by Date implementations.
 * 
 * @author Srinivas Hasti
 * 
 */
public abstract class BaseDate implements Date {
    
    /**
     * Implements multiple inheritance via delegate pattern to an inner class
     * 
     */
    private Observable delegatedObservable = new DefaultObservable(this);

    /**
     * 
     * {@inheritDoc}  	
     *
     */
    public void addObserver(Observer observer) {
        delegatedObservable.addObserver(observer);
    }

    /**
     * 
     * {@inheritDoc}  	
     *
     */
    public int countObservers() {
        return delegatedObservable.countObservers();
    }

    /**
     * 
     * {@inheritDoc}  	
     *
     */
    public void deleteObserver(Observer observer) {
        delegatedObservable.deleteObserver(observer);
    }

    /**
     * 
     * {@inheritDoc}  	
     *
     */
    public void notifyObservers() {
        delegatedObservable.notifyObservers();
    }

    /**
     * 
     * {@inheritDoc}  	
     *
     */
    public void notifyObservers(Object arg) {
        delegatedObservable.notifyObservers(arg);
    }

    /**
     * 
     * {@inheritDoc}  	
     *
     */
    public void deleteObservers() {
        delegatedObservable.deleteObservers();
    }

    /**
     * 
     * {@inheritDoc}  	
     *
     */
    public List<Observer> getObservers() {
        return delegatedObservable.getObservers();
    }

}
