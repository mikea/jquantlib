/*
 Copyright (C) 2007 Srinivas Hasti

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

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;


/**
 * @author Srinivas Hasti
 */
public class TimeSeries<T> implements Observable {

	private final TreeMap<Date, T> timeBucket = new TreeMap<Date, T>(new DateComparator()) ;
//	private final List<Date> dates = new ObjectArrayList<Date>();
//    private final List<T> values = new ObjectArrayList<T>();
	
	
    public TimeSeries() {
    	// nothing
    }

    public TimeSeries(final List<Date> dates, final List<T> values) {
    	this();
        for (int i = 0; i < dates.size(); i++) {
        	timeBucket.put(dates.get(i), values.get(i)) ;
        }
    }

    public TimeSeries(final Date startingDate, final List<T> values) {
    	this();
        Date tmp = startingDate;
        for (int i = 0; i < values.size(); i++) {
        	timeBucket.put(tmp, values.get(i)) ;
            tmp = startingDate.getDateAfter(i);
        }
    }

    /**
     * @return the first date for which a historical datum exists
     */
    public Date firstDate() /* @ReadOnly */ {
    	return timeBucket.firstKey() ;
    }

    /**
     * @return the last date for which a historical datum exists
     */
    public Date lastDate() /* @ReadOnly */ {
    	return timeBucket.lastKey() ;
    }

    /**
     * @return the number of historical data including null ones
     */
    public int size() /* @ReadOnly */ {
        return timeBucket.size();
    }

    /**
     * @return whether the series contains any data
     */
    public boolean isEmpty() /* @ReadOnly */ {
        return timeBucket.isEmpty();
    }

    public T find(final Date d) /* @ReadOnly */ {
    	return timeBucket.get(d);
    }

    public void add(final Date date, final T dt) {
    	timeBucket.put(date, dt) ;
    }

    public List<Date> dates() {
        return new ObjectArrayList<Date>(timeBucket.keySet());
    }
    
    public List<T> values() {
        return new ObjectArrayList<T>(timeBucket.values());
    }
    
    

//    @SuppressWarnings("unchecked")
//    public ObjectForwardIterator<Date> dates() /*@Readonly*/ { 
//        return Iterators.forwardIterator( ((ObjectArrayList<Date>)dates).elements() );
//    }
//    
//    @SuppressWarnings("unchecked")
//    public ObjectForwardIterator<T> values() /*@Readonly*/ { 
//        return Iterators.forwardIterator( ((ObjectArrayList<T>)values).elements() );
//    }
    

    
    //
    // implements Observable
    //
    
    /**
     * Implements multiple inheritance via delegate pattern to an inner class
     */
    private final Observable delegatedObservable = new DefaultObservable(this);

    @Override
    public void addObserver(final Observer observer) {
        delegatedObservable.addObserver(observer);
    }

    @Override
    public int countObservers() {
        return delegatedObservable.countObservers();
    }

    @Override
    public void deleteObserver(final Observer observer) {
        delegatedObservable.deleteObserver(observer);
    }

    @Override
    public void notifyObservers() {
        delegatedObservable.notifyObservers();
    }

    @Override
    public void notifyObservers(final Object arg) {
        delegatedObservable.notifyObservers(arg);
    }

    @Override
    public void deleteObservers() {
        delegatedObservable.deleteObservers();
    }

    @Override
    public List<Observer> getObservers() {
        return delegatedObservable.getObservers();
    }

    /**
     * The comparator
     */
    private static class DateComparator implements Comparator<Date> {

		@Override
        public int compare(Date o1, Date o2) {
	        return o1.compareTo(o2);
        }
    	
    }
}
