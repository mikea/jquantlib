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

import static org.jquantlib.time.Weekday.MONDAY;
import static org.jquantlib.time.Weekday.TUESDAY;
import static org.jquantlib.util.Month.APRIL;
import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.FEBRUARY;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.JULY;
import static org.jquantlib.util.Month.JUNE;
import static org.jquantlib.util.Month.MARCH;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.NOVEMBER;
import static org.jquantlib.util.Month.OCTOBER;
import static org.jquantlib.util.Month.SEPTEMBER;

import org.jquantlib.time.AbstractCalendar;
import org.jquantlib.time.Weekday;
import org.jquantlib.util.Date;
import org.jquantlib.util.Month;

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
public class Japan extends AbstractCalendar {
	private static Japan JAPAN = new Japan();

	private Japan() {
	}

	public static Japan getCalendar() {
		return JAPAN;
	}

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
				|| (d == 1 && m == JANUARY)
				// Bank Holiday
				|| (d == 2 && m == JANUARY)
				// Bank Holiday
				|| (d == 3 && m == JANUARY)
				// Coming of Age Day (2nd Monday in JANUARY),
				// was JANUARY 15th until 2000
				|| (w == MONDAY && (d >= 8 && d <= 14) && m == JANUARY && y >= 2000)
				|| ((d == 15 || (d == 16 && w == MONDAY)) && m == JANUARY && y < 2000)
				// National Foundation Day
				|| ((d == 11 || (d == 12 && w == MONDAY)) && m == FEBRUARY)
				// Vernal Equinox
				|| ((d == ve || (d == ve + 1 && w == MONDAY)) && m == MARCH)
				// Greenery Day
				|| ((d == 29 || (d == 30 && w == MONDAY)) && m == APRIL)
				// Constitution Memorial Day
				|| (d == 3 && m == MAY)
				// Holiday for a Nation
				|| (d == 4 && m == MAY)
				// Children's Day
				|| ((d == 5 || (d == 6 && w == MONDAY)) && m == MAY)
				// Marine Day (3rd Monday in JULY),
				// was JULY 20th until 2003, not a holiday before 1996
				|| (w == MONDAY && (d >= 15 && d <= 21) && m == JULY && y >= 2003)
				|| ((d == 20 || (d == 21 && w == MONDAY)) && m == JULY
						&& y >= 1996 && y < 2003)
				// Respect for the Aged Day (3rd Monday in SEPTEMBER),
				// was SEPTEMBER 15th until 2003
				|| (w == MONDAY && (d >= 15 && d <= 21) && m == SEPTEMBER && y >= 2003)
				|| ((d == 15 || (d == 16 && w == MONDAY)) && m == SEPTEMBER && y < 2003)
				// If a single day falls between Respect for the Aged Day
				// and the Autumnal Equinox, it is holiday
				|| (w == TUESDAY && d + 1 == ae && d >= 16 && d <= 22
						&& m == SEPTEMBER && y >= 2003)
				// Autumnal Equinox
				|| ((d == ae || (d == ae + 1 && w == MONDAY)) && m == SEPTEMBER)
				// Health and Sports Day (2nd Monday in OCTOBER),
				// was OCTOBER 10th until 2000
				|| (w == MONDAY && (d >= 8 && d <= 14) && m == OCTOBER && y >= 2000)
				|| ((d == 10 || (d == 11 && w == MONDAY)) && m == OCTOBER && y < 2000)
				// National Culture Day
				|| ((d == 3 || (d == 4 && w == MONDAY)) && m == NOVEMBER)
				// Labor Thanksgiving Day
				|| ((d == 23 || (d == 24 && w == MONDAY)) && m == NOVEMBER)
				// Emperor's Birthday
				|| ((d == 23 || (d == 24 && w == MONDAY)) && m == DECEMBER && y >= 1989)
				// Bank Holiday
				|| (d == 31 && m == DECEMBER)
				// one-shot holidays
				// Marriage of Prince Akihito
				|| (d == 10 && m == APRIL && y == 1959)
				// Rites of Imperial Funeral
				|| (d == 24 && m == FEBRUARY && y == 1989)
				// Enthronement Ceremony
				|| (d == 12 && m == NOVEMBER && y == 1990)
				// Marriage of Prince Naruhito
				|| (d == 9 && m == JUNE && y == 1993))
			return false;
		return true;
	}

    public String getName() {
       return "Japan";
    }

    public final boolean isWeekend(Weekday weekday) {
        return weekday == Weekday.SATURDAY || weekday == Weekday.SUNDAY;
    }
}
