/*
 Copyright (C) 2007 Richard Gomes

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

import org.jquantlib.math.interpolation.Extrapolator;
import org.jquantlib.time.IMM;
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
 * @note This class extends Observable and is potentially harmful to
 * performance due to the large number of Date objects that can be observable
 * at the same time on some families of applications. A simple solution for this
 * problem could be the implementation of an interface which enables this object
 * to turn on and turn off the notification of its observers.
 * 
 * @see Observable
 * 
 * @author Richard Gomes
 */
// FIXME: potential performance issue
public class Date implements Observable { 
    
    public static final Date NULL_DATE = new Date();
        
	private /*@NonNegative*/ int value;
	
	public int getValue() /*@ReadOnly*/ {
		return value;
	}
	
	static private final int MinimumSerialNumber = 367;      // Jan 1st, 1901
    static private final int MaximumSerialNumber = 73050;    // Dec 31st, 2099
	static private final Date maximumSerialNumber = new Date(MaximumSerialNumber);
	static private final Date minimumSerialNumber = new Date(MinimumSerialNumber);

	static final int monthLength[] = {
        31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
    };
    
	static private final int monthLeapLength[] = {
        31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
    };
    
	static private final int monthOffset[] = {
        0,  31,  59,  90, 120, 151,   // Jan - Jun
      181, 212, 243, 273, 304, 334,   // Jun - Dec
      365     // used in dayOfMonth to bracket day
	};
	
	static private final int monthLeapOffset[] = {
	        0,  31,  60,  91, 121, 152,   // Jan - Jun
	      182, 213, 244, 274, 305, 335,   // Jun - Dec
	      366     // used in dayOfMonth to bracket day
	};
    
	// the list of all December 31st in the preceding year
    // e.g. for 1901 yearOffset[1] is 366, that is, December 31 1900
    static private final int yearOffset[] = {
        // 1900-1909
            0,  366,  731, 1096, 1461, 1827, 2192, 2557, 2922, 3288,
        // 1910-1919
         3653, 4018, 4383, 4749, 5114, 5479, 5844, 6210, 6575, 6940,
        // 1920-1929
         7305, 7671, 8036, 8401, 8766, 9132, 9497, 9862,10227,10593,
        // 1930-1939
        10958,11323,11688,12054,12419,12784,13149,13515,13880,14245,
        // 1940-1949
        14610,14976,15341,15706,16071,16437,16802,17167,17532,17898,
        // 1950-1959
        18263,18628,18993,19359,19724,20089,20454,20820,21185,21550,
        // 1960-1969
        21915,22281,22646,23011,23376,23742,24107,24472,24837,25203,
        // 1970-1979
        25568,25933,26298,26664,27029,27394,27759,28125,28490,28855,
        // 1980-1989
        29220,29586,29951,30316,30681,31047,31412,31777,32142,32508,
        // 1990-1999
        32873,33238,33603,33969,34334,34699,35064,35430,35795,36160,
        // 2000-2009
        36525,36891,37256,37621,37986,38352,38717,39082,39447,39813,
        // 2010-2019
        40178,40543,40908,41274,41639,42004,42369,42735,43100,43465,
        // 2020-2029
        43830,44196,44561,44926,45291,45657,46022,46387,46752,47118,
        // 2030-2039
        47483,47848,48213,48579,48944,49309,49674,50040,50405,50770,
        // 2040-2049
        51135,51501,51866,52231,52596,52962,53327,53692,54057,54423,
        // 2050-2059
        54788,55153,55518,55884,56249,56614,56979,57345,57710,58075,
        // 2060-2069
        58440,58806,59171,59536,59901,60267,60632,60997,61362,61728,
        // 2070-2079
        62093,62458,62823,63189,63554,63919,64284,64650,65015,65380,
        // 2080-2089
        65745,66111,66476,66841,67206,67572,67937,68302,68667,69033,
        // 2090-2099
        69398,69763,70128,70494,70859,71224,71589,71955,72320,72685,
        // 2100
        73050
    };
    
