/*
 Copyright (C) 2007 Srinivas Hasti

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

package org.jquantlib.indexes;

import java.util.List;

import org.jquantlib.time.Calendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.DefaultObservable;
import org.jquantlib.util.Observable;
import org.jquantlib.util.Observer;

/**
 * @author Srinivas Hasti
 *
 */
public abstract class Index implements Observable{

	public abstract String getName();
	
	
    //! returns the calendar defining valid fixing dates
    public abstract Calendar fixingCalendar() ;
    
    //! returns TRUE if the fixing date is a valid one
    public abstract boolean isValidFixingDate(Date fixingDate)
    ;
    //! returns the fixing at the given date
    /*! the date passed as arguments must be the actual calendar
        date of the fixing; no settlement days must be used.
    */
    public abstract double fixing(Date fixingDate,
                        boolean forecastTodaysFixing);
    
    //! returns the fixing TimeSeries
    //const TimeSeries<Real>& timeSeries() const {
    //    return IndexManager::instance().getHistory(name());
    //}
    //! stores the historical fixing at the given date
    /*! the date passed as arguments must be the actual calendar
        date of the fixing; no settlement days must be used.
    */
    public abstract void addFixing(Date fixingDate,
                           double fixing,
                           boolean forceOverwrite);
    
    //! stores historical fixings from a TimeSeries
    /*! the dates in the TimeSeries must be the actual calendar
        dates of the fixings; no settlement days must be used.
    */
    //void addFixings(const TimeSeries<Real>& t,
    //                bool forceOverwrite = false);
	
	  /**
     * Implements multiple inheritance via delegate pattern to an inner class
     * 
     */
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
