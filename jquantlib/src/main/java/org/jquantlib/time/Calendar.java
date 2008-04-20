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

import org.jquantlib.util.Date;

/**
 * This class provides methods for determining whether a date is a business day
 * or a holiday for a given market, and for incrementing/decrementing a date of
 * a given number of business days.
 * 
 * <p>
 * The Bridge pattern is used to provide the base behavior of the calendar,
 * namely, to determine whether a date is a business day.
 * 
 * <p>
 * A calendar should be defined for specific exchange holiday schedule or for
 * general country holiday schedule. Legacy city holiday schedule calendars will
 * be moved to the exchange/country convention.
 */
public interface Calendar {
	
	
//XXX
//	/**
//	 * Returns whether or not the calendar is initialized
//	 */
//	public boolean empty() /* @ReadOnly */;

	/**
	 * Returns the name of the calendar.
	 * 
	 * @note This method is used for output and comparison between calendars. It
	 *       is <b>not</b> meant to be used for writing switch-on-type code.
	 * 
	 */
	public String getName() /* @ReadOnly */;

	/**
	 * Returns <code>true</code> if the date is a business day for the given
	 * market.
	 */
	public boolean isBusinessDay(final Date d) /* @ReadOnly */;

	/**
     * Returns <code>true</code> if the date is a holiday for the given market.
     * 
     * @param date is the Date to be tested
	 */
	public boolean isHoliday(final Date d) /* @ReadOnly */;

	/**
	 * Returns <code>true</code> if the weekday is part of the weekend for the
	 * given market.
	 */
	public boolean isWeekend(Weekday w) /* @ReadOnly */;

    /**
     * Returns <code>true</code> if the date is last business day for the
     * month in given market.
     */
	public boolean isEndOfMonth(final Date d) /* @ReadOnly */;

    /**
     * Last business day of the month to which the given date belongs
     * 
     * @param date
     * @return
     */
	public Date getEndOfMonth(final Date d) /* @ReadOnly */;


	
// FIXME: code review	
//	/**
//	 * Adds a date to the set of holidays for the given calendar.
//	 */
//	public void addHoliday(final Date d);
//
//	/**
//	 * Removes a date from the set of holidays for the given calendar.
//	 */
//	public void removeHoliday(final Date d);
//
//	/**
//	 * Adjusts a non-business day to the appropriate near business day with
//	 * respect to the given convention.
//	 */
//	public Date adjust(final Date d, final BusinessDayConvention convention) /* @ReadOnly */;

	
    /**
     * Adjusts a non-business day to the appropriate near business day
     * with respect to the given convention.
     */
    public Date adjust(final Date d) /* @ReadOnly */;
    
	/**
	 * Advances the given date of the given number of business days and returns
	 * the result.
	 */
	// FIXME: improve comments
	public Date advance(final Date d, int n, final TimeUnit unit);
	
    /**
     * Advances the given date as specified by the given period and
     * returns the result. Assumes that it's not the end of a month.
     */
    public Date advance(final Date d, final Period p, final BusinessDayConvention c) /* @ReadOnly */;
    
    /**
     * Advances the given date as specified by the given period and
     * returns the result. 
     */
	// FIXME: improve comments
	public Date advance(final Date d, final int n, final TimeUnit unit, final BusinessDayConvention convention, final boolean endOfMonth) /* @ReadOnly */;

	/**
	 * Advances the given date as specified by the given period and returns the
	 * result.
	 */
	// FIXME: improve comments
	public Date advance(final Date date, final Period period, final BusinessDayConvention convention, final boolean endOfMonth) /* @ReadOnly */;

    /**
     * Calculates the number of business days between two given
     * dates and returns the result.
     */
	public long businessDaysBetween(final Date from, final Date to, boolean includeFirst, boolean includeLast) /* @ReadOnly */;

}
