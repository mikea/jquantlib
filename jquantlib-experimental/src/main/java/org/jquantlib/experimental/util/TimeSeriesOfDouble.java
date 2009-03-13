package org.jquantlib.experimental.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.jquantlib.util.Date;
import org.jquantlib.util.TimeSeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

/**
 * 
 * @author gyg-quan
 * after
 * @see org.jquantlib.util.reflect.TimeSeriesDouble by Hasti
 */
public class TimeSeriesOfDouble {
    private final static Logger logger = LoggerFactory.getLogger(TimeSeriesOfDouble.class);

    //
    // private fields
    //
    // temporary hack lack of the map in the joda as a Map and DoubleArrayList
    // TODO: as soon as joda implements maps, use unified view
    
    private final NavigableMap<Date, Integer> map;
	private final ArrayList<Double> array;
    
    //
    // public constructors
    //
    
    public TimeSeriesOfDouble() {
        this.map = new TreeMap<Date, Integer>();
        array = new ArrayList<Double>();
    }

    public TimeSeriesOfDouble(final Date[] dates, final Double[] values) {
    	this();

    	if ( dates.length != values.length) {
            String msg = MessageFormatter.arrayFormat("size mismatch({}, {})", 
                    new Object[] { dates.length, values.length } );
            logger.debug(msg);
            throw new IllegalArgumentException(msg);
        }
        
        for (int i = 0; i < dates.length; i++) {
        	map.put(dates[i], i) ;
        	array.add(values[i]);
        }
    }

    public TimeSeriesOfDouble(final Date startingDate, final List<Double> values) {
    	this();
        Date tmp = startingDate;
        for (int i = 0; i < values.size(); i++) {
        	map.put(tmp, i) ;
        	array.add(values.get(i));
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

    public Double find(final Date d) /*@ReadOnly*/ {
    	return array.get(map.get(d));
    }

    public void add(final Date date, final Double dt) {
    	map.put(date, map.size()) ;
    	array.add(dt);
    }

    public Date[] dates() {
        return (Date[])map.keySet().toArray(new Date[0]);
    }
    
    public Collection<Double> values() {
        return array;
    }
    
}
