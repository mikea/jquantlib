/*
 Copyright (C) 2008 Srinivas Hasti

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

import org.jquantlib.QL;
import org.jquantlib.time.Weekday;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * DateUtil for getting at Date implementation. Different implementations
 * of Date can be plugged in using this utility. By default, JQuantLib implementation
 * is used.
 *
 * @author Srinivas Hasti
 *
 */
//TODO: OSGi
public abstract class DateFactory {

    //
    // logger
    //
    private final static Logger logger = LoggerFactory.getLogger(DateFactory.class);

    private static DateFactory dateFactory;
    private static final DateFactory DEFAULT_DATE_UTIL = new DefaultDate.JQLibDateUtil();

    /**
     * Sets the dateUtil to be used
     *
     * @param dateUtil
     */
    public static void setFactory(final DateFactory dateUtil) {
        QL.require(DateFactory.dateFactory == null , "Dateutil already set "); // QA:[RG]::verified // TODO: message
        DateFactory.dateFactory = dateUtil;
    }

    /**
     * To get the date util
     *
     * @return
     */
    public static DateFactory getFactory() {
        if (dateFactory == null)
            return DEFAULT_DATE_UTIL;
        return dateFactory;
    }
    /**
     * Returns a instance that is the Maximum date that can be represented by
     * the Date implementation.
     *
     * @return Maximum date represented by the implementation
     */
    public Date getMaxDate(){
        return dateFactory.getMaxDate();
    }

    /**
     * Returns a instance that is the Minimum date that can be represented by
     * the current Date implementation.
     *
     * @return Minimum date represented by the implementation
     */
    public Date getMinDate(){
        return dateFactory.getMinDate();
    }

    /**
     * Returns todays date represented by the system
     *
     * @return
     */
    public Date getTodaysDate(){
        return dateFactory.getTodaysDate();
    }

    /**
     * Returns a Date represented by parameters specified
     *
     * @param day
     * @param month
     * @param year
     * @return
     */
    public Date getDate(final int day, final Month month, final int year){
        return dateFactory.getDate(day, month, year);
    }

    /**
     * Returns a Date represented by parameters specified
     *
     * @param day
     * @param month
     * @param year
     * @return
     */
    public Date getDate(final int day, final int month, final int year){
        return dateFactory.getDate(day, month, year);
    }

    /**
     * Returns a new Date which is the n-th week day of a certain month/year
     *
     * @param nth
     *            is the desired week
     * @param dayOfWeek
     *            is the desired week day
     * @param month
     *            is the desired month
     * @param year
     *            is the desired year
     * @return a new Date which is the n-th week day of a certain month/year
     */
    public Date getNthWeekday(final int nth, final Weekday dayOfWeek, final Month month, final int year){
        return dateFactory.getNthWeekday(nth, dayOfWeek, month, year);
    }


    /**
     *
     * @param str
     * @return
     */
    public Date parseISO(final String str){
        return dateFactory.parseISO(str);
    }

    /**
     *
     * @param str
     * @param fmt
     * @return
     */
    public Date parse(final String str, final String fmt){
        return dateFactory.parse(str, fmt);
    }

    /**
     * Check whether given year is leap year or not
     *
     * @param year
     * @return
     */
    public boolean isLeap(final int year){
        return dateFactory.isLeap(year);
    }

}
