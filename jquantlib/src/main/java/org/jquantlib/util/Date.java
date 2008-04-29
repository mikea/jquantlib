/*
 Copyright (C) 2008 Srinivas Hasti

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

import java.util.List;

import org.jquantlib.time.Period;
import org.jquantlib.time.Weekday;

/**
 * Date class to represent time in days.
 * 
 * @author Srinivas Hasti
 * 
 */
public interface Date extends Observable{

    /**
     * Returns Month of the year
     * 
     * @return a number which represents a month [1..12]
     */
    public int getMonth();

    /**
     * @see Month
     * @return the month as an <code>enum</code>
     */
    public Month getMonthEnum();

    /**
     * Returns year of the date
     * 
     * @return the year
     */
    public int getYear();

    /**
     * Increments the date by one day
     * 
     * @return <code>this</code> Date incremented
     */
    public Date increment();

    /**
     * Decrements the date by one day
     * 
     * @return <code>this</code> Date decremented
     */
    public Date decrement();

    /**
     * Increments the date by a given number of days
     * 
     * @param days
     *            is the quantity of days
     * @return <code>this</code> Date incremented by a given number of days
     */
    public Date increment(final int days);

    /**
     * Decrements the date by a given number of days
     * 
     * @param days
     *            is the quantity of days
     * @return <code>this</code> Date decremented by a given number of days
     */
    public Date decrement(final int days);

    /**
     * Move the date by a given <code>Period</code>
     * 
     * @param p
     *            is the Period
     * @return <code>this</code> Date moved by a given Period
     */
    public Date adjust(final Period p);

    /**
     * Tests if this Date belongs to a leap year
     * 
     * @return <code>true</code> if a leap year; <code>false</code>
     *         otherwise
     */
    public boolean isLeap();

    /**
     * Returns a new Date instance which contains the next week day matching a
     * parameter
     * 
     * @param dayOfWeek
     *            is the desired day of week
     * @return a new Date object which contains the next week day matching a
     *         parameter
     */
    public Date getNextWeekday(final Weekday dayOfWeek);

    /**
     * Returns a new Date instance representing next day from the current
     * instance
     * 
     * @return next day
     */
    public Date getNextDay();

    /**
     * Returns a new Date instance representing previous day from the current
     * instance
     * 
     * @return
     */
    public Date getPreviousDay();

    /**
     * Returns a new Date instance representing date in future or past by the
     * period specified.
     * 
     * @param p
     * @return new Date separated by specified period
     */
    public Date getDateAfter(Period p);

    /**
     * @return the week day of this Date as a <code>enum</code>
     */
    public Weekday getWeekday();

    /**
     * Returns day of the month. Number in range [1-31]
     * 
     * @return the day of month
     */
    public int getDayOfMonth();

    /**
     * Returns day of the year. Number in range [1-366]
     * 
     * @return the day of year
     */
    public int getDayOfYear();

    /**
     * Returns Date representing month end
     * 
     * @return a new Date which represents the last day of this month
     */
    public Date getEndOfMonth();

    /**
     * Tells if this date is at the end of month
     * 
     * @return <code>true</code> if this Date is at the end of month;
     *         <code>false</code> otherwise
     */
    public boolean isEndOfMonth();

    /**
     * Compares if <code>this</code> Date is less than to a given Date
     * 
     * @param date
     *            is the date to be compared against <code>this</code> Date
     * @return f <code>this</code> Date is less than to a given Date
     */
    public boolean lt(final Date date);

    /**
     * Compares if <code>this</code> Date is less than to a Date represented
     * by the parameters.
     * 
     * @param date
     *            is the date to be compared against <code>this</code> Date
     * @return f <code>this</code> Date is less than to a given Date
     */
    public boolean lt(int day, Month month, int year);

    /**
     * Compares if <code>this</code> Date is less than or equal to a given
     * Date
     * 
     * @param date
     *            is the date to be compared against <code>this</code> Date
     * @return f <code>this</code> Date is less than or equal to a given Date
     */
    public boolean le(final Date date);

    /**
     * Compares if <code>this</code> Date is less than or equal to a Date
     * represented by the parameters.
     * 
     * @param date
     *            is the date to be compared against <code>this</code> Date
     * @return f <code>this</code> Date is less than or equal to a given Date
     */
    public boolean le(int day, Month month, int year);

    /**
     * Compares if <code>this</code> Date is greater than to a given Date
     * 
     * @param date
     *            is the date to be compared against <code>this</code> Date
     * @return f <code>this</code> Date is greater than to a given Date
     */
    public boolean gt(final Date date);

    /**
     * Compares if <code>this</code> Date is greater than to a Date
     * represented by the parameters
     * 
     * @param date
     *            is the date to be compared against <code>this</code> Date
     * @return f <code>this</code> Date is greater than to a given Date
     */
    public boolean gt(int day, Month month, int year);

    /**
     * Compares if <code>this</code> Date is greater than or equal to a given
     * Date
     * 
     * @param date
     *            is the date to be compared against <code>this</code> Date
     * @return f <code>this</code> Date is greater than or equal to a given
     *         Date
     */
    public boolean ge(final Date date);

    /**
     * Compares if <code>this</code> Date is greater than or equal to a Date
     * represented by the parameters
     * 
     * @param date
     *            is the date to be compared against <code>this</code> Date
     * @return f <code>this</code> Date is greater than or equal to a given
     *         Date
     */
    public boolean ge(int day, Month month, int year);

    /**
     * Compares if <code>this</code> Date is equal to a Date represented by
     * the parameters
     * 
     * @param date
     *            is the date to be compared against <code>this</code> Date
     * @return f <code>this</code> Date is equal to a given Date
     */
    public boolean eq(int day, Month month, int year);
    

