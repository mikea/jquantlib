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

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.util.Date;

import cern.colt.list.ObjectArrayList;

public abstract class AbstractCalendar implements Calendar {

    /**
     * To store artifically added holidays
     */
    private ObjectArrayList addedHolidays;

    /**
     * Constructor
     */
    protected AbstractCalendar() {
        this.addedHolidays = new ObjectArrayList();
    }

    public void addHoliday(final Date d) {
        // if it's already a holiday, leave the calendar alone.
        // Otherwise, add it.
        if (isBusinessDay(d))
            addedHolidays.add(d);
    }

    public void removeHoliday(final Date d) {
        // if d was an artificially-added holiday, revert the change
        addedHolidays.delete(d, true);
    }

    /**
     * Check whether holiday is artificially added
     * 
     * @param date
     * @return true if its added holiday
     *         false if its not added
     */
    protected boolean isAddedHoliday(Date date) {
        return addedHolidays.contains(date, true);
    }

    /**
     * This base class only checks in list added by addHoliday(Date) call.
     * 
     * {@inheritDoc}  	
     */
    public boolean isBusinessDay(final Date date) {
        if (isAddedHoliday(date)) {
            return false;
        }
        return true;
    }

    /**
     * Advances the given date of the given number of business days and returns
     * the result.
     * 
     * @return Date is date adjusted to next n-th business day
     */
    private final Date adjust(final Date d, final BusinessDayConvention c) {
        if (d == null)
            throw new NullPointerException();

        if (c == BusinessDayConvention.UNADJUSTED)
            return d;

        Date d1 = d;
        if (c == BusinessDayConvention.FOLLOWING || c == BusinessDayConvention.MODIFIED_FOLLOWING) {
            while (isHoliday(d1))
                d1.increment();
            if (c == BusinessDayConvention.MODIFIED_FOLLOWING) {
                if (d1.getMonth() != d.getMonth()) {
                    return adjust(d, BusinessDayConvention.PRECEDING);
                }
            }
        } else if (c == BusinessDayConvention.PRECEDING || c == BusinessDayConvention.MODIFIED_PRECEDING) {
            while (isHoliday(d1))
                d1.decrement();
            if (c == BusinessDayConvention.MODIFIED_PRECEDING && d1.getMonth() != d.getMonth()) {
                return adjust(d, BusinessDayConvention.FOLLOWING);
            }
        } else {
            throw new IllegalArgumentException("unknown business-day convention");
        }
        return d1;
    }

    public final boolean isEndOfMonth(final Date date) {
        return (date.getMonth() != advance(date.getNextDay()).getMonth());
    }

    public final Date getEndOfMonth(final Date date) {
        return adjust(date.getEndOfMonth(), BusinessDayConvention.PRECEDING);
    }

    public final boolean isHoliday(final Date date) {
        return !isBusinessDay(date);
    }

    /**
     * 
     * @param d
     * @return
     */
    public final Date advance(final Date d) {
        return adjust(d, BusinessDayConvention.FOLLOWING);
    }

    public final Date advance(final Date d, int n, final TimeUnit unit) {
        return advance(d, n, unit, BusinessDayConvention.FOLLOWING);
    }

    /**
     * 
     * @param d
     * @param n
     * @param unit
     * @param c
     * @return
     */
    public final Date advance(final Date d, int n, final TimeUnit unit, final BusinessDayConvention c) {
        return advance(d, n, unit, c, false);
    }

    public final Date advance(final Date d, int n, final TimeUnit unit, final BusinessDayConvention c,
            boolean endOfMonth) {
        if (d == null)
            throw new NullPointerException();
        if (n == 0) {
            return adjust(d, c);
        } else if (unit == TimeUnit.DAYS) {
            Date d1 = d;
            if (n > 0) {
                while (n > 0) {
                    d1.increment();
                    while (isHoliday(d1))
                        d1.increment();
                    n--;
                }
            } else {
                while (n < 0) {
                    d1.decrement();
                    while (isHoliday(d1))
                        d1.decrement();
                    n++;
                }
            }
            return d1;
        } else if (unit == TimeUnit.WEEKS) {
            Date d1 = d.adjust(new Period(n, unit));
            return adjust(d1, c);
        } else {
            Date d1 = d.adjust(new Period(n, unit));
            if (endOfMonth && (unit == TimeUnit.MONTHS || unit == TimeUnit.YEARS) && isEndOfMonth(d)) {
                return getEndOfMonth(d1);
            }
            return adjust(d1, c);
        }
    }

    public Date advance(final Date d, final Period p, final BusinessDayConvention c) {
        return advance(d, p.getLength(), p.getUnits(), c, false);
    }

    public Date advance(final Date d, final Period p, final BusinessDayConvention c, boolean endOfMonth) {
        return advance(d, p.getLength(), p.getUnits(), c, endOfMonth);
    }

    public long businessDaysBetween(final Date from, final Date to, boolean includeFirst, boolean includeLast) {
        int wd = 0;
        if (from.equals(to)) {
            if (isBusinessDay(from) && (includeFirst || includeLast))
                wd = 1;
        } else {
            if (from.lt(to)) {
                Date d = from;
                while (d.le(to)) {
                    if (isBusinessDay(d))
                        wd++;
                    d = d.getDateAfter(1);
                }
            } else if (from.gt(to)) {
                Date d = to;
                while (d.le(from)) {
                    if (isBusinessDay(d))
                        wd++;
                    d = d.getDateAfter(1);
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

    public List<Date> getHolidayList(final Date from, final Date to, boolean includeWeekEnds) {
        List<Date> holidays = new ArrayList<Date>();
        if (from.ge(to))
            throw new IllegalStateException("To date  must be after from date ");
        Date startDate = from.getDateAfter(0);
        for (Date d = startDate; d.le(to); d = d.getDateAfter(1)) {
            if (isHoliday(d) && (includeWeekEnds || !isWeekend(d.getWeekday())))
                holidays.add(d);
        }

        return holidays;
    }

}
