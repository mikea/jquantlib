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

import static org.jquantlib.util.Month.AUGUST;
import static org.jquantlib.util.Month.DECEMBER;
import static org.jquantlib.util.Month.JANUARY;
import static org.jquantlib.util.Month.MAY;
import static org.jquantlib.util.Month.NOVEMBER;

import org.jquantlib.time.Calendar;
import org.jquantlib.time.Weekday;
import org.jquantlib.time.WesternCalendar;
import org.jquantlib.util.Date;
import org.jquantlib.util.Month;

/**
 * 
 * @author Anand Mani
 * @author Renjith Nair
 */

//! Polish calendar
/*! Holidays:
    <ul>
    <li>Saturdays</li>
    <li>Sundays</li>
    <li>Easter Monday</li>
    <li>Corpus Christi</li>
    <li>New Year's Day, January 1st</li>
    <li>May Day, May 1st</li>
    <li>Constitution Day, May 3rd</li>
    <li>Assumption of the Blessed Virgin Mary, August 15th</li>
    <li>All Saints Day, November 1st</li>
    <li>Independence Day, November 11th</li>
    <li>Christmas, December 25th</li>
    <li>2nd Day of Christmas, December 26th</li>
    </ul>

    \ingroup calendars
*/	
public class Poland extends DelegateCalendar {
	public static enum Market {
		WSE, //  Warsaw Stock Exchange
	};

	//Warsaw Stock Exchange Calendar
	private final static Poland WSE_CALENDAR = new Poland(
			Market.WSE);

	private Poland(Market market) {
		Calendar delegate;
		switch (market) {
		case WSE:
			delegate = new PolandWSECalendar();
			break;
		default:
			throw new IllegalArgumentException("unknown market");
		}
		setDelegate(delegate);
	}

	public static Poland getCalendar(Market market) {
		switch (market) {
		case WSE:
			return WSE_CALENDAR;
		default:
			throw new IllegalArgumentException("unknown market");
		}
	}
}

final class PolandWSECalendar extends WesternCalendar {

	public String getName() {
		return "Warsaw Stock Exchange";
	}
    
	public boolean isBusinessDay(final Date date /* @ReadOnly */) /* @ReadOnly */{
		Weekday w = date.getWeekday();
		int d = date.getDayOfMonth(), dd = date.getDayOfYear();
		Month m = date.getMonthEnum();
		int y = date.getYear();
		int em = easterMonday(y);
		if (isWeekend(w)
		// Easter Monday
				|| (dd == em)
				|| (dd == (em-3) && y == 2009)
				// Corpus Christi
				|| (dd == em + 59)
				// New Year's Day
				|| (d == 1 && m == JANUARY)
				|| (d == 2 && m == JANUARY && y == 2009)
				// May Day
				|| (d == 1 && m == MAY)
				// Constitution Day
				|| (d == 3 && m == MAY)
				// Assumption of the Blessed Virgin Mary
				|| (d == 15 && m == AUGUST)
				// All Saints Day
				|| (d == 1 && m == NOVEMBER)
				// Independence Day
				|| (d == 11 && m == NOVEMBER)
				// Christmas
				// updated the value from http://www.polishworld.com/wse/
				|| (d == 24 && m == DECEMBER && (y == 2008 || y == 2009))
				|| (d == 25 && m == DECEMBER)
				// 2nd Day of Christmas
				|| (d == 26 && m == DECEMBER))
			return false;
		return true;
	}

}