    /**
     * Returns the String representing this Date in a long format. This is the
     * same format as returned by toString method
     * 
     * @return the String representing this Date in a long format
     * @see Date#toString
     */
    public String getLongFormat();

    /**
     * Returns the String representing this Date in a short format
     * 
     * @return the String representing this Date in a short format
     */
    public String getShortFormat();

    /**
     * Returns the String representing this Date in ISO format
     * 
     * @return the String representing this Date in ISO format
     */
    public String getISOFormat();

    /**
     * To determine number of days to/from another Date. If specified date is in
     * the past, negative value is returned. If specified date is in future,
     * positive value is returned. 0 is returned if specified date represents
     * same as current date.
     * 
     * @param date
     * @return negative value if argument is in future, 0 for same Date,
     *         positive for a Date in future
     */
    public int getDayCount(Date date);

    /**
     * Returns a new Date instance that is n days after or before the current
     * date. If n is <0 date returned is in the past and if n > 0 date returned
     * is in the future. When n =0, Date returned is equal to current instance.
     * 
     * @param i
     * @return
     */
    public Date getDateAfter(int n);
    
    /**
     * Returns Updatable reference that can be used to replace with new value.
     * 
     * @return
     */
    public Updatable<Date> getUpdatable();

    /**
     * To use it in place of non initialized date object reference
     */
    public Date NULL_DATE = new Date() {
        public Date decrement() {
            throw new RuntimeException("Operation not supported on NULL Date");
        }

        public Date decrement(int days) {
            throw new RuntimeException("Operation not supported on NULL Date");
        }

        public boolean ge(Date date) {
            throw new RuntimeException("Operation not supported on NULL Date");
        }

        public int getDayCount(Date date) {
            throw new RuntimeException("Operation not supported on NULL Date");
        }

        public int getDayOfMonth() {
            throw new RuntimeException("Operation not supported on NULL Date");
        }

        public int getDayOfYear() {
            throw new RuntimeException("Operation not supported on NULL Date");
        }

        public Date getEndOfMonth() {
            throw new RuntimeException("Operation not supported on NULL Date");
        }

        public String getISOFormat() {
            return "NULL Date";
        }

        public String getLongFormat() {
            return "NULL Date";
        }

        public int getMonth() {
            throw new RuntimeException("Operation not supported on NULL Date");
        }

        public Month getMonthEnum() {
            throw new RuntimeException("Operation not supported on NULL Date");
        }

        public Date getNextWeekday(Weekday dayOfWeek) {
            throw new RuntimeException("Operation not supported on NULL Date");
        }

        public String getShortFormat() {
            return "NULL Date";
        }

        public Weekday getWeekday() {
            throw new RuntimeException("Operation not supported on NULL Date");
        }

        public int getYear() {
            throw new RuntimeException("Operation not supported on NULL Date");
        }

        public boolean gt(Date date) {
            throw new RuntimeException("Operation not supported on NULL Date");
        }

        public Date increment() {
            throw new RuntimeException("Operation not supported on NULL Date");
        }

        public Date increment(int days) {
            throw new RuntimeException("Operation not supported on NULL Date");
        }

        public Date adjust(Period p) {
            throw new RuntimeException("Operation not supported on NULL Date");
        }

        public boolean isEndOfMonth() {
            throw new RuntimeException("Operation not supported on NULL Date");
        }

        public boolean isLeap() {
            throw new RuntimeException("Operation not supported on NULL Date");
        }

        public boolean le(Date date) {
            throw new RuntimeException("Operation not supported on NULL Date");
        }

        public boolean lt(Date date) {
            throw new RuntimeException("Operation not supported on NULL Date");
        }

        public Date getDateAfter(Period p) {
            throw new RuntimeException("Operation not supported on NULL Date");
        }

        public Date getNextDay() {
            throw new RuntimeException("Operation not supported on NULL Date");
        }

        public Date getPreviousDay() {
            throw new RuntimeException("Operation not supported on NULL Date");
        }

        public boolean eq(int day, Month month, int year) {
            throw new RuntimeException("Operation not supported on NULL Date");
        }

        public boolean ge(int day, Month month, int year) {
            throw new RuntimeException("Operation not supported on NULL Date");
        }

        public boolean gt(int day, Month month, int year) {
            throw new RuntimeException("Operation not supported on NULL Date");
        }

        public boolean le(int day, Month month, int year) {
            throw new RuntimeException("Operation not supported on NULL Date");
        }

        public boolean lt(int day, Month month, int year) {
            throw new RuntimeException("Operation not supported on NULL Date");
        }

        public void addObserver(Observer observer) {
            throw new RuntimeException("Operation not supported on NULL Date");
        }

        public int countObservers() {
            throw new RuntimeException("Operation not supported on NULL Date");
        }

        public void deleteObserver(Observer observer) {
            throw new RuntimeException("Operation not supported on NULL Date");
        }

        public void deleteObservers() {
            throw new RuntimeException("Operation not supported on NULL Date");
        }

        public List<Observer> getObservers() {
            throw new RuntimeException("Operation not supported on NULL Date");
        }

        public void notifyObservers() {
            throw new RuntimeException("Operation not supported on NULL Date");
        }

        public void notifyObservers(Object arg) {
            throw new RuntimeException("Operation not supported on NULL Date");
        }

        public Date getDateAfter(int n) {
            throw new RuntimeException("Operation not supported on NULL Date");
        }

        public Updatable<Date> getUpdatable() {
            throw new RuntimeException("Operation not supported on NULL Date");
        }

    };

}