    static private final boolean yearIsLeap[] = {
        // 1900 is leap in agreement with Excel's bug
        // 1900 is out of valid date range anyway
        // 1900-1909
         true,false,false,false, true,false,false,false, true,false,
        // 1910-1919
        false,false, true,false,false,false, true,false,false,false,
        // 1920-1929
         true,false,false,false, true,false,false,false, true,false,
        // 1930-1939
        false,false, true,false,false,false, true,false,false,false,
        // 1940-1949
         true,false,false,false, true,false,false,false, true,false,
        // 1950-1959
        false,false, true,false,false,false, true,false,false,false,
        // 1960-1969
         true,false,false,false, true,false,false,false, true,false,
        // 1970-1979
        false,false, true,false,false,false, true,false,false,false,
        // 1980-1989
         true,false,false,false, true,false,false,false, true,false,
        // 1990-1999
        false,false, true,false,false,false, true,false,false,false,
        // 2000-2009
         true,false,false,false, true,false,false,false, true,false,
        // 2010-2019
        false,false, true,false,false,false, true,false,false,false,
        // 2020-2029
         true,false,false,false, true,false,false,false, true,false,
        // 2030-2039
        false,false, true,false,false,false, true,false,false,false,
        // 2040-2049
         true,false,false,false, true,false,false,false, true,false,
        // 2050-2059
        false,false, true,false,false,false, true,false,false,false,
        // 2060-2069
         true,false,false,false, true,false,false,false, true,false,
        // 2070-2079
        false,false, true,false,false,false, true,false,false,false,
        // 2080-2089
         true,false,false,false, true,false,false,false, true,false,
        // 2090-2099
        false,false, true,false,false,false, true,false,false,false,
        // 2100
        false
    };

    private static StringFormatter SHORT_FORMAT = new ShortFormat();
    private static StringFormatter LONG_FORMAT = new LongFormat();
    private static StringFormatter ISO_FORMAT = new ISOFormat();
    
    //
    // ==============================================================================================================
    //

    
	/**
     * Creates a null Date
     */
    public Date() {
    	this(0);
    }

    public Date(int value) {
    	this.value = value;
    }

    /**
     * Creates a new Date
     * @param day is the day of month
     * @param month is an <code>enum</code> with represents the month
     * @param year is the year
     */
    public Date(int day, Date.Month month, int year) {
    	this(fromDMY(day, month.toInteger(), year));
    }
    
    /**
     * Creates a new Date
     * @param day is the day of month
     * @param month is the month
     * @param year is the year
     */
    public Date(int day, int month, int year) {
    	this(fromDMY(day, month, year));
    }
    
    /**
     * This method is intended to calculate the integer value of a (day, month, year)
     * 
     * @param d is the day as a number
     * @param m is the month as a number
     * @param y is the year as a number
     * @return
     */
    static private int fromDMY(int d, int m, int y) {
    	if (! (y > 1900 && y < 2100) ) throw new IllegalArgumentException("year "+y+" out of bound. It must be in [1901,2099]");
        if (! (m > 0 && m < 13) ) throw new IllegalArgumentException("month "+m+" outside January-December range [1,12]");

        boolean leap = isLeap(y);
        int len = getMonthLength(m,leap), offset = getMonthOffset(m,leap);
        if (! (d > 0 && d <= len) ) throw new ArithmeticException("day outside month ("+m+") day-range [1,"+len+"]");
        int result = d + offset + getYearOffset(y);
        return result;
    }
    


    /**
     * 
     * @return a number which represents a month [1..12]
     */
    public final int getMonth() {
        int d = getDayOfYear(); // dayOfYear is 1 based
        Integer m = d/30 + 1;
        boolean leap = isLeap(getYear());
        while (d <= getMonthOffset(m, leap))
            --m;
        while (d > getMonthOffset(m+1, leap))
            ++m;
        return m;
    }

