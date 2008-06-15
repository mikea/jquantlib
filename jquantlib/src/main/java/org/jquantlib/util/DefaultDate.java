/*
 Copyright (C) 2007 Richard Gomes

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquant-devel@lists.sourceforge.net>. The license is also available online at
 <http://www.jquantlib.org/index.php/LICENSE.TXT>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */

/*
 Copyright (C) 2000, 2001, 2002, 2003 RiskMap srl
 Copyright (C) 2003, 2004, 2005, 2006 StatPro Italia srl
 Copyright (C) 2004, 2005, 2006 Ferdinando Ametrano
 Copyright (C) 2006 Katiuscia Manzoni
 Copyright (C) 2004 Toyin Akin

 This file is part of QuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://quantlib.org/

 QuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <quantlib-dev@lists.sf.net>. The license is also available online at
 <http://quantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 */

package org.jquantlib.util;

import java.util.Formatter;
import java.util.Locale;

import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.time.Weekday;

/**
 * Date and time related classes, typedefs and enumerations
 * 
 * <p>
 * This class provides methods to inspect dates as well as methods and operators
 * which implement a limited date algebra (increasing and decreasing dates, and
 * calculating their difference).
 * 
 * @note This class extends Observable and is potentially harmful to performance
 *       due to the large number of Date objects that can be observable at the
 *       same time on some families of applications. A simple solution for this
 *       problem could be the implementation of an interface which enables this
 *       object to turn on and turn off the notification of its observers.
 * 
 * @see Observable
 * 
 * @author Richard Gomes
 * @author Srinivas Hasti
 */
//TODO: OSGi
public class DefaultDate extends BaseDate {

    private/* @NonNegative */int value;

    static private final int MinimumSerialNumber = 367; // Jan 1st, 1901
    static private final int MaximumSerialNumber = 73050; // Dec 31st, 2099
    //static private final JQLibDate maximumSerialNumber = new JQLibDate(MaximumSerialNumber);
    //static private final JQLibDate minimumSerialNumber = new JQLibDate(MinimumSerialNumber);

