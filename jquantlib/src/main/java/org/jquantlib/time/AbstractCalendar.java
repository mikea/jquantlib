/*
 Copyright (C) 2008 Richard Gomes
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

package org.jquantlib.time;

import java.util.ArrayList;
import java.util.List;

import org.jquantlib.QL;
import org.jquantlib.lang.exceptions.LibraryException;
import org.slf4j.helpers.MessageFormatter;


/**
 *
 * @author Srinivas Hasti
 *
 */
// TODO: needs comments and code review
public abstract class AbstractCalendar implements Calendar {

    protected static final String UNKNOWN_MARKET = "unknown market";
    protected static final String YEAR_OUT_OF_RANGE = "Year out of range";

    /**
     * To store artifially added holidays
     */
    private final ArrayList<Date> addedHolidays;

    /**
     * Constructor
     */
    protected AbstractCalendar() {
        this.addedHolidays = new ArrayList<Date>();
    }

    public void addHoliday(final Date d) {
        // if it's already a holiday, leave the calendar alone.
        // Otherwise, add it.
        if (isBusinessDay(d)) {
            addedHolidays.add(d);
        }
    }

    public void removeHoliday(final Date d) {
        // if d was an artificially-added holiday, revert the change
        addedHolidays.remove(d);
    }

    /**
     * Check whether holiday is artificially added
     *
     * @param date
     * @return true if its added holiday false if its not added
     */
    protected boolean isAddedHoliday(final Date date) {
        return addedHolidays.contains(date);
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
     * Advances the given date of the given number of business days and returns the result.
     * Uses the default BusinessDayConvention BusinessDayConvention.Following
     *
     * @return Date is date adjusted to next n-th business day
     */
    public final Date adjust(final Date d) {
        return adjust(d, BusinessDayConvention.FOLLOWING);
    }

    /**
     * Advances the given date of the given number of business days and returns the result. Returned reference is same as original
     * date reference passed in
     *
     * @return Date is date adjusted to next n-th business day
     */
    public final Date adjust(final Date d, final BusinessDayConvention c) {
        QL.require(d != null , "date not specified"); // QA:[RG]::verified // TODO: message

        if (c == BusinessDayConvention.UNADJUSTED) {
            return d;
        }

        final Date d1 = d;
        if (c == BusinessDayConvention.FOLLOWING || c == BusinessDayConvention.MODIFIED_FOLLOWING) {
            while (isHoliday(d1)) {
                d1.inc();
            }
            if (c == BusinessDayConvention.MODIFIED_FOLLOWING) {
                if (d1.month() != d.month()) {
                    return adjust(d, BusinessDayConvention.PRECEDING);
                }
            }
        } else if (c == BusinessDayConvention.PRECEDING || c == BusinessDayConvention.MODIFIED_PRECEDING) {
            while (isHoliday(d1)) {
                d1.dec();
            }
            if (c == BusinessDayConvention.MODIFIED_PRECEDING && d1.month() != d.month()) {
                return adjust(d, BusinessDayConvention.FOLLOWING);
            }
        } else {
            throw new LibraryException("unknown business-day convention"); // QA:[RG]::verified
        }
        return d1;
    }

    public final boolean isEndOfMonth(final Date date) {
        return (date.month() != advance(date.add(1)).month());
    }

    public final Date getEndOfMonth(final Date date) {
        return adjust(date.statics().endOfMonth(date), BusinessDayConvention.PRECEDING);
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
        return advance(d, 0, TimeUnit.DAYS);
    }

    public final Date advance(final Date d, final int units, final TimeUnit unit) {
        return advance(d, units, unit, BusinessDayConvention.FOLLOWING);
    }

    /**
     *
     * @param d
     * @param n
     * @param unit
     * @param c
     * @return
     */
    public final Date advance(final Date d, final int units, final TimeUnit unit, final BusinessDayConvention c) {
        return advance(d, units, unit, c, false);
    }

    public final Date advance(final Date date, final int units, final TimeUnit unit, final BusinessDayConvention c, final boolean endOfMonth) {
        QL.require(date != null , "date not specified"); // QA:[RG]::verified // TODO: message

        Date d1 = date.clone();
        int n = units;
        if (n == 0) {
            return adjust(d1, c);
        } else if (unit == TimeUnit.DAYS) {
            if (n > 0) {
                while (n > 0) {
                    d1.inc();
                    while (isHoliday(d1)) {
                        d1.inc();
                    }
                    n--;
                }
            } else {
                while (n < 0) {
                    d1.dec();
                    while (isHoliday(d1)) {
                        d1.dec();
                    }
                    n++;
                }
            }
            return d1;
        } else if (unit == TimeUnit.WEEKS) {
            d1 = d1.addAssign(new Period(n, unit));
            return adjust(d1, c);
        } else {
            d1 = d1.addAssign(new Period(n, unit));
            if (endOfMonth && (unit == TimeUnit.MONTHS || unit == TimeUnit.YEARS) && isEndOfMonth(d1)) {
                return getEndOfMonth(d1);
            }
            return adjust(d1, c);
        }
    }

    public Date advance(final Date d, final Period p, final BusinessDayConvention c) {
        return advance(d, p.length(), p.units(), c, false);
    }

    public Date advance(final Date d, final Period p, final BusinessDayConvention c, final boolean endOfMonth) {
        return advance(d, p.length(), p.units(), c, endOfMonth);
    }

    public long businessDaysBetween(final Date from, final Date to, final boolean includeFirst, final boolean includeLast) {
        int wd = 0;
        if (from.equals(to)) {
            if (isBusinessDay(from) && (includeFirst || includeLast)) {
                wd = 1;
            }
        } else {
            if (from.lt(to)) {
                Date d = from;
                while (d.le(to)) {
                    if (isBusinessDay(d)) {
                        wd++;
                    }
                    d = d.inc();
                }
            } else if (from.gt(to)) {
                Date d = to;
                while (d.le(from)) {
                    if (isBusinessDay(d)) {
                        wd++;
                    }
                    d = d.inc();
                }
            }

            if (isBusinessDay(from) && !includeFirst) {
                wd--;
            }
            if (isBusinessDay(to) && !includeLast) {
                wd--;
            }

            if (from.gt(to)) {
                wd = -wd;
            }
        }

        return wd;
    }

    public List<Date> getHolidayList(final Date from, final Date to, final boolean includeWeekEnds) {
        final List<Date> holidays = new ArrayList<Date>();
        if (from.ge(to)) {
            final String msg = MessageFormatter.format("{} should be after {}", new Object[] { to, from });
            throw new IllegalStateException(msg);
        }
        final Date startDate = from.clone();
        for (Date d = startDate; d.le(to); d = d.add(1)) {
            if (isHoliday(d) && (includeWeekEnds || !isWeekend(d.weekday()))) {
                holidays.add(d);
            }
        }

        return holidays;
    }

}