    /**
     * @see Date.Month
     * @return the month as an <code>enum</code>
     */
    public final Month getMonthEnum() {
        return Month.valueOf(getMonth());
    }

    /**
     * 
     * @return the year
     */
    public final int getYear() {
        int y = (value / 365)+1900;
        // yearOffset(y) is December 31st of the preceding year
        if (value <= getYearOffset(y))
            --y;
        return y;
    }

    /**
     * Increments the date
     * 
     * @return <code>this</code> Date incremented
     */
    public Date inc() {
    	value++;
        notifyObservers();
        return this;
    }

    /**
     * Decrements the date
     * 
     * @return <code>this</code> Date decremented
     */
    public Date dec() {
    	value--;
        notifyObservers();
        return this;
    }

    /**
     * Increments the date by a given number of days
     * 
     * @param days is the quantity of days
     * @return <code>this</code> Date incremented by a given number of days
     */
    public Date inc(final int days) {
    	value += days;
        notifyObservers();
        return this;
    }

    /**
     * Decrements the date by a given number of days
     * 
     * @param days is the quantity of days
     * @return <code>this</code> Date decremented by a given number of days
     */
    public Date dec(final int days) {
    	value -= days;
        notifyObservers();
        return this;
    }

    /**
     * Increments the date by a given <code>Period</code>
     * @param p is the Period
     * @return <code>this</code> Date incremented by a given Period
     */
    public Date inc(final Period p) {
        value += getAdvancedDate(this, p.getLength(), p.getUnits()).value;
        notifyObservers();
        return this;
    }

    /**
     * Decrements the date by a given <code>Period</code>
     * @param p is the Period
     * @return <code>this</code> Date decremented by a given Period
     */
    public Date dec(final Period p) {
        value = getAdvancedDate(this, -p.getLength(), p.getUnits()).value;
        notifyObservers();
        return this;
    }

    /**
     * Returns a new Date which represents the current date
     * 
     * @return a new Date object which represents the current Date
     */
    static public Date getTodaysDate() {
    	java.util.Calendar cal = java.util.Calendar.getInstance();
    	int d = cal.get(java.util.Calendar.DAY_OF_MONTH);
    	int m = cal.get(java.util.Calendar.MONTH);
    	int y = cal.get(java.util.Calendar.YEAR);
        return new Date(d, m+1, y);
    }

    /**
     * Returns the minimum Date allowed
     * @return the minimum Date allowed
     */
    static public final Date getMinDate() {
    	return minimumSerialNumber;
    }

    /**
     * Returns the maximum Date allowed
     * @return the maximum Date allowed
     */
    static public final Date getMaxDate() {
    	return maximumSerialNumber;
    }

    /**
     * Tests if this Date belongs to a leap year
     * 
     * @return <code>true</code> if a leap year; <code>false</code> otherwise
     */
    public boolean isLeap() {
    	return Date.isLeap(this.getYear());
    }
    
    /**
     * Tests if a certain year is a leap year
     * 
     * @param y is the year
     * @return <code>true</code> if a leap year; <code>false</code> otherwise
     */
    static public boolean isLeap(int y) {
        return yearIsLeap[y-1900];
    }

    /**
     * Returns a new Date which contains the next week day matching a parameter
     * 
     * @param dayOfWeek is the desired day of week
     * @return a new Date object which contains the next week day matching a parameter
     */
    public Date getNextWeekday(final Weekday dayOfWeek) {
        int wd = this.getWeekday().toInteger();
        int dow = dayOfWeek.toInteger();
        return this.add( (wd>dow ? 7 : 0) - wd + dow );
    }

