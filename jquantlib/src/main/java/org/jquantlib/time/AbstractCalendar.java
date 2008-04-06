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

package org.jquantlib.time;

import org.jquantlib.util.Date;

import cern.colt.list.IntArrayList;



///**
// * Returns the holidays between two dates
// */ 
//static Date[] holidayList(final Calendar calendar,
//                          final Date from,
//                          final Date to,
//                          boolean includeWeekEnds = false);
//
//public Date adjust(final Date d,
//        BusinessDayConvention convention = Following) /* @ReadOnly */;
//
//
///** Advances the given date of the given number of business days and
//returns the result.
//*/
//Date advance(final Date d,
//         final int n,
//         final TimeUnit unit,
//         final BusinessDayConvention convention = Following,
//         final boolean endOfMonth = false) /* @ReadOnly */;
//
//
//public Date advance(
//		final Date date,
//		final Period& period,
//		final BusinessDayConvention convention = Following,
//		final boolean endOfMonth = false) /* @ReadOnly */;
//
//
//
//public long businessDaysBetween(final Date from,
//        final Date to,
//        boolean includeFirst = true,
//        boolean includeLast = false) /* @ReadOnly */;
//









public abstract class AbstractCalendar implements Calendar {

	protected IntArrayList addedHolidays;
	protected IntArrayList removedHolidays;

	public AbstractCalendar() {
		this.addedHolidays   = new IntArrayList();
		this.removedHolidays = new IntArrayList();
	}

	
	
//    /**
//     * Returns <code>true</code> if the date is a business day for the given market.
//     */
//    public boolean isBusinessDay(final Date d) {
//        if (addedHolidays.contains(d.getValue()))
//            return false;
//        if (removedHolidays.contains(d.getValue()))
//            return true;
//        return this.isCustomBusinessDay(d);
//    }
//
//    /**
//     * Returns <code>true</code> if the weekday is part of the
//     * weekend for the given market.
//     * 
//     * @param weekday is the week day of interest
//     * @return <code>true</code> if the weekday is part of the
//     * weekend for the given market; <code>false</code> otherwise
//     */
//    public boolean isWeekend(Weekday weekday) {
//        return isCustomWeekend(weekday);
//    }

    
    
    
    
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

    
	private void addHoliday(final Date d) {
        // if date was a genuine holiday previously removed, revert the change
        removedHolidays.remove(d.getValue());
        // if it's already a holiday, leave the calendar alone.
        // Otherwise, add it.
        if (isBusinessDay(d))
            addedHolidays.add(d.getValue());
    }

    private void removeHoliday(final Date d) {
        // if d was an artificially-added holiday, revert the change
        addedHolidays.remove(d.getValue());
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
    public long businessDaysBetween(final Date from, final Date to, boolean includeFirst, boolean includeLast) {
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

}
