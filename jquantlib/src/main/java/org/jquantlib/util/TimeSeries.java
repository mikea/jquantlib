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
import org.jquantlib.QL;
import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.lang.reflect.TypeTokenTree;
import org.jquantlib.math.IntervalPrice;

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
 * @author Richard Gomes
 */
public class TimeSeries<T> {

    //
    // private fields
    //

    private final Series delegate;


    //
    // public constructors
    //

    public TimeSeries() {
        final Class<?> klass = new TypeTokenTree(this.getClass()).getRoot().get(0).getElement();
        if (Double.class.isAssignableFrom(klass)) {
            this.delegate = new TimeSeriesDouble();
        } else if (IntervalPrice.class.isAssignableFrom(klass)) {
            this.delegate = new TimeSeriesIntervalPrice();
        } else {
            throw new LibraryException("only Double and IntervalPrice are supported"); // QA:[RG]::verified // TODO: message
        }
    }

    public TimeSeries(final Date[] dates, final double[] values) {
        final Class<?> klass = new TypeTokenTree(this.getClass()).getRoot().get(0).getElement();
        if (Double.class.isAssignableFrom(klass)) {
            this.delegate = new TimeSeriesDouble(dates, values);
        } else {
            throw new LibraryException("only double[] is supported"); // QA:[RG]::verified // TODO: message
        }
    }

    public TimeSeries(final Date[] dates, final Double[] values) {
        final Class<?> klass = new TypeTokenTree(this.getClass()).getRoot().get(0).getElement();
        if (Double.class.isAssignableFrom(klass)) {
            this.delegate = new TimeSeriesDouble(dates, values);
        } else {
            throw new LibraryException("only Double[] is supported"); // QA:[RG]::verified // TODO: message
        }
    }

    public TimeSeries(final Date[] dates, final IntervalPrice[] values) {
        final Class<?> klass = new TypeTokenTree(this.getClass()).getRoot().get(0).getElement();
        if (IntervalPrice.class.isAssignableFrom(klass)) {
            this.delegate = new TimeSeriesIntervalPrice(dates, values);
        } else {
            throw new LibraryException("only IntervalPrice[] is supported"); // QA:[RG]::verified // TODO: message
        }
    }

