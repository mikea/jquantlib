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
 Copyright (C) 2004 Jeff Yu

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

package org.jquantlib.time;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

import org.jquantlib.util.Date;

/**
 *  This class provides methods for determining whether a date is a
 * business day or a holiday for a given market, and for
 * incrementing/decrementing a date of a given number of business days.
 *
 * <p>The Bridge pattern is used to provide the base behavior of the
 * calendar, namely, to determine whether a date is a business day.
 *
 * <p>A calendar should be defined for specific exchange holiday schedule
 * or for general country holiday schedule. Legacy city holiday schedule
 * calendars will be moved to the exchange/country convention.
 *
 * @category datetime
 */
// TODO test: the methods for adding and removing holidays are tested by inspecting the calendar before and after their invocation.
// FIXME: This class should be called AbastractCalendar whilst DefaultCalendar should be called Calendar
public abstract class Calendar {

	protected IntList addedHolidays;
	protected IntList removedHolidays;

	protected abstract boolean isCustomBusinessDay(final Date date);
	protected abstract boolean isCustomWeekend(final Weekday weekday);


	public Calendar() {
		this.addedHolidays   = new IntArrayList();
		this.removedHolidays = new IntArrayList();
	}

    /**
     * Returns <code>true</code> if the date is a business day for the given market.
     */
    protected final boolean isBusinessDay(final Date d) {
        if (addedHolidays.contains(d))
            return false;
        if (removedHolidays.contains(d))
            return true;
        return this.isCustomBusinessDay(d);
    }

    /**
     * Returns <code>true</code> if the weekday is part of the
     * weekend for the given market.
     * 
     * @param weekday is the week day of interest
     * @return <code>true</code> if the weekday is part of the
     * weekend for the given market; <code>false</code> otherwise
     */
    public final boolean isWeekend(Weekday weekday) {
        return isCustomWeekend(weekday);
    }

    /**
     * Returns <code>true</code> if the date is last business day for the
     * month in given market.
     */
    // TODO: add comments
    public final boolean isEndOfMonth(final Date date) {
        return (date.getMonth() != adjust(date.inc()).getMonth());
    }

    /**
     * Last business day of the month to which the given date belongs
     * 
     * @param date
     * @return
     */
    // TODO: add comments
    public final Date getEndOfMonth(final Date date) {
        return adjust(date.getEndOfMonth(), BusinessDayConvention.Preceding);
    }

    /**
     * Returns <code>true</code> if the date is a holiday for the given market.
     * 
     * @param date is the Date to be tested
     */
    // TODO: add comments
    public final boolean isHoliday(final Date date) {
        return !isBusinessDay(date);
    }

    
	void addHoliday(final Date d) {
        // if date was a genuine holiday previously removed, revert the change
        removedHolidays.remove(d);
        // if it's already a holiday, leave the calendar alone.
        // Otherwise, add it.
        if (isBusinessDay(d))
            addedHolidays.add(d.getValue());
    }

    void removeHoliday(final Date d) {
        // if d was an artificially-added holiday, revert the change
        addedHolidays.remove(d);
        // if it's already a business day, leave the calendar alone.
        // Otherwise, add it.
        if (isBusinessDay(d))
            removedHolidays.add(d.getValue());
    }

    /**
     * Adjusts a non-business day to the appropriate near business day
     * with respect to the given convention.  
     */
    private final Date adjust(final Date d) {
    	return adjust(d, BusinessDayConvention.Following);
    }

    /** Advances the given date of the given number of business days and
     * returns the result.
     */
    private final Date adjust(final Date d, final BusinessDayConvention c) {
        if (d==null) throw new NullPointerException();
        
        if (c == BusinessDayConvention.Unadjusted)
            return d;

        Date d1 = d;
        if (c == BusinessDayConvention.Following || c == BusinessDayConvention.ModifiedFollowing) {
            while (isHoliday(d1))
                d1.inc();
            if (c == BusinessDayConvention.ModifiedFollowing) {
                if (d1.getMonth() != d.getMonth()) {
                    return adjust(d, BusinessDayConvention.Preceding);
                }
            }
        } else if (c == BusinessDayConvention.Preceding || c == BusinessDayConvention.ModifiedPreceding) {
            while (isHoliday(d1))
                d1.dec();
            if (c == BusinessDayConvention.ModifiedPreceding && d1.getMonth() != d.getMonth()) {
                return adjust(d, BusinessDayConvention.Following);
            }
        } else {
            throw new IllegalArgumentException("unknown business-day convention");
        }
        return d1;
    }

