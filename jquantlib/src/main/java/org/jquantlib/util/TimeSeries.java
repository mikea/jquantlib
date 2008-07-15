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

import it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectCollections;

import java.util.Collection;
import java.util.List;
import java.util.SortedMap;

/**
 * TODO: Class arranges the dates in order which is different from quantlib, make sure behavior is ok
 * 
 * @author Srinivas Hasti
 */
public class TimeSeries<T> implements Observable {

    private final SortedMap<Date, T> series = new Object2ObjectAVLTreeMap<Date, T>();

    public TimeSeries() {

    }

    public TimeSeries(final List<Date> dates, final List<T> values) {
        for (int i = 0; i < dates.size(); i++) {
            series.put(dates.get(i), values.get(i));
        }
    }

    public TimeSeries(final Date startingDate, final List<T> values) {
        Date tmp = startingDate;
        for (int i = 0; i < values.size(); i++) {
            series.put(tmp, values.get(i));
            tmp = startingDate.getDateAfter(i);
        }
    }

    /**
     * @return the first date for which a historical datum exists
     */
    public Date getFirstDate() /* @ReadOnly */ {
        return series.firstKey();
    }

    /**
     * @return the last date for which a historical datum exists
     */
    public Date lastDate() /* @ReadOnly */ {
        return series.lastKey();
    }

    /**
     * @return the number of historical data including null ones
     */
    public int size() /* @ReadOnly */ {
        return series.size();
    }

    /**
     * @return whether the series contains any data
     */
    public boolean isEmpty() /* @ReadOnly */ {
        return series.isEmpty();
    }

    public T find(final Date d) /* @ReadOnly */ {
        return series.get(d);
    }

    public Collection<T> values() /* @ReadOnly */ {
        return ObjectCollections.unmodifiable((ObjectCollection<T>)series.values());
    }

    @SuppressWarnings("unchecked")
    public Collection<Date> dates() /* @ReadOnly */ {
        return ObjectCollections.unmodifiable((ObjectCollection<Date>)series.keySet());
    }

    public void add(final Date date, final T dt) {
        series.put(date, dt);
    }


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

}