    public TimeSeries(final Date startingDate, final List<T> values) {
        final Class<?> klass = new TypeTokenTree(this.getClass()).getRoot().get(0).getElement();
        if (Double.class.isAssignableFrom(klass)) {
            this.delegate = new TimeSeriesDouble(startingDate, (List<Double>)values);
        } else if (IntervalPrice.class.isAssignableFrom(klass)) {
            this.delegate = new TimeSeriesIntervalPrice(startingDate, (List<IntervalPrice>)values);
        } else {
            throw new LibraryException("only List<Double> and List<IntervalPrice> are supported"); // QA:[RG]::verified // TODO: message
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

    public void add(final Date date, final double dt) {
        ((TimeSeriesDouble)delegate).add(date, dt) ;
    }

    public void add(final Date date, final IntervalPrice dt) {
        ((TimeSeriesIntervalPrice)delegate).addIntervalPrice(date, dt) ;
    }

    public double find(final Date d) /*@ReadOnly*/ {
        return ((TimeSeriesDouble)delegate).find(d);
    }

    public IntervalPrice findIntervalPrice(final Date d) /*@ReadOnly*/ {
        return ((TimeSeriesIntervalPrice)delegate).findIntervalPrice(d);
    }

    public double[] values() {
        return ((TimeSeriesDouble)delegate).values();
    }

    public Collection<IntervalPrice> valuesIntervalPrice() {
        return ((TimeSeriesIntervalPrice)delegate).valuesIntervalPrice();
    }


    //
    // private interfaces
    //

    private interface Series {

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

    }

    private interface SeriesDouble extends Series {

        /**
         * Adds a single double to this series
         */
        public void add(final Date date, final double dt);

        /**
         * @param d is a {@link Date} we are interested on
         * @return the double value associated to a {@link Date} informed
         */
        public double find(final Date d) /*@ReadOnly*/;

        /**
         * @return a double[] stored
         */
        public double[] values();

    }

    private interface SeriesIntervalPrice extends Series {

        /**
         * Adds a single data to this series
         */
        public void addIntervalPrice(final Date date, final IntervalPrice dt);

        /**
         * @param d is a {@link Date} we are interested on
         * @return the value associated to a {@link Date} informed
         */
        public IntervalPrice findIntervalPrice(final Date d) /*@ReadOnly*/;

        /**
         * @return the full {@link Collection} of values stored
         */
        public Collection<IntervalPrice> valuesIntervalPrice();

    }


    //
    // private inner classes
    //

    /**
     * This TimeSeries implementation is a specialised to work with {@link IntervalPrice}
     */
    private static class TimeSeriesIntervalPrice implements SeriesIntervalPrice {

        private final NavigableMap<Date, IntervalPrice> map;


        public TimeSeriesIntervalPrice() {
            this.map = new TreeMap<Date, IntervalPrice>();
        }

        public TimeSeriesIntervalPrice(final Date[] dates, final IntervalPrice[] values) {
            this();
            QL.require(dates.length == values.length , "sizes mismatch"); // QA:[RG]::verified // TODO: message
            for (int i = 0; i < dates.length; i++) {
                this.addIntervalPrice(dates[i], values[i]) ;
            }
        }

        public TimeSeriesIntervalPrice(final Date startingDate, final List<IntervalPrice> values) {
            this();
            Date tmp = startingDate;
            for (int i = 0; i < values.size();) {
                this.addIntervalPrice(tmp, values.get(i++)) ;
                tmp = startingDate.add(i);
            }
        }

        @Override
        public Date firstDate() /*@ReadOnly*/ {
            return map.firstKey();
        }

        @Override
        public Date lastDate() /*@ReadOnly*/ {
            return map.lastKey();
        }

        @Override
        public int size() /*@ReadOnly*/ {
            return map.size();
        }

        @Override
        public boolean isEmpty() /*@ReadOnly*/ {
            return map.isEmpty();
        }

        @Override
        public Date[] dates() {
            return map.keySet().toArray(new Date[0]);
        }

        @Override
        public void addIntervalPrice(final Date date, final IntervalPrice dt) {
            map.put(date, dt) ;
        }

        @Override
        public IntervalPrice findIntervalPrice(final Date d) /*@ReadOnly*/ {
            return map.get(d);
        }

        @Override
        public Collection<IntervalPrice> valuesIntervalPrice() {
            return map.values();
        }

    }


    /**
     * This TimeSeries implementation is specialised to work with a single price value
     */
    private static class TimeSeriesDouble implements SeriesDouble {

        //XXX: private final NavigableMap<Date, Double> map;
        private final List<Date>   dates;
        private final ArrayDoubleList values;

        //
        // public constructors
        //

        public TimeSeriesDouble() {
            //XXX: this.map = new TreeMap<Date, T>();
            this.dates  = new ArrayList<Date>();
            this.values = new ArrayDoubleList();
        }

        public TimeSeriesDouble(final Date[] dates, final double[] values) {
            this();
            QL.require(dates.length == values.length , "sizes mismatch"); // QA:[RG]::verified // TODO: message
            for (int i = 0; i < dates.length; i++) {
                this.add(dates[i], values[i]) ;
            }
        }

        public TimeSeriesDouble(final Date[] dates, final Double[] values) {
            this();
            QL.require(dates.length == values.length , "sizes mismatch"); // QA:[RG]::verified // TODO: message
            for (int i = 0; i < dates.length; i++) {
                this.add(dates[i], values[i]) ;
            }
        }

        public TimeSeriesDouble(final Date startingDate, final List<Double> values) {
            this();
            Date tmp = startingDate;
            for (int i = 0; i < values.size();) {
                add(tmp, values.get(i++)) ;
                tmp = startingDate.add(i);
            }
        }

        @Override
        public Date firstDate() /*@ReadOnly*/ {
            //XXX: return map.firstKey();
            return dates.get(0);
        }

        @Override
        public Date lastDate() /*@ReadOnly*/ {
            //XXX: return map.lastKey();
            return dates.get(dates.size()-1);
        }

        @Override
        public int size() /*@ReadOnly*/ {
            //XXX: return map.size();
            return dates.size();
        }

        public boolean isEmpty() /*@ReadOnly*/ {
            //XXX: return map.isEmpty();
            return dates.isEmpty();
        }

        @Override
        public Date[] dates() {
            //XXX: return (Date[])map.keySet().toArray(new Date[0]);
            return dates.toArray(new Date[dates.size()]);
        }

        @Override
        public void add(final Date date, final double dt) {
            //XXX: map.put(date, dt) ;
            dates.add(date) ;
            values.add(dt);
        }

        @Override
        public double find(final Date d) /*@ReadOnly*/ {
            //XXX: return map.get(d);
            final int index = dates.indexOf(d);
            return values.getDouble(index);
        }

        @Override
        public double[] values() {
            //XXX: return map.values();
            return values.toDoubleArray();
        }
    }

}
