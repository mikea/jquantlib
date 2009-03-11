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

import java.util.NavigableMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Container for historical data
 * <p>
 * This class acts as a specialised repository for double historical data. 
 * Any single datum can be accessed through its date, while
 * sets of consecutive data can be accessed through iterators.
 * 
 * @see TimeSeries
 * 
 * @author Srinivas Hasti
 */
public class TimeSeriesDouble {

    private final static Logger logger = LoggerFactory.getLogger(TimeSeriesDouble.class);
	
    //
    // private fields
    //
    
    private final NavigableMap<Date, Double> map;

	
    //
    // public constructors
    //
    
    public TimeSeriesDouble() {
		this.map = new TreeMap<Date, Double>();
	}

	public TimeSeriesDouble(final Date[] dates, final double[] values) {
		this();
		
		if (dates.length != values.length) {
	        throw new IllegalArgumentException("sizes mismatch"); // TODO: message
		}
		
        for (int i = 0; i < dates.length; i++) {
			map.put(dates[i], values[i]);
		}
	}

	public TimeSeriesDouble(final Date startingDate, final double[] values) {
		this();
		Date tmp = startingDate;
		for (int i = 0; i < values.length; i++) {
			map.put(tmp, values[i]);
			tmp = startingDate.getDateAfter(i);
		}
	}

	
	//
	// public methods
	//
	
	/**
	 * @return the first date for which a historical datum exists
	 */
	public Date firstDate() /*@ReadOnly*/ {
		return map.firstKey();
	}

	/**
	 * @return the last date for which a historical datum exists
	 */
	public Date lastDate() /*@ReadOnly*/ {
		return map.lastKey();
	}

	/**
	 * @return the number of historical data including null ones
	 */
	public int size() /*@ReadOnly*/ {
		return map.size();
	}

	/**
	 * @return whether the series contains any data
	 */
	public boolean isEmpty() /*@ReadOnly*/ {
		return map.isEmpty();
	}

	public double find(final Date d) /*@ReadOnly*/ {
		return map.get(d);
	}

	public void add(final Date date, final double dt) {
		map.put(date, dt);
	}

	public Date[] dates() {
		return (Date[]) map.keySet().toArray(new Date[0]);
	}

	public double[] values() {
		Double[] tmp = (Double[]) map.values().toArray(new Double[0]);
		final double[] values = new double[tmp.length];
		for (int i=0; i<tmp.length; i++) {
		    values[i] = tmp[i];
		}
		return values;
	}

}
