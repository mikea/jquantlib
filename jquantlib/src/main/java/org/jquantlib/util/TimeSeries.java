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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.joda.primitives.list.impl.ArrayDoubleList;
import org.jquantlib.lang.reflect.TypeTokenTree;
import org.jquantlib.math.IntervalPrice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

/**
 * Container for historical data
 * <p>
 * This class acts as a generic repository for a set of historical data. 
 * Any single datum can be accessed through its date, while
 * sets of consecutive data can be accessed through iterators.
 * 
 * @see TimeSeriesDouble
 * 
 * @author Srinivas Hasti
 */
public class TimeSeries<T> {

    private final static Logger logger = LoggerFactory.getLogger(TimeSeries.class);
    
    private final static String USE_ADD_INSTEAD             = "add(Date, IntervalPrice) expected";
    private final static String USE_FIND_INSTEAD            = "find(Date) expected";
    private final static String USE_VALUES_INSTEAD          = "values() expected";
    private final static String USE_ADDDOUBLE_INSTEAD       = "addDouble(Date, double) expected";
    private final static String USE_FINDDOUBLE_INSTEAD      = "findDouble(Date) expected";
    private final static String USE_VALUESASDOUBLES_INSTEAD = "valuesAsDoubles() expected";

    
    //
    // private fields
    //
    
    private final Series<T> delegate;
    private final Class klass;
    
    
    //
    // public constructors
    //
    
    public TimeSeries() {
        this.klass = new TypeTokenTree(this.getClass()).getRoot().get(0).getElement();
        if (Double.class.isAssignableFrom(klass)) {
            this.delegate = new SeriesDouble<T>();
        } else if (IntervalPrice.class.isAssignableFrom(klass)) {
            this.delegate = new SeriesIntervalPrice<T>();
        } else {
            throw new UnsupportedOperationException("only Double and IntervalPrice are supported");
        }
    }

    public TimeSeries(final Date[] dates, final T[] values) {
        this();
    	if ( dates.length != values.length) {
            String msg = MessageFormatter.arrayFormat("size mismatch({}, {})", new Object[] { dates.length, values.length } );
            throw new IllegalArgumentException(msg);
        }

        for (int i = 0; i < dates.length; i++) {
            if (Double.class.isAssignableFrom(klass)) {
                throw new ClassCastException("generic type IntervalPrice expected");
            } else if (IntervalPrice.class.isAssignableFrom(klass)) {
                this.delegate.add(dates[i], values[i]) ;
            }
        }
    }

    public TimeSeries(final Date[] dates, final double[] values) {
        this();
        if ( dates.length != values.length) {
            String msg = MessageFormatter.arrayFormat("size mismatch({}, {})", new Object[] { dates.length, values.length } );
            throw new IllegalArgumentException(msg);
        }

        for (int i = 0; i < dates.length; i++) {
            if (Double.class.isAssignableFrom(klass)) {
                this.delegate.addDouble(dates[i], values[i]) ;
            } else if (IntervalPrice.class.isAssignableFrom(klass)) {
                throw new ClassCastException("generic type Double expected");
            }
        }
    }

    public TimeSeries(final Date startingDate, final List<T> values) {
    	this();
        Date tmp = startingDate;

        for (int i = 0; i < values.size(); i++) {
            if (Double.class.isAssignableFrom(klass)) {
                throw new ClassCastException("generic type IntervalPrice expected");
            } else if (IntervalPrice.class.isAssignableFrom(klass)) {
                this.delegate.add(tmp, values.get(i)) ;
                tmp = startingDate.getDateAfter(i);
            }
        }
    }

    
    //
    // public methods
    //
    
    /**
     * @return the first date for which a historical datum exists
     */
    public Date firstDate() /*@ReadOnly*/ {
        return delegate.firstDate();
    }

    /**
     * @return the last date for which a historical datum exists
     */
    public Date lastDate() /*@ReadOnly*/ {
        return delegate.lastDate();
    }

    /**
     * @return the number of historical data including null ones
     */
    public int size() /*@ReadOnly*/ {
        return delegate.size();
    }

    /**
     * @return whether the series contains any data
     */
    public boolean isEmpty() /*@ReadOnly*/ {
        return delegate.isEmpty();
    }

    public Date[] dates() {
        return delegate.dates();
    }
    
    public void add(final Date date, final T dt) {
        delegate.add(date, dt) ;
    }

    public void addDouble(final Date date, final double dt) {
        delegate.addDouble(date, dt) ;
    }

    public T find(final Date d) /*@ReadOnly*/ {
    	return delegate.find(d);
    }

    public double findDouble(final Date d) /*@ReadOnly*/ {
        return delegate.findDouble(d);
    }

    public Collection<T> values() {
        return delegate.values();
    }
    
    public double[] valuesAsDoubles() {
        return delegate.valuesAsDoubles();
    }
    

    //
    // public interfaces
    //
    
    public interface Series<T> {
        
