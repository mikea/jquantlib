/*
 Copyright (C) 2008 Anand Mani
 
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

package org.jquantlib.time.calendars;

import static org.jquantlib.time.Weekday.MONDAY;
import static org.jquantlib.time.Weekday.TUESDAY;
import static org.jquantlib.util.Month.APRIL;
import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.FEBRUARY;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.JUNE;
import static org.jquantlib.util.Month.OCTOBER;

import org.jquantlib.time.Weekday;
import org.jquantlib.time.WesternCalendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.Month;

/**
 * New Zealand calendar
 * <p>
 * Holidays:
 * <ul>
 * <li>Saturdays</li>
 * <li>Sundays</li>
 * <li>New Year's Day, January 1st (possibly moved to Monday or Tuesday)</li>
 * <li>Day after New Year's Day, January 2st (possibly moved to Monday or Tuesday)</li>
 * <li>Anniversary Day, Monday nearest January 22nd</li>
 * <li>Waitangi Day. February 6th</li>
 * <li>Good Friday</li>
 * <li>Easter Monday</li>
 * <li>ANZAC Day. April 25th</li>
 * <li>Queen's Birthday, first Monday in June</li>
 * <li>Labour Day, fourth Monday in October</li>
 * <li>Christmas, December 25th (possibly moved to Monday or Tuesday)</li>
 * <li>Boxing Day, December 26th (possibly moved to Monday or Tuesday)</li>
 * </ul>
 * 
 * @category calendars
 * 
 * @see <a href="http://www.nzx.com">New Zealand Stock Exchange</a>
 * 
 * @author Anand Mani
 */
public class NewZealand extends WesternCalendar {
	
	private static final NewZealand NEW_ZEALAND = new NewZealand();

	public static NewZealand getCalendar() {
		return NEW_ZEALAND;
	}

	private NewZealand() {
	}

	public String getName() {
		return "New Zealand";
	}

	public boolean isBusinessDay(final Date date /* @ReadOnly */) /* @ReadOnly */{
		final Weekday w = date.getWeekday();
		final int d = date.getDayOfMonth(), dd = date.getDayOfYear();
		final Month m = date.getMonthEnum();
		final int y = date.getYear();
		final int em = easterMonday(y);
		
		if (isWeekend(w)
				// New Year's Day (possibly moved to Monday or Tuesday)
				|| ((d == 1 || (d == 3 && (w == MONDAY || w == TUESDAY))) && m == JANUARY)
				// Day after New Year's Day (possibly moved to Mon or Tuesday)
				|| ((d == 2 || (d == 4 && (w == MONDAY || w == TUESDAY))) && m == JANUARY)

// Do not seem to be observed by NZX :: see http://bugs.jquantlib.org/view.php?id=72 				
//				// Anniversary Day, Monday nearest January 22nd
				|| ((d >= 19 && d <= 25) && w == MONDAY && m == JANUARY)

				// Waitangi Day. February 6th
				|| (d == 6 && m == FEBRUARY)
				// Good Friday
				|| (dd == em - 3)
				// Easter Monday
				|| (dd == em)
				// ANZAC Day. April 25th
				|| (d == 25 && m == APRIL)
				// Queen's Birthday, first Monday in June
				|| (d <= 7 && w == MONDAY && m == JUNE)
				// Labour Day, fourth Monday in October
				|| ((d >= 22 && d <= 28) && w == MONDAY && m == OCTOBER)
				// Christmas, December 25th (possibly Monday or Tuesday)
				|| ((d == 25 || (d == 27 && (w == MONDAY || w == TUESDAY))) && m == DECEMBER)
				// Boxing Day, December 26th (possibly Monday or Tuesday)
				|| ((d == 26 || (d == 28 && (w == MONDAY || w == TUESDAY))) && m == DECEMBER))
			return false;
		
		return true;
	}

}