    public final Date advance(final Date d, int n, final TimeUnit unit) {
    	return advance(d, n, unit, BusinessDayConvention.Following);
    }
    
    public final Date advance(final Date d, int n, final TimeUnit unit, final BusinessDayConvention c) {
    	return advance(d, n, unit, c, false);
    }
    
    /**
     * Advances the given date as specified by the given period and
     * returns the result. 
     */
    public final Date advance(final Date d, int n, final TimeUnit unit, final BusinessDayConvention c, boolean endOfMonth) {
    	if (d==null) throw new NullPointerException();
        if (n == 0) {
            return adjust(d,c);
        } else if (unit == TimeUnit.Days) {
            Date d1 = d;
            if (n > 0) {
                while (n > 0) {
                    d1.inc();
                    while (isHoliday(d1))
                        d1.inc();
                    n--;
                }
            } else {
                while (n < 0) {
                    d1.dec();
                    while(isHoliday(d1))
                        d1.dec();
                    n++;
                }
            }
            return d1;
        } else if (unit == TimeUnit.Weeks) {
            Date d1 = d.add(new Period(n, unit));
            return adjust(d1,c);
        } else {
            Date d1 = d.add(new Period(n, unit));

            if (endOfMonth && (unit==TimeUnit.Months || unit==TimeUnit.Years)
                           && isEndOfMonth(d)) {
                return getEndOfMonth(d1);
            }

            return adjust(d1, c);
        }
    }

    /**
     * Advances the given date as specified by the given period and
     * returns the result. Assumes that it's not the end of a month.
     * 
     * @param d
     * @param p
     * @param c
     * @return
     * @see Calendar#advance(Date, Period, BusinessDayConvention, boolean)
     */
    // TODO: add comments
    public Date advance(final Date d, final Period p, final BusinessDayConvention c) {
        return advance(d, p.getLength(), p.getUnits(), c, false);
    }

    /**
     * Advances the given date as specified by the given period and
     * returns the result.
     * 
     * @param d
     * @param p
     * @param c
     * @param endOfMonth
     * @return
     */
    // TODO: add comments
    public Date advance(final Date d, final Period p, final BusinessDayConvention c, boolean endOfMonth) {
        return advance(d, p.getLength(), p.getUnits(), c, endOfMonth);
    }

    /**
     * Calculates the number of business days between two given
     * dates and returns the result.
     * 
     * @param from
     * @param to
     * @param includeFirst
     * @param includeLast
     * @return
     */
    // TODO: add comments
    public int businessDaysBetween(final Date from, final Date to, boolean includeFirst, boolean includeLast) {
        int wd = 0;
        if (from == to) {
            if (isBusinessDay(from) && (includeFirst || includeLast))
                wd = 1;
        } else {
            if (from.lt(to)) {
                Date d = from;
                while (d.le(to)) {
                    if (isBusinessDay(d))
                        wd++;
                    d.dec();
                }
            } else if (from.gt(to)) {
                Date d = to;
                while (d.le(from)) {
                    if (isBusinessDay(d))
                        wd++;
                    d.inc();
                }
            }

            if (isBusinessDay(from) && !includeFirst)
                wd--;
            if (isBusinessDay(to) && !includeLast)
                wd--;

            if (from.gt(to))
                wd = -wd;
        }

        return wd;
    }

	
    static final byte westernEasterMonday[] = { // Note: byte range is -128..+127
        107,  98,  90, 103,  95, 114, 106,  91, 111, 102,   // 1900-1909
         87, 107,  99,  83, 103,  95, 115,  99,  91, 111,   // 1910-1919
         96,  87, 107,  92, 112, 103,  95, 108, 100,  91,   // 1920-1929
        111,  96,  88, 107,  92, 112, 104,  88, 108, 100,   // 1930-1939
         85, 104,  96, 116, 101,  92, 112,  97,  89, 108,   // 1940-1949
        100,  85, 105,  96, 109, 101,  93, 112,  97,  89,   // 1950-1959
        109,  93, 113, 105,  90, 109, 101,  86, 106,  97,   // 1960-1969
         89, 102,  94, 113, 105,  90, 110, 101,  86, 106,   // 1970-1979
         98, 110, 102,  94, 114,  98,  90, 110,  95,  86,   // 1980-1989
        106,  91, 111, 102,  94, 107,  99,  90, 103,  95,   // 1990-1999
        115, 106,  91, 111, 103,  87, 107,  99,  84, 103,   // 2000-2009
         95, 115, 100,  91, 111,  96,  88, 107,  92, 112,   // 2010-2019
        104,  95, 108, 100,  92, 111,  96,  88, 108,  92,   // 2020-2029
        112, 104,  89, 108, 100,  85, 105,  96, 116, 101,   // 2030-2039
         93, 112,  97,  89, 109, 100,  85, 105,  97, 109,   // 2040-2049
        101,  93, 113,  97,  89, 109,  94, 113, 105,  90,   // 2050-2059
        110, 101,  86, 106,  98,  89, 102,  94, 114, 105,   // 2060-2069
         90, 110, 102,  86, 106,  98, 111, 102,  94, 107,   // 2070-2079
         99,  90, 110,  95,  87, 106,  91, 111, 103,  94,   // 2080-2089
        107,  99,  91, 103,  95, 115, 107,  91, 111, 103    // 2090-2099
    };

