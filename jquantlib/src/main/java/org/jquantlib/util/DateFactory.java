/*
 Copyright (C) 2007 Srinivas Hasti

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquantlib-dev@lists.sf.net>. The license is also available online at
 <http://jquantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the originating copyright notice follows below.
 */
package org.jquantlib.util;

import org.jquantlib.time.Weekday;

/**
 * DateUtil for getting at Date implementation. Different implementations
 * of Date can be plugged in using this utility. By default, JQuantLib implementation
 * is used.
 *
 * @author Srinivas Hasti
 * 
 */
public abstract class DateFactory {
    private static DateFactory dateUtil;
    private static DateFactory DEFAULT_DATE_UTIL = new DefaultDate.JQLibDateUtil();

    /**
     * Sets the dateUtil to be used
     * 
     * @param dateUtil
     */
    public static void setFactory(DateFactory dateUtil) {
        if(DateFactory.dateUtil != null)
            throw new RuntimeException("Dateutil already set ");
        DateFactory.dateUtil = dateUtil;
    }

    /**
     * To get the date util
     * 
     * @return
     */
    public static DateFactory getFactory() {
        if(dateUtil == null)
            return DEFAULT_DATE_UTIL;
        return dateUtil;
    }
    /**
     * Returns a instance that is the Maximum date that can be represented by
     * the Date implementation.
     * 
     * @return Maximum date represented by the implementation
     */
    public Date getMaxDate(){
        return dateUtil.getMaxDate();
    }
    
    /**
     * Returns a instance that is the Minimum date that can be represented by
     * the current Date implementation.
     * 
     * @return Minimum date represented by the implementation
     */
    public Date getMinDate(){
        return dateUtil.getMinDate();
    }
    
    /**
     * Returns todays date represented by the system
     * 
     * @return
     */
    public Date getTodaysDate(){
        return dateUtil.getTodaysDate();
    }
    
    /**
     * Returns a Date represented by parameters specified
     * 
     * @param day
     * @param month
     * @param year
     * @return
     */
    public Date getDate(int day, Month month, int year){
        return dateUtil.getDate(day, month, year);
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
    public Date getNthWeekday(int nth, Weekday dayOfWeek, Month month, int year){
        return dateUtil.getNthWeekday(nth, dayOfWeek, month, year);
    }
    
    
    /**
     * 
     * @param str
     * @return
     */
    public Date parseISO(String str){
        return dateUtil.parseISO(str);
    }
    
    /**
     * 
     * @param str
     * @param fmt
     * @return
     */
    public Date parse(String str, String fmt){
        return dateUtil.parse(str, fmt);
    }
    
    /**
     * Check whether given year is leap year or not
     * 
     * @param year
     * @return
     */
    public boolean isLeap(int year){
       return dateUtil.isLeap(year);   
    }
 
}