    static final int monthLength[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

    static private final int monthLeapLength[] = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

    static private final int monthOffset[] = {
    		0, 31, 59, 90, 120, 151,		// Jan - Jun
            181, 212, 243, 273, 304, 334,	// Jun - Dec
            365 // used in dayOfMonth to bracket day
    };

    static private final int monthLeapOffset[] = {
    		0, 31, 60, 91, 121, 152, 		// Jan - Jun
            182, 213, 244, 274, 305, 335,	// Jun - Dec
            366 // used in dayOfMonth to bracket day
    };

    // the list of all December 31st in the preceding year
    // e.g. for 1901 yearOffset[1] is 366, that is, December 31 1900
    static private final int yearOffset[] = {
    		// 1900-1909
            0, 366, 731, 1096, 1461, 1827, 2192, 2557, 2922, 3288,
            // 1910-1919
            3653, 4018, 4383, 4749, 5114, 5479, 5844, 6210, 6575, 6940,
            // 1920-1929
            7305, 7671, 8036, 8401, 8766, 9132, 9497, 9862, 10227, 10593,
            // 1930-1939
            10958, 11323, 11688, 12054, 12419, 12784, 13149, 13515, 13880, 14245,
            // 1940-1949
            14610, 14976, 15341, 15706, 16071, 16437, 16802, 17167, 17532, 17898,
            // 1950-1959
            18263, 18628, 18993, 19359, 19724, 20089, 20454, 20820, 21185, 21550,
            // 1960-1969
            21915, 22281, 22646, 23011, 23376, 23742, 24107, 24472, 24837, 25203,
            // 1970-1979
            25568, 25933, 26298, 26664, 27029, 27394, 27759, 28125, 28490, 28855,
            // 1980-1989
            29220, 29586, 29951, 30316, 30681, 31047, 31412, 31777, 32142, 32508,
            // 1990-1999
            32873, 33238, 33603, 33969, 34334, 34699, 35064, 35430, 35795, 36160,
            // 2000-2009
            36525, 36891, 37256, 37621, 37986, 38352, 38717, 39082, 39447, 39813,
            // 2010-2019
            40178, 40543, 40908, 41274, 41639, 42004, 42369, 42735, 43100, 43465,
            // 2020-2029
            43830, 44196, 44561, 44926, 45291, 45657, 46022, 46387, 46752, 47118,
            // 2030-2039
            47483, 47848, 48213, 48579, 48944, 49309, 49674, 50040, 50405, 50770,
            // 2040-2049
            51135, 51501, 51866, 52231, 52596, 52962, 53327, 53692, 54057, 54423,
            // 2050-2059
            54788, 55153, 55518, 55884, 56249, 56614, 56979, 57345, 57710, 58075,
            // 2060-2069
            58440, 58806, 59171, 59536, 59901, 60267, 60632, 60997, 61362, 61728,
            // 2070-2079
            62093, 62458, 62823, 63189, 63554, 63919, 64284, 64650, 65015, 65380,
            // 2080-2089
            65745, 66111, 66476, 66841, 67206, 67572, 67937, 68302, 68667, 69033,
            // 2090-2099
            69398, 69763, 70128, 70494, 70859, 71224, 71589, 71955, 72320, 72685,
            // 2100
            73050 };

    static private final boolean yearIsLeap[] = {
    		// 1900 is leap in agreement with Excel's bug
            // 1900 is out of valid date range anyway
            // 1900-1909
            true, false, false, false, true, false, false, false, true, false,
            // 1910-1919
            false, false, true, false, false, false, true, false, false, false,
            // 1920-1929
            true, false, false, false, true, false, false, false, true, false,
            // 1930-1939
            false, false, true, false, false, false, true, false, false, false,
            // 1940-1949
            true, false, false, false, true, false, false, false, true, false,
            // 1950-1959
            false, false, true, false, false, false, true, false, false, false,
            // 1960-1969
            true, false, false, false, true, false, false, false, true, false,
            // 1970-1979
            false, false, true, false, false, false, true, false, false, false,
            // 1980-1989
            true, false, false, false, true, false, false, false, true, false,
            // 1990-1999
            false, false, true, false, false, false, true, false, false, false,
            // 2000-2009
            true, false, false, false, true, false, false, false, true, false,
            // 2010-2019
            false, false, true, false, false, false, true, false, false, false,
            // 2020-2029
            true, false, false, false, true, false, false, false, true, false,
            // 2030-2039
            false, false, true, false, false, false, true, false, false, false,
            // 2040-2049
            true, false, false, false, true, false, false, false, true, false,
            // 2050-2059
            false, false, true, false, false, false, true, false, false, false,
            // 2060-2069
            true, false, false, false, true, false, false, false, true, false,
            // 2070-2079
            false, false, true, false, false, false, true, false, false, false,
            // 2080-2089
            true, false, false, false, true, false, false, false, true, false,
            // 2090-2099
            false, false, true, false, false, false, true, false, false, false,
            // 2100
            false };

    /**
     * This constructor is intended to be used by Date class itself.
     * 
     * 
     * @param value
     *            is a serial which corresponds to a Date
     */
    private DefaultDate(final int value) {
        this.value = value;
    }

    /**
     * Creates a new Date
     * 
     * @param day
     *            is the day of month
     * @param month
     *            is an <code>enum</code> with represents the month
     * @param year
     *            is the year
     */
    public DefaultDate(final int day, final Month month, final int year) {
        this(fromDMY(day, month.toInteger(), year));
    }

    /**
     * Creates a new Date
     * 
     * @param day
     *            is the day of month
     * @param month
     *            is the month
     * @param year
     *            is the year
     */
    public DefaultDate(final int day, final int month, final int year) {
        this(fromDMY(day, month, year));
    }

    /**
     * Create Date instance form the given date
     * 
     * @param date
     */
    public DefaultDate(final DefaultDate date) {
        this(date.value);
    }

    /**
     * This method is intended to calculate the integer value of a (day, month,
     * year)
     * 
     * @param d
     *            is the day as a number
     * @param m
     *            is the month as a number
     * @param y
     *            is the year as a number
     * @return
     */
    static private final int fromDMY(final int d, final int m, final int y) {
        if (!(y > 1900 && y < 2100))
            throw new IllegalArgumentException("year " + y + " out of bound. It must be in [1901,2099]");
        if (!(m > 0 && m < 13))
            throw new IllegalArgumentException("month " + m + " outside January-December range [1,12]");

        final boolean leap = isLeap(y);
        final int len = getMonthLength(m, leap), offset = getMonthOffset(m, leap);
        if (!(d > 0 && d <= len))
            throw new ArithmeticException("day outside month (" + m + ") day-range [1," + len + "]");
        final int result = d + offset + getYearOffset(y);
        return result;
    }

    @Override
    public final int hashCode() {
    	return ((Object)this).hashCode();
    }
    
    @Override
    public final boolean equals(final Object anObject) {
        if (anObject==null) return false;
        if (!(anObject instanceof DefaultDate)) return false;
        return eq((Date)anObject);
    }

    public final int getMonth() {
        final int d = getDayOfYear(); // dayOfYear is 1 based
        Integer m = d / 30 + 1;
        final boolean leap = isLeap(getYear());
        while (d <= getMonthOffset(m, leap))
            --m;
        while (d > getMonthOffset(m + 1, leap))
            ++m;
        return m;
    }

    public final Month getMonthEnum() {
        return Month.valueOf(getMonth());
    }

    public final int getYear() {
        int y = (value / 365) + 1900;
        if (value <= getYearOffset(y))
            --y;
        return y;
    }

    public final DefaultDate increment() {
        value++;
        notifyObservers();
        return this;
    }

    public final Date decrement() {
        value--;
        notifyObservers();
        return this;
    }

    public final DefaultDate increment(final int days) {
        value += days;
        notifyObservers();
        return this;
    }

    public final Date decrement(final int days) {
        value -= days;
        notifyObservers();
        return this;
    }

    public final Date adjust(final Period p) {
        value = getAdvancedDateValue(this, p.getLength(), p.getUnits());
        notifyObservers();
        return this;
    }

    /**
     * Returns a new Date which represents the current date of the system
     * 
     * @return a new Date object which represents the current Date
     */
    static public final Date getTodaysDate() {
        final java.util.Calendar cal = java.util.Calendar.getInstance();
        final int d = cal.get(java.util.Calendar.DAY_OF_MONTH);
        final int m = cal.get(java.util.Calendar.MONTH);
        final int y = cal.get(java.util.Calendar.YEAR);
        return new DefaultDate(d, m + 1, y);
    }

    /**
     * Returns the minimum Date allowed
     * 
     * @return the minimum Date allowed
     */
    public static final Date getMinDate() {
        return new DefaultDate(MinimumSerialNumber);
    }

    /**
     * Returns the maximum Date allowed
     * 
     * @return the maximum Date allowed
     */
    public static final Date getMaxDate() {
        return new DefaultDate(MaximumSerialNumber);
    }

    public final boolean isLeap() {
        return isLeap(this.getYear());
    }

    /**
     * Tests if a certain year is a leap year
     * 
     * @param y
     *            is the year
     * @return <code>true</code> if a leap year; <code>false</code>
     *         otherwise
     */
    public static final boolean isLeap(final int y) {
        return yearIsLeap[y - 1900];
    }

    public final Date getNextWeekday(final Weekday dayOfWeek) {
        final int wd = this.getWeekday().toInteger();
        final int dow = dayOfWeek.toInteger();
        return this.increment((wd > dow ? 7 : 0) - wd + dow);
    }
    
    
    /**
     * Returns a new Date which is the n-th week day of a month/year represented
     * by current date
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
    public final Date getNthWeekday(final int nth, final Weekday dayOfWeek) {
        return getNthWeekday(nth, dayOfWeek, this.getMonth(), this.getYear());
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
    public static final Date getNthWeekday(final int nth, final Weekday dayOfWeek, final Month month, final int year) {
        return getNthWeekday(nth, dayOfWeek, month.toInteger(), year);
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
    public static final DefaultDate getNthWeekday(final int nth, final Weekday dayOfWeek, final int month, final int year) {
        if (!(nth > 0))
            throw new IllegalArgumentException("zeroth day of week in a given (month, year) is undefined");
        if (!(nth < 6))
            throw new IllegalArgumentException("no more than 5 weekday in a given (month, year)");
        final int m = month;
        final int y = year;
        final int dow = dayOfWeek.toInteger();
        final int first = new DefaultDate(1, m, y).getWeekday().toInteger();
        final int skip = nth - (dow >= first ? 1 : 0);
        return new DefaultDate(1 + dow - first + skip * 7, m, y);
    }

    /**
     * Returns the length of a certain month
     * 
     * @param m
     *            is the desired month, as a number
     * @param leapYear
     *            if <code>true</code> means a leap year
     * @return the length of a certain month
     */
    static private final int getMonthLength(final int m, final boolean leapYear) {
        return (leapYear ? monthLeapLength[m - 1] : monthLength[m - 1]);
    }

    /**
     * Returns the offset of a certain month
     * 
     * @param m
     *            is the desired month, as a number. If you specify 13, you will
     *            get the number of days of a year
     * @param leapYear
     *            if <code>true</code> means a leap year
     * @return the offset of a certain month or the length of an year
     * @see DefaultDate#yearOffset
     */
    static private final int getMonthOffset(final int m, final boolean leapYear) {
        return (leapYear ? monthLeapOffset[m - 1] : monthOffset[m - 1]);
    }

    /**
     * Returns the offset of a certain year
     * 
     * @param y
     *            is the desired year
     * @return the offset of a certain year
     */
    static private final int getYearOffset(final int y) {
        return yearOffset[y - 1900];
    }

    public final Weekday getWeekday() {
        final int w = value % 7;
        return Weekday.valueOf(w == 0 ? 7 : w);
    }

    public final int getDayOfMonth() {
        return getDayOfYear() - getMonthOffset(getMonth(), isLeap(getYear()));
    }

    public final int getDayOfYear() {
        return value - getYearOffset(getYear());
    }

    public final DefaultDate getEndOfMonth() {
        final int m = this.getMonth();
        final int y = this.getYear();
        return new DefaultDate(getMonthLength(m, isLeap(y)), m, y);
    }

    public final boolean isEndOfMonth() {
        return (this.getDayOfMonth() == getMonthLength(this.getMonth(), isLeap(this.getYear())));
    }

    public final boolean eq(final Date date) {
        return value == ((DefaultDate) date).value;
    }

    public final boolean lt(final Date date) {
        return value < ((DefaultDate) date).value;
    }

    public final boolean le(final Date date) {
        return value <= ((DefaultDate) date).value;
    }

    public final boolean gt(final Date date) {
        return value > ((DefaultDate) date).value;
    }

    public final boolean ge(final Date date) {
        return value >= ((DefaultDate) date).value;
    }
    

    /**
     * Returns the String representing this Date in a long format. This is the
     * same format as returned by getLongFormat method
     * 
     * @return the String representing this Date in a long format
     * @see DefaultDate#getLongFormat
     */
    public final String toString() {
        return getLongFormat();
    }

    public final String getLongFormat() {
        if (NULL_DATE.equals(this)) {
            return "null date";
        } else {
            final StringBuilder sb = new StringBuilder();
            final Formatter formatter = new Formatter(sb, Locale.US);
            formatter.format("%s %d, %d", this.getMonthEnum(), this.getDayOfMonth(), this.getYear());
            return sb.toString();
        }
    }

    public final String getShortFormat() {
        if (NULL_DATE.equals(this)) {
            return "null date";
        } else {
            final StringBuilder sb = new StringBuilder();
            final Formatter formatter = new Formatter(sb, Locale.US);
            formatter.format("%s %d, %d", this.getMonthEnum(), this.getDayOfMonth(), this.getYear());
            return sb.toString();
        }
    }

    public final String getISOFormat() {
        if (NULL_DATE.equals(this)) {
            return "null date";
        } else {
            final StringBuilder sb = new StringBuilder();
            final Formatter formatter = new Formatter(sb, Locale.US);
            formatter.format("%04d-%02d-%02d", this.getYear(), this.getMonth(), this.getDayOfMonth());
            return sb.toString();
        }
    }

    private final void checkSerialNumber(final int value) {
        if (!(value >= MinimumSerialNumber && value <= MaximumSerialNumber))
            throw new IllegalArgumentException("Date's serial number (" + value + ") outside allowed range ["
                    + MinimumSerialNumber + "-" + MaximumSerialNumber + "], i.e. [" + getMinDate() + "-" + getMaxDate()
                    + "]");
    }

    private final int getAdvancedDateValue(final DefaultDate date, final int n, final TimeUnit units) {
        switch (units) {
        case DAYS:
            return (n+date.value);
        case WEEKS:
            return (7 * n + date.value);
        case MONTHS: {
            int d = date.getDayOfMonth();
            int m = date.getMonth() + n;
            int y = date.getYear();
            while (m > 12) {
                m -= 12;
                y += 1;
            }
            while (m < 1) {
                m += 12;
                y -= 1;
            }

            if (!(y >= 1900 && y <= 2099))
                throw new IllegalArgumentException("year " + y + " out of bounds. It must be in [1901,2099]");

            final int length = getMonthLength(m, isLeap(y));
            if (d > length)
                d = length;

            return fromDMY(d, m, y);
        }
        case YEARS: {
            int d = date.getDayOfMonth();
            final int m = date.getMonth();
            final int y = date.getYear() + n;

            if (!(y >= 1900 && y <= 2099))
                throw new IllegalArgumentException("year " + y + " out of bounds. It must be in [1901,2099]");

            if (d == 29 && m == Month.FEBRUARY.toInteger() && !isLeap(y))
                d = 28;

            return fromDMY(d, m, y);
        }
        default:
            throw new IllegalArgumentException("undefined time units");
        }
    } 

   
    public final int getDayCount(final Date date) {
        return  ((DefaultDate) date).value - value;
    }

    public final Date getDateAfter(final Period p) {
       final int newDateValue = getAdvancedDateValue(this, p.getLength(), p.getUnits());
       return new DefaultDate(newDateValue);
    }

    public final Date getNextDay() {
        final int newDateValue = value + 1;
        return new DefaultDate(newDateValue);
    }

    public final Date getPreviousDay() {
        final int newDateValue = value - 1;
        return new DefaultDate(newDateValue);
    }

    public final boolean eq(final int day, final Month month, final int year) {
       return this.value == fromDMY(day,month.toInteger(),year);
    }

    public final boolean ge(final int day, final Month month, final int year) {
        return this.value >= fromDMY(day,month.toInteger(),year);
    }

    public final boolean gt(final int day, final Month month, final int year) {
       return this.value > fromDMY(day,month.toInteger(),year);
    }

    public final boolean le(final int day, final Month month, final int year) {
        return this.value <= fromDMY(day,month.toInteger(),year);
    }

    public final boolean lt(final int day, final Month month, final int year) {
        return this.value < fromDMY(day,month.toInteger(),year);
    }

    public final Date getDateAfter(final int n) {
       return new DefaultDate(value+n);
    }
    
    /**
     * @return an accessor object which provides controlled update access to this object.
     * 
     * @see Updatable
     */
    public final Updatable<Date> getUpdatable() {
        if (updatable==null) updatable = new UpdatableDate(this);
        return updatable;
    }
    
    
    //
    // inner classes
    //
    
    //XXX private final UpdatableDate updatable = new UpdatableDate(this);
    private UpdatableDate updatable = null;
    
    /**
     * This inner class provides controlled update access to a Date object.
     * 
     * @see Date
     * 
     * @author Richard Gomes
     */
    private final class UpdatableDate implements Updatable<Date> {
        
        private final DefaultDate target;
        
        public UpdatableDate(final DefaultDate date) {
            this.target = date;
        }
        
        public final void update(final Date source) {
            if (source==null) throw new NullPointerException();
            
            if (this.target==NULL_DATE) {
                throw new IllegalStateException("not updatable");
            }
            target.value = ((DefaultDate)source).value;
        }
        
    }
    
    public static final class JQLibDateUtil extends DateFactory {
        /**
         * Returns a instance that is the Maximum date that can be represented by
         * the Date implementation.
         * 
         * @return Maximum date represented by the implementation
         */
        public final Date getMaxDate(){
            return DefaultDate.getMaxDate();
        }
        
        /**
         * Returns a instance that is the Minimum date that can be represented by
         * the current Date implementation.
         * 
         * @return Minimum date represented by the implementation
         */
        public final Date getMinDate(){
            return DefaultDate.getMinDate();
        }
        
        /**
         * Returns todays date represented by the system
         * 
         * @return
         */
        public final Date getTodaysDate(){
            return DefaultDate.getTodaysDate();
        }
        
        /**
         * Returns a Date represented by parameters specified
         * 
         * @param day
         * @param month
         * @param year
         * @return
         */
        public final Date getDate(final int day, final Month month, final int year){
            return new DefaultDate(day, month, year);
        }
        
        /**
         * Returns a Date represented by parameters specified
         * 
         * @param day
         * @param month
         * @param year
         * @return
         */
        public final Date getDate(final int day, final int month, final int year){
            return new DefaultDate(day, month, year);
        }
        
        public final Date getNthWeekday(final int nth, final Weekday dayOfWeek, final Month month, final int year){
            return DefaultDate.getNthWeekday(nth, dayOfWeek, month, year);
        }
        
        /**
         * 
         * @param str
         * @return
         */
        public final Date parseISO(final String str){
            return DateParser.parseISO(str);
        }
        
        /**
         * 
         * @param str
         * @param fmt
         * @return
         */
        public final Date parse(final String str, final String fmt){
            return DateParser.parse(str, fmt);
        }
     
        public final boolean isLeap(final int year){
            return DefaultDate.isLeap(year);
        }
    }


    //
    // implements FunctionDate
    //

    public final int dateValue() /* @ReadOnly */{
        return value;
    }

}