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
package org.jquantlib.time.calendars;

import static org.jquantlib.time.Weekday.Monday;
import static org.jquantlib.time.Weekday.Tuesday;
import static org.jquantlib.util.Date.Month.April;
import static org.jquantlib.util.Date.Month.December;
import static org.jquantlib.util.Date.Month.February;
import static org.jquantlib.util.Date.Month.January;
import static org.jquantlib.util.Date.Month.July;
import static org.jquantlib.util.Date.Month.June;
import static org.jquantlib.util.Date.Month.March;
import static org.jquantlib.util.Date.Month.May;
import static org.jquantlib.util.Date.Month.November;
import static org.jquantlib.util.Date.Month.October;
import static org.jquantlib.util.Date.Month.September;

import org.jquantlib.time.Weekday;
import org.jquantlib.util.Date;
import org.jquantlib.util.Date.Month;

//! Japanese calendar
/**
 * ! Holidays:
 * <ul>
 * <li>Saturdays</li>
 * <li>Sundays</li>
 * <li>New Year's Day, January 1st</li>
 * <li>Bank Holiday, January 2nd</li>
 * <li>Bank Holiday, January 3rd</li>
 * <li>Coming of Age Day, 2nd Monday in January</li>
 * <li>National Foundation Day, February 11th</li>
 * <li>Vernal Equinox</li>
 * <li>Greenery Day, April 29th</li>
 * <li>Constitution Memorial Day, May 3rd</li>
 * <li>Holiday for a Nation, May 4th</li>
 * <li>Children's Day, May 5th</li>
 * <li>Marine Day, 3rd Monday in July</li>
 * <li>Respect for the Aged Day, 3rd Monday in September</li>
 * <li>Autumnal Equinox</li>
 * <li>Health and Sports Day, 2nd Monday in October</li>
 * <li>National Culture Day, November 3rd</li>
 * <li>Labor Thanksgiving Day, November 23rd</li>
 * <li>Emperor's Birthday, December 23rd</li>
 * <li>Bank Holiday, December 31st</li>
 * <li>a few one-shot holidays</li>
 * </ul>
 * Holidays falling on a Sunday are observed on the Monday following except for
 * the bank holidays associated with the new year.
 * 
 * @author Srinivas Hasti
 */
public class Japan extends DelegateCalendar {
	private static Japan JAPAN = new Japan();

	private Japan() {
		setDelegate(this);
	}

	public static Japan getCalendar() {
		return JAPAN;
	}

	@Override
	public boolean isBusinessDay(Date date) {
		Weekday w = date.getWeekday();
		int d = date.getDayOfMonth();
		Month m = date.getMonthEnum();
		int y = date.getYear();
		// equinox calculation : TODO: is double usage the right type ???
		final double exact_vernal_equinox_time = 20.69115;
		final double exact_autumnal_equinox_time = 23.09;
		final double diff_per_year = 0.242194;
		final double moving_amount = (y - 2000) * diff_per_year;
		Integer number_of_leap_years = (y - 2000) / 4 + (y - 2000) / 100
				- (y - 2000) / 400;
		int ve = // vernal equinox day
		(int) (exact_vernal_equinox_time + moving_amount - number_of_leap_years);
		int ae = // autumnal equinox day
		(int) (exact_autumnal_equinox_time + moving_amount - number_of_leap_years);
		// checks
		if (isWeekend(w)
				// New Year's Day
				|| (d == 1 && m == January)
				// Bank Holiday
				|| (d == 2 && m == January)
				// Bank Holiday
				|| (d == 3 && m == January)
				// Coming of Age Day (2nd Monday in January),
				// was January 15th until 2000
				|| (w == Monday && (d >= 8 && d <= 14) && m == January && y >= 2000)
				|| ((d == 15 || (d == 16 && w == Monday)) && m == January && y < 2000)
				// National Foundation Day
				|| ((d == 11 || (d == 12 && w == Monday)) && m == February)
				// Vernal Equinox
				|| ((d == ve || (d == ve + 1 && w == Monday)) && m == March)
				// Greenery Day
				|| ((d == 29 || (d == 30 && w == Monday)) && m == April)
				// Constitution Memorial Day
				|| (d == 3 && m == May)
				// Holiday for a Nation
				|| (d == 4 && m == May)
				// Children's Day
				|| ((d == 5 || (d == 6 && w == Monday)) && m == May)
				// Marine Day (3rd Monday in July),
				// was July 20th until 2003, not a holiday before 1996
				|| (w == Monday && (d >= 15 && d <= 21) && m == July && y >= 2003)
				|| ((d == 20 || (d == 21 && w == Monday)) && m == July
						&& y >= 1996 && y < 2003)
				// Respect for the Aged Day (3rd Monday in September),
				// was September 15th until 2003
				|| (w == Monday && (d >= 15 && d <= 21) && m == September && y >= 2003)
				|| ((d == 15 || (d == 16 && w == Monday)) && m == September && y < 2003)
				// If a single day falls between Respect for the Aged Day
				// and the Autumnal Equinox, it is holiday
				|| (w == Tuesday && d + 1 == ae && d >= 16 && d <= 22
						&& m == September && y >= 2003)
				// Autumnal Equinox
				|| ((d == ae || (d == ae + 1 && w == Monday)) && m == September)
				// Health and Sports Day (2nd Monday in October),
				// was October 10th until 2000
				|| (w == Monday && (d >= 8 && d <= 14) && m == October && y >= 2000)
				|| ((d == 10 || (d == 11 && w == Monday)) && m == October && y < 2000)
				// National Culture Day
				|| ((d == 3 || (d == 4 && w == Monday)) && m == November)
				// Labor Thanksgiving Day
				|| ((d == 23 || (d == 24 && w == Monday)) && m == November)
				// Emperor's Birthday
				|| ((d == 23 || (d == 24 && w == Monday)) && m == December && y >= 1989)
				// Bank Holiday
				|| (d == 31 && m == December)
				// one-shot holidays
				// Marriage of Prince Akihito
				|| (d == 10 && m == April && y == 1959)
				// Rites of Imperial Funeral
				|| (d == 24 && m == February && y == 1989)
				// Enthronement Ceremony
				|| (d == 12 && m == November && y == 1990)
				// Marriage of Prince Naruhito
				|| (d == 9 && m == June && y == 1993))
			return false;
		return true;
	}
}