    /**
     * This class provides the means of determining the Easter
     * Monday for a given year, as well as specifying Saturdays
     * and Sundays as weekend days.
     */
    protected class WesternImpl {

        protected final boolean isWeekend(Weekday weekday) {
            return weekday == Weekday.Saturday || weekday == Weekday.Sunday;
        }
        
        /**
         * @return the offset of the Easter Monday relative to the
         * first day of the year
         */ 
        protected final int easterMonday(int year) {
        	return westernEasterMonday[year-1900];
        }
    }

    
    static final short ortodoxEasterMonday[] = { // Note: short range is -32,768 .. 32,767
	        105, 118, 110, 102, 121, 106, 126, 118, 102,   // 1901-1909
	   122, 114,  99, 118, 110,  95, 115, 106, 126, 111,   // 1910-1919
	   103, 122, 107,  99, 119, 110, 123, 115, 107, 126,   // 1920-1929
	   111, 103, 123, 107,  99, 119, 104, 123, 115, 100,   // 1930-1939
	   120, 111,  96, 116, 108, 127, 112, 104, 124, 115,   // 1940-1949
	   100, 120, 112,  96, 116, 108, 128, 112, 104, 124,   // 1950-1959
	   109, 100, 120, 105, 125, 116, 101, 121, 113, 104,   // 1960-1969
	   117, 109, 101, 120, 105, 125, 117, 101, 121, 113,   // 1970-1979
	    98, 117, 109, 129, 114, 105, 125, 110, 102, 121,   // 1980-1989
	   106,  98, 118, 109, 122, 114, 106, 118, 110, 102,   // 1990-1999
	   122, 106, 126, 118, 103, 122, 114,  99, 119, 110,   // 2000-2009
	    95, 115, 107, 126, 111, 103, 123, 107,  99, 119,   // 2010-2019
	   111, 123, 115, 107, 127, 111, 103, 123, 108,  99,   // 2020-2029
	   119, 104, 124, 115, 100, 120, 112,  96, 116, 108,   // 2030-2039
	   128, 112, 104, 124, 116, 100, 120, 112,  97, 116,   // 2040-2049
	   108, 128, 113, 104, 124, 109, 101, 120, 105, 125,   // 2050-2059
	   117, 101, 121, 113, 105, 117, 109, 101, 121, 105,   // 2060-2069
	   125, 110, 102, 121, 113,  98, 118, 109, 129, 114,   // 2070-2079
	   106, 125, 110, 102, 122, 106,  98, 118, 110, 122,   // 2080-2089
	   114,  99, 119, 110, 102, 115, 107, 126, 118, 103    // 2090-2099
	};

    /**
     * This class provides the means of determining the Orthodox
     * Easter Monday for a given year, as well as specifying
     * Saturdays and Sundays as weekend days.
     */
    protected class OrthodoxImpl {
        protected final boolean isWeekend(Weekday weekday) {
            return weekday == Weekday.Saturday || weekday == Weekday.Sunday;
        }
    	
        /**
         * @return the offset of the Easter Monday relative to the
         * first day of the year
         */ 
        protected final int easterMonday(int year) {
        	return ortodoxEasterMonday[year-1901];
        }
    }

}