    /**
     * Returns a new Date which is the n-th week day of a certain month/year
     * 
     * @param nth is the desired week
     * @param dayOfWeek is the desired week day
     * @param month is the desired month
     * @param year is the desired year
     * @return a new Date which is the n-th week day of a certain month/year
     */
    static public Date getNthWeekday(int nth, Weekday dayOfWeek, Month month, int year) {
    	return getNthWeekday(nth, dayOfWeek, month.toInteger(), year);
    }
    
    /**
     * Returns a new Date which is the n-th week day of a certain month/year
     * 
     * @param nth is the desired week
     * @param dayOfWeek is the desired week day
     * @param month is the desired month
     * @param year is the desired year
     * @return a new Date which is the n-th week day of a certain month/year
     */
    static public Date getNthWeekday(int nth, Weekday dayOfWeek, int month, int year) {
        if (! (nth>0) ) throw new IllegalArgumentException("zeroth day of week in a given (month, year) is undefined");
        if (! (nth<6) ) throw new IllegalArgumentException("no more than 5 weekday in a given (month, year)");
        int m = month;
        int y = year;
        int dow = dayOfWeek.toInteger();
        int first = new Date(1, m, y).getWeekday().toInteger();
        int skip = nth - (dow>=first ? 1 : 0);
        return new Date(1 + dow-first + skip*7, m, y);
    }

    /**
     * Returns the length of a certain month
     * 
     * @param m is the desired month, as a number
     * @param leapYear if <code>true</code> means a leap year
     * @return the length of a certain month
     */
    static private int getMonthLength(final int m, boolean leapYear) {
        return (leapYear? monthLeapLength[m-1] : monthLength[m-1]);
    }

    /**
     * Returns the offset of a certain month
     * 
     * @param m is the desired month, as a number. If you specify 13, you will get the number of days of a year
     * @param leapYear if <code>true</code> means a leap year
     * @return the offset of a certain month or the length of an year
     * @see Date#yearOffset
     */
    static private int getMonthOffset(final int m, boolean leapYear) {
        return (leapYear? monthLeapOffset[m-1] : monthOffset[m-1]);
    }

    /**
     * Returns the offset of a certain year
     * @param y is the desired year
     * @return the offset of a certain year
     */
    static private int getYearOffset(final int y) {
        return yearOffset[y-1900];
    }

    /**
     * @return the week day of this Date as a <code>enum</code>
     */
    public final Weekday getWeekday() {
        int w = value % 7;
        return Weekday.valueOf(w == 0 ? 7 : w);
    }

    /**
     * 
     * @return the day of month
     */
    public final int getDayOfMonth() {
        return getDayOfYear() - getMonthOffset(getMonth(),isLeap(getYear()));
    }

    /**
     * 
     * @return the day of year
     */
    public final int getDayOfYear() {
        return value - getYearOffset(getYear());
    }

//    /**
//     * Returns a new Date object by adding a another Date
//     * @param date is another Date
//     * @return a new Date object by adding a another Date
//     */
//    public final Date add(final Date date) {
//    	return new Date(value+date.value);
//    }
    
    /**
     * Returns a new Date object by subtracting a another Date
     * @param date is another Date
     * @return a new Date object by subtracting a another Date
     */
    public final int subtract(final Date date) {
    	return value - date.value;
    }
    
    /**
     * Returns a new Date object by adding a given number of days
     * @param days is the number of days to be added
     * @return a new Date object by adding a given number of days
     */
    public final Date add(int days) {
        return new Date(value + days);
    }

    /**
     * Returns a new Date object by subtracting a given number of days
     * @param days is the number of days to be subtracted
     * @return a new Date object by subtracting a given number of days
     */
    public final Date subtract(int days) {
        return new Date(value - days);
    }

    /**
     * Returns a new Date object by adding a given period
     * @param p is the period to be added
     * @return a new Date object by adding a given period
     */
    public final Date add(final Period p) {
        return getAdvancedDate(this, p.getLength(), p.getUnits());
    }

