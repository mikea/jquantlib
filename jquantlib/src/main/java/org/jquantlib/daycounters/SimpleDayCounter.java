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

package org.jquantlib.daycounters;

import org.jquantlib.util.Date;

/**
 * Simple day counter for reproducing theoretical calculations.
 * 
 * <p>
 * This day counter tries to ensure that whole-month distances are returned as a
 * simple fraction, i.e., 1 year = 1.0, 6 months = 0.5, 3 months = 0.25 and so
 * forth.
 * 
 * @note This day counter should be used together with NullCalendar, which
 *       ensures that dates at whole-month distances share the same day of
 *       month. It is <b>not</b> guaranteed to work with any other calendar.
 */
public class SimpleDayCounter extends AbstractDayCounter {

    private DayCounter fallback = null;

    public SimpleDayCounter() {
        this.fallback = new Thirty360();
    }

    public final String getName() {
        return "Simple";
    }

    public int getDayCount(final Date dateStart, final Date dateEnd) /* @ReadOnly */{
        return fallback.getDayCount(dateStart, dateEnd);
    }

    public/* @Time */double getYearFraction(final Date dateStart, final Date dateEnd, final Date refPeriodStart,
            final Date refPeriodEnd) /* @ReadOnly */{
        int dm1 = dateStart.getDayOfMonth();
        int dm2 = dateEnd.getDayOfMonth();
        int mm1 = dateStart.getMonth();
        int mm2 = dateEnd.getMonth();
        int yy1 = dateStart.getYear();
        int yy2 = dateEnd.getYear();

        if (dm1 == dm2 ||
        // e.g., Aug 30 -> Feb 28 ?
                (dm1 > dm2 && dateEnd.isEndOfMonth()) ||
                // e.g., Feb 28 -> Aug 30 ?
                (dm1 < dm2 && dateStart.isEndOfMonth())) {
            return (yy2 - yy1) + (mm2 - mm1) / 12.0;
        } else {
            return fallback.getYearFraction(dateStart, dateEnd);
        }
    }

    public/* @Time */double getYearFraction(final Date dateStart, final Date dateEnd) /* @ReadOnly */{
        return this.getYearFraction(dateStart, dateEnd, Date.NULL_DATE, Date.NULL_DATE);
    }

}
