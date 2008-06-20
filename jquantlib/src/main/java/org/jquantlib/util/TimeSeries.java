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

package org.jquantlib.util;

import java.util.Collection; //FIXME: performance
import java.util.List; //FIXME: performance
import java.util.SortedMap; //FIXME: performance
import java.util.TreeMap; //FIXME: performance

/**
 * TODO: Class arranges the dates in order which is different from quantlib, make sure
 * behavior is ok
 *
 * @author Srinivas Hasti
 *
 */

//TODO: Make this a Observable
public class TimeSeries<T> implements Observable {
	
	private SortedMap<Date,T> series = new TreeMap<Date,T>();
	
	public TimeSeries(){
		
	}
	
	public TimeSeries(List<Date> dates, List<T> values){
		for(int i=0;i<dates.size();i++){
			series.put(dates.get(i), values.get(i));
		}
	}
	
	public TimeSeries(Date startingDate, List<T> values){
		Date tmp = startingDate;
		for(int i=0;i<values.size();i++){			
			series.put(tmp, values.get(i));
			tmp = startingDate.getDateAfter(i);
		}
	}
	
	 //! returns the first date for which a historical datum exists
    public Date getFirstDate(){
    	return series.firstKey(); //TODO: make it read only
    }
    //! returns the last date for which a historical datum exists
    public Date lastDate(){
    	return series.lastKey(); //TODO: make it read only
    }
    //! returns the number of historical data including null ones
    public int size(){
    	return series.size();
    }
    
    //! returns whether the series contains any data
    boolean isEmpty(){
    	return series.isEmpty();
    }
    
    public T find(Date d){
    	return series.get(d);
    }
    
    public Collection<T> values(){
    	return series.values();
    }
    
    public Collection<Date> dates(){
    	return series.keySet();
    }

	public void add(Date date, T dt) {
		series.put(date, dt);
	}
	
	
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
