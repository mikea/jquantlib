/*
 Copyright (C) 2008 Richard Gomes

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

import java.util.List; //FIXME: performance

import org.jquantlib.util.Date;

/**
 * This class provides methods for determining whether a date is a business day
 * or a holiday for a given market, and for incrementing/decrementing a date of
 * a given number of business days.
 * 
 * <p>
 * A calendar should be defined for specific exchange holiday schedule or for
 * general country holiday schedule. Legacy city holiday schedule calendars will
 * be moved to the exchange/country convention.
 */
public interface Calendar {

    /**
     * Returns the name of the calendar.
     * 
     * @note This method is used for output and comparison between calendars. It
     *       is <b>not</b> meant to be used for writing switch-on-type code.
     *
     * @return name of the calendar
     */
    public String getName();

    /**
     * Returns <code>true</code> if the date is a business day for the given
     * market.
     * 
     * @param date date to be checked
     * @return true for business day 
     *         false for non-business day
     */
    public boolean isBusinessDay(final Date d);

    /**
     * Returns <code>true</code> if the date is a holiday for the given
     * market.
     * 
     * @param date  Date to be tested
     * @return true for non-business day and false for business day
     */
    public boolean isHoliday(final Date d);

    /**
     * Returns <code>true</code> if the weekday is part of the weekend for the
     * given market.
     * 
     * @param w Day to be tested if its weekend
     * @return true for weekend
     *         false for non-weekend
     */
    public boolean isWeekend(Weekday w);

    /**
     * Returns <code>true</code> if the date is last business day for the
     * month in given market.
     * 
     * @param D Is Date end of Month
     * @return true if date represents end of month
     *         false if date doens't represent end of month
     */
    public boolean isEndOfMonth(final Date d);

    /**
     * Last business day of the month to which the given date belongs
     * 
     * @param date a day in a month
     * @return returns end of the month
     */
    public Date getEndOfMonth(final Date d);

    
    /**
    * Adds a date to the set of holidays for the given calendar.
    * 
    * @param d date to be added to the list of holidays
    */
    public void addHoliday(final Date d);
    
    /**
    * Removes a date from the set of holidays for the given calendar.
    * 
    * @param d date to be removed from the list of holidays
    */
    public void removeHoliday(final Date d);
    
    /**
     * Adjusts a non-business day to the appropriate near business day
     * with respect to the given convention.
     */
    public Date advance(final Date d);
    
    /**
     * Adjusts a non-business day to the appropriate near business day
     * with respect to the given convention.
     */
    public Date adjust(final Date d,final BusinessDayConvention c);
    
    /**
     * Adjusts a non-business day to the appropriate near business day and the 
     * default Convention (FOLLOWING).
     */
    public Date adjust(final Date d);
    
    /**
     * Adjusts the given date of the given number of business days and returns
     * the result.
     */
    public Date advance(final Date d, int n, final TimeUnit unit);

    /**
     * Adjusts the given date as specified by the given period and returns the
     * result. Assumes that it's not the end of a month.
     */
    public Date advance(final Date d, final Period p, final BusinessDayConvention c);

    /**
     * Adjusts the given date as specified by the given period and returns the
     * result.
     */
    public Date advance(final Date d, final int n, final TimeUnit unit, final BusinessDayConvention convention,
            final boolean endOfMonth);

    /**
     * Adjust the given date as specified by the given period and returns the
     * result.
     */
    public Date advance(final Date date, final Period period, final BusinessDayConvention convention,
            final boolean endOfMonth);

    /**
     * Calculates the number of business days between two given dates and
     * returns the result.
     */
    public long businessDaysBetween(final Date from, final Date to, boolean includeFirst, boolean includeLast);

    /**
     * Returns list of holidays in Date format between two given dates.
     * 
     * @param from
     * @param to
     * @param includeWeekEnds
     * @return
     */
    public List<Date> getHolidayList(final Date from, final Date to, boolean includeWeekEnds);

}