        /**
         * @return the first date for which a historical datum exists
         */
        public Date firstDate() /*@ReadOnly*/;

        /**
         * @return the last date for which a historical datum exists
         */
        public Date lastDate() /*@ReadOnly*/;

        /**
         * @return the number of historical data including null ones
         */
        public int size() /*@ReadOnly*/;

        /**
         * @return whether the series contains any data
         */
        public boolean isEmpty() /*@ReadOnly*/;

        /**
         * @return a list of dates stored in this historical series
         */
        public Date[] dates();
        
        /**
         * Adds a single data to this series
         */
        public void add(final Date date, final T dt);
        
        /**
         * Adds a single double to this series
         */
        public void addDouble(final Date date, final double dt);
        
        /**
         * @param d is a {@link Date} we are interested on
         * @return the value associated to a {@link Date} informed
         */
        public T find(final Date d) /*@ReadOnly*/;
        
        /**
         * @param d is a {@link Date} we are interested on
         * @return the double value associated to a {@link Date} informed
         */
        public double findDouble(final Date d) /*@ReadOnly*/;
        
        /**
         * @return the full {@link Collection} of values stored
         */
        public Collection<T> values();
        
        /**
         * @return a double[] stored
         */
        public double[] valuesAsDoubles();
    }
    
    
    //
    // private inner classes
    //

    /**
     * This TimeSeries implementation is a specialised to work with {@link IntervalPrice} 
     */
    private class SeriesIntervalPrice<T> implements Series<T> {
        
        private final NavigableMap<Date, T> map;
        
        public SeriesIntervalPrice() {
            this.map = new TreeMap<Date, T>();
        }
        
        
        //
        // public methods
        //
        
        public Date firstDate() /*@ReadOnly*/ {
            return map.firstKey();
        }

        public Date lastDate() /*@ReadOnly*/ {
            return map.lastKey();
        }

        public int size() /*@ReadOnly*/ {
            return map.size();
        }

        public boolean isEmpty() /*@ReadOnly*/ {
            return map.isEmpty();
        }

        public Date[] dates() {
            return (Date[])map.keySet().toArray(new Date[0]);
        }
        
        public void add(final Date date, final T dt) {
            map.put(date, dt) ;
        }

        public T find(final Date d) /*@ReadOnly*/ {
            return map.get(d);
        }

        public Collection<T> values() {
            return map.values();
        }
        
        public void addDouble(final Date date, final double dt) {
            throw new UnsupportedOperationException(USE_ADD_INSTEAD);
        }

        public double findDouble(final Date d) /*@ReadOnly*/ {
            throw new UnsupportedOperationException(USE_FIND_INSTEAD);
        }

        public double[] valuesAsDoubles() {
            throw new UnsupportedOperationException(USE_VALUES_INSTEAD);
        }
        
    }
    

    /**
     * This TimeSeries implementation is specialised to work with a single price value 
     */
    private class SeriesDouble<T> implements Series<T> {
        
        
        // Ideally, we would like to use a NavigableMap here but joda-primitives does not offer maps
        //
        // private final NavigableMap<Date, Double> map;
        private final List<Date>   dates;
        private final List<Double> values;
        
        public SeriesDouble() {
            //this.map = new TreeMap<Date, T>();
            this.dates  = new ArrayList<Date>();
            this.values = new ArrayDoubleList();
        }
        
        
        //
        // public methods
        //
        
        public Date firstDate() /*@ReadOnly*/ {
            //return map.firstKey();
            return dates.get(0);
        }

        public Date lastDate() /*@ReadOnly*/ {
            //return map.lastKey();
            return dates.get(dates.size()-1);
        }

        public int size() /*@ReadOnly*/ {
            //return map.size();
            return dates.size();
        }

        public boolean isEmpty() /*@ReadOnly*/ {
            //return map.isEmpty();
            return dates.isEmpty();
        }

        public Date[] dates() {
            //return (Date[])map.keySet().toArray(new Date[0]);
            return dates.toArray(new Date[dates.size()]);
        }
        
        public void add(final Date date, final T dt) {
            throw new UnsupportedOperationException(USE_ADDDOUBLE_INSTEAD);
        }

        public T find(final Date d) /*@ReadOnly*/ {
            throw new UnsupportedOperationException(USE_FINDDOUBLE_INSTEAD);
        }

        public Collection<T> values() {
            throw new UnsupportedOperationException(USE_VALUESASDOUBLES_INSTEAD);
        }
        
        public void addDouble(final Date date, final double dt) {
            //map.put(date, dt) ;
            dates.add(date) ;
            ((ArrayDoubleList)values).add(dt);
        }

        public double findDouble(final Date d) /*@ReadOnly*/ {
            //return map.get(d);
            int index = dates.indexOf(d);
            return ((ArrayDoubleList)values).getDouble(index);
        }

        public double[] valuesAsDoubles() {
            //return map.values();
            return ((ArrayDoubleList)values).toDoubleArray();
        }
        
    }
    
}