    /**
     * Returns a new Date object by subtracting a given period
     * @param p is the period to be subtracted
     * @return a new Date object by subtracting a given period
     */
    public final Date subtract(final Period p) {
        return getAdvancedDate(this, -p.getLength(), p.getUnits());
    }

    /**
     * @return a new Date which represents the last day of this month
     */
    public Date getEndOfMonth() {
        int m = this.getMonth();
        int y = this.getYear();
        return new Date(getMonthLength(m, isLeap(y)), m, y);
    }

    /**
     * Tells if this date is at the end of month
     * @return <code>true</code> if this Date is at the end of month; <code>false</code> otherwise
     */
    public boolean isEndOfMonth() {
       return (this.getDayOfMonth() == getMonthLength(this.getMonth(), isLeap(this.getYear())));
    }

    /**
     * Compares if <code>this</code> Date is equal to a given Date
     * 
     * @param date is the date to be compared against <code>this</code> Date
     * @return f <code>this</code> Date is equal to a given Date
     */
    public boolean eq(final Date date) {
        return value == date.value;
    }

    /**
     * Compares if <code>this</code> Date is not equal to a given Date
     * 
     * @param date is the date to be compared against <code>this</code> Date
     * @return f <code>this</code> Date is not equal to a given Date
     */
    public boolean neq(final Date date) {
        return value != date.value;
    }

    /**
     * Compares if <code>this</code> Date is less than to a given Date
     * 
     * @param date is the date to be compared against <code>this</code> Date
     * @return f <code>this</code> Date is less than to a given Date
     */
    public boolean lt(final Date date) {
        return value < date.value;
    }

    /**
     * Compares if <code>this</code> Date is less than or equal to a given Date
     * 
     * @param date is the date to be compared against <code>this</code> Date
     * @return f <code>this</code> Date is less than or equal to a given Date
     */
    public boolean le(final Date date) {
        return value <= date.value;
    }

    /**
     * Compares if <code>this</code> Date is greater than to a given Date
     * 
     * @param date is the date to be compared against <code>this</code> Date
     * @return f <code>this</code> Date is greater than to a given Date
     */
    public boolean gt(final Date date) {
        return value > date.value;
    }

    /**
     * Compares if <code>this</code> Date is greater than or equal to a given Date
     * 
     * @param date is the date to be compared against <code>this</code> Date
     * @return f <code>this</code> Date is greater than or equal to a given Date
     */
    public boolean ge(final Date date) {
        return value >= date.value;
    }


	/**
	 * Returns the String representing this Date in a long format. This is the same format as returned by getLongFormat method
	 * 
	 * @return the String representing this Date in a long format
	 * @see Date#getLongFormat
	 */
	public String toString() {
		return getLongFormat();
	}
	
	/**
	 * Returns the String representing this Date in a long format. This is the same format as returned by toString method
	 * 
	 * @return the String representing this Date in a long format
	 * @see Date#toString
	 */
	public String getLongFormat() {
		return LONG_FORMAT.toString(this);
	}
	
	/**
	 * Returns the String representing this Date in a short format
	 * 
	 * @return the String representing this Date in a short format
	 */
	public String getShortFormat() {
		return SHORT_FORMAT.toString(this);
	}
	
	/**
	 * Returns the String representing this Date in ISO format
	 * 
	 * @return the String representing this Date in ISO format
	 */
	public String getISOFormat() {
		return ISO_FORMAT.toString(this);
	}
	
    private void checkSerialNumber(int value) {
        if (! (value >= MinimumSerialNumber && value <= MaximumSerialNumber) )
        	new IllegalArgumentException(
                    "Date's serial number ("+value+") outside allowed range ["
                    +MinimumSerialNumber+"-"+MaximumSerialNumber+"], i.e. ["
                    +getMinDate()+"-"+getMaxDate()+"]" );
    }

