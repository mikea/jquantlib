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

import java.util.Collection;
import java.util.List;
import java.util.SortedMap;


/**
 * @author Srinivas Hasti
 */

//FIXME: PERFORMANCE:: We should use (maybe!) specialized versions of TimeSeries backed by primitive types
public class TimeSeries<T> {

	private final SortedMap<Date, T> map;
	
    public TimeSeries() {
        this.map = new Object2ObjectAVLTreeMap<Date, T>();
    }

    public TimeSeries(final List<Date> dates, final List<T> values) {
    	this();
        for (int i = 0; i < dates.size(); i++) {
        	map.put(dates.get(i), values.get(i)) ;
        }
    }

    public TimeSeries(final Date startingDate, final List<T> values) {
    	this();
        Date tmp = startingDate;
        for (int i = 0; i < values.size(); i++) {
        	map.put(tmp, values.get(i)) ;
            tmp = startingDate.getDateAfter(i);
        }
    }

    /**
     * @return the first date for which a historical datum exists
     */
    public Date firstDate() /* @ReadOnly */ {
    	return map.firstKey();
    }

    /**
     * @return the last date for which a historical datum exists
     */
    public Date lastDate() /* @ReadOnly */ {
    	return map.lastKey() ;
    }

    /**
     * @return the number of historical data including null ones
     */
    public int size() /* @ReadOnly */ {
        return map.size();
    }

    /**
     * @return whether the series contains any data
     */
    public boolean isEmpty() /* @ReadOnly */ {
        return map.isEmpty();
    }

    public T find(final Date d) /* @ReadOnly */ {
    	return map.get(d);
    }

    public void add(final Date date, final T dt) {
    	map.put(date, dt) ;
    }

    public Date[] dates() {
        return (Date[])map.keySet().toArray(new Date[0]);
    }
    
    public Collection<T> values() {
        return map.values();
    }
    
}
