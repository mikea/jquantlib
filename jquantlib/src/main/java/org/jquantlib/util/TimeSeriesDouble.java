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

import it.unimi.dsi.fastutil.objects.Object2DoubleAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleSortedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

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
    
    private final Object2DoubleSortedMap<Date> map;

	
    //
    // public constructors
    //
    
    public TimeSeriesDouble() {
		this.map = new Object2DoubleAVLTreeMap<Date>();
	}

	public TimeSeriesDouble(final Date[] dates, final double[] values) {
		this();
		
		if ( dates.length != values.length) {
	        String msg = MessageFormatter.arrayFormat("size mismatch({}, {})", 
	                new Object[] { dates.length, values.length } );
	        logger.debug(msg);
	        throw new IllegalArgumentException(msg);
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
		return map.values().toDoubleArray();
	}

}