    private Date getAdvancedDate(final Date date, final Integer n, final TimeUnit units) {
        switch (units) {
          case Days:
            return date.inc(n);
          case Weeks:
            return date.inc(7*n);
          case Months: {
            int d = date.getDayOfMonth();
            int m = date.getMonth()+n;
            int y = date.getYear();
            while (m > 12) {
                m -= 12;
                y += 1;
            }
            while (m < 1) {
                m += 12;
                y -= 1;
            }

            if ( !(y >= 1900 && y <= 2099) ) throw new IllegalArgumentException("year "+y+" out of bounds. It must be in [1901,2099]");

            int length = getMonthLength(m, isLeap(y));
            if (d > length)
                d = length;

            return new Date(d, m, y);
          }
          case Years: {
              int d = date.getDayOfMonth();
              int m = date.getMonth();
              int y = date.getYear()+n;

              if ( !(y >= 1900 && y <= 2099) ) throw new IllegalArgumentException("year "+y+" out of bounds. It must be in [1901,2099]");

              if (d == 29 && m == Month.February.toInteger() && !isLeap(y))
                  d = 28;

              return new Date(d,m,y);
          }
          default:
            throw new IllegalArgumentException("undefined time units");
        }
    }


    
//    /**
//     * Implements the {@link Comparable} interface
//     */
//    public int compareTo(Date o) {
//		return new Integer(value).compareTo(o.value);
//	}


    interface StringFormatter{
        String toString(Date date);
    }
	
    /**
     * Output dates in long format (Month ddth, yyyy)
     */ 
    private static class LongFormat implements StringFormatter{	
        public String toString(Date date) {
			if (NULL_DATE.equals(date)) {
				return "null date";
			} else {
				StringBuilder sb = new StringBuilder();
				Formatter formatter = new Formatter(sb, Locale.US);
				formatter.format("%s %d, %d", date.getMonthEnum(), date.getDayOfMonth(), date.getYear());
				return sb.toString();
			}
		}
	}

    /**
     * Output dates in short format (mm/dd/yyyy)
     */ 
	 private static class ShortFormat implements StringFormatter{
	    public String toString(Date date) {
		    if (NULL_DATE.equals(date)) {
				return "null date";
			} else {
				StringBuilder sb = new StringBuilder();
				Formatter formatter = new Formatter(sb, Locale.US);
				formatter.format("%02d/%02d/%04d", date.getDayOfMonth(), date.getMonth(), date.getYear());
				return sb.toString();
			}
		}
	}

    /**
     * Output dates in ISO format (yyyy-mm-dd)
     */
	private static class ISOFormat implements StringFormatter{		
		public String toString(Date date) {
			if (NULL_DATE.equals(date)) {
				return "null date";
			} else {
				StringBuilder sb = new StringBuilder();
				Formatter formatter = new Formatter(sb, Locale.US);
				formatter.format("%04d-%02d-%02d", date.getYear(), date.getMonth(), date.getDayOfMonth());
				return sb.toString();
			}
		}
	}


	/**
     * Month names
     */
    public enum Month {
    	January   (1),
    	February  (2),
        March     (3),
        April     (4),
        May       (5),
        June      (6),
        July      (7),
        August    (8),
        September (9),
        October   (10),
        November  (11),
        December  (12),
        Jan (1),
        Feb (2),
        Mar (3),
        Apr (4),
        Jun (6),
        Jul (7),
        Aug (8),
        Sep (9),
        Oct (10),
        Nov (11),
        Dec (12);
	
		private final int enumValue;
		
		private Month(int month) {
			this.enumValue = month;
		}
		
		/**
		 * Returns the ordinal number of this Month
		 * 
		 * @return the ordinal number of this Month
		 */
		public int toInteger() {
			return enumValue;
		}
		
		/**
		 * Returns a new Month given it's ordinal number
		 * 
		 * @param month is the ordinal number
		 * @return a new Month given it's ordinal number
		 */
		static public Month valueOf(int month) {
			switch (month) {
			case 1:
				return Month.Jan;
			case 2:
				return Month.Feb;
			case 3:
				return Month.Mar;
			case 4:
				return Month.Apr;
			case 5:
				return Month.May;
			case 6:
				return Month.Jun;
			case 7:
				return Month.Jul;
			case 8:
				return Month.Aug;
			case 9:
				return Month.Sep;
			case 10:
				return Month.Oct;
			case 11:
				return Month.Nov;
			case 12:
				return Month.Dec;
			default:
				throw new IllegalArgumentException();
			}
		}
		
	    /**
	     * Returns the IMM char for this Month
	     * 
	     * @return the IMM char for this Month
	     * @see IMM
	     */
		public char getImmChar() {
			switch (enumValue) {
			case 1:
				return 'F';
			case 2:
				return 'G';
			case 3:
				return 'H';
			case 4:
				return 'J';
			case 5:
				return 'K';
			case 6:
				return 'M';
			case 7:
				return 'N';
			case 8:
				return 'Q';
			case 9:
				return 'U';
			case 10:
				return 'V';
			case 11:
				return 'X';
			case 12:
				return 'Z';
			default:
				throw new IllegalArgumentException();
			}
		}

		/**
		 * Returns a new month given it's IMM code
		 * 
		 * @param immCode is the IMM code 
		 * @return a new month given it's IMM code
		 */
		static public Month valueOf(char immCode) {
			switch (immCode) {
			case 'F': return Month.Jan;
			case 'G': return Month.Feb;
			case 'H': return Month.Mar;
			case 'J': return Month.Apr;
			case 'K': return Month.May;
			case 'M': return Month.Jun;
			case 'N': return Month.Jul;
			case 'Q': return Month.Aug;
			case 'U': return Month.Sep;
			case 'V': return Month.Oct;
			case 'X': return Month.Nov;
			case 'Z': return Month.Dec;
			default:
				throw new IllegalArgumentException();
			}
		}
		
		public String toString() {
			switch (enumValue) {
			case 1:
				return "January";
			case 2:
				return "February";
			case 3:
				return "March";
			case 4:
				return "April";
			case 5:
				return "May";
			case 6:
				return "June";
			case 7:
				return "July";
			case 8:
				return "August";
			case 9:
				return "September";
			case 10:
				return "October";
			case 11:
				return "November";
			case 12:
				return "December";
			default:
				throw new IllegalArgumentException();
			}
		}

    }
    

	//
	// implements Observable interface
	//
	
	/**
	 * Implements multiple inheritance via delegate pattern to an inner class
	 * 
	 * @see Extrapolator
	 */
    private Observable delegatedObservable = new DefaultObservable(this);

	public void addObserver(Observer observer) {
		delegatedObservable.addObserver(observer);
	}

	public void deleteObserver(Observer observer) {
		delegatedObservable.deleteObserver(observer);
	}

	public void notifyObservers() {
		delegatedObservable.notifyObservers();
	}

	public void notifyObservers(Object arg) {
		delegatedObservable.notifyObservers(arg);
	}


	//
	// inner classes
	//
	
	/**
	 * @return an accessor object which provides controlled update access to this object.
	 * 
	 * @see Updatable
	 */
	public Updatable<Date> getUpdatable() {
		return new UpdatableDate(this);
	}
	
	
	/**
	 * This inner class provides controlled update access to a Date object.
	 * 
	 * @see Date
	 * 
	 * @author Richard Gomes
	 */
	private class UpdatableDate implements Updatable<Date> {
		
		private Date target = null;
		
		public UpdatableDate(Date date) {
			this.target = date;
		}
		
		public void update(Date source) {
			if (source==null) throw new NullPointerException();
			
			if (this.target==NULL_DATE) {
				throw new IllegalStateException("not updatable");
			}
			target.value = source.value;
		}
		
	}
	
	
	


}